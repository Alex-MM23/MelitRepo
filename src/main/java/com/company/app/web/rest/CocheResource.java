package com.company.app.web.rest;

import com.company.app.domain.Coche;
import com.company.app.repository.CocheRepository;
import com.company.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.company.app.domain.Coche}.
 */
@RestController
@RequestMapping("/api/coches")
@Transactional
public class CocheResource {

    private final Logger log = LoggerFactory.getLogger(CocheResource.class);

    private static final String ENTITY_NAME = "coche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CocheRepository cocheRepository;

    public CocheResource(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    /**
     * {@code POST  /coches} : Create a new coche.
     *
     * @param coche the coche to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coche, or with status {@code 400 (Bad Request)} if the coche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Coche> createCoche(@RequestBody Coche coche) throws URISyntaxException {
        log.debug("REST request to save Coche : {}", coche);
        if (coche.getId() != null) {
            throw new BadRequestAlertException("A new coche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coche = cocheRepository.save(coche);
        return ResponseEntity.created(new URI("/api/coches/" + coche.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, coche.getId().toString()))
            .body(coche);
    }

    /**
     * {@code PUT  /coches/:id} : Updates an existing coche.
     *
     * @param id the id of the coche to save.
     * @param coche the coche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coche,
     * or with status {@code 400 (Bad Request)} if the coche is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Coche> updateCoche(@PathVariable(value = "id", required = false) final Long id, @RequestBody Coche coche)
        throws URISyntaxException {
        log.debug("REST request to update Coche : {}, {}", id, coche);
        if (coche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coche.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cocheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coche = cocheRepository.save(coche);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coche.getId().toString()))
            .body(coche);
    }

    /**
     * {@code PATCH  /coches/:id} : Partial updates given fields of an existing coche, field will ignore if it is null
     *
     * @param id the id of the coche to save.
     * @param coche the coche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coche,
     * or with status {@code 400 (Bad Request)} if the coche is not valid,
     * or with status {@code 404 (Not Found)} if the coche is not found,
     * or with status {@code 500 (Internal Server Error)} if the coche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Coche> partialUpdateCoche(@PathVariable(value = "id", required = false) final Long id, @RequestBody Coche coche)
        throws URISyntaxException {
        log.debug("REST request to partial update Coche partially : {}, {}", id, coche);
        if (coche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coche.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cocheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Coche> result = cocheRepository
            .findById(coche.getId())
            .map(existingCoche -> {
                if (coche.getColor() != null) {
                    existingCoche.setColor(coche.getColor());
                }
                if (coche.getNumeroSerie() != null) {
                    existingCoche.setNumeroSerie(coche.getNumeroSerie());
                }
                if (coche.getPrecio() != null) {
                    existingCoche.setPrecio(coche.getPrecio());
                }
                if (coche.getExposicion() != null) {
                    existingCoche.setExposicion(coche.getExposicion());
                }
                if(coche.getNPuertas() != null) {
                    existingCoche.setNPuertas(coche.getNPuertas());
                }

                return existingCoche;
            })
            .map(cocheRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coche.getId().toString())
        );
    }

    /**
     * {@code GET  /coches} : get all the coches.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coches in body.
     */
    @GetMapping("")
    public List<Coche> getAllCoches(@RequestParam(name = "filter", required = false) String filter) {
        if ("venta-is-null".equals(filter)) {
            log.debug("REST request to get all Coches where venta is null");
            return StreamSupport.stream(cocheRepository.findAll().spliterator(), false).filter(coche -> coche.getVenta() == null).toList();
        }
        log.debug("REST request to get all Coches");
        log.debug(cocheRepository.findAll().toString() + "coches");
        return cocheRepository.findAll();
    }

    /**
     * {@code GET  /coches/:id} : get the "id" coche.
     *
     * @param id the id of the coche to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coche, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Coche> getCoche(@PathVariable("id") Long id) {
        log.debug("REST request to get Coche : {}", id);
        Optional<Coche> coche = cocheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(coche);
    }

    /**
     * {@code DELETE  /coches/:id} : delete the "id" coche.
     *
     * @param id the id of the coche to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoche(@PathVariable("id") Long id) {
        log.debug("REST request to delete Coche : {}", id);
        cocheRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
