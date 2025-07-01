-- Создание keyspace (если его нет)
CREATE KEYSPACE IF NOT EXISTS storage_service_keyspace
    WITH REPLICATION = {
        'class': 'NetworkTopologyStrategy',
        'datacenter1': 1
        };

-- Подключение к keyspace (обязательно перед созданием таблиц!)
USE storage_service_keyspace;

-- Создание таблицы (если её нет)
CREATE TABLE IF NOT EXISTS my_table (
                                        id UUID PRIMARY KEY,
                                        data TEXT
);

-- Создание пользователя (если аутентификация включена)
CREATE USER IF NOT EXISTS user WITH PASSWORD '1234' NOSUPERUSER;
GRANT ALL PERMISSIONS ON KEYSPACE storage_service_keyspace TO user;