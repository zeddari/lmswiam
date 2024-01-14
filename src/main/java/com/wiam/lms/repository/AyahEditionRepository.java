package com.wiam.lms.repository;

import com.wiam.lms.domain.AyahEdition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AyahEdition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AyahEditionRepository extends JpaRepository<AyahEdition, Integer> {}
