package com.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente().id(1L).dni("dni1").nombre("nombre1").numeroCompras(1).tier(1);
    }

    public static Cliente getClienteSample2() {
        return new Cliente().id(2L).dni("dni2").nombre("nombre2").numeroCompras(2).tier(2);
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .dni(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .numeroCompras(intCount.incrementAndGet())
            .tier(intCount.incrementAndGet());
    }
}
