package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.DbToolApp;
import com.alphadevs.tools.domain.Bank;
import com.alphadevs.tools.repository.BankRepository;
import com.alphadevs.tools.service.BankService;
import com.alphadevs.tools.web.rest.errors.ExceptionTranslator;
import com.alphadevs.tools.service.dto.BankCriteria;
import com.alphadevs.tools.service.BankQueryService;

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
 * Integration tests for the {@link BankResource} REST controller.
 */
@SpringBootTest(classes = DbToolApp.class)
public class BankResourceIT {

    private static final String DEFAULT_BANK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankService bankService;

    @Autowired
    private BankQueryService bankQueryService;

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

    private MockMvc restBankMockMvc;

    private Bank bank;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BankResource bankResource = new BankResource(bankService, bankQueryService);
        this.restBankMockMvc = MockMvcBuilders.standaloneSetup(bankResource)
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
    public static Bank createEntity(EntityManager em) {
        Bank bank = new Bank()
            .bankCode(DEFAULT_BANK_CODE)
            .bankName(DEFAULT_BANK_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
        return bank;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createUpdatedEntity(EntityManager em) {
        Bank bank = new Bank()
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        return bank;
    }

    @BeforeEach
    public void initTest() {
        bank = createEntity(em);
    }

    @Test
    @Transactional
    public void createBank() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank
        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isCreated());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate + 1);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getBankCode()).isEqualTo(DEFAULT_BANK_CODE);
        assertThat(testBank.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testBank.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createBankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank with an existing ID
        bank.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBankCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setBankCode(null);

        // Create the Bank, which fails.

        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBankNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setBankName(null);

        // Create the Bank, which fails.

        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBanks() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList
        restBankMockMvc.perform(get("/api/banks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bank.getId().intValue()))
            .andExpect(jsonPath("$.bankCode").value(DEFAULT_BANK_CODE))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getBanksByIdFiltering() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        Long id = bank.getId();

        defaultBankShouldBeFound("id.equals=" + id);
        defaultBankShouldNotBeFound("id.notEquals=" + id);

        defaultBankShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBankShouldNotBeFound("id.greaterThan=" + id);

        defaultBankShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBankShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBanksByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankCode equals to DEFAULT_BANK_CODE
        defaultBankShouldBeFound("bankCode.equals=" + DEFAULT_BANK_CODE);

        // Get all the bankList where bankCode equals to UPDATED_BANK_CODE
        defaultBankShouldNotBeFound("bankCode.equals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    public void getAllBanksByBankCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankCode not equals to DEFAULT_BANK_CODE
        defaultBankShouldNotBeFound("bankCode.notEquals=" + DEFAULT_BANK_CODE);

        // Get all the bankList where bankCode not equals to UPDATED_BANK_CODE
        defaultBankShouldBeFound("bankCode.notEquals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    public void getAllBanksByBankCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankCode in DEFAULT_BANK_CODE or UPDATED_BANK_CODE
        defaultBankShouldBeFound("bankCode.in=" + DEFAULT_BANK_CODE + "," + UPDATED_BANK_CODE);

        // Get all the bankList where bankCode equals to UPDATED_BANK_CODE
        defaultBankShouldNotBeFound("bankCode.in=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    public void getAllBanksByBankCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankCode is not null
        defaultBankShouldBeFound("bankCode.specified=true");

        // Get all the bankList where bankCode is null
        defaultBankShouldNotBeFound("bankCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllBanksByBankCodeContainsSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankCode contains DEFAULT_BANK_CODE
        defaultBankShouldBeFound("bankCode.contains=" + DEFAULT_BANK_CODE);

        // Get all the bankList where bankCode contains UPDATED_BANK_CODE
        defaultBankShouldNotBeFound("bankCode.contains=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    public void getAllBanksByBankCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankCode does not contain DEFAULT_BANK_CODE
        defaultBankShouldNotBeFound("bankCode.doesNotContain=" + DEFAULT_BANK_CODE);

        // Get all the bankList where bankCode does not contain UPDATED_BANK_CODE
        defaultBankShouldBeFound("bankCode.doesNotContain=" + UPDATED_BANK_CODE);
    }


    @Test
    @Transactional
    public void getAllBanksByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName equals to DEFAULT_BANK_NAME
        defaultBankShouldBeFound("bankName.equals=" + DEFAULT_BANK_NAME);

        // Get all the bankList where bankName equals to UPDATED_BANK_NAME
        defaultBankShouldNotBeFound("bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByBankNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName not equals to DEFAULT_BANK_NAME
        defaultBankShouldNotBeFound("bankName.notEquals=" + DEFAULT_BANK_NAME);

        // Get all the bankList where bankName not equals to UPDATED_BANK_NAME
        defaultBankShouldBeFound("bankName.notEquals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName in DEFAULT_BANK_NAME or UPDATED_BANK_NAME
        defaultBankShouldBeFound("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME);

        // Get all the bankList where bankName equals to UPDATED_BANK_NAME
        defaultBankShouldNotBeFound("bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName is not null
        defaultBankShouldBeFound("bankName.specified=true");

        // Get all the bankList where bankName is null
        defaultBankShouldNotBeFound("bankName.specified=false");
    }
                @Test
    @Transactional
    public void getAllBanksByBankNameContainsSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName contains DEFAULT_BANK_NAME
        defaultBankShouldBeFound("bankName.contains=" + DEFAULT_BANK_NAME);

        // Get all the bankList where bankName contains UPDATED_BANK_NAME
        defaultBankShouldNotBeFound("bankName.contains=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByBankNameNotContainsSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName does not contain DEFAULT_BANK_NAME
        defaultBankShouldNotBeFound("bankName.doesNotContain=" + DEFAULT_BANK_NAME);

        // Get all the bankList where bankName does not contain UPDATED_BANK_NAME
        defaultBankShouldBeFound("bankName.doesNotContain=" + UPDATED_BANK_NAME);
    }


    @Test
    @Transactional
    public void getAllBanksByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBankShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the bankList where isActive equals to UPDATED_IS_ACTIVE
        defaultBankShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllBanksByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultBankShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the bankList where isActive not equals to UPDATED_IS_ACTIVE
        defaultBankShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllBanksByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBankShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the bankList where isActive equals to UPDATED_IS_ACTIVE
        defaultBankShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllBanksByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where isActive is not null
        defaultBankShouldBeFound("isActive.specified=true");

        // Get all the bankList where isActive is null
        defaultBankShouldNotBeFound("isActive.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankShouldBeFound(String filter) throws Exception {
        restBankMockMvc.perform(get("/api/banks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBankMockMvc.perform(get("/api/banks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankShouldNotBeFound(String filter) throws Exception {
        restBankMockMvc.perform(get("/api/banks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankMockMvc.perform(get("/api/banks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBank() throws Exception {
        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBank() throws Exception {
        // Initialize the database
        bankService.save(bank);

        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Update the bank
        Bank updatedBank = bankRepository.findById(bank.getId()).get();
        // Disconnect from session so that the updates on updatedBank are not directly saved in db
        em.detach(updatedBank);
        updatedBank
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restBankMockMvc.perform(put("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBank)))
            .andExpect(status().isOk());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testBank.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBank.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingBank() throws Exception {
        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Create the Bank

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankMockMvc.perform(put("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBank() throws Exception {
        // Initialize the database
        bankService.save(bank);

        int databaseSizeBeforeDelete = bankRepository.findAll().size();

        // Delete the bank
        restBankMockMvc.perform(delete("/api/banks/{id}", bank.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
