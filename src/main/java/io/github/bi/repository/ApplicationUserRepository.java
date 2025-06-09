package io.github.bi.repository;

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

import io.github.bi.domain.ApplicationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApplicationUser entity.
 *
 * When extending this class, extend ApplicationUserRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ApplicationUserRepository
    extends ApplicationUserRepositoryWithBagRelationships, JpaRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {
    default Optional<ApplicationUser> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<ApplicationUser> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.organization left join fetch applicationUser.department left join fetch applicationUser.securityClearance left join fetch applicationUser.dealerIdentity",
        countQuery = "select count(applicationUser) from ApplicationUser applicationUser"
    )
    Page<ApplicationUser> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.organization left join fetch applicationUser.department left join fetch applicationUser.securityClearance left join fetch applicationUser.dealerIdentity"
    )
    List<ApplicationUser> findAllWithToOneRelationships();

    @Query(
        "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.organization left join fetch applicationUser.department left join fetch applicationUser.securityClearance left join fetch applicationUser.dealerIdentity where applicationUser.id =:id"
    )
    Optional<ApplicationUser> findOneWithToOneRelationships(@Param("id") Long id);
}
