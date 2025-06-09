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

import io.github.bi.domain.FiscalYear;
import io.github.bi.repository.FiscalYearRepository;
import io.github.bi.repository.search.FiscalYearSearchRepository;
import io.github.bi.service.FiscalYearService;
import io.github.bi.service.dto.FiscalYearDTO;
import io.github.bi.service.mapper.FiscalYearMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.FiscalYear}.
 */
@Service
@Transactional
public class FiscalYearServiceImpl implements FiscalYearService {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalYearServiceImpl.class);

    private final FiscalYearRepository fiscalYearRepository;

    private final FiscalYearMapper fiscalYearMapper;

    private final FiscalYearSearchRepository fiscalYearSearchRepository;

    public FiscalYearServiceImpl(
        FiscalYearRepository fiscalYearRepository,
        FiscalYearMapper fiscalYearMapper,
        FiscalYearSearchRepository fiscalYearSearchRepository
    ) {
        this.fiscalYearRepository = fiscalYearRepository;
        this.fiscalYearMapper = fiscalYearMapper;
        this.fiscalYearSearchRepository = fiscalYearSearchRepository;
    }

    @Override
    public FiscalYearDTO save(FiscalYearDTO fiscalYearDTO) {
        LOG.debug("Request to save FiscalYear : {}", fiscalYearDTO);
        FiscalYear fiscalYear = fiscalYearMapper.toEntity(fiscalYearDTO);
        fiscalYear = fiscalYearRepository.save(fiscalYear);
        fiscalYearSearchRepository.index(fiscalYear);
        return fiscalYearMapper.toDto(fiscalYear);
    }

    @Override
    public FiscalYearDTO update(FiscalYearDTO fiscalYearDTO) {
        LOG.debug("Request to update FiscalYear : {}", fiscalYearDTO);
        FiscalYear fiscalYear = fiscalYearMapper.toEntity(fiscalYearDTO);
        fiscalYear = fiscalYearRepository.save(fiscalYear);
        fiscalYearSearchRepository.index(fiscalYear);
        return fiscalYearMapper.toDto(fiscalYear);
    }

    @Override
    public Optional<FiscalYearDTO> partialUpdate(FiscalYearDTO fiscalYearDTO) {
        LOG.debug("Request to partially update FiscalYear : {}", fiscalYearDTO);

        return fiscalYearRepository
            .findById(fiscalYearDTO.getId())
            .map(existingFiscalYear -> {
                fiscalYearMapper.partialUpdate(existingFiscalYear, fiscalYearDTO);

                return existingFiscalYear;
            })
            .map(fiscalYearRepository::save)
            .map(savedFiscalYear -> {
                fiscalYearSearchRepository.index(savedFiscalYear);
                return savedFiscalYear;
            })
            .map(fiscalYearMapper::toDto);
    }

    public Page<FiscalYearDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fiscalYearRepository.findAllWithEagerRelationships(pageable).map(fiscalYearMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiscalYearDTO> findOne(Long id) {
        LOG.debug("Request to get FiscalYear : {}", id);
        return fiscalYearRepository.findOneWithEagerRelationships(id).map(fiscalYearMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FiscalYear : {}", id);
        fiscalYearRepository.deleteById(id);
        fiscalYearSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalYearDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of FiscalYears for query {}", query);
        return fiscalYearSearchRepository.search(query, pageable).map(fiscalYearMapper::toDto);
    }
}
