# Booking System

Проект расположен в `finalProject/booking-system`.

Это backend на **Spring Boot 3.3.2** для системы бронирования:
- PostgreSQL как основная БД
- Spring Security + JWT для авторизации
- Spring Data JPA для доступа к данным
- Swagger/OpenAPI для документации
- асинхронные задачи для уведомлений, сканирования файлов и генерации отчетов

Ниже описано, что лежит в каждой директории и за что отвечает каждый слой.

## 1) Общая архитектура

Проект построен по классической слоистой схеме:

- `controller` принимает HTTP-запросы и возвращает DTO
- `service` содержит бизнес-логику и проверки
- `repo` отвечает за доступ к PostgreSQL через Spring Data JPA
- `entity` описывает таблицы и связи
- `dto` хранит модели запросов и ответов
- `mapper` вручную преобразует Entity <-> DTO

Дополнительно выделены:

- `security` - JWT, Spring Security, фильтры и конфигурация доступа
- `common` - общие исключения, ответы об ошибках, базовая сущность, логирование
- `notification`, `report`, `file` - отдельные прикладные подсистемы

## 2) Корень проекта

### `pom.xml`

Главный файл Maven-конфигурации.

Содержит:
- зависимости Spring Web, Validation, JPA, Security
- PostgreSQL driver
- JJWT для JWT-токенов
- Springdoc OpenAPI
- Actuator
- Lombok
- тестовые зависимости

### `Dockerfile`

Сборка backend-сервиса в Docker-образ.

Используется для запуска приложения в контейнере вместе с PostgreSQL через `docker-compose.yml`.

### `docker-compose.yml`

Описание инфраструктуры для локального запуска.

Обычно поднимает:
- PostgreSQL
- само приложение

### `Makefile`

Набор коротких команд для запуска/сборки проекта.

### `README.md`

Краткая стартовая документация проекта.

### `summary.md`

Этот файл. В нём собрана архитектурная карта и назначение папок/файлов.

### `booking-system.postman_collection.json`

Коллекция Postman с готовыми запросами к API.

Полезна для ручной проверки эндпоинтов:
- авторизация
- CRUD по отелям, комнатам и бронированиям
- загрузка файлов
- отчеты

### `.gitignore`

Список файлов и директорий, которые не должны попадать в Git:
- build-артефакты
- IDE-файлы
- временные файлы
- storage и другие локальные данные, если они не должны коммититься

## 3) `src/main/resources`

### `application.yml`

Главный файл конфигурации приложения.

Здесь настроены:
- порт сервера
- подключение к PostgreSQL
- `hibernate.ddl-auto`
- форматирование SQL
- параметры multipart-загрузки
- уровень логирования
- JWT secret и срок жизни токена
- директория хранения файлов
- настройки actuator

Это центральное место, где меняются параметры окружения без правки кода.

## 4) `src/main/java/kz/booking`

Это основной код приложения.  
Здесь лежит entry point и все доменные пакеты.

### `BekbolatovZholamanBookingSystemApplication.java`

Точка входа Spring Boot-приложения.

Именно этот класс запускает контекст приложения.

## 5) Пакет `auth`

Отвечает за вход и регистрацию.

### `auth/controller`

- `BekbolatovZholamanAuthController.java` - HTTP-эндпоинты регистрации и логина

### `auth/service`

- `BekbolatovZholamanAuthService.java` - бизнес-логика регистрации, аутентификации и выдачи JWT

### `auth/dto`

- `BekbolatovZholamanAuthRequest.java` - запрос на логин
- `BekbolatovZholamanRegisterRequest.java` - запрос на регистрацию
- `BekbolatovZholamanAuthResponse.java` - ответ с токеном и данными пользователя

## 6) Пакет `booking`

Главный домен проекта - бронирования.

### `booking/controller`

- `BekbolatovZholamanBookingController.java` - HTTP API для создания, просмотра, обновления и удаления бронирований

### `booking/service`

- `BekbolatovZholamanBookingService.java` - бизнес-правила бронирования:
  - проверка дат
  - контроль пересечений
  - права доступа владельца и администратора
  - смена статуса

### `booking/repo`

- `BekbolatovZholamanBookingRepository.java` - доступ к таблице бронирований и поисковые запросы

### `booking/entity`

- `BekbolatovZholamanBooking.java` - сущность бронирования
- `BekbolatovZholamanBookingStatus.java` - статусы брони

### `booking/dto`

- `BekbolatovZholamanCreateBookingRequest.java` - создание брони
- `BekbolatovZholamanUpdateBookingRequest.java` - обновление брони
- `BekbolatovZholamanBookingResponse.java` - ответ API по бронированию

### `booking/mapper`

- `BekbolatovZholamanBookingMapper.java` - преобразование Booking Entity в DTO и обратно

## 7) Пакет `hotel`

Сущность отеля и CRUD для него.

### `hotel/controller`

- `BekbolatovZholamanHotelController.java` - эндпоинты отелей

### `hotel/service`

- `BekbolatovZholamanHotelService.java` - бизнес-логика отелей

### `hotel/repo`

- `BekbolatovZholamanHotelRepository.java` - работа с БД по отелям

### `hotel/entity`

- `BekbolatovZholamanHotel.java` - таблица отелей

### `hotel/dto`

- `BekbolatovZholamanCreateHotelRequest.java` - создание отеля
- `BekbolatovZholamanUpdateHotelRequest.java` - обновление отеля
- `BekbolatovZholamanHotelResponse.java` - ответ API по отелю

### `hotel/mapper`

- `BekbolatovZholamanHotelMapper.java` - маппинг между entity и DTO

## 8) Пакет `room`

Комнаты принадлежат отелям и используются при бронировании.

### `room/controller`

- `BekbolatovZholamanRoomController.java` - HTTP API по комнатам

### `room/service`

- `BekbolatovZholamanRoomService.java` - логика по комнатам

### `room/repo`

- `BekbolatovZholamanRoomRepository.java` - запросы к таблице комнат

### `room/entity`

- `BekbolatovZholamanRoom.java` - сущность комнаты

### `room/dto`

- `BekbolatovZholamanCreateRoomRequest.java` - создание комнаты
- `BekbolatovZholamanUpdateRoomRequest.java` - обновление комнаты
- `BekbolatovZholamanRoomResponse.java` - ответ API по комнате

### `room/mapper`

- `BekbolatovZholamanRoomMapper.java` - преобразование Room Entity в DTO

## 9) Пакет `user`

Пользователи, роли и первичная инициализация данных.

### `user/controller`

- `BekbolatovZholamanUserController.java` - API для работы с пользователями

### `user/service`

- `BekbolatovZholamanUserService.java` - логика пользователя, ролей и операций вокруг аккаунтов

### `user/repo`

- `BekbolatovZholamanUserRepository.java` - запросы к таблице пользователей
- `BekbolatovZholamanRoleRepository.java` - запросы к таблице ролей

### `user/entity`

- `BekbolatovZholamanUser.java` - пользователь системы
- `BekbolatovZholamanRole.java` - роль
- `BekbolatovZholamanRoleName.java` - enum названий ролей

### `user/dto`

- `BekbolatovZholamanUserResponse.java` - ответ API по пользователю

### `user/mapper`

- `BekbolatovZholamanUserMapper.java` - маппинг User Entity в DTO

### `user/init`

- `BekbolatovZholamanDataInitializer.java` - стартовая инициализация данных

Обычно здесь создаются:
- базовые роли
- начальный admin-пользователь, если его нет

## 10) Пакет `security`

Все, что связано с безопасностью и JWT.

### `security/BekbolatovZholamanSecurityConfig.java`

Главная конфигурация Spring Security:
- правила доступа
- публичные и защищенные URL
- подключение JWT-фильтра
- настройки authentication/authorization

### `security/BekbolatovZholamanJwtService.java`

Создание и проверка JWT.

### `security/BekbolatovZholamanJwtAuthFilter.java`

Фильтр, который извлекает JWT из `Authorization: Bearer ...` и поднимает пользователя в SecurityContext.

### `security/BekbolatovZholamanUserDetailsService.java`

Загрузка пользователя из БД для Spring Security.

### `security/BekbolatovZholamanSecurityErrorHandler.java`

Обработка ошибок аутентификации и авторизации.

## 11) Пакет `common`

Общие классы, которые используются в нескольких модулях.

### `common/entity`

- `BekbolatovZholamanBaseEntity.java` - базовая сущность с общими полями, например идентификатором и аудиторскими данными

### `common/exception`

- `BekbolatovZholamanNotFoundException.java` - сущность не найдена
- `BekbolatovZholamanForbiddenException.java` - доступ запрещен
- `BekbolatovZholamanBadRequestException.java` - неверный запрос или нарушение бизнес-правил

### `common/api`

- `BekbolatovZholamanApiError.java` - унифицированный ответ об ошибке
- `BekbolatovZholamanFieldViolation.java` - ошибка по конкретному полю

### `common/web`

- `BekbolatovZholamanGlobalExceptionHandler.java` - глобальный `@ControllerAdvice`
- `BekbolatovZholamanRequestLoggingFilter.java` - логирование входящих HTTP-запросов

### `common/security`

- `BekbolatovZholamanSecurityUtils.java` - вспомогательные методы для работы с текущим пользователем, ролями и security context

## 12) Пакет `file`

Работа с файлами, привязанными к бронированиям.

### `file/controller`

- `BekbolatovZholamanFileController.java` - API для загрузки и скачивания файлов

### `file/service`

- `BekbolatovZholamanFileStorageService.java` - сохранение и выдача файлов с диска
- `BekbolatovZholamanFileScanService.java` - асинхронная проверка/сканирование загруженного файла

### `file/repo`

- `BekbolatovZholamanStoredFileRepository.java` - доступ к метаданным файлов в БД

### `file/entity`

- `BekbolatovZholamanStoredFile.java` - запись о загруженном файле

### `file/dto`

- `BekbolatovZholamanStoredFileResponse.java` - ответ API по файлу

### `file/mapper`

- `BekbolatovZholamanStoredFileMapper.java` - преобразование entity в DTO

## 13) Пакет `notification`

- `BekbolatovZholamanNotificationService.java`

Асинхронный сервис уведомлений.

Используется для действий вроде:
- уведомление после создания брони
- уведомление после подтверждения брони

В текущем проекте это отдельный сервис, чтобы не смешивать бизнес-логику бронирования с отправкой уведомлений.

## 14) Пакет `report`

### `BekbolatovZholamanBookingReportService.java`

Асинхронная генерация отчетов по бронированиям.

### `BekbolatovZholamanReportController.java`

HTTP API для запуска генерации и скачивания отчета.

Этот модуль отделен от основного CRUD, потому что отчет - это не базовая операция над сущностью, а отдельный сценарий обработки данных.

## 15) Пакет `config`

- `BekbolatovZholamanOpenApiConfig.java`

Конфигурация Swagger/OpenAPI.

Здесь обычно настраиваются:
- описание API
- заголовки безопасности
- группировка эндпоинтов
- схема документации

## 16) Что лежит по папкам с технической точки зрения

Если смотреть не на домены, а на роль директорий, картина такая:

- `controller` - вход в приложение по HTTP
- `service` - правила и сценарии
- `repo` - SQL/ORM слой
- `entity` - таблицы и связи
- `dto` - контракт API
- `mapper` - преобразование данных
- `security` - authn/authz
- `common` - переиспользуемые компоненты

Это помогает быстро понять, куда добавлять новый код:
- новый endpoint -> `controller`
- новая бизнес-логика -> `service`
- новая таблица/связь -> `entity`
- новый запрос к БД -> `repo`
- новый формат ответа -> `dto`
- новая конвертация -> `mapper`

## 17) Важные бизнес-сценарии

### Авторизация

Пользователь регистрируется или входит через `/api/auth/*`, получает JWT и дальше работает с защищенными эндпоинтами через `Authorization: Bearer <token>`.

### Отели и комнаты

Администратор управляет справочниками отелей и комнат. Обычные пользователи в основном читают данные и используют их при бронировании.

### Бронирования

Это центральная сущность системы.

Здесь проверяются:
- даты
- пересечения
- права владельца
- статус брони

### Файлы

Файлы сохраняются на диск, а в БД хранится метаинформация.

### Отчеты

Отчеты генерируются асинхронно, чтобы не блокировать основной HTTP-запрос.

## 18) Текущее состояние тестов

В репозитории на момент анализа нет файлов в `src/test`.

Это значит, что проект опирается в основном на ручную проверку через:
- Postman collection
- Swagger UI
- запуск через Docker Compose или локально

## 19) Краткая схема по пакетам

```text
src/main/java/kz/booking
├── auth           login/register/JWT response
├── booking        bookings, status, validation, filters
├── common         base entity, errors, logging, shared utils
├── config         OpenAPI configuration
├── file           upload/download/stored file metadata
├── hotel          hotels CRUD
├── notification   async notifications
├── report         async CSV reports
├── room           rooms CRUD
├── security       JWT + Spring Security
└── user           users, roles, bootstrap data
```

## 20) Коротко

Если смотреть совсем просто:

- `controller` - принимает запрос
- `service` - решает, что с ним делать
- `repo` - ходит в БД
- `entity` - хранит структуру данных
- `dto` - показывает данные наружу
- `mapper` - переводит одно в другое
- `security` - защищает API
- `common` - общие кирпичи проекта

Если нужно, следующим сообщением могу сделать ещё более практичную версию:
- либо `дерево проекта с пояснениями по каждому файлу`
- либо `короткую карту зависимостей между пакетами и классами`
