package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.domain.Branch;
import com.alphadevs.tools.service.BranchService;
import com.alphadevs.tools.web.rest.errors.BadRequestAlertException;
import com.alphadevs.tools.service.dto.BranchCriteria;
import com.alphadevs.tools.service.BranchQueryService;

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
 * REST controller for managing {@link com.alphadevs.tools.domain.Branch}.
 */
@RestController
@RequestMapping("/api")
public class BranchResource {

    private final Logger log = LoggerFactory.getLogger(BranchResource.class);

    private static final String ENTITY_NAME = "branch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchService branchService;

    private final BranchQueryService branchQueryService;

    public BranchResource(BranchService branchService, BranchQueryService branchQueryService) {
        this.branchService = branchService;
        this.branchQueryService = branchQueryService;
    }

    /**
     * {@code POST  /branches} : Create a new branch.
     *
     * @param branch the branch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branch, or with status {@code 400 (Bad Request)} if the branch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branches")
    public ResponseEntity<Branch> createBranch(@Valid @RequestBody Branch branch) throws URISyntaxException {
        log.debug("REST request to save Branch : {}", branch);
        if (branch.getId() != null) {
            throw new BadRequestAlertException("A new branch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Branch result = branchService.save(branch);
        return ResponseEntity.created(new URI("/api/branches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branches} : Updates an existing branch.
     *
     * @param branch the branch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branch,
     * or with status {@code 400 (Bad Request)} if the branch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branches")
    public ResponseEntity<Branch> updateBranch(@Valid @RequestBody Branch branch) throws URISyntaxException {
        log.debug("REST request to update Branch : {}", branch);
        if (branch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Branch result = branchService.save(branch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branch.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branches} : get all the branches.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branches in body.
     */
    @GetMapping("/branches")
    public ResponseEntity<List<Branch>> getAllBranches(BranchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Branches by criteria: {}", criteria);
        Page<Branch> page = branchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /branches/count} : count all the branches.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/branches/count")
    public ResponseEntity<Long> countBranches(BranchCriteria criteria) {
        log.debug("REST request to count Branches by criteria: {}", criteria);
        return ResponseEntity.ok().body(branchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /branches/:id} : get the "id" branch.
     *
     * @param id the id of the branch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branches/{id}")
    public ResponseEntity<Branch> getBranch(@PathVariable Long id) {
        log.debug("REST request to get Branch : {}", id);
        Optional<Branch> branch = branchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branch);
    }

    /**
     * {@code DELETE  /branches/:id} : delete the "id" branch.
     *
     * @param id the id of the branch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        log.debug("REST request to delete Branch : {}", id);
        branchService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
