package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.DbToolApp;
import com.alphadevs.tools.domain.Branch;
import com.alphadevs.tools.domain.Bank;
import com.alphadevs.tools.domain.ExUser;
import com.alphadevs.tools.repository.BranchRepository;
import com.alphadevs.tools.service.BranchService;
import com.alphadevs.tools.web.rest.errors.ExceptionTranslator;
import com.alphadevs.tools.service.dto.BranchCriteria;
import com.alphadevs.tools.service.BranchQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alphadevs.tools.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BranchResource} REST controller.
 */
@SpringBootTest(classes = DbToolApp.class)
public class BranchResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchService branchService;

    @Autowired
    private BranchQueryService branchQueryService;

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

    private MockMvc restBranchMockMvc;

    private Branch branch;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BranchResource branchResource = new BranchResource(branchService, branchQueryService);
        this.restBranchMockMvc = MockMvcBuilders.standaloneSetup(branchResource)
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
    public static Branch createEntity(EntityManager em) {
        Branch branch = new Branch()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchName(DEFAULT_BRANCH_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Bank bank;
        if (TestUtil.findAll(em, Bank.class).isEmpty()) {
            bank = BankResourceIT.createEntity(em);
            em.persist(bank);
            em.flush();
        } else {
            bank = TestUtil.findAll(em, Bank.class).get(0);
        }
        branch.setBank(bank);
        // Add required entity
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            exUser = ExUserResourceIT.createEntity(em);
            em.persist(exUser);
            em.flush();
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        branch.getUsers().add(exUser);
        return branch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createUpdatedEntity(EntityManager em) {
        Branch branch = new Branch()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Bank bank;
        if (TestUtil.findAll(em, Bank.class).isEmpty()) {
            bank = BankResourceIT.createUpdatedEntity(em);
            em.persist(bank);
            em.flush();
        } else {
            bank = TestUtil.findAll(em, Bank.class).get(0);
        }
        branch.setBank(bank);
        // Add required entity
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            exUser = ExUserResourceIT.createUpdatedEntity(em);
            em.persist(exUser);
            em.flush();
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        branch.getUsers().add(exUser);
        return branch;
    }

    @BeforeEach
    public void initTest() {
        branch = createEntity(em);
    }

    @Test
    @Transactional
    public void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch
        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchCode()).isEqualTo(DEFAULT_BRANCH_CODE);
        assertThat(testBranch.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);
        assertThat(testBranch.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createBranchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch with an existing ID
        branch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBranchCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setBranchCode(null);

        // Create the Branch, which fails.

        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isBadRequest());

        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBranchNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setBranchName(null);

        // Create the Branch, which fails.

        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isBadRequest());

        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getBranchesByIdFiltering() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        Long id = branch.getId();

        defaultBranchShouldBeFound("id.equals=" + id);
        defaultBranchShouldNotBeFound("id.notEquals=" + id);

        defaultBranchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode equals to DEFAULT_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.equals=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode equals to UPDATED_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode not equals to DEFAULT_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.notEquals=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode not equals to UPDATED_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.notEquals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode in DEFAULT_BRANCH_CODE or UPDATED_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE);

        // Get all the branchList where branchCode equals to UPDATED_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.in=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode is not null
        defaultBranchShouldBeFound("branchCode.specified=true");

        // Get all the branchList where branchCode is null
        defaultBranchShouldNotBeFound("branchCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode contains DEFAULT_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.contains=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode contains UPDATED_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode does not contain DEFAULT_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode does not contain UPDATED_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName equals to DEFAULT_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.equals=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName equals to UPDATED_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.equals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName not equals to DEFAULT_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.notEquals=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName not equals to UPDATED_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.notEquals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName in DEFAULT_BRANCH_NAME or UPDATED_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.in=" + DEFAULT_BRANCH_NAME + "," + UPDATED_BRANCH_NAME);

        // Get all the branchList where branchName equals to UPDATED_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.in=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName is not null
        defaultBranchShouldBeFound("branchName.specified=true");

        // Get all the branchList where branchName is null
        defaultBranchShouldNotBeFound("branchName.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchNameContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName contains DEFAULT_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.contains=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName contains UPDATED_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.contains=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName does not contain DEFAULT_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.doesNotContain=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName does not contain UPDATED_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.doesNotContain=" + UPDATED_BRANCH_NAME);
    }


    @Test
    @Transactional
    public void getAllBranchesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBranchShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the branchList where isActive equals to UPDATED_IS_ACTIVE
        defaultBranchShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllBranchesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultBranchShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the branchList where isActive not equals to UPDATED_IS_ACTIVE
        defaultBranchShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllBranchesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBranchShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the branchList where isActive equals to UPDATED_IS_ACTIVE
        defaultBranchShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllBranchesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where isActive is not null
        defaultBranchShouldBeFound("isActive.specified=true");

        // Get all the branchList where isActive is null
        defaultBranchShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllBranchesByBankIsEqualToSomething() throws Exception {
        // Get already existing entity
        Bank bank = branch.getBank();
        branchRepository.saveAndFlush(branch);
        Long bankId = bank.getId();

        // Get all the branchList where bank equals to bankId
        defaultBranchShouldBeFound("bankId.equals=" + bankId);

        // Get all the branchList where bank equals to bankId + 1
        defaultBranchShouldNotBeFound("bankId.equals=" + (bankId + 1));
    }


    @Test
    @Transactional
    public void getAllBranchesByUsersIsEqualToSomething() throws Exception {
        // Get already existing entity
        ExUser users = branch.getUsers();
        branchRepository.saveAndFlush(branch);
        Long usersId = users.getId();

        // Get all the branchList where users equals to usersId
        defaultBranchShouldBeFound("usersId.equals=" + usersId);

        // Get all the branchList where users equals to usersId + 1
        defaultBranchShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchShouldBeFound(String filter) throws Exception {
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBranchMockMvc.perform(get("/api/branches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchShouldNotBeFound(String filter) throws Exception {
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchMockMvc.perform(get("/api/branches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranch() throws Exception {
        // Initialize the database
        branchService.save(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        Branch updatedBranch = branchRepository.findById(branch.getId()).get();
        // Disconnect from session so that the updates on updatedBranch are not directly saved in db
        em.detach(updatedBranch);
        updatedBranch
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restBranchMockMvc.perform(put("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBranch)))
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testBranch.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
        assertThat(testBranch.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Create the Branch

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc.perform(put("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBranch() throws Exception {
        // Initialize the database
        branchService.save(branch);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Delete the branch
        restBranchMockMvc.perform(delete("/api/branches/{id}", branch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
