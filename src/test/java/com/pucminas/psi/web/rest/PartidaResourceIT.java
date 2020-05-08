package com.pucminas.psi.web.rest;

import com.pucminas.psi.PsiApp;
import com.pucminas.psi.domain.Partida;
import com.pucminas.psi.repository.PartidaRepository;
import com.pucminas.psi.repository.search.PartidaSearchRepository;
import com.pucminas.psi.service.PartidaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Integration tests for the {@link PartidaResource} REST controller.
 */
@SpringBootTest(classes = PsiApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PartidaResourceIT {

    private static final Integer DEFAULT_MANDANTE_ID = 1;
    private static final Integer UPDATED_MANDANTE_ID = 2;

    private static final Integer DEFAULT_VISITANTE_ID = 1;
    private static final Integer UPDATED_VISITANTE_ID = 2;

    private static final Integer DEFAULT_MANDANTE_GOLS = 1;
    private static final Integer UPDATED_MANDANTE_GOLS = 2;

    private static final Integer DEFAULT_VISITANTE_GOLS = 1;
    private static final Integer UPDATED_VISITANTE_GOLS = 2;

    private static final String DEFAULT_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAMPEONATO = 1;
    private static final Integer UPDATED_CAMPEONATO = 2;

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private PartidaService partidaService;

    /**
     * This repository is mocked in the com.pucminas.psi.repository.search test package.
     *
     * @see com.pucminas.psi.repository.search.PartidaSearchRepositoryMockConfiguration
     */
    @Autowired
    private PartidaSearchRepository mockPartidaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartidaMockMvc;

    private Partida partida;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partida createEntity(EntityManager em) {
        Partida partida = new Partida()
            .mandanteID(DEFAULT_MANDANTE_ID)
            .visitanteID(DEFAULT_VISITANTE_ID)
            .mandanteGols(DEFAULT_MANDANTE_GOLS)
            .visitanteGols(DEFAULT_VISITANTE_GOLS)
            .local(DEFAULT_LOCAL)
            .campeonato(DEFAULT_CAMPEONATO);
        return partida;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partida createUpdatedEntity(EntityManager em) {
        Partida partida = new Partida()
            .mandanteID(UPDATED_MANDANTE_ID)
            .visitanteID(UPDATED_VISITANTE_ID)
            .mandanteGols(UPDATED_MANDANTE_GOLS)
            .visitanteGols(UPDATED_VISITANTE_GOLS)
            .local(UPDATED_LOCAL)
            .campeonato(UPDATED_CAMPEONATO);
        return partida;
    }

    @BeforeEach
    public void initTest() {
        partida = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartida() throws Exception {
        int databaseSizeBeforeCreate = partidaRepository.findAll().size();

        // Create the Partida
        restPartidaMockMvc.perform(post("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partida)))
            .andExpect(status().isCreated());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeCreate + 1);
        Partida testPartida = partidaList.get(partidaList.size() - 1);
        assertThat(testPartida.getMandanteID()).isEqualTo(DEFAULT_MANDANTE_ID);
        assertThat(testPartida.getVisitanteID()).isEqualTo(DEFAULT_VISITANTE_ID);
        assertThat(testPartida.getMandanteGols()).isEqualTo(DEFAULT_MANDANTE_GOLS);
        assertThat(testPartida.getVisitanteGols()).isEqualTo(DEFAULT_VISITANTE_GOLS);
        assertThat(testPartida.getLocal()).isEqualTo(DEFAULT_LOCAL);
        assertThat(testPartida.getCampeonato()).isEqualTo(DEFAULT_CAMPEONATO);

        // Validate the Partida in Elasticsearch
        verify(mockPartidaSearchRepository, times(1)).save(testPartida);
    }

    @Test
    @Transactional
    public void createPartidaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partidaRepository.findAll().size();

        // Create the Partida with an existing ID
        partida.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartidaMockMvc.perform(post("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partida)))
            .andExpect(status().isBadRequest());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Partida in Elasticsearch
        verify(mockPartidaSearchRepository, times(0)).save(partida);
    }


    @Test
    @Transactional
    public void getAllPartidas() throws Exception {
        // Initialize the database
        partidaRepository.saveAndFlush(partida);

        // Get all the partidaList
        restPartidaMockMvc.perform(get("/api/partidas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partida.getId().intValue())))
            .andExpect(jsonPath("$.[*].mandanteID").value(hasItem(DEFAULT_MANDANTE_ID)))
            .andExpect(jsonPath("$.[*].visitanteID").value(hasItem(DEFAULT_VISITANTE_ID)))
            .andExpect(jsonPath("$.[*].mandanteGols").value(hasItem(DEFAULT_MANDANTE_GOLS)))
            .andExpect(jsonPath("$.[*].visitanteGols").value(hasItem(DEFAULT_VISITANTE_GOLS)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].campeonato").value(hasItem(DEFAULT_CAMPEONATO)));
    }
    
    @Test
    @Transactional
    public void getPartida() throws Exception {
        // Initialize the database
        partidaRepository.saveAndFlush(partida);

        // Get the partida
        restPartidaMockMvc.perform(get("/api/partidas/{id}", partida.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partida.getId().intValue()))
            .andExpect(jsonPath("$.mandanteID").value(DEFAULT_MANDANTE_ID))
            .andExpect(jsonPath("$.visitanteID").value(DEFAULT_VISITANTE_ID))
            .andExpect(jsonPath("$.mandanteGols").value(DEFAULT_MANDANTE_GOLS))
            .andExpect(jsonPath("$.visitanteGols").value(DEFAULT_VISITANTE_GOLS))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL))
            .andExpect(jsonPath("$.campeonato").value(DEFAULT_CAMPEONATO));
    }

    @Test
    @Transactional
    public void getNonExistingPartida() throws Exception {
        // Get the partida
        restPartidaMockMvc.perform(get("/api/partidas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartida() throws Exception {
        // Initialize the database
        partidaService.save(partida);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPartidaSearchRepository);

        int databaseSizeBeforeUpdate = partidaRepository.findAll().size();

        // Update the partida
        Partida updatedPartida = partidaRepository.findById(partida.getId()).get();
        // Disconnect from session so that the updates on updatedPartida are not directly saved in db
        em.detach(updatedPartida);
        updatedPartida
            .mandanteID(UPDATED_MANDANTE_ID)
            .visitanteID(UPDATED_VISITANTE_ID)
            .mandanteGols(UPDATED_MANDANTE_GOLS)
            .visitanteGols(UPDATED_VISITANTE_GOLS)
            .local(UPDATED_LOCAL)
            .campeonato(UPDATED_CAMPEONATO);

        restPartidaMockMvc.perform(put("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPartida)))
            .andExpect(status().isOk());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeUpdate);
        Partida testPartida = partidaList.get(partidaList.size() - 1);
        assertThat(testPartida.getMandanteID()).isEqualTo(UPDATED_MANDANTE_ID);
        assertThat(testPartida.getVisitanteID()).isEqualTo(UPDATED_VISITANTE_ID);
        assertThat(testPartida.getMandanteGols()).isEqualTo(UPDATED_MANDANTE_GOLS);
        assertThat(testPartida.getVisitanteGols()).isEqualTo(UPDATED_VISITANTE_GOLS);
        assertThat(testPartida.getLocal()).isEqualTo(UPDATED_LOCAL);
        assertThat(testPartida.getCampeonato()).isEqualTo(UPDATED_CAMPEONATO);

        // Validate the Partida in Elasticsearch
        verify(mockPartidaSearchRepository, times(1)).save(testPartida);
    }

    @Test
    @Transactional
    public void updateNonExistingPartida() throws Exception {
        int databaseSizeBeforeUpdate = partidaRepository.findAll().size();

        // Create the Partida

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartidaMockMvc.perform(put("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partida)))
            .andExpect(status().isBadRequest());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Partida in Elasticsearch
        verify(mockPartidaSearchRepository, times(0)).save(partida);
    }

    @Test
    @Transactional
    public void deletePartida() throws Exception {
        // Initialize the database
        partidaService.save(partida);

        int databaseSizeBeforeDelete = partidaRepository.findAll().size();

        // Delete the partida
        restPartidaMockMvc.perform(delete("/api/partidas/{id}", partida.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Partida in Elasticsearch
        verify(mockPartidaSearchRepository, times(1)).deleteById(partida.getId());
    }

    @Test
    @Transactional
    public void searchPartida() throws Exception {
        // Initialize the database
        partidaService.save(partida);
        when(mockPartidaSearchRepository.search(queryStringQuery("id:" + partida.getId())))
            .thenReturn(Collections.singletonList(partida));
        // Search the partida
        restPartidaMockMvc.perform(get("/api/_search/partidas?query=id:" + partida.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partida.getId().intValue())))
            .andExpect(jsonPath("$.[*].mandanteID").value(hasItem(DEFAULT_MANDANTE_ID)))
            .andExpect(jsonPath("$.[*].visitanteID").value(hasItem(DEFAULT_VISITANTE_ID)))
            .andExpect(jsonPath("$.[*].mandanteGols").value(hasItem(DEFAULT_MANDANTE_GOLS)))
            .andExpect(jsonPath("$.[*].visitanteGols").value(hasItem(DEFAULT_VISITANTE_GOLS)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].campeonato").value(hasItem(DEFAULT_CAMPEONATO)));
    }
}
