package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.SessionCourses;
import com.wiam.lms.repository.SessionCoursesRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SessionCourses} entity.
 */
public interface SessionCoursesSearchRepository
    extends ElasticsearchRepository<SessionCourses, Long>, SessionCoursesSearchRepositoryInternal {}

interface SessionCoursesSearchRepositoryInternal {
    Stream<SessionCourses> search(String query);

    Stream<SessionCourses> search(Query query);

    @Async
    void index(SessionCourses entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SessionCoursesSearchRepositoryInternalImpl implements SessionCoursesSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SessionCoursesRepository repository;

    SessionCoursesSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SessionCoursesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SessionCourses> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SessionCourses> search(Query query) {
        return elasticsearchTemplate.search(query, SessionCourses.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SessionCourses entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SessionCourses.class);
    }
}
