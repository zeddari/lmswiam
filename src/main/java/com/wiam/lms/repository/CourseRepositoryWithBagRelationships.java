package com.wiam.lms.repository;

import com.wiam.lms.domain.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CourseRepositoryWithBagRelationships {
    Optional<Course> fetchBagRelationships(Optional<Course> course);

    List<Course> fetchBagRelationships(List<Course> courses);

    Page<Course> fetchBagRelationships(Page<Course> courses);
}
