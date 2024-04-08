package com.company.app.service;

import com.company.app.domain.Modelo;
import com.company.app.repository.ModeloRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.company.app.domain.Modelo}.
 */
@Service
@Transactional
public class ModeloService {

    private final Logger log = LoggerFactory.getLogger(ModeloService.class);

    private final ModeloRepository modeloRepository;

    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    /**
     * Save a modelo.
     *
     * @param modelo the entity to save.
     * @return the persisted entity.
     */
    public Modelo save(Modelo modelo) {
        log.debug("Request to save Modelo : {}", modelo);
        return modeloRepository.save(modelo);
    }

    /**
     * Update a modelo.
     *
     * @param modelo the entity to save.
     * @return the persisted entity.
     */
    public Modelo update(Modelo modelo) {
        log.debug("Request to update Modelo : {}", modelo);
        return modeloRepository.save(modelo);
    }

    /**
     * Partially update a modelo.
     *
     * @param modelo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Modelo> partialUpdate(Modelo modelo) {
        log.debug("Request to partially update Modelo : {}", modelo);

        return modeloRepository
            .findById(modelo.getId())
            .map(existingModelo -> {
                if (modelo.getNombre() != null) {
                    existingModelo.setNombre(modelo.getNombre());
                }

                return existingModelo;
            })
            .map(modeloRepository::save);
    }

    /**
     * Get all the modelos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Modelo> findAll(Pageable pageable) {
        log.debug("Request to get all Modelos");
        return modeloRepository.findAll(pageable);
    }

    /**
     * Get one modelo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Modelo> findOne(Long id) {
        log.debug("Request to get Modelo : {}", id);
        return modeloRepository.findById(id);
    }

    /**
     * Delete the modelo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Modelo : {}", id);
        modeloRepository.deleteById(id);
    }
}
