package com.wiam.lms.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.repository.UserRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<UserCustom, Long>, UserSearchRepositoryInternal {}

interface UserSearchRepositoryInternal {
    Stream<UserCustom> search(String query);

    @Async
    @Transactional
    void index(UserCustom entity);

    @Async
    @Transactional
    void deleteFromIndex(UserCustom entity);
}

class UserSearchRepositoryInternalImpl implements UserSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UserRepository repository;

    UserSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UserRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<UserCustom> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return elasticsearchTemplate.search(nativeQuery, UserCustom.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(UserCustom entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndex(UserCustom entity) {
        elasticsearchTemplate.delete(entity);
    }
}
