package com.wiam.lms.repository;

import com.wiam.lms.domain.Surahs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Surahs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurahsRepository extends JpaRepository<Surahs, Integer> {}
