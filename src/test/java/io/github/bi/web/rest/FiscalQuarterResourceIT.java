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

import static io.github.bi.domain.FiscalQuarterAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.FiscalQuarter;
import io.github.bi.domain.FiscalYear;
import io.github.bi.domain.Placeholder;
import io.github.bi.repository.FiscalQuarterRepository;
import io.github.bi.repository.search.FiscalQuarterSearchRepository;
import io.github.bi.service.FiscalQuarterService;
import io.github.bi.service.dto.FiscalQuarterDTO;
import io.github.bi.service.mapper.FiscalQuarterMapper;
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
 * Integration tests for the {@link FiscalQuarterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalQuarterResourceIT {

    private static final Integer DEFAULT_QUARTER_NUMBER = 1;
    private static final Integer UPDATED_QUARTER_NUMBER = 2;
    private static final Integer SMALLER_QUARTER_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FISCAL_QUARTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_QUARTER_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fiscal-quarters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fiscal-quarters/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FiscalQuarterRepository fiscalQuarterRepository;

    @Mock
    private FiscalQuarterRepository fiscalQuarterRepositoryMock;

    @Autowired
    private FiscalQuarterMapper fiscalQuarterMapper;

    @Mock
    private FiscalQuarterService fiscalQuarterServiceMock;

    @Autowired
    private FiscalQuarterSearchRepository fiscalQuarterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalQuarterMockMvc;

    private FiscalQuarter fiscalQuarter;

    private FiscalQuarter insertedFiscalQuarter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalQuarter createEntity(EntityManager em) {
        FiscalQuarter fiscalQuarter = new FiscalQuarter()
            .quarterNumber(DEFAULT_QUARTER_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalQuarterCode(DEFAULT_FISCAL_QUARTER_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity();
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalQuarter.setFiscalYear(fiscalYear);
        return fiscalQuarter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalQuarter createUpdatedEntity(EntityManager em) {
        FiscalQuarter updatedFiscalQuarter = new FiscalQuarter()
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity();
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        updatedFiscalQuarter.setFiscalYear(fiscalYear);
        return updatedFiscalQuarter;
    }

    @BeforeEach
    void initTest() {
        fiscalQuarter = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFiscalQuarter != null) {
            fiscalQuarterRepository.delete(insertedFiscalQuarter);
            fiscalQuarterSearchRepository.delete(insertedFiscalQuarter);
            insertedFiscalQuarter = null;
        }
    }

    @Test
    @Transactional
    void createFiscalQuarter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);
        var returnedFiscalQuarterDTO = om.readValue(
            restFiscalQuarterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalQuarterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FiscalQuarterDTO.class
        );

        // Validate the FiscalQuarter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFiscalQuarter = fiscalQuarterMapper.toEntity(returnedFiscalQuarterDTO);
        assertFiscalQuarterUpdatableFieldsEquals(returnedFiscalQuarter, getPersistedFiscalQuarter(returnedFiscalQuarter));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFiscalQuarter = returnedFiscalQuarter;
    }

    @Test
    @Transactional
    void createFiscalQuarterWithExistingId() throws Exception {
        // Create the FiscalQuarter with an existing ID
        fiscalQuarter.setId(1L);
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalQuarterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalQuarterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkQuarterNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        // set the field null
        fiscalQuarter.setQuarterNumber(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalQuarterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        // set the field null
        fiscalQuarter.setStartDate(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalQuarterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        // set the field null
        fiscalQuarter.setEndDate(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalQuarterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFiscalQuarterCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        // set the field null
        fiscalQuarter.setFiscalQuarterCode(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalQuarterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFiscalQuarters() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalQuartersWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalQuarterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalQuarterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalQuartersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalQuarterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fiscalQuarterRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFiscalQuarter() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalQuarter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalQuarter.getId().intValue()))
            .andExpect(jsonPath("$.quarterNumber").value(DEFAULT_QUARTER_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalQuarterCode").value(DEFAULT_FISCAL_QUARTER_CODE));
    }

    @Test
    @Transactional
    void getFiscalQuartersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        Long id = fiscalQuarter.getId();

        defaultFiscalQuarterFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFiscalQuarterFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFiscalQuarterFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber equals to
        defaultFiscalQuarterFiltering("quarterNumber.equals=" + DEFAULT_QUARTER_NUMBER, "quarterNumber.equals=" + UPDATED_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber in
        defaultFiscalQuarterFiltering(
            "quarterNumber.in=" + DEFAULT_QUARTER_NUMBER + "," + UPDATED_QUARTER_NUMBER,
            "quarterNumber.in=" + UPDATED_QUARTER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is not null
        defaultFiscalQuarterFiltering("quarterNumber.specified=true", "quarterNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is greater than or equal to
        defaultFiscalQuarterFiltering(
            "quarterNumber.greaterThanOrEqual=" + DEFAULT_QUARTER_NUMBER,
            "quarterNumber.greaterThanOrEqual=" + UPDATED_QUARTER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is less than or equal to
        defaultFiscalQuarterFiltering(
            "quarterNumber.lessThanOrEqual=" + DEFAULT_QUARTER_NUMBER,
            "quarterNumber.lessThanOrEqual=" + SMALLER_QUARTER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is less than
        defaultFiscalQuarterFiltering(
            "quarterNumber.lessThan=" + UPDATED_QUARTER_NUMBER,
            "quarterNumber.lessThan=" + DEFAULT_QUARTER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is greater than
        defaultFiscalQuarterFiltering(
            "quarterNumber.greaterThan=" + SMALLER_QUARTER_NUMBER,
            "quarterNumber.greaterThan=" + DEFAULT_QUARTER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate equals to
        defaultFiscalQuarterFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate in
        defaultFiscalQuarterFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is not null
        defaultFiscalQuarterFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is greater than or equal to
        defaultFiscalQuarterFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is less than or equal to
        defaultFiscalQuarterFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is less than
        defaultFiscalQuarterFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is greater than
        defaultFiscalQuarterFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate equals to
        defaultFiscalQuarterFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate in
        defaultFiscalQuarterFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is not null
        defaultFiscalQuarterFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is greater than or equal to
        defaultFiscalQuarterFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is less than or equal to
        defaultFiscalQuarterFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is less than
        defaultFiscalQuarterFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is greater than
        defaultFiscalQuarterFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode equals to
        defaultFiscalQuarterFiltering(
            "fiscalQuarterCode.equals=" + DEFAULT_FISCAL_QUARTER_CODE,
            "fiscalQuarterCode.equals=" + UPDATED_FISCAL_QUARTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode in
        defaultFiscalQuarterFiltering(
            "fiscalQuarterCode.in=" + DEFAULT_FISCAL_QUARTER_CODE + "," + UPDATED_FISCAL_QUARTER_CODE,
            "fiscalQuarterCode.in=" + UPDATED_FISCAL_QUARTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode is not null
        defaultFiscalQuarterFiltering("fiscalQuarterCode.specified=true", "fiscalQuarterCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode contains
        defaultFiscalQuarterFiltering(
            "fiscalQuarterCode.contains=" + DEFAULT_FISCAL_QUARTER_CODE,
            "fiscalQuarterCode.contains=" + UPDATED_FISCAL_QUARTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode does not contain
        defaultFiscalQuarterFiltering(
            "fiscalQuarterCode.doesNotContain=" + UPDATED_FISCAL_QUARTER_CODE,
            "fiscalQuarterCode.doesNotContain=" + DEFAULT_FISCAL_QUARTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalYearIsEqualToSomething() throws Exception {
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
            fiscalYear = FiscalYearResourceIT.createEntity();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        em.persist(fiscalYear);
        em.flush();
        fiscalQuarter.setFiscalYear(fiscalYear);
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        Long fiscalYearId = fiscalYear.getId();
        // Get all the fiscalQuarterList where fiscalYear equals to fiscalYearId
        defaultFiscalQuarterShouldBeFound("fiscalYearId.equals=" + fiscalYearId);

        // Get all the fiscalQuarterList where fiscalYear equals to (fiscalYearId + 1)
        defaultFiscalQuarterShouldNotBeFound("fiscalYearId.equals=" + (fiscalYearId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        fiscalQuarter.addPlaceholder(placeholder);
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        Long placeholderId = placeholder.getId();
        // Get all the fiscalQuarterList where placeholder equals to placeholderId
        defaultFiscalQuarterShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fiscalQuarterList where placeholder equals to (placeholderId + 1)
        defaultFiscalQuarterShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultFiscalQuarterFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFiscalQuarterShouldBeFound(shouldBeFound);
        defaultFiscalQuarterShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiscalQuarterShouldBeFound(String filter) throws Exception {
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));

        // Check, that the count call also returns 1
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiscalQuarterShouldNotBeFound(String filter) throws Exception {
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFiscalQuarter() throws Exception {
        // Get the fiscalQuarter
        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiscalQuarter() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        fiscalQuarterSearchRepository.save(fiscalQuarter);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());

        // Update the fiscalQuarter
        FiscalQuarter updatedFiscalQuarter = fiscalQuarterRepository.findById(fiscalQuarter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFiscalQuarter are not directly saved in db
        em.detach(updatedFiscalQuarter);
        updatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(updatedFiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFiscalQuarterToMatchAllProperties(updatedFiscalQuarter);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FiscalQuarter> fiscalQuarterSearchList = Streamable.of(fiscalQuarterSearchRepository.findAll()).toList();
                FiscalQuarter testFiscalQuarterSearch = fiscalQuarterSearchList.get(searchDatabaseSizeAfter - 1);

                assertFiscalQuarterAllPropertiesEquals(testFiscalQuarterSearch, updatedFiscalQuarter);
            });
    }

    @Test
    @Transactional
    void putNonExistingFiscalQuarter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        fiscalQuarter.setId(longCount.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalQuarter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        fiscalQuarter.setId(longCount.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalQuarter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        fiscalQuarter.setId(longCount.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalQuarterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFiscalQuarterWithPatch() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fiscalQuarter using partial update
        FiscalQuarter partialUpdatedFiscalQuarter = new FiscalQuarter();
        partialUpdatedFiscalQuarter.setId(fiscalQuarter.getId());

        partialUpdatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);

        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalQuarter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiscalQuarter))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiscalQuarterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFiscalQuarter, fiscalQuarter),
            getPersistedFiscalQuarter(fiscalQuarter)
        );
    }

    @Test
    @Transactional
    void fullUpdateFiscalQuarterWithPatch() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fiscalQuarter using partial update
        FiscalQuarter partialUpdatedFiscalQuarter = new FiscalQuarter();
        partialUpdatedFiscalQuarter.setId(fiscalQuarter.getId());

        partialUpdatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);

        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalQuarter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiscalQuarter))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiscalQuarterUpdatableFieldsEquals(partialUpdatedFiscalQuarter, getPersistedFiscalQuarter(partialUpdatedFiscalQuarter));
    }

    @Test
    @Transactional
    void patchNonExistingFiscalQuarter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        fiscalQuarter.setId(longCount.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalQuarter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        fiscalQuarter.setId(longCount.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalQuarter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        fiscalQuarter.setId(longCount.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fiscalQuarterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalQuarter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFiscalQuarter() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        fiscalQuarterRepository.save(fiscalQuarter);
        fiscalQuarterSearchRepository.save(fiscalQuarter);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalQuarter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalQuarterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFiscalQuarter() throws Exception {
        // Initialize the database
        insertedFiscalQuarter = fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        fiscalQuarterSearchRepository.save(fiscalQuarter);

        // Search the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalQuarter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));
    }

    protected long getRepositoryCount() {
        return fiscalQuarterRepository.count();
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

    protected FiscalQuarter getPersistedFiscalQuarter(FiscalQuarter fiscalQuarter) {
        return fiscalQuarterRepository.findById(fiscalQuarter.getId()).orElseThrow();
    }

    protected void assertPersistedFiscalQuarterToMatchAllProperties(FiscalQuarter expectedFiscalQuarter) {
        assertFiscalQuarterAllPropertiesEquals(expectedFiscalQuarter, getPersistedFiscalQuarter(expectedFiscalQuarter));
    }

    protected void assertPersistedFiscalQuarterToMatchUpdatableProperties(FiscalQuarter expectedFiscalQuarter) {
        assertFiscalQuarterAllUpdatablePropertiesEquals(expectedFiscalQuarter, getPersistedFiscalQuarter(expectedFiscalQuarter));
    }
}
