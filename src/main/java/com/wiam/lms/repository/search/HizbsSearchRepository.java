package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.Hizbs;
import com.wiam.lms.repository.HizbsRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Hizbs} entity.
 */
public interface HizbsSearchRepository extends ElasticsearchRepository<Hizbs, Integer>, HizbsSearchRepositoryInternal {}

interface HizbsSearchRepositoryInternal {
    Stream<Hizbs> search(String query);

    Stream<Hizbs> search(Query query);

    @Async
    void index(Hizbs entity);

    @Async
    void deleteFromIndexById(Integer id);
}

class HizbsSearchRepositoryInternalImpl implements HizbsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final HizbsRepository repository;

    HizbsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, HizbsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Hizbs> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Hizbs> search(Query query) {
        return elasticsearchTemplate.search(query, Hizbs.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Hizbs entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Integer id) {
        elasticsearchTemplate.delete(String.valueOf(id), Hizbs.class);
    }
}
