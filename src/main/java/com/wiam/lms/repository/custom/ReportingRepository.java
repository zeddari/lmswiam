package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportDetailInterface;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Progression entity.
 */
@Repository
public interface ReportingRepository extends JpaRepository<Progression, Long> {
    @Query(
        value = "SELECT " +
        "DATE_FORMAT(:startDate, '%Y-%m-%d') AS startDate, " +
        "DATE_FORMAT(:endDate, '%Y-%m-%d') AS endDate, " +
        "st.name_ar AS schoolName, " +
        "st.name_lat AS schoolNameLat, " +
        "ss.title AS sessionName, " +
        "CONCAT(uc.first_name, ' ', uc.last_name) AS teacherName, " +
        "ss.targeted_gender AS groupName, " +
        "ss.targeted_gender AS targetedGender, " +
        "COUNT(DISTINCT pr.student_id) AS nbStudentTotal, " +
        // Total number of students with attendance 'PRESENT'
        "(SELECT COUNT(DISTINCT pr1.student_id) " +
        " FROM progression pr1 " +
        " INNER JOIN session_instance si1 ON si1.id = pr1.session_instance_id " +
        " WHERE si1.attendance = 'PRESENT' " +
        " AND si1.session_date BETWEEN :startDate AND :endDate " +
        " AND si1.title = si.title) AS nbStudentTotal2, " +
        // Number of school days within the period
        "(4 * (DATEDIFF(:endDate, :startDate) / 7) + " +
        " MID('0123444401233334012222340111123400012345001234550', " +
        " 7 * WEEKDAY(:startDate) + WEEKDAY(:endDate) + 1, 1)) AS nbDaysSchoolingTotal, " +
        "0 AS optionalLessons, " +
        "0 AS tajweedLessons, " +
        // Hifd Juz'a progression total
        "(SELECT (MAX(ay.hizb_id) - MIN(ay.hizb_id)) / 2 " +
        " FROM progression pr2 " +
        " INNER JOIN ayahs ay ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num " +
        " WHERE pr2.tilawa_type = 'HIFD') AS nbAjzaeHifdTotal, " +
        // Student name
        "(SELECT CONCAT(uc1.first_name, ' ', uc1.last_name) " +
        " FROM user_custom uc1 " +
        " WHERE uc1.id = pr.student_id) AS studentName, " +
        // Hifd Juz'a progression for each student
        "(SELECT (MAX(ay.hizb_id) - MIN(ay.hizb_id)) / 2 " +
        " FROM progression pr2 " +
        " INNER JOIN ayahs ay ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num " +
        " WHERE pr2.tilawa_type = 'HIFD' AND pr2.student_id = pr.student_id) AS nbHifdAjzaeStudent, " +
        "COUNT(pr.hifd_score) AS hifzScore, " +
        // Count of student school days
        "(SELECT COUNT(DISTINCT pr1.student_id) " +
        " FROM progression pr1 " +
        " INNER JOIN session_instance si1 ON si1.id = pr1.session_instance_id " +
        " WHERE si1.attendance = 'PRESENT' " +
        " AND si1.session_date BETWEEN :startDate AND :endDate " +
        " AND pr1.student_id = pr.student_id) AS nbDaysSchoolingStudent, " +
        // Attendance percentage
        "(SELECT COUNT(DISTINCT pr1.student_id) " +
        " FROM progression pr1 " +
        " INNER JOIN session_instance si1 ON si1.id = pr1.session_instance_id " +
        " WHERE si1.attendance = 'PRESENT' " +
        " AND si1.session_date BETWEEN :startDate AND :endDate " +
        " AND pr1.student_id = pr.student_id) * 100 / " +
        "(SELECT COUNT(DISTINCT si1.professor_id) " +
        " FROM progression pr1 " +
        " INNER JOIN session_instance si1 ON si1.id = pr1.session_instance_id " +
        " WHERE si1.attendance = 'PRESENT' " +
        " AND si1.session_date BETWEEN :startDate AND :endDate " +
        " AND si1.title = si.title) AS percentDaysSchoolingTotal " +
        "FROM progression pr " +
        "INNER JOIN session_instance si ON si.id = pr.session_instance_id " +
        "INNER JOIN session ss ON ss.id = si.session1_id " +
        "INNER JOIN site st ON st.id = ss.site14_id " +
        "INNER JOIN user_custom uc ON uc.id = si.professor_id " +
        "WHERE pr.createdAt BETWEEN :startDate AND :endDate " +
        "AND ss.id = :sessionId " +
        "GROUP BY st.name_ar, st.name_lat, uc.first_name, uc.last_name, ss.title, ss.targeted_gender, pr.student_id, si.title",
        nativeQuery = true
    )
    List<PeriodicReportDetailInterface> getNativePeriodicReportOrig(
        @Param("sessionId") Long id,
        @Param("startDate") Date start,
        @Param("endDate") Date end
    );

    @Query(
        value = "SELECT DATE_FORMAT(:startDate, '%Y-%m-%d') AS startDate, DATE_FORMAT(:endDate, '%Y-%m-%d') AS endDate, st.name_ar AS schoolName, st.name_lat AS schoolNameLat, ss.title AS sessionName, CONCAT(uc.first_name, ' ', uc.last_name) AS teacherName, ss.targeted_gender AS groupName, ss.targeted_gender AS targetedGender, COUNT(DISTINCT pr.student_id) AS nbStudentTotal, (SELECT COUNT(DISTINCT pr1.student_id)  FROM progression pr1  \tINNER JOIN session_instance si1 ON si1.id = pr1.session_instance_id      WHERE si1.attendance = 'PRESENT'  AND si1.session_date BETWEEN :startDate AND :endDate  AND si1.title = si.title) AS nbStudentTotal2, (4 * (DATEDIFF(:endDate, :startDate) / 7) +  MID('0123444401233334012222340111123400012345001234550',  7 * WEEKDAY(:startDate) + WEEKDAY(:endDate) + 1,  1)) AS nbDaysSchoolingTotal, 0 AS optionalLessons, 0 AS tajweedLessons,  (SELECT (MAX(ay.juz_id) - MIN(ay.juz_id)) FROM progression pr2   INNER JOIN ayahs ay ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num  WHERE pr2.tilawa_type = 'HIFD')  AS nbAjzaeHifdTotal, (SELECT (MAX(ay.juz_id) - MIN(ay.juz_id))  FROM progression pr2  INNER JOIN ayahs ay  ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num  WHERE pr2.tilawa_type = 'HIFD'  AND pr2.student_id = pr.student_id) AS nbHifdAjzaeStudent,  (SELECT (MAX(ay.page) - MIN(ay.page))  FROM progression pr2  INNER JOIN ayahs ay  ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num  WHERE pr2.tilawa_type = 'HIFD'  AND pr2.student_id = pr.student_id) AS nbPageHifz,   (SELECT (MAX(ay.juz_id) - MIN(ay.juz_id))  FROM progression pr2  INNER JOIN ayahs ay  ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num  WHERE pr2.tilawa_type = 'MORAJA3A'  AND pr2.student_id = pr.student_id) AS nbAjzaeRev,  (SELECT (MAX(ay.page) - MIN(ay.page))  FROM progression pr2  INNER JOIN ayahs ay  ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num  WHERE pr2.tilawa_type = 'MORAJA3A'  AND pr2.student_id = pr.student_id) AS nbPageRev,   (SELECT (MAX(ay.juz_id) - MIN(ay.juz_id))  FROM progression pr2  INNER JOIN ayahs ay  ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num  WHERE pr2.tilawa_type = 'TILAWA'  AND pr2.student_id = pr.student_id) AS nbAjzaeHomeExam,  (SELECT (MAX(ay.juz_id) - MIN(ay.juz_id))  FROM progression pr2  INNER JOIN ayahs ay  ON ay.number_in_surah BETWEEN from_aya_num AND to_aya_num  WHERE pr2.tilawa_type = 'TILAWA'  AND pr2.student_id = pr.student_id) AS nbPageHomeExam, COUNT(pr.hifd_score) AS hifzScore, COUNT(pr.adae_score) AS scoreRev, COUNT(pr.tajweed_score) AS scoreTilawa,  (SELECT COUNT(DISTINCT pr1.student_id)  FROM progression pr1  INNER JOIN session_instance si1  ON si1.id = pr1.session_instance_id AND si1.session_date BETWEEN :startDate AND :endDate  WHERE pr1.attendance = 'PRESENT' AND pr1.student_id = pr.student_id) AS nbDaysSchoolingStudent,  (  (select count(*) from progression where is_for_attendance = true and attendance = 'PRESENT')/(  (select count(*) from progression where is_for_attendance = true and attendance = 'PRESENT')+  (select count(*) from progression where is_for_attendance = true and attendance = 'ABSENT'))  )AS percentDaysSchoolingTotal, (SELECT CONCAT(uc1.first_name, ' ', uc1.last_name)  FROM user_custom uc1  WHERE uc1.id = pr.student_id)  AS studentName  FROM progression pr  INNER JOIN session_instance si ON si.id = pr.session_instance_id  INNER JOIN session ss ON ss.id = si.session1_id INNER JOIN site st ON st.id = ss.site14_id  INNER JOIN user_custom uc ON uc.id = si.professor_id WHERE si.session_date BETWEEN :startDate AND :endDate  AND ss.id = :sessionId  GROUP BY st.name_ar, st.name_lat, uc.first_name, uc.last_name, ss.title, ss.targeted_gender, pr.student_id, si.title ",
    nativeQuery = true
        )
    List<PeriodicReportDetailInterface> getNativePeriodicReport(
        @Param("sessionId") Long id,
        @Param("startDate") Date start,
        @Param("endDate") Date end
    );
}
