package io.github.bi.domain;

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

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationUserAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertApplicationUserAllPropertiesEquals(ApplicationUser expected, ApplicationUser actual) {
        assertApplicationUserAutoGeneratedPropertiesEquals(expected, actual);
        assertApplicationUserAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertApplicationUserAllUpdatablePropertiesEquals(ApplicationUser expected, ApplicationUser actual) {
        assertApplicationUserUpdatableFieldsEquals(expected, actual);
        assertApplicationUserUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertApplicationUserAutoGeneratedPropertiesEquals(ApplicationUser expected, ApplicationUser actual) {
        assertThat(actual)
            .as("Verify ApplicationUser auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertApplicationUserUpdatableFieldsEquals(ApplicationUser expected, ApplicationUser actual) {
        assertThat(actual)
            .as("Verify ApplicationUser relevant properties")
            .satisfies(a -> assertThat(a.getDesignation()).as("check designation").isEqualTo(expected.getDesignation()))
            .satisfies(a ->
                assertThat(a.getApplicationIdentity()).as("check applicationIdentity").isEqualTo(expected.getApplicationIdentity())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertApplicationUserUpdatableRelationshipsEquals(ApplicationUser expected, ApplicationUser actual) {
        assertThat(actual)
            .as("Verify ApplicationUser relationships")
            .satisfies(a -> assertThat(a.getOrganization()).as("check organization").isEqualTo(expected.getOrganization()))
            .satisfies(a -> assertThat(a.getDepartment()).as("check department").isEqualTo(expected.getDepartment()))
            .satisfies(a -> assertThat(a.getSecurityClearance()).as("check securityClearance").isEqualTo(expected.getSecurityClearance()))
            .satisfies(a -> assertThat(a.getDealerIdentity()).as("check dealerIdentity").isEqualTo(expected.getDealerIdentity()))
            .satisfies(a -> assertThat(a.getPlaceholders()).as("check placeholders").isEqualTo(expected.getPlaceholders()));
    }
}
