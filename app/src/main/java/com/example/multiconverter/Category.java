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
    DISTANCE("Distancia", Negatives.NOT_ALLOWED,
            new Unit("Milímetros", 0.001),
            new Unit("Centímetros", 0.01),
            new Unit("Decímetros", 0.1),
            new Unit("Metros", 1),
            new Unit("Kilómetros", 1000),
            new Unit("Pulgadas", 0.0254),
            new Unit("Pies", 0.3048),
            new Unit("Yardas", 0.9144),
            new Unit("Millas", 1609.344),
            new Unit("Millas náuticas", 1852)),

    // Base: degree Celsius. Allows negative values, but never below absolute zero.
    // Only the three scales in real use; there are no other well-known temperature units.
    TEMPERATURE("Temperatura", Negatives.ALLOWED,
            -273.15, "La temperatura no puede ser inferior al cero absoluto.",
            new Unit("Celsius", 1),
            new Unit("Fahrenheit", 5.0 / 9.0, -32),
            new Unit("Kelvin", 1, -273.15)),

    // Base: litre
    VOLUME("Volumen", Negatives.NOT_ALLOWED,
            new Unit("Mililitros", 0.001),
            new Unit("Centilitros", 0.01),
            new Unit("Decilitros", 0.1),
            new Unit("Litros", 1),
            new Unit("Metros cúbicos", 1000),
            new Unit("Cucharaditas (5 ml)", 0.005),
            new Unit("Cucharadas (15 ml)", 0.015),
            new Unit("Tazas (250 ml)", 0.25),
            new Unit("Pintas (R. U.)", 0.56826125),
            new Unit("Galones (EE. UU.)", 3.785411784)),

    // Base: gram
    MASS("Masa", Negatives.NOT_ALLOWED,
            new Unit("Miligramos", 0.001),
            new Unit("Centigramos", 0.01),
            new Unit("Decigramos", 0.1),
            new Unit("Gramos", 1),
            new Unit("Decagramos", 10),
            new Unit("Hectogramos", 100),
            new Unit("Kilogramos", 1000),
            new Unit("Toneladas", 1000000),
            new Unit("Onzas", 28.349523125),
            new Unit("Libras", 453.59237)),

    // Base: second
    TIME("Tiempo", Negatives.NOT_ALLOWED,
            new Unit("Milisegundos", 0.001),
            new Unit("Segundos", 1),
            new Unit("Minutos", 60),
            new Unit("Horas", 3600),
            new Unit("Días", 86400),
            new Unit("Semanas", 604800),
            new Unit("Meses (30 días)", 2592000),
            new Unit("Años (365 días)", 31536000),
            new Unit("Décadas", 315360000),
            new Unit("Siglos", 3153600000.0)),

    // Base: byte
    DIGITAL("Información digital", Negatives.NOT_ALLOWED, "1 kilobyte = 1024 bytes y 1 kilobit = 1024 bits.",
            new Unit("Bits", 0.125),
            new Unit("Bytes", 1),
            new Unit("Kilobits", 128),
            new Unit("Kilobytes", 1024),
            new Unit("Megabits", 131072),
            new Unit("Megabytes", 1048576),
            new Unit("Gigabits", 134217728),
            new Unit("Gigabytes", 1073741824),
            new Unit("Terabytes", 1099511627776.0),
            new Unit("Petabytes", 1125899906842624.0)),

    // Base: euro. Factor = value of one unit of the currency in euros.
    CURRENCY("Moneda", Negatives.NOT_ALLOWED, "Tipos de cambio de ejemplo, solo para demostración. Son fijos y no se actualizan.",
            new Unit("Euros", 1),
            new Unit("Dólares estadounidenses", 0.90),
            new Unit("Libras esterlinas", 1.15),
            new Unit("Francos suizos", 1.05),
            new Unit("Yenes japoneses", 0.0060),
            new Unit("Yuanes chinos", 0.125),
            new Unit("Dólares canadienses", 0.65),
            new Unit("Dólares australianos", 0.60),
            new Unit("Pesos mexicanos", 0.050),
            new Unit("Reales brasileños", 0.16)),

    // Base: square metre
    AREA("Área", Negatives.NOT_ALLOWED,
            new Unit("Milímetros cuadrados", 0.000001),
            new Unit("Centímetros cuadrados", 0.0001),
            new Unit("Decímetros cuadrados", 0.01),
            new Unit("Metros cuadrados", 1),
            new Unit("Hectáreas", 10000),
            new Unit("Kilómetros cuadrados", 1000000),
            new Unit("Pulgadas cuadradas", 0.00064516),
            new Unit("Pies cuadrados", 0.09290304),
            new Unit("Acres", 4046.8564224),
            new Unit("Millas cuadradas", 2589988.110336)),

    // Base: metre per second
    SPEED("Velocidad", Negatives.NOT_ALLOWED,
            new Unit("Centímetros por segundo", 0.01),
            new Unit("Metros por minuto", 1.0 / 60.0),
            new Unit("Metros por segundo", 1),
            new Unit("Kilómetros por hora", 1 / 3.6),
            new Unit("Kilómetros por segundo", 1000),
            new Unit("Millas por hora", 0.44704),
            new Unit("Pies por segundo", 0.3048),
            new Unit("Nudos", 1852.0 / 3600.0),
            new Unit("Mach (velocidad del sonido)", 343),
            new Unit("Velocidad de la luz", 299792458)),

    // Base: pascal
    PRESSURE("Presión", Negatives.NOT_ALLOWED,
            new Unit("Pascales", 1),
            new Unit("Hectopascales", 100),
            new Unit("Milibares", 100),
            new Unit("Milímetros de mercurio (mmHg)", 133.322387415),
            new Unit("Kilopascales", 1000),
            new Unit("PSI (libras por pulgada cuadrada)", 6894.757293168),
            new Unit("Kilogramos por centímetro cuadrado", 98066.5),
            new Unit("Bares", 100000),
            new Unit("Atmósferas", 101325),
            new Unit("Megapascales", 1000000));

    /** Whether a category accepts values below zero. Every category above states it. */
    public enum Negatives { ALLOWED, NOT_ALLOWED }

    /** Absorbs rounding errors when a value is exactly at the minimum, such as -459.67 F. */
    private static final double TOLERANCE = 1e-9;

    private final String label;
    private final Negatives negatives;
    private final double minimumBase;
    private final String minimumError;
    private final String note;
    private final List<Unit> units;

    Category(String label, Negatives negatives, Unit... units) {
        this(label, negatives, (String) null, units);
    }

    Category(String label, Negatives negatives, String note, Unit... units) {
        this(label, negatives, note, Double.NEGATIVE_INFINITY, null, units);
    }

    /** For a category with a physical lower limit, such as absolute zero for temperature. */
    Category(String label, Negatives negatives, double minimumBase, String minimumError, Unit... units) {
        this(label, negatives, null, minimumBase, minimumError, units);
    }

    Category(String label, Negatives negatives, String note,
             double minimumBase, String minimumError, Unit... units) {
        this.label = label;
        this.negatives = negatives;
        this.note = note;
        this.minimumBase = minimumBase;
        this.minimumError = minimumError;
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

    public boolean allowsNegatives() {
        return negatives == Negatives.ALLOWED;
    }

    /** Extra information to show with this category, or null if it has none. */
    public String getNote() {
        return note;
    }

    public List<Unit> getUnits() {
        return units;
    }

    /** Returns why the value cannot be converted from that unit, or null if it can. */
    public String checkValue(double value, Unit unit) {
        if (!allowsNegatives() && value < 0) {
            return label + " no admite valores negativos.";
        }
        if (unit.toBase(value) < minimumBase - TOLERANCE) {
            return minimumError;
        }
        return null;
    }
}
