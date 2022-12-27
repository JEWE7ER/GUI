package ru.psu.techjava.view

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import ru.psu.techjava.model.CDepartment
import ru.psu.techjava.model.CEmployee
import ru.psu.techjava.model.CPosition
import ru.psu.techjava.viewmodels.CViewModelEmployeeList
import ru.psu.techjava.viewmodels.CViewModelPositionList
import tornadofx.*

class CFragmentViewDepartment: Fragment("Position's & Employee's Department") {
    //Отдел, по которому показывать должность и сотрудник.
    val department: CDepartment by param()
    private val viewModelPos: CViewModelPositionList by inject()
    private val viewModelEmp: CViewModelEmployeeList by inject()
    private var positionList = posByDepartment()
    private var employeeList = empByDepartment()
    private val depHead = department.employee

    override val root = BorderPane()
    init {
        root.top = label("Отдел: ${department.name}")
        root.center {
            tabpane {
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

                setPrefSize(800.0, 200.0)
                tab(messages["Positions"], HBox()) {
                    tableview<CPosition> {
                        items = positionList
                        column(messages["ID"], CPosition::propertyId).remainingWidth()
                        column(messages["Name"], CPosition::propertyName).remainingWidth()
                        column(messages["Salary"], CPosition::propertySalary).remainingWidth()
                        column(messages["marker_dep_head"], CPosition::propertyMarkerDepHead).remainingWidth()
                    }
                }
                tab(messages["Employees"], HBox()) {
                    tableview<CEmployee> {
                        items = employeeList//.asObservable()
                        column(messages["ID"], CEmployee::propertyId)
                        column(messages["FIO"], CEmployee::propertyName)
                        column(messages["Birthday"], CEmployee::propertyBirthday)
                        column(messages["Coefficient"], CEmployee::propertyCoefficient).remainingWidth()
                        column(messages["Position_ID"], CEmployee::propertyPos).remainingWidth()
                    }
                }
                tab(messages["DepHead"], HBox()) {
                    tableview<CEmployee>{
                        items = listOf(depHead).asObservable()
                        column(messages["ID"], CEmployee::propertyId)
                        column(messages["FIO"], CEmployee::propertyName)
                        column(messages["Birthday"], CEmployee::propertyBirthday)
                        column(messages["Coefficient"], CEmployee::propertyCoefficient).remainingWidth()
                        column(messages["Position_ID"], CEmployee::propertyPos).remainingWidth()
                    }
                }
            }
        }
    }
    private fun posByDepartment(): ObservableList<CPosition>? {
        val temp = FXCollections.observableArrayList<CPosition>()
        viewModelPos.positions.forEach { item ->
            if (item.dep?.equals(department.id) == true)
                temp.add(item)
        }
        return temp
    }

    private fun empByDepartment(): ObservableList<CEmployee>? {
        val temp = FXCollections.observableArrayList<CEmployee>()
        viewModelEmp.staff.forEach { item ->
            positionList?.forEach { position ->
                if (item.pos?.equals(position?.id) == true)
                    temp.add(item)
            }
        }
        return temp
    }
}