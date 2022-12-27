package ru.psu.techjava.view

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import ru.psu.techjava.model.CDepartment
import ru.psu.techjava.model.CEmployee
import ru.psu.techjava.model.CPosition
import ru.psu.techjava.viewmodels.CViewModelDepartmentList
import ru.psu.techjava.viewmodels.CViewModelEmployeeList
import tornadofx.*

class CFragmentViewPosition: Fragment("Employee's & Department's Position")  {
    private fun empByPosition(): ObservableList<CEmployee>? {
        val temp = FXCollections.observableArrayList<CEmployee>()
        viewModelEmp.staff.forEach { item ->
            if (item.pos?.equals(position.id) == true)
                temp.add(item)
        }
        return temp
    }

    private fun depByPosition(): CDepartment? {
        viewModelDep.departments.forEach { item ->
            if (item.id?.equals(position.dep) == true)
                return item
        }
        return null
    }

    //Должность, по которой показывать список сотрудников и отдел.
    val position: CPosition by param()
    private val viewModelEmp: CViewModelEmployeeList by inject()
    private val viewModelDep: CViewModelDepartmentList by inject()
    private var employeeList = empByPosition()
    private var department = depByPosition()

    override val root = BorderPane()
    init {
        root.top = label("Должность: ${position.name}")
        root.center {
            tabpane {
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

                setPrefSize(800.0, 200.0)
                tab("Employees", HBox()) {
                    tableview<CEmployee> {
                        items = employeeList//.asObservable()
                        column(messages["ID"], CEmployee::propertyId)
                        column(messages["FIO"], CEmployee::propertyName)
                        column(messages["Birthday"], CEmployee::propertyBirthday)
                        column(messages["Coefficient"], CEmployee::propertyCoefficient).remainingWidth()
                    }
                }
                tab("Department", HBox()) {
                    tableview<CDepartment> {
                        items = listOf(department).asObservable()
                        column(messages["ID"], CDepartment::propertyId).remainingWidth()
                        column(messages["Name"], CDepartment::propertyName).remainingWidth()
                    }
                }

            }
        }
    }
}