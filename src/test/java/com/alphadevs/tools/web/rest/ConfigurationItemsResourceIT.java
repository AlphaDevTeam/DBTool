package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.DbToolApp;
import com.alphadevs.tools.domain.ConfigurationItems;
import com.alphadevs.tools.domain.CoreConfigurationItems;
import com.alphadevs.tools.domain.Company;
import com.alphadevs.tools.repository.ConfigurationItemsRepository;
import com.alphadevs.tools.service.ConfigurationItemsService;
import com.alphadevs.tools.web.rest.errors.ExceptionTranslator;
import com.alphadevs.tools.service.dto.ConfigurationItemsCriteria;
import com.alphadevs.tools.service.ConfigurationItemsQueryService;

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
 * Integration tests for the {@link ConfigurationItemsResource} REST controller.
 */
@SpringBootTest(classes = DbToolApp.class)
public class ConfigurationItemsResourceIT {

    private static final Boolean DEFAULT_CONFIG_ENABLED = false;
    private static final Boolean UPDATED_CONFIG_ENABLED = true;

    private static final String DEFAULT_CONFIG_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_PARAMETER = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_PARAMETER = "BBBBBBBBBB";

    @Autowired
    private ConfigurationItemsRepository configurationItemsRepository;

    @Autowired
    private ConfigurationItemsService configurationItemsService;

    @Autowired
    private ConfigurationItemsQueryService configurationItemsQueryService;

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

    private MockMvc restConfigurationItemsMockMvc;

    private ConfigurationItems configurationItems;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigurationItemsResource configurationItemsResource = new ConfigurationItemsResource(configurationItemsService, configurationItemsQueryService);
        this.restConfigurationItemsMockMvc = MockMvcBuilders.standaloneSetup(configurationItemsResource)
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
    public static ConfigurationItems createEntity(EntityManager em) {
        ConfigurationItems configurationItems = new ConfigurationItems()
            .configEnabled(DEFAULT_CONFIG_ENABLED)
            .configTypeCode(DEFAULT_CONFIG_TYPE_CODE)
            .configParameter(DEFAULT_CONFIG_PARAMETER);
        // Add required entity
        CoreConfigurationItems coreConfigurationItems;
        if (TestUtil.findAll(em, CoreConfigurationItems.class).isEmpty()) {
            coreConfigurationItems = CoreConfigurationItemsResourceIT.createEntity(em);
            em.persist(coreConfigurationItems);
            em.flush();
        } else {
            coreConfigurationItems = TestUtil.findAll(em, CoreConfigurationItems.class).get(0);
        }
        configurationItems.setCoreConfig(coreConfigurationItems);
        return configurationItems;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationItems createUpdatedEntity(EntityManager em) {
        ConfigurationItems configurationItems = new ConfigurationItems()
            .configEnabled(UPDATED_CONFIG_ENABLED)
            .configTypeCode(UPDATED_CONFIG_TYPE_CODE)
            .configParameter(UPDATED_CONFIG_PARAMETER);
        // Add required entity
        CoreConfigurationItems coreConfigurationItems;
        if (TestUtil.findAll(em, CoreConfigurationItems.class).isEmpty()) {
            coreConfigurationItems = CoreConfigurationItemsResourceIT.createUpdatedEntity(em);
            em.persist(coreConfigurationItems);
            em.flush();
        } else {
            coreConfigurationItems = TestUtil.findAll(em, CoreConfigurationItems.class).get(0);
        }
        configurationItems.setCoreConfig(coreConfigurationItems);
        return configurationItems;
    }

    @BeforeEach
    public void initTest() {
        configurationItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigurationItems() throws Exception {
        int databaseSizeBeforeCreate = configurationItemsRepository.findAll().size();

        // Create the ConfigurationItems
        restConfigurationItemsMockMvc.perform(post("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isCreated());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigurationItems testConfigurationItems = configurationItemsList.get(configurationItemsList.size() - 1);
        assertThat(testConfigurationItems.isConfigEnabled()).isEqualTo(DEFAULT_CONFIG_ENABLED);
        assertThat(testConfigurationItems.getConfigTypeCode()).isEqualTo(DEFAULT_CONFIG_TYPE_CODE);
        assertThat(testConfigurationItems.getConfigParameter()).isEqualTo(DEFAULT_CONFIG_PARAMETER);
    }

    @Test
    @Transactional
    public void createConfigurationItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configurationItemsRepository.findAll().size();

        // Create the ConfigurationItems with an existing ID
        configurationItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationItemsMockMvc.perform(post("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].configEnabled").value(hasItem(DEFAULT_CONFIG_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].configTypeCode").value(hasItem(DEFAULT_CONFIG_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].configParameter").value(hasItem(DEFAULT_CONFIG_PARAMETER)));
    }
    
    @Test
    @Transactional
    public void getConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get the configurationItems
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/{id}", configurationItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configurationItems.getId().intValue()))
            .andExpect(jsonPath("$.configEnabled").value(DEFAULT_CONFIG_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.configTypeCode").value(DEFAULT_CONFIG_TYPE_CODE))
            .andExpect(jsonPath("$.configParameter").value(DEFAULT_CONFIG_PARAMETER));
    }


    @Test
    @Transactional
    public void getConfigurationItemsByIdFiltering() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        Long id = configurationItems.getId();

        defaultConfigurationItemsShouldBeFound("id.equals=" + id);
        defaultConfigurationItemsShouldNotBeFound("id.notEquals=" + id);

        defaultConfigurationItemsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConfigurationItemsShouldNotBeFound("id.greaterThan=" + id);

        defaultConfigurationItemsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConfigurationItemsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled equals to DEFAULT_CONFIG_ENABLED
        defaultConfigurationItemsShouldBeFound("configEnabled.equals=" + DEFAULT_CONFIG_ENABLED);

        // Get all the configurationItemsList where configEnabled equals to UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldNotBeFound("configEnabled.equals=" + UPDATED_CONFIG_ENABLED);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled not equals to DEFAULT_CONFIG_ENABLED
        defaultConfigurationItemsShouldNotBeFound("configEnabled.notEquals=" + DEFAULT_CONFIG_ENABLED);

        // Get all the configurationItemsList where configEnabled not equals to UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldBeFound("configEnabled.notEquals=" + UPDATED_CONFIG_ENABLED);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled in DEFAULT_CONFIG_ENABLED or UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldBeFound("configEnabled.in=" + DEFAULT_CONFIG_ENABLED + "," + UPDATED_CONFIG_ENABLED);

        // Get all the configurationItemsList where configEnabled equals to UPDATED_CONFIG_ENABLED
        defaultConfigurationItemsShouldNotBeFound("configEnabled.in=" + UPDATED_CONFIG_ENABLED);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configEnabled is not null
        defaultConfigurationItemsShouldBeFound("configEnabled.specified=true");

        // Get all the configurationItemsList where configEnabled is null
        defaultConfigurationItemsShouldNotBeFound("configEnabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configTypeCode equals to DEFAULT_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldBeFound("configTypeCode.equals=" + DEFAULT_CONFIG_TYPE_CODE);

        // Get all the configurationItemsList where configTypeCode equals to UPDATED_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldNotBeFound("configTypeCode.equals=" + UPDATED_CONFIG_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configTypeCode not equals to DEFAULT_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldNotBeFound("configTypeCode.notEquals=" + DEFAULT_CONFIG_TYPE_CODE);

        // Get all the configurationItemsList where configTypeCode not equals to UPDATED_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldBeFound("configTypeCode.notEquals=" + UPDATED_CONFIG_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configTypeCode in DEFAULT_CONFIG_TYPE_CODE or UPDATED_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldBeFound("configTypeCode.in=" + DEFAULT_CONFIG_TYPE_CODE + "," + UPDATED_CONFIG_TYPE_CODE);

        // Get all the configurationItemsList where configTypeCode equals to UPDATED_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldNotBeFound("configTypeCode.in=" + UPDATED_CONFIG_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configTypeCode is not null
        defaultConfigurationItemsShouldBeFound("configTypeCode.specified=true");

        // Get all the configurationItemsList where configTypeCode is null
        defaultConfigurationItemsShouldNotBeFound("configTypeCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllConfigurationItemsByConfigTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configTypeCode contains DEFAULT_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldBeFound("configTypeCode.contains=" + DEFAULT_CONFIG_TYPE_CODE);

        // Get all the configurationItemsList where configTypeCode contains UPDATED_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldNotBeFound("configTypeCode.contains=" + UPDATED_CONFIG_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configTypeCode does not contain DEFAULT_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldNotBeFound("configTypeCode.doesNotContain=" + DEFAULT_CONFIG_TYPE_CODE);

        // Get all the configurationItemsList where configTypeCode does not contain UPDATED_CONFIG_TYPE_CODE
        defaultConfigurationItemsShouldBeFound("configTypeCode.doesNotContain=" + UPDATED_CONFIG_TYPE_CODE);
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParameterIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParameter equals to DEFAULT_CONFIG_PARAMETER
        defaultConfigurationItemsShouldBeFound("configParameter.equals=" + DEFAULT_CONFIG_PARAMETER);

        // Get all the configurationItemsList where configParameter equals to UPDATED_CONFIG_PARAMETER
        defaultConfigurationItemsShouldNotBeFound("configParameter.equals=" + UPDATED_CONFIG_PARAMETER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParameterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParameter not equals to DEFAULT_CONFIG_PARAMETER
        defaultConfigurationItemsShouldNotBeFound("configParameter.notEquals=" + DEFAULT_CONFIG_PARAMETER);

        // Get all the configurationItemsList where configParameter not equals to UPDATED_CONFIG_PARAMETER
        defaultConfigurationItemsShouldBeFound("configParameter.notEquals=" + UPDATED_CONFIG_PARAMETER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParameterIsInShouldWork() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParameter in DEFAULT_CONFIG_PARAMETER or UPDATED_CONFIG_PARAMETER
        defaultConfigurationItemsShouldBeFound("configParameter.in=" + DEFAULT_CONFIG_PARAMETER + "," + UPDATED_CONFIG_PARAMETER);

        // Get all the configurationItemsList where configParameter equals to UPDATED_CONFIG_PARAMETER
        defaultConfigurationItemsShouldNotBeFound("configParameter.in=" + UPDATED_CONFIG_PARAMETER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParameterIsNullOrNotNull() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParameter is not null
        defaultConfigurationItemsShouldBeFound("configParameter.specified=true");

        // Get all the configurationItemsList where configParameter is null
        defaultConfigurationItemsShouldNotBeFound("configParameter.specified=false");
    }
                @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParameterContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParameter contains DEFAULT_CONFIG_PARAMETER
        defaultConfigurationItemsShouldBeFound("configParameter.contains=" + DEFAULT_CONFIG_PARAMETER);

        // Get all the configurationItemsList where configParameter contains UPDATED_CONFIG_PARAMETER
        defaultConfigurationItemsShouldNotBeFound("configParameter.contains=" + UPDATED_CONFIG_PARAMETER);
    }

    @Test
    @Transactional
    public void getAllConfigurationItemsByConfigParameterNotContainsSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);

        // Get all the configurationItemsList where configParameter does not contain DEFAULT_CONFIG_PARAMETER
        defaultConfigurationItemsShouldNotBeFound("configParameter.doesNotContain=" + DEFAULT_CONFIG_PARAMETER);

        // Get all the configurationItemsList where configParameter does not contain UPDATED_CONFIG_PARAMETER
        defaultConfigurationItemsShouldBeFound("configParameter.doesNotContain=" + UPDATED_CONFIG_PARAMETER);
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByCoreConfigIsEqualToSomething() throws Exception {
        // Get already existing entity
        CoreConfigurationItems coreConfig = configurationItems.getCoreConfig();
        configurationItemsRepository.saveAndFlush(configurationItems);
        Long coreConfigId = coreConfig.getId();

        // Get all the configurationItemsList where coreConfig equals to coreConfigId
        defaultConfigurationItemsShouldBeFound("coreConfigId.equals=" + coreConfigId);

        // Get all the configurationItemsList where coreConfig equals to coreConfigId + 1
        defaultConfigurationItemsShouldNotBeFound("coreConfigId.equals=" + (coreConfigId + 1));
    }


    @Test
    @Transactional
    public void getAllConfigurationItemsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        configurationItemsRepository.saveAndFlush(configurationItems);
        Company location = CompanyResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        configurationItems.addLocation(location);
        configurationItemsRepository.saveAndFlush(configurationItems);
        Long locationId = location.getId();

        // Get all the configurationItemsList where location equals to locationId
        defaultConfigurationItemsShouldBeFound("locationId.equals=" + locationId);

        // Get all the configurationItemsList where location equals to locationId + 1
        defaultConfigurationItemsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigurationItemsShouldBeFound(String filter) throws Exception {
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].configEnabled").value(hasItem(DEFAULT_CONFIG_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].configTypeCode").value(hasItem(DEFAULT_CONFIG_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].configParameter").value(hasItem(DEFAULT_CONFIG_PARAMETER)));

        // Check, that the count call also returns 1
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfigurationItemsShouldNotBeFound(String filter) throws Exception {
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingConfigurationItems() throws Exception {
        // Get the configurationItems
        restConfigurationItemsMockMvc.perform(get("/api/configuration-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsService.save(configurationItems);

        int databaseSizeBeforeUpdate = configurationItemsRepository.findAll().size();

        // Update the configurationItems
        ConfigurationItems updatedConfigurationItems = configurationItemsRepository.findById(configurationItems.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurationItems are not directly saved in db
        em.detach(updatedConfigurationItems);
        updatedConfigurationItems
            .configEnabled(UPDATED_CONFIG_ENABLED)
            .configTypeCode(UPDATED_CONFIG_TYPE_CODE)
            .configParameter(UPDATED_CONFIG_PARAMETER);

        restConfigurationItemsMockMvc.perform(put("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigurationItems)))
            .andExpect(status().isOk());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationItems testConfigurationItems = configurationItemsList.get(configurationItemsList.size() - 1);
        assertThat(testConfigurationItems.isConfigEnabled()).isEqualTo(UPDATED_CONFIG_ENABLED);
        assertThat(testConfigurationItems.getConfigTypeCode()).isEqualTo(UPDATED_CONFIG_TYPE_CODE);
        assertThat(testConfigurationItems.getConfigParameter()).isEqualTo(UPDATED_CONFIG_PARAMETER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigurationItems() throws Exception {
        int databaseSizeBeforeUpdate = configurationItemsRepository.findAll().size();

        // Create the ConfigurationItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationItemsMockMvc.perform(put("/api/configuration-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationItems)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationItems in the database
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigurationItems() throws Exception {
        // Initialize the database
        configurationItemsService.save(configurationItems);

        int databaseSizeBeforeDelete = configurationItemsRepository.findAll().size();

        // Delete the configurationItems
        restConfigurationItemsMockMvc.perform(delete("/api/configuration-items/{id}", configurationItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigurationItems> configurationItemsList = configurationItemsRepository.findAll();
        assertThat(configurationItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
