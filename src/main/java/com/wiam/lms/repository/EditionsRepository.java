package com.wiam.lms.repository;

import com.wiam.lms.domain.Editions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Editions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EditionsRepository extends JpaRepository<Editions, Integer> {}
