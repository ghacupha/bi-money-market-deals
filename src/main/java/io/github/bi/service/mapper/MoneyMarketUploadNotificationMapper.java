package io.github.bi.service.mapper;

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
import io.github.bi.domain.MoneyMarketUploadNotification;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.ReportBatch;
import io.github.bi.service.dto.MoneyMarketListDTO;
import io.github.bi.service.dto.MoneyMarketUploadNotificationDTO;
import io.github.bi.service.dto.PlaceholderDTO;
import io.github.bi.service.dto.ReportBatchDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyMarketUploadNotification} and its DTO {@link MoneyMarketUploadNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyMarketUploadNotificationMapper extends EntityMapper<MoneyMarketUploadNotificationDTO, MoneyMarketUploadNotification> {
    @Mapping(target = "moneyMarketList", source = "moneyMarketList", qualifiedByName = "moneyMarketListDescription")
    @Mapping(target = "reportBatch", source = "reportBatch", qualifiedByName = "reportBatchDescription")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "placeholderTokenSet")
    MoneyMarketUploadNotificationDTO toDto(MoneyMarketUploadNotification s);

    @Mapping(target = "removePlaceholder", ignore = true)
    MoneyMarketUploadNotification toEntity(MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO);

    @Named("moneyMarketListDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    MoneyMarketListDTO toDtoMoneyMarketListDescription(MoneyMarketList moneyMarketList);

    @Named("reportBatchDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    ReportBatchDTO toDtoReportBatchDescription(ReportBatch reportBatch);

    @Named("placeholderToken")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "token", source = "token")
    PlaceholderDTO toDtoPlaceholderToken(Placeholder placeholder);

    @Named("placeholderTokenSet")
    default Set<PlaceholderDTO> toDtoPlaceholderTokenSet(Set<Placeholder> placeholder) {
        return placeholder.stream().map(this::toDtoPlaceholderToken).collect(Collectors.toSet());
    }
}
