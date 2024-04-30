package com.wiam.lms.repository;

import com.wiam.lms.domain.Comments;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CommentsRepositoryWithBagRelationshipsImpl implements CommentsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Comments> fetchBagRelationships(Optional<Comments> comments) {
        return comments.map(this::fetchSessions8s);
    }

    @Override
    public Page<Comments> fetchBagRelationships(Page<Comments> comments) {
        return new PageImpl<>(fetchBagRelationships(comments.getContent()), comments.getPageable(), comments.getTotalElements());
    }

    @Override
    public List<Comments> fetchBagRelationships(List<Comments> comments) {
        return Optional.of(comments).map(this::fetchSessions8s).orElse(Collections.emptyList());
    }

    Comments fetchSessions8s(Comments result) {
        return entityManager
            .createQuery(
                "select comments from Comments comments left join fetch comments.sessions8s where comments.id = :id",
                Comments.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Comments> fetchSessions8s(List<Comments> comments) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, comments.size()).forEach(index -> order.put(comments.get(index).getId(), index));
        List<Comments> result = entityManager
            .createQuery(
                "select comments from Comments comments left join fetch comments.sessions8s where comments in :comments",
                Comments.class
            )
            .setParameter("comments", comments)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
