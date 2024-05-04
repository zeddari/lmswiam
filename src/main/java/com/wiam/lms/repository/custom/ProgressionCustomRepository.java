package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.service.dto.FollowupAvgDTO;
import com.wiam.lms.service.dto.FollowupListDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Progression entity.
 */
@Repository
public interface ProgressionCustomRepository extends JpaRepository<Progression, Long> {
    default Optional<Progression> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Progression> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Progression> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select progression from Progression progression left join fetch progression.site17 left join fetch progression.sessionInstance left join fetch progression.student",
        countQuery = "select count(progression) from Progression progression"
    )
    Page<Progression> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select progression from Progression progression left join fetch progression.site17 left join fetch progression.sessionInstance left join fetch progression.student"
    )
    List<Progression> findAllWithToOneRelationships();

    @Query(
        "select progression from Progression progression left join fetch progression.site17 left join fetch progression.sessionInstance left join fetch progression.student where progression.id =:id"
    )
    Optional<Progression> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select new com.wiam.lms.service.dto.FollowupAvgDTO(progression.tilawaType,AVG(progression.hifdScore)) from Progression progression where progression.student.id=:id and progression.sessionInstance.sessionDate BETWEEN :startDate AND :endDate group by progression.tilawaType"
    )
    List<FollowupAvgDTO> followupStudentScores(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query(
        "select new com.wiam.lms.service.dto.FollowupListDTO(progression.tilawaType,GROUP_CONCAT(progression.sessionInstance.startTime,'#',progression.hifdScore)) from Progression progression where progression.student.id=:id and progression.sessionInstance.sessionDate BETWEEN :startDate AND :endDate group by progression.tilawaType"
    )
    List<FollowupListDTO> followupStudentList(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
