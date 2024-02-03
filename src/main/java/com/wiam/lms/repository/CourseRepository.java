package com.wiam.lms.repository;

import com.wiam.lms.domain.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Course entity.
 *
 * When extending this class, extend CourseRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CourseRepository extends CourseRepositoryWithBagRelationships, JpaRepository<Course, Long> {
    default Optional<Course> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Course> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Course> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select course from Course course left join fetch course.site1 left join fetch course.topic3",
        countQuery = "select count(course) from Course course"
    )
    Page<Course> findAllWithToOneRelationships(Pageable pageable);

    @Query("select course from Course course left join fetch course.site1 left join fetch course.topic3")
    List<Course> findAllWithToOneRelationships();

    @Query("select course from Course course left join fetch course.site1 left join fetch course.topic3 where course.id =:id")
    Optional<Course> findOneWithToOneRelationships(@Param("id") Long id);
}
