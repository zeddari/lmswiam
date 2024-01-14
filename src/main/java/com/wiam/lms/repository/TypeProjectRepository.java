package com.wiam.lms.repository;

import com.wiam.lms.domain.TypeProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeProjectRepository extends JpaRepository<TypeProject, Long> {}
