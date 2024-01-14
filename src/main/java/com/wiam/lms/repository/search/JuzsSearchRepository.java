package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.Juzs;
import com.wiam.lms.repository.JuzsRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Juzs} entity.
 */
public interface JuzsSearchRepository extends ElasticsearchRepository<Juzs, Integer>, JuzsSearchRepositoryInternal {}

interface JuzsSearchRepositoryInternal {
    Stream<Juzs> search(String query);

    Stream<Juzs> search(Query query);

    @Async
    void index(Juzs entity);

    @Async
    void deleteFromIndexById(Integer id);
}

class JuzsSearchRepositoryInternalImpl implements JuzsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final JuzsRepository repository;

    JuzsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, JuzsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Juzs> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Juzs> search(Query query) {
        return elasticsearchTemplate.search(query, Juzs.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Juzs entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Integer id) {
        elasticsearchTemplate.delete(String.valueOf(id), Juzs.class);
    }
}
