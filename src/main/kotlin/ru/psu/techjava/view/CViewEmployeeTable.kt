package ru.psu.techjava.view

import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Labeled
import javafx.scene.layout.BorderPane
import javafx.util.converter.NumberStringConverter
import ru.psu.techjava.model.CEmployee
import ru.psu.techjava.services.CServiceDepartment
import ru.psu.techjava.viewmodels.CViewModelEmployee
import ru.psu.techjava.viewmodels.CViewModelEmployeeList
import tornadofx.*
import tornadofx.Stylesheet.Companion.readonly
import java.io.Serializable
import java.time.LocalDate

/********************************************************************************************************
 * Страница со списком сотрудников.                                                                     *
 * @author Панин А.Р. 2022 1121.                                                                        *
 ********************************************************************************************************/
class CViewEmployeeTable : View("Staff") {
    //Корневой элемент формы - элемент с 5ю областями (Верх, Низ, Левая, Правая, Центр)
    override val root = BorderPane()

    val selectedPosition = SimpleStringProperty()
    //Модель представления для списка в целом.
    private val viewModelList: CViewModelEmployeeList by inject()
    private val positionList = FXCollections.observableArrayList<String>()
    private val serviceDepartment: CServiceDepartment by inject()
    private val changeLabel = SimpleBooleanProperty()
    //Модель представления для одного элемента,
    //отображаемого в правой боковой панели редактирвоания.
    val viewModelItem = CViewModelEmployee(CEmployee())
    //Таблица сотрудников.
    //В качестве параметра передаём список сотрудников из модели представления.
    val table = tableview(viewModelList.staff) {
        isEditable = true
        column(messages["ID"], CEmployee::propertyId).fixedWidth(250.0)
        column(messages["FIO"], CEmployee::propertyName).fixedWidth(300.0)
        column(messages["Birthday"], CEmployee::propertyBirthday).fixedWidth(150.0)
        column(messages["Coefficient"], CEmployee::propertyCoefficient).fixedWidth(200.0)
        //column(messages["Position_ID"], CEmployee::propertyPos).remainingWidth()
        //Изменение веделения предаём в модель представления правой формы.
        viewModelItem.rebindOnChange(this) { selectedItem ->
            item = selectedItem ?: CEmployee()
            positionList.clear()
            positionList.addAll(viewModelList.namePositionList)
            viewModelItem.change.set(false)
        }
        //Изменение веделения передаём в модель представления всего списка.
        onSelectionChange {
            viewModelList.onSelectionChange(this.selectedItem)
        }
        //Двойной клик
        onDoubleClick {
            //Если есть выделение
            this.selectedItem?.let {
                //Открываем второе окно с информацией о должности и отделе,
                //передаём туда выделенного сотрудника.
                //Это пример создания дочернего окна, исходное окно остаётся не доступным для пользователя (параметр 'block').
                find<CFragmentViewEmployee>(mapOf(CFragmentViewEmployee::employee to this.selectedItem))
                    .openModal(block = true)
            }
        }
    }

    init {
        positionList.addAll(viewModelList.namePositionList)
        //Верхняя часть окна
        root.top {
            //Делится на 2 строки
            vbox {
                //Самая верхняя строка - меню.
                menubar {
                    menu(messages["Windows"]) {
                        item(messages["Positions"]).action {
                            //Пример замены содержимого окна на другую View
                            replaceWith<CViewPositionTable>()
                        }
                        item(messages["Departments"]).action {
                            //Пример замены содержимого окна на другую View
                            replaceWith<CViewDepartmentTable>()
                        }
                    }
                }
                //Вторая строчка сверху - кнопки для работы со списком объектов.
                hbox {
                    button(messages["Add"]) {
                        action {
                            //добавить пустой элемент в локальный список
                            viewModelList.add()
                            //Поменять занчение проверки наличе изменений на false
                            viewModelList.globalChanges.set(false)
                        }
                    }
                    button(messages["Delete"]) {
                        //Активность кнопки удалить зависит от поля в модели представления.
                        enableWhen(viewModelList.elementSelected)
                        action {
                            //Удалить из локального списка объект
                            viewModelList.delete(table.selectedItem)
                            //Поменять занчение проверки наличе изменений на true
                            viewModelList.globalChanges.set(true)
                        }
                    }
                    button(messages["Save"]) {
                        //Активность кнопки при наличи изменений
                        enableWhen(viewModelList.globalChanges)
                        action {
                            //Отправить изменённый локальный список
                            viewModelList.saveAll()
                            //Поменять занчение проверки наличе изменений на false
                            viewModelList.globalChanges.set(false)
                            //Если кто-то поменялся на должности руководителя отдела,
                            //то сразу сохраним на сервер локальный изменённый список отделов
                            //с новыми руководителями
                            if (viewModelItem.changeDepHead.value) {
                                serviceDepartment.saveAll()
                                //Меням значение переменной,
                                //которая отражает поменялись ли руководители,
                                //на false
                                viewModelItem.changeDepHead.set(false)
                            }
                        }
                    }
                }
            }
        }
        root.center {
            //В центральной части располагается таблица.
            this += table
        }
        root.right {
            changeLabel.set(false)
            //В правой части располагается форма для редактирования одного объекта.
            form {
                visibleWhen(viewModelList.elementSelected)
                fieldset(messages["Edit_person"]) {
                    field(messages["FIO"]) {
                        textfield(viewModelItem.name) {
                            validator { //невохможно сохранить изменения, если имя путое
                                if (it.isNullOrBlank()) error(messages["name_required"]) else null
                            }
                        }
                    }
                    field(messages["Birthday"]) {
                        datepicker(viewModelItem.birthday) {
                            isEditable = false
                            value = LocalDate.now()
                        }
                    }
                    field(messages["Coefficient"]) {
                        textfield(viewModelItem.coefficient, NumberStringConverter()){
                            validator {
                                if(viewModelItem.coefficient.value < 1) error(messages["coef_invalid"]) else null
                            }
                        }
                    }
                    field(messages["Position_Name"]) {
                        label(viewModelItem.posName)
                        label(" -> ") {
                            visibleWhen(changeLabel)
                        }
                        combobox(selectedPosition, positionList)
                    }
                    //если выбрано что-то из combobox'а
                    selectedPosition.onChange {
                        changeLabel.set(true)
                        viewModelItem.getNewPosName(it) //узнаём, что выбрано
                        val condition = observableListOf( //создаем список всех изменяемых параметров
                            viewModelItem.name,
                            viewModelItem.coefficient,
                            viewModelItem.birthday
                        )
                        val trueCondition: List<Property<out Serializable>>
                        trueCondition = condition.filter { it.isDirty == true } //отбираем те параметры, которые были уже изменены
                        if (viewModelItem.change.value) //если выбарна не такая же должность
                            viewModelItem.newPosName.value = selectedPosition.value //меняем занчение в модели
                        //иначе, если были изменения в параметрах, то отктываем должность до изначального значения,
                        //и говорим, что измененные значения являются измененными, чтобы вся модель была изменённая
                        //(конкретно в этом случае rollback не хочет откатывать только занчение позиции,
                        //rollback меняет занчение model.dirty на false, несмотря на то, что другоие параметры менялись)
                        else if ((trueCondition.isNotEmpty()) and (viewModelItem.change.value == false)) {
                            viewModelItem.rollback(viewModelItem.newPosName)
                            changeLabel.set(false)
                            for (param in trueCondition)
                                viewModelItem.markDirty(param)
                        } else
                            viewModelItem.rollback(viewModelItem.newPosName)
                    }
                    hbox {
                        button(messages["Change"]) {
                            enableWhen(viewModelItem.dirty)
                            action {
                                if ((viewModelItem.posName.value==messages["None"]) and
                                    (changeLabel.value == false)){
                                    val randomPosNameList = viewModelItem.randomName()
                                    val newPosName = randomPosNameList.random()
                                    viewModelItem.posName.value = newPosName
                                    viewModelItem.changePosID()
                                }
                                //если у сотрудка менялась должность, то меняем для в модели
                                if (viewModelItem.change.value) {
                                    viewModelItem.posName.value = selectedPosition.value
                                    viewModelItem.changePosID()
                                    viewModelItem.change.set(false)
                                }
                                //чистим список и заполянем его заного, чтобы selectedItem работал корректно
                                positionList.clear()
                                positionList.addAll(viewModelList.namePositionList)
                                viewModelList.globalChanges.set(true)
                                //сохранем все изменения в моедль
                                viewModelItem.save()
                            }
                        }
                        button(messages["Cancel"]){
                            enableWhen(viewModelItem.dirty)
                            action {
                                positionList.clear()
                                positionList.addAll(viewModelList.namePositionList)
                                viewModelItem.rollback()
                                changeLabel.set(false)
                            }
                        }
                    }
                }
            }
        }
    }
}