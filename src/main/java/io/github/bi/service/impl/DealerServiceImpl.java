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

import io.github.bi.domain.Dealer;
import io.github.bi.repository.DealerRepository;
import io.github.bi.repository.search.DealerSearchRepository;
import io.github.bi.service.DealerService;
import io.github.bi.service.dto.DealerDTO;
import io.github.bi.service.mapper.DealerMapper;
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
 * Service Implementation for managing {@link io.github.bi.domain.Dealer}.
 */
@Service
@Transactional
public class DealerServiceImpl implements DealerService {

    private static final Logger LOG = LoggerFactory.getLogger(DealerServiceImpl.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;

    private final DealerSearchRepository dealerSearchRepository;

    public DealerServiceImpl(DealerRepository dealerRepository, DealerMapper dealerMapper, DealerSearchRepository dealerSearchRepository) {
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
        this.dealerSearchRepository = dealerSearchRepository;
    }

    @Override
    public DealerDTO save(DealerDTO dealerDTO) {
        LOG.debug("Request to save Dealer : {}", dealerDTO);
        Dealer dealer = dealerMapper.toEntity(dealerDTO);
        dealer = dealerRepository.save(dealer);
        dealerSearchRepository.index(dealer);
        return dealerMapper.toDto(dealer);
    }

    @Override
    public DealerDTO update(DealerDTO dealerDTO) {
        LOG.debug("Request to update Dealer : {}", dealerDTO);
        Dealer dealer = dealerMapper.toEntity(dealerDTO);
        dealer = dealerRepository.save(dealer);
        dealerSearchRepository.index(dealer);
        return dealerMapper.toDto(dealer);
    }

    @Override
    public Optional<DealerDTO> partialUpdate(DealerDTO dealerDTO) {
        LOG.debug("Request to partially update Dealer : {}", dealerDTO);

        return dealerRepository
            .findById(dealerDTO.getId())
            .map(existingDealer -> {
                dealerMapper.partialUpdate(existingDealer, dealerDTO);

                return existingDealer;
            })
            .map(dealerRepository::save)
            .map(savedDealer -> {
                dealerSearchRepository.index(savedDealer);
                return savedDealer;
            })
            .map(dealerMapper::toDto);
    }

    public Page<DealerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dealerRepository.findAllWithEagerRelationships(pageable).map(dealerMapper::toDto);
    }

    /**
     *  Get all the dealers where ApplicationUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DealerDTO> findAllWhereApplicationUserIsNull() {
        LOG.debug("Request to get all dealers where ApplicationUser is null");
        return StreamSupport.stream(dealerRepository.findAll().spliterator(), false)
            .filter(dealer -> dealer.getApplicationUser() == null)
            .map(dealerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DealerDTO> findOne(Long id) {
        LOG.debug("Request to get Dealer : {}", id);
        return dealerRepository.findOneWithEagerRelationships(id).map(dealerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Dealer : {}", id);
        dealerRepository.deleteById(id);
        dealerSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Dealers for query {}", query);
        return dealerSearchRepository.search(query, pageable).map(dealerMapper::toDto);
    }
}
