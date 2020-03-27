package com.pucminas.psi.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TimeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TimeSearchRepositoryMockConfiguration {

    @MockBean
    private TimeSearchRepository mockTimeSearchRepository;

}
