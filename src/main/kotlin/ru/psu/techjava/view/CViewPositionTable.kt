package ru.psu.techjava.view

import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane
import ru.psu.techjava.model.CPosition
import ru.psu.techjava.viewmodels.CViewModelPosition
import ru.psu.techjava.viewmodels.CViewModelPositionList
import tornadofx.*
import java.io.Serializable

/********************************************************************************************************
 * Страница со списком оценок.                                                                          *
 * @author Селетков И.П. 2022 1205.                                                                     *
 *******************************************************************************************************/
class CViewPositionTable: View("Position") {
    //Тестовое содержимое, надо расписать самостоятельно под вашу предметную область.
    override val root = BorderPane()

    val selectedDepartment = SimpleStringProperty()
    //Модель представления для списка в целом.
    private val viewModelList: CViewModelPositionList by inject()
    val departmentList = FXCollections.observableArrayList<String>()
    //Модель представления для одного элемента,
    //отображаемого в правой боковой панели редактирвоания.
    val viewModelItem = CViewModelPosition(CPosition())

    var toggleGroup = ToggleGroup()
    var value: String? = null
    //Таблица должностей.
    //В качестве параметра передаём список должностей из модели представления.
    val table = tableview(viewModelList.positions) {
        isEditable = true
        column(messages["ID"], CPosition::propertyId)
        column(messages["Name"], CPosition::propertyName)
        column(messages["Salary"], CPosition::propertySalary)
        //column(messages["marker_dep_head"], CPosition::propertyMarkerDepHead)
        column(messages["department_ID"], CPosition::propertyDep)
        //Изменение веделения предаём в модель представления правой формы.
        viewModelItem.rebindOnChange(this) { selectedItem ->
            item = selectedItem ?: CPosition()
            departmentList.clear()
            departmentList.addAll(viewModelList.nameDepartmentList)
            viewModelItem.change.set(false)
        }
        //Изменение веделения передаём в модель представления всего списка.
        onSelectionChange {
            viewModelList.onSelectionChange(this.selectedItem)
        }
        onDoubleClick {
            //Если есть выделение
            this.selectedItem?.let {
                //Открываем второе окно со списком сотрудников и информацией об отделе,
                //передаём туда выделенную должность.
                //Это пример создания дочернего окна, исходное окно остаётся не доступным для пользователя (параметр 'block').
                find<CFragmentViewPosition>(mapOf(CFragmentViewPosition::position to this.selectedItem))
                    .openModal(block = true)
            }
        }
    }

    init {
        departmentList.addAll((viewModelList.nameDepartmentList))
        //Верхняя часть окна
        root.top{
            //Делится на 2 строки
            vbox {
                //Самая верхняя строка - меню.
                menubar {
                    menu(messages["Windows"]) {
                        item(messages["Employees"]).action{
                            //Пример замены содержимого окна на другую View
                            replaceWith<CViewEmployeeTable>()
                        }
                        item(messages["Departments"]).action{
                            //Пример замены содержимого окна на другую View
                            replaceWith<CViewDepartmentTable>()
                        }
                    }
                }
                //Вторая строчка сверху - кнопки для работы со списком объектов.
                hbox {
                    button(messages["Add"]) {
                        action{
                            viewModelList.add()
                            viewModelList.globalChanges.set(true)
                        }
                    }
                    button(messages["Delete"]) {
                        //Активность кнопки удалить зависит от поля в модели представления.
                        enableWhen(viewModelList.elementSelected)
                        action{
                            viewModelList.delete(table.selectedItem)
                            viewModelList.globalChanges.set(true)
                        }
                    }
                    button(messages["Save"]) {
                        enableWhen(viewModelList.globalChanges)
                        action{
                            viewModelList.saveAll()
                            viewModelList.globalChanges.set(false)
                        }
                    }
                }
            }
        }
        root.center{
            //В центральной части располагается таблица.
            this += table
        }
        root.right  {
            //В правой части располагается форма для редактирования одного объекта.
            form{
                visibleWhen(viewModelList.elementSelected)
                fieldset(messages["Edit_position"]) {
                    field(messages["Name"]) {
                        textfield(viewModelItem.name) {
                            validator {
                                if (it.isNullOrBlank()) error("The name field is required") else null
                            }
                        }
                    }
                    field(messages["Salary"]) {
                        textfield(viewModelItem.salary){
//                            validator {
//                                if (it?.toDouble()!! <12000) error("The salary field is required") else null
//                            }
                        }
                    }
                    field(messages["Department_Name"]) {
                        label(viewModelItem.depName)
                        label(" -> "){
                            visibleWhen(viewModelItem.change)
                        }
                        combobox(selectedDepartment, departmentList)//.textfield().bind(viewModelItem.posName, true)
                    }
                    selectedDepartment.onChange {
                        viewModelItem.getNewDepName(it)
                        val condition = observableListOf(viewModelItem.name,
                            viewModelItem.salary,
                            viewModelItem.marker)
                        val trueCondition: List<Property<out Serializable>>
                        trueCondition = condition.filter { it.isDirty == true }
                        if (viewModelItem.change.value)
                            viewModelItem.markDirty(viewModelItem.newDepName)
                        else if ((trueCondition.isNotEmpty()) and (viewModelItem.change.value == false)) {
                            viewModelItem.rollback(viewModelItem.newDepName)
                            for (param in trueCondition)
                                viewModelItem.markDirty(param)
                        }
                        else
                            viewModelItem.rollback(viewModelItem.newDepName)
                    }
                    field(messages["marker_dep_head"]) {
                        label(viewModelItem.marker)
                    }
                    field(messages["Change_marker"]) {
                        radiobutton(messages["Yes"],toggleGroup){
                            setOnAction {
                                viewModelItem.markerDepHead.value = 1
                                value = messages["Yes"]
                            }
                        }
                        radiobutton(messages["No"],toggleGroup) {
                            setOnAction {
                                viewModelItem.markerDepHead.value = 0
                                value = messages["No"]
                            }
                        }
                    }
                    hbox{
                        button(messages["Change"]) {
                            enableWhen(viewModelItem.dirty)
                            action{
                                viewModelItem.marker.value = value

                                if(viewModelItem.change.value) {
                                    viewModelItem.depName.value = selectedDepartment.value
                                    viewModelItem.changeDepID()
                                    viewModelItem.change.set(false)
                                }

                                departmentList.clear()
                                departmentList.addAll(viewModelList.nameDepartmentList)
                                viewModelList.globalChanges.set(true)
                                viewModelItem.save()
                            }
                        }
                        button(messages["Cancel"]).action {
                            departmentList.clear()
                            departmentList.addAll(viewModelList.nameDepartmentList)
                            viewModelItem.rollback()
                        }
                    }
                }
            }
        }
    }
}