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

import static io.github.bi.domain.PlaceholderAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.FiscalMonth;
import io.github.bi.domain.FiscalQuarter;
import io.github.bi.domain.FiscalYear;
import io.github.bi.domain.MoneyMarketList;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.Placeholder;
import io.github.bi.repository.PlaceholderRepository;
import io.github.bi.repository.search.PlaceholderSearchRepository;
import io.github.bi.service.PlaceholderService;
import io.github.bi.service.dto.PlaceholderDTO;
import io.github.bi.service.mapper.PlaceholderMapper;
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
 * Integration tests for the {@link PlaceholderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlaceholderResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/placeholders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/placeholders/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlaceholderRepository placeholderRepository;

    @Mock
    private PlaceholderRepository placeholderRepositoryMock;

    @Autowired
    private PlaceholderMapper placeholderMapper;

    @Mock
    private PlaceholderService placeholderServiceMock;

    @Autowired
    private PlaceholderSearchRepository placeholderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaceholderMockMvc;

    private Placeholder placeholder;

    private Placeholder insertedPlaceholder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Placeholder createEntity() {
        return new Placeholder().description(DEFAULT_DESCRIPTION).token(DEFAULT_TOKEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Placeholder createUpdatedEntity() {
        return new Placeholder().description(UPDATED_DESCRIPTION).token(UPDATED_TOKEN);
    }

    @BeforeEach
    void initTest() {
        placeholder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlaceholder != null) {
            placeholderRepository.delete(insertedPlaceholder);
            placeholderSearchRepository.delete(insertedPlaceholder);
            insertedPlaceholder = null;
        }
    }

    @Test
    @Transactional
    void createPlaceholder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);
        var returnedPlaceholderDTO = om.readValue(
            restPlaceholderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeholderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlaceholderDTO.class
        );

        // Validate the Placeholder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlaceholder = placeholderMapper.toEntity(returnedPlaceholderDTO);
        assertPlaceholderUpdatableFieldsEquals(returnedPlaceholder, getPersistedPlaceholder(returnedPlaceholder));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPlaceholder = returnedPlaceholder;
    }

    @Test
    @Transactional
    void createPlaceholderWithExistingId() throws Exception {
        // Create the Placeholder with an existing ID
        placeholder.setId(1L);
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceholderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeholderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        // set the field null
        placeholder.setDescription(null);

        // Create the Placeholder, which fails.
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        restPlaceholderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeholderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPlaceholders() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeholder.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlaceholdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(placeholderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlaceholderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(placeholderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlaceholdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(placeholderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlaceholderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(placeholderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlaceholder() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get the placeholder
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL_ID, placeholder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(placeholder.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN));
    }

    @Test
    @Transactional
    void getPlaceholdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        Long id = placeholder.getId();

        defaultPlaceholderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPlaceholderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPlaceholderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description equals to
        defaultPlaceholderFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description in
        defaultPlaceholderFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description is not null
        defaultPlaceholderFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description contains
        defaultPlaceholderFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description does not contain
        defaultPlaceholderFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token equals to
        defaultPlaceholderFiltering("token.equals=" + DEFAULT_TOKEN, "token.equals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token in
        defaultPlaceholderFiltering("token.in=" + DEFAULT_TOKEN + "," + UPDATED_TOKEN, "token.in=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token is not null
        defaultPlaceholderFiltering("token.specified=true", "token.specified=false");
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token contains
        defaultPlaceholderFiltering("token.contains=" + DEFAULT_TOKEN, "token.contains=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token does not contain
        defaultPlaceholderFiltering("token.doesNotContain=" + UPDATED_TOKEN, "token.doesNotContain=" + DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByContainingPlaceholderIsEqualToSomething() throws Exception {
        Placeholder containingPlaceholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholderRepository.saveAndFlush(placeholder);
            containingPlaceholder = PlaceholderResourceIT.createEntity();
        } else {
            containingPlaceholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(containingPlaceholder);
        em.flush();
        placeholder.setContainingPlaceholder(containingPlaceholder);
        placeholderRepository.saveAndFlush(placeholder);
        Long containingPlaceholderId = containingPlaceholder.getId();
        // Get all the placeholderList where containingPlaceholder equals to containingPlaceholderId
        defaultPlaceholderShouldBeFound("containingPlaceholderId.equals=" + containingPlaceholderId);

        // Get all the placeholderList where containingPlaceholder equals to (containingPlaceholderId + 1)
        defaultPlaceholderShouldNotBeFound("containingPlaceholderId.equals=" + (containingPlaceholderId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFiscalYearIsEqualToSomething() throws Exception {
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            placeholderRepository.saveAndFlush(placeholder);
            fiscalYear = FiscalYearResourceIT.createEntity();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        em.persist(fiscalYear);
        em.flush();
        placeholder.addFiscalYear(fiscalYear);
        placeholderRepository.saveAndFlush(placeholder);
        Long fiscalYearId = fiscalYear.getId();
        // Get all the placeholderList where fiscalYear equals to fiscalYearId
        defaultPlaceholderShouldBeFound("fiscalYearId.equals=" + fiscalYearId);

        // Get all the placeholderList where fiscalYear equals to (fiscalYearId + 1)
        defaultPlaceholderShouldNotBeFound("fiscalYearId.equals=" + (fiscalYearId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFiscalQuarterIsEqualToSomething() throws Exception {
        FiscalQuarter fiscalQuarter;
        if (TestUtil.findAll(em, FiscalQuarter.class).isEmpty()) {
            placeholderRepository.saveAndFlush(placeholder);
            fiscalQuarter = FiscalQuarterResourceIT.createEntity(em);
        } else {
            fiscalQuarter = TestUtil.findAll(em, FiscalQuarter.class).get(0);
        }
        em.persist(fiscalQuarter);
        em.flush();
        placeholder.addFiscalQuarter(fiscalQuarter);
        placeholderRepository.saveAndFlush(placeholder);
        Long fiscalQuarterId = fiscalQuarter.getId();
        // Get all the placeholderList where fiscalQuarter equals to fiscalQuarterId
        defaultPlaceholderShouldBeFound("fiscalQuarterId.equals=" + fiscalQuarterId);

        // Get all the placeholderList where fiscalQuarter equals to (fiscalQuarterId + 1)
        defaultPlaceholderShouldNotBeFound("fiscalQuarterId.equals=" + (fiscalQuarterId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFiscalMonthIsEqualToSomething() throws Exception {
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            placeholderRepository.saveAndFlush(placeholder);
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(fiscalMonth);
        em.flush();
        placeholder.addFiscalMonth(fiscalMonth);
        placeholderRepository.saveAndFlush(placeholder);
        Long fiscalMonthId = fiscalMonth.getId();
        // Get all the placeholderList where fiscalMonth equals to fiscalMonthId
        defaultPlaceholderShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the placeholderList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultPlaceholderShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByMoneyMarketListIsEqualToSomething() throws Exception {
        MoneyMarketList moneyMarketList;
        if (TestUtil.findAll(em, MoneyMarketList.class).isEmpty()) {
            placeholderRepository.saveAndFlush(placeholder);
            moneyMarketList = MoneyMarketListResourceIT.createEntity();
        } else {
            moneyMarketList = TestUtil.findAll(em, MoneyMarketList.class).get(0);
        }
        em.persist(moneyMarketList);
        em.flush();
        placeholder.addMoneyMarketList(moneyMarketList);
        placeholderRepository.saveAndFlush(placeholder);
        Long moneyMarketListId = moneyMarketList.getId();
        // Get all the placeholderList where moneyMarketList equals to moneyMarketListId
        defaultPlaceholderShouldBeFound("moneyMarketListId.equals=" + moneyMarketListId);

        // Get all the placeholderList where moneyMarketList equals to (moneyMarketListId + 1)
        defaultPlaceholderShouldNotBeFound("moneyMarketListId.equals=" + (moneyMarketListId + 1));
    }

    private void defaultPlaceholderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPlaceholderShouldBeFound(shouldBeFound);
        defaultPlaceholderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlaceholderShouldBeFound(String filter) throws Exception {
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeholder.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));

        // Check, that the count call also returns 1
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlaceholderShouldNotBeFound(String filter) throws Exception {
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlaceholder() throws Exception {
        // Get the placeholder
        restPlaceholderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlaceholder() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        placeholderSearchRepository.save(placeholder);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());

        // Update the placeholder
        Placeholder updatedPlaceholder = placeholderRepository.findById(placeholder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlaceholder are not directly saved in db
        em.detach(updatedPlaceholder);
        updatedPlaceholder.description(UPDATED_DESCRIPTION).token(UPDATED_TOKEN);
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(updatedPlaceholder);

        restPlaceholderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeholderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(placeholderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlaceholderToMatchAllProperties(updatedPlaceholder);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Placeholder> placeholderSearchList = Streamable.of(placeholderSearchRepository.findAll()).toList();
                Placeholder testPlaceholderSearch = placeholderSearchList.get(searchDatabaseSizeAfter - 1);

                assertPlaceholderAllPropertiesEquals(testPlaceholderSearch, updatedPlaceholder);
            });
    }

    @Test
    @Transactional
    void putNonExistingPlaceholder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        placeholder.setId(longCount.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeholderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlaceholder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        placeholder.setId(longCount.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlaceholder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        placeholder.setId(longCount.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeholderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePlaceholderWithPatch() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the placeholder using partial update
        Placeholder partialUpdatedPlaceholder = new Placeholder();
        partialUpdatedPlaceholder.setId(placeholder.getId());

        partialUpdatedPlaceholder.token(UPDATED_TOKEN);

        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaceholder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaceholder))
            )
            .andExpect(status().isOk());

        // Validate the Placeholder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceholderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlaceholder, placeholder),
            getPersistedPlaceholder(placeholder)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlaceholderWithPatch() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the placeholder using partial update
        Placeholder partialUpdatedPlaceholder = new Placeholder();
        partialUpdatedPlaceholder.setId(placeholder.getId());

        partialUpdatedPlaceholder.description(UPDATED_DESCRIPTION).token(UPDATED_TOKEN);

        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaceholder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaceholder))
            )
            .andExpect(status().isOk());

        // Validate the Placeholder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceholderUpdatableFieldsEquals(partialUpdatedPlaceholder, getPersistedPlaceholder(partialUpdatedPlaceholder));
    }

    @Test
    @Transactional
    void patchNonExistingPlaceholder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        placeholder.setId(longCount.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, placeholderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlaceholder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        placeholder.setId(longCount.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlaceholder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        placeholder.setId(longCount.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(placeholderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Placeholder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePlaceholder() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);
        placeholderRepository.save(placeholder);
        placeholderSearchRepository.save(placeholder);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the placeholder
        restPlaceholderMockMvc
            .perform(delete(ENTITY_API_URL_ID, placeholder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPlaceholder() throws Exception {
        // Initialize the database
        insertedPlaceholder = placeholderRepository.saveAndFlush(placeholder);
        placeholderSearchRepository.save(placeholder);

        // Search the placeholder
        restPlaceholderMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + placeholder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeholder.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));
    }

    protected long getRepositoryCount() {
        return placeholderRepository.count();
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

    protected Placeholder getPersistedPlaceholder(Placeholder placeholder) {
        return placeholderRepository.findById(placeholder.getId()).orElseThrow();
    }

    protected void assertPersistedPlaceholderToMatchAllProperties(Placeholder expectedPlaceholder) {
        assertPlaceholderAllPropertiesEquals(expectedPlaceholder, getPersistedPlaceholder(expectedPlaceholder));
    }

    protected void assertPersistedPlaceholderToMatchUpdatableProperties(Placeholder expectedPlaceholder) {
        assertPlaceholderAllUpdatablePropertiesEquals(expectedPlaceholder, getPersistedPlaceholder(expectedPlaceholder));
    }
}
