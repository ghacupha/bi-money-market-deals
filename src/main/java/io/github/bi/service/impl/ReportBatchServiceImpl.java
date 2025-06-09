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

import io.github.bi.domain.ReportBatch;
import io.github.bi.repository.ReportBatchRepository;
import io.github.bi.repository.search.ReportBatchSearchRepository;
import io.github.bi.service.ReportBatchService;
import io.github.bi.service.dto.ReportBatchDTO;
import io.github.bi.service.mapper.ReportBatchMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.ReportBatch}.
 */
@Service
@Transactional
public class ReportBatchServiceImpl implements ReportBatchService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportBatchServiceImpl.class);

    private final ReportBatchRepository reportBatchRepository;

    private final ReportBatchMapper reportBatchMapper;

    private final ReportBatchSearchRepository reportBatchSearchRepository;

    public ReportBatchServiceImpl(
        ReportBatchRepository reportBatchRepository,
        ReportBatchMapper reportBatchMapper,
        ReportBatchSearchRepository reportBatchSearchRepository
    ) {
        this.reportBatchRepository = reportBatchRepository;
        this.reportBatchMapper = reportBatchMapper;
        this.reportBatchSearchRepository = reportBatchSearchRepository;
    }

    @Override
    public ReportBatchDTO save(ReportBatchDTO reportBatchDTO) {
        LOG.debug("Request to save ReportBatch : {}", reportBatchDTO);
        ReportBatch reportBatch = reportBatchMapper.toEntity(reportBatchDTO);
        reportBatch = reportBatchRepository.save(reportBatch);
        reportBatchSearchRepository.index(reportBatch);
        return reportBatchMapper.toDto(reportBatch);
    }

    @Override
    public ReportBatchDTO update(ReportBatchDTO reportBatchDTO) {
        LOG.debug("Request to update ReportBatch : {}", reportBatchDTO);
        ReportBatch reportBatch = reportBatchMapper.toEntity(reportBatchDTO);
        reportBatch = reportBatchRepository.save(reportBatch);
        reportBatchSearchRepository.index(reportBatch);
        return reportBatchMapper.toDto(reportBatch);
    }

    @Override
    public Optional<ReportBatchDTO> partialUpdate(ReportBatchDTO reportBatchDTO) {
        LOG.debug("Request to partially update ReportBatch : {}", reportBatchDTO);

        return reportBatchRepository
            .findById(reportBatchDTO.getId())
            .map(existingReportBatch -> {
                reportBatchMapper.partialUpdate(existingReportBatch, reportBatchDTO);

                return existingReportBatch;
            })
            .map(reportBatchRepository::save)
            .map(savedReportBatch -> {
                reportBatchSearchRepository.index(savedReportBatch);
                return savedReportBatch;
            })
            .map(reportBatchMapper::toDto);
    }

    public Page<ReportBatchDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportBatchRepository.findAllWithEagerRelationships(pageable).map(reportBatchMapper::toDto);
    }

    /**
     *  Get all the reportBatches where MoneyMarketList is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportBatchDTO> findAllWhereMoneyMarketListIsNull() {
        LOG.debug("Request to get all reportBatches where MoneyMarketList is null");
        return StreamSupport.stream(reportBatchRepository.findAll().spliterator(), false)
            .filter(reportBatch -> reportBatch.getMoneyMarketList() == null)
            .map(reportBatchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportBatchDTO> findOne(Long id) {
        LOG.debug("Request to get ReportBatch : {}", id);
        return reportBatchRepository.findOneWithEagerRelationships(id).map(reportBatchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReportBatch : {}", id);
        reportBatchRepository.deleteById(id);
        reportBatchSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportBatchDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ReportBatches for query {}", query);
        return reportBatchSearchRepository.search(query, pageable).map(reportBatchMapper::toDto);
    }
}
