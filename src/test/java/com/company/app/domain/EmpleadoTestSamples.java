package com.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Empleado getEmpleadoSample1() {
        return new Empleado().id(1L).dni("dni1").nombre("nombre1").numeroVentas(1);
    }

    public static Empleado getEmpleadoSample2() {
        return new Empleado().id(2L).dni("dni2").nombre("nombre2").numeroVentas(2);
    }

    public static Empleado getEmpleadoRandomSampleGenerator() {
        return new Empleado()
            .id(longCount.incrementAndGet())
            .dni(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .numeroVentas(intCount.incrementAndGet());
    }
}
