package ru.psu.techjava.services

import ru.psu.techjava.model.CDepartment
import ru.psu.techjava.repositories.CRepositoryDepartment
import tornadofx.Controller
import java.util.*

class CServiceDepartment: Controller() {
    private val repositoryDepartment: CRepositoryDepartment by inject()
    /****************************************************************************************************
     * Запрос списка всех доступных объектов на сервере.                                                *
     ****************************************************************************************************/
    fun getAll()
            = repositoryDepartment.getAll()

    /****************************************************************************************************
     * Отправка данных объекта на сервер.                                                               *
     * @param department - объект для отправки.                                                           *
     ****************************************************************************************************/
    fun save(department: CDepartment)
            = repositoryDepartment.save(department)
    /****************************************************************************************************
     * Отправка всех данных из списка на сервер.                                                        *
     ***************************************************************************************************/
    fun saveAll()
            = repositoryDepartment.saveAll()
    /****************************************************************************************************
     * Отправка данных объекта на сервер.                                                               *
     * @param department - объект для отправки.                                                            *
     ***************************************************************************************************/
    fun add(department: CDepartment){
        department.id = UUID.randomUUID()
        repositoryDepartment.add(department)
    }
    /****************************************************************************************************
     * Отправка данных объекта на сервер.                                                               *
     * @param department - объект для отправки.                                                            *
     ***************************************************************************************************/
    fun delete(department: CDepartment)
            = repositoryDepartment.delete(department)
}