package com.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CocheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Coche getCocheSample1() {
        return new Coche().id(1L).color("color1").numeroSerie("numeroSerie1");
    }

    public static Coche getCocheSample2() {
        return new Coche().id(2L).color("color2").numeroSerie("numeroSerie2");
    }

    public static Coche getCocheRandomSampleGenerator() {
        return new Coche().id(longCount.incrementAndGet()).color(UUID.randomUUID().toString()).numeroSerie(UUID.randomUUID().toString());
    }
}
