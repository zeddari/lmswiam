package com.wiam.lms.repository;

import com.wiam.lms.domain.Ayahs;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

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
