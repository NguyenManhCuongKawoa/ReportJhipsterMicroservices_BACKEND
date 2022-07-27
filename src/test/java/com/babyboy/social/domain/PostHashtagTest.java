package com.babyboy.social.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babyboy.social.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostHashtagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostHashtag.class);
        PostHashtag postHashtag1 = new PostHashtag();
        postHashtag1.setId(1L);
        PostHashtag postHashtag2 = new PostHashtag();
        postHashtag2.setId(postHashtag1.getId());
        assertThat(postHashtag1).isEqualTo(postHashtag2);
        postHashtag2.setId(2L);
        assertThat(postHashtag1).isNotEqualTo(postHashtag2);
        postHashtag1.setId(null);
        assertThat(postHashtag1).isNotEqualTo(postHashtag2);
    }
}
