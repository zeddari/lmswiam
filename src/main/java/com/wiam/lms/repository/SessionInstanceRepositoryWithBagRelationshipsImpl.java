package com.wiam.lms.repository;

import com.wiam.lms.domain.SessionInstance;
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
public class SessionInstanceRepositoryWithBagRelationshipsImpl implements SessionInstanceRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SessionInstance> fetchBagRelationships(Optional<SessionInstance> sessionInstance) {
        return sessionInstance.map(this::fetchLinks);
    }

    @Override
    public Page<SessionInstance> fetchBagRelationships(Page<SessionInstance> sessionInstances) {
        return new PageImpl<>(
            fetchBagRelationships(sessionInstances.getContent()),
            sessionInstances.getPageable(),
            sessionInstances.getTotalElements()
        );
    }

    @Override
    public List<SessionInstance> fetchBagRelationships(List<SessionInstance> sessionInstances) {
        return Optional.of(sessionInstances).map(this::fetchLinks).orElse(Collections.emptyList());
    }

    SessionInstance fetchLinks(SessionInstance result) {
        return entityManager
            .createQuery(
                "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.links where sessionInstance.id = :id",
                SessionInstance.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<SessionInstance> fetchLinks(List<SessionInstance> sessionInstances) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, sessionInstances.size()).forEach(index -> order.put(sessionInstances.get(index).getId(), index));
        List<SessionInstance> result = entityManager
            .createQuery(
                "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.links where sessionInstance in :sessionInstances",
                SessionInstance.class
            )
            .setParameter("sessionInstances", sessionInstances)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
