package com.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ModeloTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Modelo getModeloSample1() {
        return new Modelo().id(1L).nombre("nombre1");
    }

    public static Modelo getModeloSample2() {
        return new Modelo().id(2L).nombre("nombre2");
    }

    public static Modelo getModeloRandomSampleGenerator() {
        return new Modelo().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString());
    }
}
