package com.company.app.web.rest;

import static com.company.app.domain.ClienteAsserts.*;
import static com.company.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.company.app.IntegrationTest;
import com.company.app.domain.Cliente;
import com.company.app.repository.ClienteRepository;
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
 * Integration tests for the {@link ClienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClienteResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_COMPRAS = 1;
    private static final Integer UPDATED_NUMERO_COMPRAS = 2;

    private static final Integer DEFAULT_TIER = 1;
    private static final Integer UPDATED_TIER = 2;

    private static final String ENTITY_API_URL = "/api/clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente().dni(DEFAULT_DNI).nombre(DEFAULT_NOMBRE).numeroCompras(DEFAULT_NUMERO_COMPRAS).tier(DEFAULT_TIER);
        return cliente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createUpdatedEntity(EntityManager em) {
        Cliente cliente = new Cliente().dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).numeroCompras(UPDATED_NUMERO_COMPRAS).tier(UPDATED_TIER);
        return cliente;
    }

    @BeforeEach
    public void initTest() {
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    void createCliente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cliente
        var returnedCliente = om.readValue(
            restClienteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cliente)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Cliente.class
        );

        // Validate the Cliente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertClienteUpdatableFieldsEquals(returnedCliente, getPersistedCliente(returnedCliente));
    }

    @Test
    @Transactional
    void createClienteWithExistingId() throws Exception {
        // Create the Cliente with an existing ID
        cliente.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cliente)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].numeroCompras").value(hasItem(DEFAULT_NUMERO_COMPRAS)))
            .andExpect(jsonPath("$.[*].tier").value(hasItem(DEFAULT_TIER)));
    }

    @Test
    @Transactional
    void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.numeroCompras").value(DEFAULT_NUMERO_COMPRAS))
            .andExpect(jsonPath("$.tier").value(DEFAULT_TIER));
    }

    @Test
    @Transactional
    void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).numeroCompras(UPDATED_NUMERO_COMPRAS).tier(UPDATED_TIER);

        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCliente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClienteToMatchAllProperties(updatedCliente);
    }

    @Test
    @Transactional
    void putNonExistingCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(put(ENTITY_API_URL_ID, cliente.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cliente)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cliente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cliente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).numeroCompras(UPDATED_NUMERO_COMPRAS);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCliente, cliente), getPersistedCliente(cliente));
    }

    @Test
    @Transactional
    void fullUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).numeroCompras(UPDATED_NUMERO_COMPRAS).tier(UPDATED_TIER);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteUpdatableFieldsEquals(partialUpdatedCliente, getPersistedCliente(partialUpdatedCliente));
    }

    @Test
    @Transactional
    void patchNonExistingCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cliente.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cliente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cliente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cliente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cliente
        restClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cliente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clienteRepository.count();
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

    protected Cliente getPersistedCliente(Cliente cliente) {
        return clienteRepository.findById(cliente.getId()).orElseThrow();
    }

    protected void assertPersistedClienteToMatchAllProperties(Cliente expectedCliente) {
        assertClienteAllPropertiesEquals(expectedCliente, getPersistedCliente(expectedCliente));
    }

    protected void assertPersistedClienteToMatchUpdatableProperties(Cliente expectedCliente) {
        assertClienteAllUpdatablePropertiesEquals(expectedCliente, getPersistedCliente(expectedCliente));
    }
}
