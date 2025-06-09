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

import io.github.bi.domain.MoneyMarketUploadNotification;
import io.github.bi.repository.MoneyMarketUploadNotificationRepository;
import io.github.bi.repository.search.MoneyMarketUploadNotificationSearchRepository;
import io.github.bi.service.MoneyMarketUploadNotificationService;
import io.github.bi.service.dto.MoneyMarketUploadNotificationDTO;
import io.github.bi.service.mapper.MoneyMarketUploadNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.MoneyMarketUploadNotification}.
 */
@Service
@Transactional
public class MoneyMarketUploadNotificationServiceImpl implements MoneyMarketUploadNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketUploadNotificationServiceImpl.class);

    private final MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepository;

    private final MoneyMarketUploadNotificationMapper moneyMarketUploadNotificationMapper;

    private final MoneyMarketUploadNotificationSearchRepository moneyMarketUploadNotificationSearchRepository;

    public MoneyMarketUploadNotificationServiceImpl(
        MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepository,
        MoneyMarketUploadNotificationMapper moneyMarketUploadNotificationMapper,
        MoneyMarketUploadNotificationSearchRepository moneyMarketUploadNotificationSearchRepository
    ) {
        this.moneyMarketUploadNotificationRepository = moneyMarketUploadNotificationRepository;
        this.moneyMarketUploadNotificationMapper = moneyMarketUploadNotificationMapper;
        this.moneyMarketUploadNotificationSearchRepository = moneyMarketUploadNotificationSearchRepository;
    }

    @Override
    public MoneyMarketUploadNotificationDTO save(MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO) {
        LOG.debug("Request to save MoneyMarketUploadNotification : {}", moneyMarketUploadNotificationDTO);
        MoneyMarketUploadNotification moneyMarketUploadNotification = moneyMarketUploadNotificationMapper.toEntity(
            moneyMarketUploadNotificationDTO
        );
        moneyMarketUploadNotification = moneyMarketUploadNotificationRepository.save(moneyMarketUploadNotification);
        moneyMarketUploadNotificationSearchRepository.index(moneyMarketUploadNotification);
        return moneyMarketUploadNotificationMapper.toDto(moneyMarketUploadNotification);
    }

    @Override
    public MoneyMarketUploadNotificationDTO update(MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO) {
        LOG.debug("Request to update MoneyMarketUploadNotification : {}", moneyMarketUploadNotificationDTO);
        MoneyMarketUploadNotification moneyMarketUploadNotification = moneyMarketUploadNotificationMapper.toEntity(
            moneyMarketUploadNotificationDTO
        );
        moneyMarketUploadNotification = moneyMarketUploadNotificationRepository.save(moneyMarketUploadNotification);
        moneyMarketUploadNotificationSearchRepository.index(moneyMarketUploadNotification);
        return moneyMarketUploadNotificationMapper.toDto(moneyMarketUploadNotification);
    }

    @Override
    public Optional<MoneyMarketUploadNotificationDTO> partialUpdate(MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO) {
        LOG.debug("Request to partially update MoneyMarketUploadNotification : {}", moneyMarketUploadNotificationDTO);

        return moneyMarketUploadNotificationRepository
            .findById(moneyMarketUploadNotificationDTO.getId())
            .map(existingMoneyMarketUploadNotification -> {
                moneyMarketUploadNotificationMapper.partialUpdate(existingMoneyMarketUploadNotification, moneyMarketUploadNotificationDTO);

                return existingMoneyMarketUploadNotification;
            })
            .map(moneyMarketUploadNotificationRepository::save)
            .map(savedMoneyMarketUploadNotification -> {
                moneyMarketUploadNotificationSearchRepository.index(savedMoneyMarketUploadNotification);
                return savedMoneyMarketUploadNotification;
            })
            .map(moneyMarketUploadNotificationMapper::toDto);
    }

    public Page<MoneyMarketUploadNotificationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moneyMarketUploadNotificationRepository
            .findAllWithEagerRelationships(pageable)
            .map(moneyMarketUploadNotificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyMarketUploadNotificationDTO> findOne(Long id) {
        LOG.debug("Request to get MoneyMarketUploadNotification : {}", id);
        return moneyMarketUploadNotificationRepository.findOneWithEagerRelationships(id).map(moneyMarketUploadNotificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyMarketUploadNotification : {}", id);
        moneyMarketUploadNotificationRepository.deleteById(id);
        moneyMarketUploadNotificationSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyMarketUploadNotificationDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MoneyMarketUploadNotifications for query {}", query);
        return moneyMarketUploadNotificationSearchRepository.search(query, pageable).map(moneyMarketUploadNotificationMapper::toDto);
    }
}
