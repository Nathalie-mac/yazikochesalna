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
INIT_FLAG="/bitnami/cassandra/.initialized"

# Проверяем прошедшую инициализацию
if [ ! -f "$INIT_FLAG" ]; then

#INIT_SCRIPT="/docker-entrypoint-initdb.d/init-table.cql"

echo "=== Starting Cassandra initialization ==="

# 1. Ожидание 3 минуты
echo "1. Waiting ${WAIT_MINUTES} minutes before start..."
sleep $((WAIT_MINUTES * 60))

# 2. Подключение к Cassandra и выполнение команд
echo "2. Creating keyspace and deleting default cassandra user..."
cqlsh -u "$ADMIN_USER" -p "$ADMIN_PASS" <<CQL
CREATE KEYSPACE IF NOT EXISTS $KEYSPACE WITH REPLICATION = {'class': 'NetworkTopologyStrategy', '$DC_NAME': 1};
ALTER ROLE cassandra WITH SUPERUSER = false AND LOGIN = false;
LIST ROLES;
CQL

# 3. Создание таблиц и индексов
echo "3. Creating tables and indexes..."
cqlsh -u "$ADMIN_USER" -p "$ADMIN_PASS" <<-CQL
  USE $KEYSPACE;
  CREATE TABLE IF NOT EXISTS messages (
      id UUID,
      type TEXT,
      sender_id BIGINT,
      chat_id BIGINT,
      text TEXT,
      send_time TIMESTAMP,
      marked_to_delete BOOLEAN,
      PRIMARY KEY (id)
  );
  CREATE TABLE IF NOT EXISTS messages_by_chat (
      chat_id BIGINT,
      send_time TIMESTAMP,
      id UUID,
      text TEXT,
      sender_id BIGINT,
      type TEXT,
      marked_to_delete BOOLEAN,
      PRIMARY KEY ((chat_id), send_time, id)
  ) WITH CLUSTERING ORDER BY (send_time DESC);

  CREATE TABLE IF NOT EXISTS attachments (
      id UUID,
      message_id UUID,
      type TEXT,
      PRIMARY KEY ((message_id), id)
  );
CQL


mkdir -p "$(dirname "$INIT_FLAG")"
    touch "$INIT_FLAG"
    echo "=== Initialiation completed! ==="
else
    echo "=== Scip custom initialization scripts (flag $INIT_FLAG already exists) ==="
fi

echo "=== Start default Cassandra process ==="
exec /opt/bitnami/scripts/cassandra/run.sh