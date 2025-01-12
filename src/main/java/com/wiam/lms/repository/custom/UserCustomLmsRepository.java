package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.custom.ElementDto;
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
    /*@Query(
        value="select userCustom from UserCustom userCustom  left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2 where userCustom.role =:role and userCustom.sex =:sex and userCustom.accountStatus =:accountStatus and userCustom.site13.id =:siteId",
        countQuery="select count(*) from UserCustom userCustom  left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2 where userCustom.role =:role and userCustom.sex =:sex and userCustom.accountStatus =:accountStatus and userCustom.site13.id =:siteId",
        nativeQuery = true
    )*/
    @Query(
        "select userCustom from UserCustom userCustom  left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2 where userCustom.role =:role and userCustom.sex =:sex and userCustom.accountStatus =:accountStatus and userCustom.site13.id =:siteId"
    )
    Page<UserCustom> getUsers(
        Pageable pageable,
        @Param("role") Role role,
        @Param("siteId") Long siteId,
        @Param("accountStatus") AccountStatus accountStatus,
        @Param("sex") Sex sex
    );

    public UserCustom findByLogin(String login);

    @Query("select userCustom from UserCustom userCustom where userCustom.role =:role")
    public List<UserCustom> findByRole(@Param("role") Role role);

    @Query(
        "select new com.wiam.lms.domain.dto.custom.ElementDto(" +
        "userCustom.id, " +
        "concat(userCustom.firstName, ' ', userCustom.lastName)) " +
        "from UserCustom userCustom " +
        "join userCustom.authorities authority " + // Join with authorities to filter by authority
        "where authority = :authority and userCustom.site13.id = :siteId"
    )
    public List<ElementDto> findByRoleSite(@Param("authority") Authority authority, @Param("siteId") Long siteId);

    @Query(
        "select new com.wiam.lms.domain.dto.custom.ElementDto(userCustom.id, concat(userCustom.firstName, ' ', userCustom.lastName)) " +
        "from UserCustom userCustom where userCustom.role = :role and userCustom.site13.id = :siteId"
    )
    public List<ElementDto> findByRoleAndSite(@Param("role") Role role, @Param("siteId") Long siteId);
}
