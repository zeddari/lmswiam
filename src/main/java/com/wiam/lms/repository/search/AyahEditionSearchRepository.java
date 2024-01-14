package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.AyahEdition;
import com.wiam.lms.repository.AyahEditionRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link AyahEdition} entity.
 */
public interface AyahEditionSearchRepository extends ElasticsearchRepository<AyahEdition, Integer>, AyahEditionSearchRepositoryInternal {}

interface AyahEditionSearchRepositoryInternal {
    Stream<AyahEdition> search(String query);

    Stream<AyahEdition> search(Query query);

    @Async
    void index(AyahEdition entity);

    @Async
    void deleteFromIndexById(Integer id);
}

class AyahEditionSearchRepositoryInternalImpl implements AyahEditionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AyahEditionRepository repository;

    AyahEditionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AyahEditionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<AyahEdition> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<AyahEdition> search(Query query) {
        return elasticsearchTemplate.search(query, AyahEdition.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(AyahEdition entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Integer id) {
        elasticsearchTemplate.delete(String.valueOf(id), AyahEdition.class);
    }
}
