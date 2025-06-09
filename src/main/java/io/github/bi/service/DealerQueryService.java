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

import io.github.bi.domain.*; // for static metamodels
import io.github.bi.domain.Dealer;
import io.github.bi.repository.DealerRepository;
import io.github.bi.repository.search.DealerSearchRepository;
import io.github.bi.service.criteria.DealerCriteria;
import io.github.bi.service.dto.DealerDTO;
import io.github.bi.service.mapper.DealerMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Dealer} entities in the database.
 * The main input is a {@link DealerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DealerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DealerQueryService extends QueryService<Dealer> {

    private static final Logger LOG = LoggerFactory.getLogger(DealerQueryService.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;

    private final DealerSearchRepository dealerSearchRepository;

    public DealerQueryService(DealerRepository dealerRepository, DealerMapper dealerMapper, DealerSearchRepository dealerSearchRepository) {
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
        this.dealerSearchRepository = dealerSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DealerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DealerDTO> findByCriteria(DealerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dealer> specification = createSpecification(criteria);
        return dealerRepository.fetchBagRelationships(dealerRepository.findAll(specification, page)).map(dealerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DealerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Dealer> specification = createSpecification(criteria);
        return dealerRepository.count(specification);
    }

    /**
     * Function to convert {@link DealerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dealer> createSpecification(DealerCriteria criteria) {
        Specification<Dealer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Dealer_.id),
                buildStringSpecification(criteria.getDealerName(), Dealer_.dealerName),
                buildStringSpecification(criteria.getTaxNumber(), Dealer_.taxNumber),
                buildStringSpecification(criteria.getIdentificationDocumentNumber(), Dealer_.identificationDocumentNumber),
                buildStringSpecification(criteria.getOrganizationName(), Dealer_.organizationName),
                buildStringSpecification(criteria.getDepartment(), Dealer_.department),
                buildStringSpecification(criteria.getPosition(), Dealer_.position),
                buildStringSpecification(criteria.getPostalAddress(), Dealer_.postalAddress),
                buildStringSpecification(criteria.getPhysicalAddress(), Dealer_.physicalAddress),
                buildStringSpecification(criteria.getAccountName(), Dealer_.accountName),
                buildStringSpecification(criteria.getAccountNumber(), Dealer_.accountNumber),
                buildStringSpecification(criteria.getBankersName(), Dealer_.bankersName),
                buildStringSpecification(criteria.getBankersBranch(), Dealer_.bankersBranch),
                buildStringSpecification(criteria.getBankersSwiftCode(), Dealer_.bankersSwiftCode),
                buildStringSpecification(criteria.getFileUploadToken(), Dealer_.fileUploadToken),
                buildStringSpecification(criteria.getCompilationToken(), Dealer_.compilationToken),
                buildStringSpecification(criteria.getOtherNames(), Dealer_.otherNames),
                buildSpecification(criteria.getDealerGroupId(), root -> root.join(Dealer_.dealerGroup, JoinType.LEFT).get(Dealer_.id)),
                buildSpecification(criteria.getPlaceholderId(), root -> root.join(Dealer_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                ),
                buildSpecification(criteria.getApplicationUserId(), root ->
                    root.join(Dealer_.applicationUser, JoinType.LEFT).get(ApplicationUser_.id)
                )
            );
        }
        return specification;
    }
}
