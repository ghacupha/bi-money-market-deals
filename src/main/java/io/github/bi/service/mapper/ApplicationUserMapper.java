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

import io.github.bi.domain.ApplicationUser;
import io.github.bi.domain.Dealer;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.SecurityClearance;
import io.github.bi.service.dto.ApplicationUserDTO;
import io.github.bi.service.dto.DealerDTO;
import io.github.bi.service.dto.PlaceholderDTO;
import io.github.bi.service.dto.SecurityClearanceDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "organization", source = "organization", qualifiedByName = "dealerDealerName")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerDealerName")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "securityClearanceClearanceLevel")
    @Mapping(target = "dealerIdentity", source = "dealerIdentity", qualifiedByName = "dealerDealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "placeholderDescriptionSet")
    ApplicationUserDTO toDto(ApplicationUser s);

    @Mapping(target = "removePlaceholder", ignore = true)
    ApplicationUser toEntity(ApplicationUserDTO applicationUserDTO);

    @Named("dealerDealerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dealerName", source = "dealerName")
    DealerDTO toDtoDealerDealerName(Dealer dealer);

    @Named("securityClearanceClearanceLevel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "clearanceLevel", source = "clearanceLevel")
    SecurityClearanceDTO toDtoSecurityClearanceClearanceLevel(SecurityClearance securityClearance);

    @Named("placeholderDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    PlaceholderDTO toDtoPlaceholderDescription(Placeholder placeholder);

    @Named("placeholderDescriptionSet")
    default Set<PlaceholderDTO> toDtoPlaceholderDescriptionSet(Set<Placeholder> placeholder) {
        return placeholder.stream().map(this::toDtoPlaceholderDescription).collect(Collectors.toSet());
    }
}
