package com.wiam.lms.repository;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserCustom entity.
 *
 * When extending this class, extend UserCustomRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface UserCustomRepository extends UserCustomRepositoryWithBagRelationships, JpaRepository<UserCustom, Long> {
    default Optional<UserCustom> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<UserCustom> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<UserCustom> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select userCustom from UserCustom userCustom  left join fetch userCustom.city left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2",
        countQuery = "select count(userCustom) from UserCustom userCustom"
    )
    Page<UserCustom> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select userCustom from UserCustom userCustom  left join fetch userCustom.city left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2"
    )
    List<UserCustom> findAllWithToOneRelationships();

    @Query(
        "select userCustom from UserCustom userCustom  left join fetch userCustom.city left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2 where userCustom.id =:id"
    )
    Optional<UserCustom> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select userCustom from UserCustom userCustom  left join fetch userCustom.city left join fetch userCustom.site13 left join fetch userCustom.country left join fetch userCustom.nationality left join fetch userCustom.job left join fetch userCustom.departement2 where userCustom.login =:login"
    )
    Optional<UserCustom> findUserCustomByLogin(@Param("login") String login);

    @Query(
        "select userCustom from UserCustom userCustom left join fetch userCustom.city left join fetch userCustom.groups where userCustom.id=:id"
    )
    Optional<UserCustom> findByIdforGroup(Long id);

    @Query(
        "SELECT u FROM UserCustom u left join fetch u.site13 left join fetch u.city left join fetch u.country left join fetch u.departement2 left join fetch u.job  left join fetch u.nationality WHERE (" +
        " (:role IS NULL OR u.role = :role)" +
        " AND (:firstName IS NULL OR u.firstName LIKE CONCAT('%', :firstName, '%'))" +
        " AND (:lastName IS NULL OR u.lastName LIKE CONCAT('%', :lastName, '%'))" +
        " AND (:siteId IS NULL OR u.site13.id = :siteId)" +
        " AND (:accountStatus IS NULL OR u.accountStatus = :accountStatus)" +
        " AND (:sex IS NULL OR u.sex = :sex))"
    )
    Page<UserCustom> searchUsers(
        Pageable pageable,
        @Param("firstName") String firstName,
        @Param("lastName") String lastName,
        @Param("role") Role role,
        @Param("siteId") Long siteId,
        @Param("accountStatus") AccountStatus accountStatus,
        @Param("sex") Sex sex
    );
}
