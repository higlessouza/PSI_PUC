package com.pucminas.psi.web.rest;

import com.pucminas.psi.PsiApp;
import com.pucminas.psi.domain.Time;
import com.pucminas.psi.repository.TimeRepository;
import com.pucminas.psi.repository.search.TimeSearchRepository;
import com.pucminas.psi.service.TimeService;
import com.pucminas.psi.service.dto.TimeDTO;
import com.pucminas.psi.service.mapper.TimeMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TimeResource} REST controller.
 */
@SpringBootTest(classes = PsiApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TimeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_ESCUDO = "AAAAAAAAAA";
    private static final String UPDATED_ESCUDO = "BBBBBBBBBB";

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private TimeMapper timeMapper;

    @Autowired
    private TimeService timeService;

    /**
     * This repository is mocked in the com.pucminas.psi.repository.search test package.
     *
     * @see com.pucminas.psi.repository.search.TimeSearchRepositoryMockConfiguration
     */
    @Autowired
    private TimeSearchRepository mockTimeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeMockMvc;

    private Time time;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Time createEntity(EntityManager em) {
        Time time = new Time()
            .nome(DEFAULT_NOME)
            .escudo(DEFAULT_ESCUDO);
        return time;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Time createUpdatedEntity(EntityManager em) {
        Time time = new Time()
            .nome(UPDATED_NOME)
            .escudo(UPDATED_ESCUDO);
        return time;
    }

    @BeforeEach
    public void initTest() {
        time = createEntity(em);
    }

    @Test
    @Transactional
    public void createTime() throws Exception {
        int databaseSizeBeforeCreate = timeRepository.findAll().size();

        // Create the Time
        TimeDTO timeDTO = timeMapper.toDto(time);
        restTimeMockMvc.perform(post("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isCreated());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeCreate + 1);
        Time testTime = timeList.get(timeList.size() - 1);
        assertThat(testTime.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTime.getEscudo()).isEqualTo(DEFAULT_ESCUDO);

        // Validate the Time in Elasticsearch
        verify(mockTimeSearchRepository, times(1)).save(testTime);
    }

    @Test
    @Transactional
    public void createTimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeRepository.findAll().size();

        // Create the Time with an existing ID
        time.setId(1L);
        TimeDTO timeDTO = timeMapper.toDto(time);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeMockMvc.perform(post("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Time in Elasticsearch
        verify(mockTimeSearchRepository, times(0)).save(time);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeRepository.findAll().size();
        // set the field null
        time.setNome(null);

        // Create the Time, which fails.
        TimeDTO timeDTO = timeMapper.toDto(time);

        restTimeMockMvc.perform(post("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isBadRequest());

        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEscudoIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeRepository.findAll().size();
        // set the field null
        time.setEscudo(null);

        // Create the Time, which fails.
        TimeDTO timeDTO = timeMapper.toDto(time);

        restTimeMockMvc.perform(post("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isBadRequest());

        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimes() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        // Get all the timeList
        restTimeMockMvc.perform(get("/api/times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(time.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].escudo").value(hasItem(DEFAULT_ESCUDO)));
    }
    
    @Test
    @Transactional
    public void getTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        // Get the time
        restTimeMockMvc.perform(get("/api/times/{id}", time.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(time.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.escudo").value(DEFAULT_ESCUDO));
    }

    @Test
    @Transactional
    public void getNonExistingTime() throws Exception {
        // Get the time
        restTimeMockMvc.perform(get("/api/times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        int databaseSizeBeforeUpdate = timeRepository.findAll().size();

        // Update the time
        Time updatedTime = timeRepository.findById(time.getId()).get();
        // Disconnect from session so that the updates on updatedTime are not directly saved in db
        em.detach(updatedTime);
        updatedTime
            .nome(UPDATED_NOME)
            .escudo(UPDATED_ESCUDO);
        TimeDTO timeDTO = timeMapper.toDto(updatedTime);

        restTimeMockMvc.perform(put("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isOk());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeUpdate);
        Time testTime = timeList.get(timeList.size() - 1);
        assertThat(testTime.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTime.getEscudo()).isEqualTo(UPDATED_ESCUDO);

        // Validate the Time in Elasticsearch
        verify(mockTimeSearchRepository, times(1)).save(testTime);
    }

    @Test
    @Transactional
    public void updateNonExistingTime() throws Exception {
        int databaseSizeBeforeUpdate = timeRepository.findAll().size();

        // Create the Time
        TimeDTO timeDTO = timeMapper.toDto(time);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeMockMvc.perform(put("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Time in Elasticsearch
        verify(mockTimeSearchRepository, times(0)).save(time);
    }

    @Test
    @Transactional
    public void deleteTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        int databaseSizeBeforeDelete = timeRepository.findAll().size();

        // Delete the time
        restTimeMockMvc.perform(delete("/api/times/{id}", time.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Time in Elasticsearch
        verify(mockTimeSearchRepository, times(1)).deleteById(time.getId());
    }

    @Test
    @Transactional
    public void searchTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);
        when(mockTimeSearchRepository.search(queryStringQuery("id:" + time.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(time), PageRequest.of(0, 1), 1));
        // Search the time
        restTimeMockMvc.perform(get("/api/_search/times?query=id:" + time.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(time.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].escudo").value(hasItem(DEFAULT_ESCUDO)));
    }
}
