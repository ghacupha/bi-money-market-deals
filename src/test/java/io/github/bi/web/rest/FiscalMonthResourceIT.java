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

import static io.github.bi.domain.FiscalMonthAsserts.*;
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
import io.github.bi.domain.Placeholder;
import io.github.bi.repository.FiscalMonthRepository;
import io.github.bi.repository.search.FiscalMonthSearchRepository;
import io.github.bi.service.FiscalMonthService;
import io.github.bi.service.dto.FiscalMonthDTO;
import io.github.bi.service.mapper.FiscalMonthMapper;
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
 * Integration tests for the {@link FiscalMonthResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalMonthResourceIT {

    private static final Integer DEFAULT_MONTH_NUMBER = 1;
    private static final Integer UPDATED_MONTH_NUMBER = 2;
    private static final Integer SMALLER_MONTH_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FISCAL_MONTH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_MONTH_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fiscal-months";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fiscal-months/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FiscalMonthRepository fiscalMonthRepository;

    @Mock
    private FiscalMonthRepository fiscalMonthRepositoryMock;

    @Autowired
    private FiscalMonthMapper fiscalMonthMapper;

    @Mock
    private FiscalMonthService fiscalMonthServiceMock;

    @Autowired
    private FiscalMonthSearchRepository fiscalMonthSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalMonthMockMvc;

    private FiscalMonth fiscalMonth;

    private FiscalMonth insertedFiscalMonth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalMonth createEntity(EntityManager em) {
        FiscalMonth fiscalMonth = new FiscalMonth()
            .monthNumber(DEFAULT_MONTH_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalMonthCode(DEFAULT_FISCAL_MONTH_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity();
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalMonth.setFiscalYear(fiscalYear);
        return fiscalMonth;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalMonth createUpdatedEntity(EntityManager em) {
        FiscalMonth updatedFiscalMonth = new FiscalMonth()
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity();
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        updatedFiscalMonth.setFiscalYear(fiscalYear);
        return updatedFiscalMonth;
    }

    @BeforeEach
    void initTest() {
        fiscalMonth = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFiscalMonth != null) {
            fiscalMonthRepository.delete(insertedFiscalMonth);
            fiscalMonthSearchRepository.delete(insertedFiscalMonth);
            insertedFiscalMonth = null;
        }
    }

    @Test
    @Transactional
    void createFiscalMonth() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);
        var returnedFiscalMonthDTO = om.readValue(
            restFiscalMonthMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalMonthDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FiscalMonthDTO.class
        );

        // Validate the FiscalMonth in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFiscalMonth = fiscalMonthMapper.toEntity(returnedFiscalMonthDTO);
        assertFiscalMonthUpdatableFieldsEquals(returnedFiscalMonth, getPersistedFiscalMonth(returnedFiscalMonth));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFiscalMonth = returnedFiscalMonth;
    }

    @Test
    @Transactional
    void createFiscalMonthWithExistingId() throws Exception {
        // Create the FiscalMonth with an existing ID
        fiscalMonth.setId(1L);
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalMonthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalMonthDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMonthNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        // set the field null
        fiscalMonth.setMonthNumber(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalMonthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        // set the field null
        fiscalMonth.setStartDate(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalMonthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        // set the field null
        fiscalMonth.setEndDate(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalMonthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFiscalMonthCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        // set the field null
        fiscalMonth.setFiscalMonthCode(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalMonthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFiscalMonths() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalMonthsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalMonthServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalMonthServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalMonthsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalMonthServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fiscalMonthRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFiscalMonth() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get the fiscalMonth
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalMonth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalMonth.getId().intValue()))
            .andExpect(jsonPath("$.monthNumber").value(DEFAULT_MONTH_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalMonthCode").value(DEFAULT_FISCAL_MONTH_CODE));
    }

    @Test
    @Transactional
    void getFiscalMonthsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        Long id = fiscalMonth.getId();

        defaultFiscalMonthFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFiscalMonthFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFiscalMonthFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber equals to
        defaultFiscalMonthFiltering("monthNumber.equals=" + DEFAULT_MONTH_NUMBER, "monthNumber.equals=" + UPDATED_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber in
        defaultFiscalMonthFiltering(
            "monthNumber.in=" + DEFAULT_MONTH_NUMBER + "," + UPDATED_MONTH_NUMBER,
            "monthNumber.in=" + UPDATED_MONTH_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is not null
        defaultFiscalMonthFiltering("monthNumber.specified=true", "monthNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is greater than or equal to
        defaultFiscalMonthFiltering(
            "monthNumber.greaterThanOrEqual=" + DEFAULT_MONTH_NUMBER,
            "monthNumber.greaterThanOrEqual=" + UPDATED_MONTH_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is less than or equal to
        defaultFiscalMonthFiltering(
            "monthNumber.lessThanOrEqual=" + DEFAULT_MONTH_NUMBER,
            "monthNumber.lessThanOrEqual=" + SMALLER_MONTH_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is less than
        defaultFiscalMonthFiltering("monthNumber.lessThan=" + UPDATED_MONTH_NUMBER, "monthNumber.lessThan=" + DEFAULT_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is greater than
        defaultFiscalMonthFiltering("monthNumber.greaterThan=" + SMALLER_MONTH_NUMBER, "monthNumber.greaterThan=" + DEFAULT_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate equals to
        defaultFiscalMonthFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate in
        defaultFiscalMonthFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is not null
        defaultFiscalMonthFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is greater than or equal to
        defaultFiscalMonthFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is less than or equal to
        defaultFiscalMonthFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is less than
        defaultFiscalMonthFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is greater than
        defaultFiscalMonthFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate equals to
        defaultFiscalMonthFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate in
        defaultFiscalMonthFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is not null
        defaultFiscalMonthFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is greater than or equal to
        defaultFiscalMonthFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is less than or equal to
        defaultFiscalMonthFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is less than
        defaultFiscalMonthFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is greater than
        defaultFiscalMonthFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode equals to
        defaultFiscalMonthFiltering(
            "fiscalMonthCode.equals=" + DEFAULT_FISCAL_MONTH_CODE,
            "fiscalMonthCode.equals=" + UPDATED_FISCAL_MONTH_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode in
        defaultFiscalMonthFiltering(
            "fiscalMonthCode.in=" + DEFAULT_FISCAL_MONTH_CODE + "," + UPDATED_FISCAL_MONTH_CODE,
            "fiscalMonthCode.in=" + UPDATED_FISCAL_MONTH_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode is not null
        defaultFiscalMonthFiltering("fiscalMonthCode.specified=true", "fiscalMonthCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode contains
        defaultFiscalMonthFiltering(
            "fiscalMonthCode.contains=" + DEFAULT_FISCAL_MONTH_CODE,
            "fiscalMonthCode.contains=" + UPDATED_FISCAL_MONTH_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode does not contain
        defaultFiscalMonthFiltering(
            "fiscalMonthCode.doesNotContain=" + UPDATED_FISCAL_MONTH_CODE,
            "fiscalMonthCode.doesNotContain=" + DEFAULT_FISCAL_MONTH_CODE
        );
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalYearIsEqualToSomething() throws Exception {
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalMonthRepository.saveAndFlush(fiscalMonth);
            fiscalYear = FiscalYearResourceIT.createEntity();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        em.persist(fiscalYear);
        em.flush();
        fiscalMonth.setFiscalYear(fiscalYear);
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        Long fiscalYearId = fiscalYear.getId();
        // Get all the fiscalMonthList where fiscalYear equals to fiscalYearId
        defaultFiscalMonthShouldBeFound("fiscalYearId.equals=" + fiscalYearId);

        // Get all the fiscalMonthList where fiscalYear equals to (fiscalYearId + 1)
        defaultFiscalMonthShouldNotBeFound("fiscalYearId.equals=" + (fiscalYearId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            fiscalMonthRepository.saveAndFlush(fiscalMonth);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        fiscalMonth.addPlaceholder(placeholder);
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        Long placeholderId = placeholder.getId();
        // Get all the fiscalMonthList where placeholder equals to placeholderId
        defaultFiscalMonthShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fiscalMonthList where placeholder equals to (placeholderId + 1)
        defaultFiscalMonthShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalQuarterIsEqualToSomething() throws Exception {
        FiscalQuarter fiscalQuarter;
        if (TestUtil.findAll(em, FiscalQuarter.class).isEmpty()) {
            fiscalMonthRepository.saveAndFlush(fiscalMonth);
            fiscalQuarter = FiscalQuarterResourceIT.createEntity(em);
        } else {
            fiscalQuarter = TestUtil.findAll(em, FiscalQuarter.class).get(0);
        }
        em.persist(fiscalQuarter);
        em.flush();
        fiscalMonth.setFiscalQuarter(fiscalQuarter);
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        Long fiscalQuarterId = fiscalQuarter.getId();
        // Get all the fiscalMonthList where fiscalQuarter equals to fiscalQuarterId
        defaultFiscalMonthShouldBeFound("fiscalQuarterId.equals=" + fiscalQuarterId);

        // Get all the fiscalMonthList where fiscalQuarter equals to (fiscalQuarterId + 1)
        defaultFiscalMonthShouldNotBeFound("fiscalQuarterId.equals=" + (fiscalQuarterId + 1));
    }

    private void defaultFiscalMonthFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFiscalMonthShouldBeFound(shouldBeFound);
        defaultFiscalMonthShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiscalMonthShouldBeFound(String filter) throws Exception {
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));

        // Check, that the count call also returns 1
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiscalMonthShouldNotBeFound(String filter) throws Exception {
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFiscalMonth() throws Exception {
        // Get the fiscalMonth
        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiscalMonth() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        fiscalMonthSearchRepository.save(fiscalMonth);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());

        // Update the fiscalMonth
        FiscalMonth updatedFiscalMonth = fiscalMonthRepository.findById(fiscalMonth.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFiscalMonth are not directly saved in db
        em.detach(updatedFiscalMonth);
        updatedFiscalMonth
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(updatedFiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalMonthDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFiscalMonthToMatchAllProperties(updatedFiscalMonth);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FiscalMonth> fiscalMonthSearchList = Streamable.of(fiscalMonthSearchRepository.findAll()).toList();
                FiscalMonth testFiscalMonthSearch = fiscalMonthSearchList.get(searchDatabaseSizeAfter - 1);

                assertFiscalMonthAllPropertiesEquals(testFiscalMonthSearch, updatedFiscalMonth);
            });
    }

    @Test
    @Transactional
    void putNonExistingFiscalMonth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        fiscalMonth.setId(longCount.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalMonth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        fiscalMonth.setId(longCount.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalMonth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        fiscalMonth.setId(longCount.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fiscalMonthDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFiscalMonthWithPatch() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fiscalMonth using partial update
        FiscalMonth partialUpdatedFiscalMonth = new FiscalMonth();
        partialUpdatedFiscalMonth.setId(fiscalMonth.getId());

        partialUpdatedFiscalMonth.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalMonth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiscalMonth))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiscalMonthUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFiscalMonth, fiscalMonth),
            getPersistedFiscalMonth(fiscalMonth)
        );
    }

    @Test
    @Transactional
    void fullUpdateFiscalMonthWithPatch() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fiscalMonth using partial update
        FiscalMonth partialUpdatedFiscalMonth = new FiscalMonth();
        partialUpdatedFiscalMonth.setId(fiscalMonth.getId());

        partialUpdatedFiscalMonth
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);

        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalMonth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiscalMonth))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiscalMonthUpdatableFieldsEquals(partialUpdatedFiscalMonth, getPersistedFiscalMonth(partialUpdatedFiscalMonth));
    }

    @Test
    @Transactional
    void patchNonExistingFiscalMonth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        fiscalMonth.setId(longCount.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalMonth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        fiscalMonth.setId(longCount.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalMonth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        fiscalMonth.setId(longCount.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fiscalMonthDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalMonth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFiscalMonth() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);
        fiscalMonthRepository.save(fiscalMonth);
        fiscalMonthSearchRepository.save(fiscalMonth);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the fiscalMonth
        restFiscalMonthMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalMonth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fiscalMonthSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFiscalMonth() throws Exception {
        // Initialize the database
        insertedFiscalMonth = fiscalMonthRepository.saveAndFlush(fiscalMonth);
        fiscalMonthSearchRepository.save(fiscalMonth);

        // Search the fiscalMonth
        restFiscalMonthMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalMonth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));
    }

    protected long getRepositoryCount() {
        return fiscalMonthRepository.count();
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

    protected FiscalMonth getPersistedFiscalMonth(FiscalMonth fiscalMonth) {
        return fiscalMonthRepository.findById(fiscalMonth.getId()).orElseThrow();
    }

    protected void assertPersistedFiscalMonthToMatchAllProperties(FiscalMonth expectedFiscalMonth) {
        assertFiscalMonthAllPropertiesEquals(expectedFiscalMonth, getPersistedFiscalMonth(expectedFiscalMonth));
    }

    protected void assertPersistedFiscalMonthToMatchUpdatableProperties(FiscalMonth expectedFiscalMonth) {
        assertFiscalMonthAllUpdatablePropertiesEquals(expectedFiscalMonth, getPersistedFiscalMonth(expectedFiscalMonth));
    }
}
