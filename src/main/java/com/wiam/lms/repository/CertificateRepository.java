package com.wiam.lms.repository;

import com.wiam.lms.domain.Certificate;
import com.wiam.lms.domain.UserCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Certificate entity.
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    default Optional<Certificate> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Certificate> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Certificate> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select certificate from Certificate certificate left join fetch certificate.site19 left join fetch certificate.userCustom6 left join fetch certificate.comitte left join fetch certificate.topic4",
        countQuery = "select count(certificate) from Certificate certificate"
    )
    Page<Certificate> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select certificate from Certificate certificate left join fetch certificate.site19 left join fetch certificate.userCustom6 left join fetch certificate.comitte left join fetch certificate.topic4"
    )
    List<Certificate> findAllWithToOneRelationships();

    @Query(
        "select certificate from Certificate certificate left join fetch certificate.site19 left join fetch certificate.userCustom6 left join fetch certificate.comitte left join fetch certificate.topic4 where certificate.id =:id"
    )
    Optional<Certificate> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select c from Certificate c left join fetch c.site19 left join fetch c.userCustom6 left join fetch c.comitte left join fetch c.topic4 where c.userCustom6 = :userCustom"
    )
    Page<Certificate> findAllByUserCustom(Pageable pageable, @Param("userCustom") UserCustom userCustom);
}
