package com.wiam.lms.repository;

import com.wiam.lms.domain.QuizResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizResult entity.
 */
@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    default Optional<QuizResult> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QuizResult> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QuizResult> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quizResult from QuizResult quizResult left join fetch quizResult.site8 left join fetch quizResult.userCustom2",
        countQuery = "select count(quizResult) from QuizResult quizResult"
    )
    Page<QuizResult> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quizResult from QuizResult quizResult left join fetch quizResult.site8 left join fetch quizResult.userCustom2")
    List<QuizResult> findAllWithToOneRelationships();

    @Query(
        "select quizResult from QuizResult quizResult left join fetch quizResult.site8 left join fetch quizResult.userCustom2 where quizResult.id =:id"
    )
    Optional<QuizResult> findOneWithToOneRelationships(@Param("id") Long id);
}
