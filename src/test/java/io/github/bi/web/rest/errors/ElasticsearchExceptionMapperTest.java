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

import static org.junit.jupiter.api.Assertions.*;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch._types.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;

class ElasticsearchExceptionMapperTest {

    @Test
    void testMapException() {
        ErrorCause rootCause = new ErrorCause.Builder().reason("Failed to parse query [").build();
        ErrorResponse response = new ErrorResponse.Builder()
            .error(new ErrorCause.Builder().reason("").rootCause(rootCause).build())
            .status(400)
            .build();
        ElasticsearchException esException = new ElasticsearchException("", response);
        UncategorizedElasticsearchException cause = new UncategorizedElasticsearchException("", esException);
        assertInstanceOf(
            QuerySyntaxException.class,
            ElasticsearchExceptionMapper.mapException(new UncategorizedElasticsearchException("", cause))
        );
    }
}
