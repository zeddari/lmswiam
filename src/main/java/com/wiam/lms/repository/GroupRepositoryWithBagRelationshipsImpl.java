package com.wiam.lms.repository;

import com.wiam.lms.domain.Group;
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
public class GroupRepositoryWithBagRelationshipsImpl implements GroupRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Group> fetchBagRelationships(Optional<Group> group) {
        return group.map(this::fetchElements);
    }

    @Override
    public Page<Group> fetchBagRelationships(Page<Group> groups) {
        return new PageImpl<>(fetchBagRelationships(groups.getContent()), groups.getPageable(), groups.getTotalElements());
    }

    @Override
    public List<Group> fetchBagRelationships(List<Group> groups) {
        return Optional.of(groups).map(this::fetchElements).orElse(Collections.emptyList());
    }

    Group fetchElements(Group result) {
        return entityManager
            .createQuery("select group from Group group left join fetch group.elements where group.id = :id", Group.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Group> fetchElements(List<Group> groups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, groups.size()).forEach(index -> order.put(groups.get(index).getId(), index));
        List<Group> result = entityManager
            .createQuery("select group from Group group left join fetch group.elements where group in :groups", Group.class)
            .setParameter("groups", groups)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
