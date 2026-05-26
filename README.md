# 📚 Library Hub — REST API Система Керування Бібліотечною Структурою

**Library Hub** — це сучасний веб-застосунок (SPA) для керування ієрархічною структурою університетських бібліотек, кафедр, навчальних дисциплін та супутньої літератури. Проєкт реалізовано за архітектурним патерном **Only-REST API**, де бекенд на Spring Boot повністю відокремлений від легкого фронтенду на чистій веб-платформі (HTML5/CSS3/JavaScript).

---

### 🛠️ Технологічний стек

* **Backend:** Java 17, Spring Boot 3.2.x, Spring Security (JWT), Spring Data JPA.
* **Database:** PostgreSQL 15.
* **Frontend:** Vanilla JavaScript (ES6+), HTML5, CSS3 (Modern Flexbox/Grid UI).
* **DevOps & Infrastructure:** Docker, Docker Compose (Multi-file configuration for production/development), Spring Boot DevTools (Hot Reloading).

---

### ✨ Ключові функції

* **Рольова модель доступу (RBAC):** Авторизація на основі JWT-токенів (ролі `ADMIN` та `USER`).
* **Ієрархічний CRUD:** Повний цикл керування даними за ланцюжком: `Бібліотеки ➔ Кафедри ➔ Дисципліни ➔ Література`.
* **Візуальні індикатори доступу:** Обробка специфічних бізнес-прапорців із бази даних (відображення бейджу `🔒 Specialized` для кафедр з особливим статусом).
* **Контейнеризація «із коробки»:** Миттєве розгортання всієї інфраструктури однією командою.
* **Hot Reload Environment:** Налаштоване середовище розробки, що дозволяє миттєво бачити зміни в коді без перезапуску Docker-контейнерів.

---

### 📦 Структура бази даних (Схема)

Проєкт оперує наступними сутностями:
1. **Library:** Назва та загальна кількість кафедр.
2. **Department:** Назва, ім'я декана/завідувача та прапорець спеціального доступу (`spec`).
3. **Subject:** Назва дисципліни, кількість кредитів ECTS, силлабус.
4. **Literature:** Книга, автор, рік видання.
5. **User:** Користувачі системи з хэшованими за допомогою BCrypt паролями.

---

### 🚀 Як запустити проєкт

#### 1. Попередні вимоги
Переконайтеся, що у вас встановлено **Docker** (включаючи плагін/CLI **Docker Compose**).

#### 2. Налаштування змінних оточення
Створіть у корені проєкту файл `.env` та заповніть його параметрами для бази даних:
```env
DB_NAME=library_db
DB_USER=lisa_admin
DB_PASSWORD=your_secure_password
DB_PORT_EXTERNAL=5555
```

#### 3. Запуск у режимі розробки (з гарячим перезавантаженням)
Для запуску середовища... виконайте команду у вашому терміналі:
```bash
docker compose -f compose.yaml -f docker-compose.dev.yml up --build
```

#### 4. Доступ до застосунку
* **Клієнтський інтерфейс (Фронтенд):** Перейдіть у браузері за адресою `http://localhost:8080`
* **База даних (PostgreSQL):** Доступна на порту `5555`

#### 🔐 Тестові дані для входу (ініціалізуються автоматично через `init.sql`):
* **Адміністратор (Повний CRUD):** Логін: `admin` | Пароль: `admin`
* **Звичайний користувач (Тільки перегляд):** Логін: `lisa` | Пароль: `password`

---

### 📂 Основні API Ендпоінти

| Метод | Ендпоінт | Опис | Доступ |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Аутентифікація, повернення JWT токена | Для всіх |
| `GET` | `/api/v1/libraries` | Отримати список усіх бібліотек | USER, ADMIN |
| `POST` | `/api/v1/libraries` | Створити нову бібліотеку | Тільки ADMIN |
| `GET` | `/api/v1/libraries/{id}/departments` | Кафедри конкретної бібліотеки | USER, ADMIN |
| `PUT` | `/api/v1/departments/{id}` | Редагувати дані кафедри | Тільки ADMIN |
| `DELETE` | `/api/v1/literature/{id}` | Видалити книгу зі списку | Тільки ADMIN |

<br><br>

---

# 📚 Library Hub — Library Structure Management REST API

**Library Hub** is a modern Single Page Application (SPA) designed to manage the hierarchical structure of university libraries, faculty departments, academic subjects, and corresponding literature. Built on an **Only-REST API** architectural pattern, the backend powered by Spring Boot is fully decoupled from a lightweight client crafted with pure web standards (HTML5/CSS3/JavaScript).

---

### 🛠️ Tech Stack

* **Backend:** Java 17, Spring Boot 3.2.x, Spring Security (JWT), Spring Data JPA.
* **Database:** PostgreSQL 15.
* **Frontend:** Vanilla JavaScript (ES6+), HTML5, CSS3 (Modern Flexbox/Grid UI).
* **DevOps & Infrastructure:** Docker, Docker Compose (Multi-file dev/prod workflows), Spring Boot DevTools (Hot Reloading).

---

### ✨ Key Features

* **Role-Based Access Control (RBAC):** Secure authorization driven by JWT tokens split into `ADMIN` and `USER` clearance.
* **Hierarchical CRUD:** Full-cycle data management mapped logically: `Libraries ➔ Departments ➔ Subjects ➔ Literature`.
* **UI Indicators:** Dynamic processing of boolean flags from the DB (custom rendering of a `🔒 Specialized` badge for sensitive departments).
* **Containerized Ecosystem:** Instant environment provisioning via a single command.
* **Hot Reload Setup:** Native binding between host OS source code and running containers, enabling sub-second context refreshes on build.

---

### 📦 Database Architecture (Schema)

The system governs the following interconnected entities:
1. **Library:** Name and total department counter.
2. **Department:** Title, Dean's name, and a specific access restriction flag (`spec`).
3. **Subject:** Name, ECTS credits allocation, syllabus description.
4. **Literature:** Book title, author, publication year.
5. **User:** System credentials secured with BCrypt password hashing.

---

### 🚀 Getting Started

#### 1. Prerequisites
Ensure you have **Docker** installed (including the **Docker Compose** plugin/CLI).

#### 2. Environment Configuration
Create a `.env` file in the root directory and define your database variables:
```env
DB_NAME=library_db
DB_USER=lisa_admin
DB_PASSWORD=your_secure_password
DB_PORT_EXTERNAL=5555
```

#### 3. Spin Up Development Mode (with Hot Reloading)
To trigger the automated developer workflow utilizing multi-stage configurations and mounted volumes, run:
```bash
docker compose -f compose.yaml -f docker-compose.dev.yml up --build
```

#### 4. Accessing the Application
* **Web Client (UI):** Open your browser and navigate to `http://localhost:8080`
* **Database Connection:** Exposed externally via port `5555`

#### 🔐 Demo Credentials (Seeded automatically via `init.sql`):
* **Administrator (Full CRUD):** Username: `admin` | Password: `admin`
* **Standard User (Read-Only):** Username: `lisa` | Password: `password`

---

### 📂 Key API Endpoints

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Authenticate user & issue JWT bearer token | Public |
| `GET` | `/api/v1/libraries` | Fetch all registered libraries | USER, ADMIN |
| `POST` | `/api/v1/libraries` | Instantiate a new library record | ADMIN Only |
| `GET` | `/api/v1/libraries/{id}/departments` | Get departments assigned to a library | USER, ADMIN |
| `PUT` | `/api/v1/departments/{id}` | Update specific department info | ADMIN Only |
| `DELETE` | `/api/v1/literature/{id}` | Wipe a book entry from a subject | ADMIN Only |