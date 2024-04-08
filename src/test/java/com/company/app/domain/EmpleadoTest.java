package com.company.app.domain;

import static com.company.app.domain.EmpleadoTestSamples.*;
import static com.company.app.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empleado.class);
        Empleado empleado1 = getEmpleadoSample1();
        Empleado empleado2 = new Empleado();
        assertThat(empleado1).isNotEqualTo(empleado2);

        empleado2.setId(empleado1.getId());
        assertThat(empleado1).isEqualTo(empleado2);

        empleado2 = getEmpleadoSample2();
        assertThat(empleado1).isNotEqualTo(empleado2);
    }

    @Test
    void ventaTest() throws Exception {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        empleado.addVenta(ventaBack);
        assertThat(empleado.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getEmpleado()).isEqualTo(empleado);

        empleado.removeVenta(ventaBack);
        assertThat(empleado.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getEmpleado()).isNull();

        empleado.ventas(new HashSet<>(Set.of(ventaBack)));
        assertThat(empleado.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getEmpleado()).isEqualTo(empleado);

        empleado.setVentas(new HashSet<>());
        assertThat(empleado.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getEmpleado()).isNull();
    }
}
