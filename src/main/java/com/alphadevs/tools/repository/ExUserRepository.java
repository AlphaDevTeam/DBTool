package com.alphadevs.tools.repository;

import com.alphadevs.tools.domain.ExUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ExUser entity.
 */
@Repository
public interface ExUserRepository extends JpaRepository<ExUser, Long>, JpaSpecificationExecutor<ExUser> {

    @Query(value = "select distinct exUser from ExUser exUser left join fetch exUser.branches left join fetch exUser.userGroups left join fetch exUser.userPermissions left join fetch exUser.userAuths",
        countQuery = "select count(distinct exUser) from ExUser exUser")
    Page<ExUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct exUser from ExUser exUser left join fetch exUser.branches left join fetch exUser.userGroups left join fetch exUser.userPermissions left join fetch exUser.userAuths")
    List<ExUser> findAllWithEagerRelationships();

    @Query("select exUser from ExUser exUser left join fetch exUser.branches left join fetch exUser.userGroups left join fetch exUser.userPermissions left join fetch exUser.userAuths where exUser.id =:id")
    Optional<ExUser> findOneWithEagerRelationships(@Param("id") Long id);

}
