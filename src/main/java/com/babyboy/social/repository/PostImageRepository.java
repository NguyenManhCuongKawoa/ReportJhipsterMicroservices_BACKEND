package com.babyboy.social.repository;

import com.babyboy.social.domain.PostImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PostImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {}
