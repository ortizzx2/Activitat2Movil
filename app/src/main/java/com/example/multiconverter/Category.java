package com.example.multiconverter;

import java.util.Arrays;
import java.util.List;

/**
 * Every conversion category and its units. This table is the only place to edit
 * when adding a unit or a category. The comment above each entry names its base unit.
 *
 * The first text of each entry is its label: the title of the screen and also the
 * exact text Activity 1 sends in the Intent.
 */
public enum Category {

    // Base: metre
    DISTANCE("Distancia",
            new Unit("Milímetros", 0.001),
            new Unit("Centímetros", 0.01),
            new Unit("Metros", 1),
            new Unit("Kilómetros", 1000),
            new Unit("Pulgadas", 0.0254),
            new Unit("Pies", 0.3048),
            new Unit("Yardas", 0.9144),
            new Unit("Millas", 1609.344),
            new Unit("Millas náuticas", 1852)),

    // Base: degree Celsius. Only category that allows negative values, down to absolute zero.
    TEMPERATURE("Temperatura", -273.15, "La temperatura no puede ser inferior al cero absoluto.", null,
            new Unit("Celsius", 1),
            new Unit("Fahrenheit", 5.0 / 9.0, -32),
            new Unit("Kelvin", 1, -273.15)),

    // Base: litre
    VOLUME("Volumen",
            new Unit("Mililitros", 0.001),
            new Unit("Litros", 1),
            new Unit("Metros cúbicos", 1000),
            new Unit("Onzas líquidas (EE. UU.)", 0.0295735295625),
            new Unit("Tazas (EE. UU.)", 0.2365882365),
            new Unit("Pintas (EE. UU.)", 0.473176473),
            new Unit("Galones (EE. UU.)", 3.785411784),
            new Unit("Galones (R. U.)", 4.54609)),

    // Base: gram
    MASS("Masa",
            new Unit("Miligramos", 0.001),
            new Unit("Gramos", 1),
            new Unit("Kilogramos", 1000),
            new Unit("Toneladas", 1000000),
            new Unit("Onzas", 28.349523125),
            new Unit("Libras", 453.59237),
            new Unit("Stones", 6350.29318)),

    // Base: second
    TIME("Tiempo",
            new Unit("Milisegundos", 0.001),
            new Unit("Segundos", 1),
            new Unit("Minutos", 60),
            new Unit("Horas", 3600),
            new Unit("Días", 86400),
            new Unit("Semanas", 604800),
            new Unit("Años (365 días)", 31536000)),

    // Base: byte
    DIGITAL("Información digital", "1 kilobyte = 1024 bytes.",
            new Unit("Bits", 0.125),
            new Unit("Bytes", 1),
            new Unit("Kilobytes", 1024),
            new Unit("Megabytes", 1048576),
            new Unit("Gigabytes", 1073741824),
            new Unit("Terabytes", 1099511627776.0)),

    // Base: euro. Factor = value of one unit of the currency in euros.
    CURRENCY("Moneda", "Tipos de cambio de ejemplo, solo para demostración. Son fijos y no se actualizan.",
            new Unit("Euros", 1),
            new Unit("Dólares estadounidenses", 0.90),
            new Unit("Libras esterlinas", 1.15),
            new Unit("Francos suizos", 1.05),
            new Unit("Yenes japoneses", 0.0060)),

    // Base: square metre
    AREA("Área",
            new Unit("Centímetros cuadrados", 0.0001),
            new Unit("Metros cuadrados", 1),
            new Unit("Pies cuadrados", 0.09290304),
            new Unit("Yardas cuadradas", 0.83612736),
            new Unit("Acres", 4046.8564224),
            new Unit("Hectáreas", 10000),
            new Unit("Kilómetros cuadrados", 1000000),
            new Unit("Millas cuadradas", 2589988.110336)),

    // Base: metre per second
    SPEED("Velocidad",
            new Unit("Metros por segundo", 1),
            new Unit("Kilómetros por hora", 1 / 3.6),
            new Unit("Millas por hora", 0.44704),
            new Unit("Pies por segundo", 0.3048),
            new Unit("Nudos", 1852.0 / 3600.0)),

    // Base: pascal
    PRESSURE("Presión",
            new Unit("Pascales", 1),
            new Unit("Hectopascales", 100),
            new Unit("Kilopascales", 1000),
            new Unit("Bares", 100000),
            new Unit("Atmósferas", 101325),
            new Unit("PSI", 6894.757293168),
            new Unit("mmHg", 133.322387415));

    /** Absorbs rounding errors when a value is exactly at the minimum, such as -459.67 F. */
    private static final double TOLERANCE = 1e-9;

    private final String label;
    private final double minimumBase;
    private final String rangeError;
    private final String note;
    private final List<Unit> units;

    Category(String label, Unit... units) {
        this(label, null, units);
    }

    Category(String label, String note, Unit... units) {
        this(label, 0, "El valor no puede ser negativo.", note, units);
    }

    Category(String label, double minimumBase, String rangeError, String note, Unit... units) {
        this.label = label;
        this.minimumBase = minimumBase;
        this.rangeError = rangeError;
        this.note = note;
        this.units = Arrays.asList(units);
    }

    /** Returns the category with that label, or null if there is none. */
    public static Category fromLabel(String label) {
        for (Category category : values()) {
            if (category.label.equals(label)) {
                return category;
            }
        }
        return null;
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
