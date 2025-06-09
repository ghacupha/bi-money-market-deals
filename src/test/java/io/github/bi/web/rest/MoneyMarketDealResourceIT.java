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

import static io.github.bi.domain.MoneyMarketDealAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static io.github.bi.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.MoneyMarketDeal;
import io.github.bi.domain.MoneyMarketList;
import io.github.bi.repository.MoneyMarketDealRepository;
import io.github.bi.repository.search.MoneyMarketDealSearchRepository;
import io.github.bi.service.MoneyMarketDealService;
import io.github.bi.service.dto.MoneyMarketDealDTO;
import io.github.bi.service.mapper.MoneyMarketDealMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MoneyMarketDealResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MoneyMarketDealResourceIT {

    private static final String DEFAULT_DEAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TRADING_BOOK = "AAAAAAAAAA";
    private static final String UPDATED_TRADING_BOOK = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTER_PARTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_PARTY_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FINAL_INTEREST_ACCRUAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINAL_INTEREST_ACCRUAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FINAL_INTEREST_ACCRUAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COUNTER_PARTY_SIDE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_PARTY_SIDE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_OF_COLLECTION_STATEMENT = "AAAAAAAAAA";
    private static final String UPDATED_DATE_OF_COLLECTION_STATEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRINCIPAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRINCIPAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRINCIPAL_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_RATE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_ACCRUED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_ACCRUED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_ACCRUED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_INTEREST_AT_MATURITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_INTEREST_AT_MATURITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_INTEREST_AT_MATURITY = new BigDecimal(1 - 1);

    private static final String DEFAULT_COUNTERPARTY_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_NATIONALITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TREASURY_LEDGER = "AAAAAAAAAA";
    private static final String UPDATED_TREASURY_LEDGER = "BBBBBBBBBB";

    private static final String DEFAULT_DEAL_SUBTYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEAL_SUBTYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHILLING_EQUIVALENT_PRINCIPAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_SHILLING_EQUIVALENT_PRINCIPAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED = new BigDecimal(2);
    private static final BigDecimal SMALLER_SHILLING_EQUIVALENT_INTEREST_ACCRUED = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SHILLING_EQUIVALENT_PV_FULL = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHILLING_EQUIVALENT_PV_FULL = new BigDecimal(2);
    private static final BigDecimal SMALLER_SHILLING_EQUIVALENT_PV_FULL = new BigDecimal(1 - 1);

    private static final String DEFAULT_COUNTERPARTY_DOMICILE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_DOMICILE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SETTLEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SETTLEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SETTLEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_COLLATERAL = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_COLLATERAL = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MATURITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MATURITY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MATURITY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_INSTITUTION_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_REPORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/money-market-deals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/money-market-deals/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyMarketDealRepository moneyMarketDealRepository;

    @Mock
    private MoneyMarketDealRepository moneyMarketDealRepositoryMock;

    @Autowired
    private MoneyMarketDealMapper moneyMarketDealMapper;

    @Mock
    private MoneyMarketDealService moneyMarketDealServiceMock;

    @Autowired
    private MoneyMarketDealSearchRepository moneyMarketDealSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyMarketDealMockMvc;

    private MoneyMarketDeal moneyMarketDeal;

    private MoneyMarketDeal insertedMoneyMarketDeal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketDeal createEntity(EntityManager em) {
        MoneyMarketDeal moneyMarketDeal = new MoneyMarketDeal()
            .dealNumber(DEFAULT_DEAL_NUMBER)
            .tradingBook(DEFAULT_TRADING_BOOK)
            .counterPartyName(DEFAULT_COUNTER_PARTY_NAME)
            .finalInterestAccrualDate(DEFAULT_FINAL_INTEREST_ACCRUAL_DATE)
            .counterPartySideType(DEFAULT_COUNTER_PARTY_SIDE_TYPE)
            .dateOfCollectionStatement(DEFAULT_DATE_OF_COLLECTION_STATEMENT)
            .currencyCode(DEFAULT_CURRENCY_CODE)
            .principalAmount(DEFAULT_PRINCIPAL_AMOUNT)
            .interestRate(DEFAULT_INTEREST_RATE)
            .interestAccruedAmount(DEFAULT_INTEREST_ACCRUED_AMOUNT)
            .totalInterestAtMaturity(DEFAULT_TOTAL_INTEREST_AT_MATURITY)
            .counterpartyNationality(DEFAULT_COUNTERPARTY_NATIONALITY)
            .endDate(DEFAULT_END_DATE)
            .treasuryLedger(DEFAULT_TREASURY_LEDGER)
            .dealSubtype(DEFAULT_DEAL_SUBTYPE)
            .shillingEquivalentPrincipal(DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL)
            .shillingEquivalentInterestAccrued(DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED)
            .shillingEquivalentPVFull(DEFAULT_SHILLING_EQUIVALENT_PV_FULL)
            .counterpartyDomicile(DEFAULT_COUNTERPARTY_DOMICILE)
            .settlementDate(DEFAULT_SETTLEMENT_DATE)
            .transactionCollateral(DEFAULT_TRANSACTION_COLLATERAL)
            .institutionType(DEFAULT_INSTITUTION_TYPE)
            .maturityDate(DEFAULT_MATURITY_DATE)
            .institutionReportName(DEFAULT_INSTITUTION_REPORT_NAME)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .reportDate(DEFAULT_REPORT_DATE)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        MoneyMarketList moneyMarketList;
        if (TestUtil.findAll(em, MoneyMarketList.class).isEmpty()) {
            moneyMarketList = MoneyMarketListResourceIT.createEntity();
            em.persist(moneyMarketList);
            em.flush();
        } else {
            moneyMarketList = TestUtil.findAll(em, MoneyMarketList.class).get(0);
        }
        moneyMarketDeal.setMoneyMarketList(moneyMarketList);
        return moneyMarketDeal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketDeal createUpdatedEntity(EntityManager em) {
        MoneyMarketDeal updatedMoneyMarketDeal = new MoneyMarketDeal()
            .dealNumber(UPDATED_DEAL_NUMBER)
            .tradingBook(UPDATED_TRADING_BOOK)
            .counterPartyName(UPDATED_COUNTER_PARTY_NAME)
            .finalInterestAccrualDate(UPDATED_FINAL_INTEREST_ACCRUAL_DATE)
            .counterPartySideType(UPDATED_COUNTER_PARTY_SIDE_TYPE)
            .dateOfCollectionStatement(UPDATED_DATE_OF_COLLECTION_STATEMENT)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .principalAmount(UPDATED_PRINCIPAL_AMOUNT)
            .interestRate(UPDATED_INTEREST_RATE)
            .interestAccruedAmount(UPDATED_INTEREST_ACCRUED_AMOUNT)
            .totalInterestAtMaturity(UPDATED_TOTAL_INTEREST_AT_MATURITY)
            .counterpartyNationality(UPDATED_COUNTERPARTY_NATIONALITY)
            .endDate(UPDATED_END_DATE)
            .treasuryLedger(UPDATED_TREASURY_LEDGER)
            .dealSubtype(UPDATED_DEAL_SUBTYPE)
            .shillingEquivalentPrincipal(UPDATED_SHILLING_EQUIVALENT_PRINCIPAL)
            .shillingEquivalentInterestAccrued(UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED)
            .shillingEquivalentPVFull(UPDATED_SHILLING_EQUIVALENT_PV_FULL)
            .counterpartyDomicile(UPDATED_COUNTERPARTY_DOMICILE)
            .settlementDate(UPDATED_SETTLEMENT_DATE)
            .transactionCollateral(UPDATED_TRANSACTION_COLLATERAL)
            .institutionType(UPDATED_INSTITUTION_TYPE)
            .maturityDate(UPDATED_MATURITY_DATE)
            .institutionReportName(UPDATED_INSTITUTION_REPORT_NAME)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .reportDate(UPDATED_REPORT_DATE)
            .active(UPDATED_ACTIVE);
        // Add required entity
        MoneyMarketList moneyMarketList;
        if (TestUtil.findAll(em, MoneyMarketList.class).isEmpty()) {
            moneyMarketList = MoneyMarketListResourceIT.createUpdatedEntity();
            em.persist(moneyMarketList);
            em.flush();
        } else {
            moneyMarketList = TestUtil.findAll(em, MoneyMarketList.class).get(0);
        }
        updatedMoneyMarketDeal.setMoneyMarketList(moneyMarketList);
        return updatedMoneyMarketDeal;
    }

    @BeforeEach
    void initTest() {
        moneyMarketDeal = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMoneyMarketDeal != null) {
            moneyMarketDealRepository.delete(insertedMoneyMarketDeal);
            moneyMarketDealSearchRepository.delete(insertedMoneyMarketDeal);
            insertedMoneyMarketDeal = null;
        }
    }

    @Test
    @Transactional
    void createMoneyMarketDeal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // Create the MoneyMarketDeal
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);
        var returnedMoneyMarketDealDTO = om.readValue(
            restMoneyMarketDealMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoneyMarketDealDTO.class
        );

        // Validate the MoneyMarketDeal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoneyMarketDeal = moneyMarketDealMapper.toEntity(returnedMoneyMarketDealDTO);
        assertMoneyMarketDealUpdatableFieldsEquals(returnedMoneyMarketDeal, getPersistedMoneyMarketDeal(returnedMoneyMarketDeal));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMoneyMarketDeal = returnedMoneyMarketDeal;
    }

    @Test
    @Transactional
    void createMoneyMarketDealWithExistingId() throws Exception {
        // Create the MoneyMarketDeal with an existing ID
        moneyMarketDeal.setId(1L);
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDealNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // set the field null
        moneyMarketDeal.setDealNumber(null);

        // Create the MoneyMarketDeal, which fails.
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFinalInterestAccrualDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // set the field null
        moneyMarketDeal.setFinalInterestAccrualDate(null);

        // Create the MoneyMarketDeal, which fails.
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // set the field null
        moneyMarketDeal.setEndDate(null);

        // Create the MoneyMarketDeal, which fails.
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSettlementDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // set the field null
        moneyMarketDeal.setSettlementDate(null);

        // Create the MoneyMarketDeal, which fails.
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMaturityDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // set the field null
        moneyMarketDeal.setMaturityDate(null);

        // Create the MoneyMarketDeal, which fails.
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // set the field null
        moneyMarketDeal.setReportDate(null);

        // Create the MoneyMarketDeal, which fails.
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        // set the field null
        moneyMarketDeal.setActive(null);

        // Create the MoneyMarketDeal, which fails.
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDeals() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList
        restMoneyMarketDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketDeal.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealNumber").value(hasItem(DEFAULT_DEAL_NUMBER)))
            .andExpect(jsonPath("$.[*].tradingBook").value(hasItem(DEFAULT_TRADING_BOOK)))
            .andExpect(jsonPath("$.[*].counterPartyName").value(hasItem(DEFAULT_COUNTER_PARTY_NAME)))
            .andExpect(jsonPath("$.[*].finalInterestAccrualDate").value(hasItem(DEFAULT_FINAL_INTEREST_ACCRUAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].counterPartySideType").value(hasItem(DEFAULT_COUNTER_PARTY_SIDE_TYPE)))
            .andExpect(jsonPath("$.[*].dateOfCollectionStatement").value(hasItem(DEFAULT_DATE_OF_COLLECTION_STATEMENT)))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].principalAmount").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(sameNumber(DEFAULT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].interestAccruedAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalInterestAtMaturity").value(hasItem(sameNumber(DEFAULT_TOTAL_INTEREST_AT_MATURITY))))
            .andExpect(jsonPath("$.[*].counterpartyNationality").value(hasItem(DEFAULT_COUNTERPARTY_NATIONALITY)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].treasuryLedger").value(hasItem(DEFAULT_TREASURY_LEDGER)))
            .andExpect(jsonPath("$.[*].dealSubtype").value(hasItem(DEFAULT_DEAL_SUBTYPE)))
            .andExpect(jsonPath("$.[*].shillingEquivalentPrincipal").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL))))
            .andExpect(
                jsonPath("$.[*].shillingEquivalentInterestAccrued").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED)))
            )
            .andExpect(jsonPath("$.[*].shillingEquivalentPVFull").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PV_FULL))))
            .andExpect(jsonPath("$.[*].counterpartyDomicile").value(hasItem(DEFAULT_COUNTERPARTY_DOMICILE)))
            .andExpect(jsonPath("$.[*].settlementDate").value(hasItem(DEFAULT_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionCollateral").value(hasItem(DEFAULT_TRANSACTION_COLLATERAL)))
            .andExpect(jsonPath("$.[*].institutionType").value(hasItem(DEFAULT_INSTITUTION_TYPE)))
            .andExpect(jsonPath("$.[*].maturityDate").value(hasItem(DEFAULT_MATURITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].institutionReportName").value(hasItem(DEFAULT_INSTITUTION_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyMarketDealsWithEagerRelationshipsIsEnabled() throws Exception {
        when(moneyMarketDealServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyMarketDealMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(moneyMarketDealServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyMarketDealsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(moneyMarketDealServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyMarketDealMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(moneyMarketDealRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMoneyMarketDeal() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get the moneyMarketDeal
        restMoneyMarketDealMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyMarketDeal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyMarketDeal.getId().intValue()))
            .andExpect(jsonPath("$.dealNumber").value(DEFAULT_DEAL_NUMBER))
            .andExpect(jsonPath("$.tradingBook").value(DEFAULT_TRADING_BOOK))
            .andExpect(jsonPath("$.counterPartyName").value(DEFAULT_COUNTER_PARTY_NAME))
            .andExpect(jsonPath("$.finalInterestAccrualDate").value(DEFAULT_FINAL_INTEREST_ACCRUAL_DATE.toString()))
            .andExpect(jsonPath("$.counterPartySideType").value(DEFAULT_COUNTER_PARTY_SIDE_TYPE))
            .andExpect(jsonPath("$.dateOfCollectionStatement").value(DEFAULT_DATE_OF_COLLECTION_STATEMENT))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE))
            .andExpect(jsonPath("$.principalAmount").value(sameNumber(DEFAULT_PRINCIPAL_AMOUNT)))
            .andExpect(jsonPath("$.interestRate").value(sameNumber(DEFAULT_INTEREST_RATE)))
            .andExpect(jsonPath("$.interestAccruedAmount").value(sameNumber(DEFAULT_INTEREST_ACCRUED_AMOUNT)))
            .andExpect(jsonPath("$.totalInterestAtMaturity").value(sameNumber(DEFAULT_TOTAL_INTEREST_AT_MATURITY)))
            .andExpect(jsonPath("$.counterpartyNationality").value(DEFAULT_COUNTERPARTY_NATIONALITY))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.treasuryLedger").value(DEFAULT_TREASURY_LEDGER))
            .andExpect(jsonPath("$.dealSubtype").value(DEFAULT_DEAL_SUBTYPE))
            .andExpect(jsonPath("$.shillingEquivalentPrincipal").value(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL)))
            .andExpect(jsonPath("$.shillingEquivalentInterestAccrued").value(sameNumber(DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED)))
            .andExpect(jsonPath("$.shillingEquivalentPVFull").value(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PV_FULL)))
            .andExpect(jsonPath("$.counterpartyDomicile").value(DEFAULT_COUNTERPARTY_DOMICILE))
            .andExpect(jsonPath("$.settlementDate").value(DEFAULT_SETTLEMENT_DATE.toString()))
            .andExpect(jsonPath("$.transactionCollateral").value(DEFAULT_TRANSACTION_COLLATERAL))
            .andExpect(jsonPath("$.institutionType").value(DEFAULT_INSTITUTION_TYPE))
            .andExpect(jsonPath("$.maturityDate").value(DEFAULT_MATURITY_DATE.toString()))
            .andExpect(jsonPath("$.institutionReportName").value(DEFAULT_INSTITUTION_REPORT_NAME))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getMoneyMarketDealsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        Long id = moneyMarketDeal.getId();

        defaultMoneyMarketDealFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMoneyMarketDealFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMoneyMarketDealFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealNumber equals to
        defaultMoneyMarketDealFiltering("dealNumber.equals=" + DEFAULT_DEAL_NUMBER, "dealNumber.equals=" + UPDATED_DEAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealNumber in
        defaultMoneyMarketDealFiltering(
            "dealNumber.in=" + DEFAULT_DEAL_NUMBER + "," + UPDATED_DEAL_NUMBER,
            "dealNumber.in=" + UPDATED_DEAL_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealNumber is not null
        defaultMoneyMarketDealFiltering("dealNumber.specified=true", "dealNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealNumber contains
        defaultMoneyMarketDealFiltering("dealNumber.contains=" + DEFAULT_DEAL_NUMBER, "dealNumber.contains=" + UPDATED_DEAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealNumber does not contain
        defaultMoneyMarketDealFiltering(
            "dealNumber.doesNotContain=" + UPDATED_DEAL_NUMBER,
            "dealNumber.doesNotContain=" + DEFAULT_DEAL_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTradingBookIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where tradingBook equals to
        defaultMoneyMarketDealFiltering("tradingBook.equals=" + DEFAULT_TRADING_BOOK, "tradingBook.equals=" + UPDATED_TRADING_BOOK);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTradingBookIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where tradingBook in
        defaultMoneyMarketDealFiltering(
            "tradingBook.in=" + DEFAULT_TRADING_BOOK + "," + UPDATED_TRADING_BOOK,
            "tradingBook.in=" + UPDATED_TRADING_BOOK
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTradingBookIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where tradingBook is not null
        defaultMoneyMarketDealFiltering("tradingBook.specified=true", "tradingBook.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTradingBookContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where tradingBook contains
        defaultMoneyMarketDealFiltering("tradingBook.contains=" + DEFAULT_TRADING_BOOK, "tradingBook.contains=" + UPDATED_TRADING_BOOK);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTradingBookNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where tradingBook does not contain
        defaultMoneyMarketDealFiltering(
            "tradingBook.doesNotContain=" + UPDATED_TRADING_BOOK,
            "tradingBook.doesNotContain=" + DEFAULT_TRADING_BOOK
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartyName equals to
        defaultMoneyMarketDealFiltering(
            "counterPartyName.equals=" + DEFAULT_COUNTER_PARTY_NAME,
            "counterPartyName.equals=" + UPDATED_COUNTER_PARTY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartyName in
        defaultMoneyMarketDealFiltering(
            "counterPartyName.in=" + DEFAULT_COUNTER_PARTY_NAME + "," + UPDATED_COUNTER_PARTY_NAME,
            "counterPartyName.in=" + UPDATED_COUNTER_PARTY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartyName is not null
        defaultMoneyMarketDealFiltering("counterPartyName.specified=true", "counterPartyName.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartyName contains
        defaultMoneyMarketDealFiltering(
            "counterPartyName.contains=" + DEFAULT_COUNTER_PARTY_NAME,
            "counterPartyName.contains=" + UPDATED_COUNTER_PARTY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartyName does not contain
        defaultMoneyMarketDealFiltering(
            "counterPartyName.doesNotContain=" + UPDATED_COUNTER_PARTY_NAME,
            "counterPartyName.doesNotContain=" + DEFAULT_COUNTER_PARTY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByFinalInterestAccrualDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where finalInterestAccrualDate equals to
        defaultMoneyMarketDealFiltering(
            "finalInterestAccrualDate.equals=" + DEFAULT_FINAL_INTEREST_ACCRUAL_DATE,
            "finalInterestAccrualDate.equals=" + UPDATED_FINAL_INTEREST_ACCRUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByFinalInterestAccrualDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where finalInterestAccrualDate in
        defaultMoneyMarketDealFiltering(
            "finalInterestAccrualDate.in=" + DEFAULT_FINAL_INTEREST_ACCRUAL_DATE + "," + UPDATED_FINAL_INTEREST_ACCRUAL_DATE,
            "finalInterestAccrualDate.in=" + UPDATED_FINAL_INTEREST_ACCRUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByFinalInterestAccrualDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where finalInterestAccrualDate is not null
        defaultMoneyMarketDealFiltering("finalInterestAccrualDate.specified=true", "finalInterestAccrualDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByFinalInterestAccrualDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where finalInterestAccrualDate is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "finalInterestAccrualDate.greaterThanOrEqual=" + DEFAULT_FINAL_INTEREST_ACCRUAL_DATE,
            "finalInterestAccrualDate.greaterThanOrEqual=" + UPDATED_FINAL_INTEREST_ACCRUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByFinalInterestAccrualDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where finalInterestAccrualDate is less than or equal to
        defaultMoneyMarketDealFiltering(
            "finalInterestAccrualDate.lessThanOrEqual=" + DEFAULT_FINAL_INTEREST_ACCRUAL_DATE,
            "finalInterestAccrualDate.lessThanOrEqual=" + SMALLER_FINAL_INTEREST_ACCRUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByFinalInterestAccrualDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where finalInterestAccrualDate is less than
        defaultMoneyMarketDealFiltering(
            "finalInterestAccrualDate.lessThan=" + UPDATED_FINAL_INTEREST_ACCRUAL_DATE,
            "finalInterestAccrualDate.lessThan=" + DEFAULT_FINAL_INTEREST_ACCRUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByFinalInterestAccrualDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where finalInterestAccrualDate is greater than
        defaultMoneyMarketDealFiltering(
            "finalInterestAccrualDate.greaterThan=" + SMALLER_FINAL_INTEREST_ACCRUAL_DATE,
            "finalInterestAccrualDate.greaterThan=" + DEFAULT_FINAL_INTEREST_ACCRUAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartySideTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartySideType equals to
        defaultMoneyMarketDealFiltering(
            "counterPartySideType.equals=" + DEFAULT_COUNTER_PARTY_SIDE_TYPE,
            "counterPartySideType.equals=" + UPDATED_COUNTER_PARTY_SIDE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartySideTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartySideType in
        defaultMoneyMarketDealFiltering(
            "counterPartySideType.in=" + DEFAULT_COUNTER_PARTY_SIDE_TYPE + "," + UPDATED_COUNTER_PARTY_SIDE_TYPE,
            "counterPartySideType.in=" + UPDATED_COUNTER_PARTY_SIDE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartySideTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartySideType is not null
        defaultMoneyMarketDealFiltering("counterPartySideType.specified=true", "counterPartySideType.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartySideTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartySideType contains
        defaultMoneyMarketDealFiltering(
            "counterPartySideType.contains=" + DEFAULT_COUNTER_PARTY_SIDE_TYPE,
            "counterPartySideType.contains=" + UPDATED_COUNTER_PARTY_SIDE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterPartySideTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterPartySideType does not contain
        defaultMoneyMarketDealFiltering(
            "counterPartySideType.doesNotContain=" + UPDATED_COUNTER_PARTY_SIDE_TYPE,
            "counterPartySideType.doesNotContain=" + DEFAULT_COUNTER_PARTY_SIDE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDateOfCollectionStatementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dateOfCollectionStatement equals to
        defaultMoneyMarketDealFiltering(
            "dateOfCollectionStatement.equals=" + DEFAULT_DATE_OF_COLLECTION_STATEMENT,
            "dateOfCollectionStatement.equals=" + UPDATED_DATE_OF_COLLECTION_STATEMENT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDateOfCollectionStatementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dateOfCollectionStatement in
        defaultMoneyMarketDealFiltering(
            "dateOfCollectionStatement.in=" + DEFAULT_DATE_OF_COLLECTION_STATEMENT + "," + UPDATED_DATE_OF_COLLECTION_STATEMENT,
            "dateOfCollectionStatement.in=" + UPDATED_DATE_OF_COLLECTION_STATEMENT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDateOfCollectionStatementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dateOfCollectionStatement is not null
        defaultMoneyMarketDealFiltering("dateOfCollectionStatement.specified=true", "dateOfCollectionStatement.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDateOfCollectionStatementContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dateOfCollectionStatement contains
        defaultMoneyMarketDealFiltering(
            "dateOfCollectionStatement.contains=" + DEFAULT_DATE_OF_COLLECTION_STATEMENT,
            "dateOfCollectionStatement.contains=" + UPDATED_DATE_OF_COLLECTION_STATEMENT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDateOfCollectionStatementNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dateOfCollectionStatement does not contain
        defaultMoneyMarketDealFiltering(
            "dateOfCollectionStatement.doesNotContain=" + UPDATED_DATE_OF_COLLECTION_STATEMENT,
            "dateOfCollectionStatement.doesNotContain=" + DEFAULT_DATE_OF_COLLECTION_STATEMENT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where currencyCode equals to
        defaultMoneyMarketDealFiltering("currencyCode.equals=" + DEFAULT_CURRENCY_CODE, "currencyCode.equals=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCurrencyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where currencyCode in
        defaultMoneyMarketDealFiltering(
            "currencyCode.in=" + DEFAULT_CURRENCY_CODE + "," + UPDATED_CURRENCY_CODE,
            "currencyCode.in=" + UPDATED_CURRENCY_CODE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCurrencyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where currencyCode is not null
        defaultMoneyMarketDealFiltering("currencyCode.specified=true", "currencyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCurrencyCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where currencyCode contains
        defaultMoneyMarketDealFiltering("currencyCode.contains=" + DEFAULT_CURRENCY_CODE, "currencyCode.contains=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCurrencyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where currencyCode does not contain
        defaultMoneyMarketDealFiltering(
            "currencyCode.doesNotContain=" + UPDATED_CURRENCY_CODE,
            "currencyCode.doesNotContain=" + DEFAULT_CURRENCY_CODE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByPrincipalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where principalAmount equals to
        defaultMoneyMarketDealFiltering(
            "principalAmount.equals=" + DEFAULT_PRINCIPAL_AMOUNT,
            "principalAmount.equals=" + UPDATED_PRINCIPAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByPrincipalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where principalAmount in
        defaultMoneyMarketDealFiltering(
            "principalAmount.in=" + DEFAULT_PRINCIPAL_AMOUNT + "," + UPDATED_PRINCIPAL_AMOUNT,
            "principalAmount.in=" + UPDATED_PRINCIPAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByPrincipalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where principalAmount is not null
        defaultMoneyMarketDealFiltering("principalAmount.specified=true", "principalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByPrincipalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where principalAmount is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "principalAmount.greaterThanOrEqual=" + DEFAULT_PRINCIPAL_AMOUNT,
            "principalAmount.greaterThanOrEqual=" + UPDATED_PRINCIPAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByPrincipalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where principalAmount is less than or equal to
        defaultMoneyMarketDealFiltering(
            "principalAmount.lessThanOrEqual=" + DEFAULT_PRINCIPAL_AMOUNT,
            "principalAmount.lessThanOrEqual=" + SMALLER_PRINCIPAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByPrincipalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where principalAmount is less than
        defaultMoneyMarketDealFiltering(
            "principalAmount.lessThan=" + UPDATED_PRINCIPAL_AMOUNT,
            "principalAmount.lessThan=" + DEFAULT_PRINCIPAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByPrincipalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where principalAmount is greater than
        defaultMoneyMarketDealFiltering(
            "principalAmount.greaterThan=" + SMALLER_PRINCIPAL_AMOUNT,
            "principalAmount.greaterThan=" + DEFAULT_PRINCIPAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestRate equals to
        defaultMoneyMarketDealFiltering("interestRate.equals=" + DEFAULT_INTEREST_RATE, "interestRate.equals=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestRate in
        defaultMoneyMarketDealFiltering(
            "interestRate.in=" + DEFAULT_INTEREST_RATE + "," + UPDATED_INTEREST_RATE,
            "interestRate.in=" + UPDATED_INTEREST_RATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestRate is not null
        defaultMoneyMarketDealFiltering("interestRate.specified=true", "interestRate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestRate is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "interestRate.greaterThanOrEqual=" + DEFAULT_INTEREST_RATE,
            "interestRate.greaterThanOrEqual=" + UPDATED_INTEREST_RATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestRate is less than or equal to
        defaultMoneyMarketDealFiltering(
            "interestRate.lessThanOrEqual=" + DEFAULT_INTEREST_RATE,
            "interestRate.lessThanOrEqual=" + SMALLER_INTEREST_RATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestRate is less than
        defaultMoneyMarketDealFiltering("interestRate.lessThan=" + UPDATED_INTEREST_RATE, "interestRate.lessThan=" + DEFAULT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestRate is greater than
        defaultMoneyMarketDealFiltering(
            "interestRate.greaterThan=" + SMALLER_INTEREST_RATE,
            "interestRate.greaterThan=" + DEFAULT_INTEREST_RATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestAccruedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestAccruedAmount equals to
        defaultMoneyMarketDealFiltering(
            "interestAccruedAmount.equals=" + DEFAULT_INTEREST_ACCRUED_AMOUNT,
            "interestAccruedAmount.equals=" + UPDATED_INTEREST_ACCRUED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestAccruedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestAccruedAmount in
        defaultMoneyMarketDealFiltering(
            "interestAccruedAmount.in=" + DEFAULT_INTEREST_ACCRUED_AMOUNT + "," + UPDATED_INTEREST_ACCRUED_AMOUNT,
            "interestAccruedAmount.in=" + UPDATED_INTEREST_ACCRUED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestAccruedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestAccruedAmount is not null
        defaultMoneyMarketDealFiltering("interestAccruedAmount.specified=true", "interestAccruedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestAccruedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestAccruedAmount is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "interestAccruedAmount.greaterThanOrEqual=" + DEFAULT_INTEREST_ACCRUED_AMOUNT,
            "interestAccruedAmount.greaterThanOrEqual=" + UPDATED_INTEREST_ACCRUED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestAccruedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestAccruedAmount is less than or equal to
        defaultMoneyMarketDealFiltering(
            "interestAccruedAmount.lessThanOrEqual=" + DEFAULT_INTEREST_ACCRUED_AMOUNT,
            "interestAccruedAmount.lessThanOrEqual=" + SMALLER_INTEREST_ACCRUED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestAccruedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestAccruedAmount is less than
        defaultMoneyMarketDealFiltering(
            "interestAccruedAmount.lessThan=" + UPDATED_INTEREST_ACCRUED_AMOUNT,
            "interestAccruedAmount.lessThan=" + DEFAULT_INTEREST_ACCRUED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInterestAccruedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where interestAccruedAmount is greater than
        defaultMoneyMarketDealFiltering(
            "interestAccruedAmount.greaterThan=" + SMALLER_INTEREST_ACCRUED_AMOUNT,
            "interestAccruedAmount.greaterThan=" + DEFAULT_INTEREST_ACCRUED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTotalInterestAtMaturityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where totalInterestAtMaturity equals to
        defaultMoneyMarketDealFiltering(
            "totalInterestAtMaturity.equals=" + DEFAULT_TOTAL_INTEREST_AT_MATURITY,
            "totalInterestAtMaturity.equals=" + UPDATED_TOTAL_INTEREST_AT_MATURITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTotalInterestAtMaturityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where totalInterestAtMaturity in
        defaultMoneyMarketDealFiltering(
            "totalInterestAtMaturity.in=" + DEFAULT_TOTAL_INTEREST_AT_MATURITY + "," + UPDATED_TOTAL_INTEREST_AT_MATURITY,
            "totalInterestAtMaturity.in=" + UPDATED_TOTAL_INTEREST_AT_MATURITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTotalInterestAtMaturityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where totalInterestAtMaturity is not null
        defaultMoneyMarketDealFiltering("totalInterestAtMaturity.specified=true", "totalInterestAtMaturity.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTotalInterestAtMaturityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where totalInterestAtMaturity is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "totalInterestAtMaturity.greaterThanOrEqual=" + DEFAULT_TOTAL_INTEREST_AT_MATURITY,
            "totalInterestAtMaturity.greaterThanOrEqual=" + UPDATED_TOTAL_INTEREST_AT_MATURITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTotalInterestAtMaturityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where totalInterestAtMaturity is less than or equal to
        defaultMoneyMarketDealFiltering(
            "totalInterestAtMaturity.lessThanOrEqual=" + DEFAULT_TOTAL_INTEREST_AT_MATURITY,
            "totalInterestAtMaturity.lessThanOrEqual=" + SMALLER_TOTAL_INTEREST_AT_MATURITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTotalInterestAtMaturityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where totalInterestAtMaturity is less than
        defaultMoneyMarketDealFiltering(
            "totalInterestAtMaturity.lessThan=" + UPDATED_TOTAL_INTEREST_AT_MATURITY,
            "totalInterestAtMaturity.lessThan=" + DEFAULT_TOTAL_INTEREST_AT_MATURITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTotalInterestAtMaturityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where totalInterestAtMaturity is greater than
        defaultMoneyMarketDealFiltering(
            "totalInterestAtMaturity.greaterThan=" + SMALLER_TOTAL_INTEREST_AT_MATURITY,
            "totalInterestAtMaturity.greaterThan=" + DEFAULT_TOTAL_INTEREST_AT_MATURITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyNationality equals to
        defaultMoneyMarketDealFiltering(
            "counterpartyNationality.equals=" + DEFAULT_COUNTERPARTY_NATIONALITY,
            "counterpartyNationality.equals=" + UPDATED_COUNTERPARTY_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyNationality in
        defaultMoneyMarketDealFiltering(
            "counterpartyNationality.in=" + DEFAULT_COUNTERPARTY_NATIONALITY + "," + UPDATED_COUNTERPARTY_NATIONALITY,
            "counterpartyNationality.in=" + UPDATED_COUNTERPARTY_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyNationality is not null
        defaultMoneyMarketDealFiltering("counterpartyNationality.specified=true", "counterpartyNationality.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyNationalityContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyNationality contains
        defaultMoneyMarketDealFiltering(
            "counterpartyNationality.contains=" + DEFAULT_COUNTERPARTY_NATIONALITY,
            "counterpartyNationality.contains=" + UPDATED_COUNTERPARTY_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyNationality does not contain
        defaultMoneyMarketDealFiltering(
            "counterpartyNationality.doesNotContain=" + UPDATED_COUNTERPARTY_NATIONALITY,
            "counterpartyNationality.doesNotContain=" + DEFAULT_COUNTERPARTY_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where endDate equals to
        defaultMoneyMarketDealFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where endDate in
        defaultMoneyMarketDealFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where endDate is not null
        defaultMoneyMarketDealFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where endDate is greater than or equal to
        defaultMoneyMarketDealFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where endDate is less than or equal to
        defaultMoneyMarketDealFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where endDate is less than
        defaultMoneyMarketDealFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where endDate is greater than
        defaultMoneyMarketDealFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTreasuryLedgerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where treasuryLedger equals to
        defaultMoneyMarketDealFiltering(
            "treasuryLedger.equals=" + DEFAULT_TREASURY_LEDGER,
            "treasuryLedger.equals=" + UPDATED_TREASURY_LEDGER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTreasuryLedgerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where treasuryLedger in
        defaultMoneyMarketDealFiltering(
            "treasuryLedger.in=" + DEFAULT_TREASURY_LEDGER + "," + UPDATED_TREASURY_LEDGER,
            "treasuryLedger.in=" + UPDATED_TREASURY_LEDGER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTreasuryLedgerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where treasuryLedger is not null
        defaultMoneyMarketDealFiltering("treasuryLedger.specified=true", "treasuryLedger.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTreasuryLedgerContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where treasuryLedger contains
        defaultMoneyMarketDealFiltering(
            "treasuryLedger.contains=" + DEFAULT_TREASURY_LEDGER,
            "treasuryLedger.contains=" + UPDATED_TREASURY_LEDGER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTreasuryLedgerNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where treasuryLedger does not contain
        defaultMoneyMarketDealFiltering(
            "treasuryLedger.doesNotContain=" + UPDATED_TREASURY_LEDGER,
            "treasuryLedger.doesNotContain=" + DEFAULT_TREASURY_LEDGER
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealSubtypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealSubtype equals to
        defaultMoneyMarketDealFiltering("dealSubtype.equals=" + DEFAULT_DEAL_SUBTYPE, "dealSubtype.equals=" + UPDATED_DEAL_SUBTYPE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealSubtypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealSubtype in
        defaultMoneyMarketDealFiltering(
            "dealSubtype.in=" + DEFAULT_DEAL_SUBTYPE + "," + UPDATED_DEAL_SUBTYPE,
            "dealSubtype.in=" + UPDATED_DEAL_SUBTYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealSubtypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealSubtype is not null
        defaultMoneyMarketDealFiltering("dealSubtype.specified=true", "dealSubtype.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealSubtypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealSubtype contains
        defaultMoneyMarketDealFiltering("dealSubtype.contains=" + DEFAULT_DEAL_SUBTYPE, "dealSubtype.contains=" + UPDATED_DEAL_SUBTYPE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByDealSubtypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where dealSubtype does not contain
        defaultMoneyMarketDealFiltering(
            "dealSubtype.doesNotContain=" + UPDATED_DEAL_SUBTYPE,
            "dealSubtype.doesNotContain=" + DEFAULT_DEAL_SUBTYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPrincipalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPrincipal equals to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPrincipal.equals=" + DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL,
            "shillingEquivalentPrincipal.equals=" + UPDATED_SHILLING_EQUIVALENT_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPrincipalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPrincipal in
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPrincipal.in=" + DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL + "," + UPDATED_SHILLING_EQUIVALENT_PRINCIPAL,
            "shillingEquivalentPrincipal.in=" + UPDATED_SHILLING_EQUIVALENT_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPrincipalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPrincipal is not null
        defaultMoneyMarketDealFiltering("shillingEquivalentPrincipal.specified=true", "shillingEquivalentPrincipal.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPrincipalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPrincipal is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPrincipal.greaterThanOrEqual=" + DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL,
            "shillingEquivalentPrincipal.greaterThanOrEqual=" + UPDATED_SHILLING_EQUIVALENT_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPrincipalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPrincipal is less than or equal to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPrincipal.lessThanOrEqual=" + DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL,
            "shillingEquivalentPrincipal.lessThanOrEqual=" + SMALLER_SHILLING_EQUIVALENT_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPrincipalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPrincipal is less than
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPrincipal.lessThan=" + UPDATED_SHILLING_EQUIVALENT_PRINCIPAL,
            "shillingEquivalentPrincipal.lessThan=" + DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPrincipalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPrincipal is greater than
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPrincipal.greaterThan=" + SMALLER_SHILLING_EQUIVALENT_PRINCIPAL,
            "shillingEquivalentPrincipal.greaterThan=" + DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentInterestAccruedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentInterestAccrued equals to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentInterestAccrued.equals=" + DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED,
            "shillingEquivalentInterestAccrued.equals=" + UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentInterestAccruedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentInterestAccrued in
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentInterestAccrued.in=" +
            DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED +
            "," +
            UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED,
            "shillingEquivalentInterestAccrued.in=" + UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentInterestAccruedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentInterestAccrued is not null
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentInterestAccrued.specified=true",
            "shillingEquivalentInterestAccrued.specified=false"
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentInterestAccruedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentInterestAccrued is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentInterestAccrued.greaterThanOrEqual=" + DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED,
            "shillingEquivalentInterestAccrued.greaterThanOrEqual=" + UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentInterestAccruedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentInterestAccrued is less than or equal to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentInterestAccrued.lessThanOrEqual=" + DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED,
            "shillingEquivalentInterestAccrued.lessThanOrEqual=" + SMALLER_SHILLING_EQUIVALENT_INTEREST_ACCRUED
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentInterestAccruedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentInterestAccrued is less than
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentInterestAccrued.lessThan=" + UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED,
            "shillingEquivalentInterestAccrued.lessThan=" + DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentInterestAccruedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentInterestAccrued is greater than
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentInterestAccrued.greaterThan=" + SMALLER_SHILLING_EQUIVALENT_INTEREST_ACCRUED,
            "shillingEquivalentInterestAccrued.greaterThan=" + DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPVFullIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPVFull equals to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPVFull.equals=" + DEFAULT_SHILLING_EQUIVALENT_PV_FULL,
            "shillingEquivalentPVFull.equals=" + UPDATED_SHILLING_EQUIVALENT_PV_FULL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPVFullIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPVFull in
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPVFull.in=" + DEFAULT_SHILLING_EQUIVALENT_PV_FULL + "," + UPDATED_SHILLING_EQUIVALENT_PV_FULL,
            "shillingEquivalentPVFull.in=" + UPDATED_SHILLING_EQUIVALENT_PV_FULL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPVFullIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPVFull is not null
        defaultMoneyMarketDealFiltering("shillingEquivalentPVFull.specified=true", "shillingEquivalentPVFull.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPVFullIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPVFull is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPVFull.greaterThanOrEqual=" + DEFAULT_SHILLING_EQUIVALENT_PV_FULL,
            "shillingEquivalentPVFull.greaterThanOrEqual=" + UPDATED_SHILLING_EQUIVALENT_PV_FULL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPVFullIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPVFull is less than or equal to
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPVFull.lessThanOrEqual=" + DEFAULT_SHILLING_EQUIVALENT_PV_FULL,
            "shillingEquivalentPVFull.lessThanOrEqual=" + SMALLER_SHILLING_EQUIVALENT_PV_FULL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPVFullIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPVFull is less than
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPVFull.lessThan=" + UPDATED_SHILLING_EQUIVALENT_PV_FULL,
            "shillingEquivalentPVFull.lessThan=" + DEFAULT_SHILLING_EQUIVALENT_PV_FULL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByShillingEquivalentPVFullIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where shillingEquivalentPVFull is greater than
        defaultMoneyMarketDealFiltering(
            "shillingEquivalentPVFull.greaterThan=" + SMALLER_SHILLING_EQUIVALENT_PV_FULL,
            "shillingEquivalentPVFull.greaterThan=" + DEFAULT_SHILLING_EQUIVALENT_PV_FULL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyDomicileIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyDomicile equals to
        defaultMoneyMarketDealFiltering(
            "counterpartyDomicile.equals=" + DEFAULT_COUNTERPARTY_DOMICILE,
            "counterpartyDomicile.equals=" + UPDATED_COUNTERPARTY_DOMICILE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyDomicileIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyDomicile in
        defaultMoneyMarketDealFiltering(
            "counterpartyDomicile.in=" + DEFAULT_COUNTERPARTY_DOMICILE + "," + UPDATED_COUNTERPARTY_DOMICILE,
            "counterpartyDomicile.in=" + UPDATED_COUNTERPARTY_DOMICILE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyDomicileIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyDomicile is not null
        defaultMoneyMarketDealFiltering("counterpartyDomicile.specified=true", "counterpartyDomicile.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyDomicileContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyDomicile contains
        defaultMoneyMarketDealFiltering(
            "counterpartyDomicile.contains=" + DEFAULT_COUNTERPARTY_DOMICILE,
            "counterpartyDomicile.contains=" + UPDATED_COUNTERPARTY_DOMICILE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByCounterpartyDomicileNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where counterpartyDomicile does not contain
        defaultMoneyMarketDealFiltering(
            "counterpartyDomicile.doesNotContain=" + UPDATED_COUNTERPARTY_DOMICILE,
            "counterpartyDomicile.doesNotContain=" + DEFAULT_COUNTERPARTY_DOMICILE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsBySettlementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where settlementDate equals to
        defaultMoneyMarketDealFiltering(
            "settlementDate.equals=" + DEFAULT_SETTLEMENT_DATE,
            "settlementDate.equals=" + UPDATED_SETTLEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsBySettlementDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where settlementDate in
        defaultMoneyMarketDealFiltering(
            "settlementDate.in=" + DEFAULT_SETTLEMENT_DATE + "," + UPDATED_SETTLEMENT_DATE,
            "settlementDate.in=" + UPDATED_SETTLEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsBySettlementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where settlementDate is not null
        defaultMoneyMarketDealFiltering("settlementDate.specified=true", "settlementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsBySettlementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where settlementDate is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "settlementDate.greaterThanOrEqual=" + DEFAULT_SETTLEMENT_DATE,
            "settlementDate.greaterThanOrEqual=" + UPDATED_SETTLEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsBySettlementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where settlementDate is less than or equal to
        defaultMoneyMarketDealFiltering(
            "settlementDate.lessThanOrEqual=" + DEFAULT_SETTLEMENT_DATE,
            "settlementDate.lessThanOrEqual=" + SMALLER_SETTLEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsBySettlementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where settlementDate is less than
        defaultMoneyMarketDealFiltering(
            "settlementDate.lessThan=" + UPDATED_SETTLEMENT_DATE,
            "settlementDate.lessThan=" + DEFAULT_SETTLEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsBySettlementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where settlementDate is greater than
        defaultMoneyMarketDealFiltering(
            "settlementDate.greaterThan=" + SMALLER_SETTLEMENT_DATE,
            "settlementDate.greaterThan=" + DEFAULT_SETTLEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionCollateralIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionCollateral equals to
        defaultMoneyMarketDealFiltering(
            "transactionCollateral.equals=" + DEFAULT_TRANSACTION_COLLATERAL,
            "transactionCollateral.equals=" + UPDATED_TRANSACTION_COLLATERAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionCollateralIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionCollateral in
        defaultMoneyMarketDealFiltering(
            "transactionCollateral.in=" + DEFAULT_TRANSACTION_COLLATERAL + "," + UPDATED_TRANSACTION_COLLATERAL,
            "transactionCollateral.in=" + UPDATED_TRANSACTION_COLLATERAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionCollateralIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionCollateral is not null
        defaultMoneyMarketDealFiltering("transactionCollateral.specified=true", "transactionCollateral.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionCollateralContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionCollateral contains
        defaultMoneyMarketDealFiltering(
            "transactionCollateral.contains=" + DEFAULT_TRANSACTION_COLLATERAL,
            "transactionCollateral.contains=" + UPDATED_TRANSACTION_COLLATERAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionCollateralNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionCollateral does not contain
        defaultMoneyMarketDealFiltering(
            "transactionCollateral.doesNotContain=" + UPDATED_TRANSACTION_COLLATERAL,
            "transactionCollateral.doesNotContain=" + DEFAULT_TRANSACTION_COLLATERAL
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionType equals to
        defaultMoneyMarketDealFiltering(
            "institutionType.equals=" + DEFAULT_INSTITUTION_TYPE,
            "institutionType.equals=" + UPDATED_INSTITUTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionType in
        defaultMoneyMarketDealFiltering(
            "institutionType.in=" + DEFAULT_INSTITUTION_TYPE + "," + UPDATED_INSTITUTION_TYPE,
            "institutionType.in=" + UPDATED_INSTITUTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionType is not null
        defaultMoneyMarketDealFiltering("institutionType.specified=true", "institutionType.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionType contains
        defaultMoneyMarketDealFiltering(
            "institutionType.contains=" + DEFAULT_INSTITUTION_TYPE,
            "institutionType.contains=" + UPDATED_INSTITUTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionType does not contain
        defaultMoneyMarketDealFiltering(
            "institutionType.doesNotContain=" + UPDATED_INSTITUTION_TYPE,
            "institutionType.doesNotContain=" + DEFAULT_INSTITUTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMaturityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where maturityDate equals to
        defaultMoneyMarketDealFiltering("maturityDate.equals=" + DEFAULT_MATURITY_DATE, "maturityDate.equals=" + UPDATED_MATURITY_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMaturityDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where maturityDate in
        defaultMoneyMarketDealFiltering(
            "maturityDate.in=" + DEFAULT_MATURITY_DATE + "," + UPDATED_MATURITY_DATE,
            "maturityDate.in=" + UPDATED_MATURITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMaturityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where maturityDate is not null
        defaultMoneyMarketDealFiltering("maturityDate.specified=true", "maturityDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMaturityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where maturityDate is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "maturityDate.greaterThanOrEqual=" + DEFAULT_MATURITY_DATE,
            "maturityDate.greaterThanOrEqual=" + UPDATED_MATURITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMaturityDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where maturityDate is less than or equal to
        defaultMoneyMarketDealFiltering(
            "maturityDate.lessThanOrEqual=" + DEFAULT_MATURITY_DATE,
            "maturityDate.lessThanOrEqual=" + SMALLER_MATURITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMaturityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where maturityDate is less than
        defaultMoneyMarketDealFiltering("maturityDate.lessThan=" + UPDATED_MATURITY_DATE, "maturityDate.lessThan=" + DEFAULT_MATURITY_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMaturityDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where maturityDate is greater than
        defaultMoneyMarketDealFiltering(
            "maturityDate.greaterThan=" + SMALLER_MATURITY_DATE,
            "maturityDate.greaterThan=" + DEFAULT_MATURITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionReportName equals to
        defaultMoneyMarketDealFiltering(
            "institutionReportName.equals=" + DEFAULT_INSTITUTION_REPORT_NAME,
            "institutionReportName.equals=" + UPDATED_INSTITUTION_REPORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionReportName in
        defaultMoneyMarketDealFiltering(
            "institutionReportName.in=" + DEFAULT_INSTITUTION_REPORT_NAME + "," + UPDATED_INSTITUTION_REPORT_NAME,
            "institutionReportName.in=" + UPDATED_INSTITUTION_REPORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionReportName is not null
        defaultMoneyMarketDealFiltering("institutionReportName.specified=true", "institutionReportName.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionReportNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionReportName contains
        defaultMoneyMarketDealFiltering(
            "institutionReportName.contains=" + DEFAULT_INSTITUTION_REPORT_NAME,
            "institutionReportName.contains=" + UPDATED_INSTITUTION_REPORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByInstitutionReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where institutionReportName does not contain
        defaultMoneyMarketDealFiltering(
            "institutionReportName.doesNotContain=" + UPDATED_INSTITUTION_REPORT_NAME,
            "institutionReportName.doesNotContain=" + DEFAULT_INSTITUTION_REPORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionType equals to
        defaultMoneyMarketDealFiltering(
            "transactionType.equals=" + DEFAULT_TRANSACTION_TYPE,
            "transactionType.equals=" + UPDATED_TRANSACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionType in
        defaultMoneyMarketDealFiltering(
            "transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE,
            "transactionType.in=" + UPDATED_TRANSACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionType is not null
        defaultMoneyMarketDealFiltering("transactionType.specified=true", "transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionType contains
        defaultMoneyMarketDealFiltering(
            "transactionType.contains=" + DEFAULT_TRANSACTION_TYPE,
            "transactionType.contains=" + UPDATED_TRANSACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByTransactionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where transactionType does not contain
        defaultMoneyMarketDealFiltering(
            "transactionType.doesNotContain=" + UPDATED_TRANSACTION_TYPE,
            "transactionType.doesNotContain=" + DEFAULT_TRANSACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where reportDate equals to
        defaultMoneyMarketDealFiltering("reportDate.equals=" + DEFAULT_REPORT_DATE, "reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where reportDate in
        defaultMoneyMarketDealFiltering(
            "reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE,
            "reportDate.in=" + UPDATED_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where reportDate is not null
        defaultMoneyMarketDealFiltering("reportDate.specified=true", "reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where reportDate is greater than or equal to
        defaultMoneyMarketDealFiltering(
            "reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE,
            "reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where reportDate is less than or equal to
        defaultMoneyMarketDealFiltering(
            "reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE,
            "reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where reportDate is less than
        defaultMoneyMarketDealFiltering("reportDate.lessThan=" + UPDATED_REPORT_DATE, "reportDate.lessThan=" + DEFAULT_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where reportDate is greater than
        defaultMoneyMarketDealFiltering("reportDate.greaterThan=" + SMALLER_REPORT_DATE, "reportDate.greaterThan=" + DEFAULT_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where active equals to
        defaultMoneyMarketDealFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where active in
        defaultMoneyMarketDealFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        // Get all the moneyMarketDealList where active is not null
        defaultMoneyMarketDealFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealsByMoneyMarketListIsEqualToSomething() throws Exception {
        MoneyMarketList moneyMarketList;
        if (TestUtil.findAll(em, MoneyMarketList.class).isEmpty()) {
            moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);
            moneyMarketList = MoneyMarketListResourceIT.createEntity();
        } else {
            moneyMarketList = TestUtil.findAll(em, MoneyMarketList.class).get(0);
        }
        em.persist(moneyMarketList);
        em.flush();
        moneyMarketDeal.setMoneyMarketList(moneyMarketList);
        moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);
        Long moneyMarketListId = moneyMarketList.getId();
        // Get all the moneyMarketDealList where moneyMarketList equals to moneyMarketListId
        defaultMoneyMarketDealShouldBeFound("moneyMarketListId.equals=" + moneyMarketListId);

        // Get all the moneyMarketDealList where moneyMarketList equals to (moneyMarketListId + 1)
        defaultMoneyMarketDealShouldNotBeFound("moneyMarketListId.equals=" + (moneyMarketListId + 1));
    }

    private void defaultMoneyMarketDealFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMoneyMarketDealShouldBeFound(shouldBeFound);
        defaultMoneyMarketDealShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMoneyMarketDealShouldBeFound(String filter) throws Exception {
        restMoneyMarketDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketDeal.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealNumber").value(hasItem(DEFAULT_DEAL_NUMBER)))
            .andExpect(jsonPath("$.[*].tradingBook").value(hasItem(DEFAULT_TRADING_BOOK)))
            .andExpect(jsonPath("$.[*].counterPartyName").value(hasItem(DEFAULT_COUNTER_PARTY_NAME)))
            .andExpect(jsonPath("$.[*].finalInterestAccrualDate").value(hasItem(DEFAULT_FINAL_INTEREST_ACCRUAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].counterPartySideType").value(hasItem(DEFAULT_COUNTER_PARTY_SIDE_TYPE)))
            .andExpect(jsonPath("$.[*].dateOfCollectionStatement").value(hasItem(DEFAULT_DATE_OF_COLLECTION_STATEMENT)))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].principalAmount").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(sameNumber(DEFAULT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].interestAccruedAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalInterestAtMaturity").value(hasItem(sameNumber(DEFAULT_TOTAL_INTEREST_AT_MATURITY))))
            .andExpect(jsonPath("$.[*].counterpartyNationality").value(hasItem(DEFAULT_COUNTERPARTY_NATIONALITY)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].treasuryLedger").value(hasItem(DEFAULT_TREASURY_LEDGER)))
            .andExpect(jsonPath("$.[*].dealSubtype").value(hasItem(DEFAULT_DEAL_SUBTYPE)))
            .andExpect(jsonPath("$.[*].shillingEquivalentPrincipal").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL))))
            .andExpect(
                jsonPath("$.[*].shillingEquivalentInterestAccrued").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED)))
            )
            .andExpect(jsonPath("$.[*].shillingEquivalentPVFull").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PV_FULL))))
            .andExpect(jsonPath("$.[*].counterpartyDomicile").value(hasItem(DEFAULT_COUNTERPARTY_DOMICILE)))
            .andExpect(jsonPath("$.[*].settlementDate").value(hasItem(DEFAULT_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionCollateral").value(hasItem(DEFAULT_TRANSACTION_COLLATERAL)))
            .andExpect(jsonPath("$.[*].institutionType").value(hasItem(DEFAULT_INSTITUTION_TYPE)))
            .andExpect(jsonPath("$.[*].maturityDate").value(hasItem(DEFAULT_MATURITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].institutionReportName").value(hasItem(DEFAULT_INSTITUTION_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restMoneyMarketDealMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMoneyMarketDealShouldNotBeFound(String filter) throws Exception {
        restMoneyMarketDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMoneyMarketDealMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMoneyMarketDeal() throws Exception {
        // Get the moneyMarketDeal
        restMoneyMarketDealMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyMarketDeal() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyMarketDealSearchRepository.save(moneyMarketDeal);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());

        // Update the moneyMarketDeal
        MoneyMarketDeal updatedMoneyMarketDeal = moneyMarketDealRepository.findById(moneyMarketDeal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyMarketDeal are not directly saved in db
        em.detach(updatedMoneyMarketDeal);
        updatedMoneyMarketDeal
            .dealNumber(UPDATED_DEAL_NUMBER)
            .tradingBook(UPDATED_TRADING_BOOK)
            .counterPartyName(UPDATED_COUNTER_PARTY_NAME)
            .finalInterestAccrualDate(UPDATED_FINAL_INTEREST_ACCRUAL_DATE)
            .counterPartySideType(UPDATED_COUNTER_PARTY_SIDE_TYPE)
            .dateOfCollectionStatement(UPDATED_DATE_OF_COLLECTION_STATEMENT)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .principalAmount(UPDATED_PRINCIPAL_AMOUNT)
            .interestRate(UPDATED_INTEREST_RATE)
            .interestAccruedAmount(UPDATED_INTEREST_ACCRUED_AMOUNT)
            .totalInterestAtMaturity(UPDATED_TOTAL_INTEREST_AT_MATURITY)
            .counterpartyNationality(UPDATED_COUNTERPARTY_NATIONALITY)
            .endDate(UPDATED_END_DATE)
            .treasuryLedger(UPDATED_TREASURY_LEDGER)
            .dealSubtype(UPDATED_DEAL_SUBTYPE)
            .shillingEquivalentPrincipal(UPDATED_SHILLING_EQUIVALENT_PRINCIPAL)
            .shillingEquivalentInterestAccrued(UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED)
            .shillingEquivalentPVFull(UPDATED_SHILLING_EQUIVALENT_PV_FULL)
            .counterpartyDomicile(UPDATED_COUNTERPARTY_DOMICILE)
            .settlementDate(UPDATED_SETTLEMENT_DATE)
            .transactionCollateral(UPDATED_TRANSACTION_COLLATERAL)
            .institutionType(UPDATED_INSTITUTION_TYPE)
            .maturityDate(UPDATED_MATURITY_DATE)
            .institutionReportName(UPDATED_INSTITUTION_REPORT_NAME)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .reportDate(UPDATED_REPORT_DATE)
            .active(UPDATED_ACTIVE);
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(updatedMoneyMarketDeal);

        restMoneyMarketDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyMarketDealDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketDealDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoneyMarketDealToMatchAllProperties(updatedMoneyMarketDeal);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MoneyMarketDeal> moneyMarketDealSearchList = Streamable.of(moneyMarketDealSearchRepository.findAll()).toList();
                MoneyMarketDeal testMoneyMarketDealSearch = moneyMarketDealSearchList.get(searchDatabaseSizeAfter - 1);

                assertMoneyMarketDealAllPropertiesEquals(testMoneyMarketDealSearch, updatedMoneyMarketDeal);
            });
    }

    @Test
    @Transactional
    void putNonExistingMoneyMarketDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        moneyMarketDeal.setId(longCount.incrementAndGet());

        // Create the MoneyMarketDeal
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMarketDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyMarketDealDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketDealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyMarketDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        moneyMarketDeal.setId(longCount.incrementAndGet());

        // Create the MoneyMarketDeal
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyMarketDealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyMarketDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        moneyMarketDeal.setId(longCount.incrementAndGet());

        // Create the MoneyMarketDeal
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketDealMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMoneyMarketDealWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyMarketDeal using partial update
        MoneyMarketDeal partialUpdatedMoneyMarketDeal = new MoneyMarketDeal();
        partialUpdatedMoneyMarketDeal.setId(moneyMarketDeal.getId());

        partialUpdatedMoneyMarketDeal
            .counterPartyName(UPDATED_COUNTER_PARTY_NAME)
            .dateOfCollectionStatement(UPDATED_DATE_OF_COLLECTION_STATEMENT)
            .totalInterestAtMaturity(UPDATED_TOTAL_INTEREST_AT_MATURITY)
            .treasuryLedger(UPDATED_TREASURY_LEDGER)
            .dealSubtype(UPDATED_DEAL_SUBTYPE)
            .shillingEquivalentPrincipal(UPDATED_SHILLING_EQUIVALENT_PRINCIPAL)
            .counterpartyDomicile(UPDATED_COUNTERPARTY_DOMICILE)
            .settlementDate(UPDATED_SETTLEMENT_DATE)
            .maturityDate(UPDATED_MATURITY_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .active(UPDATED_ACTIVE);

        restMoneyMarketDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyMarketDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyMarketDeal))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketDeal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyMarketDealUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoneyMarketDeal, moneyMarketDeal),
            getPersistedMoneyMarketDeal(moneyMarketDeal)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoneyMarketDealWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyMarketDeal using partial update
        MoneyMarketDeal partialUpdatedMoneyMarketDeal = new MoneyMarketDeal();
        partialUpdatedMoneyMarketDeal.setId(moneyMarketDeal.getId());

        partialUpdatedMoneyMarketDeal
            .dealNumber(UPDATED_DEAL_NUMBER)
            .tradingBook(UPDATED_TRADING_BOOK)
            .counterPartyName(UPDATED_COUNTER_PARTY_NAME)
            .finalInterestAccrualDate(UPDATED_FINAL_INTEREST_ACCRUAL_DATE)
            .counterPartySideType(UPDATED_COUNTER_PARTY_SIDE_TYPE)
            .dateOfCollectionStatement(UPDATED_DATE_OF_COLLECTION_STATEMENT)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .principalAmount(UPDATED_PRINCIPAL_AMOUNT)
            .interestRate(UPDATED_INTEREST_RATE)
            .interestAccruedAmount(UPDATED_INTEREST_ACCRUED_AMOUNT)
            .totalInterestAtMaturity(UPDATED_TOTAL_INTEREST_AT_MATURITY)
            .counterpartyNationality(UPDATED_COUNTERPARTY_NATIONALITY)
            .endDate(UPDATED_END_DATE)
            .treasuryLedger(UPDATED_TREASURY_LEDGER)
            .dealSubtype(UPDATED_DEAL_SUBTYPE)
            .shillingEquivalentPrincipal(UPDATED_SHILLING_EQUIVALENT_PRINCIPAL)
            .shillingEquivalentInterestAccrued(UPDATED_SHILLING_EQUIVALENT_INTEREST_ACCRUED)
            .shillingEquivalentPVFull(UPDATED_SHILLING_EQUIVALENT_PV_FULL)
            .counterpartyDomicile(UPDATED_COUNTERPARTY_DOMICILE)
            .settlementDate(UPDATED_SETTLEMENT_DATE)
            .transactionCollateral(UPDATED_TRANSACTION_COLLATERAL)
            .institutionType(UPDATED_INSTITUTION_TYPE)
            .maturityDate(UPDATED_MATURITY_DATE)
            .institutionReportName(UPDATED_INSTITUTION_REPORT_NAME)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .reportDate(UPDATED_REPORT_DATE)
            .active(UPDATED_ACTIVE);

        restMoneyMarketDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyMarketDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyMarketDeal))
            )
            .andExpect(status().isOk());

        // Validate the MoneyMarketDeal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyMarketDealUpdatableFieldsEquals(
            partialUpdatedMoneyMarketDeal,
            getPersistedMoneyMarketDeal(partialUpdatedMoneyMarketDeal)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMoneyMarketDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        moneyMarketDeal.setId(longCount.incrementAndGet());

        // Create the MoneyMarketDeal
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMarketDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyMarketDealDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyMarketDealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyMarketDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        moneyMarketDeal.setId(longCount.incrementAndGet());

        // Create the MoneyMarketDeal
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyMarketDealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyMarketDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        moneyMarketDeal.setId(longCount.incrementAndGet());

        // Create the MoneyMarketDeal
        MoneyMarketDealDTO moneyMarketDealDTO = moneyMarketDealMapper.toDto(moneyMarketDeal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMarketDealMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moneyMarketDealDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyMarketDeal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMoneyMarketDeal() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);
        moneyMarketDealRepository.save(moneyMarketDeal);
        moneyMarketDealSearchRepository.save(moneyMarketDeal);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the moneyMarketDeal
        restMoneyMarketDealMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyMarketDeal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyMarketDealSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMoneyMarketDeal() throws Exception {
        // Initialize the database
        insertedMoneyMarketDeal = moneyMarketDealRepository.saveAndFlush(moneyMarketDeal);
        moneyMarketDealSearchRepository.save(moneyMarketDeal);

        // Search the moneyMarketDeal
        restMoneyMarketDealMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + moneyMarketDeal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketDeal.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealNumber").value(hasItem(DEFAULT_DEAL_NUMBER)))
            .andExpect(jsonPath("$.[*].tradingBook").value(hasItem(DEFAULT_TRADING_BOOK)))
            .andExpect(jsonPath("$.[*].counterPartyName").value(hasItem(DEFAULT_COUNTER_PARTY_NAME)))
            .andExpect(jsonPath("$.[*].finalInterestAccrualDate").value(hasItem(DEFAULT_FINAL_INTEREST_ACCRUAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].counterPartySideType").value(hasItem(DEFAULT_COUNTER_PARTY_SIDE_TYPE)))
            .andExpect(jsonPath("$.[*].dateOfCollectionStatement").value(hasItem(DEFAULT_DATE_OF_COLLECTION_STATEMENT)))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].principalAmount").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(sameNumber(DEFAULT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].interestAccruedAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalInterestAtMaturity").value(hasItem(sameNumber(DEFAULT_TOTAL_INTEREST_AT_MATURITY))))
            .andExpect(jsonPath("$.[*].counterpartyNationality").value(hasItem(DEFAULT_COUNTERPARTY_NATIONALITY)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].treasuryLedger").value(hasItem(DEFAULT_TREASURY_LEDGER)))
            .andExpect(jsonPath("$.[*].dealSubtype").value(hasItem(DEFAULT_DEAL_SUBTYPE)))
            .andExpect(jsonPath("$.[*].shillingEquivalentPrincipal").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PRINCIPAL))))
            .andExpect(
                jsonPath("$.[*].shillingEquivalentInterestAccrued").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_INTEREST_ACCRUED)))
            )
            .andExpect(jsonPath("$.[*].shillingEquivalentPVFull").value(hasItem(sameNumber(DEFAULT_SHILLING_EQUIVALENT_PV_FULL))))
            .andExpect(jsonPath("$.[*].counterpartyDomicile").value(hasItem(DEFAULT_COUNTERPARTY_DOMICILE)))
            .andExpect(jsonPath("$.[*].settlementDate").value(hasItem(DEFAULT_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionCollateral").value(hasItem(DEFAULT_TRANSACTION_COLLATERAL)))
            .andExpect(jsonPath("$.[*].institutionType").value(hasItem(DEFAULT_INSTITUTION_TYPE)))
            .andExpect(jsonPath("$.[*].maturityDate").value(hasItem(DEFAULT_MATURITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].institutionReportName").value(hasItem(DEFAULT_INSTITUTION_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    protected long getRepositoryCount() {
        return moneyMarketDealRepository.count();
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

    protected MoneyMarketDeal getPersistedMoneyMarketDeal(MoneyMarketDeal moneyMarketDeal) {
        return moneyMarketDealRepository.findById(moneyMarketDeal.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyMarketDealToMatchAllProperties(MoneyMarketDeal expectedMoneyMarketDeal) {
        assertMoneyMarketDealAllPropertiesEquals(expectedMoneyMarketDeal, getPersistedMoneyMarketDeal(expectedMoneyMarketDeal));
    }

    protected void assertPersistedMoneyMarketDealToMatchUpdatableProperties(MoneyMarketDeal expectedMoneyMarketDeal) {
        assertMoneyMarketDealAllUpdatablePropertiesEquals(expectedMoneyMarketDeal, getPersistedMoneyMarketDeal(expectedMoneyMarketDeal));
    }
}
