package com.company.app.web.rest;

import static com.company.app.domain.ModeloAsserts.*;
import static com.company.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.company.app.IntegrationTest;
import com.company.app.domain.Modelo;
import com.company.app.repository.ModeloRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ModeloResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModeloResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modelos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModeloMockMvc;

    private Modelo modelo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modelo createEntity(EntityManager em) {
        Modelo modelo = new Modelo().nombre(DEFAULT_NOMBRE);
        return modelo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modelo createUpdatedEntity(EntityManager em) {
        Modelo modelo = new Modelo().nombre(UPDATED_NOMBRE);
        return modelo;
    }

    @BeforeEach
    public void initTest() {
        modelo = createEntity(em);
    }

    @Test
    @Transactional
    void createModelo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Modelo
        var returnedModelo = om.readValue(
            restModeloMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Modelo.class
        );

        // Validate the Modelo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertModeloUpdatableFieldsEquals(returnedModelo, getPersistedModelo(returnedModelo));
    }

    @Test
    @Transactional
    void createModeloWithExistingId() throws Exception {
        // Create the Modelo with an existing ID
        modelo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelo)))
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllModelos() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList
        restModeloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getModelo() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        // Get the modelo
        restModeloMockMvc
            .perform(get(ENTITY_API_URL_ID, modelo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modelo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingModelo() throws Exception {
        // Get the modelo
        restModeloMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModelo() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modelo
        Modelo updatedModelo = modeloRepository.findById(modelo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModelo are not directly saved in db
        em.detach(updatedModelo);
        updatedModelo.nombre(UPDATED_NOMBRE);

        restModeloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedModelo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedModelo))
            )
            .andExpect(status().isOk());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModeloToMatchAllProperties(updatedModelo);
    }

    @Test
    @Transactional
    void putNonExistingModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(put(ENTITY_API_URL_ID, modelo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelo)))
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modelo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModeloWithPatch() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modelo using partial update
        Modelo partialUpdatedModelo = new Modelo();
        partialUpdatedModelo.setId(modelo.getId());

        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModelo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModelo))
            )
            .andExpect(status().isOk());

        // Validate the Modelo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModeloUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedModelo, modelo), getPersistedModelo(modelo));
    }

    @Test
    @Transactional
    void fullUpdateModeloWithPatch() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modelo using partial update
        Modelo partialUpdatedModelo = new Modelo();
        partialUpdatedModelo.setId(modelo.getId());

        partialUpdatedModelo.nombre(UPDATED_NOMBRE);

        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModelo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModelo))
            )
            .andExpect(status().isOk());

        // Validate the Modelo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModeloUpdatableFieldsEquals(partialUpdatedModelo, getPersistedModelo(partialUpdatedModelo));
    }

    @Test
    @Transactional
    void patchNonExistingModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modelo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(modelo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(modelo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(modelo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModelo() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the modelo
        restModeloMockMvc
            .perform(delete(ENTITY_API_URL_ID, modelo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return modeloRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Modelo getPersistedModelo(Modelo modelo) {
        return modeloRepository.findById(modelo.getId()).orElseThrow();
    }

    protected void assertPersistedModeloToMatchAllProperties(Modelo expectedModelo) {
        assertModeloAllPropertiesEquals(expectedModelo, getPersistedModelo(expectedModelo));
    }

    protected void assertPersistedModeloToMatchUpdatableProperties(Modelo expectedModelo) {
        assertModeloAllUpdatablePropertiesEquals(expectedModelo, getPersistedModelo(expectedModelo));
    }
}
