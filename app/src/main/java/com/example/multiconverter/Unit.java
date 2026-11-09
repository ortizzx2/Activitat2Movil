package com.example.multiconverter;

/**
 * A unit of measurement, described by how it relates to the base unit of its category:
 *
 *     base = (value + offset) * factor
 *
 * Linear units only need a factor (1 km = 1000 m). Temperature scales also need an
 * offset because they do not share the same zero. The same two lines of code cover
 * every unit, so adding one never requires a new if or switch.
 */
public final class Unit {

    private final String name;
    private final double factor;
    private final double offset;

    public Unit(String name, double factor) {
        this(name, factor, 0);
    }

    public Unit(String name, double factor, double offset) {
        this.name = name;
        this.factor = factor;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public double toBase(double value) {
        return (value + offset) * factor;
    }

    public double fromBase(double base) {
        return base / factor - offset;
    }

    public double convertTo(double value, Unit target) {
        return target.fromBase(toBase(value));
    }
}
