package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Ayahs;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(
        "SELECT a FROM Ayahs a WHERE " +
        "LOWER(a.suraNameEn) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
        "LOWER(a.suraNameAr) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
        "CAST(a.numberInSurah AS string) LIKE CONCAT('%', :filter, '%') OR " +
        "CAST(a.page AS string) LIKE CONCAT('%', :filter, '%')"
    )
    Page<Ayahs> findByFilter(@Param("filter") String filter, Pageable pageable);

    @Query("SELECT a FROM Ayahs a WHERE a.page = :pageNumber ORDER BY a.numberInSurah ASC")
    List<Ayahs> findByPage(@Param("pageNumber") int pageNumber);
}
