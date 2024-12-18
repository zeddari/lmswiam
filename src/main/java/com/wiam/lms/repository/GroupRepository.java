package com.wiam.lms.repository;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.enumeration.GroupType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Group entity.
 *
 * When extending this class, extend GroupRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface GroupRepository extends GroupRepositoryWithBagRelationships, JpaRepository<Group, Long> {
    default Optional<Group> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Group> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Group> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select jhiGroup from Group jhiGroup left join fetch jhiGroup.site11 left join fetch jhiGroup.group1",
        countQuery = "select count(jhiGroup) from Group jhiGroup"
    )
    Page<Group> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jhiGroup from Group jhiGroup left join fetch jhiGroup.site11 left join fetch jhiGroup.group1")
    List<Group> findAllWithToOneRelationships();

    @Query("select jhiGroup from Group jhiGroup left join fetch jhiGroup.site11 left join fetch jhiGroup.group1 where jhiGroup.id =:id")
    Optional<Group> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select jhiGroup from Group jhiGroup left join fetch jhiGroup.site11 where jhiGroup.site11.id=:id")
    List<Group> findAllAbstract(@Param("id") Long id);

    @Query("select jhiGroup from Group jhiGroup left join fetch jhiGroup.elements")
    Page<Group> findAllByGroup(Pageable pageable);

    @Query(
        "select jhiGroup from Group jhiGroup " +
        "join fetch jhiGroup.site11 site " +
        "where (:siteId is null or jhiGroup.site11.id = :siteId) " +
        "and (:groupType is null or jhiGroup.groupType = :groupType) " +
        "and (:query is null or jhiGroup.nameAr like CONCAT('%', :query, '%'))"
    )
    Page<Group> findAllByGroupTypeAndSiteAndNameAr(
        Pageable pageable,
        @Param("siteId") Long siteId,
        @Param("groupType") GroupType groupType,
        @Param("query") String query
    );
}
