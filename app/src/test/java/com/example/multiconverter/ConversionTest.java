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
        assertEquals(500000, convert(Category.DISTANCE, 5, "Kilometres", "Centimetres"), DELTA);
        assertEquals(0.621371, convert(Category.DISTANCE, 1, "Kilometres", "Miles"), DELTA);
        assertEquals(2.204623, convert(Category.MASS, 1, "Kilograms", "Pounds"), DELTA);
        assertEquals(1024, convert(Category.DIGITAL, 1, "Gigabytes", "Megabytes"), DELTA);
        assertEquals(1.013250, convert(Category.PRESSURE, 1, "Atmospheres", "Bars"), DELTA);
        assertEquals(36, convert(Category.SPEED, 10, "Metres per second", "Kilometres per hour"), DELTA);
        assertEquals(2.471054, convert(Category.AREA, 1, "Hectares", "Acres"), DELTA);
    }

    @Test
    public void temperatureUsesScaleAndOffset() {
        assertEquals(212, convert(Category.TEMPERATURE, 100, "Celsius", "Fahrenheit"), DELTA);
        assertEquals(273.15, convert(Category.TEMPERATURE, 32, "Fahrenheit", "Kelvin"), DELTA);
        assertEquals(-40, convert(Category.TEMPERATURE, -40, "Fahrenheit", "Celsius"), DELTA);
        assertEquals(-273.15, convert(Category.TEMPERATURE, 0, "Kelvin", "Celsius"), DELTA);
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
        assertFalse(Category.DISTANCE.isInRange(-1, unit(Category.DISTANCE, "Metres")));
        assertTrue(Category.DISTANCE.isInRange(0, unit(Category.DISTANCE, "Metres")));
        assertTrue(Category.TEMPERATURE.isInRange(-40, unit(Category.TEMPERATURE, "Celsius")));
        assertFalse(Category.TEMPERATURE.isInRange(-300, unit(Category.TEMPERATURE, "Celsius")));
        assertFalse(Category.TEMPERATURE.isInRange(-1, unit(Category.TEMPERATURE, "Kelvin")));
        assertTrue(Category.TEMPERATURE.isInRange(0, unit(Category.TEMPERATURE, "Kelvin")));
        assertTrue(Category.TEMPERATURE.isInRange(-459.67, unit(Category.TEMPERATURE, "Fahrenheit")));
    }

    @Test
    public void fromNameFindsKnownCategoriesOnly() {
        assertSame(Category.AREA, Category.fromName("AREA"));
        assertNull(Category.fromName("NOPE"));
        assertNull(Category.fromName(null));
    }
}
