package com.alphadevs.tools.web.rest;

import com.alphadevs.tools.domain.Bank;
import com.alphadevs.tools.service.BankService;
import com.alphadevs.tools.web.rest.errors.BadRequestAlertException;
import com.alphadevs.tools.service.dto.BankCriteria;
import com.alphadevs.tools.service.BankQueryService;

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
 * REST controller for managing {@link com.alphadevs.tools.domain.Bank}.
 */
@RestController
@RequestMapping("/api")
public class BankResource {

    private final Logger log = LoggerFactory.getLogger(BankResource.class);

    private static final String ENTITY_NAME = "bank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankService bankService;

    private final BankQueryService bankQueryService;

    public BankResource(BankService bankService, BankQueryService bankQueryService) {
        this.bankService = bankService;
        this.bankQueryService = bankQueryService;
    }

    /**
     * {@code POST  /banks} : Create a new bank.
     *
     * @param bank the bank to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bank, or with status {@code 400 (Bad Request)} if the bank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banks")
    public ResponseEntity<Bank> createBank(@Valid @RequestBody Bank bank) throws URISyntaxException {
        log.debug("REST request to save Bank : {}", bank);
        if (bank.getId() != null) {
            throw new BadRequestAlertException("A new bank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bank result = bankService.save(bank);
        return ResponseEntity.created(new URI("/api/banks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /banks} : Updates an existing bank.
     *
     * @param bank the bank to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bank,
     * or with status {@code 400 (Bad Request)} if the bank is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bank couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/banks")
    public ResponseEntity<Bank> updateBank(@Valid @RequestBody Bank bank) throws URISyntaxException {
        log.debug("REST request to update Bank : {}", bank);
        if (bank.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bank result = bankService.save(bank);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bank.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /banks} : get all the banks.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banks in body.
     */
    @GetMapping("/banks")
    public ResponseEntity<List<Bank>> getAllBanks(BankCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Banks by criteria: {}", criteria);
        Page<Bank> page = bankQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /banks/count} : count all the banks.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/banks/count")
    public ResponseEntity<Long> countBanks(BankCriteria criteria) {
        log.debug("REST request to count Banks by criteria: {}", criteria);
        return ResponseEntity.ok().body(bankQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /banks/:id} : get the "id" bank.
     *
     * @param id the id of the bank to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bank, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/banks/{id}")
    public ResponseEntity<Bank> getBank(@PathVariable Long id) {
        log.debug("REST request to get Bank : {}", id);
        Optional<Bank> bank = bankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bank);
    }

    /**
     * {@code DELETE  /banks/:id} : delete the "id" bank.
     *
     * @param id the id of the bank to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/banks/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        log.debug("REST request to delete Bank : {}", id);
        bankService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
