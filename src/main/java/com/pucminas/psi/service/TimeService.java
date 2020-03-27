package com.pucminas.psi.service;

import com.pucminas.psi.domain.Time;
import com.pucminas.psi.repository.TimeRepository;
import com.pucminas.psi.repository.search.TimeSearchRepository;
import com.pucminas.psi.service.dto.TimeDTO;
import com.pucminas.psi.service.mapper.TimeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Time}.
 */
@Service
@Transactional
public class TimeService {

    private final Logger log = LoggerFactory.getLogger(TimeService.class);

    private final TimeRepository timeRepository;

    private final TimeMapper timeMapper;

    private final TimeSearchRepository timeSearchRepository;

    public TimeService(TimeRepository timeRepository, TimeMapper timeMapper, TimeSearchRepository timeSearchRepository) {
        this.timeRepository = timeRepository;
        this.timeMapper = timeMapper;
        this.timeSearchRepository = timeSearchRepository;
    }

    /**
     * Save a time.
     *
     * @param timeDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeDTO save(TimeDTO timeDTO) {
        log.debug("Request to save Time : {}", timeDTO);
        Time time = timeMapper.toEntity(timeDTO);
        time = timeRepository.save(time);
        TimeDTO result = timeMapper.toDto(time);
        timeSearchRepository.save(time);
        return result;
    }

    /**
     * Get all the times.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TimeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Times");
        return timeRepository.findAll(pageable)
            .map(timeMapper::toDto);
    }

    /**
     * Get one time by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimeDTO> findOne(Long id) {
        log.debug("Request to get Time : {}", id);
        return timeRepository.findById(id)
            .map(timeMapper::toDto);
    }

    /**
     * Delete the time by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Time : {}", id);
        timeRepository.deleteById(id);
        timeSearchRepository.deleteById(id);
    }

    /**
     * Search for the time corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TimeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Times for query {}", query);
        return timeSearchRepository.search(queryStringQuery(query), pageable)
            .map(timeMapper::toDto);
    }
}
