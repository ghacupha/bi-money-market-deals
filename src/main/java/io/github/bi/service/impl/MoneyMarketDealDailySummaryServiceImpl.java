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

import io.github.bi.domain.MoneyMarketDealDailySummary;
import io.github.bi.repository.MoneyMarketDealDailySummaryRepository;
import io.github.bi.repository.search.MoneyMarketDealDailySummarySearchRepository;
import io.github.bi.service.MoneyMarketDealDailySummaryService;
import io.github.bi.service.dto.MoneyMarketDealDailySummaryDTO;
import io.github.bi.service.mapper.MoneyMarketDealDailySummaryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.MoneyMarketDealDailySummary}.
 */
@Service
@Transactional
public class MoneyMarketDealDailySummaryServiceImpl implements MoneyMarketDealDailySummaryService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketDealDailySummaryServiceImpl.class);

    private final MoneyMarketDealDailySummaryRepository moneyMarketDealDailySummaryRepository;

    private final MoneyMarketDealDailySummaryMapper moneyMarketDealDailySummaryMapper;

    private final MoneyMarketDealDailySummarySearchRepository moneyMarketDealDailySummarySearchRepository;

    public MoneyMarketDealDailySummaryServiceImpl(
        MoneyMarketDealDailySummaryRepository moneyMarketDealDailySummaryRepository,
        MoneyMarketDealDailySummaryMapper moneyMarketDealDailySummaryMapper,
        MoneyMarketDealDailySummarySearchRepository moneyMarketDealDailySummarySearchRepository
    ) {
        this.moneyMarketDealDailySummaryRepository = moneyMarketDealDailySummaryRepository;
        this.moneyMarketDealDailySummaryMapper = moneyMarketDealDailySummaryMapper;
        this.moneyMarketDealDailySummarySearchRepository = moneyMarketDealDailySummarySearchRepository;
    }

    @Override
    public MoneyMarketDealDailySummaryDTO save(MoneyMarketDealDailySummaryDTO moneyMarketDealDailySummaryDTO) {
        LOG.debug("Request to save MoneyMarketDealDailySummary : {}", moneyMarketDealDailySummaryDTO);
        MoneyMarketDealDailySummary moneyMarketDealDailySummary = moneyMarketDealDailySummaryMapper.toEntity(
            moneyMarketDealDailySummaryDTO
        );
        moneyMarketDealDailySummary = moneyMarketDealDailySummaryRepository.save(moneyMarketDealDailySummary);
        moneyMarketDealDailySummarySearchRepository.index(moneyMarketDealDailySummary);
        return moneyMarketDealDailySummaryMapper.toDto(moneyMarketDealDailySummary);
    }

    @Override
    public MoneyMarketDealDailySummaryDTO update(MoneyMarketDealDailySummaryDTO moneyMarketDealDailySummaryDTO) {
        LOG.debug("Request to update MoneyMarketDealDailySummary : {}", moneyMarketDealDailySummaryDTO);
        MoneyMarketDealDailySummary moneyMarketDealDailySummary = moneyMarketDealDailySummaryMapper.toEntity(
            moneyMarketDealDailySummaryDTO
        );
        moneyMarketDealDailySummary = moneyMarketDealDailySummaryRepository.save(moneyMarketDealDailySummary);
        moneyMarketDealDailySummarySearchRepository.index(moneyMarketDealDailySummary);
        return moneyMarketDealDailySummaryMapper.toDto(moneyMarketDealDailySummary);
    }

    @Override
    public Optional<MoneyMarketDealDailySummaryDTO> partialUpdate(MoneyMarketDealDailySummaryDTO moneyMarketDealDailySummaryDTO) {
        LOG.debug("Request to partially update MoneyMarketDealDailySummary : {}", moneyMarketDealDailySummaryDTO);

        return moneyMarketDealDailySummaryRepository
            .findById(moneyMarketDealDailySummaryDTO.getId())
            .map(existingMoneyMarketDealDailySummary -> {
                moneyMarketDealDailySummaryMapper.partialUpdate(existingMoneyMarketDealDailySummary, moneyMarketDealDailySummaryDTO);

                return existingMoneyMarketDealDailySummary;
            })
            .map(moneyMarketDealDailySummaryRepository::save)
            .map(savedMoneyMarketDealDailySummary -> {
                moneyMarketDealDailySummarySearchRepository.index(savedMoneyMarketDealDailySummary);
                return savedMoneyMarketDealDailySummary;
            })
            .map(moneyMarketDealDailySummaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyMarketDealDailySummaryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MoneyMarketDealDailySummaries");
        return moneyMarketDealDailySummaryRepository.findAll(pageable).map(moneyMarketDealDailySummaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyMarketDealDailySummaryDTO> findOne(Long id) {
        LOG.debug("Request to get MoneyMarketDealDailySummary : {}", id);
        return moneyMarketDealDailySummaryRepository.findById(id).map(moneyMarketDealDailySummaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyMarketDealDailySummary : {}", id);
        moneyMarketDealDailySummaryRepository.deleteById(id);
        moneyMarketDealDailySummarySearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyMarketDealDailySummaryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MoneyMarketDealDailySummaries for query {}", query);
        return moneyMarketDealDailySummarySearchRepository.search(query, pageable).map(moneyMarketDealDailySummaryMapper::toDto);
    }
}
