package com.wiam.lms.repository;

import com.wiam.lms.domain.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    default Optional<Payment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Payment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Payment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select payment from Payment payment left join fetch payment.site9 left join fetch payment.enrolment left join fetch payment.sponsoring left join fetch payment.session left join fetch payment.currency",
        countQuery = "select count(payment) from Payment payment"
    )
    Page<Payment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select payment from Payment payment left join fetch payment.site9 left join fetch payment.enrolment left join fetch payment.sponsoring left join fetch payment.session left join fetch payment.currency"
    )
    List<Payment> findAllWithToOneRelationships();

    @Query(
        "select payment from Payment payment left join fetch payment.site9 left join fetch payment.enrolment left join fetch payment.sponsoring left join fetch payment.session left join fetch payment.currency where payment.id =:id"
    )
    Optional<Payment> findOneWithToOneRelationships(@Param("id") Long id);
}
