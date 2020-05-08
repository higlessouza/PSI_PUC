package com.pucminas.psi.repository.search;

import com.pucminas.psi.domain.Partida;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Partida} entity.
 */
public interface PartidaSearchRepository extends ElasticsearchRepository<Partida, Long> {
}
