package com.babyboy.social.service.impl;

import com.babyboy.social.domain.Emotion;
import com.babyboy.social.repository.EmotionRepository;
import com.babyboy.social.service.EmotionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Emotion}.
 */
@Service
@Transactional
public class EmotionServiceImpl implements EmotionService {

    private final Logger log = LoggerFactory.getLogger(EmotionServiceImpl.class);

    private final EmotionRepository emotionRepository;

    public EmotionServiceImpl(EmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

    @Override
    public Emotion save(Emotion emotion) {
        log.debug("Request to save Emotion : {}", emotion);
        return emotionRepository.save(emotion);
    }

    @Override
    public Optional<Emotion> partialUpdate(Emotion emotion) {
        log.debug("Request to partially update Emotion : {}", emotion);

        return emotionRepository
            .findById(emotion.getId())
            .map(existingEmotion -> {
                if (emotion.getName() != null) {
                    existingEmotion.setName(emotion.getName());
                }
                if (emotion.getIcon() != null) {
                    existingEmotion.setIcon(emotion.getIcon());
                }

                return existingEmotion;
            })
            .map(emotionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emotion> findAll() {
        log.debug("Request to get all Emotions");
        return emotionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Emotion> findOne(Long id) {
        log.debug("Request to get Emotion : {}", id);
        return emotionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Emotion : {}", id);
        emotionRepository.deleteById(id);
    }
}
