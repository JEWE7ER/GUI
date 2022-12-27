package ru.psu.techjava.view

import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import ru.psu.techjava.model.CDepartment
import ru.psu.techjava.model.CEmployee
import ru.psu.techjava.model.CPosition
import ru.psu.techjava.viewmodels.CViewModelDepartmentList
import ru.psu.techjava.viewmodels.CViewModelPositionList
import tornadofx.*
import tornadofx.Stylesheet.Companion.tableView
/********************************************************************************************************
 * Страница с информацией о должности и отедле конкретного сотрудника.                                  *
 * @author Панин А.Р. 2022 1121.                                                                        *
 ********************************************************************************************************/
class CFragmentViewEmployee: Fragment("Position's & Department's Employee") {
    //Сотрудник, по которому показывать должность и отдел.
    val employee: CEmployee by param()
    //Модель представления для списка должностей.
    private val viewModelPos: CViewModelPositionList by inject()
    //Модель представления для списка отедлов.
    private val viewModelDep: CViewModelDepartmentList by inject()

    private var position = posByEmployee()
    private var department = depByEmployee()

    override val root = BorderPane()
    init {
        root.top = label("${messages["Employee"]}: ${employee.name}")
        root.center {
            tabpane {
                //Закрыть листы нельзя.
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                setPrefSize(800.0, 200.0) // Размер окна.
                tab(messages["Position"], HBox()) {
                    tableview<CPosition> { //Таблица с информацией о должности.
                        items = listOf(position).asObservable()
                        column(messages["ID"], CPosition::propertyId).fixedWidth(250.0)
                        column(messages["Name"], CPosition::propertyName).fixedWidth(250.0)
                        column(messages["Salary"], CPosition::propertySalary).fixedWidth(150.0)
                    }
                }
                tab(messages["Department"], HBox()) {
                    tableview<CDepartment> {// таблица с инфрамцией о отделе.
                        items = listOf(department).asObservable()
                        column(messages["ID"], CDepartment::propertyId).fixedWidth(250.0)
                        column(messages["Name"], CDepartment::propertyName).fixedWidth(250.0)
                    }
                }
            }
        }
    }
    /********************************************************************************************************
     * Поиск должности сотрудника из списка всех должностей.                                                *
     ********************************************************************************************************/
    private fun posByEmployee(): CPosition? {
        viewModelPos.positions.forEach { item ->
            if (item.id?.equals(employee.pos) == true)
                return item
        }
        return null
    }
    /********************************************************************************************************
     * Поиск отдела сотрудника из списка всех отделов на основе информации о его должности.                 *
     ********************************************************************************************************/
    private fun depByEmployee(): CDepartment? {
        viewModelDep.departments.forEach { item ->
            if (item.id?.equals(position?.dep) == true)
                return item
        }
        return null
    }
}
