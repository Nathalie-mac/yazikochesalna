#!/bin/bash

# Конфигурация
CASSANDRA_CONTAINER="cassandra_db"
WAIT_MINUTES=3
CASSANDRA_USER="cassandra"
CASSANDRA_PASS="cassandra"
ADMIN_USER="db_admin"
ADMIN_PASS="1234"
KEYSPACE="storage_service_keyspace"
DC_NAME="datacenter1"
INIT_SCRIPT="init-table.cql"

echo "=== Начало инициализации Cassandra ==="

# 1. Ожидание 3 минуты
echo "1. Ожидание ${WAIT_MINUTES} минут перед началом..."
sleep $((WAIT_MINUTES * 60))

# 2. Подключение к Cassandra и выполнение команд
echo "2. Создание администратора и keyspace..."
echo "2. Создание объектов..."
cqlsh -u "$CASSANDRA_USER" -p "$CASSANDRA_PASS" <<CQL
CREATE ROLE IF NOT EXISTS $ADMIN_USER WITH SUPERUSER = true AND LOGIN = true AND PASSWORD = '$ADMIN_PASS';
CREATE KEYSPACE IF NOT EXISTS $KEYSPACE WITH REPLICATION = {'class': 'NetworkTopologyStrategy', '$DC_NAME': 1};
GRANT ALL PERMISSIONS ON KEYSPACE $KEYSPACE TO $ADMIN_USER;
CQL

# 3. Отключение стандартного пользователя
echo "3. Отключение пользователя cassandra..."
cqlsh -u "$ADMIN_USER" -p "$ADMIN_PASS" <<CQL
ALTER ROLE cassandra WITH SUPERUSER = false AND LOGIN = false;
LIST ROLES;
CQL

# 4. Выполнение init-скрипта
echo "4. Проверка $INIT_SCRIPT..."
if [ -f "$INIT_SCRIPT" ]; then
    echo "Выполнение скрипта..."
    cqlsh -u "$ADMIN_USER" -p "$ADMIN_PASS" -f "$INIT_SCRIPT"
else
    echo "Файл $INIT_SCRIPT не найден!" >&2
    exit 1
fi

echo "=== Инициализация завершена успешно ==="