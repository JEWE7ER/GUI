package ru.psu.techjava.repositories

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.psu.techjava.model.CEmployee
import tornadofx.*

/********************************************************************************************************
 * Репозиторий с запросами к серверу в части списка сотрудников.                                        *
 * @author Панин А.Р. 2022 1128.                                                                        *
 * @link https://github.com/edvin/tornadofx/wiki/REST-Client                                            *
 ********************************************************************************************************/
class CRepositoryStaff : Controller(){
    //Объект для отправки запросов к API сервера.
    private val api: Rest by inject()
    //"Кэшированный" список студентов для локальной работы.
    private val staff = FXCollections.observableArrayList<CEmployee>()
    /****************************************************************************************************
     * Запрос списка всех доступных объектов на сервере.                                                *
     ****************************************************************************************************/
    fun getAll(): ObservableList<CEmployee>{
        //Запрашиваем актуальные данные на сервере
        val listFromServer = requestAll()
        //Пересохраняем в локальный список,
        //в котором дополнительно будем кэшировать изменения в процессе
        //редактирования таблицы.
        staff.addAll(listFromServer)
        //Возвращаем ссылку на кэш.
        return staff
    }
    /****************************************************************************************************
     * Запрос списка студентов на сервере и обработка возможных проблем.                                *
     ***************************************************************************************************/
    private fun requestAll(): ObservableList<CEmployee>{
        var response : Rest.Response? = null
        try {
            //Собственно выполнение GET запроса к пути /employee
            response = api.get("employee")
            if (response.ok()) {
                //Конвертация json в список объектов типа CEmployee
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
     * Отправка списка объектов на сервер.                                                              *
     ****************************************************************************************************/
    fun saveAll() {
        //Запрос данных с сервера.
        var staffFromServer = requestAll()
        //Перебираем всех студентов с сервера,
        //ищем тех, кто отсутствует в локальном списке,
        //для них отправляем запрос на удаление.
        staffFromServer.forEach { employeeFromServer ->
                //Для того, чтобы метод contains сработал правильно,
                //и чтобы не писать ручной перебор,
                //переопределён метод CStudent.equals
                if (!staff.contains(employeeFromServer)) {
                    api.delete("employee", employeeFromServer)
                    //println("${employeeFromServer.propertyName} was removed")
                }
        }
        staffFromServer = requestAll()
        //Перебираем все записи из локального кэша
        var temp: List<CEmployee>
        staff.forEach { employeeLocal->
                        //Для каждой локальной записи находим записи из сервера с такими же id.
                        temp = staffFromServer.filter { employeeFromServer ->
                                employeeLocal.equals(employeeFromServer)
                                }
                        //Записи с такими же id
                        // (должна быть максимум 1, но гарантий никто не даст)
                        // фильтруем по наличию изменений
                        temp.firstOrNull { employeeFromServer ->
                                employeeLocal.hasChanges(employeeFromServer)
                                }
                            //Если изменения в полях есть, публикуем текущую запись на сервер.
                            ?.let {
                                    var response: Rest.Response? = null
                                try {
                                    response = api.post("employee", employeeLocal)
                                    if (response.ok()){
                                        println("Apply")
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
                            api.post("employee", employeeLocal)

        }
    }
    /****************************************************************************************************
     * Добвление сотрудника в локальный список.                                                         *
     * @param employee - объект для отправки.                                                           *
     ****************************************************************************************************/
    fun add(employee: CEmployee){
        staff.add(employee)
    }
    /****************************************************************************************************
     * Удаление сотрудника из локального списка.                                                        *
     * @param employee - объект для отправки.                                                           *
     ****************************************************************************************************/
    fun delete(employee: CEmployee){
        staff.remove(employee)
    }
}