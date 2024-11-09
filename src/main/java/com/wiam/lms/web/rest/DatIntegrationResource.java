package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.*;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.SessionInstanceRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.UserCustomSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * REST controller for managing {@link UserCustom}.
 */
@RestController
@RequestMapping("/api/data/integration")
@Transactional
public class DatIntegrationResource {

    private final Logger log = LoggerFactory.getLogger(DatIntegrationResource.class);

    private static final String ENTITY_NAME = "userCustom";

    private static final Attendance DEFAULT_ATTENDANCE = Attendance.PRESENT;
    private static final Attendance UPDATED_ATTENDANCE = Attendance.ABSENT;

    private static final String DEFAULT_JUSTIF_REF = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIF_REF = "BBBBBBBBBB";

    private static final Boolean DEFAULT_LATE_ARRIVAL = false;
    private static final Boolean UPDATED_LATE_ARRIVAL = true;

    private static final Boolean DEFAULT_EARLY_DEPARTURE = false;
    private static final Boolean UPDATED_EARLY_DEPARTURE = true;

    private static final ProgressionMode DEFAULT_PROGRESSION_MODE = ProgressionMode.SLOW;
    private static final ProgressionMode UPDATED_PROGRESSION_MODE = ProgressionMode.INTERMEDIATE;

    private static final ExamType DEFAULT_EXAM_TYPE = ExamType.NONE;
    private static final ExamType UPDATED_EXAM_TYPE = ExamType.OPTIONAL;

    private static final Riwayats DEFAULT_RIWAYA = Riwayats.WARSHS_NARRATION_ON_THE_AUTHORITY_OF_NAFI_VIA_AL_SHATIBIYYAH;
    private static final Riwayats UPDATED_RIWAYA = Riwayats.QALOUNS_NARRATION_ON_THE_AUTHORITY_OF_NAFI_ON_THE_AUTHORITY_OF_AL_SHATIBIYYAH;

    private static final Sourate DEFAULT_FROM_SOURATE = Sourate.FATIHA;
    private static final Sourate UPDATED_FROM_SOURATE = Sourate.BA9ARA;

    private static final Sourate DEFAULT_TO_SOURATE = Sourate.FATIHA;
    private static final Sourate UPDATED_TO_SOURATE = Sourate.BA9ARA;

    private static final Integer DEFAULT_FROM_AYA_NUM = 1;
    private static final Integer UPDATED_FROM_AYA_NUM = 2;

    private static final Integer DEFAULT_TO_AYA_NUM = 1;
    private static final Integer UPDATED_TO_AYA_NUM = 2;

    private static final String DEFAULT_FROM_AYA_VERSET = " إن الذين كفروا ";
    private static final String UPDATED_FROM_AYA_VERSET = "BBBBBBBBBB";

    private static final String DEFAULT_TO_AYA_VERSET = "فَمَا رَبِحَت تِّجَارَتُهُمْ وَمَا كَانُوا مُهْتَدِينَ";
    private static final String UPDATED_TO_AYA_VERSET = "BBBBBBBBBB";

    private static final Tilawa DEFAULT_TILAWA_TYPE = Tilawa.HIFD;
    private static final Tilawa UPDATED_TILAWA_TYPE = Tilawa.MORAJA3A;

    private static final Boolean DEFAULT_TASK_DONE = false;
    private static final Boolean UPDATED_TASK_DONE = true;

    private static final Integer DEFAULT_TAJWEED_SCORE = 1;
    private static final Integer UPDATED_TAJWEED_SCORE = 2;

    private static final Integer DEFAULT_HIFD_SCORE = 1;
    private static final Integer UPDATED_HIFD_SCORE = 2;

    private static final Integer DEFAULT_ADAE_SCORE = 1;
    private static final Integer UPDATED_ADAE_SCORE = 2;

    private static final String DEFAULT_OBSERVATION = "طالب متميز";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/progressions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/progressions/_search";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCustomRepository userCustomRepository;

    private final UserCustomSearchRepository userCustomSearchRepository;

    @Autowired
    private ProgressionRepository progressionRepository;

    @Autowired
    private SessionInstanceRepository sessionInstanceRepository;

    public DatIntegrationResource(UserCustomRepository userCustomRepository, UserCustomSearchRepository userCustomSearchRepository) {
        this.userCustomRepository = userCustomRepository;
        this.userCustomSearchRepository = userCustomSearchRepository;
    }

    /**
     * {@code POST  /user-customs} : Create a new userCustom.
     *
     * @param userCustom the userCustom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userCustom, or with status {@code 400 (Bad Request)} if the userCustom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/progression")
    public void createProgression() throws URISyntaxException {
        List<SessionInstance> sessionInstances = sessionInstanceRepository.findAll();
        AtomicInteger index = new AtomicInteger();
        sessionInstances.forEach(sessionInstance -> {
            index.set(0);
            List<Set<UserCustom>> userCustomsSet = sessionInstance.getSession1().getGroups().stream().map(group -> group.getElements()).toList();
            userCustomsSet.get(0).forEach(userCustom -> {
                if (userCustomsSet.get(0).size() > 0  && userCustomsSet.get(0).size() > index.get()) {
                    Progression progression = new Progression()
                        .attendance(DEFAULT_ATTENDANCE)
                        .justifRef(DEFAULT_JUSTIF_REF)
                        .lateArrival(DEFAULT_LATE_ARRIVAL)
                        .earlyDeparture(DEFAULT_EARLY_DEPARTURE)
                        .progressionMode(DEFAULT_PROGRESSION_MODE)
                        .examType(DEFAULT_EXAM_TYPE)
                        .riwaya(DEFAULT_RIWAYA)
                        .fromAyaNum(DEFAULT_FROM_AYA_NUM)
                        .toAyaNum(DEFAULT_TO_AYA_NUM)
                        .fromAyaVerset(DEFAULT_FROM_AYA_VERSET)
                        .toAyaVerset(DEFAULT_TO_AYA_VERSET)
                        .tilawaType(DEFAULT_TILAWA_TYPE)
                        .taskDone(DEFAULT_TASK_DONE)
                        .tajweedScore(DEFAULT_TAJWEED_SCORE)
                        .hifdScore(DEFAULT_HIFD_SCORE)
                        .adaeScore(DEFAULT_ADAE_SCORE)
                        .observation(DEFAULT_OBSERVATION)
                        .sessionInstance(sessionInstance)
                        .site17(sessionInstance.getSite16())
                        .student(userCustomsSet.get(0).stream().toList().get(index.get()))
                        ;
                    progression.setIsForAttendance(false);
                    progressionRepository.save(progression);

                    progression.setTilawaType(Tilawa.TILAWA);
                    progressionRepository.save(progression);

                    progression.setTilawaType(Tilawa.MORAJA3A);
                    progressionRepository.save(progression);

                    progression.setIsForAttendance(true);
                    progressionRepository.save(progression);
                }
                index.getAndIncrement();
            });


        });
    }


}
