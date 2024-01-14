package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.Ayahs;
import com.wiam.lms.repository.AyahsRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Ayahs} entity.
 */
public interface AyahsSearchRepository extends ElasticsearchRepository<Ayahs, Integer>, AyahsSearchRepositoryInternal {}

interface AyahsSearchRepositoryInternal {
    Stream<Ayahs> search(String query);

    Stream<Ayahs> search(Query query);

    @Async
    void index(Ayahs entity);

    @Async
    void deleteFromIndexById(Integer id);
}

class AyahsSearchRepositoryInternalImpl implements AyahsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AyahsRepository repository;

    AyahsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AyahsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Ayahs> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Ayahs> search(Query query) {
        return elasticsearchTemplate.search(query, Ayahs.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Ayahs entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Integer id) {
        elasticsearchTemplate.delete(String.valueOf(id), Ayahs.class);
    }
}
