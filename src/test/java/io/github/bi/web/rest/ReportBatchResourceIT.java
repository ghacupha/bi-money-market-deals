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

import static io.github.bi.domain.ReportBatchAsserts.*;
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
import io.github.bi.domain.ApplicationUser;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.ReportBatch;
import io.github.bi.domain.enumeration.FileProcessFlag;
import io.github.bi.domain.enumeration.reportBatchStatus;
import io.github.bi.repository.ReportBatchRepository;
import io.github.bi.repository.search.ReportBatchSearchRepository;
import io.github.bi.service.ReportBatchService;
import io.github.bi.service.dto.ReportBatchDTO;
import io.github.bi.service.mapper.ReportBatchMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link ReportBatchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportBatchResourceIT {

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_UPLOAD_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOAD_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPLOAD_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final reportBatchStatus DEFAULT_STATUS = reportBatchStatus.ACTIVE;
    private static final reportBatchStatus UPDATED_STATUS = reportBatchStatus.CANCELLED;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final UUID DEFAULT_FILE_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FILE_IDENTIFIER = UUID.randomUUID();

    private static final FileProcessFlag DEFAULT_PROCESS_FLAG = FileProcessFlag.PENDING;
    private static final FileProcessFlag UPDATED_PROCESS_FLAG = FileProcessFlag.IN_PROGRESS;

    private static final byte[] DEFAULT_CSV_FILE_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CSV_FILE_ATTACHMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CSV_FILE_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CSV_FILE_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/report-batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/report-batches/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportBatchRepository reportBatchRepository;

    @Mock
    private ReportBatchRepository reportBatchRepositoryMock;

    @Autowired
    private ReportBatchMapper reportBatchMapper;

    @Mock
    private ReportBatchService reportBatchServiceMock;

    @Autowired
    private ReportBatchSearchRepository reportBatchSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportBatchMockMvc;

    private ReportBatch reportBatch;

    private ReportBatch insertedReportBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportBatch createEntity(EntityManager em) {
        ReportBatch reportBatch = new ReportBatch()
            .reportDate(DEFAULT_REPORT_DATE)
            .uploadTimeStamp(DEFAULT_UPLOAD_TIME_STAMP)
            .status(DEFAULT_STATUS)
            .active(DEFAULT_ACTIVE)
            .description(DEFAULT_DESCRIPTION)
            .fileIdentifier(DEFAULT_FILE_IDENTIFIER)
            .processFlag(DEFAULT_PROCESS_FLAG)
            .csvFileAttachment(DEFAULT_CSV_FILE_ATTACHMENT)
            .csvFileAttachmentContentType(DEFAULT_CSV_FILE_ATTACHMENT_CONTENT_TYPE);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        reportBatch.setUploadedBy(applicationUser);
        return reportBatch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportBatch createUpdatedEntity(EntityManager em) {
        ReportBatch updatedReportBatch = new ReportBatch()
            .reportDate(UPDATED_REPORT_DATE)
            .uploadTimeStamp(UPDATED_UPLOAD_TIME_STAMP)
            .status(UPDATED_STATUS)
            .active(UPDATED_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .fileIdentifier(UPDATED_FILE_IDENTIFIER)
            .processFlag(UPDATED_PROCESS_FLAG)
            .csvFileAttachment(UPDATED_CSV_FILE_ATTACHMENT)
            .csvFileAttachmentContentType(UPDATED_CSV_FILE_ATTACHMENT_CONTENT_TYPE);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createUpdatedEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        updatedReportBatch.setUploadedBy(applicationUser);
        return updatedReportBatch;
    }

    @BeforeEach
    void initTest() {
        reportBatch = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReportBatch != null) {
            reportBatchRepository.delete(insertedReportBatch);
            reportBatchSearchRepository.delete(insertedReportBatch);
            insertedReportBatch = null;
        }
    }

    @Test
    @Transactional
    void createReportBatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        // Create the ReportBatch
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);
        var returnedReportBatchDTO = om.readValue(
            restReportBatchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportBatchDTO.class
        );

        // Validate the ReportBatch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportBatch = reportBatchMapper.toEntity(returnedReportBatchDTO);
        assertReportBatchUpdatableFieldsEquals(returnedReportBatch, getPersistedReportBatch(returnedReportBatch));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedReportBatch = returnedReportBatch;
    }

    @Test
    @Transactional
    void createReportBatchWithExistingId() throws Exception {
        // Create the ReportBatch with an existing ID
        reportBatch.setId(1L);
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        // set the field null
        reportBatch.setReportDate(null);

        // Create the ReportBatch, which fails.
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        restReportBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUploadTimeStampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        // set the field null
        reportBatch.setUploadTimeStamp(null);

        // Create the ReportBatch, which fails.
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        restReportBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        // set the field null
        reportBatch.setStatus(null);

        // Create the ReportBatch, which fails.
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        restReportBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        // set the field null
        reportBatch.setActive(null);

        // Create the ReportBatch, which fails.
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        restReportBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        // set the field null
        reportBatch.setDescription(null);

        // Create the ReportBatch, which fails.
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        restReportBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFileIdentifierIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        // set the field null
        reportBatch.setFileIdentifier(null);

        // Create the ReportBatch, which fails.
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        restReportBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllReportBatches() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList
        restReportBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadTimeStamp").value(hasItem(sameInstant(DEFAULT_UPLOAD_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileIdentifier").value(hasItem(DEFAULT_FILE_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].processFlag").value(hasItem(DEFAULT_PROCESS_FLAG.toString())))
            .andExpect(jsonPath("$.[*].csvFileAttachmentContentType").value(hasItem(DEFAULT_CSV_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].csvFileAttachment").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CSV_FILE_ATTACHMENT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportBatchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportBatchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportBatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportBatchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportBatchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportBatchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportBatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reportBatchRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReportBatch() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get the reportBatch
        restReportBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, reportBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportBatch.getId().intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.uploadTimeStamp").value(sameInstant(DEFAULT_UPLOAD_TIME_STAMP)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileIdentifier").value(DEFAULT_FILE_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.processFlag").value(DEFAULT_PROCESS_FLAG.toString()))
            .andExpect(jsonPath("$.csvFileAttachmentContentType").value(DEFAULT_CSV_FILE_ATTACHMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.csvFileAttachment").value(Base64.getEncoder().encodeToString(DEFAULT_CSV_FILE_ATTACHMENT)));
    }

    @Test
    @Transactional
    void getReportBatchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        Long id = reportBatch.getId();

        defaultReportBatchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportBatchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportBatchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportBatchesByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where reportDate equals to
        defaultReportBatchFiltering("reportDate.equals=" + DEFAULT_REPORT_DATE, "reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllReportBatchesByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where reportDate in
        defaultReportBatchFiltering(
            "reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE,
            "reportDate.in=" + UPDATED_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where reportDate is not null
        defaultReportBatchFiltering("reportDate.specified=true", "reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReportBatchesByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where reportDate is greater than or equal to
        defaultReportBatchFiltering(
            "reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE,
            "reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where reportDate is less than or equal to
        defaultReportBatchFiltering(
            "reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE,
            "reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where reportDate is less than
        defaultReportBatchFiltering("reportDate.lessThan=" + UPDATED_REPORT_DATE, "reportDate.lessThan=" + DEFAULT_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllReportBatchesByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where reportDate is greater than
        defaultReportBatchFiltering("reportDate.greaterThan=" + SMALLER_REPORT_DATE, "reportDate.greaterThan=" + DEFAULT_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where uploadTimeStamp equals to
        defaultReportBatchFiltering(
            "uploadTimeStamp.equals=" + DEFAULT_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.equals=" + UPDATED_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where uploadTimeStamp in
        defaultReportBatchFiltering(
            "uploadTimeStamp.in=" + DEFAULT_UPLOAD_TIME_STAMP + "," + UPDATED_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.in=" + UPDATED_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where uploadTimeStamp is not null
        defaultReportBatchFiltering("uploadTimeStamp.specified=true", "uploadTimeStamp.specified=false");
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where uploadTimeStamp is greater than or equal to
        defaultReportBatchFiltering(
            "uploadTimeStamp.greaterThanOrEqual=" + DEFAULT_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.greaterThanOrEqual=" + UPDATED_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where uploadTimeStamp is less than or equal to
        defaultReportBatchFiltering(
            "uploadTimeStamp.lessThanOrEqual=" + DEFAULT_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.lessThanOrEqual=" + SMALLER_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where uploadTimeStamp is less than
        defaultReportBatchFiltering(
            "uploadTimeStamp.lessThan=" + UPDATED_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.lessThan=" + DEFAULT_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where uploadTimeStamp is greater than
        defaultReportBatchFiltering(
            "uploadTimeStamp.greaterThan=" + SMALLER_UPLOAD_TIME_STAMP,
            "uploadTimeStamp.greaterThan=" + DEFAULT_UPLOAD_TIME_STAMP
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where status equals to
        defaultReportBatchFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportBatchesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where status in
        defaultReportBatchFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportBatchesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where status is not null
        defaultReportBatchFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllReportBatchesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where active equals to
        defaultReportBatchFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReportBatchesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where active in
        defaultReportBatchFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReportBatchesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where active is not null
        defaultReportBatchFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllReportBatchesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where description equals to
        defaultReportBatchFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportBatchesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where description in
        defaultReportBatchFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where description is not null
        defaultReportBatchFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllReportBatchesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where description contains
        defaultReportBatchFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportBatchesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where description does not contain
        defaultReportBatchFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByFileIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where fileIdentifier equals to
        defaultReportBatchFiltering("fileIdentifier.equals=" + DEFAULT_FILE_IDENTIFIER, "fileIdentifier.equals=" + UPDATED_FILE_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllReportBatchesByFileIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where fileIdentifier in
        defaultReportBatchFiltering(
            "fileIdentifier.in=" + DEFAULT_FILE_IDENTIFIER + "," + UPDATED_FILE_IDENTIFIER,
            "fileIdentifier.in=" + UPDATED_FILE_IDENTIFIER
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByFileIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where fileIdentifier is not null
        defaultReportBatchFiltering("fileIdentifier.specified=true", "fileIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllReportBatchesByProcessFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where processFlag equals to
        defaultReportBatchFiltering("processFlag.equals=" + DEFAULT_PROCESS_FLAG, "processFlag.equals=" + UPDATED_PROCESS_FLAG);
    }

    @Test
    @Transactional
    void getAllReportBatchesByProcessFlagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where processFlag in
        defaultReportBatchFiltering(
            "processFlag.in=" + DEFAULT_PROCESS_FLAG + "," + UPDATED_PROCESS_FLAG,
            "processFlag.in=" + UPDATED_PROCESS_FLAG
        );
    }

    @Test
    @Transactional
    void getAllReportBatchesByProcessFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        // Get all the reportBatchList where processFlag is not null
        defaultReportBatchFiltering("processFlag.specified=true", "processFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllReportBatchesByUploadedByIsEqualToSomething() throws Exception {
        ApplicationUser uploadedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            reportBatchRepository.saveAndFlush(reportBatch);
            uploadedBy = ApplicationUserResourceIT.createEntity(em);
        } else {
            uploadedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(uploadedBy);
        em.flush();
        reportBatch.setUploadedBy(uploadedBy);
        reportBatchRepository.saveAndFlush(reportBatch);
        Long uploadedById = uploadedBy.getId();
        // Get all the reportBatchList where uploadedBy equals to uploadedById
        defaultReportBatchShouldBeFound("uploadedById.equals=" + uploadedById);

        // Get all the reportBatchList where uploadedBy equals to (uploadedById + 1)
        defaultReportBatchShouldNotBeFound("uploadedById.equals=" + (uploadedById + 1));
    }

    @Test
    @Transactional
    void getAllReportBatchesByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            reportBatchRepository.saveAndFlush(reportBatch);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        reportBatch.addPlaceholder(placeholder);
        reportBatchRepository.saveAndFlush(reportBatch);
        Long placeholderId = placeholder.getId();
        // Get all the reportBatchList where placeholder equals to placeholderId
        defaultReportBatchShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the reportBatchList where placeholder equals to (placeholderId + 1)
        defaultReportBatchShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultReportBatchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportBatchShouldBeFound(shouldBeFound);
        defaultReportBatchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportBatchShouldBeFound(String filter) throws Exception {
        restReportBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadTimeStamp").value(hasItem(sameInstant(DEFAULT_UPLOAD_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileIdentifier").value(hasItem(DEFAULT_FILE_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].processFlag").value(hasItem(DEFAULT_PROCESS_FLAG.toString())))
            .andExpect(jsonPath("$.[*].csvFileAttachmentContentType").value(hasItem(DEFAULT_CSV_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].csvFileAttachment").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CSV_FILE_ATTACHMENT))));

        // Check, that the count call also returns 1
        restReportBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportBatchShouldNotBeFound(String filter) throws Exception {
        restReportBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportBatch() throws Exception {
        // Get the reportBatch
        restReportBatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportBatch() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportBatchSearchRepository.save(reportBatch);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());

        // Update the reportBatch
        ReportBatch updatedReportBatch = reportBatchRepository.findById(reportBatch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportBatch are not directly saved in db
        em.detach(updatedReportBatch);
        updatedReportBatch
            .reportDate(UPDATED_REPORT_DATE)
            .uploadTimeStamp(UPDATED_UPLOAD_TIME_STAMP)
            .status(UPDATED_STATUS)
            .active(UPDATED_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .fileIdentifier(UPDATED_FILE_IDENTIFIER)
            .processFlag(UPDATED_PROCESS_FLAG)
            .csvFileAttachment(UPDATED_CSV_FILE_ATTACHMENT)
            .csvFileAttachmentContentType(UPDATED_CSV_FILE_ATTACHMENT_CONTENT_TYPE);
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(updatedReportBatch);

        restReportBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportBatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportBatchToMatchAllProperties(updatedReportBatch);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ReportBatch> reportBatchSearchList = Streamable.of(reportBatchSearchRepository.findAll()).toList();
                ReportBatch testReportBatchSearch = reportBatchSearchList.get(searchDatabaseSizeAfter - 1);

                assertReportBatchAllPropertiesEquals(testReportBatchSearch, updatedReportBatch);
            });
    }

    @Test
    @Transactional
    void putNonExistingReportBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        reportBatch.setId(longCount.incrementAndGet());

        // Create the ReportBatch
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        reportBatch.setId(longCount.incrementAndGet());

        // Create the ReportBatch
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        reportBatch.setId(longCount.incrementAndGet());

        // Create the ReportBatch
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportBatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateReportBatchWithPatch() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportBatch using partial update
        ReportBatch partialUpdatedReportBatch = new ReportBatch();
        partialUpdatedReportBatch.setId(reportBatch.getId());

        partialUpdatedReportBatch
            .uploadTimeStamp(UPDATED_UPLOAD_TIME_STAMP)
            .active(UPDATED_ACTIVE)
            .processFlag(UPDATED_PROCESS_FLAG)
            .csvFileAttachment(UPDATED_CSV_FILE_ATTACHMENT)
            .csvFileAttachmentContentType(UPDATED_CSV_FILE_ATTACHMENT_CONTENT_TYPE);

        restReportBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportBatch))
            )
            .andExpect(status().isOk());

        // Validate the ReportBatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportBatchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportBatch, reportBatch),
            getPersistedReportBatch(reportBatch)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportBatchWithPatch() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportBatch using partial update
        ReportBatch partialUpdatedReportBatch = new ReportBatch();
        partialUpdatedReportBatch.setId(reportBatch.getId());

        partialUpdatedReportBatch
            .reportDate(UPDATED_REPORT_DATE)
            .uploadTimeStamp(UPDATED_UPLOAD_TIME_STAMP)
            .status(UPDATED_STATUS)
            .active(UPDATED_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .fileIdentifier(UPDATED_FILE_IDENTIFIER)
            .processFlag(UPDATED_PROCESS_FLAG)
            .csvFileAttachment(UPDATED_CSV_FILE_ATTACHMENT)
            .csvFileAttachmentContentType(UPDATED_CSV_FILE_ATTACHMENT_CONTENT_TYPE);

        restReportBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportBatch))
            )
            .andExpect(status().isOk());

        // Validate the ReportBatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportBatchUpdatableFieldsEquals(partialUpdatedReportBatch, getPersistedReportBatch(partialUpdatedReportBatch));
    }

    @Test
    @Transactional
    void patchNonExistingReportBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        reportBatch.setId(longCount.incrementAndGet());

        // Create the ReportBatch
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportBatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        reportBatch.setId(longCount.incrementAndGet());

        // Create the ReportBatch
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        reportBatch.setId(longCount.incrementAndGet());

        // Create the ReportBatch
        ReportBatchDTO reportBatchDTO = reportBatchMapper.toDto(reportBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportBatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportBatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteReportBatch() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);
        reportBatchRepository.save(reportBatch);
        reportBatchSearchRepository.save(reportBatch);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the reportBatch
        restReportBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportBatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchReportBatch() throws Exception {
        // Initialize the database
        insertedReportBatch = reportBatchRepository.saveAndFlush(reportBatch);
        reportBatchSearchRepository.save(reportBatch);

        // Search the reportBatch
        restReportBatchMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadTimeStamp").value(hasItem(sameInstant(DEFAULT_UPLOAD_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileIdentifier").value(hasItem(DEFAULT_FILE_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].processFlag").value(hasItem(DEFAULT_PROCESS_FLAG.toString())))
            .andExpect(jsonPath("$.[*].csvFileAttachmentContentType").value(hasItem(DEFAULT_CSV_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].csvFileAttachment").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CSV_FILE_ATTACHMENT))));
    }

    protected long getRepositoryCount() {
        return reportBatchRepository.count();
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

    protected ReportBatch getPersistedReportBatch(ReportBatch reportBatch) {
        return reportBatchRepository.findById(reportBatch.getId()).orElseThrow();
    }

    protected void assertPersistedReportBatchToMatchAllProperties(ReportBatch expectedReportBatch) {
        assertReportBatchAllPropertiesEquals(expectedReportBatch, getPersistedReportBatch(expectedReportBatch));
    }

    protected void assertPersistedReportBatchToMatchUpdatableProperties(ReportBatch expectedReportBatch) {
        assertReportBatchAllUpdatablePropertiesEquals(expectedReportBatch, getPersistedReportBatch(expectedReportBatch));
    }
}
