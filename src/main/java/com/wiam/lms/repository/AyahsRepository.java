package com.wiam.lms.repository;

import com.wiam.lms.domain.Ayahs;
import com.wiam.lms.domain.Progression;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ayahs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AyahsRepository extends JpaRepository<Ayahs, Integer> {
    @Query("select ayahs from Ayahs ayahs where ayahs.numberInSurah=:ayahNumber and ayahs.surahId=:surahNumber")
    Optional<Ayahs> findByAyaSurah(@Param("ayahNumber") Integer ayahNumber, @Param("surahNumber") Integer surahNumber);

    @Query("select ayahs from Ayahs ayahs where ayahs.surahId=:surahId")
    List<Ayahs> findAllBySurahsId(@Param("surahId") Integer surahId);
}
