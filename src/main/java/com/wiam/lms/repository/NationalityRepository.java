package com.wiam.lms.repository;

import com.wiam.lms.domain.Nationality;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Nationality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Long> {}
