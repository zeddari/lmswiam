package com.wiam.lms.repository;

import com.wiam.lms.domain.SessionCourses;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SessionCourses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionCoursesRepository extends JpaRepository<SessionCourses, Long> {}
