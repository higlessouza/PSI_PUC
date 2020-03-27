package com.pucminas.psi.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pucminas.psi.web.rest.TestUtil;

public class TimeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeDTO.class);
        TimeDTO timeDTO1 = new TimeDTO();
        timeDTO1.setId(1L);
        TimeDTO timeDTO2 = new TimeDTO();
        assertThat(timeDTO1).isNotEqualTo(timeDTO2);
        timeDTO2.setId(timeDTO1.getId());
        assertThat(timeDTO1).isEqualTo(timeDTO2);
        timeDTO2.setId(2L);
        assertThat(timeDTO1).isNotEqualTo(timeDTO2);
        timeDTO1.setId(null);
        assertThat(timeDTO1).isNotEqualTo(timeDTO2);
    }
}
