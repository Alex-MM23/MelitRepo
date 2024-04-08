package com.company.app.domain;

import static com.company.app.domain.ClienteTestSamples.*;
import static com.company.app.domain.CocheTestSamples.*;
import static com.company.app.domain.EmpleadoTestSamples.*;
import static com.company.app.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venta.class);
        Venta venta1 = getVentaSample1();
        Venta venta2 = new Venta();
        assertThat(venta1).isNotEqualTo(venta2);

        venta2.setId(venta1.getId());
        assertThat(venta1).isEqualTo(venta2);

        venta2 = getVentaSample2();
        assertThat(venta1).isNotEqualTo(venta2);
    }

    @Test
    void empleadoTest() throws Exception {
        Venta venta = getVentaRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        venta.setEmpleado(empleadoBack);
        assertThat(venta.getEmpleado()).isEqualTo(empleadoBack);

        venta.empleado(null);
        assertThat(venta.getEmpleado()).isNull();
    }

    @Test
    void clienteTest() throws Exception {
        Venta venta = getVentaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        venta.setCliente(clienteBack);
        assertThat(venta.getCliente()).isEqualTo(clienteBack);

        venta.cliente(null);
        assertThat(venta.getCliente()).isNull();
    }

    @Test
    void id_cocheTest() throws Exception {
        Venta venta = getVentaRandomSampleGenerator();
        Coche cocheBack = getCocheRandomSampleGenerator();

        venta.setId_coche(cocheBack);
        assertThat(venta.getId_coche()).isEqualTo(cocheBack);

        venta.id_coche(null);
        assertThat(venta.getId_coche()).isNull();
    }
}
