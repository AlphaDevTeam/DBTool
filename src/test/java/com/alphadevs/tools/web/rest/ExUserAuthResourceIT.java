package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.DbToolApp;
import com.alphadevs.tools.domain.ExUserAuth;
import com.alphadevs.tools.domain.ExUser;
import com.alphadevs.tools.repository.ExUserAuthRepository;
import com.alphadevs.tools.service.ExUserAuthService;
import com.alphadevs.tools.web.rest.errors.ExceptionTranslator;
import com.alphadevs.tools.service.dto.ExUserAuthCriteria;
import com.alphadevs.tools.service.ExUserAuthQueryService;

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
 * Integration tests for the {@link ExUserAuthResource} REST controller.
 */
@SpringBootTest(classes = DbToolApp.class)
public class ExUserAuthResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private ExUserAuthRepository exUserAuthRepository;

    @Autowired
    private ExUserAuthService exUserAuthService;

    @Autowired
    private ExUserAuthQueryService exUserAuthQueryService;

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

    private MockMvc restExUserAuthMockMvc;

    private ExUserAuth exUserAuth;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExUserAuthResource exUserAuthResource = new ExUserAuthResource(exUserAuthService, exUserAuthQueryService);
        this.restExUserAuthMockMvc = MockMvcBuilders.standaloneSetup(exUserAuthResource)
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
    public static ExUserAuth createEntity(EntityManager em) {
        ExUserAuth exUserAuth = new ExUserAuth()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return exUserAuth;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExUserAuth createUpdatedEntity(EntityManager em) {
        ExUserAuth exUserAuth = new ExUserAuth()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return exUserAuth;
    }

    @BeforeEach
    public void initTest() {
        exUserAuth = createEntity(em);
    }

    @Test
    @Transactional
    public void createExUserAuth() throws Exception {
        int databaseSizeBeforeCreate = exUserAuthRepository.findAll().size();

        // Create the ExUserAuth
        restExUserAuthMockMvc.perform(post("/api/ex-user-auths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUserAuth)))
            .andExpect(status().isCreated());

        // Validate the ExUserAuth in the database
        List<ExUserAuth> exUserAuthList = exUserAuthRepository.findAll();
        assertThat(exUserAuthList).hasSize(databaseSizeBeforeCreate + 1);
        ExUserAuth testExUserAuth = exUserAuthList.get(exUserAuthList.size() - 1);
        assertThat(testExUserAuth.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExUserAuth.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExUserAuth.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createExUserAuthWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exUserAuthRepository.findAll().size();

        // Create the ExUserAuth with an existing ID
        exUserAuth.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExUserAuthMockMvc.perform(post("/api/ex-user-auths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUserAuth)))
            .andExpect(status().isBadRequest());

        // Validate the ExUserAuth in the database
        List<ExUserAuth> exUserAuthList = exUserAuthRepository.findAll();
        assertThat(exUserAuthList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserAuthRepository.findAll().size();
        // set the field null
        exUserAuth.setCode(null);

        // Create the ExUserAuth, which fails.

        restExUserAuthMockMvc.perform(post("/api/ex-user-auths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUserAuth)))
            .andExpect(status().isBadRequest());

        List<ExUserAuth> exUserAuthList = exUserAuthRepository.findAll();
        assertThat(exUserAuthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserAuthRepository.findAll().size();
        // set the field null
        exUserAuth.setDescription(null);

        // Create the ExUserAuth, which fails.

        restExUserAuthMockMvc.perform(post("/api/ex-user-auths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUserAuth)))
            .andExpect(status().isBadRequest());

        List<ExUserAuth> exUserAuthList = exUserAuthRepository.findAll();
        assertThat(exUserAuthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExUserAuths() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList
        restExUserAuthMockMvc.perform(get("/api/ex-user-auths?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUserAuth.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getExUserAuth() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get the exUserAuth
        restExUserAuthMockMvc.perform(get("/api/ex-user-auths/{id}", exUserAuth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exUserAuth.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getExUserAuthsByIdFiltering() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        Long id = exUserAuth.getId();

        defaultExUserAuthShouldBeFound("id.equals=" + id);
        defaultExUserAuthShouldNotBeFound("id.notEquals=" + id);

        defaultExUserAuthShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExUserAuthShouldNotBeFound("id.greaterThan=" + id);

        defaultExUserAuthShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExUserAuthShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExUserAuthsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where code equals to DEFAULT_CODE
        defaultExUserAuthShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the exUserAuthList where code equals to UPDATED_CODE
        defaultExUserAuthShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where code not equals to DEFAULT_CODE
        defaultExUserAuthShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the exUserAuthList where code not equals to UPDATED_CODE
        defaultExUserAuthShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where code in DEFAULT_CODE or UPDATED_CODE
        defaultExUserAuthShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the exUserAuthList where code equals to UPDATED_CODE
        defaultExUserAuthShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where code is not null
        defaultExUserAuthShouldBeFound("code.specified=true");

        // Get all the exUserAuthList where code is null
        defaultExUserAuthShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUserAuthsByCodeContainsSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where code contains DEFAULT_CODE
        defaultExUserAuthShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the exUserAuthList where code contains UPDATED_CODE
        defaultExUserAuthShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where code does not contain DEFAULT_CODE
        defaultExUserAuthShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the exUserAuthList where code does not contain UPDATED_CODE
        defaultExUserAuthShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllExUserAuthsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where description equals to DEFAULT_DESCRIPTION
        defaultExUserAuthShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the exUserAuthList where description equals to UPDATED_DESCRIPTION
        defaultExUserAuthShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where description not equals to DEFAULT_DESCRIPTION
        defaultExUserAuthShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the exUserAuthList where description not equals to UPDATED_DESCRIPTION
        defaultExUserAuthShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultExUserAuthShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the exUserAuthList where description equals to UPDATED_DESCRIPTION
        defaultExUserAuthShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where description is not null
        defaultExUserAuthShouldBeFound("description.specified=true");

        // Get all the exUserAuthList where description is null
        defaultExUserAuthShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllExUserAuthsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where description contains DEFAULT_DESCRIPTION
        defaultExUserAuthShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the exUserAuthList where description contains UPDATED_DESCRIPTION
        defaultExUserAuthShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where description does not contain DEFAULT_DESCRIPTION
        defaultExUserAuthShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the exUserAuthList where description does not contain UPDATED_DESCRIPTION
        defaultExUserAuthShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllExUserAuthsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where isActive equals to DEFAULT_IS_ACTIVE
        defaultExUserAuthShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the exUserAuthList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserAuthShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultExUserAuthShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the exUserAuthList where isActive not equals to UPDATED_IS_ACTIVE
        defaultExUserAuthShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultExUserAuthShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the exUserAuthList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserAuthShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);

        // Get all the exUserAuthList where isActive is not null
        defaultExUserAuthShouldBeFound("isActive.specified=true");

        // Get all the exUserAuthList where isActive is null
        defaultExUserAuthShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllExUserAuthsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserAuthRepository.saveAndFlush(exUserAuth);
        ExUser user = ExUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        exUserAuth.addUser(user);
        exUserAuthRepository.saveAndFlush(exUserAuth);
        Long userId = user.getId();

        // Get all the exUserAuthList where user equals to userId
        defaultExUserAuthShouldBeFound("userId.equals=" + userId);

        // Get all the exUserAuthList where user equals to userId + 1
        defaultExUserAuthShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExUserAuthShouldBeFound(String filter) throws Exception {
        restExUserAuthMockMvc.perform(get("/api/ex-user-auths?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUserAuth.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restExUserAuthMockMvc.perform(get("/api/ex-user-auths/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExUserAuthShouldNotBeFound(String filter) throws Exception {
        restExUserAuthMockMvc.perform(get("/api/ex-user-auths?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExUserAuthMockMvc.perform(get("/api/ex-user-auths/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExUserAuth() throws Exception {
        // Get the exUserAuth
        restExUserAuthMockMvc.perform(get("/api/ex-user-auths/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExUserAuth() throws Exception {
        // Initialize the database
        exUserAuthService.save(exUserAuth);

        int databaseSizeBeforeUpdate = exUserAuthRepository.findAll().size();

        // Update the exUserAuth
        ExUserAuth updatedExUserAuth = exUserAuthRepository.findById(exUserAuth.getId()).get();
        // Disconnect from session so that the updates on updatedExUserAuth are not directly saved in db
        em.detach(updatedExUserAuth);
        updatedExUserAuth
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restExUserAuthMockMvc.perform(put("/api/ex-user-auths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExUserAuth)))
            .andExpect(status().isOk());

        // Validate the ExUserAuth in the database
        List<ExUserAuth> exUserAuthList = exUserAuthRepository.findAll();
        assertThat(exUserAuthList).hasSize(databaseSizeBeforeUpdate);
        ExUserAuth testExUserAuth = exUserAuthList.get(exUserAuthList.size() - 1);
        assertThat(testExUserAuth.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExUserAuth.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExUserAuth.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingExUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = exUserAuthRepository.findAll().size();

        // Create the ExUserAuth

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExUserAuthMockMvc.perform(put("/api/ex-user-auths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUserAuth)))
            .andExpect(status().isBadRequest());

        // Validate the ExUserAuth in the database
        List<ExUserAuth> exUserAuthList = exUserAuthRepository.findAll();
        assertThat(exUserAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExUserAuth() throws Exception {
        // Initialize the database
        exUserAuthService.save(exUserAuth);

        int databaseSizeBeforeDelete = exUserAuthRepository.findAll().size();

        // Delete the exUserAuth
        restExUserAuthMockMvc.perform(delete("/api/ex-user-auths/{id}", exUserAuth.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExUserAuth> exUserAuthList = exUserAuthRepository.findAll();
        assertThat(exUserAuthList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
