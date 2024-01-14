package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.repository.SessionInstanceRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SessionInstance} entity.
 */
public interface SessionInstanceSearchRepository
    extends ElasticsearchRepository<SessionInstance, Long>, SessionInstanceSearchRepositoryInternal {}

interface SessionInstanceSearchRepositoryInternal {
    Stream<SessionInstance> search(String query);

    Stream<SessionInstance> search(Query query);

    @Async
    void index(SessionInstance entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SessionInstanceSearchRepositoryInternalImpl implements SessionInstanceSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SessionInstanceRepository repository;

    SessionInstanceSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SessionInstanceRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SessionInstance> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SessionInstance> search(Query query) {
        return elasticsearchTemplate.search(query, SessionInstance.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SessionInstance entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SessionInstance.class);
    }
}
