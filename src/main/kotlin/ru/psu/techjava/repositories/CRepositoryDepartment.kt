package ru.psu.techjava.repositories

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.psu.techjava.model.CDepartment
import tornadofx.Controller
import tornadofx.Rest
import tornadofx.toModel

class CRepositoryDepartment: Controller() {
    //Объект для отправки запросов к API сервера.
    private val api: Rest by inject()
    //"Кэшированный" список студентов для локальной работы.
    private val departments = FXCollections.observableArrayList<CDepartment>()
    /****************************************************************************************************
     * Запрос списка всех доступных объектов на сервере.                                                *
     ****************************************************************************************************/
    fun getAll(): ObservableList<CDepartment> {
        //Запрашиваем актуальные данные на сервере
        val listFromServer = requestAll()
        //Пересохраняем в локальный список,
        //в котором дополнительно будем кэшировать изменения в процессе
        //редактирования таблицы.
        departments.addAll(listFromServer)
        //Возвращаем ссылку на кэш.
        return departments
    }
    /****************************************************************************************************
     * Запрос списка студентов на сервере и обработка возможных проблем.                                *
     ***************************************************************************************************/
    private fun requestAll(): ObservableList<CDepartment> {
        var response : Rest.Response? = null
        try {
            //Собственно выполнение GET запроса к пути /department
            response = api.get("department")
            if (response.ok()) {
                //Конвертация json в список объектов типа CDepartment
                return response.list().toModel()
            } else if (response.statusCode == 404)
                throw Exception("404")
            else
                throw Exception("getCustomer returned ${response.statusCode} ${response.reason}")
        }
        catch(e: Exception) {
            throw Exception("Отсутствует соединение с сервером", e)
        }
        finally {
            response?.consume()
        }
    }

    /****************************************************************************************************
     * Отправка данных объекта на сервер.                                                               *
     * @param department - объект для отправки.                                                           *
     ****************************************************************************************************/
    fun save(department: CDepartment){
        //api.post("employee", employee)
    }
    /****************************************************************************************************
     * Отправка списка объектов на сервер.                                                              *
     ****************************************************************************************************/
    fun saveAll() {
        //Запрос данных с сервера.
        val departmentsFromServer = requestAll()

        //Перебираем всех студентов с сервера,
        //ищем тех, кто отсутствует в локальном списке,
        //для них отправляем запрос на удаление.
        departmentsFromServer.forEach { itemFromServer ->
            //Для того, чтобы метод contains сработал правильно,
            //и чтобы не писать ручной перебор,
            //переопределён метод CStudent.equals
            if (!departments.contains(itemFromServer)) {
                api.delete("department", itemFromServer)
            }
        }
        //Перебираем все записи из локального кэша
        var temp: List<CDepartment>
        departments.forEach { itemLocal->
            //Для каждой локальной записи находим записи из сервера с такими же id.
            temp = departmentsFromServer.filter { itemFromServer ->
                itemLocal.equals(itemFromServer)
            }
            //Записи с такими же id
            // (должна быть максимум 1, но гарантий никто не даст)
            // фильтруем по наличию изменений
            temp.firstOrNull { itemFromServer ->
                itemLocal.hasChanges(itemFromServer)
            }
                //Если изменения в полях есть, публикуем текущую запись на сервер.
                ?.let {
                    var response: Rest.Response? = null
                    try {
                        response = api.post("department", itemLocal)
                        if (response.ok()){
                            println("Unusual")
                        }
                        else if (response.statusCode == 409)
                            throw Exception("409")
                        else
                            throw Exception("getCustomer returned ${response.statusCode} ${response.reason}")
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            //Если вообще на сервере записей с таким id нет
            //публикуем текущую запись.
            if (temp.isEmpty())
                api.post("department", itemLocal)

        }
    }
    /****************************************************************************************************
     * Добвление сотрудника в локальный список.                                                         *
     * @param department - объект для отправки.                                                           *
     ****************************************************************************************************/
    fun add(department: CDepartment){
        departments.add(department)
    }
    /****************************************************************************************************
     * Удаление сотрудника из локального списка.                                                        *
     * @param department - объект для отправки.                                                           *
     ****************************************************************************************************/
    fun delete(department: CDepartment){
        departments.remove(department)
    }

    fun checkChanges(): Boolean {
        //Запрос данных с сервера.
        val departmentsFromServer = requestAll()

        //Перебираем всех студентов с сервера,
        //ищем тех, кто отсутствует в локальном списке,
        //для них отправляем запрос на удаление.
        departmentsFromServer.forEach { itemFromServer ->
            //Для того, чтобы метод contains сработал правильно,
            //и чтобы не писать ручной перебор,
            //переопределён метод CStudent.equals
            if (!departments.contains(itemFromServer)) {
                api.delete("department", itemFromServer)
            }
        }
        //Перебираем все записи из локального кэша
        var temp: List<CDepartment>
        departments.forEach { itemLocal ->
            //Для каждой локальной записи находим записи из сервера с такими же id.
            temp = departmentsFromServer.filter { itemFromServer ->
                itemLocal.equals(itemFromServer)
            }
            //Записи с такими же id
            // (должна быть максимум 1, но гарантий никто не даст)
            // фильтруем по наличию изменений
            temp.firstOrNull { itemFromServer ->
                itemLocal.hasChanges(itemFromServer)
            }
                //Если изменения в полях есть, публикуем текущую запись на сервер.
                ?.let {
                    return true
                }
        }
        return false
    }
}