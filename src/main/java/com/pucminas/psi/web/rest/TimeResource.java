package com.pucminas.psi.web.rest;

import com.pucminas.psi.service.TimeService;
import com.pucminas.psi.web.rest.errors.BadRequestAlertException;
import com.pucminas.psi.service.dto.TimeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.pucminas.psi.domain.Time}.
 */
@RestController
@RequestMapping("/api")
public class TimeResource {

    private final Logger log = LoggerFactory.getLogger(TimeResource.class);

    private static final String ENTITY_NAME = "time";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeService timeService;

    public TimeResource(TimeService timeService) {
        this.timeService = timeService;
    }

    /**
     * {@code POST  /times} : Create a new time.
     *
     * @param timeDTO the timeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeDTO, or with status {@code 400 (Bad Request)} if the time has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/times")
    public ResponseEntity<TimeDTO> createTime(@Valid @RequestBody TimeDTO timeDTO) throws URISyntaxException {
        log.debug("REST request to save Time : {}", timeDTO);
        if (timeDTO.getId() != null) {
            throw new BadRequestAlertException("A new time cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeDTO result = timeService.save(timeDTO);
        return ResponseEntity.created(new URI("/api/times/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /times} : Updates an existing time.
     *
     * @param timeDTO the timeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeDTO,
     * or with status {@code 400 (Bad Request)} if the timeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/times")
    public ResponseEntity<TimeDTO> updateTime(@Valid @RequestBody TimeDTO timeDTO) throws URISyntaxException {
        log.debug("REST request to update Time : {}", timeDTO);
        if (timeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TimeDTO result = timeService.save(timeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /times} : get all the times.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of times in body.
     */
    @GetMapping("/times")
    public ResponseEntity<List<TimeDTO>> getAllTimes(Pageable pageable) {
        log.debug("REST request to get a page of Times");
        Page<TimeDTO> page = timeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /times/:id} : get the "id" time.
     *
     * @param id the id of the timeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/times/{id}")
    public ResponseEntity<TimeDTO> getTime(@PathVariable Long id) {
        log.debug("REST request to get Time : {}", id);
        Optional<TimeDTO> timeDTO = timeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeDTO);
    }

    /**
     * {@code DELETE  /times/:id} : delete the "id" time.
     *
     * @param id the id of the timeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        log.debug("REST request to delete Time : {}", id);
        timeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/times?query=:query} : search for the time corresponding
     * to the query.
     *
     * @param query the query of the time search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/times")
    public ResponseEntity<List<TimeDTO>> searchTimes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Times for query {}", query);
        Page<TimeDTO> page = timeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
