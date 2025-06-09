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

import io.github.bi.domain.MoneyMarketDeal;
import io.github.bi.repository.MoneyMarketDealRepository;
import io.github.bi.repository.search.MoneyMarketDealSearchRepository;
import io.github.bi.service.MoneyMarketDealService;
import io.github.bi.service.dto.MoneyMarketDealDTO;
import io.github.bi.service.mapper.MoneyMarketDealMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.MoneyMarketDeal}.
 */
@Service
@Transactional
public class MoneyMarketDealServiceImpl implements MoneyMarketDealService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketDealServiceImpl.class);

    private final MoneyMarketDealRepository moneyMarketDealRepository;

    private final MoneyMarketDealMapper moneyMarketDealMapper;

    private final MoneyMarketDealSearchRepository moneyMarketDealSearchRepository;

    public MoneyMarketDealServiceImpl(
        MoneyMarketDealRepository moneyMarketDealRepository,
        MoneyMarketDealMapper moneyMarketDealMapper,
        MoneyMarketDealSearchRepository moneyMarketDealSearchRepository
    ) {
        this.moneyMarketDealRepository = moneyMarketDealRepository;
        this.moneyMarketDealMapper = moneyMarketDealMapper;
        this.moneyMarketDealSearchRepository = moneyMarketDealSearchRepository;
    }

    @Override
    public MoneyMarketDealDTO save(MoneyMarketDealDTO moneyMarketDealDTO) {
        LOG.debug("Request to save MoneyMarketDeal : {}", moneyMarketDealDTO);
        MoneyMarketDeal moneyMarketDeal = moneyMarketDealMapper.toEntity(moneyMarketDealDTO);
        moneyMarketDeal = moneyMarketDealRepository.save(moneyMarketDeal);
        moneyMarketDealSearchRepository.index(moneyMarketDeal);
        return moneyMarketDealMapper.toDto(moneyMarketDeal);
    }

    @Override
    public MoneyMarketDealDTO update(MoneyMarketDealDTO moneyMarketDealDTO) {
        LOG.debug("Request to update MoneyMarketDeal : {}", moneyMarketDealDTO);
        MoneyMarketDeal moneyMarketDeal = moneyMarketDealMapper.toEntity(moneyMarketDealDTO);
        moneyMarketDeal = moneyMarketDealRepository.save(moneyMarketDeal);
        moneyMarketDealSearchRepository.index(moneyMarketDeal);
        return moneyMarketDealMapper.toDto(moneyMarketDeal);
    }

    @Override
    public Optional<MoneyMarketDealDTO> partialUpdate(MoneyMarketDealDTO moneyMarketDealDTO) {
        LOG.debug("Request to partially update MoneyMarketDeal : {}", moneyMarketDealDTO);

        return moneyMarketDealRepository
            .findById(moneyMarketDealDTO.getId())
            .map(existingMoneyMarketDeal -> {
                moneyMarketDealMapper.partialUpdate(existingMoneyMarketDeal, moneyMarketDealDTO);

                return existingMoneyMarketDeal;
            })
            .map(moneyMarketDealRepository::save)
            .map(savedMoneyMarketDeal -> {
                moneyMarketDealSearchRepository.index(savedMoneyMarketDeal);
                return savedMoneyMarketDeal;
            })
            .map(moneyMarketDealMapper::toDto);
    }

    public Page<MoneyMarketDealDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moneyMarketDealRepository.findAllWithEagerRelationships(pageable).map(moneyMarketDealMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyMarketDealDTO> findOne(Long id) {
        LOG.debug("Request to get MoneyMarketDeal : {}", id);
        return moneyMarketDealRepository.findOneWithEagerRelationships(id).map(moneyMarketDealMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyMarketDeal : {}", id);
        moneyMarketDealRepository.deleteById(id);
        moneyMarketDealSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyMarketDealDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MoneyMarketDeals for query {}", query);
        return moneyMarketDealSearchRepository.search(query, pageable).map(moneyMarketDealMapper::toDto);
    }
}
