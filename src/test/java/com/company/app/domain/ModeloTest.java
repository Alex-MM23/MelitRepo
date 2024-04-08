package com.company.app.domain;

import static com.company.app.domain.CocheTestSamples.*;
import static com.company.app.domain.MarcaTestSamples.*;
import static com.company.app.domain.ModeloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ModeloTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Modelo.class);
        Modelo modelo1 = getModeloSample1();
        Modelo modelo2 = new Modelo();
        assertThat(modelo1).isNotEqualTo(modelo2);

        modelo2.setId(modelo1.getId());
        assertThat(modelo1).isEqualTo(modelo2);

        modelo2 = getModeloSample2();
        assertThat(modelo1).isNotEqualTo(modelo2);
    }

    @Test
    void marcaTest() throws Exception {
        Modelo modelo = getModeloRandomSampleGenerator();
        Marca marcaBack = getMarcaRandomSampleGenerator();

        modelo.setMarca(marcaBack);
        assertThat(modelo.getMarca()).isEqualTo(marcaBack);

        modelo.marca(null);
        assertThat(modelo.getMarca()).isNull();
    }

    @Test
    void cocheTest() throws Exception {
        Modelo modelo = getModeloRandomSampleGenerator();
        Coche cocheBack = getCocheRandomSampleGenerator();

        modelo.addCoche(cocheBack);
        assertThat(modelo.getCoches()).containsOnly(cocheBack);
        assertThat(cocheBack.getModelo()).isEqualTo(modelo);

        modelo.removeCoche(cocheBack);
        assertThat(modelo.getCoches()).doesNotContain(cocheBack);
        assertThat(cocheBack.getModelo()).isNull();

        modelo.coches(new HashSet<>(Set.of(cocheBack)));
        assertThat(modelo.getCoches()).containsOnly(cocheBack);
        assertThat(cocheBack.getModelo()).isEqualTo(modelo);

        modelo.setCoches(new HashSet<>());
        assertThat(modelo.getCoches()).doesNotContain(cocheBack);
        assertThat(cocheBack.getModelo()).isNull();
    }
}
