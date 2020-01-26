package com.alphadevs.tools.service;

import com.alphadevs.tools.domain.CoreConfigurationItems;
import com.alphadevs.tools.repository.CoreConfigurationItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CoreConfigurationItems}.
 */
@Service
@Transactional
public class CoreConfigurationItemsService {

    private final Logger log = LoggerFactory.getLogger(CoreConfigurationItemsService.class);

    private final CoreConfigurationItemsRepository coreConfigurationItemsRepository;

    public CoreConfigurationItemsService(CoreConfigurationItemsRepository coreConfigurationItemsRepository) {
        this.coreConfigurationItemsRepository = coreConfigurationItemsRepository;
    }

    /**
     * Save a coreConfigurationItems.
     *
     * @param coreConfigurationItems the entity to save.
     * @return the persisted entity.
     */
    public CoreConfigurationItems save(CoreConfigurationItems coreConfigurationItems) {
        log.debug("Request to save CoreConfigurationItems : {}", coreConfigurationItems);
        return coreConfigurationItemsRepository.save(coreConfigurationItems);
    }

    /**
     * Get all the coreConfigurationItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CoreConfigurationItems> findAll(Pageable pageable) {
        log.debug("Request to get all CoreConfigurationItems");
        return coreConfigurationItemsRepository.findAll(pageable);
    }


    /**
     * Get one coreConfigurationItems by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CoreConfigurationItems> findOne(Long id) {
        log.debug("Request to get CoreConfigurationItems : {}", id);
        return coreConfigurationItemsRepository.findById(id);
    }

    /**
     * Delete the coreConfigurationItems by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CoreConfigurationItems : {}", id);
        coreConfigurationItemsRepository.deleteById(id);
    }
}
