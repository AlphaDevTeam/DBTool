package com.alphadevs.tools.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.alphadevs.tools.domain.Bank;
import com.alphadevs.tools.domain.*; // for static metamodels
import com.alphadevs.tools.repository.BankRepository;
import com.alphadevs.tools.service.dto.BankCriteria;

/**
 * Service for executing complex queries for {@link Bank} entities in the database.
 * The main input is a {@link BankCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Bank} or a {@link Page} of {@link Bank} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankQueryService extends QueryService<Bank> {

    private final Logger log = LoggerFactory.getLogger(BankQueryService.class);

    private final BankRepository bankRepository;

    public BankQueryService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    /**
     * Return a {@link List} of {@link Bank} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Bank> findByCriteria(BankCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Bank> specification = createSpecification(criteria);
        return bankRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Bank} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Bank> findByCriteria(BankCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bank> specification = createSpecification(criteria);
        return bankRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Bank> specification = createSpecification(criteria);
        return bankRepository.count(specification);
    }

    /**
     * Function to convert {@link BankCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bank> createSpecification(BankCriteria criteria) {
        Specification<Bank> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bank_.id));
            }
            if (criteria.getBankCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankCode(), Bank_.bankCode));
            }
            if (criteria.getBankName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankName(), Bank_.bankName));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Bank_.isActive));
            }
        }
        return specification;
    }
}
