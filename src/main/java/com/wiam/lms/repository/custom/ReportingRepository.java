package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportPdfDetailInterface;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Progression entity.
 */
@Repository
public interface ReportingRepository extends JpaRepository<Progression, Long> {
    @Query(
        value = "select  \n" +
        "st.name_ar as schoolName,\n" +
        "st.name_lat as schoolNameLat,\n" +
        "uc.first_name as firstName,\n" +
        "si.title as title,\n" +
        "ss.targeted_gender as targetedGender,\n" +
        "count(pr.student_id) nbStudentTotal,\n" +
        "(select count(si1.professor_id) from progression pr1 inner join session_instance si1 on si1.id = pr1.session_instance_id\n" +
        "  and si1.attendance = 'PRESENT'\n" +
        "  where si1.session_date between '2024-02-01' and '2024-03-01' and si1.title = 'sessionAB'\n" +
        "  )  as nbStudentTotal2,\n" +
        " ( 4 * (DATEDIFF('2024-03-01', '2024-02-01') / 7) + MID('0123444401233334012222340111123400012345001234550', 7 * WEEKDAY('2024-02-01') + WEEKDAY('2024-03-01') + 1, 1)) as nbAttendanceTotal,\n" +
        "0 as optionalCourses,\n" +
        "0 as tajweedCourses,\n" +
        "(select (max(ay.hizb_id) - min(ay.hizb_id))/2  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'HIFD') nbHifdAjzaeToral,\n" +
        "(select (max(ay.hizb_id) - min(ay.hizb_id))/2  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'MORAJA3A') nbMorajaaAjzaeToral,\n" +
        "(select (max(ay.hizb_id) - min(ay.hizb_id))/2  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'TILAWA') nbTilawaAjzaeTotal,\n" +
        "(select uc1.first_name from user_custom uc1 where uc1.id = pr.student_id) as studentName,\n" +
        "(select (max(ay.hizb_id) - min(ay.hizb_id))/2  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'HIFD' and pr2.student_id = pr.student_id) nbHifdAjzaeStudent,\n" +
        "count(pr.hifd_score) studentHifdScore,\n" +
        "(select (max(ay.page) - min(ay.page))  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'HIFD') nbHifdPageTotal,\n" +
        "\n" +
        "(select (max(ay.hizb_id) - min(ay.hizb_id))/2  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'MORAJA3A' and pr2.student_id = pr.student_id) nbRevAjzaeStudent,\n" +
        "count(pr.tajweed_score) studentRevScore,\n" +
        "(select (max(ay.page) - min(ay.page))  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'MORAJA3A') nbRevPageTotal,\n" +
        "\n" +
        "(select (max(ay.hizb_id) - min(ay.hizb_id))/2  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'MORAJA3A' and pr2.student_id = pr.student_id) nbHomeAjzaeStudent,\n" +
        "count(pr.adae_score) studentHomeScore,\n" +
        "(select (max(ay.page) - min(ay.page))  from progression pr2 inner join ayahs ay on ay.number_in_surah >= from_aya_num and ay.number_in_surah <= to_aya_num where pr2.tilawa_type =  'MORAJA3A') nbHomePageTotal,\n" +
        "\n" +
        "(select count(pr1.student_id) from progression pr1 inner join session_instance si1 on si1.id = pr1.session_instance_id\n" +
        "  and si1.attendance = 'PRESENT'\n" +
        "  where si1.session_date between '2024-02-01' and '2024-03-01' and pr1.student_id = pr.student_id\n" +
        "  )  as nbAttendanceStudent,\n" +
        "(\n" +
        "(select count(pr1.student_id) from progression pr1 inner join session_instance si1 on si1.id = pr1.session_instance_id\n" +
        "  and si1.attendance = 'PRESENT'\n" +
        "  where si1.session_date between '2024-02-01' and '2024-03-01' and pr1.student_id = pr.student_id\n" +
        "  ) /\n" +
        "  (select count(si1.professor_id) from progression pr1 inner join session_instance si1 on si1.id = pr1.session_instance_id\n" +
        "  and si1.attendance = 'PRESENT'\n" +
        "  where si1.session_date between '2024-02-01' and '2024-03-01' and si1.title = 'sessionAB'\n" +
        "  )\n" +
        ") as studentAttendeePercent\n" +
        "from progression pr\n" +
        "inner join session_instance si on si.id = pr.session_instance_id\n" +
        "inner join session ss on ss.id = si.session1_id\n" +
        "inner join site st on st.id = ss.site14_id\n" +
        "inner join user_custom uc on uc.id = si.professor_id\n" +
        " where si.session_date between '2024-02-01' and '2024-03-01'\n" +
        " and si.title = 'sessionAB'\n" +
        " group by st.name_ar,\n" +
        "st.name_lat,\n" +
        "uc.first_name,\n" +
        "si.title,\n" +
        "ss.targeted_gender,\n" +
        "pr.student_id\n" +
        " ;",
        nativeQuery = true
    )
    List<PeriodicReportPdfDetailInterface> getNativePeriodicReport();
}
