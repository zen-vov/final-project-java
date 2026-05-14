# Booking System (Final Project)

Тема: **Booking System** (Spring Boot + PostgreSQL + JWT).

Проект расположен в: `finalProject/booking-system`.

## 1) Архитектура (слои)

Структура сделана по слоям:

- `controller` принимает HTTP запросы/Path+Query параметры, возвращает DTO
- `service` содержит бизнес-логику, проверки, транзакции
- `repo` (repository) слой доступа к PostgreSQL через Spring Data JPA

Дополнительно:

- `dto` запросы/ответы
- `mapper` ручные маппинги Entity <-> DTO
- `common/web` глобальная обработка ошибок и логирование
- `security` JWT + Spring Security

## 2) Сущности (6)

1. `BekbolatovZholamanUser` (users)
2. `BekbolatovZholamanRole` (roles) + `BekbolatovZholamanRoleName`
3. `BekbolatovZholamanHotel` (hotels)
4. `BekbolatovZholamanRoom` (rooms)
5. `BekbolatovZholamanBooking` (bookings) + `BekbolatovZholamanBookingStatus`
6. `BekbolatovZholamanStoredFile` (stored_files)

Связи:

- User <-> Role: ManyToMany
- Hotel -> Room: ManyToOne (Room хранит `hotel_id`)
- Booking -> User/Room: ManyToOne
- StoredFile -> Booking: ManyToOne

## 3) Безопасность (Spring Security + JWT)

Реализовано:

- Регистрация `/api/auth/register`
- Логин `/api/auth/login`
- JWT Bearer токен в `Authorization: Bearer <token>`
- Роли: `ROLE_USER`, `ROLE_ADMIN`

Правила доступа:

- Swagger и OpenAPI: разрешены без авторизации
- `GET /api/hotels/**` и `GET /api/rooms/**`: публичные
- CRUD Hotels/Rooms: только `ADMIN`
- Bookings: доступ по JWT. Обычный пользователь видит/меняет **только свои** бронирования, админ видит все.

Bootstrap admin:

- при старте создаются роли
- если нет админа, создается admin по настройкам:
  - `app.bootstrap.admin.email` (default `admin@local`)
  - `app.bootstrap.admin.password` (default `admin12345`)

## 4) REST эндпоинты (GET/POST/PUT/DELETE)

### Auth

- `POST /api/auth/register` (DTO + validation)
- `POST /api/auth/login` -> JWT

### Hotels

- `POST /api/hotels` (ADMIN)
- `GET /api/hotels/{id}`
- `GET /api/hotels?city=...&q=...&page=0&size=20&sort=name,asc`
- `PUT /api/hotels/{id}` (ADMIN)
- `DELETE /api/hotels/{id}` (ADMIN)

### Rooms

- `POST /api/rooms` (ADMIN)
- `GET /api/rooms/{id}`
- `GET /api/rooms?hotelId=...&page=0&size=20&sort=pricePerNight,desc`
- `PUT /api/rooms/{id}` (ADMIN)
- `DELETE /api/rooms/{id}` (ADMIN)

### Bookings (пагинация + сортировка + поиск + фильтрация)

Основной эндпоинт, который закрывает требования:

- `GET /api/bookings`

Query параметры:

- `userId` (работает только для ADMIN)
- `hotelId`, `roomId`
- `status` (PENDING/CONFIRMED/CANCELED)
- `from`, `to` (фильтр по датам)
- `q` (поиск по `notes`)
- пагинация/сортировка: `page`, `size`, `sort`

Примеры:

- `GET /api/bookings?page=0&size=10&sort=startDate,desc`
- `GET /api/bookings?status=CONFIRMED&from=2026-05-14&to=2026-06-01`
- `GET /api/bookings?hotelId=1&q=late`

CRUD:

- `POST /api/bookings` (создание брони текущим пользователем)
- `GET /api/bookings/{id}` (владелец или ADMIN)
- `PUT /api/bookings/{id}` (user может CANCEL, admin может CONFIRM)
- `DELETE /api/bookings/{id}` (ADMIN)

Бизнес-валидация:

- `startDate/endDate` обязательны
- `endDate >= startDate`
- `startDate` не в прошлом
- проверка пересечения бронирований в одной комнате (`existsOverlapping(...)`)

### Files (upload/download)

- `POST /api/bookings/{bookingId}/files` (multipart `file`) (владелец брони или ADMIN)
- `GET /api/bookings/{bookingId}/files` (список метаданных)
- `GET /api/files/{fileId}/download` (download)

Файлы хранятся на диске в директории `app.files.storage-dir` (по умолчанию `./storage`).

### Async reports (ADMIN)

- `GET /api/admin/reports/bookings?from=2026-05-14&to=2026-06-01` -> вернет `reportKey` (генерация CSV асинхронно)
- `GET /api/admin/reports/bookings/download?reportKey=...` -> скачать CSV

## 5) Async процессы (2-3)

1. `BekbolatovZholamanNotificationService`:
   - отправка "email" после создания брони
   - отправка "email" после подтверждения брони
2. `BekbolatovZholamanFileScanService`:
   - асинхронный "scan" загруженного файла
3. `BekbolatovZholamanBookingReportService`:
   - асинхронная генерация CSV отчета

Технически: `@EnableAsync` + `@Async` + `CompletableFuture`.

## 6) Валидация + обработка ошибок

- DTO валидация через `jakarta.validation` (`@Valid`, `@NotBlank`, `@Email`, и т.д.)
- `BekbolatovZholamanGlobalExceptionHandler` возвращает `BekbolatovZholamanApiError`

## 7) Swagger UI

Swagger UI:

- `GET /swagger-ui/index.html`

OpenAPI JSON:

- `GET /v3/api-docs`

## 8) Логирование

- `BekbolatovZholamanRequestLoggingFilter` логирует `method/path/status/user/tookMs`
- `BekbolatovZholamanGlobalExceptionHandler` логирует неожиданные ошибки

## 9) Запуск

### Вариант A: Docker Compose (рекомендуется)

Из папки `finalProject/booking-system`:

```bash
docker compose up --build
```

Сервисы:

- `postgres` (PostgreSQL 16) на `localhost:5432`
- `api` на `http://localhost:8080`

Healthcheck:

- `http://localhost:8080/actuator/health`

Swagger:

- `http://localhost:8080/swagger-ui/index.html`

### Вариант B: Локально (без Docker)

1. Поднять PostgreSQL и создать БД/юзера:
   - db: `booking_db`
   - user: `booking`
   - password: `booking`
2. Запуск:

```bash
mvn -Dmaven.repo.local=/private/tmp/m2 spring-boot:run
```

## 10) Полезные заметки

- По умолчанию админ создается автоматически:
  - email: `admin@local`
  - password: `admin12345`
- Для запросов с protected эндпоинтами используйте заголовок:
  - `Authorization: Bearer <token>`

