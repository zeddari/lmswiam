package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesData;
import com.wiam.lms.domain.enumeration.SessionMode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Session entity.
 */
@Repository
public interface MaqraaRepository extends JpaRepository<Session, Long> {
    List<Session> findBySessionMode(SessionMode sessionMode);
}
