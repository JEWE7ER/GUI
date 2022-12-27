package ru.psu.techjava.util.convertes

import javafx.util.converter.NumberStringConverter

object CConverterCoefficient: NumberStringConverter() {
    override fun toString(item: Number?): String{
        if (item == null) {
            return ""
        }
        val formatter = numberFormat
        return formatter.format(item)
    }

    override fun fromString(string: String?): Double? {
        if (string == null) return null
        if (string.toDouble() < 1.0) return null
        string.trim()
        return try {
            string.toDouble()
        } catch (e: Exception) {
            null
        }
    }
}