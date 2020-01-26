package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.domain.ExUserAuth;
import com.alphadevs.tools.service.ExUserAuthService;
import com.alphadevs.tools.web.rest.errors.BadRequestAlertException;
import com.alphadevs.tools.service.dto.ExUserAuthCriteria;
import com.alphadevs.tools.service.ExUserAuthQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.tools.domain.ExUserAuth}.
 */
@RestController
@RequestMapping("/api")
public class ExUserAuthResource {

    private final Logger log = LoggerFactory.getLogger(ExUserAuthResource.class);

    private static final String ENTITY_NAME = "exUserAuth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExUserAuthService exUserAuthService;

    private final ExUserAuthQueryService exUserAuthQueryService;

    public ExUserAuthResource(ExUserAuthService exUserAuthService, ExUserAuthQueryService exUserAuthQueryService) {
        this.exUserAuthService = exUserAuthService;
        this.exUserAuthQueryService = exUserAuthQueryService;
    }

    /**
     * {@code POST  /ex-user-auths} : Create a new exUserAuth.
     *
     * @param exUserAuth the exUserAuth to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exUserAuth, or with status {@code 400 (Bad Request)} if the exUserAuth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ex-user-auths")
    public ResponseEntity<ExUserAuth> createExUserAuth(@Valid @RequestBody ExUserAuth exUserAuth) throws URISyntaxException {
        log.debug("REST request to save ExUserAuth : {}", exUserAuth);
        if (exUserAuth.getId() != null) {
            throw new BadRequestAlertException("A new exUserAuth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExUserAuth result = exUserAuthService.save(exUserAuth);
        return ResponseEntity.created(new URI("/api/ex-user-auths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ex-user-auths} : Updates an existing exUserAuth.
     *
     * @param exUserAuth the exUserAuth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exUserAuth,
     * or with status {@code 400 (Bad Request)} if the exUserAuth is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exUserAuth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ex-user-auths")
    public ResponseEntity<ExUserAuth> updateExUserAuth(@Valid @RequestBody ExUserAuth exUserAuth) throws URISyntaxException {
        log.debug("REST request to update ExUserAuth : {}", exUserAuth);
        if (exUserAuth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExUserAuth result = exUserAuthService.save(exUserAuth);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exUserAuth.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ex-user-auths} : get all the exUserAuths.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exUserAuths in body.
     */
    @GetMapping("/ex-user-auths")
    public ResponseEntity<List<ExUserAuth>> getAllExUserAuths(ExUserAuthCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExUserAuths by criteria: {}", criteria);
        Page<ExUserAuth> page = exUserAuthQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /ex-user-auths/count} : count all the exUserAuths.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/ex-user-auths/count")
    public ResponseEntity<Long> countExUserAuths(ExUserAuthCriteria criteria) {
        log.debug("REST request to count ExUserAuths by criteria: {}", criteria);
        return ResponseEntity.ok().body(exUserAuthQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ex-user-auths/:id} : get the "id" exUserAuth.
     *
     * @param id the id of the exUserAuth to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exUserAuth, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ex-user-auths/{id}")
    public ResponseEntity<ExUserAuth> getExUserAuth(@PathVariable Long id) {
        log.debug("REST request to get ExUserAuth : {}", id);
        Optional<ExUserAuth> exUserAuth = exUserAuthService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exUserAuth);
    }

    /**
     * {@code DELETE  /ex-user-auths/:id} : delete the "id" exUserAuth.
     *
     * @param id the id of the exUserAuth to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ex-user-auths/{id}")
    public ResponseEntity<Void> deleteExUserAuth(@PathVariable Long id) {
        log.debug("REST request to delete ExUserAuth : {}", id);
        exUserAuthService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
