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

import static io.github.bi.domain.FiscalYearAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.FiscalYear;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.enumeration.FiscalYearStatusType;
import io.github.bi.repository.FiscalYearRepository;
import io.github.bi.repository.search.FiscalYearSearchRepository;
import io.github.bi.service.FiscalYearService;
import io.github.bi.service.dto.FiscalYearDTO;
import io.github.bi.service.mapper.FiscalYearMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FiscalYearResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalYearResourceIT {

    private static final String DEFAULT_FISCAL_YEAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_YEAR_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final FiscalYearStatusType DEFAULT_FISCAL_YEAR_STATUS = FiscalYearStatusType.OPEN;
    private static final FiscalYearStatusType UPDATED_FISCAL_YEAR_STATUS = FiscalYearStatusType.CLOSED;

    private static final String ENTITY_API_URL = "/api/fiscal-years";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fiscal-years/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Mock
    private FiscalYearRepository fiscalYearRepositoryMock;

    @Autowired
    private FiscalYearMapper fiscalYearMapper;

    @Mock
    private FiscalYearService fiscalYearServiceMock;

    @Autowired
    private FiscalYearSearchRepository fiscalYearSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalYearMockMvc;

    private FiscalYear fiscalYear;

    private FiscalYear insertedFiscalYear;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalYear createEntity() {
        return new FiscalYear()
            .fiscalYearCode(DEFAULT_FISCAL_YEAR_CODE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalYearStatus(DEFAULT_FISCAL_YEAR_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalYear createUpdatedEntity() {
        return new FiscalYear()
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);
    }

    @BeforeEach
    void initTest() {
        fiscalYear = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFiscalYear != null) {
            fiscalYearRepository.delete(insertedFiscalYear);
            fiscalYearSearchRepository.delete(insertedFiscalYear);
            insertedFiscalYear = null;
        }
    }

    @Test
    @Transactional
    void createFiscalYear() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);
        var returnedFiscalYearDTO = om.readValue(
            restFiscalYearMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalYearDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FiscalYearDTO.class
        );

        // Validate the FiscalYear in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFiscalYear = fiscalYearMapper.toEntity(returnedFiscalYearDTO);
        assertFiscalYearUpdatableFieldsEquals(returnedFiscalYear, getPersistedFiscalYear(returnedFiscalYear));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFiscalYear = returnedFiscalYear;
    }

    @Test
    @Transactional
    void createFiscalYearWithExistingId() throws Exception {
        // Create the FiscalYear with an existing ID
        fiscalYear.setId(1L);
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFiscalYearCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        // set the field null
        fiscalYear.setFiscalYearCode(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        // set the field null
        fiscalYear.setStartDate(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        // set the field null
        fiscalYear.setEndDate(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFiscalYears() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalYearsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalYearServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalYearMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalYearServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalYearsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalYearServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalYearMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fiscalYearRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFiscalYear() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get the fiscalYear
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalYear.getId().intValue()))
            .andExpect(jsonPath("$.fiscalYearCode").value(DEFAULT_FISCAL_YEAR_CODE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalYearStatus").value(DEFAULT_FISCAL_YEAR_STATUS.toString()));
    }

    @Test
    @Transactional
    void getFiscalYearsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        Long id = fiscalYear.getId();

        defaultFiscalYearFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFiscalYearFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFiscalYearFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode equals to
        defaultFiscalYearFiltering(
            "fiscalYearCode.equals=" + DEFAULT_FISCAL_YEAR_CODE,
            "fiscalYearCode.equals=" + UPDATED_FISCAL_YEAR_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode in
        defaultFiscalYearFiltering(
            "fiscalYearCode.in=" + DEFAULT_FISCAL_YEAR_CODE + "," + UPDATED_FISCAL_YEAR_CODE,
            "fiscalYearCode.in=" + UPDATED_FISCAL_YEAR_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode is not null
        defaultFiscalYearFiltering("fiscalYearCode.specified=true", "fiscalYearCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode contains
        defaultFiscalYearFiltering(
            "fiscalYearCode.contains=" + DEFAULT_FISCAL_YEAR_CODE,
            "fiscalYearCode.contains=" + UPDATED_FISCAL_YEAR_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode does not contain
        defaultFiscalYearFiltering(
            "fiscalYearCode.doesNotContain=" + UPDATED_FISCAL_YEAR_CODE,
            "fiscalYearCode.doesNotContain=" + DEFAULT_FISCAL_YEAR_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate equals to
        defaultFiscalYearFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate in
        defaultFiscalYearFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is not null
        defaultFiscalYearFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is greater than or equal to
        defaultFiscalYearFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is less than or equal to
        defaultFiscalYearFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is less than
        defaultFiscalYearFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is greater than
        defaultFiscalYearFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate equals to
        defaultFiscalYearFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate in
        defaultFiscalYearFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is not null
        defaultFiscalYearFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is greater than or equal to
        defaultFiscalYearFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is less than or equal to
        defaultFiscalYearFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is less than
        defaultFiscalYearFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is greater than
        defaultFiscalYearFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearStatus equals to
        defaultFiscalYearFiltering(
            "fiscalYearStatus.equals=" + DEFAULT_FISCAL_YEAR_STATUS,
            "fiscalYearStatus.equals=" + UPDATED_FISCAL_YEAR_STATUS
        );
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearStatus in
        defaultFiscalYearFiltering(
            "fiscalYearStatus.in=" + DEFAULT_FISCAL_YEAR_STATUS + "," + UPDATED_FISCAL_YEAR_STATUS,
            "fiscalYearStatus.in=" + UPDATED_FISCAL_YEAR_STATUS
        );
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearStatus is not null
        defaultFiscalYearFiltering("fiscalYearStatus.specified=true", "fiscalYearStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            fiscalYearRepository.saveAndFlush(fiscalYear);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        fiscalYear.addPlaceholder(placeholder);
        fiscalYearRepository.saveAndFlush(fiscalYear);
        Long placeholderId = placeholder.getId();
        // Get all the fiscalYearList where placeholder equals to placeholderId
        defaultFiscalYearShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fiscalYearList where placeholder equals to (placeholderId + 1)
        defaultFiscalYearShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultFiscalYearFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFiscalYearShouldBeFound(shouldBeFound);
        defaultFiscalYearShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiscalYearShouldBeFound(String filter) throws Exception {
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));

        // Check, that the count call also returns 1
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiscalYearShouldNotBeFound(String filter) throws Exception {
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFiscalYear() throws Exception {
        // Get the fiscalYear
        restFiscalYearMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiscalYear() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        fiscalYearSearchRepository.save(fiscalYear);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());

        // Update the fiscalYear
        FiscalYear updatedFiscalYear = fiscalYearRepository.findById(fiscalYear.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFiscalYear are not directly saved in db
        em.detach(updatedFiscalYear);
        updatedFiscalYear
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(updatedFiscalYear);

        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalYearDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFiscalYearToMatchAllProperties(updatedFiscalYear);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FiscalYear> fiscalYearSearchList = Streamable.of(fiscalYearSearchRepository.findAll()).toList();
                FiscalYear testFiscalYearSearch = fiscalYearSearchList.get(searchDatabaseSizeAfter - 1);

                assertFiscalYearAllPropertiesEquals(testFiscalYearSearch, updatedFiscalYear);
            });
    }

    @Test
    @Transactional
    void putNonExistingFiscalYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        fiscalYear.setId(longCount.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        fiscalYear.setId(longCount.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        fiscalYear.setId(longCount.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalYearDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFiscalYearWithPatch() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fiscalYear using partial update
        FiscalYear partialUpdatedFiscalYear = new FiscalYear();
        partialUpdatedFiscalYear.setId(fiscalYear.getId());

        partialUpdatedFiscalYear
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);

        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiscalYear))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiscalYearUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFiscalYear, fiscalYear),
            getPersistedFiscalYear(fiscalYear)
        );
    }

    @Test
    @Transactional
    void fullUpdateFiscalYearWithPatch() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fiscalYear using partial update
        FiscalYear partialUpdatedFiscalYear = new FiscalYear();
        partialUpdatedFiscalYear.setId(fiscalYear.getId());

        partialUpdatedFiscalYear
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);

        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiscalYear))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiscalYearUpdatableFieldsEquals(partialUpdatedFiscalYear, getPersistedFiscalYear(partialUpdatedFiscalYear));
    }

    @Test
    @Transactional
    void patchNonExistingFiscalYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        fiscalYear.setId(longCount.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        fiscalYear.setId(longCount.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        fiscalYear.setId(longCount.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fiscalYearDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFiscalYear() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);
        fiscalYearRepository.save(fiscalYear);
        fiscalYearSearchRepository.save(fiscalYear);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the fiscalYear
        restFiscalYearMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalYear.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalYearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFiscalYear() throws Exception {
        // Initialize the database
        insertedFiscalYear = fiscalYearRepository.saveAndFlush(fiscalYear);
        fiscalYearSearchRepository.save(fiscalYear);

        // Search the fiscalYear
        restFiscalYearMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return fiscalYearRepository.count();
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

    protected FiscalYear getPersistedFiscalYear(FiscalYear fiscalYear) {
        return fiscalYearRepository.findById(fiscalYear.getId()).orElseThrow();
    }

    protected void assertPersistedFiscalYearToMatchAllProperties(FiscalYear expectedFiscalYear) {
        assertFiscalYearAllPropertiesEquals(expectedFiscalYear, getPersistedFiscalYear(expectedFiscalYear));
    }

    protected void assertPersistedFiscalYearToMatchUpdatableProperties(FiscalYear expectedFiscalYear) {
        assertFiscalYearAllUpdatablePropertiesEquals(expectedFiscalYear, getPersistedFiscalYear(expectedFiscalYear));
    }
}
