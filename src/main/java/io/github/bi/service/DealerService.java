package io.github.bi.service;

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

import io.github.bi.service.dto.DealerDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.bi.domain.Dealer}.
 */
public interface DealerService {
    /**
     * Save a dealer.
     *
     * @param dealerDTO the entity to save.
     * @return the persisted entity.
     */
    DealerDTO save(DealerDTO dealerDTO);

    /**
     * Updates a dealer.
     *
     * @param dealerDTO the entity to update.
     * @return the persisted entity.
     */
    DealerDTO update(DealerDTO dealerDTO);

    /**
     * Partially updates a dealer.
     *
     * @param dealerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DealerDTO> partialUpdate(DealerDTO dealerDTO);

    /**
     * Get all the DealerDTO where ApplicationUser is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<DealerDTO> findAllWhereApplicationUserIsNull();

    /**
     * Get all the dealers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DealerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" dealer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DealerDTO> findOne(Long id);

    /**
     * Delete the "id" dealer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the dealer corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DealerDTO> search(String query, Pageable pageable);
}
