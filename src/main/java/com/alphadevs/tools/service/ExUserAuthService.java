package com.alphadevs.tools.service;

import com.alphadevs.tools.domain.ExUserAuth;
import com.alphadevs.tools.repository.ExUserAuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExUserAuth}.
 */
@Service
@Transactional
public class ExUserAuthService {

    private final Logger log = LoggerFactory.getLogger(ExUserAuthService.class);

    private final ExUserAuthRepository exUserAuthRepository;

    public ExUserAuthService(ExUserAuthRepository exUserAuthRepository) {
        this.exUserAuthRepository = exUserAuthRepository;
    }

    /**
     * Save a exUserAuth.
     *
     * @param exUserAuth the entity to save.
     * @return the persisted entity.
     */
    public ExUserAuth save(ExUserAuth exUserAuth) {
        log.debug("Request to save ExUserAuth : {}", exUserAuth);
        return exUserAuthRepository.save(exUserAuth);
    }

    /**
     * Get all the exUserAuths.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExUserAuth> findAll(Pageable pageable) {
        log.debug("Request to get all ExUserAuths");
        return exUserAuthRepository.findAll(pageable);
    }


    /**
     * Get one exUserAuth by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExUserAuth> findOne(Long id) {
        log.debug("Request to get ExUserAuth : {}", id);
        return exUserAuthRepository.findById(id);
    }

    /**
     * Delete the exUserAuth by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExUserAuth : {}", id);
        exUserAuthRepository.deleteById(id);
    }
}
