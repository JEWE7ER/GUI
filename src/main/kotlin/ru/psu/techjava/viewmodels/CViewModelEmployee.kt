package ru.psu.techjava.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import ru.psu.techjava.model.CEmployee
import ru.psu.techjava.services.CServiceDepartment
import tornadofx.*

/********************************************************************************************************
 * Модель представления для одного сотрудника.                                                          *
 * @author Панин А.Р. 2022 1128.                                                                        *
 ********************************************************************************************************/
class CViewModelEmployee(employee: CEmployee): ItemViewModel<CEmployee>(employee){
    private val viewModelPositionList: CViewModelPositionList by inject()
    private val viewModelDepartmentList: CViewModelDepartmentList by inject()
    private val serviceDepartment: CServiceDepartment by inject()
    //var tempPosID: String = ""
    val id = bind(CEmployee::propertyId)
    val name = bind(CEmployee::propertyName)
    val birthday = bind(CEmployee::propertyBirthday)
    val coefficient = bind(CEmployee::propertyCoefficient)
    var posID = bind(CEmployee::propertyPos)
    var posName = bind{getPosName().toProperty()}

    var newPosName = bind{SimpleStringProperty()}
    val change = SimpleBooleanProperty(false)
    val changeDepHead = SimpleBooleanProperty(false)
    /****************************************************************************************************
     * Сохранение данных из полей формы в объект в оперативной памяти.                                  *
     ***************************************************************************************************/
    fun save() {
        this.commit() //Сохранение данных из полей на форме в сущность
        //serviceEmployee.save(this.item)//Передача данных на сервер (изменили на кэш только)
    }
    /****************************************************************************************************
     * Найти навзание должности из списка моедли представления                                          *
     * на основе информации об ID должности у объекта.                                                  *
     ****************************************************************************************************/
    private fun getPosName(): String{
        var temp = messages["None"]
        for(position in viewModelPositionList.positions){
            if(this.posID.value==position.id){
                temp=position.name
            }
        }
        return temp
    }
    /****************************************************************************************************
     * Проверить новое навзание должности из списка моедли представления,                               *
     * полученное  из combobox.                                                                         *
     ****************************************************************************************************/
    fun getNewPosName(posNameStr: String?) {
        if(posNameStr!=null) {
            change.set(false)
            for (position in viewModelPositionList.positions) {
                if ((position.name == posNameStr) and (this.posName.value != posNameStr)) {
                    this.newPosName.value = posNameStr.toString()
                    change.set(true)
                    break
                }
            }
        }
    }
    /****************************************************************************************************
     * Заменить старый ID должности на новый.                                                           *
     ****************************************************************************************************/
    fun changePosID() {
        for (position in viewModelPositionList.positions) {
            if (position.name == this.posName.value) {
                this.posID.value = position.id// = position.id.toProperty()
                if (position.markerDepHead == 1){
                    val depHead = CEmployee(this.id.value.value,
                        this.name.value,this.birthday.value,this.coefficient.value,this.posID.value)
                    for (departemnt in viewModelDepartmentList.departments)
                        if(position.dep==departemnt.id) {
                            departemnt.employee = depHead
                            serviceDepartment.save(departemnt)
                            changeDepHead.set(true)
                        }
                }
                break
            }
        }
    }
    /****************************************************************************************************
     * Заменить пустую должность на любую не руководящую.                                               *
     ****************************************************************************************************/
    fun randomName(): MutableList<String> {
        val temp = mutableListOf<String>()
        for (position in viewModelPositionList.positions){
            if (position.markerDepHead==0)
                temp.add(position.name)
        }
        return temp
    }
}