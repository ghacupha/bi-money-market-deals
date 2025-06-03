package io.github.bi.web.rest.errors;

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

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.ErrorCause;
import java.util.List;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;

public class ElasticsearchExceptionMapper {

    private ElasticsearchExceptionMapper() {}

    public static RuntimeException mapException(RuntimeException originalException) {
        RuntimeException e = originalException;
        if (e.getCause() instanceof UncategorizedElasticsearchException) {
            e = (UncategorizedElasticsearchException) e.getCause();
        }
        if (e.getCause() instanceof ElasticsearchException) {
            ElasticsearchException esException = (ElasticsearchException) e.getCause();
            List<ErrorCause> rootCause = esException.response().error().rootCause();
            if (!rootCause.isEmpty()) {
                String reason = rootCause.get(0).reason();
                if (reason != null && reason.startsWith("Failed to parse query [")) {
                    return new QuerySyntaxException();
                }
            }
        }

        return originalException;
    }
}
