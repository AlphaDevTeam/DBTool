package com.alphadevs.tools.repository;

import com.alphadevs.tools.domain.CoreConfigurationItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CoreConfigurationItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoreConfigurationItemsRepository extends JpaRepository<CoreConfigurationItems, Long>, JpaSpecificationExecutor<CoreConfigurationItems> {

}
