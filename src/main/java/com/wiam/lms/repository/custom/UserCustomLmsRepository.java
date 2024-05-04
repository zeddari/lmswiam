package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
import com.wiam.lms.repository.UserCustomRepositoryWithBagRelationships;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserCustom entity.
 *
 * When extending this class, extend UserCustomRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface UserCustomLmsRepository extends UserCustomRepositoryWithBagRelationships, JpaRepository<UserCustom, Long> {
    @Query(
        "select userCustom from UserCustom userCustom left join fetch userCustom.user left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2 where userCustom.role =:role and userCustom.sex =:sex and userCustom.accountStatus =:accountStatus and userCustom.site13.id =:siteId"
    )
    List<UserCustom> getUsers(
        @Param("role") Role role,
        @Param("siteId") Long siteId,
        @Param("accountStatus") AccountStatus accountStatus,
        @Param("sex") Sex sex
    );
}
