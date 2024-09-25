package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Ayahs;
import com.wiam.lms.domain.Surahs;
import com.wiam.lms.repository.AyahsRepository;
import com.wiam.lms.repository.SurahsRepository;
import com.wiam.lms.repository.custom.AyahsCustomRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/quran")
public class QuranController {

    private static final Logger LOG = LoggerFactory.getLogger(ProgressionResource.class);

    private final AyahsCustomRepository ayahsRepository;
    private final SurahsRepository surahsRepository;

    public QuranController(SurahsRepository surahsRepository, AyahsCustomRepository ayahsRepository) {
        this.surahsRepository = surahsRepository;
        this.ayahsRepository = ayahsRepository;
    }

    /**
     * {@code SEARCH  /ayahs/_search?query=:query} : search for ayahs
     * to the query.
     *
     * @param query the query of the ayahs search.
     * @return the result of the search.
     */
    @GetMapping(value = "/ayahs/_search", produces = "application/json;charset=utf-8")
    public List<Ayahs> searchAyahs(@RequestParam String query) {
        LOG.debug("REST request to search searchAyahs for params {}", query);
        //      String query2 = "'\"" + query + "'\"";  // if we use MATCH(ayah.textdesc_normalized) AGAINST
        query = "%" + query + "%"; // if we use like
        return ayahsRepository.searchAyahs(query);
    }

    /**
     * {@code GET  /ayahs/:id} : get all ayahs of surah "id".
     *
     * @param id the id of the surah.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ayahs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ayahs/{id}")
    public List<Ayahs> getAyahs(@PathVariable Integer id) {
        LOG.debug("REST request to get Ayahs : {}", id);
        return ayahsRepository.getAllBySurah(id);
    }

    /**
     * {@code GET  /surahs} : get all the surahs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surahs in body.
     */
    @GetMapping("/surahs")
    public List<Surahs> getAllSurahs() {
        LOG.debug("REST request to get all Surahs");
        return surahsRepository.findAll();
    }

    @GetMapping("/mushaf")
    public ResponseEntity<List<Ayahs>> getAllAyahs(Pageable pageable, @RequestParam(required = false) String filter) {
        Page<Ayahs> page = this.findAll(pageable, filter);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    public Page<Ayahs> findAll(Pageable pageable, String filter) {
        if (filter == null || filter.isEmpty()) {
            return ayahsRepository.findAll(pageable);
        } else {
            return ayahsRepository.findByFilter(filter, pageable);
        }
    }

    @GetMapping("/ayahs/page/{pageNumber}")
    public ResponseEntity<List<Ayahs>> getAyahsByPage(@PathVariable int pageNumber) {
        List<Ayahs> ayahs = this.findByPage(pageNumber);
        return ResponseEntity.ok().body(ayahs);
    }

    public List<Ayahs> findByPage(int pageNumber) {
        return ayahsRepository.findByPage(pageNumber);
    }
}
