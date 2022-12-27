package ru.psu.techjava.view

import javafx.scene.layout.BorderPane
import ru.psu.techjava.model.CDepartment
import ru.psu.techjava.viewmodels.CViewModelDepartment
import ru.psu.techjava.viewmodels.CViewModelDepartmentList
import tornadofx.*

class CViewDepartmentTable: View("Department") {
    override val root = BorderPane()
    private val viewModelList: CViewModelDepartmentList by inject()
    val viewModelItem = CViewModelDepartment(CDepartment())
    val table = tableview(viewModelList.departments) {
        isEditable = true
        column(messages["ID"], CDepartment::propertyId)
        column(messages["Name"], CDepartment::propertyName)//.makeEditable()
        //columnResizePolicy = SmartResize.POLICY
        //column<CDepartment, String>(messages["DepHead"], { it.value.propertyEmployee.value?.propertyName ?: "Нет главы".toProperty() })

        //Изменение веделения предаём в модель представления правой формы.
        viewModelItem.rebindOnChange(this) { selectedItem ->
            item                            = selectedItem ?: CDepartment()
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
                find<CFragmentViewDepartment>(mapOf(CFragmentViewDepartment::department to this.selectedItem))
                    .openModal(block = true)
            }
        }
    }

    init {
        //Верхняя часть окна
        root.top {
            //Делится на 2 строки
            vbox {
                //Самая верхняя строка - меню.
                menubar {
                    menu(messages["Windows"]) {
                        item(messages["Employees"]).action {
                            //Пример замены содержимого окна на другую View
                            replaceWith<CViewEmployeeTable>()
                        }
                        item(messages["Positions"]).action {
                            replaceWith<CViewPositionTable>()
                        }
                    }
                }
                //Вторая строчка сверху - кнопки для работы со списком объектов.
                hbox {
                    button(messages["Add"]) {
                        action {
                            //добавить пустой элемент в локальный список
                            viewModelList.add()
                            //Поменять занчение проверки наличе изменений на true
                            viewModelList.globalChanges.set(true)
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
            //В правой части располагается форма для редактирования одного объекта.
            form {
                fieldset(messages["Edit_department"]) {
                    field(messages["Name"]) {
                        textfield(viewModelItem.name) {
                            validator {
                                if (it.isNullOrBlank()) error("The name field is required") else null
                            }
                        }
                    }
                    hbox {
                        button(messages["Change"]) {
                            enableWhen(viewModelItem.dirty)
                            action {
                                viewModelItem.save()
                                viewModelList.globalChanges.set(true)
                            }
                        }
                        button(messages["Cancel"]).action {
                            viewModelItem.rollback()
                        }
                    }
                }
            }
        }
    }
}