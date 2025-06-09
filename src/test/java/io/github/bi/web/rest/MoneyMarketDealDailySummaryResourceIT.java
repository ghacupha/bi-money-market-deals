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

import static io.github.bi.domain.MoneyMarketDealDailySummaryAsserts.*;
import static io.github.bi.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.MoneyMarketDealDailySummary;
import io.github.bi.repository.MoneyMarketDealDailySummaryRepository;
import io.github.bi.repository.search.MoneyMarketDealDailySummarySearchRepository;
import io.github.bi.service.mapper.MoneyMarketDealDailySummaryMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MoneyMarketDealDailySummaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoneyMarketDealDailySummaryResourceIT {

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LEDGER = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_DEALS = 1;
    private static final Integer UPDATED_NUMBER_OF_DEALS = 2;

    private static final BigDecimal DEFAULT_TOTAL_PRINCIPAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRINCIPAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTEREST_ACCRUED = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_ACCRUED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_PV_FULL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PV_FULL = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/money-market-deal-daily-summaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/money-market-deal-daily-summaries/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyMarketDealDailySummaryRepository moneyMarketDealDailySummaryRepository;

    @Autowired
    private MoneyMarketDealDailySummaryMapper moneyMarketDealDailySummaryMapper;

    @Autowired
    private MoneyMarketDealDailySummarySearchRepository moneyMarketDealDailySummarySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyMarketDealDailySummaryMockMvc;

    private MoneyMarketDealDailySummary moneyMarketDealDailySummary;

    private MoneyMarketDealDailySummary insertedMoneyMarketDealDailySummary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketDealDailySummary createEntity() {
        return new MoneyMarketDealDailySummary()
            .reportDate(DEFAULT_REPORT_DATE)
            .ledger(DEFAULT_LEDGER)
            .numberOfDeals(DEFAULT_NUMBER_OF_DEALS)
            .totalPrincipal(DEFAULT_TOTAL_PRINCIPAL)
            .interestAccrued(DEFAULT_INTEREST_ACCRUED)
            .totalPVFull(DEFAULT_TOTAL_PV_FULL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyMarketDealDailySummary createUpdatedEntity() {
        return new MoneyMarketDealDailySummary()
            .reportDate(UPDATED_REPORT_DATE)
            .ledger(UPDATED_LEDGER)
            .numberOfDeals(UPDATED_NUMBER_OF_DEALS)
            .totalPrincipal(UPDATED_TOTAL_PRINCIPAL)
            .interestAccrued(UPDATED_INTEREST_ACCRUED)
            .totalPVFull(UPDATED_TOTAL_PV_FULL);
    }

    @BeforeEach
    void initTest() {
        moneyMarketDealDailySummary = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoneyMarketDealDailySummary != null) {
            moneyMarketDealDailySummaryRepository.delete(insertedMoneyMarketDealDailySummary);
            moneyMarketDealDailySummarySearchRepository.delete(insertedMoneyMarketDealDailySummary);
            insertedMoneyMarketDealDailySummary = null;
        }
    }

    @Test
    @Transactional
    void getAllMoneyMarketDealDailySummaries() throws Exception {
        // Initialize the database
        insertedMoneyMarketDealDailySummary = moneyMarketDealDailySummaryRepository.saveAndFlush(moneyMarketDealDailySummary);

        // Get all the moneyMarketDealDailySummaryList
        restMoneyMarketDealDailySummaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketDealDailySummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].ledger").value(hasItem(DEFAULT_LEDGER)))
            .andExpect(jsonPath("$.[*].numberOfDeals").value(hasItem(DEFAULT_NUMBER_OF_DEALS)))
            .andExpect(jsonPath("$.[*].totalPrincipal").value(hasItem(sameNumber(DEFAULT_TOTAL_PRINCIPAL))))
            .andExpect(jsonPath("$.[*].interestAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED))))
            .andExpect(jsonPath("$.[*].totalPVFull").value(hasItem(sameNumber(DEFAULT_TOTAL_PV_FULL))));
    }

    @Test
    @Transactional
    void getMoneyMarketDealDailySummary() throws Exception {
        // Initialize the database
        insertedMoneyMarketDealDailySummary = moneyMarketDealDailySummaryRepository.saveAndFlush(moneyMarketDealDailySummary);

        // Get the moneyMarketDealDailySummary
        restMoneyMarketDealDailySummaryMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyMarketDealDailySummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyMarketDealDailySummary.getId().intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.ledger").value(DEFAULT_LEDGER))
            .andExpect(jsonPath("$.numberOfDeals").value(DEFAULT_NUMBER_OF_DEALS))
            .andExpect(jsonPath("$.totalPrincipal").value(sameNumber(DEFAULT_TOTAL_PRINCIPAL)))
            .andExpect(jsonPath("$.interestAccrued").value(sameNumber(DEFAULT_INTEREST_ACCRUED)))
            .andExpect(jsonPath("$.totalPVFull").value(sameNumber(DEFAULT_TOTAL_PV_FULL)));
    }

    @Test
    @Transactional
    void getNonExistingMoneyMarketDealDailySummary() throws Exception {
        // Get the moneyMarketDealDailySummary
        restMoneyMarketDealDailySummaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchMoneyMarketDealDailySummary() throws Exception {
        // Initialize the database
        insertedMoneyMarketDealDailySummary = moneyMarketDealDailySummaryRepository.saveAndFlush(moneyMarketDealDailySummary);
        moneyMarketDealDailySummarySearchRepository.save(moneyMarketDealDailySummary);

        // Search the moneyMarketDealDailySummary
        restMoneyMarketDealDailySummaryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + moneyMarketDealDailySummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyMarketDealDailySummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].ledger").value(hasItem(DEFAULT_LEDGER)))
            .andExpect(jsonPath("$.[*].numberOfDeals").value(hasItem(DEFAULT_NUMBER_OF_DEALS)))
            .andExpect(jsonPath("$.[*].totalPrincipal").value(hasItem(sameNumber(DEFAULT_TOTAL_PRINCIPAL))))
            .andExpect(jsonPath("$.[*].interestAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED))))
            .andExpect(jsonPath("$.[*].totalPVFull").value(hasItem(sameNumber(DEFAULT_TOTAL_PV_FULL))));
    }

    protected long getRepositoryCount() {
        return moneyMarketDealDailySummaryRepository.count();
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

    protected MoneyMarketDealDailySummary getPersistedMoneyMarketDealDailySummary(MoneyMarketDealDailySummary moneyMarketDealDailySummary) {
        return moneyMarketDealDailySummaryRepository.findById(moneyMarketDealDailySummary.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyMarketDealDailySummaryToMatchAllProperties(
        MoneyMarketDealDailySummary expectedMoneyMarketDealDailySummary
    ) {
        assertMoneyMarketDealDailySummaryAllPropertiesEquals(
            expectedMoneyMarketDealDailySummary,
            getPersistedMoneyMarketDealDailySummary(expectedMoneyMarketDealDailySummary)
        );
    }

    protected void assertPersistedMoneyMarketDealDailySummaryToMatchUpdatableProperties(
        MoneyMarketDealDailySummary expectedMoneyMarketDealDailySummary
    ) {
        assertMoneyMarketDealDailySummaryAllUpdatablePropertiesEquals(
            expectedMoneyMarketDealDailySummary,
            getPersistedMoneyMarketDealDailySummary(expectedMoneyMarketDealDailySummary)
        );
    }
}
