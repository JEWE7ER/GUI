package ru.psu.techjava.util.convertes

import javafx.util.StringConverter
import java.time.LocalDate

object CConverterDate: StringConverter<LocalDate>() {
    override fun toString(item: LocalDate?)
                        = item?.toString() ?: ""
    override fun fromString(string: String?): LocalDate?{
                        if (string == null) return null
                        if (string.length!=10) return null
                        return try {
                            LocalDate.parse(string)
                        }
                        catch (e: Exception){
                            null
                        }
    }
}