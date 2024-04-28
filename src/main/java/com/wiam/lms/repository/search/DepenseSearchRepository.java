package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.Depense;
import com.wiam.lms.repository.DepenseRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Depense} entity.
 */
public interface DepenseSearchRepository extends ElasticsearchRepository<Depense, Long>, DepenseSearchRepositoryInternal {}

interface DepenseSearchRepositoryInternal {
    Stream<Depense> search(String query);

    Stream<Depense> search(Query query);

    @Async
    void index(Depense entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DepenseSearchRepositoryInternalImpl implements DepenseSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DepenseRepository repository;

    DepenseSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DepenseRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Depense> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Depense> search(Query query) {
        return elasticsearchTemplate.search(query, Depense.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Depense entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Depense.class);
    }
}
