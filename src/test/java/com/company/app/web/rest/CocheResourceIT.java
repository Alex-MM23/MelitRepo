package com.company.app.web.rest;

import static com.company.app.domain.CocheAsserts.*;
import static com.company.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.company.app.IntegrationTest;
import com.company.app.domain.Coche;
import com.company.app.domain.enumeration.motor;
import com.company.app.repository.CocheRepository;
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
 * Integration tests for the {@link CocheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CocheResourceIT {

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SERIE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRECIO = 1D;
    private static final Double UPDATED_PRECIO = 2D;

    private static final Boolean DEFAULT_EXPOSICION = false;
    private static final Boolean UPDATED_EXPOSICION = true;

    private static final Integer DEFAULT_N_PUERTAS = 1;
    private static final Integer UPDATED_N_PUERTAS = 2;

    private static final motor DEFAULT_MOTOR = motor.GASOLINA;
    private static final motor UPDATED_MOTOR = motor.DIESEL;

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CocheRepository cocheRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCocheMockMvc;

    private Coche coche;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coche createEntity(EntityManager em) {
        Coche coche = new Coche()
            .color(DEFAULT_COLOR)
            .numeroSerie(DEFAULT_NUMERO_SERIE)
            .precio(DEFAULT_PRECIO)
            .exposicion(DEFAULT_EXPOSICION)
            .nPuertas(DEFAULT_N_PUERTAS)
            .motor(DEFAULT_MOTOR)
            .matricula(DEFAULT_MATRICULA);
        return coche;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coche createUpdatedEntity(EntityManager em) {
        Coche coche = new Coche()
            .color(UPDATED_COLOR)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .precio(UPDATED_PRECIO)
            .exposicion(UPDATED_EXPOSICION)
            .nPuertas(UPDATED_N_PUERTAS)
            .motor(UPDATED_MOTOR)
            .matricula(UPDATED_MATRICULA);
        return coche;
    }

    @BeforeEach
    public void initTest() {
        coche = createEntity(em);
    }

    @Test
    @Transactional
    void createCoche() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Coche
        var returnedCoche = om.readValue(
            restCocheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coche)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Coche.class
        );

        // Validate the Coche in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCocheUpdatableFieldsEquals(returnedCoche, getPersistedCoche(returnedCoche));
    }

    @Test
    @Transactional
    void createCocheWithExistingId() throws Exception {
        // Create the Coche with an existing ID
        coche.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCocheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coche)))
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoches() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        // Get all the cocheList
        restCocheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coche.getId().intValue())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].numeroSerie").value(hasItem(DEFAULT_NUMERO_SERIE)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].exposicion").value(hasItem(DEFAULT_EXPOSICION.booleanValue())))
            .andExpect(jsonPath("$.[*].nPuertas").value(hasItem(DEFAULT_N_PUERTAS)))
            .andExpect(jsonPath("$.[*].motor").value(hasItem(DEFAULT_MOTOR.toString())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)));
    }

    @Test
    @Transactional
    void getCoche() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        // Get the coche
        restCocheMockMvc
            .perform(get(ENTITY_API_URL_ID, coche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coche.getId().intValue()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.numeroSerie").value(DEFAULT_NUMERO_SERIE))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.exposicion").value(DEFAULT_EXPOSICION.booleanValue()))
            .andExpect(jsonPath("$.nPuertas").value(DEFAULT_N_PUERTAS))
            .andExpect(jsonPath("$.motor").value(DEFAULT_MOTOR.toString()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA));
    }

    @Test
    @Transactional
    void getNonExistingCoche() throws Exception {
        // Get the coche
        restCocheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoche() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coche
        Coche updatedCoche = cocheRepository.findById(coche.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCoche are not directly saved in db
        em.detach(updatedCoche);
        updatedCoche
            .color(UPDATED_COLOR)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .precio(UPDATED_PRECIO)
            .exposicion(UPDATED_EXPOSICION)
            .nPuertas(UPDATED_N_PUERTAS)
            .motor(UPDATED_MOTOR)
            .matricula(UPDATED_MATRICULA);

        restCocheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoche.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCoche))
            )
            .andExpect(status().isOk());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCocheToMatchAllProperties(updatedCoche);
    }

    @Test
    @Transactional
    void putNonExistingCoche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coche.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCocheMockMvc
            .perform(put(ENTITY_API_URL_ID, coche.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coche)))
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coche.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCocheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coche.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCocheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coche)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCocheWithPatch() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coche using partial update
        Coche partialUpdatedCoche = new Coche();
        partialUpdatedCoche.setId(coche.getId());

        partialUpdatedCoche.color(UPDATED_COLOR).precio(UPDATED_PRECIO).nPuertas(UPDATED_N_PUERTAS).motor(UPDATED_MOTOR);

        restCocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoche.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoche))
            )
            .andExpect(status().isOk());

        // Validate the Coche in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCocheUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCoche, coche), getPersistedCoche(coche));
    }

    @Test
    @Transactional
    void fullUpdateCocheWithPatch() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coche using partial update
        Coche partialUpdatedCoche = new Coche();
        partialUpdatedCoche.setId(coche.getId());

        partialUpdatedCoche
            .color(UPDATED_COLOR)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .precio(UPDATED_PRECIO)
            .exposicion(UPDATED_EXPOSICION)
            .nPuertas(UPDATED_N_PUERTAS)
            .motor(UPDATED_MOTOR)
            .matricula(UPDATED_MATRICULA);

        restCocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoche.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoche))
            )
            .andExpect(status().isOk());

        // Validate the Coche in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCocheUpdatableFieldsEquals(partialUpdatedCoche, getPersistedCoche(partialUpdatedCoche));
    }

    @Test
    @Transactional
    void patchNonExistingCoche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coche.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coche.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coche.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coche.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCocheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coche)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoche() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coche
        restCocheMockMvc
            .perform(delete(ENTITY_API_URL_ID, coche.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cocheRepository.count();
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

    protected Coche getPersistedCoche(Coche coche) {
        return cocheRepository.findById(coche.getId()).orElseThrow();
    }

    protected void assertPersistedCocheToMatchAllProperties(Coche expectedCoche) {
        assertCocheAllPropertiesEquals(expectedCoche, getPersistedCoche(expectedCoche));
    }

    protected void assertPersistedCocheToMatchUpdatableProperties(Coche expectedCoche) {
        assertCocheAllUpdatablePropertiesEquals(expectedCoche, getPersistedCoche(expectedCoche));
    }
}
