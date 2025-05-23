# Дипломный проект: Автоматизация тестирования сервиса покупки тура

## 🧾 Описание

Веб-приложение предоставляет два способа покупки тура:
- 💳 Обычная оплата по дебетовой карте
- 🏦 Покупка в кредит по данным банковской карты

Приложение взаимодействует с банковскими сервисами (эмулятор), а также записывает статус операции в базу данных (MySQL/PostgreSQL).  
В рамках проекта реализована автоматизация UI-тестов и проверка данных в БД.

---

## ⚙️ Технологии

- Java 17  
- Selenide  
- JUnit5  
- Gradle  
- Allure  
- Faker  
- Apache DBUtils  
- Docker + Docker Compose  
- MySQL / PostgreSQL  
- Node.js (эмулятор банковского API)

---

## 🚀 Как запустить проект

### 1. Клонировать репозиторий

```bash
git clone https://github.com/TregubovAV/diploma_work.git
cd diploma_work
```

### 2. Запустить окружение (БД + эмулятор)

```bash
docker-compose up --build
```

Контейнеры:
- `MySQL` → `localhost:3306`, логин: `app`, пароль: `pass`
- `PostgreSQL` → `localhost:5432`, логин: `app`, пароль: `pass`
- `Gate Simulator` → `localhost:9999`

> 🛈 Эмулятор банков отображает `Cannot GET /` — это нормально, он работает через REST API.

---

### 3. Запустить приложение вручную

Выберите нужную базу данных (MySQL или PostgreSQL) и используйте соответствующую команду:

#### ✅ Для MySQL:

```bash
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar
```

#### ✅ Для PostgreSQL:

```bash
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar
```

Приложение будет доступно по адресу:  
📍 `http://localhost:8080`

---

## 🧪 Запуск автотестов

#### ▶ Прогон тестов с MySQL:

```bash
./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"
```

#### ▶ Прогон тестов с PostgreSQL:

```bash
./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"
```

---

<!-- пока что не реализовано
### Генерация отчёта Allure:

```bash
./gradlew allureReport
```

### Просмотр Allure-отчёта в браузере:

```bash
./gradlew allureServe
``` -->

---

## 🗂️ Структура проекта

```
src/test/java/
├── data/         # Генерация и хранение тестовых данных
├── db/           # Работа с БД (Apache DBUtils)
├── pages/        # Page Object'ы для UI-тестов
├── tests/        # Сценарии автотестов
```

---

## 📄 Документы в проекте

- `Plan.md` — план автоматизации (сценарии, инструменты, риски, оценка)
- `docs/Report.md` — отчёт о тестировании (кейсы, баги, результат)
- `docs/Summary.md` — отчёт об автоматизации (что сделано, итоги)
