package com.company.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmpleadoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoAllPropertiesEquals(Empleado expected, Empleado actual) {
        assertEmpleadoAutoGeneratedPropertiesEquals(expected, actual);
        assertEmpleadoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoAllUpdatablePropertiesEquals(Empleado expected, Empleado actual) {
        assertEmpleadoUpdatableFieldsEquals(expected, actual);
        assertEmpleadoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoAutoGeneratedPropertiesEquals(Empleado expected, Empleado actual) {
        assertThat(expected)
            .as("Verify Empleado auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoUpdatableFieldsEquals(Empleado expected, Empleado actual) {
        assertThat(expected)
            .as("Verify Empleado relevant properties")
            .satisfies(e -> assertThat(e.getDni()).as("check dni").isEqualTo(actual.getDni()))
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getActivo()).as("check activo").isEqualTo(actual.getActivo()))
            .satisfies(e -> assertThat(e.getNumeroVentas()).as("check numeroVentas").isEqualTo(actual.getNumeroVentas()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadoUpdatableRelationshipsEquals(Empleado expected, Empleado actual) {}
}