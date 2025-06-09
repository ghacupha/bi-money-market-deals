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

import io.github.bi.domain.FiscalQuarter;
import io.github.bi.domain.FiscalYear;
import io.github.bi.domain.Placeholder;
import io.github.bi.service.dto.FiscalQuarterDTO;
import io.github.bi.service.dto.FiscalYearDTO;
import io.github.bi.service.dto.PlaceholderDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FiscalQuarter} and its DTO {@link FiscalQuarterDTO}.
 */
@Mapper(componentModel = "spring")
public interface FiscalQuarterMapper extends EntityMapper<FiscalQuarterDTO, FiscalQuarter> {
    @Mapping(target = "fiscalYear", source = "fiscalYear", qualifiedByName = "fiscalYearFiscalYearCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "placeholderDescriptionSet")
    FiscalQuarterDTO toDto(FiscalQuarter s);

    @Mapping(target = "removePlaceholder", ignore = true)
    FiscalQuarter toEntity(FiscalQuarterDTO fiscalQuarterDTO);

    @Named("fiscalYearFiscalYearCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fiscalYearCode", source = "fiscalYearCode")
    FiscalYearDTO toDtoFiscalYearFiscalYearCode(FiscalYear fiscalYear);

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
