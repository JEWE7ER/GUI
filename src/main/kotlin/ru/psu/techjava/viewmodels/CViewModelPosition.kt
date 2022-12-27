package ru.psu.techjava.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import ru.psu.techjava.model.CPosition
import tornadofx.ItemViewModel
import tornadofx.get
import tornadofx.toProperty

class CViewModelPosition(position: CPosition): ItemViewModel<CPosition>(position){
    private val viewModelDepartmentList: CViewModelDepartmentList by inject()
    val id = bind(CPosition::propertyId)
    val name = bind(CPosition::propertyName)
    val salary = bind(CPosition::propertySalary)
    val markerDepHead = bind(CPosition::propertyMarkerDepHead)
    var depID = bind(CPosition::propertyDep)
    var depName = bind{getDepName().toProperty()}

    var newDepName = bind{ SimpleStringProperty() }
    val change = SimpleBooleanProperty(false)
    var marker = bind{getMarkerStr().toProperty()}
    /****************************************************************************************************
     * Сохранение данных из полей формы в объект в оперативной памяти.                                  *
     ***************************************************************************************************/
    fun save() {
        this.commit() //Сохранение данных из полей на форме в сущность
        //servicePosition.save(this.item)//Передача данных на сервер (изменили на кэш только)
    }
    private fun getMarkerStr(): String? {
        if (this.markerDepHead.value==1)
            return messages["Yes"]
        else return messages["No"]
    }
    /****************************************************************************************************
     * Найти навзание отдела из списка моедли представления                                             *
     * на основе информации об ID отдела у объекта.                                                     *
     ****************************************************************************************************/
    private fun getDepName(): String{
        var temp = ""
        for(department in viewModelDepartmentList.departments){
            if(this.depID.value==department.id){
                temp=department.name
            }
        }
        return temp
    }
    /****************************************************************************************************
     * Проверить новое навзание отдела из списка моедли представления,                                  *
     * полученное  из combobox.                                                                         *
     ****************************************************************************************************/
    fun getNewDepName(depNameStr: String?) {
        if(depNameStr!=null) {
            change.set(false)
            for (department in viewModelDepartmentList.departments) {
                if ((department.name == depNameStr) and (this.depName.value != depNameStr)) {
                    //newDepName = depNameStr.toString()
                    change.set(true)
                    break
                }
            }
        }
    }
    /****************************************************************************************************
     * Заменить старый ID отдела на новый.                                                              *
     ****************************************************************************************************/
    fun changeDepID() {
        for (department in viewModelDepartmentList.departments) {
            if (department.name == this.depName.value) {
                this.depID.value = department.id// = position.id.toProperty()
                break
            }
        }
    }
}