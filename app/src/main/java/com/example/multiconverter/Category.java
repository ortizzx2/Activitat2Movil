package com.example.multiconverter;

import java.util.Arrays;
import java.util.List;

/**
 * Every conversion category and its units. This table is the only place to edit
 * when adding a unit or a category. The comment above each entry names its base unit.
 *
 * The first text of each entry is its key: the exact text Activity 1 sends in the Intent.
 */
public enum Category {

    // Base: metre
    DISTANCE("Distancia", "Distance",
            new Unit("Millimetres", 0.001),
            new Unit("Centimetres", 0.01),
            new Unit("Metres", 1),
            new Unit("Kilometres", 1000),
            new Unit("Inches", 0.0254),
            new Unit("Feet", 0.3048),
            new Unit("Yards", 0.9144),
            new Unit("Miles", 1609.344),
            new Unit("Nautical miles", 1852)),

    // Base: degree Celsius. Only category that allows negative values, down to absolute zero.
    TEMPERATURE("Temperatura", "Temperature", -273.15, "The temperature cannot be below absolute zero.", null,
            new Unit("Celsius", 1),
            new Unit("Fahrenheit", 5.0 / 9.0, -32),
            new Unit("Kelvin", 1, -273.15)),

    // Base: litre
    VOLUME("Volumen", "Volume",
            new Unit("Millilitres", 0.001),
            new Unit("Litres", 1),
            new Unit("Cubic metres", 1000),
            new Unit("US fluid ounces", 0.0295735295625),
            new Unit("US cups", 0.2365882365),
            new Unit("US pints", 0.473176473),
            new Unit("US gallons", 3.785411784),
            new Unit("UK gallons", 4.54609)),

    // Base: gram
    MASS("Masa", "Mass and Weight",
            new Unit("Milligrams", 0.001),
            new Unit("Grams", 1),
            new Unit("Kilograms", 1000),
            new Unit("Tonnes", 1000000),
            new Unit("Ounces", 28.349523125),
            new Unit("Pounds", 453.59237),
            new Unit("Stones", 6350.29318)),

    // Base: second
    TIME("Tiempo", "Time",
            new Unit("Milliseconds", 0.001),
            new Unit("Seconds", 1),
            new Unit("Minutes", 60),
            new Unit("Hours", 3600),
            new Unit("Days", 86400),
            new Unit("Weeks", 604800),
            new Unit("Years (365 days)", 31536000)),

    // Base: byte
    DIGITAL("Información digital", "Digital Information", "1 kilobyte = 1024 bytes.",
            new Unit("Bits", 0.125),
            new Unit("Bytes", 1),
            new Unit("Kilobytes", 1024),
            new Unit("Megabytes", 1048576),
            new Unit("Gigabytes", 1073741824),
            new Unit("Terabytes", 1099511627776.0)),

    // Base: euro. Factor = value of one unit of the currency in euros.
    CURRENCY("Moneda", "Currency", "Sample exchange rates for demonstration only. They are fixed and never updated.",
            new Unit("Euros", 1),
            new Unit("US dollars", 0.90),
            new Unit("British pounds", 1.15),
            new Unit("Swiss francs", 1.05),
            new Unit("Japanese yen", 0.0060)),

    // Base: square metre
    AREA("Área", "Area",
            new Unit("Square centimetres", 0.0001),
            new Unit("Square metres", 1),
            new Unit("Square feet", 0.09290304),
            new Unit("Square yards", 0.83612736),
            new Unit("Acres", 4046.8564224),
            new Unit("Hectares", 10000),
            new Unit("Square kilometres", 1000000),
            new Unit("Square miles", 2589988.110336)),

    // Base: metre per second
    SPEED("Velocidad", "Speed",
            new Unit("Metres per second", 1),
            new Unit("Kilometres per hour", 1 / 3.6),
            new Unit("Miles per hour", 0.44704),
            new Unit("Feet per second", 0.3048),
            new Unit("Knots", 1852.0 / 3600.0)),

    // Base: pascal
    PRESSURE("Presión", "Pressure",
            new Unit("Pascals", 1),
            new Unit("Hectopascals", 100),
            new Unit("Kilopascals", 1000),
            new Unit("Bars", 100000),
            new Unit("Atmospheres", 101325),
            new Unit("PSI", 6894.757293168),
            new Unit("mmHg", 133.322387415));

    /** Absorbs rounding errors when a value is exactly at the minimum, such as -459.67 F. */
    private static final double TOLERANCE = 1e-9;

    private final String key;
    private final String label;
    private final double minimumBase;
    private final String rangeError;
    private final String note;
    private final List<Unit> units;

    Category(String key, String label, Unit... units) {
        this(key, label, null, units);
    }

    Category(String key, String label, String note, Unit... units) {
        this(key, label, 0, "The value cannot be negative.", note, units);
    }

    Category(String key, String label, double minimumBase, String rangeError, String note, Unit... units) {
        this.key = key;
        this.label = label;
        this.minimumBase = minimumBase;
        this.rangeError = rangeError;
        this.note = note;
        this.units = Arrays.asList(units);
    }

    /** Returns the category with that key, or null if there is none. */
    public static Category fromKey(String key) {
        for (Category category : values()) {
            if (category.key.equals(key)) {
                return category;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public String getRangeError() {
        return rangeError;
    }

    /** Extra information to show with this category, or null if it has none. */
    public String getNote() {
        return note;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public boolean isInRange(double value, Unit unit) {
        return unit.toBase(value) >= minimumBase - TOLERANCE;
    }
}
