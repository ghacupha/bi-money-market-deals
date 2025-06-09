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

import io.github.bi.domain.Placeholder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Placeholder entity.
 */
@Repository
public interface PlaceholderRepository extends JpaRepository<Placeholder, Long>, JpaSpecificationExecutor<Placeholder> {
    default Optional<Placeholder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Placeholder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Placeholder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select placeholder from Placeholder placeholder left join fetch placeholder.containingPlaceholder",
        countQuery = "select count(placeholder) from Placeholder placeholder"
    )
    Page<Placeholder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select placeholder from Placeholder placeholder left join fetch placeholder.containingPlaceholder")
    List<Placeholder> findAllWithToOneRelationships();

    @Query("select placeholder from Placeholder placeholder left join fetch placeholder.containingPlaceholder where placeholder.id =:id")
    Optional<Placeholder> findOneWithToOneRelationships(@Param("id") Long id);
}
