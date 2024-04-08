package com.company.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class VentaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaAllPropertiesEquals(Venta expected, Venta actual) {
        assertVentaAutoGeneratedPropertiesEquals(expected, actual);
        assertVentaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaAllUpdatablePropertiesEquals(Venta expected, Venta actual) {
        assertVentaUpdatableFieldsEquals(expected, actual);
        assertVentaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaAutoGeneratedPropertiesEquals(Venta expected, Venta actual) {
        assertThat(expected)
            .as("Verify Venta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaUpdatableFieldsEquals(Venta expected, Venta actual) {
        assertThat(expected)
            .as("Verify Venta relevant properties")
            .satisfies(e -> assertThat(e.getFecha()).as("check fecha").isEqualTo(actual.getFecha()))
            .satisfies(e -> assertThat(e.getTipoPago()).as("check tipoPago").isEqualTo(actual.getTipoPago()))
            .satisfies(e -> assertThat(e.getTotal()).as("check total").isEqualTo(actual.getTotal()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaUpdatableRelationshipsEquals(Venta expected, Venta actual) {
        assertThat(expected)
            .as("Verify Venta relationships")
            .satisfies(e -> assertThat(e.getEmpleado()).as("check empleado").isEqualTo(actual.getEmpleado()))
            .satisfies(e -> assertThat(e.getCliente()).as("check cliente").isEqualTo(actual.getCliente()))
            .satisfies(e -> assertThat(e.getId_coche()).as("check id_coche").isEqualTo(actual.getId_coche()));
    }
}