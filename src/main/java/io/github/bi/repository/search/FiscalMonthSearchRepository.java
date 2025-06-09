package io.github.bi.repository.search;

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

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import io.github.bi.domain.FiscalMonth;
import io.github.bi.repository.FiscalMonthRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FiscalMonth} entity.
 */
public interface FiscalMonthSearchRepository extends ElasticsearchRepository<FiscalMonth, Long>, FiscalMonthSearchRepositoryInternal {}

interface FiscalMonthSearchRepositoryInternal {
    Page<FiscalMonth> search(String query, Pageable pageable);

    Page<FiscalMonth> search(Query query);

    @Async
    void index(FiscalMonth entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FiscalMonthSearchRepositoryInternalImpl implements FiscalMonthSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FiscalMonthRepository repository;

    FiscalMonthSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FiscalMonthRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<FiscalMonth> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<FiscalMonth> search(Query query) {
        SearchHits<FiscalMonth> searchHits = elasticsearchTemplate.search(query, FiscalMonth.class);
        List<FiscalMonth> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(FiscalMonth entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), FiscalMonth.class);
    }
}
