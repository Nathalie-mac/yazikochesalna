FROM bitnami/cassandra:latest
ENV CASSANDRA_USER=db_admin \
    CASSANDRA_PASSWORD=1234 \
    CASSANDRA_PASSWORD_SEEDER=yes \
    CASSANDRA_CLUSTER_NAME=MyCluster \
    CASSANDRA_DATACENTER=datacenter1 \
    CASSANDRA_AUTHENTICATOR=PasswordAuthenticator \
    CASSANDRA_AUTHORIZER=CassandraAuthorizer \
    CASSANDRA_ENABLE_MATERIALIZED_VIEWS=true

COPY cassandra-init-scripts/ /docker-entrypoint-initdb.d/
EXPOSE 9042
