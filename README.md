# Schedule-api
Schedule-API это это REST API по <a href="https://github.com/TimurDavletgareev/schedule-api/blob/main/%D0%A2%D0%B5%D1%81%D1%82%D0%BE%D0%B2%D0%BE%D0%B5_%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5_Java_%D1%80%D0%B0%D0%B7%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%87%D0%B8%D0%BA_%D1%81%D1%82%D0%B0%D0%B6%D1%91%D1%80_1.docx">тестовому заданию</a>

### Инструкция по локальному запуску
Для локального запуска приложения необходимы Java Development Kit, IDE и Maven

1) В IDE создать .jar используя Maven Plugin: mvn clean package -Dmaven.test.skip
2) Запустить приложение из консоли с указанием профиля local: $ java -jar target\schedule-API-1.0.jar --spring.profiles.active=local
3) После локального запуска эндпоинты API будут доступны по адресу http://localhost:8080/

### Документация OpenAPI
После локального запуска приложения Swagger UI доступен по адресу http://localhost:8080/swagger-ui/

### SOAP 
SOAP-сервис приложения позволяет добавлять в базу данных расписание врачей <a href="https://github.com/TimurDavletgareev/schedule-api/blob/main/src/main/resources/schedule.xsd">Schedule</a>.

### Инструкция Docker
Для создания docker-образов и контейнеров приложения необходимы Docker, Java Development Kit, IDE и Maven

1) В IDE создать .jar используя Maven Plugin: mvn clean package -Dmaven.test.skip
2) Запустить Docker
3) В консоли выполнить: $ docker-compose up
4) После создания и запуска контейнеров эндпоинты API будут доступны по адресу http://localhost:8080/
