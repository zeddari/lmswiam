package com.wiam.lms.repository;

import com.wiam.lms.domain.UserCustom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link UserCustom} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<UserCustom, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<UserCustom> findOneByActivationKey(String activationKey);
    List<UserCustom> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<UserCustom> findOneByResetKey(String resetKey);
    Optional<UserCustom> findOneByEmailIgnoreCase(String email);
    Optional<UserCustom> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    //@Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<UserCustom> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<UserCustom> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<UserCustom> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
