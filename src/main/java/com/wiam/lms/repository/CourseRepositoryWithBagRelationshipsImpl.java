package com.wiam.lms.repository;

import com.wiam.lms.domain.Course;
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
public class CourseRepositoryWithBagRelationshipsImpl implements CourseRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Course> fetchBagRelationships(Optional<Course> course) {
        return course.map(this::fetchProfessors);
    }

    @Override
    public Page<Course> fetchBagRelationships(Page<Course> courses) {
        return new PageImpl<>(fetchBagRelationships(courses.getContent()), courses.getPageable(), courses.getTotalElements());
    }

    @Override
    public List<Course> fetchBagRelationships(List<Course> courses) {
        return Optional.of(courses).map(this::fetchProfessors).orElse(Collections.emptyList());
    }

    Course fetchProfessors(Course result) {
        return entityManager
            .createQuery("select course from Course course left join fetch course.professors where course.id = :id", Course.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Course> fetchProfessors(List<Course> courses) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, courses.size()).forEach(index -> order.put(courses.get(index).getId(), index));
        List<Course> result = entityManager
            .createQuery("select course from Course course left join fetch course.professors where course in :courses", Course.class)
            .setParameter("courses", courses)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
