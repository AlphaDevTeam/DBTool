package com.alphadevs.tools.repository;

import com.alphadevs.tools.domain.LicenseType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LicenseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicenseTypeRepository extends JpaRepository<LicenseType, Long>, JpaSpecificationExecutor<LicenseType> {

}
