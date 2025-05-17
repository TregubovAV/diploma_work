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
java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app \
     -Dspring.datasource.username=app \
     -Dspring.datasource.password=pass \
     -Dspring.credit-gate.url=http://localhost:9999/credit \
     -Dspring.payment-gate.url=http://localhost:9999/payment \
     -jar artifacts/aqa-shop.jar
```

#### ✅ Для PostgreSQL:

```bash
java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app \
     -Dspring.datasource.username=app \
     -Dspring.datasource.password=pass \
     -Dspring.credit-gate.url=http://localhost:9999/credit \
     -Dspring.payment-gate.url=http://localhost:9999/payment \
     -jar artifacts/aqa-shop.jar
```

Приложение будет доступно по адресу:  
📍 `http://localhost:8080`

---

## 🧪 Запуск автотестов

Выбор БД для тестов осуществляется с помощью системного параметра `-DdbType`.

#### ▶ Прогон тестов с MySQL:

```bash
./gradlew clean test -DdbType=mysql
```

#### ▶ Прогон тестов с PostgreSQL:

```bash
./gradlew clean test -DdbType=postgres
```

---

### Генерация отчёта Allure:

```bash
./gradlew allureReport
```

### Просмотр Allure-отчёта в браузере:

```bash
./gradlew allureServe
```

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

---

> ⚠ `application.properties` не используется. Все параметры передаются через `-D` при запуске приложения.
