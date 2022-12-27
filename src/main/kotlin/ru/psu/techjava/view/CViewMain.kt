package ru.psu.techjava.view

import javafx.scene.layout.BorderPane
import tornadofx.*

class CViewMain: View("Home Page") {
    override val root = borderpane {
        setPrefSize(1210.0, 400.0)
        //Верхняя часть окна
        top {
            //Делится на 2 строки
            vbox {
                //Самая верхняя строка - меню (навигация по окнам).
                menubar {
                    menu(messages["Windows"]) {
                        item(messages["Employees"]).action {
                            //Пример замены содержимого окна на другую View
                            replaceWith<CViewEmployeeTable>()
                        }
                        item(messages["Positions"]).action {
                            replaceWith<CViewPositionTable>()
                        }
                        item(messages["Departments"]).action {
                            replaceWith<CViewDepartmentTable>()
                        }
                    }
                }
            }
        }
        center = label("TechJava2022 Lab3 GUI")
    }

    //root += label("TechJava2022 Lab3 GUI")
}