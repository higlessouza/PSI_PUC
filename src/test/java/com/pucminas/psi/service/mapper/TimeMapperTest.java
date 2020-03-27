package com.pucminas.psi.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeMapperTest {

    private TimeMapper timeMapper;

    @BeforeEach
    public void setUp() {
        timeMapper = new TimeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(timeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(timeMapper.fromId(null)).isNull();
    }
}
