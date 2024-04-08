package com.company.app.web.rest;

import static com.company.app.domain.MarcaAsserts.*;
import static com.company.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.company.app.IntegrationTest;
import com.company.app.domain.Marca;
import com.company.app.repository.MarcaRepository;
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
 * Integration tests for the {@link MarcaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarcaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marcas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarcaMockMvc;

    private Marca marca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marca createEntity(EntityManager em) {
        Marca marca = new Marca().nombre(DEFAULT_NOMBRE);
        return marca;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marca createUpdatedEntity(EntityManager em) {
        Marca marca = new Marca().nombre(UPDATED_NOMBRE);
        return marca;
    }

    @BeforeEach
    public void initTest() {
        marca = createEntity(em);
    }

    @Test
    @Transactional
    void createMarca() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Marca
        var returnedMarca = om.readValue(
            restMarcaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Marca.class
        );

        // Validate the Marca in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMarcaUpdatableFieldsEquals(returnedMarca, getPersistedMarca(returnedMarca));
    }

    @Test
    @Transactional
    void createMarcaWithExistingId() throws Exception {
        // Create the Marca with an existing ID
        marca.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarcaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMarcas() throws Exception {
        // Initialize the database
        marcaRepository.saveAndFlush(marca);

        // Get all the marcaList
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getMarca() throws Exception {
        // Initialize the database
        marcaRepository.saveAndFlush(marca);

        // Get the marca
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL_ID, marca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marca.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingMarca() throws Exception {
        // Get the marca
        restMarcaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMarca() throws Exception {
        // Initialize the database
        marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca
        Marca updatedMarca = marcaRepository.findById(marca.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMarca are not directly saved in db
        em.detach(updatedMarca);
        updatedMarca.nombre(UPDATED_NOMBRE);

        restMarcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarca.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMarcaToMatchAllProperties(updatedMarca);
    }

    @Test
    @Transactional
    void putNonExistingMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(put(ENTITY_API_URL_ID, marca.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarcaWithPatch() throws Exception {
        // Initialize the database
        marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca using partial update
        Marca partialUpdatedMarca = new Marca();
        partialUpdatedMarca.setId(marca.getId());

        partialUpdatedMarca.nombre(UPDATED_NOMBRE);

        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarcaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMarca, marca), getPersistedMarca(marca));
    }

    @Test
    @Transactional
    void fullUpdateMarcaWithPatch() throws Exception {
        // Initialize the database
        marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca using partial update
        Marca partialUpdatedMarca = new Marca();
        partialUpdatedMarca.setId(marca.getId());

        partialUpdatedMarca.nombre(UPDATED_NOMBRE);

        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarcaUpdatableFieldsEquals(partialUpdatedMarca, getPersistedMarca(partialUpdatedMarca));
    }

    @Test
    @Transactional
    void patchNonExistingMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marca.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marca)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarca() throws Exception {
        // Initialize the database
        marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the marca
        restMarcaMockMvc
            .perform(delete(ENTITY_API_URL_ID, marca.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return marcaRepository.count();
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

    protected Marca getPersistedMarca(Marca marca) {
        return marcaRepository.findById(marca.getId()).orElseThrow();
    }

    protected void assertPersistedMarcaToMatchAllProperties(Marca expectedMarca) {
        assertMarcaAllPropertiesEquals(expectedMarca, getPersistedMarca(expectedMarca));
    }

    protected void assertPersistedMarcaToMatchUpdatableProperties(Marca expectedMarca) {
        assertMarcaAllUpdatablePropertiesEquals(expectedMarca, getPersistedMarca(expectedMarca));
    }
}
