package com.babyboy.social.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babyboy.social.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserHashtagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserHashtag.class);
        UserHashtag userHashtag1 = new UserHashtag();
        userHashtag1.setId(1L);
        UserHashtag userHashtag2 = new UserHashtag();
        userHashtag2.setId(userHashtag1.getId());
        assertThat(userHashtag1).isEqualTo(userHashtag2);
        userHashtag2.setId(2L);
        assertThat(userHashtag1).isNotEqualTo(userHashtag2);
        userHashtag1.setId(null);
        assertThat(userHashtag1).isNotEqualTo(userHashtag2);
    }
}
