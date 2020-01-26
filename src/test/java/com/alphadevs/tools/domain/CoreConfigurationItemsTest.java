package com.alphadevs.tools.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.tools.web.rest.TestUtil;

public class CoreConfigurationItemsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoreConfigurationItems.class);
        CoreConfigurationItems coreConfigurationItems1 = new CoreConfigurationItems();
        coreConfigurationItems1.setId(1L);
        CoreConfigurationItems coreConfigurationItems2 = new CoreConfigurationItems();
        coreConfigurationItems2.setId(coreConfigurationItems1.getId());
        assertThat(coreConfigurationItems1).isEqualTo(coreConfigurationItems2);
        coreConfigurationItems2.setId(2L);
        assertThat(coreConfigurationItems1).isNotEqualTo(coreConfigurationItems2);
        coreConfigurationItems1.setId(null);
        assertThat(coreConfigurationItems1).isNotEqualTo(coreConfigurationItems2);
    }
}
