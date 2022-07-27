package com.babyboy.social.repository;

import com.babyboy.social.domain.Emotion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Emotion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {}
