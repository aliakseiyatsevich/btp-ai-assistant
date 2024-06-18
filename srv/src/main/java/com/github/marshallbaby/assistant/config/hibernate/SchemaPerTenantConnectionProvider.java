package com.github.marshallbaby.assistant.config.hibernate;

import com.github.marshallbaby.assistant.util.TenantSchemaNameBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchemaPerTenantConnectionProvider implements MultiTenantConnectionProvider {

    @Value("${multitenant.defaultTenant}")
    private String defaultTenant;

    private final DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {

        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {

        connection.close();
    }

    @Override
    public Connection getConnection(Object tenantId) throws SQLException {

        String tenantIdAsString = String.valueOf(tenantId);
        Connection connection = getAnyConnection();

        try {

            if (defaultTenant.equals(tenantIdAsString)) {

                log.info("Setting schema to [{}].", defaultTenant);
                connection.setSchema(defaultTenant);

            } else {

                log.info("Setting schema to [{}].", tenantIdAsString);
                connection.setSchema(TenantSchemaNameBuilder.buildSchemaName(tenantIdAsString));

            }

        } catch (SQLException e) {

            throw new HibernateException(format("Failed to update JDBC schema to [%s].", tenantId), e);
        }

        return connection;
    }

    @Override
    public void releaseConnection(Object tenantId, Connection connection) throws SQLException {

        log.info("Releasing connection for tenantId: [{}].", tenantId);

        try {

            connection.setSchema(defaultTenant);

        } catch (SQLException e) {

            throw new HibernateException(format("Failed to release connection" +
                    " for tenant with tenantId: [%s].", tenantId), e);
        }

        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
