package com.wiam.lms.repository;

import com.wiam.lms.domain.Juzs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Juzs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JuzsRepository extends JpaRepository<Juzs, Integer> {}
