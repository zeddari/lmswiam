package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Ayahs;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ayahs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AyahsCustomRepository extends JpaRepository<Ayahs, Integer> {
    //    @Query(value = "select ayah.* from Ayahs ayah where MATCH(ayah.textdesc_normalized) AGAINST(:part IN BOOLEAN MODE)", nativeQuery = true)
    @Query(
        value = "select ayah.* from Ayahs ayah where ayah.textdesc_normalized LIKE remove_accents(:part) ORDER BY number ASC",
        nativeQuery = true
    )
    List<Ayahs> searchAyahs(@Param("part") String part);

    @Query("select ayah from Ayahs ayah where ayah.surahId = :id")
    List<Ayahs> getAllBySurah(@Param("id") Integer id);
}
