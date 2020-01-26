package com.alphadevs.tools.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.tools.web.rest.TestUtil;

public class ExUserAuthTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExUserAuth.class);
        ExUserAuth exUserAuth1 = new ExUserAuth();
        exUserAuth1.setId(1L);
        ExUserAuth exUserAuth2 = new ExUserAuth();
        exUserAuth2.setId(exUserAuth1.getId());
        assertThat(exUserAuth1).isEqualTo(exUserAuth2);
        exUserAuth2.setId(2L);
        assertThat(exUserAuth1).isNotEqualTo(exUserAuth2);
        exUserAuth1.setId(null);
        assertThat(exUserAuth1).isNotEqualTo(exUserAuth2);
    }
}
