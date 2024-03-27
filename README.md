МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ РОССИЙСКОЙ ФЕДЕРАЦИИ
ФГАОУ ВО «Пермский государственный национальный исследовательский университет»

Физический факультет
Кафедра радиоэлектроники и защиты информации









Технологии Java
Лабораторная работа №3 на тему 
«Технологии Java для создания настольных приложений»

Выполнил:
студент 2 курса физического факультета
группа ФЗ/О ПМИ-1-2021 НБ
направление «01.03.02 Прикладная математика и информатика»
профиль «Инженерия программного обеспечения»
Панин Антон Романович






Пермь, 2022 г.
Оглавление
1.	Введение	3
2.	Постановка задачи	4
3.	Практическая реализация	5
3.1.	Используемый инструментарий	5
3.2.	Модель данных	5
3.3.	Интерфейс программы	6
3.4.	Обмен данных с серверной частью	15
3.5.	Тестирование программы	18
4.	Заключение	22
5.	Список литературы	23
Приложение	24

 
1.	Введение
Цель работы: ознакомление с существующими технологиями Java для создания кроссплатформенных приложений для персональных компьютеров, обладающих графическим интерфейсом пользователя.
Теоретическая часть по новому материалу, технологиям и библиотекам, которые будет использованы при выполнении данной работы:
1)	JavaFX[1] – представляет инструментарий для создания кроссплатформенных графических приложений на платформе Java. JavaFX позволяет создавать приложения с богатой насыщенной графикой благодаря использованию аппаратного ускорения графики и возможностей GPU.С помощью JavaFX можно создавать программы для различных операционных систем: Windows, MacOS, Linux, Android, iOS и для самых различных устройств: десктопы, смартфоны, планшеты, встроенные устройства, ТВ.
2)	TornadoFX[2] — это небольшой и легкий фреймворк JavaFX для языка программирования Kotlin. Функции включают внедрение зависимостей, типобезопасные конструкторы, типобезопасный CSS, делегирование свойств, асинхронное выполнение задач, клиент REST, MVC-подобную модель программирования и другие небольшие улучшения платформы JavaFX, часто реализуемые как функции расширения для обеспечения почти нулевой производительности.
3)	Gradle[3] — это инструмент автоматизации сборки с открытым исходным кодом, ориентированный на гибкость и производительность. Сценарии сборки Gradle пишутся с использованием Groovy или Kotlin DSL. Gradle работает на JVM. Это бонус для пользователей, знакомых с Java, поскольку логика сборки может использовать стандартные Java API.
 
2.	Постановка задачи
Написать кроссплатформенное приложение для настольных персональных компьютеров, позволяющее работать с данными штатной расстановки (вариант № 10).
Приложение должно иметь графический интерфейс пользователя.
Приложение должно позволять через REST API получать(запрашивать), добавлять, удалять, редактировать данные, которые хранятся в БД и которые требуются для построения аналитического отчёта.
Программа должна использовать инструменты Java, Java SDK, коллекции, java.time.*, TornadoFX.
 
3.	Практическая реализация
3.1.	Используемый инструментарий
Таблица 1
Инструментарий, использованный при решении поставленной задачи
Язык программирования	Java, Kotlin
Версия JavaSDK	18
Среда разработки	Intellij IDEA 2022.2.1
Система автоматизированной сборки	Gradle[3]
Инструменты JavaSDK	java.util
java.net.InetAddress
java.time
Библиотеки	JavaFX[1], TornadoFX[2].


3.2.	Модель данных
 
Рисунок 1 – Диаграмма классов данных предметной области

Employee: сотрудник
•	id - уникальный идентификатор сотрудника;
•	name - фамилия, имя и отчество сотрудника;
•	coefficient - коэффициент сотрудника;
•	birthday - дата рождения сотрудника;
•	position - должность, которой принадлежит сотрудник.
Position: должность
•	id - уникальный идентификатор должности;
•	name - название должности;
•	salary - оклад на должности;
•	department - отдел, которому принадлежит должность;
•	employee - список сотрудников, принадлежащих должности.
Department: организации на которые поданы обращения
•	id - уникальный идентификатор отдела;
•	name - название отдела;
•	depHead - руководитель отдела;
•	positions - список должностей отдела.

3.3.	Интерфейс программы
 
Рисунок 2 – Диаграмма классов моделей представлений

	В каждом окне расположено меню для навигации по окнам, кнопки для управления данными таблицы (добавление нового элемента таблицы, удаление элемента и сохранение данных из представления в базу данных), таблица со списком объектов, также расположены поля для редактирования элементов списка (поля для новых значений, кнопки сохранения и удаления изменений).
Идентификаторы новых элементов генерируются с помощью метода randomUUID() класса UUID.
 
Рисунок 6 – Главное окно программы

 
Рисунок 7 – Окно сотрудников

Когда нажимаем на объект таблицы, то появляются поля, с помощью которых можно редактировать информацию о выделенном объекте.
 
Рисунок 8 – Окно сотрудников с полями для редактрования

Для окон с информацией о должностях и отделах вид аналогичен.
Создание элементов интерфейса:
•	Навигация по окнам: 
В левой верхней части окна приложения доступно меню, в котором можно выбрать, в какое окно перейти: с сотрудниками, должностями или отделениями. Предыдущее окно заменяется выбранным из предложенного списка.
Листинг 1 – Создание навигации по окнам
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

 
Рисунок 9 – Возможность перехода между окнами

•	Кнопки для работы со списком данных: 
Кнопка «Add» добавления нового пустого элемента таблицы, кнопка «Delete» для удаления выбранного элемента из таблицы и кнопка «Save» для передачи данных таблицы из представления в базу данных.
Листинг 2 – Создание функционала кнопок для работы со списком объектов
//Вторая строчка сверху - кнопки для работы со списком объектов.
hbox {
    button(messages["Add"]) {
        action {
            //добавить пустой элемент в локальный список.
            viewModelList.add()
            //Поменять занчение проверки наличе изменений на false
            viewModelList.globalChanges.set(false)
        }
    }
    button(messages["Delete"]) {
        //Активность кнопки удалить зависит от поля в модели представления.
        enableWhen(viewModelList.elementSelected)
        action {
            //Удалить из локального списка объект.
            viewModelList.delete(table.selectedItem)
            //Поменять занчение проверки наличе изменений на true.
            viewModelList.globalChanges.set(true)
        }
    }
    button(messages["Save"]) {
        //Активность кнопки при наличи изменений.
        enableWhen(viewModelList.globalChanges)
        action {
            //Отправить изменённый локальный список.
            viewModelList.saveAll()
            //Поменять занчение проверки наличе изменений на false.
            viewModelList.globalChanges.set(false)
            //Если кто-то поменялся на должности руководителя отдела,
            //то сразу сохраним на сервер локальный изменённый список отделов
            //с новыми руководителями.
            if (viewModelItem.changeDepHead.value) {
                serviceDepartment.saveAll()
                //Меням значение переменной,
                //которая отражает поменялись ли руководители,
                //на false.
                viewModelItem.changeDepHead.set(false)
            }
        }
    }
}

•	Таблица сотрудников: 
Передаём в таблицу список сотрудников из модели представления, создаём колонки для каждого параметра сотрудника (кроме идентификатора должности) и позволяем их изменять (кроме идентификатора сотрудника), также передаём изменение выделения элементов таблицы в форму для редактирования и в модель представления всего списка.
Листинг 3 – Создание функционала таблицы граждан
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


•	Дочернее окно:
В инициализации таблицы добавляем переход в дочернее окно. В него передаем все параметры выделенного сотрудника. При двойном клике на объект в таблице открывается дополнительное (дочернее) окно с информацией о должности и отедле, которым пренадлежит сотрудник. Исходное окно становиться недоступным для взаимодействия.
Листинг 4 – Добавление перехода в дочернее окно при двойном клике по объекту
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

Ниже представлено само создание дочернего окна.
Листинг 5 – Создание дочернего окна
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


 
Рисунок 10 – Дочернее окно сотрудника с информацией о долности и отедле
 
•	Форма для редактирования сотрудника: 
Создаём форму с полями для редактирования сотрудника, кнопку для сохранения изменений из полей формы в сущность (кэш программы) и кнопку для отката этих изменений к изначальным значениям параметров сущности.
Листинг 6 – Создание функционала формы для редактирования сотрудника
//В правой части располагается форма для редактирования одного объекта.
form {
    visibleWhen(viewModelList.elementSelected)
    fieldset(messages["Edit_person"]) {
        field(messages["FIO"]) {
            textfield(viewModelItem.name) {
                validator { //невохможно сохранить изменения, если имя путое
                    if (it.isNullOrBlank()) error("The name field is required") else null
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
            textfield(viewModelItem.coefficient)
        }
        field(messages["Position_Name"]) {
            label(viewModelItem.posName)
            label(" -> ") {
                visibleWhen(viewModelItem.change)
            }
            combobox(selectedPosition, positionList)
        }
        //если выбрано что-то из combobox'а
        selectedPosition.onChange {
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
                for (param in trueCondition)
                    viewModelItem.markDirty(param)
            } else
                viewModelItem.rollback(viewModelItem.newPosName)
        }
        hbox {
            button(messages["Change"]) {
                enableWhen(viewModelItem.dirty)
                action {
                    if (viewModelItem.posName.value==messages["None"]){
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
            button(messages["Cancel"]).action {
                positionList.clear()
                positionList.addAll(viewModelList.namePositionList)
                viewModelItem.rollback()
            }
        }
    }
}


Для остальных окон весь функционал схожий.
3.4.	Обмен данных с серверной частью
 
Рисунок 11 – Диаграмма классов репозиториев
•	Создание репозитория сотрудников (связывает сервис и БД): 
Репозиторий с запросами к серверу в части списка сотрудников.
Листинг 7 – Создание репозитория сотрнудников для запросов к БД
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


Репозитории для должностей и отделов аналогичны.

Небольшое отступление. Так как, в моём случае сервер и разрабатываемое приложение находятся на одной ОС, то был разработан метод автоматического получения базовой части адреса для подключения к API сервера из ОС Windows, чтобы каждый раз не заходить в файл и не менять его.
Листинг 8 – Автоматическое получение базовой части адреса для подключения к API сервера
class CApplication: App(CViewMain::class, Styles::class){
    private val api: Rest by inject()
    init {
        //Базовая часть адреса для подключения к API сервера.
        val ipAddress = getSystemIP()
        api.baseURI = "http://${ipAddress}:8080/"
    }
    //автоматичекое получение ip
    private fun getSystemIP(): String? {
        return try {
            InetAddress.getLocalHost().hostAddress
        } catch (e: Exception){
            println("Проверьте подключение к сети")
            null
        }
    }
}

3.5.	Тестирование программы
Проверять наличие/корректность изменений на сервере мы будем с помощью Postaman[4].

1.	Изменяем данные сотрудника:
 
Рисунок 12 – Информация о сотруднике до изменений


Поменяем имя, день рожденя, коэффициент и должность: 
 
Рисунок 13 – Вид окна редактрования информации о сотруднике после изменений

Кнопки «Изменить» и «Отмена» стали активные. Если нажать «Отмена», то все изменённые значения вернуть к исходным.
 
Рисунок 14 – Вид окна редактрования информации о сотруднике после нажатия кнопки «Отмена»
Все поля приняли изначальные значения, все изменения стёрлись.
Если снова изменить занчения полей и нажать кнопку «Изменить», то новые данные заменят изначальные.
 
Рисунок 15 – Информация о сотруднике после изменений
2.	Удаление сотрудника:
Удаление сотрудника с id = 172a028a-e534-4c77-a3f5-775dfa44391c.
 
Рисунок 16 – Таблица сотрудников после удаления

3.	Добавление нового сотрудника:
 
Рисунок 17 – Таблица сотрудников после добавления

После добавления сотрудника кнопка «Сохранить» становиться недоступной, чтобы не сохранять пустого сотрудника. 
Если в поле «ФИО» в редактировании информации о сотруднике мы не введём имя, то не сможем сохранить его локальные данные. Введём новые данные для этого сотрудника.
 
Рисунок 18 – Вид окна редактрования информации о сотруднике после изменений

 
Рисунок 19 – Таблица сотрудников после изменений

4. Отправляем данные на сервер кнопкой «Сохранить». Проверяем данные на сервере через Postman.
 
Рисунок 20 – Данные на сервере

Как мы видим, все данные успешно обновелны.
 
4.	Заключение
Для интерфейса разработано 4 класса типа View, 3 класса типа Fragment, 6 классов типа ViewModel. 
Для локализации интерфейса разработано 3 пакета Resource Bundle.
Для взаимодействия приложения с сервером разработано 3 сервиса, 3 репозитория и 3 модели данных.
Поставленные перед данной работой цели были выполнены успешно.
Получены и отработаны знания по работе с TornadoFX. Получилось создать пользовательский интерфейс настольного приложения с небольшим функционалом.


 
5.	Список литературы
1. Введение в Java FX // METANIT.COM [Электронный ресурс]. URL: https://metanit.com/java/javafx/1.1.php (дата обращения: 18.12.2022).
2. Home // GitHub [Электронный ресурс]. URL: https://github.com/edvin/tornadofx/wiki (дата обращения: 18.12.2022).
3. What is Gradle? // Gradle User Manual [Электронный ресурс]. – URL: https://docs.gradle.org/current/userguide/userguide.html (дата обращения: 18.12.2022).
4. Postman API Platform | Sign Up for Free [Электронный ресурс]. – URL: https://www.postman.com/ (дата обращения: 18.12.2022).

