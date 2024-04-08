package com.company.app.domain;

import static com.company.app.domain.CocheTestSamples.*;
import static com.company.app.domain.MarcaTestSamples.*;
import static com.company.app.domain.ModeloTestSamples.*;
import static com.company.app.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CocheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coche.class);
        Coche coche1 = getCocheSample1();
        Coche coche2 = new Coche();
        assertThat(coche1).isNotEqualTo(coche2);

        coche2.setId(coche1.getId());
        assertThat(coche1).isEqualTo(coche2);

        coche2 = getCocheSample2();
        assertThat(coche1).isNotEqualTo(coche2);
    }

    @Test
    void marcaTest() throws Exception {
        Coche coche = getCocheRandomSampleGenerator();
        Marca marcaBack = getMarcaRandomSampleGenerator();

        coche.setMarca(marcaBack);
        assertThat(coche.getMarca()).isEqualTo(marcaBack);

        coche.marca(null);
        assertThat(coche.getMarca()).isNull();
    }

    @Test
    void modeloTest() throws Exception {
        Coche coche = getCocheRandomSampleGenerator();
        Modelo modeloBack = getModeloRandomSampleGenerator();

        coche.setModelo(modeloBack);
        assertThat(coche.getModelo()).isEqualTo(modeloBack);

        coche.modelo(null);
        assertThat(coche.getModelo()).isNull();
    }

    @Test
    void ventaTest() throws Exception {
        Coche coche = getCocheRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        coche.setVenta(ventaBack);
        assertThat(coche.getVenta()).isEqualTo(ventaBack);
        assertThat(ventaBack.getId_coche()).isEqualTo(coche);

        coche.venta(null);
        assertThat(coche.getVenta()).isNull();
        assertThat(ventaBack.getId_coche()).isNull();
    }
}
