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

import io.github.bi.domain.Placeholder;
import io.github.bi.repository.PlaceholderRepository;
import io.github.bi.repository.search.PlaceholderSearchRepository;
import io.github.bi.service.PlaceholderService;
import io.github.bi.service.dto.PlaceholderDTO;
import io.github.bi.service.mapper.PlaceholderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.github.bi.domain.Placeholder}.
 */
@Service
@Transactional
public class PlaceholderServiceImpl implements PlaceholderService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaceholderServiceImpl.class);

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderMapper placeholderMapper;

    private final PlaceholderSearchRepository placeholderSearchRepository;

    public PlaceholderServiceImpl(
        PlaceholderRepository placeholderRepository,
        PlaceholderMapper placeholderMapper,
        PlaceholderSearchRepository placeholderSearchRepository
    ) {
        this.placeholderRepository = placeholderRepository;
        this.placeholderMapper = placeholderMapper;
        this.placeholderSearchRepository = placeholderSearchRepository;
    }

    @Override
    public PlaceholderDTO save(PlaceholderDTO placeholderDTO) {
        LOG.debug("Request to save Placeholder : {}", placeholderDTO);
        Placeholder placeholder = placeholderMapper.toEntity(placeholderDTO);
        placeholder = placeholderRepository.save(placeholder);
        placeholderSearchRepository.index(placeholder);
        return placeholderMapper.toDto(placeholder);
    }

    @Override
    public PlaceholderDTO update(PlaceholderDTO placeholderDTO) {
        LOG.debug("Request to update Placeholder : {}", placeholderDTO);
        Placeholder placeholder = placeholderMapper.toEntity(placeholderDTO);
        placeholder = placeholderRepository.save(placeholder);
        placeholderSearchRepository.index(placeholder);
        return placeholderMapper.toDto(placeholder);
    }

    @Override
    public Optional<PlaceholderDTO> partialUpdate(PlaceholderDTO placeholderDTO) {
        LOG.debug("Request to partially update Placeholder : {}", placeholderDTO);

        return placeholderRepository
            .findById(placeholderDTO.getId())
            .map(existingPlaceholder -> {
                placeholderMapper.partialUpdate(existingPlaceholder, placeholderDTO);

                return existingPlaceholder;
            })
            .map(placeholderRepository::save)
            .map(savedPlaceholder -> {
                placeholderSearchRepository.index(savedPlaceholder);
                return savedPlaceholder;
            })
            .map(placeholderMapper::toDto);
    }

    public Page<PlaceholderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return placeholderRepository.findAllWithEagerRelationships(pageable).map(placeholderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlaceholderDTO> findOne(Long id) {
        LOG.debug("Request to get Placeholder : {}", id);
        return placeholderRepository.findOneWithEagerRelationships(id).map(placeholderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Placeholder : {}", id);
        placeholderRepository.deleteById(id);
        placeholderSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Placeholders for query {}", query);
        return placeholderSearchRepository.search(query, pageable).map(placeholderMapper::toDto);
    }
}
