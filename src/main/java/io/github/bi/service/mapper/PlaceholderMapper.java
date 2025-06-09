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

import io.github.bi.domain.FiscalMonth;
import io.github.bi.domain.FiscalQuarter;
import io.github.bi.domain.FiscalYear;
import io.github.bi.domain.MoneyMarketList;
import io.github.bi.domain.Placeholder;
import io.github.bi.service.dto.FiscalMonthDTO;
import io.github.bi.service.dto.FiscalQuarterDTO;
import io.github.bi.service.dto.FiscalYearDTO;
import io.github.bi.service.dto.MoneyMarketListDTO;
import io.github.bi.service.dto.PlaceholderDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Placeholder} and its DTO {@link PlaceholderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaceholderMapper extends EntityMapper<PlaceholderDTO, Placeholder> {
    @Mapping(target = "containingPlaceholder", source = "containingPlaceholder", qualifiedByName = "placeholderDescription")
    @Mapping(target = "fiscalYears", source = "fiscalYears", qualifiedByName = "fiscalYearIdSet")
    @Mapping(target = "fiscalQuarters", source = "fiscalQuarters", qualifiedByName = "fiscalQuarterIdSet")
    @Mapping(target = "fiscalMonths", source = "fiscalMonths", qualifiedByName = "fiscalMonthIdSet")
    @Mapping(target = "moneyMarketLists", source = "moneyMarketLists", qualifiedByName = "moneyMarketListIdSet")
    PlaceholderDTO toDto(Placeholder s);

    @Mapping(target = "fiscalYears", ignore = true)
    @Mapping(target = "removeFiscalYear", ignore = true)
    @Mapping(target = "fiscalQuarters", ignore = true)
    @Mapping(target = "removeFiscalQuarter", ignore = true)
    @Mapping(target = "fiscalMonths", ignore = true)
    @Mapping(target = "removeFiscalMonth", ignore = true)
    @Mapping(target = "moneyMarketLists", ignore = true)
    @Mapping(target = "removeMoneyMarketList", ignore = true)
    Placeholder toEntity(PlaceholderDTO placeholderDTO);

    @Named("placeholderDescription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    PlaceholderDTO toDtoPlaceholderDescription(Placeholder placeholder);

    @Named("fiscalYearId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FiscalYearDTO toDtoFiscalYearId(FiscalYear fiscalYear);

    @Named("fiscalYearIdSet")
    default Set<FiscalYearDTO> toDtoFiscalYearIdSet(Set<FiscalYear> fiscalYear) {
        return fiscalYear.stream().map(this::toDtoFiscalYearId).collect(Collectors.toSet());
    }

    @Named("fiscalQuarterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FiscalQuarterDTO toDtoFiscalQuarterId(FiscalQuarter fiscalQuarter);

    @Named("fiscalQuarterIdSet")
    default Set<FiscalQuarterDTO> toDtoFiscalQuarterIdSet(Set<FiscalQuarter> fiscalQuarter) {
        return fiscalQuarter.stream().map(this::toDtoFiscalQuarterId).collect(Collectors.toSet());
    }

    @Named("fiscalMonthId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FiscalMonthDTO toDtoFiscalMonthId(FiscalMonth fiscalMonth);

    @Named("fiscalMonthIdSet")
    default Set<FiscalMonthDTO> toDtoFiscalMonthIdSet(Set<FiscalMonth> fiscalMonth) {
        return fiscalMonth.stream().map(this::toDtoFiscalMonthId).collect(Collectors.toSet());
    }

    @Named("moneyMarketListId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MoneyMarketListDTO toDtoMoneyMarketListId(MoneyMarketList moneyMarketList);

    @Named("moneyMarketListIdSet")
    default Set<MoneyMarketListDTO> toDtoMoneyMarketListIdSet(Set<MoneyMarketList> moneyMarketList) {
        return moneyMarketList.stream().map(this::toDtoMoneyMarketListId).collect(Collectors.toSet());
    }
}
