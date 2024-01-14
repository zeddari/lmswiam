package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.Surahs;
import com.wiam.lms.repository.SurahsRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Surahs} entity.
 */
public interface SurahsSearchRepository extends ElasticsearchRepository<Surahs, Integer>, SurahsSearchRepositoryInternal {}

interface SurahsSearchRepositoryInternal {
    Stream<Surahs> search(String query);

    Stream<Surahs> search(Query query);

    @Async
    void index(Surahs entity);

    @Async
    void deleteFromIndexById(Integer id);
}

class SurahsSearchRepositoryInternalImpl implements SurahsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SurahsRepository repository;

    SurahsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SurahsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Surahs> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Surahs> search(Query query) {
        return elasticsearchTemplate.search(query, Surahs.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Surahs entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Integer id) {
        elasticsearchTemplate.delete(String.valueOf(id), Surahs.class);
    }
}
