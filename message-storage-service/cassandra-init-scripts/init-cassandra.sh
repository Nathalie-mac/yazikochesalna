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
#INIT_SCRIPT="/docker-entrypoint-initdb.d/init-table.cql"

echo "=== Начало инициализации Cassandra ==="

# 1. Ожидание 3 минуты
echo "1. Ожидание ${WAIT_MINUTES} минут перед началом..."
sleep $((WAIT_MINUTES * 60))

# 2. Подключение к Cassandra и выполнение команд
echo "2. Создание администратора и keyspace..."
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

# 4. Создание таблиц и индексов
echo "4. Создание таблиц и индексов..."
cqlsh -u "$ADMIN_USER" -p "$ADMIN_PASS" <<CQL
USE $KEYSPACE;

CREATE TABLE IF NOT EXISTS messages (
    id UUID not null,
    type TEXT not null,
    sender_id BIGINT not null,
    chat_id BIGINT not null,
    text TEXT,
    send_time TIMESTAMP not null,
    marked_to_delete BOOLEAN,
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS ON messages (sender_id);
CREATE INDEX IF NOT EXISTS ON messages (chat_id);
CREATE INDEX IF NOT EXISTS ON messages (send_time);

CREATE TABLE IF NOT EXISTS attachments (
    id BIGINT not null,
    message_id UUID not null,
    attachment_type TEXT,
    attachment TEXT,
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS ON attachments (message_id);

CQL

echo "=== Инициализация завершена успешно ==="
echo "=== Запуск основного процесса Cassandra==="
exec /opt/bitnami/scripts/cassandra/run.sh