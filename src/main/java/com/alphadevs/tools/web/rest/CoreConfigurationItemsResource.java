package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.domain.CoreConfigurationItems;
import com.alphadevs.tools.service.CoreConfigurationItemsService;
import com.alphadevs.tools.web.rest.errors.BadRequestAlertException;
import com.alphadevs.tools.service.dto.CoreConfigurationItemsCriteria;
import com.alphadevs.tools.service.CoreConfigurationItemsQueryService;

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
 * REST controller for managing {@link com.alphadevs.tools.domain.CoreConfigurationItems}.
 */
@RestController
@RequestMapping("/api")
public class CoreConfigurationItemsResource {

    private final Logger log = LoggerFactory.getLogger(CoreConfigurationItemsResource.class);

    private static final String ENTITY_NAME = "coreConfigurationItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoreConfigurationItemsService coreConfigurationItemsService;

    private final CoreConfigurationItemsQueryService coreConfigurationItemsQueryService;

    public CoreConfigurationItemsResource(CoreConfigurationItemsService coreConfigurationItemsService, CoreConfigurationItemsQueryService coreConfigurationItemsQueryService) {
        this.coreConfigurationItemsService = coreConfigurationItemsService;
        this.coreConfigurationItemsQueryService = coreConfigurationItemsQueryService;
    }

    /**
     * {@code POST  /core-configuration-items} : Create a new coreConfigurationItems.
     *
     * @param coreConfigurationItems the coreConfigurationItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coreConfigurationItems, or with status {@code 400 (Bad Request)} if the coreConfigurationItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/core-configuration-items")
    public ResponseEntity<CoreConfigurationItems> createCoreConfigurationItems(@Valid @RequestBody CoreConfigurationItems coreConfigurationItems) throws URISyntaxException {
        log.debug("REST request to save CoreConfigurationItems : {}", coreConfigurationItems);
        if (coreConfigurationItems.getId() != null) {
            throw new BadRequestAlertException("A new coreConfigurationItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoreConfigurationItems result = coreConfigurationItemsService.save(coreConfigurationItems);
        return ResponseEntity.created(new URI("/api/core-configuration-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /core-configuration-items} : Updates an existing coreConfigurationItems.
     *
     * @param coreConfigurationItems the coreConfigurationItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coreConfigurationItems,
     * or with status {@code 400 (Bad Request)} if the coreConfigurationItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coreConfigurationItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/core-configuration-items")
    public ResponseEntity<CoreConfigurationItems> updateCoreConfigurationItems(@Valid @RequestBody CoreConfigurationItems coreConfigurationItems) throws URISyntaxException {
        log.debug("REST request to update CoreConfigurationItems : {}", coreConfigurationItems);
        if (coreConfigurationItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CoreConfigurationItems result = coreConfigurationItemsService.save(coreConfigurationItems);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coreConfigurationItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /core-configuration-items} : get all the coreConfigurationItems.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coreConfigurationItems in body.
     */
    @GetMapping("/core-configuration-items")
    public ResponseEntity<List<CoreConfigurationItems>> getAllCoreConfigurationItems(CoreConfigurationItemsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CoreConfigurationItems by criteria: {}", criteria);
        Page<CoreConfigurationItems> page = coreConfigurationItemsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /core-configuration-items/count} : count all the coreConfigurationItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/core-configuration-items/count")
    public ResponseEntity<Long> countCoreConfigurationItems(CoreConfigurationItemsCriteria criteria) {
        log.debug("REST request to count CoreConfigurationItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(coreConfigurationItemsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /core-configuration-items/:id} : get the "id" coreConfigurationItems.
     *
     * @param id the id of the coreConfigurationItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coreConfigurationItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/core-configuration-items/{id}")
    public ResponseEntity<CoreConfigurationItems> getCoreConfigurationItems(@PathVariable Long id) {
        log.debug("REST request to get CoreConfigurationItems : {}", id);
        Optional<CoreConfigurationItems> coreConfigurationItems = coreConfigurationItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coreConfigurationItems);
    }

    /**
     * {@code DELETE  /core-configuration-items/:id} : delete the "id" coreConfigurationItems.
     *
     * @param id the id of the coreConfigurationItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/core-configuration-items/{id}")
    public ResponseEntity<Void> deleteCoreConfigurationItems(@PathVariable Long id) {
        log.debug("REST request to delete CoreConfigurationItems : {}", id);
        coreConfigurationItemsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
