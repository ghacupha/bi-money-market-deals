package io.github.bi.web.rest;

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

import static io.github.bi.domain.SecurityClearanceAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.SecurityClearance;
import io.github.bi.repository.SecurityClearanceRepository;
import io.github.bi.repository.search.SecurityClearanceSearchRepository;
import io.github.bi.service.SecurityClearanceService;
import io.github.bi.service.dto.SecurityClearanceDTO;
import io.github.bi.service.mapper.SecurityClearanceMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SecurityClearanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SecurityClearanceResourceIT {

    private static final String DEFAULT_CLEARANCE_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_CLEARANCE_LEVEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;
    private static final Integer SMALLER_LEVEL = 1 - 1;

    private static final String ENTITY_API_URL = "/api/security-clearances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/security-clearances/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SecurityClearanceRepository securityClearanceRepository;

    @Mock
    private SecurityClearanceRepository securityClearanceRepositoryMock;

    @Autowired
    private SecurityClearanceMapper securityClearanceMapper;

    @Mock
    private SecurityClearanceService securityClearanceServiceMock;

    @Autowired
    private SecurityClearanceSearchRepository securityClearanceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityClearanceMockMvc;

    private SecurityClearance securityClearance;

    private SecurityClearance insertedSecurityClearance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClearance createEntity() {
        return new SecurityClearance().clearanceLevel(DEFAULT_CLEARANCE_LEVEL).level(DEFAULT_LEVEL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClearance createUpdatedEntity() {
        return new SecurityClearance().clearanceLevel(UPDATED_CLEARANCE_LEVEL).level(UPDATED_LEVEL);
    }

    @BeforeEach
    void initTest() {
        securityClearance = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSecurityClearance != null) {
            securityClearanceRepository.delete(insertedSecurityClearance);
            securityClearanceSearchRepository.delete(insertedSecurityClearance);
            insertedSecurityClearance = null;
        }
    }

    @Test
    @Transactional
    void createSecurityClearance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);
        var returnedSecurityClearanceDTO = om.readValue(
            restSecurityClearanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityClearanceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SecurityClearanceDTO.class
        );

        // Validate the SecurityClearance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSecurityClearance = securityClearanceMapper.toEntity(returnedSecurityClearanceDTO);
        assertSecurityClearanceUpdatableFieldsEquals(returnedSecurityClearance, getPersistedSecurityClearance(returnedSecurityClearance));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSecurityClearance = returnedSecurityClearance;
    }

    @Test
    @Transactional
    void createSecurityClearanceWithExistingId() throws Exception {
        // Create the SecurityClearance with an existing ID
        securityClearance.setId(1L);
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityClearanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkClearanceLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        // set the field null
        securityClearance.setClearanceLevel(null);

        // Create the SecurityClearance, which fails.
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        restSecurityClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityClearanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        // set the field null
        securityClearance.setLevel(null);

        // Create the SecurityClearance, which fails.
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        restSecurityClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityClearanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSecurityClearances() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityClearancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(securityClearanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(securityClearanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityClearancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(securityClearanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(securityClearanceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSecurityClearance() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get the securityClearance
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL_ID, securityClearance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityClearance.getId().intValue()))
            .andExpect(jsonPath("$.clearanceLevel").value(DEFAULT_CLEARANCE_LEVEL))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    void getSecurityClearancesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        Long id = securityClearance.getId();

        defaultSecurityClearanceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSecurityClearanceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSecurityClearanceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel equals to
        defaultSecurityClearanceFiltering(
            "clearanceLevel.equals=" + DEFAULT_CLEARANCE_LEVEL,
            "clearanceLevel.equals=" + UPDATED_CLEARANCE_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel in
        defaultSecurityClearanceFiltering(
            "clearanceLevel.in=" + DEFAULT_CLEARANCE_LEVEL + "," + UPDATED_CLEARANCE_LEVEL,
            "clearanceLevel.in=" + UPDATED_CLEARANCE_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel is not null
        defaultSecurityClearanceFiltering("clearanceLevel.specified=true", "clearanceLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelContainsSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel contains
        defaultSecurityClearanceFiltering(
            "clearanceLevel.contains=" + DEFAULT_CLEARANCE_LEVEL,
            "clearanceLevel.contains=" + UPDATED_CLEARANCE_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel does not contain
        defaultSecurityClearanceFiltering(
            "clearanceLevel.doesNotContain=" + UPDATED_CLEARANCE_LEVEL,
            "clearanceLevel.doesNotContain=" + DEFAULT_CLEARANCE_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where level equals to
        defaultSecurityClearanceFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where level in
        defaultSecurityClearanceFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where level is not null
        defaultSecurityClearanceFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where level is greater than or equal to
        defaultSecurityClearanceFiltering("level.greaterThanOrEqual=" + DEFAULT_LEVEL, "level.greaterThanOrEqual=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where level is less than or equal to
        defaultSecurityClearanceFiltering("level.lessThanOrEqual=" + DEFAULT_LEVEL, "level.lessThanOrEqual=" + SMALLER_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where level is less than
        defaultSecurityClearanceFiltering("level.lessThan=" + UPDATED_LEVEL, "level.lessThan=" + DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where level is greater than
        defaultSecurityClearanceFiltering("level.greaterThan=" + SMALLER_LEVEL, "level.greaterThan=" + DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            securityClearanceRepository.saveAndFlush(securityClearance);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        securityClearance.addPlaceholder(placeholder);
        securityClearanceRepository.saveAndFlush(securityClearance);
        Long placeholderId = placeholder.getId();
        // Get all the securityClearanceList where placeholder equals to placeholderId
        defaultSecurityClearanceShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the securityClearanceList where placeholder equals to (placeholderId + 1)
        defaultSecurityClearanceShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultSecurityClearanceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSecurityClearanceShouldBeFound(shouldBeFound);
        defaultSecurityClearanceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSecurityClearanceShouldBeFound(String filter) throws Exception {
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));

        // Check, that the count call also returns 1
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSecurityClearanceShouldNotBeFound(String filter) throws Exception {
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSecurityClearance() throws Exception {
        // Get the securityClearance
        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSecurityClearance() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        securityClearanceSearchRepository.save(securityClearance);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());

        // Update the securityClearance
        SecurityClearance updatedSecurityClearance = securityClearanceRepository.findById(securityClearance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSecurityClearance are not directly saved in db
        em.detach(updatedSecurityClearance);
        updatedSecurityClearance.clearanceLevel(UPDATED_CLEARANCE_LEVEL).level(UPDATED_LEVEL);
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(updatedSecurityClearance);

        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(securityClearanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSecurityClearanceToMatchAllProperties(updatedSecurityClearance);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SecurityClearance> securityClearanceSearchList = Streamable.of(securityClearanceSearchRepository.findAll()).toList();
                SecurityClearance testSecurityClearanceSearch = securityClearanceSearchList.get(searchDatabaseSizeAfter - 1);

                assertSecurityClearanceAllPropertiesEquals(testSecurityClearanceSearch, updatedSecurityClearance);
            });
    }

    @Test
    @Transactional
    void putNonExistingSecurityClearance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        securityClearance.setId(longCount.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityClearance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        securityClearance.setId(longCount.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityClearance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        securityClearance.setId(longCount.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityClearanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSecurityClearanceWithPatch() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the securityClearance using partial update
        SecurityClearance partialUpdatedSecurityClearance = new SecurityClearance();
        partialUpdatedSecurityClearance.setId(securityClearance.getId());

        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSecurityClearance))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSecurityClearanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSecurityClearance, securityClearance),
            getPersistedSecurityClearance(securityClearance)
        );
    }

    @Test
    @Transactional
    void fullUpdateSecurityClearanceWithPatch() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the securityClearance using partial update
        SecurityClearance partialUpdatedSecurityClearance = new SecurityClearance();
        partialUpdatedSecurityClearance.setId(securityClearance.getId());

        partialUpdatedSecurityClearance.clearanceLevel(UPDATED_CLEARANCE_LEVEL).level(UPDATED_LEVEL);

        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSecurityClearance))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSecurityClearanceUpdatableFieldsEquals(
            partialUpdatedSecurityClearance,
            getPersistedSecurityClearance(partialUpdatedSecurityClearance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSecurityClearance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        securityClearance.setId(longCount.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityClearance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        securityClearance.setId(longCount.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityClearance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        securityClearance.setId(longCount.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(securityClearanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClearance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSecurityClearance() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);
        securityClearanceRepository.save(securityClearance);
        securityClearanceSearchRepository.save(securityClearance);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the securityClearance
        restSecurityClearanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityClearance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(securityClearanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSecurityClearance() throws Exception {
        // Initialize the database
        insertedSecurityClearance = securityClearanceRepository.saveAndFlush(securityClearance);
        securityClearanceSearchRepository.save(securityClearance);

        // Search the securityClearance
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + securityClearance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    protected long getRepositoryCount() {
        return securityClearanceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SecurityClearance getPersistedSecurityClearance(SecurityClearance securityClearance) {
        return securityClearanceRepository.findById(securityClearance.getId()).orElseThrow();
    }

    protected void assertPersistedSecurityClearanceToMatchAllProperties(SecurityClearance expectedSecurityClearance) {
        assertSecurityClearanceAllPropertiesEquals(expectedSecurityClearance, getPersistedSecurityClearance(expectedSecurityClearance));
    }

    protected void assertPersistedSecurityClearanceToMatchUpdatableProperties(SecurityClearance expectedSecurityClearance) {
        assertSecurityClearanceAllUpdatablePropertiesEquals(
            expectedSecurityClearance,
            getPersistedSecurityClearance(expectedSecurityClearance)
        );
    }
}
