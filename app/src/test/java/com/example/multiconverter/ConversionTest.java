package com.example.multiconverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConversionTest {

    private static final double DELTA = 1e-6;

    private static Unit unit(Category category, String name) {
        for (Unit unit : category.getUnits()) {
            if (unit.getName().equals(name)) {
                return unit;
            }
        }
        throw new AssertionError("Unknown unit: " + name);
    }

    private static double convert(Category category, double value, String from, String to) {
        return unit(category, from).convertTo(value, unit(category, to));
    }

    @Test
    public void linearConversions() {
        assertEquals(500000, convert(Category.DISTANCE, 5, "Kilómetros", "Centímetros"), DELTA);
        assertEquals(0.621371, convert(Category.DISTANCE, 1, "Kilómetros", "Millas"), DELTA);
        assertEquals(2.204623, convert(Category.MASS, 1, "Kilogramos", "Libras"), DELTA);
        assertEquals(1024, convert(Category.DIGITAL, 1, "Gigabytes", "Megabytes"), DELTA);
        assertEquals(1.013250, convert(Category.PRESSURE, 1, "Atmósferas", "Bares"), DELTA);
        assertEquals(36, convert(Category.SPEED, 10, "Metros por segundo", "Kilómetros por hora"), DELTA);
        assertEquals(2.471054, convert(Category.AREA, 1, "Hectáreas", "Acres"), DELTA);
    }

    @Test
    public void temperatureUsesScaleAndOffset() {
        assertEquals(212, convert(Category.TEMPERATURE, 100, "Celsius", "Fahrenheit"), DELTA);
        assertEquals(273.15, convert(Category.TEMPERATURE, 32, "Fahrenheit", "Kelvin"), DELTA);
        assertEquals(-40, convert(Category.TEMPERATURE, -40, "Fahrenheit", "Celsius"), DELTA);
        assertEquals(-273.15, convert(Category.TEMPERATURE, 0, "Kelvin", "Celsius"), DELTA);
    }

    @Test
    public void everyCategoryHasTenUnitsExceptTemperature() {
        for (Category category : Category.values()) {
            int expected = category == Category.TEMPERATURE ? 3 : 10;
            assertEquals(category.getLabel(), expected, category.getUnits().size());
        }
    }

    @Test
    public void commonUnitsConvertAsExpected() {
        assertEquals(1, convert(Category.MASS, 10, "Decagramos", "Hectogramos"), DELTA);
        assertEquals(3, convert(Category.VOLUME, 1, "Cucharadas (15 ml)", "Cucharaditas (5 ml)"), DELTA);
        assertEquals(100, convert(Category.TIME, 1, "Siglos", "Años (365 días)"), DELTA);
        assertEquals(14.223343, convert(Category.PRESSURE, 1,
                "Kilogramos por centímetro cuadrado", "PSI (libras por pulgada cuadrada)"), DELTA);
    }

    @Test
    public void everyUnitPairRoundTrips() {
        for (Category category : Category.values()) {
            for (Unit from : category.getUnits()) {
                for (Unit to : category.getUnits()) {
                    double there = from.convertTo(123.456, to);
                    assertEquals(category + ": " + from.getName() + " to " + to.getName(),
                            123.456, to.convertTo(there, from), 1e-6);
                }
            }
        }
    }

    @Test
    public void rangeDependsOnCategory() {
        assertFalse(Category.DISTANCE.isInRange(-1, unit(Category.DISTANCE, "Metros")));
        assertTrue(Category.DISTANCE.isInRange(0, unit(Category.DISTANCE, "Metros")));
        assertTrue(Category.TEMPERATURE.isInRange(-40, unit(Category.TEMPERATURE, "Celsius")));
        assertFalse(Category.TEMPERATURE.isInRange(-300, unit(Category.TEMPERATURE, "Celsius")));
        assertFalse(Category.TEMPERATURE.isInRange(-1, unit(Category.TEMPERATURE, "Kelvin")));
        assertTrue(Category.TEMPERATURE.isInRange(0, unit(Category.TEMPERATURE, "Kelvin")));
        assertTrue(Category.TEMPERATURE.isInRange(-459.67, unit(Category.TEMPERATURE, "Fahrenheit")));
    }

    @Test
    public void fromLabelFindsTheCategoriesSentByActivityOne() {
        assertSame(Category.DISTANCE, Category.fromLabel("Distancia"));
        assertSame(Category.TEMPERATURE, Category.fromLabel("Temperatura"));
        assertSame(Category.MASS, Category.fromLabel("Masa"));
        assertSame(Category.TIME, Category.fromLabel("Tiempo"));
        assertNull(Category.fromLabel("distancia"));
        assertNull(Category.fromLabel("DISTANCE"));
        assertNull(Category.fromLabel(null));
    }
}
