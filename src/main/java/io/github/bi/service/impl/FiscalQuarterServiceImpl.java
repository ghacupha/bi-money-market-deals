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

import io.github.bi.domain.FiscalQuarter;
import io.github.bi.repository.FiscalQuarterRepository;
import io.github.bi.repository.search.FiscalQuarterSearchRepository;
import io.github.bi.service.FiscalQuarterService;
import io.github.bi.service.dto.FiscalQuarterDTO;
import io.github.bi.service.mapper.FiscalQuarterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.FiscalQuarter}.
 */
@Service
@Transactional
public class FiscalQuarterServiceImpl implements FiscalQuarterService {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalQuarterServiceImpl.class);

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterMapper fiscalQuarterMapper;

    private final FiscalQuarterSearchRepository fiscalQuarterSearchRepository;

    public FiscalQuarterServiceImpl(
        FiscalQuarterRepository fiscalQuarterRepository,
        FiscalQuarterMapper fiscalQuarterMapper,
        FiscalQuarterSearchRepository fiscalQuarterSearchRepository
    ) {
        this.fiscalQuarterRepository = fiscalQuarterRepository;
        this.fiscalQuarterMapper = fiscalQuarterMapper;
        this.fiscalQuarterSearchRepository = fiscalQuarterSearchRepository;
    }

    @Override
    public FiscalQuarterDTO save(FiscalQuarterDTO fiscalQuarterDTO) {
        LOG.debug("Request to save FiscalQuarter : {}", fiscalQuarterDTO);
        FiscalQuarter fiscalQuarter = fiscalQuarterMapper.toEntity(fiscalQuarterDTO);
        fiscalQuarter = fiscalQuarterRepository.save(fiscalQuarter);
        fiscalQuarterSearchRepository.index(fiscalQuarter);
        return fiscalQuarterMapper.toDto(fiscalQuarter);
    }

    @Override
    public FiscalQuarterDTO update(FiscalQuarterDTO fiscalQuarterDTO) {
        LOG.debug("Request to update FiscalQuarter : {}", fiscalQuarterDTO);
        FiscalQuarter fiscalQuarter = fiscalQuarterMapper.toEntity(fiscalQuarterDTO);
        fiscalQuarter = fiscalQuarterRepository.save(fiscalQuarter);
        fiscalQuarterSearchRepository.index(fiscalQuarter);
        return fiscalQuarterMapper.toDto(fiscalQuarter);
    }

    @Override
    public Optional<FiscalQuarterDTO> partialUpdate(FiscalQuarterDTO fiscalQuarterDTO) {
        LOG.debug("Request to partially update FiscalQuarter : {}", fiscalQuarterDTO);

        return fiscalQuarterRepository
            .findById(fiscalQuarterDTO.getId())
            .map(existingFiscalQuarter -> {
                fiscalQuarterMapper.partialUpdate(existingFiscalQuarter, fiscalQuarterDTO);

                return existingFiscalQuarter;
            })
            .map(fiscalQuarterRepository::save)
            .map(savedFiscalQuarter -> {
                fiscalQuarterSearchRepository.index(savedFiscalQuarter);
                return savedFiscalQuarter;
            })
            .map(fiscalQuarterMapper::toDto);
    }

    public Page<FiscalQuarterDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fiscalQuarterRepository.findAllWithEagerRelationships(pageable).map(fiscalQuarterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiscalQuarterDTO> findOne(Long id) {
        LOG.debug("Request to get FiscalQuarter : {}", id);
        return fiscalQuarterRepository.findOneWithEagerRelationships(id).map(fiscalQuarterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FiscalQuarter : {}", id);
        fiscalQuarterRepository.deleteById(id);
        fiscalQuarterSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalQuarterDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of FiscalQuarters for query {}", query);
        return fiscalQuarterSearchRepository.search(query, pageable).map(fiscalQuarterMapper::toDto);
    }
}
