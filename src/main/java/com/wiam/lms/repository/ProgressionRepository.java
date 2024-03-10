package com.wiam.lms.repository;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Progression entity.
 */
@Repository
public interface ProgressionRepository extends JpaRepository<Progression, Long> {
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

    @Query("select progression from Progression progression where progression.student.id=:id")
    List<Progression> findAllByStudent(@Param("id") Long id);

    @Query(
        "select progression from Progression progression where progression.examType <> ExamType.NONE and progression.isForAttendance = false and progression.student.id=:id"
    )
    List<Progression> findExams(@Param("id") Long id);

    @Query(
        "select progression from Progression progression where progression.sessionInstance.id=:id1 and progression.student.id=:id2 and progression.isForAttendance=true"
    )
    Progression isAlreadyExists(@Param("id1") Long id1, @Param("id2") Long id2);

    @Query("select progression from Progression progression left join fetch progression.student where progression.sessionInstance.id=:id")
    List<Progression> findAllBySessionInstance(@Param("id") Long id);
}
