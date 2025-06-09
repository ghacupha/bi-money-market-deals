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

import static io.github.bi.domain.DealerAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.Dealer;
import io.github.bi.domain.Dealer;
import io.github.bi.domain.Placeholder;
import io.github.bi.repository.DealerRepository;
import io.github.bi.repository.search.DealerSearchRepository;
import io.github.bi.service.DealerService;
import io.github.bi.service.dto.DealerDTO;
import io.github.bi.service.mapper.DealerMapper;
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
 * Integration tests for the {@link DealerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DealerResourceIT {

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHYSICAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PHYSICAL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANKERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANKERS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANKERS_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BANKERS_BRANCH = "BBBBBBBBBB";

    private static final String DEFAULT_BANKERS_SWIFT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANKERS_SWIFT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_NAMES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dealers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dealers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DealerRepository dealerRepository;

    @Mock
    private DealerRepository dealerRepositoryMock;

    @Autowired
    private DealerMapper dealerMapper;

    @Mock
    private DealerService dealerServiceMock;

    @Autowired
    private DealerSearchRepository dealerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealerMockMvc;

    private Dealer dealer;

    private Dealer insertedDealer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createEntity() {
        return new Dealer()
            .dealerName(DEFAULT_DEALER_NAME)
            .taxNumber(DEFAULT_TAX_NUMBER)
            .identificationDocumentNumber(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .department(DEFAULT_DEPARTMENT)
            .position(DEFAULT_POSITION)
            .postalAddress(DEFAULT_POSTAL_ADDRESS)
            .physicalAddress(DEFAULT_PHYSICAL_ADDRESS)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .bankersName(DEFAULT_BANKERS_NAME)
            .bankersBranch(DEFAULT_BANKERS_BRANCH)
            .bankersSwiftCode(DEFAULT_BANKERS_SWIFT_CODE)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN)
            .remarks(DEFAULT_REMARKS)
            .otherNames(DEFAULT_OTHER_NAMES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createUpdatedEntity() {
        return new Dealer()
            .dealerName(UPDATED_DEALER_NAME)
            .taxNumber(UPDATED_TAX_NUMBER)
            .identificationDocumentNumber(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS)
            .otherNames(UPDATED_OTHER_NAMES);
    }

    @BeforeEach
    void initTest() {
        dealer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDealer != null) {
            dealerRepository.delete(insertedDealer);
            dealerSearchRepository.delete(insertedDealer);
            insertedDealer = null;
        }
    }

    @Test
    @Transactional
    void createDealer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);
        var returnedDealerDTO = om.readValue(
            restDealerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DealerDTO.class
        );

        // Validate the Dealer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDealer = dealerMapper.toEntity(returnedDealerDTO);
        assertDealerUpdatableFieldsEquals(returnedDealer, getPersistedDealer(returnedDealer));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDealer = returnedDealer;
    }

    @Test
    @Transactional
    void createDealerWithExistingId() throws Exception {
        // Create the Dealer with an existing ID
        dealer.setId(1L);
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDealerNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        // set the field null
        dealer.setDealerName(null);

        // Create the Dealer, which fails.
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDealers() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].identificationDocumentNumber").value(hasItem(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].otherNames").value(hasItem(DEFAULT_OTHER_NAMES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDealersWithEagerRelationshipsIsEnabled() throws Exception {
        when(dealerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDealerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dealerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDealersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dealerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDealerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dealerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get the dealer
        restDealerMockMvc
            .perform(get(ENTITY_API_URL_ID, dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dealer.getId().intValue()))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER))
            .andExpect(jsonPath("$.identificationDocumentNumber").value(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.postalAddress").value(DEFAULT_POSTAL_ADDRESS))
            .andExpect(jsonPath("$.physicalAddress").value(DEFAULT_PHYSICAL_ADDRESS))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.bankersName").value(DEFAULT_BANKERS_NAME))
            .andExpect(jsonPath("$.bankersBranch").value(DEFAULT_BANKERS_BRANCH))
            .andExpect(jsonPath("$.bankersSwiftCode").value(DEFAULT_BANKERS_SWIFT_CODE))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.otherNames").value(DEFAULT_OTHER_NAMES));
    }

    @Test
    @Transactional
    void getDealersByIdFiltering() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        Long id = dealer.getId();

        defaultDealerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDealerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDealerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName equals to
        defaultDealerFiltering("dealerName.equals=" + DEFAULT_DEALER_NAME, "dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName in
        defaultDealerFiltering("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME, "dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName is not null
        defaultDealerFiltering("dealerName.specified=true", "dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName contains
        defaultDealerFiltering("dealerName.contains=" + DEFAULT_DEALER_NAME, "dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName does not contain
        defaultDealerFiltering("dealerName.doesNotContain=" + UPDATED_DEALER_NAME, "dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber equals to
        defaultDealerFiltering("taxNumber.equals=" + DEFAULT_TAX_NUMBER, "taxNumber.equals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber in
        defaultDealerFiltering("taxNumber.in=" + DEFAULT_TAX_NUMBER + "," + UPDATED_TAX_NUMBER, "taxNumber.in=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber is not null
        defaultDealerFiltering("taxNumber.specified=true", "taxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber contains
        defaultDealerFiltering("taxNumber.contains=" + DEFAULT_TAX_NUMBER, "taxNumber.contains=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber does not contain
        defaultDealerFiltering("taxNumber.doesNotContain=" + UPDATED_TAX_NUMBER, "taxNumber.doesNotContain=" + DEFAULT_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber equals to
        defaultDealerFiltering(
            "identificationDocumentNumber.equals=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER,
            "identificationDocumentNumber.equals=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber in
        defaultDealerFiltering(
            "identificationDocumentNumber.in=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER + "," + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER,
            "identificationDocumentNumber.in=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber is not null
        defaultDealerFiltering("identificationDocumentNumber.specified=true", "identificationDocumentNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber contains
        defaultDealerFiltering(
            "identificationDocumentNumber.contains=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER,
            "identificationDocumentNumber.contains=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber does not contain
        defaultDealerFiltering(
            "identificationDocumentNumber.doesNotContain=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER,
            "identificationDocumentNumber.doesNotContain=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName equals to
        defaultDealerFiltering(
            "organizationName.equals=" + DEFAULT_ORGANIZATION_NAME,
            "organizationName.equals=" + UPDATED_ORGANIZATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName in
        defaultDealerFiltering(
            "organizationName.in=" + DEFAULT_ORGANIZATION_NAME + "," + UPDATED_ORGANIZATION_NAME,
            "organizationName.in=" + UPDATED_ORGANIZATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName is not null
        defaultDealerFiltering("organizationName.specified=true", "organizationName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName contains
        defaultDealerFiltering(
            "organizationName.contains=" + DEFAULT_ORGANIZATION_NAME,
            "organizationName.contains=" + UPDATED_ORGANIZATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName does not contain
        defaultDealerFiltering(
            "organizationName.doesNotContain=" + UPDATED_ORGANIZATION_NAME,
            "organizationName.doesNotContain=" + DEFAULT_ORGANIZATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department equals to
        defaultDealerFiltering("department.equals=" + DEFAULT_DEPARTMENT, "department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department in
        defaultDealerFiltering("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT, "department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department is not null
        defaultDealerFiltering("department.specified=true", "department.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department contains
        defaultDealerFiltering("department.contains=" + DEFAULT_DEPARTMENT, "department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department does not contain
        defaultDealerFiltering("department.doesNotContain=" + UPDATED_DEPARTMENT, "department.doesNotContain=" + DEFAULT_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position equals to
        defaultDealerFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position in
        defaultDealerFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position is not null
        defaultDealerFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position contains
        defaultDealerFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position does not contain
        defaultDealerFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress equals to
        defaultDealerFiltering("postalAddress.equals=" + DEFAULT_POSTAL_ADDRESS, "postalAddress.equals=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress in
        defaultDealerFiltering(
            "postalAddress.in=" + DEFAULT_POSTAL_ADDRESS + "," + UPDATED_POSTAL_ADDRESS,
            "postalAddress.in=" + UPDATED_POSTAL_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress is not null
        defaultDealerFiltering("postalAddress.specified=true", "postalAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress contains
        defaultDealerFiltering("postalAddress.contains=" + DEFAULT_POSTAL_ADDRESS, "postalAddress.contains=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress does not contain
        defaultDealerFiltering(
            "postalAddress.doesNotContain=" + UPDATED_POSTAL_ADDRESS,
            "postalAddress.doesNotContain=" + DEFAULT_POSTAL_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress equals to
        defaultDealerFiltering("physicalAddress.equals=" + DEFAULT_PHYSICAL_ADDRESS, "physicalAddress.equals=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress in
        defaultDealerFiltering(
            "physicalAddress.in=" + DEFAULT_PHYSICAL_ADDRESS + "," + UPDATED_PHYSICAL_ADDRESS,
            "physicalAddress.in=" + UPDATED_PHYSICAL_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress is not null
        defaultDealerFiltering("physicalAddress.specified=true", "physicalAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress contains
        defaultDealerFiltering(
            "physicalAddress.contains=" + DEFAULT_PHYSICAL_ADDRESS,
            "physicalAddress.contains=" + UPDATED_PHYSICAL_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress does not contain
        defaultDealerFiltering(
            "physicalAddress.doesNotContain=" + UPDATED_PHYSICAL_ADDRESS,
            "physicalAddress.doesNotContain=" + DEFAULT_PHYSICAL_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName equals to
        defaultDealerFiltering("accountName.equals=" + DEFAULT_ACCOUNT_NAME, "accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName in
        defaultDealerFiltering(
            "accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME,
            "accountName.in=" + UPDATED_ACCOUNT_NAME
        );
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName is not null
        defaultDealerFiltering("accountName.specified=true", "accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName contains
        defaultDealerFiltering("accountName.contains=" + DEFAULT_ACCOUNT_NAME, "accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName does not contain
        defaultDealerFiltering("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME, "accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber equals to
        defaultDealerFiltering("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER, "accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber in
        defaultDealerFiltering(
            "accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER,
            "accountNumber.in=" + UPDATED_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber is not null
        defaultDealerFiltering("accountNumber.specified=true", "accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber contains
        defaultDealerFiltering("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER, "accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber does not contain
        defaultDealerFiltering(
            "accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER,
            "accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName equals to
        defaultDealerFiltering("bankersName.equals=" + DEFAULT_BANKERS_NAME, "bankersName.equals=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName in
        defaultDealerFiltering(
            "bankersName.in=" + DEFAULT_BANKERS_NAME + "," + UPDATED_BANKERS_NAME,
            "bankersName.in=" + UPDATED_BANKERS_NAME
        );
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName is not null
        defaultDealerFiltering("bankersName.specified=true", "bankersName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName contains
        defaultDealerFiltering("bankersName.contains=" + DEFAULT_BANKERS_NAME, "bankersName.contains=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName does not contain
        defaultDealerFiltering("bankersName.doesNotContain=" + UPDATED_BANKERS_NAME, "bankersName.doesNotContain=" + DEFAULT_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch equals to
        defaultDealerFiltering("bankersBranch.equals=" + DEFAULT_BANKERS_BRANCH, "bankersBranch.equals=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch in
        defaultDealerFiltering(
            "bankersBranch.in=" + DEFAULT_BANKERS_BRANCH + "," + UPDATED_BANKERS_BRANCH,
            "bankersBranch.in=" + UPDATED_BANKERS_BRANCH
        );
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch is not null
        defaultDealerFiltering("bankersBranch.specified=true", "bankersBranch.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch contains
        defaultDealerFiltering("bankersBranch.contains=" + DEFAULT_BANKERS_BRANCH, "bankersBranch.contains=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch does not contain
        defaultDealerFiltering(
            "bankersBranch.doesNotContain=" + UPDATED_BANKERS_BRANCH,
            "bankersBranch.doesNotContain=" + DEFAULT_BANKERS_BRANCH
        );
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode equals to
        defaultDealerFiltering(
            "bankersSwiftCode.equals=" + DEFAULT_BANKERS_SWIFT_CODE,
            "bankersSwiftCode.equals=" + UPDATED_BANKERS_SWIFT_CODE
        );
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode in
        defaultDealerFiltering(
            "bankersSwiftCode.in=" + DEFAULT_BANKERS_SWIFT_CODE + "," + UPDATED_BANKERS_SWIFT_CODE,
            "bankersSwiftCode.in=" + UPDATED_BANKERS_SWIFT_CODE
        );
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode is not null
        defaultDealerFiltering("bankersSwiftCode.specified=true", "bankersSwiftCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode contains
        defaultDealerFiltering(
            "bankersSwiftCode.contains=" + DEFAULT_BANKERS_SWIFT_CODE,
            "bankersSwiftCode.contains=" + UPDATED_BANKERS_SWIFT_CODE
        );
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode does not contain
        defaultDealerFiltering(
            "bankersSwiftCode.doesNotContain=" + UPDATED_BANKERS_SWIFT_CODE,
            "bankersSwiftCode.doesNotContain=" + DEFAULT_BANKERS_SWIFT_CODE
        );
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken equals to
        defaultDealerFiltering(
            "fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN,
            "fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken in
        defaultDealerFiltering(
            "fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN,
            "fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken is not null
        defaultDealerFiltering("fileUploadToken.specified=true", "fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken contains
        defaultDealerFiltering(
            "fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN,
            "fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken does not contain
        defaultDealerFiltering(
            "fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN,
            "fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken equals to
        defaultDealerFiltering(
            "compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN,
            "compilationToken.equals=" + UPDATED_COMPILATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken in
        defaultDealerFiltering(
            "compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN,
            "compilationToken.in=" + UPDATED_COMPILATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken is not null
        defaultDealerFiltering("compilationToken.specified=true", "compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken contains
        defaultDealerFiltering(
            "compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN,
            "compilationToken.contains=" + UPDATED_COMPILATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken does not contain
        defaultDealerFiltering(
            "compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN,
            "compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames equals to
        defaultDealerFiltering("otherNames.equals=" + DEFAULT_OTHER_NAMES, "otherNames.equals=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames in
        defaultDealerFiltering("otherNames.in=" + DEFAULT_OTHER_NAMES + "," + UPDATED_OTHER_NAMES, "otherNames.in=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames is not null
        defaultDealerFiltering("otherNames.specified=true", "otherNames.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames contains
        defaultDealerFiltering("otherNames.contains=" + DEFAULT_OTHER_NAMES, "otherNames.contains=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames does not contain
        defaultDealerFiltering("otherNames.doesNotContain=" + UPDATED_OTHER_NAMES, "otherNames.doesNotContain=" + DEFAULT_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByDealerGroupIsEqualToSomething() throws Exception {
        Dealer dealerGroup;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealerRepository.saveAndFlush(dealer);
            dealerGroup = DealerResourceIT.createEntity();
        } else {
            dealerGroup = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealerGroup);
        em.flush();
        dealer.setDealerGroup(dealerGroup);
        dealerRepository.saveAndFlush(dealer);
        Long dealerGroupId = dealerGroup.getId();
        // Get all the dealerList where dealerGroup equals to dealerGroupId
        defaultDealerShouldBeFound("dealerGroupId.equals=" + dealerGroupId);

        // Get all the dealerList where dealerGroup equals to (dealerGroupId + 1)
        defaultDealerShouldNotBeFound("dealerGroupId.equals=" + (dealerGroupId + 1));
    }

    @Test
    @Transactional
    void getAllDealersByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            dealerRepository.saveAndFlush(dealer);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        dealer.addPlaceholder(placeholder);
        dealerRepository.saveAndFlush(dealer);
        Long placeholderId = placeholder.getId();
        // Get all the dealerList where placeholder equals to placeholderId
        defaultDealerShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the dealerList where placeholder equals to (placeholderId + 1)
        defaultDealerShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultDealerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDealerShouldBeFound(shouldBeFound);
        defaultDealerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDealerShouldBeFound(String filter) throws Exception {
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].identificationDocumentNumber").value(hasItem(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].otherNames").value(hasItem(DEFAULT_OTHER_NAMES)));

        // Check, that the count call also returns 1
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDealerShouldNotBeFound(String filter) throws Exception {
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDealer() throws Exception {
        // Get the dealer
        restDealerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealerSearchRepository.save(dealer);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());

        // Update the dealer
        Dealer updatedDealer = dealerRepository.findById(dealer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDealer are not directly saved in db
        em.detach(updatedDealer);
        updatedDealer
            .dealerName(UPDATED_DEALER_NAME)
            .taxNumber(UPDATED_TAX_NUMBER)
            .identificationDocumentNumber(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS)
            .otherNames(UPDATED_OTHER_NAMES);
        DealerDTO dealerDTO = dealerMapper.toDto(updatedDealer);

        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dealerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDealerToMatchAllProperties(updatedDealer);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Dealer> dealerSearchList = Streamable.of(dealerSearchRepository.findAll()).toList();
                Dealer testDealerSearch = dealerSearchList.get(searchDatabaseSizeAfter - 1);

                assertDealerAllPropertiesEquals(testDealerSearch, updatedDealer);
            });
    }

    @Test
    @Transactional
    void putNonExistingDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dealerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .position(UPDATED_POSITION)
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE);

        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDealer))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDealer, dealer), getPersistedDealer(dealer));
    }

    @Test
    @Transactional
    void fullUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer
            .dealerName(UPDATED_DEALER_NAME)
            .taxNumber(UPDATED_TAX_NUMBER)
            .identificationDocumentNumber(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS)
            .otherNames(UPDATED_OTHER_NAMES);

        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDealer))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealerUpdatableFieldsEquals(partialUpdatedDealer, getPersistedDealer(partialUpdatedDealer));
    }

    @Test
    @Transactional
    void patchNonExistingDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dealerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dealerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);
        dealerRepository.save(dealer);
        dealerSearchRepository.save(dealer);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the dealer
        restDealerMockMvc
            .perform(delete(ENTITY_API_URL_ID, dealer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(dealerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);
        dealerSearchRepository.save(dealer);

        // Search the dealer
        restDealerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].identificationDocumentNumber").value(hasItem(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())))
            .andExpect(jsonPath("$.[*].otherNames").value(hasItem(DEFAULT_OTHER_NAMES)));
    }

    protected long getRepositoryCount() {
        return dealerRepository.count();
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

    protected Dealer getPersistedDealer(Dealer dealer) {
        return dealerRepository.findById(dealer.getId()).orElseThrow();
    }

    protected void assertPersistedDealerToMatchAllProperties(Dealer expectedDealer) {
        assertDealerAllPropertiesEquals(expectedDealer, getPersistedDealer(expectedDealer));
    }

    protected void assertPersistedDealerToMatchUpdatableProperties(Dealer expectedDealer) {
        assertDealerAllUpdatablePropertiesEquals(expectedDealer, getPersistedDealer(expectedDealer));
    }
}
