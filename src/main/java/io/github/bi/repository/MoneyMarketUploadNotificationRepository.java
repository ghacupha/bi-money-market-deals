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

import io.github.bi.domain.MoneyMarketUploadNotification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoneyMarketUploadNotification entity.
 *
 * When extending this class, extend MoneyMarketUploadNotificationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MoneyMarketUploadNotificationRepository
    extends
        MoneyMarketUploadNotificationRepositoryWithBagRelationships,
        JpaRepository<MoneyMarketUploadNotification, Long>,
        JpaSpecificationExecutor<MoneyMarketUploadNotification> {
    default Optional<MoneyMarketUploadNotification> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<MoneyMarketUploadNotification> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<MoneyMarketUploadNotification> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select moneyMarketUploadNotification from MoneyMarketUploadNotification moneyMarketUploadNotification left join fetch moneyMarketUploadNotification.moneyMarketList left join fetch moneyMarketUploadNotification.reportBatch",
        countQuery = "select count(moneyMarketUploadNotification) from MoneyMarketUploadNotification moneyMarketUploadNotification"
    )
    Page<MoneyMarketUploadNotification> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select moneyMarketUploadNotification from MoneyMarketUploadNotification moneyMarketUploadNotification left join fetch moneyMarketUploadNotification.moneyMarketList left join fetch moneyMarketUploadNotification.reportBatch"
    )
    List<MoneyMarketUploadNotification> findAllWithToOneRelationships();

    @Query(
        "select moneyMarketUploadNotification from MoneyMarketUploadNotification moneyMarketUploadNotification left join fetch moneyMarketUploadNotification.moneyMarketList left join fetch moneyMarketUploadNotification.reportBatch where moneyMarketUploadNotification.id =:id"
    )
    Optional<MoneyMarketUploadNotification> findOneWithToOneRelationships(@Param("id") Long id);
}
