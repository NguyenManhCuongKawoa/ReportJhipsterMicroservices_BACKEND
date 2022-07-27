package com.babyboy.social.repository;

import com.babyboy.social.domain.Hashtag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hashtag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {}
