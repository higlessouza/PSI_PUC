package com.pucminas.psi.repository.search;

import com.pucminas.psi.domain.Time;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Time} entity.
 */
public interface TimeSearchRepository extends ElasticsearchRepository<Time, Long> {
}
