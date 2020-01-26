package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.DbToolApp;
import com.alphadevs.tools.domain.CoreConfigurationItems;
import com.alphadevs.tools.repository.CoreConfigurationItemsRepository;
import com.alphadevs.tools.service.CoreConfigurationItemsService;
import com.alphadevs.tools.web.rest.errors.ExceptionTranslator;
import com.alphadevs.tools.service.dto.CoreConfigurationItemsCriteria;
import com.alphadevs.tools.service.CoreConfigurationItemsQueryService;

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
 * Integration tests for the {@link CoreConfigurationItemsResource} REST controller.
 */
@SpringBootTest(classes = DbToolApp.class)
public class CoreConfigurationItemsResourceIT {

    private static final String DEFAULT_CONFIG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private CoreConfigurationItemsRepository coreConfigurationItemsRepository;

    @Autowired
    private CoreConfigurationItemsService coreConfigurationItemsService;

    @Autowired
    private CoreConfigurationItemsQueryService coreConfigurationItemsQueryService;

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

    private MockMvc restCoreConfigurationItemsMockMvc;

    private CoreConfigurationItems coreConfigurationItems;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoreConfigurationItemsResource coreConfigurationItemsResource = new CoreConfigurationItemsResource(coreConfigurationItemsService, coreConfigurationItemsQueryService);
        this.restCoreConfigurationItemsMockMvc = MockMvcBuilders.standaloneSetup(coreConfigurationItemsResource)
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
    public static CoreConfigurationItems createEntity(EntityManager em) {
        CoreConfigurationItems coreConfigurationItems = new CoreConfigurationItems()
            .configCode(DEFAULT_CONFIG_CODE)
            .configDescription(DEFAULT_CONFIG_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return coreConfigurationItems;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoreConfigurationItems createUpdatedEntity(EntityManager em) {
        CoreConfigurationItems coreConfigurationItems = new CoreConfigurationItems()
            .configCode(UPDATED_CONFIG_CODE)
            .configDescription(UPDATED_CONFIG_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return coreConfigurationItems;
    }

    @BeforeEach
    public void initTest() {
        coreConfigurationItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoreConfigurationItems() throws Exception {
        int databaseSizeBeforeCreate = coreConfigurationItemsRepository.findAll().size();

        // Create the CoreConfigurationItems
        restCoreConfigurationItemsMockMvc.perform(post("/api/core-configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coreConfigurationItems)))
            .andExpect(status().isCreated());

        // Validate the CoreConfigurationItems in the database
        List<CoreConfigurationItems> coreConfigurationItemsList = coreConfigurationItemsRepository.findAll();
        assertThat(coreConfigurationItemsList).hasSize(databaseSizeBeforeCreate + 1);
        CoreConfigurationItems testCoreConfigurationItems = coreConfigurationItemsList.get(coreConfigurationItemsList.size() - 1);
        assertThat(testCoreConfigurationItems.getConfigCode()).isEqualTo(DEFAULT_CONFIG_CODE);
        assertThat(testCoreConfigurationItems.getConfigDescription()).isEqualTo(DEFAULT_CONFIG_DESCRIPTION);
        assertThat(testCoreConfigurationItems.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createCoreConfigurationItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coreConfigurationItemsRepository.findAll().size();

        // Create the CoreConfigurationItems with an existing ID
        coreConfigurationItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoreConfigurationItemsMockMvc.perform(post("/api/core-configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coreConfigurationItems)))
            .andExpect(status().isBadRequest());

        // Validate the CoreConfigurationItems in the database
        List<CoreConfigurationItems> coreConfigurationItemsList = coreConfigurationItemsRepository.findAll();
        assertThat(coreConfigurationItemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkConfigCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = coreConfigurationItemsRepository.findAll().size();
        // set the field null
        coreConfigurationItems.setConfigCode(null);

        // Create the CoreConfigurationItems, which fails.

        restCoreConfigurationItemsMockMvc.perform(post("/api/core-configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coreConfigurationItems)))
            .andExpect(status().isBadRequest());

        List<CoreConfigurationItems> coreConfigurationItemsList = coreConfigurationItemsRepository.findAll();
        assertThat(coreConfigurationItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = coreConfigurationItemsRepository.findAll().size();
        // set the field null
        coreConfigurationItems.setConfigDescription(null);

        // Create the CoreConfigurationItems, which fails.

        restCoreConfigurationItemsMockMvc.perform(post("/api/core-configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coreConfigurationItems)))
            .andExpect(status().isBadRequest());

        List<CoreConfigurationItems> coreConfigurationItemsList = coreConfigurationItemsRepository.findAll();
        assertThat(coreConfigurationItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItems() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList
        restCoreConfigurationItemsMockMvc.perform(get("/api/core-configuration-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coreConfigurationItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].configCode").value(hasItem(DEFAULT_CONFIG_CODE)))
            .andExpect(jsonPath("$.[*].configDescription").value(hasItem(DEFAULT_CONFIG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCoreConfigurationItems() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get the coreConfigurationItems
        restCoreConfigurationItemsMockMvc.perform(get("/api/core-configuration-items/{id}", coreConfigurationItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coreConfigurationItems.getId().intValue()))
            .andExpect(jsonPath("$.configCode").value(DEFAULT_CONFIG_CODE))
            .andExpect(jsonPath("$.configDescription").value(DEFAULT_CONFIG_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getCoreConfigurationItemsByIdFiltering() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        Long id = coreConfigurationItems.getId();

        defaultCoreConfigurationItemsShouldBeFound("id.equals=" + id);
        defaultCoreConfigurationItemsShouldNotBeFound("id.notEquals=" + id);

        defaultCoreConfigurationItemsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCoreConfigurationItemsShouldNotBeFound("id.greaterThan=" + id);

        defaultCoreConfigurationItemsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCoreConfigurationItemsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configCode equals to DEFAULT_CONFIG_CODE
        defaultCoreConfigurationItemsShouldBeFound("configCode.equals=" + DEFAULT_CONFIG_CODE);

        // Get all the coreConfigurationItemsList where configCode equals to UPDATED_CONFIG_CODE
        defaultCoreConfigurationItemsShouldNotBeFound("configCode.equals=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configCode not equals to DEFAULT_CONFIG_CODE
        defaultCoreConfigurationItemsShouldNotBeFound("configCode.notEquals=" + DEFAULT_CONFIG_CODE);

        // Get all the coreConfigurationItemsList where configCode not equals to UPDATED_CONFIG_CODE
        defaultCoreConfigurationItemsShouldBeFound("configCode.notEquals=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigCodeIsInShouldWork() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configCode in DEFAULT_CONFIG_CODE or UPDATED_CONFIG_CODE
        defaultCoreConfigurationItemsShouldBeFound("configCode.in=" + DEFAULT_CONFIG_CODE + "," + UPDATED_CONFIG_CODE);

        // Get all the coreConfigurationItemsList where configCode equals to UPDATED_CONFIG_CODE
        defaultCoreConfigurationItemsShouldNotBeFound("configCode.in=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configCode is not null
        defaultCoreConfigurationItemsShouldBeFound("configCode.specified=true");

        // Get all the coreConfigurationItemsList where configCode is null
        defaultCoreConfigurationItemsShouldNotBeFound("configCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigCodeContainsSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configCode contains DEFAULT_CONFIG_CODE
        defaultCoreConfigurationItemsShouldBeFound("configCode.contains=" + DEFAULT_CONFIG_CODE);

        // Get all the coreConfigurationItemsList where configCode contains UPDATED_CONFIG_CODE
        defaultCoreConfigurationItemsShouldNotBeFound("configCode.contains=" + UPDATED_CONFIG_CODE);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigCodeNotContainsSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configCode does not contain DEFAULT_CONFIG_CODE
        defaultCoreConfigurationItemsShouldNotBeFound("configCode.doesNotContain=" + DEFAULT_CONFIG_CODE);

        // Get all the coreConfigurationItemsList where configCode does not contain UPDATED_CONFIG_CODE
        defaultCoreConfigurationItemsShouldBeFound("configCode.doesNotContain=" + UPDATED_CONFIG_CODE);
    }


    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configDescription equals to DEFAULT_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldBeFound("configDescription.equals=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the coreConfigurationItemsList where configDescription equals to UPDATED_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldNotBeFound("configDescription.equals=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configDescription not equals to DEFAULT_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldNotBeFound("configDescription.notEquals=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the coreConfigurationItemsList where configDescription not equals to UPDATED_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldBeFound("configDescription.notEquals=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configDescription in DEFAULT_CONFIG_DESCRIPTION or UPDATED_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldBeFound("configDescription.in=" + DEFAULT_CONFIG_DESCRIPTION + "," + UPDATED_CONFIG_DESCRIPTION);

        // Get all the coreConfigurationItemsList where configDescription equals to UPDATED_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldNotBeFound("configDescription.in=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configDescription is not null
        defaultCoreConfigurationItemsShouldBeFound("configDescription.specified=true");

        // Get all the coreConfigurationItemsList where configDescription is null
        defaultCoreConfigurationItemsShouldNotBeFound("configDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigDescriptionContainsSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configDescription contains DEFAULT_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldBeFound("configDescription.contains=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the coreConfigurationItemsList where configDescription contains UPDATED_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldNotBeFound("configDescription.contains=" + UPDATED_CONFIG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByConfigDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where configDescription does not contain DEFAULT_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldNotBeFound("configDescription.doesNotContain=" + DEFAULT_CONFIG_DESCRIPTION);

        // Get all the coreConfigurationItemsList where configDescription does not contain UPDATED_CONFIG_DESCRIPTION
        defaultCoreConfigurationItemsShouldBeFound("configDescription.doesNotContain=" + UPDATED_CONFIG_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCoreConfigurationItemsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the coreConfigurationItemsList where isActive equals to UPDATED_IS_ACTIVE
        defaultCoreConfigurationItemsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCoreConfigurationItemsShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the coreConfigurationItemsList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCoreConfigurationItemsShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCoreConfigurationItemsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the coreConfigurationItemsList where isActive equals to UPDATED_IS_ACTIVE
        defaultCoreConfigurationItemsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCoreConfigurationItemsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreConfigurationItemsRepository.saveAndFlush(coreConfigurationItems);

        // Get all the coreConfigurationItemsList where isActive is not null
        defaultCoreConfigurationItemsShouldBeFound("isActive.specified=true");

        // Get all the coreConfigurationItemsList where isActive is null
        defaultCoreConfigurationItemsShouldNotBeFound("isActive.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoreConfigurationItemsShouldBeFound(String filter) throws Exception {
        restCoreConfigurationItemsMockMvc.perform(get("/api/core-configuration-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coreConfigurationItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].configCode").value(hasItem(DEFAULT_CONFIG_CODE)))
            .andExpect(jsonPath("$.[*].configDescription").value(hasItem(DEFAULT_CONFIG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCoreConfigurationItemsMockMvc.perform(get("/api/core-configuration-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoreConfigurationItemsShouldNotBeFound(String filter) throws Exception {
        restCoreConfigurationItemsMockMvc.perform(get("/api/core-configuration-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoreConfigurationItemsMockMvc.perform(get("/api/core-configuration-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCoreConfigurationItems() throws Exception {
        // Get the coreConfigurationItems
        restCoreConfigurationItemsMockMvc.perform(get("/api/core-configuration-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoreConfigurationItems() throws Exception {
        // Initialize the database
        coreConfigurationItemsService.save(coreConfigurationItems);

        int databaseSizeBeforeUpdate = coreConfigurationItemsRepository.findAll().size();

        // Update the coreConfigurationItems
        CoreConfigurationItems updatedCoreConfigurationItems = coreConfigurationItemsRepository.findById(coreConfigurationItems.getId()).get();
        // Disconnect from session so that the updates on updatedCoreConfigurationItems are not directly saved in db
        em.detach(updatedCoreConfigurationItems);
        updatedCoreConfigurationItems
            .configCode(UPDATED_CONFIG_CODE)
            .configDescription(UPDATED_CONFIG_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restCoreConfigurationItemsMockMvc.perform(put("/api/core-configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoreConfigurationItems)))
            .andExpect(status().isOk());

        // Validate the CoreConfigurationItems in the database
        List<CoreConfigurationItems> coreConfigurationItemsList = coreConfigurationItemsRepository.findAll();
        assertThat(coreConfigurationItemsList).hasSize(databaseSizeBeforeUpdate);
        CoreConfigurationItems testCoreConfigurationItems = coreConfigurationItemsList.get(coreConfigurationItemsList.size() - 1);
        assertThat(testCoreConfigurationItems.getConfigCode()).isEqualTo(UPDATED_CONFIG_CODE);
        assertThat(testCoreConfigurationItems.getConfigDescription()).isEqualTo(UPDATED_CONFIG_DESCRIPTION);
        assertThat(testCoreConfigurationItems.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingCoreConfigurationItems() throws Exception {
        int databaseSizeBeforeUpdate = coreConfigurationItemsRepository.findAll().size();

        // Create the CoreConfigurationItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoreConfigurationItemsMockMvc.perform(put("/api/core-configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coreConfigurationItems)))
            .andExpect(status().isBadRequest());

        // Validate the CoreConfigurationItems in the database
        List<CoreConfigurationItems> coreConfigurationItemsList = coreConfigurationItemsRepository.findAll();
        assertThat(coreConfigurationItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCoreConfigurationItems() throws Exception {
        // Initialize the database
        coreConfigurationItemsService.save(coreConfigurationItems);

        int databaseSizeBeforeDelete = coreConfigurationItemsRepository.findAll().size();

        // Delete the coreConfigurationItems
        restCoreConfigurationItemsMockMvc.perform(delete("/api/core-configuration-items/{id}", coreConfigurationItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoreConfigurationItems> coreConfigurationItemsList = coreConfigurationItemsRepository.findAll();
        assertThat(coreConfigurationItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
