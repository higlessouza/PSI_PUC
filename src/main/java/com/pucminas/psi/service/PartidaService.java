package com.pucminas.psi.service;

import com.pucminas.psi.domain.Partida;
import com.pucminas.psi.repository.PartidaRepository;
import com.pucminas.psi.repository.search.PartidaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Partida}.
 */
@Service
@Transactional
public class PartidaService {

    private final Logger log = LoggerFactory.getLogger(PartidaService.class);

    private final PartidaRepository partidaRepository;

    private final PartidaSearchRepository partidaSearchRepository;

    public PartidaService(PartidaRepository partidaRepository, PartidaSearchRepository partidaSearchRepository) {
        this.partidaRepository = partidaRepository;
        this.partidaSearchRepository = partidaSearchRepository;
    }

    /**
     * Save a partida.
     *
     * @param partida the entity to save.
     * @return the persisted entity.
     */
    public Partida save(Partida partida) {
        log.debug("Request to save Partida : {}", partida);
        Partida result = partidaRepository.save(partida);
        partidaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the partidas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Partida> findAll() {
        log.debug("Request to get all Partidas");
        return partidaRepository.findAll();
    }

    /**
     * Get one partida by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Partida> findOne(Long id) {
        log.debug("Request to get Partida : {}", id);
        return partidaRepository.findById(id);
    }

    /**
     * Delete the partida by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Partida : {}", id);
        partidaRepository.deleteById(id);
        partidaSearchRepository.deleteById(id);
    }

    /**
     * Search for the partida corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Partida> search(String query) {
        log.debug("Request to search Partidas for query {}", query);
        return StreamSupport
            .stream(partidaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
