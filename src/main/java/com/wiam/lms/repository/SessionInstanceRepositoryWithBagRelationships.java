package com.wiam.lms.repository;

import com.wiam.lms.domain.SessionInstance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SessionInstanceRepositoryWithBagRelationships {
    Optional<SessionInstance> fetchBagRelationships(Optional<SessionInstance> sessionInstance);

    List<SessionInstance> fetchBagRelationships(List<SessionInstance> sessionInstances);

    Page<SessionInstance> fetchBagRelationships(Page<SessionInstance> sessionInstances);
}
