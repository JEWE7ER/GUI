package ru.psu.techjava.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.util.*
import javax.json.JsonObject

class CPosition (id: UUID? = null, name: String = "",
                 salary: Double = 0.0, markerDepHead: Int = 0,
                 dep: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")): JsonModel {

    val propertyId = SimpleObjectProperty(id)
    var id by propertyId

    val propertyName = SimpleStringProperty(name)
    var name by propertyName

    val propertySalary = SimpleDoubleProperty(salary)
    var salary by propertySalary

    val propertyMarkerDepHead = SimpleIntegerProperty(markerDepHead)
    var markerDepHead by propertyMarkerDepHead

    val propertyDep = SimpleObjectProperty(dep)
    var dep by propertyDep

    override fun updateModel(json: JsonObject) {
        with(json) {
            id = UUID.fromString(string("id"))
            name = string("name")
            salary = double("salary")!!
            markerDepHead = int("markerDepHead")!!
            dep = UUID.fromString(getJsonObject("department").string("id"))
        }
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("id", id)
            add("name", name)
            add("salary", salary)
            add("markerDepHead", markerDepHead)
            add("department", JsonBuilder().add("id", dep))
        }
    }
    /****************************************************************************************************
     * Проверка на соответствие двух объектов друг другу по идентификтаорам.                            *
     * @param other - другой обект для проверки.                                                        *
     * @return true, если объект описывает туже сущность.                                               *
     ***************************************************************************************************/
    override fun equals(other: Any?) = (other is CPosition)
            && id == other.id
    /****************************************************************************************************
     * Проверка наличия изменений в объекте по сравнению с другим объектом.                             *
     * @param other - другой обект для проверки.                                                        *
     * @return true, если объект описывает ту же сущность, но имеет отличия в полях.                    *
     ***************************************************************************************************/
    fun hasChanges(other: CPosition) = equals(other)
            //Совсем другой объект не считаем изменённым текущим.
            && ( //Изменения в любом другом поле считаем изменениями в объекте.
                    name != other.name ||
                    salary != other.salary ||
                    markerDepHead != other.markerDepHead ||
                    dep != other.dep
            )
}