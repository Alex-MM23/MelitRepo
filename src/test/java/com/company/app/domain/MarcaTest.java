package com.company.app.domain;

import static com.company.app.domain.CocheTestSamples.*;
import static com.company.app.domain.MarcaTestSamples.*;
import static com.company.app.domain.ModeloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MarcaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marca.class);
        Marca marca1 = getMarcaSample1();
        Marca marca2 = new Marca();
        assertThat(marca1).isNotEqualTo(marca2);

        marca2.setId(marca1.getId());
        assertThat(marca1).isEqualTo(marca2);

        marca2 = getMarcaSample2();
        assertThat(marca1).isNotEqualTo(marca2);
    }

    @Test
    void marcaTest() throws Exception {
        Marca marca = getMarcaRandomSampleGenerator();
        Coche cocheBack = getCocheRandomSampleGenerator();

        marca.addMarca(cocheBack);
        assertThat(marca.getMarcas()).containsOnly(cocheBack);
        assertThat(cocheBack.getMarca()).isEqualTo(marca);

        marca.removeMarca(cocheBack);
        assertThat(marca.getMarcas()).doesNotContain(cocheBack);
        assertThat(cocheBack.getMarca()).isNull();

        marca.marcas(new HashSet<>(Set.of(cocheBack)));
        assertThat(marca.getMarcas()).containsOnly(cocheBack);
        assertThat(cocheBack.getMarca()).isEqualTo(marca);

        marca.setMarcas(new HashSet<>());
        assertThat(marca.getMarcas()).doesNotContain(cocheBack);
        assertThat(cocheBack.getMarca()).isNull();
    }

    @Test
    void modeloTest() throws Exception {
        Marca marca = getMarcaRandomSampleGenerator();
        Modelo modeloBack = getModeloRandomSampleGenerator();

        marca.addModelo(modeloBack);
        assertThat(marca.getModelos()).containsOnly(modeloBack);
        assertThat(modeloBack.getMarca()).isEqualTo(marca);

        marca.removeModelo(modeloBack);
        assertThat(marca.getModelos()).doesNotContain(modeloBack);
        assertThat(modeloBack.getMarca()).isNull();

        marca.modelos(new HashSet<>(Set.of(modeloBack)));
        assertThat(marca.getModelos()).containsOnly(modeloBack);
        assertThat(modeloBack.getMarca()).isEqualTo(marca);

        marca.setModelos(new HashSet<>());
        assertThat(marca.getModelos()).doesNotContain(modeloBack);
        assertThat(modeloBack.getMarca()).isNull();
    }
}
