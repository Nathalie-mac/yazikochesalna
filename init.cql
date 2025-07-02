ALTER KEYSPACE system_auth
    WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter-1': 1};

CREATE USER IF NOT EXISTS user WITH PASSWORD '1234' NOSUPERUSER;
GRANT ALL PERMISSIONS ON KEYSPACE storage_service_keyspace TO user;

