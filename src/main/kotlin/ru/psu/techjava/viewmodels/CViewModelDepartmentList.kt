package ru.psu.techjava.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import ru.psu.techjava.model.CDepartment
import ru.psu.techjava.model.CPosition
import ru.psu.techjava.services.CServiceDepartment
import ru.psu.techjava.services.CServicePosition
import tornadofx.Controller

class CViewModelDepartmentList: Controller() {
    //Сервис с логикой для работы со списком сотрудников.
    private val serviceDepartment: CServiceDepartment by inject()

    //Список сотрудников с возможность отслеживания изменений.
    val departments = serviceDepartment.getAll()

    //Факт наличия выделения в таблице.
    //От него зависит активность некоторых кнопок.
    val elementSelected = SimpleBooleanProperty(false)
    var globalChanges = SimpleBooleanProperty(false)

    /****************************************************************************************************
     * Добавление пустой записи в таблицу.                                                              *
     ***************************************************************************************************/
    fun add() {
        serviceDepartment.add(CDepartment()) //Передача данных на сервер
    }

    /****************************************************************************************************
     * Удаление записи из списка.                                                                       *
     * @param item - элемент для удаления.                                                              *
     ***************************************************************************************************/
    fun delete(item: CDepartment?) {
        //Если элемент не указан - ничего не делаем.
        item ?: return
        serviceDepartment.delete(item) //Передача данных на сервер
    }

    /****************************************************************************************************
     * Обработка изменения выделенных элементов в списке.                                               *
     * @param selectedItem - выделенный элемент.                                                        *
     ***************************************************************************************************/
    fun onSelectionChange(selectedItem: CDepartment?) {
        elementSelected.set(selectedItem != null)
    }

    /****************************************************************************************************
     * Отправка всех текущих данных из списка на сервер.                                                *
     ***************************************************************************************************/
    fun saveAll() {
        serviceDepartment.saveAll() //Передача команды на отправку данных на сервер
    }
}