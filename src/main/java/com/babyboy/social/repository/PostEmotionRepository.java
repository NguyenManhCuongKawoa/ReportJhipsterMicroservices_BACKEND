package com.babyboy.social.repository;

import com.babyboy.social.domain.PostEmotion;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PostEmotion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostEmotionRepository extends JpaRepository<PostEmotion, Long> {
    Optional<PostEmotion> findByUserIdAndPostId(Long userId, Long postId);
}
