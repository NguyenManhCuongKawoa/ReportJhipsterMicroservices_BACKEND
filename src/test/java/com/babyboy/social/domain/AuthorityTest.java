//package com.babyboy.social.domain;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.babyboy.social.web.rest.TestUtil;
//import org.junit.jupiter.api.Test;
//
//class AuthorityTest {
//
//    @Test
//    void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Authority.class);
//        Authority jAuthority1 = new Authority();
//        jAuthority1.setId(1L);
//        Authority jAuthority2 = new Authority();
//        jAuthority2.setId(jAuthority1.getId());
//        assertThat(jAuthority1).isEqualTo(jAuthority2);
//        jAuthority2.setId(2L);
//        assertThat(jAuthority1).isNotEqualTo(jAuthority2);
//        jAuthority1.setId(null);
//        assertThat(jAuthority1).isNotEqualTo(jAuthority2);
//    }
//}
