package com.alphadevs.tools.repository;

import com.alphadevs.tools.domain.ExUserAuth;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExUserAuth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExUserAuthRepository extends JpaRepository<ExUserAuth, Long>, JpaSpecificationExecutor<ExUserAuth> {

}
