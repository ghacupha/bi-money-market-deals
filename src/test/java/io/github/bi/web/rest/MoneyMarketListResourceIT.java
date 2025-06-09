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

import static io.github.bi.domain.MoneyMarketListAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static io.github.bi.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.MoneyMarketList;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.enumeration.reportBatchStatus;
import io.github.bi.repository.MoneyMarketListRepository;
import io.github.bi.repository.search.MoneyMarketListSearchRepository;
import io.github.bi.service.MoneyMarketListService;
import io.github.bi.service.dto.MoneyMarketListDTO;
import io.github.bi.service.mapper.MoneyMarketListMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link MoneyMarketListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MoneyMarketListResourceIT {

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_UPLOAD_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOAD_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPLOAD_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final reportBatchStatus DEFAULT_STATUS = reportBatchStatus.ACTIVE;
    private static final reportBatchStatus UPDATED_STATUS = reportBatchStatus.CANCELLED;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/money-market-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/money-market-lists/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyMarketListRepository moneyMarketListRepository;

    @Mock
    private MoneyMarketListRepository moneyMarketListRepositoryMock;

    @Autowired
    private MoneyMarketListMapper moneyMarketListMapper;

    @Mock
    private MoneyMarketListService moneyMarketListServiceMock;

    @Autowired
    private MoneyMarketListSearchRepository moneyMarketListSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyMarketListMockMvc;

    private MoneyMarketList moneyMarketList;

    private MoneyMarketList insertedMoneyMarketList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketList createEntity() {
        return new MoneyMarketList()
            .reportDate(DEFAULT_REPORT_DATE)
            .uploadTimeStamp(DEFAULT_UPLOAD_TIME_STAMP)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketList createUpdatedEntity() {
        return new MoneyMarketList()
            .reportDate(UPDATED_REPORT_DATE)
            .uploadTimeStamp(UPDATED_UPLOAD_TIME_STAMP)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        moneyMarketList = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoneyMarketList != null) {
            moneyMarketListRepository.delete(insertedMoneyMarketList);
            moneyMarketListSearchRepository.delete(insertedMoneyMarketList);
            insertedMoneyMarketList = null;
        }
    }

    @Test
    @Transactional
    void createMoneyMarketList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        // Create the MoneyMarketList
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);
        var returnedMoneyMarketListDTO = om.readValue(
            restMoneyMarketListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketListDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoneyMarketListDTO.class
        );

        // Validate the MoneyMarketList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoneyMarketList = moneyMarketListMapper.toEntity(returnedMoneyMarketListDTO);
        assertMoneyMarketListUpdatableFieldsEquals(returnedMoneyMarketList, getPersistedMoneyMarketList(returnedMoneyMarketList));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMoneyMarketList = returnedMoneyMarketList;
    }

    @Test
    @Transactional
    void createMoneyMarketListWithExistingId() throws Exception {
        // Create the MoneyMarketList with an existing ID
        moneyMarketList.setId(1L);
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyMarketListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        // set the field null
        moneyMarketList.setReportDate(null);

        // Create the MoneyMarketList, which fails.
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        restMoneyMarketListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketListDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUploadTimeStampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        // set the field null
        moneyMarketList.setUploadTimeStamp(null);

        // Create the MoneyMarketList, which fails.
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        restMoneyMarketListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketListDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        // set the field null
        moneyMarketList.setStatus(null);

        // Create the MoneyMarketList, which fails.
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        restMoneyMarketListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketListDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        // set the field null
        moneyMarketList.setActive(null);

        // Create the MoneyMarketList, which fails.
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        restMoneyMarketListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketListDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMoneyMarketLists() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList
        restMoneyMarketListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketList.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadTimeStamp").value(hasItem(sameInstant(DEFAULT_UPLOAD_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyMarketListsWithEagerRelationshipsIsEnabled() throws Exception {
        when(moneyMarketListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyMarketListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(moneyMarketListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyMarketListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(moneyMarketListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyMarketListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(moneyMarketListRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMoneyMarketList() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get the moneyMarketList
        restMoneyMarketListMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyMarketList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyMarketList.getId().intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.uploadTimeStamp").value(sameInstant(DEFAULT_UPLOAD_TIME_STAMP)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getMoneyMarketListsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        Long id = moneyMarketList.getId();

        defaultMoneyMarketListFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMoneyMarketListFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMoneyMarketListFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where reportDate equals to
        defaultMoneyMarketListFiltering("reportDate.equals=" + DEFAULT_REPORT_DATE, "reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where reportDate in
        defaultMoneyMarketListFiltering(
            "reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE,
            "reportDate.in=" + UPDATED_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where reportDate is not null
        defaultMoneyMarketListFiltering("reportDate.specified=true", "reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where reportDate is greater than or equal to
        defaultMoneyMarketListFiltering(
            "reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE,
            "reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where reportDate is less than or equal to
        defaultMoneyMarketListFiltering(
            "reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE,
            "reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where reportDate is less than
        defaultMoneyMarketListFiltering("reportDate.lessThan=" + UPDATED_REPORT_DATE, "reportDate.lessThan=" + DEFAULT_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where reportDate is greater than
        defaultMoneyMarketListFiltering("reportDate.greaterThan=" + SMALLER_REPORT_DATE, "reportDate.greaterThan=" + DEFAULT_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByUploadTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where uploadTimeStamp equals to
        defaultMoneyMarketListFiltering(
            "uploadTimeStamp.equals=" + DEFAULT_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.equals=" + UPDATED_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByUploadTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where uploadTimeStamp in
        defaultMoneyMarketListFiltering(
            "uploadTimeStamp.in=" + DEFAULT_UPLOAD_TIME_STAMP + "," + UPDATED_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.in=" + UPDATED_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByUploadTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where uploadTimeStamp is not null
        defaultMoneyMarketListFiltering("uploadTimeStamp.specified=true", "uploadTimeStamp.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByUploadTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where uploadTimeStamp is greater than or equal to
        defaultMoneyMarketListFiltering(
            "uploadTimeStamp.greaterThanOrEqual=" + DEFAULT_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.greaterThanOrEqual=" + UPDATED_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByUploadTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where uploadTimeStamp is less than or equal to
        defaultMoneyMarketListFiltering(
            "uploadTimeStamp.lessThanOrEqual=" + DEFAULT_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.lessThanOrEqual=" + SMALLER_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByUploadTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where uploadTimeStamp is less than
        defaultMoneyMarketListFiltering(
            "uploadTimeStamp.lessThan=" + UPDATED_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.lessThan=" + DEFAULT_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByUploadTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where uploadTimeStamp is greater than
        defaultMoneyMarketListFiltering(
            "uploadTimeStamp.greaterThan=" + SMALLER_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.greaterThan=" + DEFAULT_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where status equals to
        defaultMoneyMarketListFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where status in
        defaultMoneyMarketListFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where status is not null
        defaultMoneyMarketListFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where description equals to
        defaultMoneyMarketListFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where description in
        defaultMoneyMarketListFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where description is not null
        defaultMoneyMarketListFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where description contains
        defaultMoneyMarketListFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where description does not contain
        defaultMoneyMarketListFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where active equals to
        defaultMoneyMarketListFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where active in
        defaultMoneyMarketListFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        // Get all the moneyMarketListList where active is not null
        defaultMoneyMarketListFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketListsByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            moneyMarketListRepository.saveAndFlush(moneyMarketList);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        moneyMarketList.addPlaceholder(placeholder);
        moneyMarketListRepository.saveAndFlush(moneyMarketList);
        Long placeholderId = placeholder.getId();
        // Get all the moneyMarketListList where placeholder equals to placeholderId
        defaultMoneyMarketListShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the moneyMarketListList where placeholder equals to (placeholderId + 1)
        defaultMoneyMarketListShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultMoneyMarketListFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMoneyMarketListShouldBeFound(shouldBeFound);
        defaultMoneyMarketListShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMoneyMarketListShouldBeFound(String filter) throws Exception {
        restMoneyMarketListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketList.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadTimeStamp").value(hasItem(sameInstant(DEFAULT_UPLOAD_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restMoneyMarketListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMoneyMarketListShouldNotBeFound(String filter) throws Exception {
        restMoneyMarketListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMoneyMarketListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMoneyMarketList() throws Exception {
        // Get the moneyMarketList
        restMoneyMarketListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyMarketList() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyMarketListSearchRepository.save(moneyMarketList);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());

        // Update the moneyMarketList
        MoneyMarketList updatedMoneyMarketList = moneyMarketListRepository.findById(moneyMarketList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyMarketList are not directly saved in db
        em.detach(updatedMoneyMarketList);
        updatedMoneyMarketList
            .reportDate(UPDATED_REPORT_DATE)
            .uploadTimeStamp(UPDATED_UPLOAD_TIME_STAMP)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(updatedMoneyMarketList);

        restMoneyMarketListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyMarketListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketListDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoneyMarketListToMatchAllProperties(updatedMoneyMarketList);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MoneyMarketList> moneyMarketListSearchList = Streamable.of(moneyMarketListSearchRepository.findAll()).toList();
                MoneyMarketList testMoneyMarketListSearch = moneyMarketListSearchList.get(searchDatabaseSizeAfter - 1);

                assertMoneyMarketListAllPropertiesEquals(testMoneyMarketListSearch, updatedMoneyMarketList);
            });
    }

    @Test
    @Transactional
    void putNonExistingMoneyMarketList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        moneyMarketList.setId(longCount.incrementAndGet());

        // Create the MoneyMarketList
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMarketListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyMarketListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyMarketList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        moneyMarketList.setId(longCount.incrementAndGet());

        // Create the MoneyMarketList
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyMarketList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        moneyMarketList.setId(longCount.incrementAndGet());

        // Create the MoneyMarketList
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMoneyMarketListWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyMarketList using partial update
        MoneyMarketList partialUpdatedMoneyMarketList = new MoneyMarketList();
        partialUpdatedMoneyMarketList.setId(moneyMarketList.getId());

        partialUpdatedMoneyMarketList.status(UPDATED_STATUS);

        restMoneyMarketListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyMarketList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyMarketList))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyMarketListUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoneyMarketList, moneyMarketList),
            getPersistedMoneyMarketList(moneyMarketList)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoneyMarketListWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyMarketList using partial update
        MoneyMarketList partialUpdatedMoneyMarketList = new MoneyMarketList();
        partialUpdatedMoneyMarketList.setId(moneyMarketList.getId());

        partialUpdatedMoneyMarketList
            .reportDate(UPDATED_REPORT_DATE)
            .uploadTimeStamp(UPDATED_UPLOAD_TIME_STAMP)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);

        restMoneyMarketListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyMarketList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyMarketList))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyMarketListUpdatableFieldsEquals(
            partialUpdatedMoneyMarketList,
            getPersistedMoneyMarketList(partialUpdatedMoneyMarketList)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMoneyMarketList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        moneyMarketList.setId(longCount.incrementAndGet());

        // Create the MoneyMarketList
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMarketListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyMarketListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyMarketListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyMarketList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        moneyMarketList.setId(longCount.incrementAndGet());

        // Create the MoneyMarketList
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyMarketListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyMarketList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        moneyMarketList.setId(longCount.incrementAndGet());

        // Create the MoneyMarketList
        MoneyMarketListDTO moneyMarketListDTO = moneyMarketListMapper.toDto(moneyMarketList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moneyMarketListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyMarketList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMoneyMarketList() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);
        moneyMarketListRepository.save(moneyMarketList);
        moneyMarketListSearchRepository.save(moneyMarketList);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the moneyMarketList
        restMoneyMarketListMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyMarketList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketListSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMoneyMarketList() throws Exception {
        // Initialize the database
        insertedMoneyMarketList = moneyMarketListRepository.saveAndFlush(moneyMarketList);
        moneyMarketListSearchRepository.save(moneyMarketList);

        // Search the moneyMarketList
        restMoneyMarketListMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + moneyMarketList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketList.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadTimeStamp").value(hasItem(sameInstant(DEFAULT_UPLOAD_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    protected long getRepositoryCount() {
        return moneyMarketListRepository.count();
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

    protected MoneyMarketList getPersistedMoneyMarketList(MoneyMarketList moneyMarketList) {
        return moneyMarketListRepository.findById(moneyMarketList.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyMarketListToMatchAllProperties(MoneyMarketList expectedMoneyMarketList) {
        assertMoneyMarketListAllPropertiesEquals(expectedMoneyMarketList, getPersistedMoneyMarketList(expectedMoneyMarketList));
    }

    protected void assertPersistedMoneyMarketListToMatchUpdatableProperties(MoneyMarketList expectedMoneyMarketList) {
        assertMoneyMarketListAllUpdatablePropertiesEquals(expectedMoneyMarketList, getPersistedMoneyMarketList(expectedMoneyMarketList));
    }
}
