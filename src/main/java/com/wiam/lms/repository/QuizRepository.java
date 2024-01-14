package com.wiam.lms.repository;

import com.wiam.lms.domain.Quiz;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quiz entity.
 *
 * When extending this class, extend QuizRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface QuizRepository extends QuizRepositoryWithBagRelationships, JpaRepository<Quiz, Long> {
    default Optional<Quiz> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Quiz> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Quiz> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(value = "select quiz from Quiz quiz left join fetch quiz.topic1", countQuery = "select count(quiz) from Quiz quiz")
    Page<Quiz> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quiz from Quiz quiz left join fetch quiz.topic1")
    List<Quiz> findAllWithToOneRelationships();

    @Query("select quiz from Quiz quiz left join fetch quiz.topic1 where quiz.id =:id")
    Optional<Quiz> findOneWithToOneRelationships(@Param("id") Long id);
}
