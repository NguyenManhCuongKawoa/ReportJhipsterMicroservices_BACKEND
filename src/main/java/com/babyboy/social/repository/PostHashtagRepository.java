package com.babyboy.social.repository;

import com.babyboy.social.domain.PostHashtag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PostHashtag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {}
