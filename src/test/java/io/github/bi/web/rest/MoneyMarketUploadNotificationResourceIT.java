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

import static io.github.bi.domain.MoneyMarketUploadNotificationAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.MoneyMarketList;
import io.github.bi.domain.MoneyMarketUploadNotification;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.ReportBatch;
import io.github.bi.repository.MoneyMarketUploadNotificationRepository;
import io.github.bi.repository.search.MoneyMarketUploadNotificationSearchRepository;
import io.github.bi.service.MoneyMarketUploadNotificationService;
import io.github.bi.service.dto.MoneyMarketUploadNotificationDTO;
import io.github.bi.service.mapper.MoneyMarketUploadNotificationMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link MoneyMarketUploadNotificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MoneyMarketUploadNotificationResourceIT {

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROW_NUMBER = 1;
    private static final Integer UPDATED_ROW_NUMBER = 2;
    private static final Integer SMALLER_ROW_NUMBER = 1 - 1;

    private static final UUID DEFAULT_REFERENCE_NUMBER = UUID.randomUUID();
    private static final UUID UPDATED_REFERENCE_NUMBER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/money-market-upload-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/money-market-upload-notifications/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepository;

    @Mock
    private MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepositoryMock;

    @Autowired
    private MoneyMarketUploadNotificationMapper moneyMarketUploadNotificationMapper;

    @Mock
    private MoneyMarketUploadNotificationService moneyMarketUploadNotificationServiceMock;

    @Autowired
    private MoneyMarketUploadNotificationSearchRepository moneyMarketUploadNotificationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyMarketUploadNotificationMockMvc;

    private MoneyMarketUploadNotification moneyMarketUploadNotification;

    private MoneyMarketUploadNotification insertedMoneyMarketUploadNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketUploadNotification createEntity() {
        return new MoneyMarketUploadNotification()
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .rowNumber(DEFAULT_ROW_NUMBER)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketUploadNotification createUpdatedEntity() {
        return new MoneyMarketUploadNotification()
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .rowNumber(UPDATED_ROW_NUMBER)
            .referenceNumber(UPDATED_REFERENCE_NUMBER);
    }

    @BeforeEach
    void initTest() {
        moneyMarketUploadNotification = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoneyMarketUploadNotification != null) {
            moneyMarketUploadNotificationRepository.delete(insertedMoneyMarketUploadNotification);
            moneyMarketUploadNotificationSearchRepository.delete(insertedMoneyMarketUploadNotification);
            insertedMoneyMarketUploadNotification = null;
        }
    }

    @Test
    @Transactional
    void createMoneyMarketUploadNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        // Create the MoneyMarketUploadNotification
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );
        var returnedMoneyMarketUploadNotificationDTO = om.readValue(
            restMoneyMarketUploadNotificationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoneyMarketUploadNotificationDTO.class
        );

        // Validate the MoneyMarketUploadNotification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoneyMarketUploadNotification = moneyMarketUploadNotificationMapper.toEntity(returnedMoneyMarketUploadNotificationDTO);
        assertMoneyMarketUploadNotificationUpdatableFieldsEquals(
            returnedMoneyMarketUploadNotification,
            getPersistedMoneyMarketUploadNotification(returnedMoneyMarketUploadNotification)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMoneyMarketUploadNotification = returnedMoneyMarketUploadNotification;
    }

    @Test
    @Transactional
    void createMoneyMarketUploadNotificationWithExistingId() throws Exception {
        // Create the MoneyMarketUploadNotification with an existing ID
        moneyMarketUploadNotification.setId(1L);
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyMarketUploadNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkReferenceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        // set the field null
        moneyMarketUploadNotification.setReferenceNumber(null);

        // Create the MoneyMarketUploadNotification, which fails.
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        restMoneyMarketUploadNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotifications() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList
        restMoneyMarketUploadNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketUploadNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].rowNumber").value(hasItem(DEFAULT_ROW_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyMarketUploadNotificationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(moneyMarketUploadNotificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyMarketUploadNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(moneyMarketUploadNotificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyMarketUploadNotificationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(moneyMarketUploadNotificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyMarketUploadNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(moneyMarketUploadNotificationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMoneyMarketUploadNotification() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get the moneyMarketUploadNotification
        restMoneyMarketUploadNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyMarketUploadNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyMarketUploadNotification.getId().intValue()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.rowNumber").value(DEFAULT_ROW_NUMBER))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER.toString()));
    }

    @Test
    @Transactional
    void getMoneyMarketUploadNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        Long id = moneyMarketUploadNotification.getId();

        defaultMoneyMarketUploadNotificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMoneyMarketUploadNotificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMoneyMarketUploadNotificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByRowNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where rowNumber equals to
        defaultMoneyMarketUploadNotificationFiltering("rowNumber.equals=" + DEFAULT_ROW_NUMBER, "rowNumber.equals=" + UPDATED_ROW_NUMBER);
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByRowNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where rowNumber in
        defaultMoneyMarketUploadNotificationFiltering(
            "rowNumber.in=" + DEFAULT_ROW_NUMBER + "," + UPDATED_ROW_NUMBER,
            "rowNumber.in=" + UPDATED_ROW_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByRowNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where rowNumber is not null
        defaultMoneyMarketUploadNotificationFiltering("rowNumber.specified=true", "rowNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByRowNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where rowNumber is greater than or equal to
        defaultMoneyMarketUploadNotificationFiltering(
            "rowNumber.greaterThanOrEqual=" + DEFAULT_ROW_NUMBER,
            "rowNumber.greaterThanOrEqual=" + UPDATED_ROW_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByRowNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where rowNumber is less than or equal to
        defaultMoneyMarketUploadNotificationFiltering(
            "rowNumber.lessThanOrEqual=" + DEFAULT_ROW_NUMBER,
            "rowNumber.lessThanOrEqual=" + SMALLER_ROW_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByRowNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where rowNumber is less than
        defaultMoneyMarketUploadNotificationFiltering(
            "rowNumber.lessThan=" + UPDATED_ROW_NUMBER,
            "rowNumber.lessThan=" + DEFAULT_ROW_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByRowNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where rowNumber is greater than
        defaultMoneyMarketUploadNotificationFiltering(
            "rowNumber.greaterThan=" + SMALLER_ROW_NUMBER,
            "rowNumber.greaterThan=" + DEFAULT_ROW_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where referenceNumber equals to
        defaultMoneyMarketUploadNotificationFiltering(
            "referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER,
            "referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where referenceNumber in
        defaultMoneyMarketUploadNotificationFiltering(
            "referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER,
            "referenceNumber.in=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        // Get all the moneyMarketUploadNotificationList where referenceNumber is not null
        defaultMoneyMarketUploadNotificationFiltering("referenceNumber.specified=true", "referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByMoneyMarketListIsEqualToSomething() throws Exception {
        MoneyMarketList moneyMarketList;
        if (TestUtil.findAll(em, MoneyMarketList.class).isEmpty()) {
            moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
            moneyMarketList = MoneyMarketListResourceIT.createEntity(em);
        } else {
            moneyMarketList = TestUtil.findAll(em, MoneyMarketList.class).get(0);
        }
        em.persist(moneyMarketList);
        em.flush();
        moneyMarketUploadNotification.setMoneyMarketList(moneyMarketList);
        moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
        Long moneyMarketListId = moneyMarketList.getId();
        // Get all the moneyMarketUploadNotificationList where moneyMarketList equals to moneyMarketListId
        defaultMoneyMarketUploadNotificationShouldBeFound("moneyMarketListId.equals=" + moneyMarketListId);

        // Get all the moneyMarketUploadNotificationList where moneyMarketList equals to (moneyMarketListId + 1)
        defaultMoneyMarketUploadNotificationShouldNotBeFound("moneyMarketListId.equals=" + (moneyMarketListId + 1));
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByReportBatchIsEqualToSomething() throws Exception {
        ReportBatch reportBatch;
        if (TestUtil.findAll(em, ReportBatch.class).isEmpty()) {
            moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
            reportBatch = ReportBatchResourceIT.createEntity(em);
        } else {
            reportBatch = TestUtil.findAll(em, ReportBatch.class).get(0);
        }
        em.persist(reportBatch);
        em.flush();
        moneyMarketUploadNotification.setReportBatch(reportBatch);
        moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
        Long reportBatchId = reportBatch.getId();
        // Get all the moneyMarketUploadNotificationList where reportBatch equals to reportBatchId
        defaultMoneyMarketUploadNotificationShouldBeFound("reportBatchId.equals=" + reportBatchId);

        // Get all the moneyMarketUploadNotificationList where reportBatch equals to (reportBatchId + 1)
        defaultMoneyMarketUploadNotificationShouldNotBeFound("reportBatchId.equals=" + (reportBatchId + 1));
    }

    @Test
    @Transactional
    void getAllMoneyMarketUploadNotificationsByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        moneyMarketUploadNotification.addPlaceholder(placeholder);
        moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
        Long placeholderId = placeholder.getId();
        // Get all the moneyMarketUploadNotificationList where placeholder equals to placeholderId
        defaultMoneyMarketUploadNotificationShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the moneyMarketUploadNotificationList where placeholder equals to (placeholderId + 1)
        defaultMoneyMarketUploadNotificationShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultMoneyMarketUploadNotificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMoneyMarketUploadNotificationShouldBeFound(shouldBeFound);
        defaultMoneyMarketUploadNotificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMoneyMarketUploadNotificationShouldBeFound(String filter) throws Exception {
        restMoneyMarketUploadNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketUploadNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].rowNumber").value(hasItem(DEFAULT_ROW_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.toString())));

        // Check, that the count call also returns 1
        restMoneyMarketUploadNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMoneyMarketUploadNotificationShouldNotBeFound(String filter) throws Exception {
        restMoneyMarketUploadNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMoneyMarketUploadNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMoneyMarketUploadNotification() throws Exception {
        // Get the moneyMarketUploadNotification
        restMoneyMarketUploadNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyMarketUploadNotification() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyMarketUploadNotificationSearchRepository.save(moneyMarketUploadNotification);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());

        // Update the moneyMarketUploadNotification
        MoneyMarketUploadNotification updatedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository
            .findById(moneyMarketUploadNotification.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyMarketUploadNotification are not directly saved in db
        em.detach(updatedMoneyMarketUploadNotification);
        updatedMoneyMarketUploadNotification
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .rowNumber(UPDATED_ROW_NUMBER)
            .referenceNumber(UPDATED_REFERENCE_NUMBER);
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            updatedMoneyMarketUploadNotification
        );

        restMoneyMarketUploadNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyMarketUploadNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoneyMarketUploadNotificationToMatchAllProperties(updatedMoneyMarketUploadNotification);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MoneyMarketUploadNotification> moneyMarketUploadNotificationSearchList = Streamable.of(
                    moneyMarketUploadNotificationSearchRepository.findAll()
                ).toList();
                MoneyMarketUploadNotification testMoneyMarketUploadNotificationSearch = moneyMarketUploadNotificationSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertMoneyMarketUploadNotificationAllPropertiesEquals(
                    testMoneyMarketUploadNotificationSearch,
                    updatedMoneyMarketUploadNotification
                );
            });
    }

    @Test
    @Transactional
    void putNonExistingMoneyMarketUploadNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        moneyMarketUploadNotification.setId(longCount.incrementAndGet());

        // Create the MoneyMarketUploadNotification
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMarketUploadNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyMarketUploadNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyMarketUploadNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        moneyMarketUploadNotification.setId(longCount.incrementAndGet());

        // Create the MoneyMarketUploadNotification
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketUploadNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyMarketUploadNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        moneyMarketUploadNotification.setId(longCount.incrementAndGet());

        // Create the MoneyMarketUploadNotification
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketUploadNotificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMoneyMarketUploadNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyMarketUploadNotification using partial update
        MoneyMarketUploadNotification partialUpdatedMoneyMarketUploadNotification = new MoneyMarketUploadNotification();
        partialUpdatedMoneyMarketUploadNotification.setId(moneyMarketUploadNotification.getId());

        partialUpdatedMoneyMarketUploadNotification.errorMessage(UPDATED_ERROR_MESSAGE).referenceNumber(UPDATED_REFERENCE_NUMBER);

        restMoneyMarketUploadNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyMarketUploadNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyMarketUploadNotification))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketUploadNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyMarketUploadNotificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoneyMarketUploadNotification, moneyMarketUploadNotification),
            getPersistedMoneyMarketUploadNotification(moneyMarketUploadNotification)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoneyMarketUploadNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyMarketUploadNotification using partial update
        MoneyMarketUploadNotification partialUpdatedMoneyMarketUploadNotification = new MoneyMarketUploadNotification();
        partialUpdatedMoneyMarketUploadNotification.setId(moneyMarketUploadNotification.getId());

        partialUpdatedMoneyMarketUploadNotification
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .rowNumber(UPDATED_ROW_NUMBER)
            .referenceNumber(UPDATED_REFERENCE_NUMBER);

        restMoneyMarketUploadNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyMarketUploadNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyMarketUploadNotification))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketUploadNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyMarketUploadNotificationUpdatableFieldsEquals(
            partialUpdatedMoneyMarketUploadNotification,
            getPersistedMoneyMarketUploadNotification(partialUpdatedMoneyMarketUploadNotification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMoneyMarketUploadNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        moneyMarketUploadNotification.setId(longCount.incrementAndGet());

        // Create the MoneyMarketUploadNotification
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMarketUploadNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyMarketUploadNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyMarketUploadNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        moneyMarketUploadNotification.setId(longCount.incrementAndGet());

        // Create the MoneyMarketUploadNotification
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketUploadNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyMarketUploadNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        moneyMarketUploadNotification.setId(longCount.incrementAndGet());

        // Create the MoneyMarketUploadNotification
        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationMapper.toDto(
            moneyMarketUploadNotification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketUploadNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyMarketUploadNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyMarketUploadNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMoneyMarketUploadNotification() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
        moneyMarketUploadNotificationRepository.save(moneyMarketUploadNotification);
        moneyMarketUploadNotificationSearchRepository.save(moneyMarketUploadNotification);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the moneyMarketUploadNotification
        restMoneyMarketUploadNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyMarketUploadNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketUploadNotificationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMoneyMarketUploadNotification() throws Exception {
        // Initialize the database
        insertedMoneyMarketUploadNotification = moneyMarketUploadNotificationRepository.saveAndFlush(moneyMarketUploadNotification);
        moneyMarketUploadNotificationSearchRepository.save(moneyMarketUploadNotification);

        // Search the moneyMarketUploadNotification
        restMoneyMarketUploadNotificationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + moneyMarketUploadNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketUploadNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].rowNumber").value(hasItem(DEFAULT_ROW_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.toString())));
    }

    protected long getRepositoryCount() {
        return moneyMarketUploadNotificationRepository.count();
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

    protected MoneyMarketUploadNotification getPersistedMoneyMarketUploadNotification(
        MoneyMarketUploadNotification moneyMarketUploadNotification
    ) {
        return moneyMarketUploadNotificationRepository.findById(moneyMarketUploadNotification.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyMarketUploadNotificationToMatchAllProperties(
        MoneyMarketUploadNotification expectedMoneyMarketUploadNotification
    ) {
        assertMoneyMarketUploadNotificationAllPropertiesEquals(
            expectedMoneyMarketUploadNotification,
            getPersistedMoneyMarketUploadNotification(expectedMoneyMarketUploadNotification)
        );
    }

    protected void assertPersistedMoneyMarketUploadNotificationToMatchUpdatableProperties(
        MoneyMarketUploadNotification expectedMoneyMarketUploadNotification
    ) {
        assertMoneyMarketUploadNotificationAllUpdatablePropertiesEquals(
            expectedMoneyMarketUploadNotification,
            getPersistedMoneyMarketUploadNotification(expectedMoneyMarketUploadNotification)
        );
    }
}
