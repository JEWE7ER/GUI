package ru.psu.techjava.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.json.JsonObject
import tornadofx.JsonBuilder

/********************************************************************************************************
 * Сотрудник.                                                                                           *
 * @author Панин А.Р. 2022 1128.                                                                        *
 ********************************************************************************************************/
//val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")


class CEmployee(id: UUID? = null, name: String = "",
                birthday: LocalDate = LocalDate.parse("01.01.1978", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                coefficient: Double = 1.0,
                pos: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")): JsonModel
{
    val propertyId = SimpleObjectProperty(id)
    var id by propertyId

    val propertyName = SimpleStringProperty(name)
    var name by propertyName

    val propertyBirthday = SimpleObjectProperty(birthday)
    var birthday by propertyBirthday

    val propertyCoefficient = SimpleDoubleProperty(coefficient)
    var coefficient by propertyCoefficient

    val propertyPos = SimpleObjectProperty(pos)
    var pos by propertyPos

    override fun updateModel(json: JsonObject){
        with(json) {
            id = UUID.fromString(string("id"))
            name = string("name")
            birthday = LocalDate.parse(string("birthday"))
            coefficient = double("coefficient")!!
            pos = UUID.fromString(getJsonObject("position").string("id"))
        }
    }

    override fun toJSON(json: JsonBuilder){
        with(json) {
            add("id", id.toString())
            add("name", name)
            add("birthday", birthday.toString())
            add("coefficient", coefficient)
            add("position", JsonBuilder().add("id", pos))
        }
    }
    /****************************************************************************************************
     * Проверка на соответствие двух объектов друг другу по идентификтаорам.                            *
     * @param other - другой обект для проверки.                                                        *
     * @return true, если объект описывает туже сущность.                                               *
     ***************************************************************************************************/
    override fun equals(other: Any?) = (other is CEmployee)
                                && id == other.id
    /****************************************************************************************************
     * Проверка наличия изменений в объекте по сравнению с другим объектом.                             *
     * @param other - другой обект для проверки.                                                        *
     * @return true, если объект описывает ту же сущность, но имеет отличия в полях.                    *
     ***************************************************************************************************/
    fun hasChanges(other: CEmployee) = equals(other)
            //Совсем другой объект не считаем изменённым текущим.
                                && ( //Изменения в любом другом поле считаем изменениями в объекте.
                                    name != other.name ||
                                    birthday != other.birthday ||
                                    coefficient != other.coefficient ||
                                    pos != other.pos
                                    )
}

