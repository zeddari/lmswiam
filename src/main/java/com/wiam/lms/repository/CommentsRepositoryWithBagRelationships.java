package com.wiam.lms.repository;

import com.wiam.lms.domain.Comments;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CommentsRepositoryWithBagRelationships {
    Optional<Comments> fetchBagRelationships(Optional<Comments> comments);

    List<Comments> fetchBagRelationships(List<Comments> comments);

    Page<Comments> fetchBagRelationships(Page<Comments> comments);
}
