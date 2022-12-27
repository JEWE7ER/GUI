package ru.psu.techjava.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.psu.techjava.model.CEmployee
import ru.psu.techjava.model.CPosition
import ru.psu.techjava.services.CServiceStaff
import tornadofx.Controller
import tornadofx.observableListOf

/********************************************************************************************************
 * Модель представления для списка сотрудников.                                                         *
 * @author Панин А.Р. 2022 1128.                                                                        *
 ********************************************************************************************************/
class CViewModelEmployeeList : Controller(){
    //Сервис с логикой для работы со списком сотрудников.
    private val serviceStaff: CServiceStaff by inject()
    //Список сотрудников с возможность отслеживания изменений.
    val staff = serviceStaff.getAll()
    //Факт наличия выделения в таблице.
    //От него зависит активность некоторых кнопок.
    val elementSelected = SimpleBooleanProperty(false)
    var globalChanges = SimpleBooleanProperty(false)
    //Модель представления для списка в целом.
    private val viewModelPositionList: CViewModelPositionList by inject()
    //Список названий должностей.
    val namePositionList = getPositionListName()
    /****************************************************************************************************
     * Добавление пустой записи в таблицу.                                                              *
     ****************************************************************************************************/
    fun add() {
        serviceStaff.add(CEmployee()) //Передача данных на сервер
    }
    /****************************************************************************************************
     * Удаление записи из списка.                                                                       *
     * @param item - элемент для удаления.                                                              *
     ****************************************************************************************************/
    fun delete(item: CEmployee?) {
        //Если элемент не указан - ничего не делаем.
        item ?: return
        serviceStaff.delete(item) //Передача данных на сервер
    }
    /****************************************************************************************************
     * Обработка изменения выделенных элементов в списке.                                               *
     * @param selectedItem - выделенный элемент.                                                        *
     ****************************************************************************************************/
    fun onSelectionChange(selectedItem: CEmployee?) {
        elementSelected.set(selectedItem != null)
    }
    /****************************************************************************************************
     * Отправка всех текущих данных из списка на сервер.                                                *
     ****************************************************************************************************/
    fun saveAll() {
        serviceStaff.saveAll() //Передача команды на отправку данных на сервер
    }
    /****************************************************************************************************
     * Взять все доступные названия должностей из списка модели представления.                          *
     ****************************************************************************************************/
    private fun getPositionListName(): ObservableList<String>{
        val temp = FXCollections.observableArrayList<String>()
        for (position in viewModelPositionList.positions){
                temp.add(position.name)
        }
        return temp
    }
}