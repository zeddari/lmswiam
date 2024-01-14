package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.QuizResult;
import com.wiam.lms.repository.QuizResultRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link QuizResult} entity.
 */
public interface QuizResultSearchRepository extends ElasticsearchRepository<QuizResult, Long>, QuizResultSearchRepositoryInternal {}

interface QuizResultSearchRepositoryInternal {
    Stream<QuizResult> search(String query);

    Stream<QuizResult> search(Query query);

    @Async
    void index(QuizResult entity);

    @Async
    void deleteFromIndexById(Long id);
}

class QuizResultSearchRepositoryInternalImpl implements QuizResultSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final QuizResultRepository repository;

    QuizResultSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, QuizResultRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<QuizResult> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<QuizResult> search(Query query) {
        return elasticsearchTemplate.search(query, QuizResult.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(QuizResult entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), QuizResult.class);
    }
}
