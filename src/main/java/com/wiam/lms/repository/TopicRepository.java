package com.wiam.lms.repository;

import com.wiam.lms.domain.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Topic entity.
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    default Optional<Topic> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Topic> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Topic> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select topic from Topic topic left join fetch topic.topic2", countQuery = "select count(topic) from Topic topic")
    Page<Topic> findAllWithToOneRelationships(Pageable pageable);

    @Query("select topic from Topic topic left join fetch topic.topic2")
    List<Topic> findAllWithToOneRelationships();

    @Query("select topic from Topic topic left join fetch topic.topic2 where topic.id =:id")
    Optional<Topic> findOneWithToOneRelationships(@Param("id") Long id);
}
