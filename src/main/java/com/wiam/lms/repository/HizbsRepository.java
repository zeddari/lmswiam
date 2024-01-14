package com.wiam.lms.repository;

import com.wiam.lms.domain.Hizbs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hizbs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HizbsRepository extends JpaRepository<Hizbs, Integer> {}
