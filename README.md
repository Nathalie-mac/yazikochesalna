<img alt="logo.png" height="200" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fmain.jpg" width="200" float="middle"/>

Корпоративный мессенджер с национальным колоритом

# Навигация

[1. О проекте](#description)

[2. Стек технологий](#stack)

[3. Документация](#docs)

[4. Развертывание](#deploy)

[5. Умельцы](#team)

<a name="description"/>

# О проекте

## Пользователям

Языкочесальня - это корпоративный мессенджер и удобное пространство для общения. Все как в телеграме

**У нас есть:**\

## Разработчикам



<a name="stack"/>

# Стек технологий

## Backend
<img alt="java.jpg" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fjava.png" width="100"/>
<img alt="kotlin.jpg" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fkotlin.png" width="240"/>
<img alt="spring.png" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fspring.png" width="240"/><br>
<img alt="docker.png" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fdocker.png" width="180"/>
<img alt="k8s.png" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fk8s.png" width="120"/><br>
<img alt="kafka.jpg" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fkafka.png" width="120"/>
<img alt="elastic.png" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Felastic.png" width="120"/><br>

## Frontend
<img alt="css.jpg" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fcss.png" width="120"/>
<img alt="hyml.jpg" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fhyml.png" width="120"/><br>
<img alt="react.png" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Freact.png" width="160"/>
<img alt="websockets.png" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fwebsockets.png" width="240"/><br>

## DB
<img alt="postgres.jpg" height="100" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fpostgres.png" width="100"/>
<img alt="redis.jpg" height="100" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fredis.png" width="100"/><br>
<img alt="cassandra.png" height="80" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fcassandra.png" width="240"/>
<img alt="hisernate.png" height="80" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fhisernate.png" width="200"/>
<img alt="liquibase.jpg" height="80" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fliquibase.png" width="200"/><br>
<img alt="minio.png" height="240" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fminio.png" width="80"/><br>


<a name = "docs"/>

# Документация


## Архитектура
[Архитектура системы]()

## Документация API

[АPI]()

## UML-диаграммы 

[UseCase-диаграмма системы]()

## Базы данных

[Модель]() баз данных

<a name = "deploy"/>

# Развертывание

**Важное**\
Данный репозиторий содержит backend-часть приложения.\
frontend хранится по [ссылке]()

## Локальное развертывание
### Основная система
1. Скачайте данный репозиторий командой `git clone `
2. Скачайте репозиторий веб-приложения командой `git clone `
3. Из папки проекта yazikochesalna запустите контейнеры Docker командой `docker compose up -d`
4. Запустите приложение командой ``
5. Из папки проекта taski-pro-frontend запустите приложение командами `npm i`, `npm run dev`


### Настройка бота
Бот - это внешний пользователь системы. Он подключатеся по вебсокету, используя свой балансир
1. Зарегистрируйте бота как пользователя (через интерфейс или другим удобным способом)
\- не забудьте указать учетные данные в `.env - файле` или `application.properties`
2. Скачайте репозиторий командой 
2. Из папки проекта YazikochesalnaBot запустите приложение командой ``
3. Ищите бота по указанному никнейму и пользуйтесь с юмором!
<a name = "team"/>

## CI/CD
Данный проект работал при поддержке раннеров GitLab-CI

Для деплоя средствами GitHub и других ньюансов, пожалуйста, свяжитесь с DevOps-инженером 
Никитой Шапошниковым ([GitHub](https://github.com/Nikita22007), [Телеграм](https://t.me/Nikita22007))
 
# Умельцы

[Лиза Антипатрова](https://github.com/LizaAntipatrova) - Backend, DevOps, SA

[Никита Шапошников](https://github.com/Nikita22007) - Backend, DevOps

[Наташа Макушкина](https://github.com/Nathalie-mac) - SA, Backend

[Ирина Хрусталева](https://github.com/rubberPlant256) - Backend

[Вова Жильцов](https://github.com/Vladimirzhil) - Frontend, UI

[Никита Сахаров](https://github.com/NikitaSah18) - Frontend, UI


[Дима Брякин](https://github.com/razondark) - воевода, начальник, держатель сервера

💛☀️🌴Т-Банк, проектная мастерская, лето 2025🧋💛
