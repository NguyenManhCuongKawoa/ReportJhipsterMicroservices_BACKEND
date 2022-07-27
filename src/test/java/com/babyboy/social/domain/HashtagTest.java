package com.babyboy.social.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babyboy.social.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HashtagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hashtag.class);
        Hashtag hashtag1 = new Hashtag();
        hashtag1.setId(1L);
        Hashtag hashtag2 = new Hashtag();
        hashtag2.setId(hashtag1.getId());
        assertThat(hashtag1).isEqualTo(hashtag2);
        hashtag2.setId(2L);
        assertThat(hashtag1).isNotEqualTo(hashtag2);
        hashtag1.setId(null);
        assertThat(hashtag1).isNotEqualTo(hashtag2);
    }
}
