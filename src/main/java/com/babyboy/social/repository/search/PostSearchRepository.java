package com.babyboy.social.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.babyboy.social.domain.Post;
import com.babyboy.social.dto.PostDto;
import com.babyboy.social.dto.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostSearchRepository extends ElasticsearchRepository<PostDto, Long>, PostSearchRepositoryInternal {}

interface PostSearchRepositoryInternal {
    Page<PostDto> search(String query, Pageable pageable);
}

class PostSearchRepositoryInternalImpl implements PostSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PostSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<PostDto> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        List<PostDto> totalRecords = elasticsearchTemplate
            .search(nativeSearchQuery, PostDto.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());
        nativeSearchQuery.setPageable(pageable);
        List<PostDto> posts = elasticsearchTemplate
            .search(nativeSearchQuery, PostDto.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(posts, pageable, totalRecords.size());
    }
}
