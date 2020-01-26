package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.DbToolApp;
import com.alphadevs.tools.domain.UserGroup;
import com.alphadevs.tools.domain.UserPermissions;
import com.alphadevs.tools.domain.ExUser;
import com.alphadevs.tools.repository.UserGroupRepository;
import com.alphadevs.tools.service.UserGroupService;
import com.alphadevs.tools.web.rest.errors.ExceptionTranslator;
import com.alphadevs.tools.service.dto.UserGroupCriteria;
import com.alphadevs.tools.service.UserGroupQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.alphadevs.tools.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserGroupResource} REST controller.
 */
@SpringBootTest(classes = DbToolApp.class)
public class UserGroupResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Mock
    private UserGroupRepository userGroupRepositoryMock;

    @Mock
    private UserGroupService userGroupServiceMock;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserGroupQueryService userGroupQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUserGroupMockMvc;

    private UserGroup userGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserGroupResource userGroupResource = new UserGroupResource(userGroupService, userGroupQueryService);
        this.restUserGroupMockMvc = MockMvcBuilders.standaloneSetup(userGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGroup createEntity(EntityManager em) {
        UserGroup userGroup = new UserGroup()
            .groupName(DEFAULT_GROUP_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        UserPermissions userPermissions;
        if (TestUtil.findAll(em, UserPermissions.class).isEmpty()) {
            userPermissions = UserPermissionsResourceIT.createEntity(em);
            em.persist(userPermissions);
            em.flush();
        } else {
            userPermissions = TestUtil.findAll(em, UserPermissions.class).get(0);
        }
        userGroup.getUserPermissions().add(userPermissions);
        return userGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGroup createUpdatedEntity(EntityManager em) {
        UserGroup userGroup = new UserGroup()
            .groupName(UPDATED_GROUP_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        UserPermissions userPermissions;
        if (TestUtil.findAll(em, UserPermissions.class).isEmpty()) {
            userPermissions = UserPermissionsResourceIT.createUpdatedEntity(em);
            em.persist(userPermissions);
            em.flush();
        } else {
            userPermissions = TestUtil.findAll(em, UserPermissions.class).get(0);
        }
        userGroup.getUserPermissions().add(userPermissions);
        return userGroup;
    }

    @BeforeEach
    public void initTest() {
        userGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserGroup() throws Exception {
        int databaseSizeBeforeCreate = userGroupRepository.findAll().size();

        // Create the UserGroup
        restUserGroupMockMvc.perform(post("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroup)))
            .andExpect(status().isCreated());

        // Validate the UserGroup in the database
        List<UserGroup> userGroupList = userGroupRepository.findAll();
        assertThat(userGroupList).hasSize(databaseSizeBeforeCreate + 1);
        UserGroup testUserGroup = userGroupList.get(userGroupList.size() - 1);
        assertThat(testUserGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testUserGroup.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createUserGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userGroupRepository.findAll().size();

        // Create the UserGroup with an existing ID
        userGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserGroupMockMvc.perform(post("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroup)))
            .andExpect(status().isBadRequest());

        // Validate the UserGroup in the database
        List<UserGroup> userGroupList = userGroupRepository.findAll();
        assertThat(userGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userGroupRepository.findAll().size();
        // set the field null
        userGroup.setGroupName(null);

        // Create the UserGroup, which fails.

        restUserGroupMockMvc.perform(post("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroup)))
            .andExpect(status().isBadRequest());

        List<UserGroup> userGroupList = userGroupRepository.findAll();
        assertThat(userGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserGroups() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList
        restUserGroupMockMvc.perform(get("/api/user-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllUserGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        UserGroupResource userGroupResource = new UserGroupResource(userGroupServiceMock, userGroupQueryService);
        when(userGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restUserGroupMockMvc = MockMvcBuilders.standaloneSetup(userGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserGroupMockMvc.perform(get("/api/user-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(userGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllUserGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        UserGroupResource userGroupResource = new UserGroupResource(userGroupServiceMock, userGroupQueryService);
            when(userGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restUserGroupMockMvc = MockMvcBuilders.standaloneSetup(userGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserGroupMockMvc.perform(get("/api/user-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(userGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserGroup() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get the userGroup
        restUserGroupMockMvc.perform(get("/api/user-groups/{id}", userGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getUserGroupsByIdFiltering() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        Long id = userGroup.getId();

        defaultUserGroupShouldBeFound("id.equals=" + id);
        defaultUserGroupShouldNotBeFound("id.notEquals=" + id);

        defaultUserGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultUserGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserGroupShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where groupName equals to DEFAULT_GROUP_NAME
        defaultUserGroupShouldBeFound("groupName.equals=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupList where groupName equals to UPDATED_GROUP_NAME
        defaultUserGroupShouldNotBeFound("groupName.equals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where groupName not equals to DEFAULT_GROUP_NAME
        defaultUserGroupShouldNotBeFound("groupName.notEquals=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupList where groupName not equals to UPDATED_GROUP_NAME
        defaultUserGroupShouldBeFound("groupName.notEquals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsInShouldWork() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where groupName in DEFAULT_GROUP_NAME or UPDATED_GROUP_NAME
        defaultUserGroupShouldBeFound("groupName.in=" + DEFAULT_GROUP_NAME + "," + UPDATED_GROUP_NAME);

        // Get all the userGroupList where groupName equals to UPDATED_GROUP_NAME
        defaultUserGroupShouldNotBeFound("groupName.in=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where groupName is not null
        defaultUserGroupShouldBeFound("groupName.specified=true");

        // Get all the userGroupList where groupName is null
        defaultUserGroupShouldNotBeFound("groupName.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserGroupsByGroupNameContainsSomething() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where groupName contains DEFAULT_GROUP_NAME
        defaultUserGroupShouldBeFound("groupName.contains=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupList where groupName contains UPDATED_GROUP_NAME
        defaultUserGroupShouldNotBeFound("groupName.contains=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByGroupNameNotContainsSomething() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where groupName does not contain DEFAULT_GROUP_NAME
        defaultUserGroupShouldNotBeFound("groupName.doesNotContain=" + DEFAULT_GROUP_NAME);

        // Get all the userGroupList where groupName does not contain UPDATED_GROUP_NAME
        defaultUserGroupShouldBeFound("groupName.doesNotContain=" + UPDATED_GROUP_NAME);
    }


    @Test
    @Transactional
    public void getAllUserGroupsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isActive equals to DEFAULT_IS_ACTIVE
        defaultUserGroupShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the userGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserGroupShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultUserGroupShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the userGroupList where isActive not equals to UPDATED_IS_ACTIVE
        defaultUserGroupShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultUserGroupShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the userGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserGroupShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isActive is not null
        defaultUserGroupShouldBeFound("isActive.specified=true");

        // Get all the userGroupList where isActive is null
        defaultUserGroupShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserGroupsByUserPermissionsIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserPermissions userPermissions = userGroup.getUserPermissions();
        userGroupRepository.saveAndFlush(userGroup);
        Long userPermissionsId = userPermissions.getId();

        // Get all the userGroupList where userPermissions equals to userPermissionsId
        defaultUserGroupShouldBeFound("userPermissionsId.equals=" + userPermissionsId);

        // Get all the userGroupList where userPermissions equals to userPermissionsId + 1
        defaultUserGroupShouldNotBeFound("userPermissionsId.equals=" + (userPermissionsId + 1));
    }


    @Test
    @Transactional
    public void getAllUserGroupsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);
        ExUser users = ExUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        userGroup.addUsers(users);
        userGroupRepository.saveAndFlush(userGroup);
        Long usersId = users.getId();

        // Get all the userGroupList where users equals to usersId
        defaultUserGroupShouldBeFound("usersId.equals=" + usersId);

        // Get all the userGroupList where users equals to usersId + 1
        defaultUserGroupShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserGroupShouldBeFound(String filter) throws Exception {
        restUserGroupMockMvc.perform(get("/api/user-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restUserGroupMockMvc.perform(get("/api/user-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserGroupShouldNotBeFound(String filter) throws Exception {
        restUserGroupMockMvc.perform(get("/api/user-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserGroupMockMvc.perform(get("/api/user-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserGroup() throws Exception {
        // Get the userGroup
        restUserGroupMockMvc.perform(get("/api/user-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserGroup() throws Exception {
        // Initialize the database
        userGroupService.save(userGroup);

        int databaseSizeBeforeUpdate = userGroupRepository.findAll().size();

        // Update the userGroup
        UserGroup updatedUserGroup = userGroupRepository.findById(userGroup.getId()).get();
        // Disconnect from session so that the updates on updatedUserGroup are not directly saved in db
        em.detach(updatedUserGroup);
        updatedUserGroup
            .groupName(UPDATED_GROUP_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restUserGroupMockMvc.perform(put("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserGroup)))
            .andExpect(status().isOk());

        // Validate the UserGroup in the database
        List<UserGroup> userGroupList = userGroupRepository.findAll();
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate);
        UserGroup testUserGroup = userGroupList.get(userGroupList.size() - 1);
        assertThat(testUserGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testUserGroup.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserGroup() throws Exception {
        int databaseSizeBeforeUpdate = userGroupRepository.findAll().size();

        // Create the UserGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGroupMockMvc.perform(put("/api/user-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userGroup)))
            .andExpect(status().isBadRequest());

        // Validate the UserGroup in the database
        List<UserGroup> userGroupList = userGroupRepository.findAll();
        assertThat(userGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserGroup() throws Exception {
        // Initialize the database
        userGroupService.save(userGroup);

        int databaseSizeBeforeDelete = userGroupRepository.findAll().size();

        // Delete the userGroup
        restUserGroupMockMvc.perform(delete("/api/user-groups/{id}", userGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserGroup> userGroupList = userGroupRepository.findAll();
        assertThat(userGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
