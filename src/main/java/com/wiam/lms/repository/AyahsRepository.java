package com.wiam.lms.repository;

import com.wiam.lms.domain.Ayahs;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Ayahs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AyahsRepository extends JpaRepository<Ayahs, Integer> {
    @Query(value = "select ayah.* from Ayahs ayah where MATCH(ayah.textdesc) AGAINST(:part IN BOOLEAN MODE)", nativeQuery = true)
    List<Ayahs> searchAyahs(@Param("part") String part);

    @Query("select ayah from Ayahs ayah where ayah.surahId = :id")
    List<Ayahs> getAllBySurah(@Param("id") Integer id);
}
