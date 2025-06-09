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

import io.github.bi.domain.SecurityClearance;
import io.github.bi.repository.SecurityClearanceRepository;
import io.github.bi.repository.search.SecurityClearanceSearchRepository;
import io.github.bi.service.SecurityClearanceService;
import io.github.bi.service.dto.SecurityClearanceDTO;
import io.github.bi.service.mapper.SecurityClearanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.SecurityClearance}.
 */
@Service
@Transactional
public class SecurityClearanceServiceImpl implements SecurityClearanceService {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityClearanceServiceImpl.class);

    private final SecurityClearanceRepository securityClearanceRepository;

    private final SecurityClearanceMapper securityClearanceMapper;

    private final SecurityClearanceSearchRepository securityClearanceSearchRepository;

    public SecurityClearanceServiceImpl(
        SecurityClearanceRepository securityClearanceRepository,
        SecurityClearanceMapper securityClearanceMapper,
        SecurityClearanceSearchRepository securityClearanceSearchRepository
    ) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.securityClearanceMapper = securityClearanceMapper;
        this.securityClearanceSearchRepository = securityClearanceSearchRepository;
    }

    @Override
    public SecurityClearanceDTO save(SecurityClearanceDTO securityClearanceDTO) {
        LOG.debug("Request to save SecurityClearance : {}", securityClearanceDTO);
        SecurityClearance securityClearance = securityClearanceMapper.toEntity(securityClearanceDTO);
        securityClearance = securityClearanceRepository.save(securityClearance);
        securityClearanceSearchRepository.index(securityClearance);
        return securityClearanceMapper.toDto(securityClearance);
    }

    @Override
    public SecurityClearanceDTO update(SecurityClearanceDTO securityClearanceDTO) {
        LOG.debug("Request to update SecurityClearance : {}", securityClearanceDTO);
        SecurityClearance securityClearance = securityClearanceMapper.toEntity(securityClearanceDTO);
        securityClearance = securityClearanceRepository.save(securityClearance);
        securityClearanceSearchRepository.index(securityClearance);
        return securityClearanceMapper.toDto(securityClearance);
    }

    @Override
    public Optional<SecurityClearanceDTO> partialUpdate(SecurityClearanceDTO securityClearanceDTO) {
        LOG.debug("Request to partially update SecurityClearance : {}", securityClearanceDTO);

        return securityClearanceRepository
            .findById(securityClearanceDTO.getId())
            .map(existingSecurityClearance -> {
                securityClearanceMapper.partialUpdate(existingSecurityClearance, securityClearanceDTO);

                return existingSecurityClearance;
            })
            .map(securityClearanceRepository::save)
            .map(savedSecurityClearance -> {
                securityClearanceSearchRepository.index(savedSecurityClearance);
                return savedSecurityClearance;
            })
            .map(securityClearanceMapper::toDto);
    }

    public Page<SecurityClearanceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return securityClearanceRepository.findAllWithEagerRelationships(pageable).map(securityClearanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecurityClearanceDTO> findOne(Long id) {
        LOG.debug("Request to get SecurityClearance : {}", id);
        return securityClearanceRepository.findOneWithEagerRelationships(id).map(securityClearanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SecurityClearance : {}", id);
        securityClearanceRepository.deleteById(id);
        securityClearanceSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityClearanceDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SecurityClearances for query {}", query);
        return securityClearanceSearchRepository.search(query, pageable).map(securityClearanceMapper::toDto);
    }
}
