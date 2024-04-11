package com.company.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CocheAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCocheAllPropertiesEquals(Coche expected, Coche actual) {
        assertCocheAutoGeneratedPropertiesEquals(expected, actual);
        assertCocheAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCocheAllUpdatablePropertiesEquals(Coche expected, Coche actual) {
        assertCocheUpdatableFieldsEquals(expected, actual);
        assertCocheUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCocheAutoGeneratedPropertiesEquals(Coche expected, Coche actual) {
        assertThat(expected)
            .as("Verify Coche auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCocheUpdatableFieldsEquals(Coche expected, Coche actual) {
        assertThat(expected)
            .as("Verify Coche relevant properties")
            .satisfies(e -> assertThat(e.getColor()).as("check color").isEqualTo(actual.getColor()))
            .satisfies(e -> assertThat(e.getNumeroSerie()).as("check numeroSerie").isEqualTo(actual.getNumeroSerie()))
            .satisfies(e -> assertThat(e.getPrecio()).as("check precio").isEqualTo(actual.getPrecio()))
            .satisfies(e -> assertThat(e.getExposicion()).as("check exposicion").isEqualTo(actual.getExposicion()))
            .satisfies(e -> assertThat(e.getnPuertas()).as("check nPuertas").isEqualTo(actual.getnPuertas()))
            .satisfies(e -> assertThat(e.getMotor()).as("check motor").isEqualTo(actual.getMotor()))
            .satisfies(e -> assertThat(e.getMatricula()).as("check matricula").isEqualTo(actual.getMatricula()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCocheUpdatableRelationshipsEquals(Coche expected, Coche actual) {
        assertThat(expected)
            .as("Verify Coche relationships")
            .satisfies(e -> assertThat(e.getMarca()).as("check marca").isEqualTo(actual.getMarca()))
            .satisfies(e -> assertThat(e.getModelo()).as("check modelo").isEqualTo(actual.getModelo()));
    }
}
