package ru.psu.techjava.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.psu.techjava.model.CPosition
import ru.psu.techjava.services.CServicePosition
import tornadofx.Controller
/********************************************************************************************************
 * Модель представления для списка должностей.                                                          *
 * @author Панин А.Р. 2022 1128.                                                                        *
 ********************************************************************************************************/
class CViewModelPositionList: Controller() {
    //Сервис с логикой для работы со списком сотрудников.
    private val servicePosition: CServicePosition by inject()
    //Список сотрудников с возможность отслеживания изменений.
    val positions = servicePosition.getAll()
    //Факт наличия выделения в таблице.
    //От него зависит активность некоторых кнопок.
    val elementSelected = SimpleBooleanProperty(false)
    var globalChanges = SimpleBooleanProperty(false)
    //Модель представления для списка в целом.
    private val viewModelDepartmentList: CViewModelDepartmentList by inject()
    //Список названий отделов.
    val nameDepartmentList = getDepartmentListName()
    /****************************************************************************************************
     * Добавление пустой записи в таблицу.                                                              *
     ***************************************************************************************************/
    fun add() {
        servicePosition.add(CPosition()) //Передача данных на сервер
    }
    /****************************************************************************************************
     * Удаление записи из списка.                                                                       *
     * @param item - элемент для удаления.                                                              *
     ***************************************************************************************************/
    fun delete(item: CPosition?) {
        //Если элемент не указан - ничего не делаем.
        item ?: return
        servicePosition.delete(item) //Передача данных на сервер
    }

    /****************************************************************************************************
     * Обработка изменения выделенных элементов в списке.                                               *
     * @param selectedItem - выделенный элемент.                                                        *
     ***************************************************************************************************/
    fun onSelectionChange(selectedItem: CPosition?) {
        elementSelected.set(selectedItem != null)
    }
    /****************************************************************************************************
     * Отправка всех текущих данных из списка на сервер.                                                *
     ***************************************************************************************************/
    fun saveAll() {
        servicePosition.saveAll() //Передача команды на отправку данных на сервер
    }
    /****************************************************************************************************
     * Взять все доступные названия отедлов из списка модели представления.                             *
     ****************************************************************************************************/
    private fun getDepartmentListName(): ObservableList<String> {
        val temp = FXCollections.observableArrayList<String>()
        for (department in viewModelDepartmentList.departments){
            temp.add(department.name)
        }
        return temp
    }
}