package com.wiam.lms.repository;

import com.wiam.lms.domain.StudentSponsoring;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentSponsoring entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentSponsoringRepository extends JpaRepository<StudentSponsoring, Long> {}
