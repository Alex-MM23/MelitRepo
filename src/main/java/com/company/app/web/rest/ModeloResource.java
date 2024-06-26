package com.company.app.web.rest;

import com.company.app.domain.Modelo;
import com.company.app.repository.ModeloRepository;
import com.company.app.service.ModeloService;
import com.company.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.company.app.domain.Modelo}.
 */
@RestController
@RequestMapping("/api/modelos")
public class ModeloResource {

    private final Logger log = LoggerFactory.getLogger(ModeloResource.class);

    private static final String ENTITY_NAME = "modelo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModeloService modeloService;

    private final ModeloRepository modeloRepository;

    public ModeloResource(ModeloService modeloService, ModeloRepository modeloRepository) {
        this.modeloService = modeloService;
        this.modeloRepository = modeloRepository;
    }

    /**
     * {@code POST  /modelos} : Create a new modelo.
     *
     * @param modelo the modelo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modelo, or with status {@code 400 (Bad Request)} if the modelo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Modelo> createModelo(@RequestBody Modelo modelo) throws URISyntaxException {
        log.debug("REST request to save Modelo : {}", modelo);
        if (modelo.getId() != null) {
            throw new BadRequestAlertException("A new modelo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modelo = modeloService.save(modelo);
        return ResponseEntity.created(new URI("/api/modelos/" + modelo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, modelo.getId().toString()))
            .body(modelo);
    }

    /**
     * {@code PUT  /modelos/:id} : Updates an existing modelo.
     *
     * @param id the id of the modelo to save.
     * @param modelo the modelo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelo,
     * or with status {@code 400 (Bad Request)} if the modelo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modelo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Modelo> updateModelo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Modelo modelo)
        throws URISyntaxException {
        log.debug("REST request to update Modelo : {}, {}", id, modelo);
        if (modelo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        modelo = modeloService.update(modelo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modelo.getId().toString()))
            .body(modelo);
    }

    /**
     * {@code PATCH  /modelos/:id} : Partial updates given fields of an existing modelo, field will ignore if it is null
     *
     * @param id the id of the modelo to save.
     * @param modelo the modelo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelo,
     * or with status {@code 400 (Bad Request)} if the modelo is not valid,
     * or with status {@code 404 (Not Found)} if the modelo is not found,
     * or with status {@code 500 (Internal Server Error)} if the modelo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Modelo> partialUpdateModelo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Modelo modelo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Modelo partially : {}, {}", id, modelo);
        if (modelo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Modelo> result = modeloService.partialUpdate(modelo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modelo.getId().toString())
        );
    }

    /**
     * {@code GET  /modelos} : get all the modelos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modelos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Modelo>> getAllModelos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Modelos");
        Page<Modelo> page = modeloService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /modelos/:id} : get the "id" modelo.
     *
     * @param id the id of the modelo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modelo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Modelo> getModelo(@PathVariable("id") Long id) {
        log.debug("REST request to get Modelo : {}", id);
        Optional<Modelo> modelo = modeloService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modelo);
    }

    /**
     * {@code DELETE  /modelos/:id} : delete the "id" modelo.
     *
     * @param id the id of the modelo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelo(@PathVariable("id") Long id) {
        log.debug("REST request to delete Modelo : {}", id);
        modeloService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
