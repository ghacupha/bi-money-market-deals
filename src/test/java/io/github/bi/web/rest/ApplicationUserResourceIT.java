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

import static io.github.bi.domain.ApplicationUserAsserts.*;
import static io.github.bi.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bi.IntegrationTest;
import io.github.bi.domain.ApplicationUser;
import io.github.bi.domain.Dealer;
import io.github.bi.domain.Placeholder;
import io.github.bi.domain.SecurityClearance;
import io.github.bi.repository.ApplicationUserRepository;
import io.github.bi.repository.search.ApplicationUserSearchRepository;
import io.github.bi.service.ApplicationUserService;
import io.github.bi.service.dto.ApplicationUserDTO;
import io.github.bi.service.mapper.ApplicationUserMapper;
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
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserResourceIT {

    private static final UUID DEFAULT_DESIGNATION = UUID.randomUUID();
    private static final UUID UPDATED_DESIGNATION = UUID.randomUUID();

    private static final String DEFAULT_APPLICATION_IDENTITY = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_IDENTITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/application-users/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @Mock
    private ApplicationUserService applicationUserServiceMock;

    @Autowired
    private ApplicationUserSearchRepository applicationUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    private ApplicationUser insertedApplicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .designation(DEFAULT_DESIGNATION)
            .applicationIdentity(DEFAULT_APPLICATION_IDENTITY);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity();
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        applicationUser.setOrganization(dealer);
        // Add required entity
        applicationUser.setDepartment(dealer);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity();
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        applicationUser.setSecurityClearance(securityClearance);
        // Add required entity
        applicationUser.setDealerIdentity(dealer);
        return applicationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser updatedApplicationUser = new ApplicationUser()
            .designation(UPDATED_DESIGNATION)
            .applicationIdentity(UPDATED_APPLICATION_IDENTITY);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity();
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        updatedApplicationUser.setOrganization(dealer);
        // Add required entity
        updatedApplicationUser.setDepartment(dealer);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createUpdatedEntity();
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        updatedApplicationUser.setSecurityClearance(securityClearance);
        // Add required entity
        updatedApplicationUser.setDealerIdentity(dealer);
        return updatedApplicationUser;
    }

    @BeforeEach
    void initTest() {
        applicationUser = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedApplicationUser != null) {
            applicationUserRepository.delete(insertedApplicationUser);
            applicationUserSearchRepository.delete(insertedApplicationUser);
            insertedApplicationUser = null;
        }
    }

    @Test
    @Transactional
    void createApplicationUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);
        var returnedApplicationUserDTO = om.readValue(
            restApplicationUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApplicationUserDTO.class
        );

        // Validate the ApplicationUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApplicationUser = applicationUserMapper.toEntity(returnedApplicationUserDTO);
        assertApplicationUserUpdatableFieldsEquals(returnedApplicationUser, getPersistedApplicationUser(returnedApplicationUser));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedApplicationUser = returnedApplicationUser;
    }

    @Test
    @Transactional
    void createApplicationUserWithExistingId() throws Exception {
        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        // set the field null
        applicationUser.setDesignation(null);

        // Create the ApplicationUser, which fails.
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        restApplicationUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkApplicationIdentityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        // set the field null
        applicationUser.setApplicationIdentity(null);

        // Create the ApplicationUser, which fails.
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        restApplicationUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllApplicationUsers() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].applicationIdentity").value(hasItem(DEFAULT_APPLICATION_IDENTITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(applicationUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getApplicationUser() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.applicationIdentity").value(DEFAULT_APPLICATION_IDENTITY));
    }

    @Test
    @Transactional
    void getApplicationUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        Long id = applicationUser.getId();

        defaultApplicationUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultApplicationUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultApplicationUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where designation equals to
        defaultApplicationUserFiltering("designation.equals=" + DEFAULT_DESIGNATION, "designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where designation in
        defaultApplicationUserFiltering(
            "designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION,
            "designation.in=" + UPDATED_DESIGNATION
        );
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where designation is not null
        defaultApplicationUserFiltering("designation.specified=true", "designation.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity equals to
        defaultApplicationUserFiltering(
            "applicationIdentity.equals=" + DEFAULT_APPLICATION_IDENTITY,
            "applicationIdentity.equals=" + UPDATED_APPLICATION_IDENTITY
        );
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity in
        defaultApplicationUserFiltering(
            "applicationIdentity.in=" + DEFAULT_APPLICATION_IDENTITY + "," + UPDATED_APPLICATION_IDENTITY,
            "applicationIdentity.in=" + UPDATED_APPLICATION_IDENTITY
        );
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity is not null
        defaultApplicationUserFiltering("applicationIdentity.specified=true", "applicationIdentity.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity contains
        defaultApplicationUserFiltering(
            "applicationIdentity.contains=" + DEFAULT_APPLICATION_IDENTITY,
            "applicationIdentity.contains=" + UPDATED_APPLICATION_IDENTITY
        );
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity does not contain
        defaultApplicationUserFiltering(
            "applicationIdentity.doesNotContain=" + UPDATED_APPLICATION_IDENTITY,
            "applicationIdentity.doesNotContain=" + DEFAULT_APPLICATION_IDENTITY
        );
    }

    @Test
    @Transactional
    void getAllApplicationUsersByOrganizationIsEqualToSomething() throws Exception {
        Dealer organization;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            organization = DealerResourceIT.createEntity();
        } else {
            organization = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(organization);
        em.flush();
        applicationUser.setOrganization(organization);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long organizationId = organization.getId();
        // Get all the applicationUserList where organization equals to organizationId
        defaultApplicationUserShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the applicationUserList where organization equals to (organizationId + 1)
        defaultApplicationUserShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDepartmentIsEqualToSomething() throws Exception {
        Dealer department;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            department = DealerResourceIT.createEntity();
        } else {
            department = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(department);
        em.flush();
        applicationUser.setDepartment(department);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long departmentId = department.getId();
        // Get all the applicationUserList where department equals to departmentId
        defaultApplicationUserShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the applicationUserList where department equals to (departmentId + 1)
        defaultApplicationUserShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersBySecurityClearanceIsEqualToSomething() throws Exception {
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            securityClearance = SecurityClearanceResourceIT.createEntity();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        em.persist(securityClearance);
        em.flush();
        applicationUser.setSecurityClearance(securityClearance);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long securityClearanceId = securityClearance.getId();
        // Get all the applicationUserList where securityClearance equals to securityClearanceId
        defaultApplicationUserShouldBeFound("securityClearanceId.equals=" + securityClearanceId);

        // Get all the applicationUserList where securityClearance equals to (securityClearanceId + 1)
        defaultApplicationUserShouldNotBeFound("securityClearanceId.equals=" + (securityClearanceId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDealerIdentityIsEqualToSomething() throws Exception {
        // Get already existing entity
        Dealer dealerIdentity = applicationUser.getDealerIdentity();
        applicationUserRepository.saveAndFlush(applicationUser);
        Long dealerIdentityId = dealerIdentity.getId();
        // Get all the applicationUserList where dealerIdentity equals to dealerIdentityId
        defaultApplicationUserShouldBeFound("dealerIdentityId.equals=" + dealerIdentityId);

        // Get all the applicationUserList where dealerIdentity equals to (dealerIdentityId + 1)
        defaultApplicationUserShouldNotBeFound("dealerIdentityId.equals=" + (dealerIdentityId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByPlaceholderIsEqualToSomething() throws Exception {
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            placeholder = PlaceholderResourceIT.createEntity();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        applicationUser.addPlaceholder(placeholder);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long placeholderId = placeholder.getId();
        // Get all the applicationUserList where placeholder equals to placeholderId
        defaultApplicationUserShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the applicationUserList where placeholder equals to (placeholderId + 1)
        defaultApplicationUserShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    private void defaultApplicationUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultApplicationUserShouldBeFound(shouldBeFound);
        defaultApplicationUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationUserShouldBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].applicationIdentity").value(hasItem(DEFAULT_APPLICATION_IDENTITY)));

        // Check, that the count call also returns 1
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationUserShouldNotBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationUser() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationUserSearchRepository.save(applicationUser);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser.designation(UPDATED_DESIGNATION).applicationIdentity(UPDATED_APPLICATION_IDENTITY);
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(updatedApplicationUser);

        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationUserToMatchAllProperties(updatedApplicationUser);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ApplicationUser> applicationUserSearchList = Streamable.of(applicationUserSearchRepository.findAll()).toList();
                ApplicationUser testApplicationUserSearch = applicationUserSearchList.get(searchDatabaseSizeAfter - 1);

                assertApplicationUserAllPropertiesEquals(testApplicationUserSearch, updatedApplicationUser);
            });
    }

    @Test
    @Transactional
    void putNonExistingApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        applicationUser.setId(longCount.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        applicationUser.setId(longCount.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        applicationUser.setId(longCount.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.applicationIdentity(UPDATED_APPLICATION_IDENTITY);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplicationUser, applicationUser),
            getPersistedApplicationUser(applicationUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.designation(UPDATED_DESIGNATION).applicationIdentity(UPDATED_APPLICATION_IDENTITY);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUserUpdatableFieldsEquals(
            partialUpdatedApplicationUser,
            getPersistedApplicationUser(partialUpdatedApplicationUser)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        applicationUser.setId(longCount.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        applicationUser.setId(longCount.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        applicationUser.setId(longCount.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(applicationUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteApplicationUser() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);
        applicationUserRepository.save(applicationUser);
        applicationUserSearchRepository.save(applicationUser);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the applicationUser
        restApplicationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchApplicationUser() throws Exception {
        // Initialize the database
        insertedApplicationUser = applicationUserRepository.saveAndFlush(applicationUser);
        applicationUserSearchRepository.save(applicationUser);

        // Search the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].applicationIdentity").value(hasItem(DEFAULT_APPLICATION_IDENTITY)));
    }

    protected long getRepositoryCount() {
        return applicationUserRepository.count();
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

    protected ApplicationUser getPersistedApplicationUser(ApplicationUser applicationUser) {
        return applicationUserRepository.findById(applicationUser.getId()).orElseThrow();
    }

    protected void assertPersistedApplicationUserToMatchAllProperties(ApplicationUser expectedApplicationUser) {
        assertApplicationUserAllPropertiesEquals(expectedApplicationUser, getPersistedApplicationUser(expectedApplicationUser));
    }

    protected void assertPersistedApplicationUserToMatchUpdatableProperties(ApplicationUser expectedApplicationUser) {
        assertApplicationUserAllUpdatablePropertiesEquals(expectedApplicationUser, getPersistedApplicationUser(expectedApplicationUser));
    }
}
