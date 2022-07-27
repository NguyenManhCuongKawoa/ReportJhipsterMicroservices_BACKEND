package com.babyboy.social.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babyboy.social.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmotionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emotion.class);
        Emotion emotion1 = new Emotion();
        emotion1.setId(1L);
        Emotion emotion2 = new Emotion();
        emotion2.setId(emotion1.getId());
        assertThat(emotion1).isEqualTo(emotion2);
        emotion2.setId(2L);
        assertThat(emotion1).isNotEqualTo(emotion2);
        emotion1.setId(null);
        assertThat(emotion1).isNotEqualTo(emotion2);
    }
}
