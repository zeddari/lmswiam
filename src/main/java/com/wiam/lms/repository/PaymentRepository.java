package com.wiam.lms.repository;

import com.wiam.lms.domain.Payment;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.wiam.lms.domain.custom.projection.interfaces.FeesNonPaidData;
import com.wiam.lms.domain.enumeration.PaymentSide;
import com.wiam.lms.domain.enumeration.PaymentType;
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


    @Query(
        "select payment from Payment payment left join fetch payment.site9 left join fetch payment.enrolment left join fetch payment.sponsoring left join fetch payment.session left join fetch payment.currency where payment.site9.id =:siteId and payment.side =:paymentSide and payment.type =:paymentType and payment.paidAt between :startDate and :endDate"
    )
    List<Payment> findPaymentWithCriteria(
        @Param("siteId") Long siteId,
        @Param("paymentSide") PaymentSide paymentSide,
        @Param("paymentType") PaymentType paymentType,
        @Param("startDate") ZonedDateTime startDate,
        @Param("endDate") ZonedDateTime endDate
    );

    @Query(
        value = "select user_id firstName, user_id lastName, month(validity_end_time) lastPaidMonth, month(now())  - month(validity_end_time) missingPaidMonth from payment where user_id is not null and type  = 'MONTHLY_FEES' group by user_id,validity_end_time having month(now())  - month(validity_end_time) > 0 union select first_Name firstName, last_Name lastName, '0' lastPaidMonth, '12' missingPaidMonth from user_custom where role = 'STUDENT' and id not in (select user_id from payment where user_id is not null)",
        nativeQuery = true
    )
    List<FeesNonPaidData> findNonPaidFees();
}
