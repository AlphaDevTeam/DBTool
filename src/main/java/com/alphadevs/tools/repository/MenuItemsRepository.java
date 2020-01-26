package com.alphadevs.tools.repository;

import com.alphadevs.tools.domain.MenuItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MenuItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemsRepository extends JpaRepository<MenuItems, Long>, JpaSpecificationExecutor<MenuItems> {

}
