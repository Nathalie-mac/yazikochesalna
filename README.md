<img alt="logo.png" height="200" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fmain.png" width="200" float="middle"/>

Корпоративный мессенджер с национальным колоритом

# Навигация

[1. О проекте](#description)

[2. Стек технологий](#stack)

[3. Документация](#docs)

[4. Развертывание](#deploy)

[5. Умельцы](#team)

<a name="description"/>

# О проекте

Языкочесальня - это корпоративный мессенджер и удобное пространство для общения. Все как в телеграме (может, чуть меньше😆)

Система развернута на микросервисах, каждый отвечает за свою часть работы (подробнее - в документации).

Между собой и веб-приложением микросервисы общаются по REST API

Есть поддержка CI/CD - конфигурации для Kubernetes-кластера с маршрутизацией запросов через Ingress(Nginx и API Gateway)

## Пользователям



**У нас есть:**\
👅 Поддержка групповых и личных чатов

👅 Кастомные аватарки пользователей и групп

👅 Мгновенная доставка сообщений на вебсокетах

👅 Бот, который возвращает все, что вы ему написали, обратно на древнерусском - 
чтоб возмущенные сообщения в рабочих диалогах стали звучать чуть легче

## Разработчикам
Проект находится в ~~разработке~~ доработке, и мы рады новым людям!
Вот список фич, которые еще не реализованы:
1. Вложения в сообщениях (готово API, в разработке на фронте)
2. Поддержка реплаев и закрепленных сообщений (готово API, в разработке на фронте)
3. Отображение последнего сообщения в диалоге в списке всех диалогов (главная страница) (готово API, в разработке на фронте)
4. Отображение статуса онлайн/офлайн (в разработке на беке)
5. Отображение количества непрочитанных сообщений (в разработке на беке)




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
<img alt="hyml.jpg" height="120" src="https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/pics%2Fhyml.png" width="110"/><br>
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
[Архитектура системы](https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/schemas/arch.md)

## Документация API

[Гугл-документ с внешними и внутренними АPI](https://docs.google.com/document/d/1jFatdmnWXy3r5CVKA3KB3qn8mYTJQARbS6X5yJyViLk/edit?tab=t.vo0g0576xmqx)

## UML-диаграммы 

[UseCase-диаграмма системы](https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/schemas/usecase.md)

## Базы данных

[Модель](https://github.com/Nathalie-mac/yazikochesalna/blob/main/docs/schemas/db.md) баз данных

<a name = "deploy"/>

# Развертывание

**Важное**\
Данный репозиторий содержит backend-часть приложения.\
frontend хранится по [ссылке](https://github.com/Vladimirzhil/Messanger)

## Локальное развертывание
### Основная система
1. Скачайте данный репозиторий командой `git clone https://github.com/Nathalie-mac/yazikochesalna.git`
2. Скачайте репозиторий веб-приложения командой `git clone https://github.com/Vladimirzhil/Messanger.git `
3. Из папки проекта (yazikochesalna) запустите контейнеры Docker командой `docker compose up -d`
4. Запустите каждый микросервис из соответсвующей папки (через IDE или консоль)
(совет: ApacheCassandra долго запускается, лучше подождать 2-3 мин)
5. Из папки проекта фронтенда (Messanger) запустите приложение командами `npm i`, `npm run dev`


### Настройка бота
Бот - это внешний пользователь системы. Он подключатеся по вебсокету, используя свой балансир
1. Зарегистрируйте бота в основной системе как пользователя (через интерфейс или другим удобным способом)
\- не забудьте указать учетные данные в `.env - файле` или `application.properties`
2. Скачайте репозиторий командой `git clone https://github.com/Nathalie-mac/YasikochesalnaBot.git` 
3. В проекте запустите сначала бота (bot), а затем балансир (bot-gateway)
4. Ищите бота по указанному никнейму и пользуйтесь с юмором!

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
