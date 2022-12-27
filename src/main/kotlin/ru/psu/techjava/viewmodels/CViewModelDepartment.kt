package ru.psu.techjava.viewmodels

import ru.psu.techjava.model.CDepartment
import tornadofx.ItemViewModel

class CViewModelDepartment(department: CDepartment) : ItemViewModel<CDepartment>(department){
    //private val serviceDepartment: CServiceDepartment by inject()
    val id = bind(CDepartment::propertyId)
    val name = bind(CDepartment::propertyName)
    //val depHead = bind(CDepartment::propertyEmployee)
    /****************************************************************************************************
     * Сохранение данных из полей формы в объект в оперативной памяти.                                  *
     ***************************************************************************************************/
    fun save() {
        this.commit() //Сохранение данных из полей на форме в сущность
        //serviceDepartment.save(this.item)//Передача данных на сервер (изменили на кэш только)
    }
}