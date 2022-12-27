package ru.psu.techjava.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.util.*
import javax.json.JsonObject

class CDepartment (id: UUID? = null, name: String = "", employee: CEmployee? = null): JsonModel
{
    val propertyId = SimpleObjectProperty(id)
    var id by propertyId

    val propertyName = SimpleStringProperty(name)
    var name by propertyName

    val propertyEmployee = SimpleObjectProperty(employee)
    var employee by propertyEmployee

    override fun updateModel(json: JsonObject){
        //val list = json.getJsonArray("position")
        with(json) {
            id = UUID.fromString(string("id"))
            name = string("name")
            employee = getJsonObject("depHead").toModel()
        }
    }

    override fun toJSON(json: JsonBuilder){
        with(json) {
            add("id", id.toString())
            add("name", name)
            add("depHead", employee?.toJSON())
        }
    }
    /****************************************************************************************************
     * Проверка на соответствие двух объектов друг другу по идентификтаорам.                            *
     * @param other - другой обект для проверки.                                                        *
     * @return true, если объект описывает туже сущность.                                               *
     ***************************************************************************************************/
    override fun equals(other: Any?) = (other is CDepartment)
            && id == other.id
    /****************************************************************************************************
     * Проверка наличия изменений в объекте по сравнению с другим объектом.                             *
     * @param other - другой обект для проверки.                                                        *
     * @return true, если объект описывает ту же сущность, но имеет отличия в полях.                    *
     ***************************************************************************************************/
    fun hasChanges(other: CDepartment) = equals(other)
            //Совсем другой объект не считаем изменённым текущим.
                && ( //Изменения в любом другом поле считаем изменениями в объекте.
                    name != other.name ||
                    employee != other.employee
                )

}