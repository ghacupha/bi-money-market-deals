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

import io.github.bi.domain.MoneyMarketList;
import io.github.bi.repository.MoneyMarketListRepository;
import io.github.bi.repository.search.MoneyMarketListSearchRepository;
import io.github.bi.service.MoneyMarketListService;
import io.github.bi.service.dto.MoneyMarketListDTO;
import io.github.bi.service.mapper.MoneyMarketListMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.MoneyMarketList}.
 */
@Service
@Transactional
public class MoneyMarketListServiceImpl implements MoneyMarketListService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketListServiceImpl.class);

    private final MoneyMarketListRepository moneyMarketListRepository;

    private final MoneyMarketListMapper moneyMarketListMapper;

    private final MoneyMarketListSearchRepository moneyMarketListSearchRepository;

    public MoneyMarketListServiceImpl(
        MoneyMarketListRepository moneyMarketListRepository,
        MoneyMarketListMapper moneyMarketListMapper,
        MoneyMarketListSearchRepository moneyMarketListSearchRepository
    ) {
        this.moneyMarketListRepository = moneyMarketListRepository;
        this.moneyMarketListMapper = moneyMarketListMapper;
        this.moneyMarketListSearchRepository = moneyMarketListSearchRepository;
    }

    @Override
    public MoneyMarketListDTO save(MoneyMarketListDTO moneyMarketListDTO) {
        LOG.debug("Request to save MoneyMarketList : {}", moneyMarketListDTO);
        MoneyMarketList moneyMarketList = moneyMarketListMapper.toEntity(moneyMarketListDTO);
        moneyMarketList = moneyMarketListRepository.save(moneyMarketList);
        moneyMarketListSearchRepository.index(moneyMarketList);
        return moneyMarketListMapper.toDto(moneyMarketList);
    }

    @Override
    public MoneyMarketListDTO update(MoneyMarketListDTO moneyMarketListDTO) {
        LOG.debug("Request to update MoneyMarketList : {}", moneyMarketListDTO);
        MoneyMarketList moneyMarketList = moneyMarketListMapper.toEntity(moneyMarketListDTO);
        moneyMarketList = moneyMarketListRepository.save(moneyMarketList);
        moneyMarketListSearchRepository.index(moneyMarketList);
        return moneyMarketListMapper.toDto(moneyMarketList);
    }

    @Override
    public Optional<MoneyMarketListDTO> partialUpdate(MoneyMarketListDTO moneyMarketListDTO) {
        LOG.debug("Request to partially update MoneyMarketList : {}", moneyMarketListDTO);

        return moneyMarketListRepository
            .findById(moneyMarketListDTO.getId())
            .map(existingMoneyMarketList -> {
                moneyMarketListMapper.partialUpdate(existingMoneyMarketList, moneyMarketListDTO);

                return existingMoneyMarketList;
            })
            .map(moneyMarketListRepository::save)
            .map(savedMoneyMarketList -> {
                moneyMarketListSearchRepository.index(savedMoneyMarketList);
                return savedMoneyMarketList;
            })
            .map(moneyMarketListMapper::toDto);
    }

    public Page<MoneyMarketListDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moneyMarketListRepository.findAllWithEagerRelationships(pageable).map(moneyMarketListMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyMarketListDTO> findOne(Long id) {
        LOG.debug("Request to get MoneyMarketList : {}", id);
        return moneyMarketListRepository.findOneWithEagerRelationships(id).map(moneyMarketListMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyMarketList : {}", id);
        moneyMarketListRepository.deleteById(id);
        moneyMarketListSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyMarketListDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MoneyMarketLists for query {}", query);
        return moneyMarketListSearchRepository.search(query, pageable).map(moneyMarketListMapper::toDto);
    }
}
