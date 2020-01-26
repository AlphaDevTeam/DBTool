package com.alphadevs.tools.repository;

import com.alphadevs.tools.domain.Bank;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Bank entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankRepository extends JpaRepository<Bank, Long>, JpaSpecificationExecutor<Bank> {

}
