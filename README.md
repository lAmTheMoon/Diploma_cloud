# Дипломная работа “Облачное хранилище”

## Описание проекта

Данное приложение - REST-сервис. Сервис предоставляет REST интерфейс для возможности загрузки файлов и вывода списка уже загруженных файлов пользователя. Все запросы к сервису авторизованы. Заранее подготовленное веб-приложение (FRONT) подключается к разработанному BACK сервису без доработок, а также используется функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

## Запуск

bd: docker pull postgres:13.3

back: Dockerfile и команда docker build -t back .

front: добавить Dockerfile в корень проекта FRONT, далее команда docker build -t front .

```Dockerfile
FROM node:14.5.0-alpine
RUN mkdir -p /app/front
WORKDIR /app/front
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 8080
CMD [ "npm", "run", "serve" ]
```
Приложение запускается командой docker-compose up

Приложение работает по адресу http://localhost:8080

Тестовый пользователь:

"login": "Lusi@mail.ru",
"password": "123"

## Работа приложения

Сервис выполняет методы:

- Вывод списка файлов
- Добавление файла
- Удаление файла
- Авторизация
