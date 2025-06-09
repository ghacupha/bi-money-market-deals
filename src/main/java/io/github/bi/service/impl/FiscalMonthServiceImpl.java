package io.github.bi.service.impl;

/*-
 * Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
 * Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.bi.domain.FiscalMonth;
import io.github.bi.repository.FiscalMonthRepository;
import io.github.bi.repository.search.FiscalMonthSearchRepository;
import io.github.bi.service.FiscalMonthService;
import io.github.bi.service.dto.FiscalMonthDTO;
import io.github.bi.service.mapper.FiscalMonthMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.FiscalMonth}.
 */
@Service
@Transactional
public class FiscalMonthServiceImpl implements FiscalMonthService {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalMonthServiceImpl.class);

    private final FiscalMonthRepository fiscalMonthRepository;

    private final FiscalMonthMapper fiscalMonthMapper;

    private final FiscalMonthSearchRepository fiscalMonthSearchRepository;

    public FiscalMonthServiceImpl(
        FiscalMonthRepository fiscalMonthRepository,
        FiscalMonthMapper fiscalMonthMapper,
        FiscalMonthSearchRepository fiscalMonthSearchRepository
    ) {
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.fiscalMonthMapper = fiscalMonthMapper;
        this.fiscalMonthSearchRepository = fiscalMonthSearchRepository;
    }

    @Override
    public FiscalMonthDTO save(FiscalMonthDTO fiscalMonthDTO) {
        LOG.debug("Request to save FiscalMonth : {}", fiscalMonthDTO);
        FiscalMonth fiscalMonth = fiscalMonthMapper.toEntity(fiscalMonthDTO);
        fiscalMonth = fiscalMonthRepository.save(fiscalMonth);
        fiscalMonthSearchRepository.index(fiscalMonth);
        return fiscalMonthMapper.toDto(fiscalMonth);
    }

    @Override
    public FiscalMonthDTO update(FiscalMonthDTO fiscalMonthDTO) {
        LOG.debug("Request to update FiscalMonth : {}", fiscalMonthDTO);
        FiscalMonth fiscalMonth = fiscalMonthMapper.toEntity(fiscalMonthDTO);
        fiscalMonth = fiscalMonthRepository.save(fiscalMonth);
        fiscalMonthSearchRepository.index(fiscalMonth);
        return fiscalMonthMapper.toDto(fiscalMonth);
    }

    @Override
    public Optional<FiscalMonthDTO> partialUpdate(FiscalMonthDTO fiscalMonthDTO) {
        LOG.debug("Request to partially update FiscalMonth : {}", fiscalMonthDTO);

        return fiscalMonthRepository
            .findById(fiscalMonthDTO.getId())
            .map(existingFiscalMonth -> {
                fiscalMonthMapper.partialUpdate(existingFiscalMonth, fiscalMonthDTO);

                return existingFiscalMonth;
            })
            .map(fiscalMonthRepository::save)
            .map(savedFiscalMonth -> {
                fiscalMonthSearchRepository.index(savedFiscalMonth);
                return savedFiscalMonth;
            })
            .map(fiscalMonthMapper::toDto);
    }

    public Page<FiscalMonthDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fiscalMonthRepository.findAllWithEagerRelationships(pageable).map(fiscalMonthMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiscalMonthDTO> findOne(Long id) {
        LOG.debug("Request to get FiscalMonth : {}", id);
        return fiscalMonthRepository.findOneWithEagerRelationships(id).map(fiscalMonthMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FiscalMonth : {}", id);
        fiscalMonthRepository.deleteById(id);
        fiscalMonthSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalMonthDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of FiscalMonths for query {}", query);
        return fiscalMonthSearchRepository.search(query, pageable).map(fiscalMonthMapper::toDto);
    }
}
