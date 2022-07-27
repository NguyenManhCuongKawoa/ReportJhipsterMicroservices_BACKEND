package com.babyboy.social.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babyboy.social.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostEmotionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostEmotion.class);
        PostEmotion postEmotion1 = new PostEmotion();
        postEmotion1.setId(1L);
        PostEmotion postEmotion2 = new PostEmotion();
        postEmotion2.setId(postEmotion1.getId());
        assertThat(postEmotion1).isEqualTo(postEmotion2);
        postEmotion2.setId(2L);
        assertThat(postEmotion1).isNotEqualTo(postEmotion2);
        postEmotion1.setId(null);
        assertThat(postEmotion1).isNotEqualTo(postEmotion2);
    }
}
