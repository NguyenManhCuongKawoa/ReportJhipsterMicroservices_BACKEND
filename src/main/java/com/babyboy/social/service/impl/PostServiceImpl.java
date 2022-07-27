package com.babyboy.social.service.impl;

import com.babyboy.social.domain.Hashtag;
import com.babyboy.social.domain.Post;
import com.babyboy.social.domain.PostHashtag;
import com.babyboy.social.domain.PostImage;
import com.babyboy.social.dto.PostDto;
import com.babyboy.social.dto.response.PostResponse;
import com.babyboy.social.repository.HashtagRepository;
import com.babyboy.social.repository.PostHashtagRepository;
import com.babyboy.social.repository.PostImageRepository;
import com.babyboy.social.repository.PostRepository;
import com.babyboy.social.repository.search.PostSearchRepository;
import com.babyboy.social.service.PostService;
import com.github.dockerjava.api.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Service Implementation for managing {@link Post}.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    @Autowired
    private PostSearchRepository postSearchRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post save(Post post) {
        log.debug("Request to save Post : {}", post);
        return postRepository.save(post);
    }

    @Override
    public Post ChangeModePost(Long id, Integer mode) {
        Optional<Post> oldPost = postRepository.findById(id);
        Optional<PostDto> oldPostDto = postSearchRepository.findById(id);
        if (oldPost.isPresent() && oldPostDto.isPresent()) {
            Post post = oldPost.get();
            post.setMode(mode);
            PostDto postDto = oldPostDto.get();
            postDto.setMode(mode);
            postSearchRepository.save(postDto);
            return postRepository.save(post);
        }
        return null;
    }

    @Override
    public PostResponse savePost(PostDto postDto) {
        // Save Post
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setUserId(postDto.getUserId());
        post.setMode(postDto.getMode());
        post.setCreatedAt(postDto.getCreatedAt());
        post.setUpdatedAt(postDto.getUpdatedAt());
        post.setTotalEmotion(postDto.getTotalEmotion());
        post.setTotalShare(postDto.getTotalShare());
        postRepository.saveAndFlush(post);
        postDto.setId(post.getId());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());

        // Save Hashtags
        List<Hashtag> hashtags = null;
        if (postDto.getHashtagIds() != null) {
            hashtags = hashtagRepository.findAllById(postDto.getHashtagIds());
            List<PostHashtag> postHashtags = new ArrayList<>();
            for (Hashtag h : hashtags) {
                postHashtags.add(new PostHashtag(h.getId(), post.getId()));
            }
            postHashtagRepository.saveAll(postHashtags);
        }

        // Save Post Images
        if (postDto.getImages() != null) {
            List<PostImage> postImages = postDto
                .getImages()
                .stream()
                .map(image -> new PostImage(image, post.getId()))
                .collect(Collectors.toList());
            postImageRepository.saveAll(postImages);
        }

        PostResponse postResponse = new PostResponse();
        postResponse.setTitle(postDto.getTitle());
        postResponse.setDescription(postDto.getDescription());
        postResponse.setContent(postDto.getContent());
        postResponse.setUserId(postDto.getUserId());
        postResponse.setMode(postDto.getMode());
        postResponse.setCreatedAt(postDto.getCreatedAt());
        postResponse.setUpdatedAt(postDto.getUpdatedAt());
        postResponse.setTotalEmotion(postDto.getTotalEmotion());
        postResponse.setTotalShare(postDto.getTotalShare());
        postResponse.setId(post.getId());
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setUpdatedAt(post.getUpdatedAt());
        postResponse.setHashtags(hashtags);
        postResponse.setImages(postDto.getImages());

        return postResponse;
    }

    @Override
    public void savePostSearch(PostDto postDto) {
        log.debug("Request to save postDto : {}", postDto);
        postSearchRepository.save(postDto);
    }

    @Override
    public Optional<Post> partialUpdate(Post post) {
        log.debug("Request to partially update Post : {}", post);
        postSearchRepository
            .findById(post.getId())
            .map(existingPost -> {
                if (post.getTitle() != null) {
                    existingPost.setTitle(post.getTitle());
                }
                if (post.getDescription() != null) {
                    existingPost.setDescription(post.getDescription());
                }
                if (post.getContent() != null) {
                    existingPost.setContent(post.getContent());
                }
                if (post.getTotalEmotion() != null) {
                    existingPost.setTotalEmotion(post.getTotalEmotion());
                }
                if (post.getTotalShare() != null) {
                    existingPost.setTotalShare(post.getTotalShare());
                }
                if (post.getCreatedAt() != null) {
                    existingPost.setCreatedAt(post.getCreatedAt());
                }
                if (post.getUpdatedAt() != null) {
                    existingPost.setUpdatedAt(post.getUpdatedAt());
                }
                if (post.getUserId() != null) {
                    existingPost.setUserId(post.getUserId());
                }

                return existingPost;
            })
            .map(postSearchRepository::save);

        return postRepository
            .findById(post.getId())
            .map(existingPost -> {
                if (post.getTitle() != null) {
                    existingPost.setTitle(post.getTitle());
                }
                if (post.getDescription() != null) {
                    existingPost.setDescription(post.getDescription());
                }
                if (post.getContent() != null) {
                    existingPost.setContent(post.getContent());
                }
                if (post.getTotalEmotion() != null) {
                    existingPost.setTotalEmotion(post.getTotalEmotion());
                }
                if (post.getTotalShare() != null) {
                    existingPost.setTotalShare(post.getTotalShare());
                }
                if (post.getCreatedAt() != null) {
                    existingPost.setCreatedAt(post.getCreatedAt());
                }
                if (post.getUpdatedAt() != null) {
                    existingPost.setUpdatedAt(post.getUpdatedAt());
                }
                if (post.getUserId() != null) {
                    existingPost.setUserId(post.getUserId());
                }

                return existingPost;
            })
            .map(postRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
        postSearchRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Posts for query {}", query);
        return postSearchRepository.search(query, pageable);
    }

    @Override
    public void incrementTotalEmotionBy(Long postId, Integer num) {
        Optional<Post> postOptional = this.findOne(postId);

        if (!postOptional.isPresent()) {
            throw new NotFoundException("Not found post with id: " + postId);
        }

        Post post = postOptional.get();
        post.setTotalEmotion(post.getTotalEmotion() + num);
        postRepository.save(post);
    }
}
