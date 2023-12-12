## Менеджер задач. Тестовый проект.

Spring-Boot приложение для управления задачами. Backend часть.

При разработке использовались JDK 21, Maven 3.8.7, MariaDB 11.2.2.

### Локальный запуск.

Установить сервер БД (MySQL или MariaDB). В клиент БД импортировать скрипт `./sql/dbInit.sql`, или вручную выполнить в клиенте БД команды из скрипта.
При желании изменить данные доступа (в этом случае их нужно также изменить в файле `./src/main/resources/application.yml`).

Переместить файл `application.yml` из корня проекта в `./src/main/resources`.

В директории проекта выполнить `mvn package`, `java -jar target/taskmanager-0.1.jar`. При необходимости переместить .jar-файл в удобноую локацию и создать скрипт запуска
(например как сервиса).

### Запуск тестовой среды в контейнерах через Docker Compose.

В директории проекта выполнить `docker compose up`. Перед этим, при необходимости, изменить данные доступа в файле `.env`.

## Документация.

Доступ к документации при запущенном приложении через Swagger: `http://{HOST}:{PORT}/swagger-ui/index.html`, `http://{HOST}:{PORT}/v3/api-docs`.
