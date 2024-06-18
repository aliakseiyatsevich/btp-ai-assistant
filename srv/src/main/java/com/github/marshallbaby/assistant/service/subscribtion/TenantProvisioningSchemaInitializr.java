package com.github.marshallbaby.assistant.service.subscribtion;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.github.marshallbaby.assistant.util.TenantSchemaNameBuilder.buildSchemaName;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantProvisioningSchemaInitializr {

    private static final Pattern TENANT_ID_PATTERN = Pattern.compile("[-\\w]+");
    private static final String LIQUIBASE_MASTER_PATH = "liquibase/hana.changelog-master.yml";

    private final DataSource dataSource;

    public void initializeSchemaForTenant(String tenantId) {

        validateTenantId(tenantId);
        String schemaName = buildSchemaName(tenantId);

        try (Connection connection = dataSource.getConnection()) {

            createSchema(tenantId, schemaName, connection);
            setupSchema(tenantId, schemaName, connection);

        } catch (SQLException | LiquibaseException e) {

            String message = format("Failed to initialize schema while subscribing tenant: [%s].", tenantId);
            throw new RuntimeException(message, e);

        }


    }

    private void validateTenantId(String tenantId) {

        boolean isValid = Objects.nonNull(tenantId) && TENANT_ID_PATTERN.matcher(tenantId).matches();

        if (!isValid) {

            String message = format("Invalid tenant id: [%s]", tenantId);
            throw new IllegalStateException(message);
        }
    }

    private void createSchema(String tenantId, String schemaName, Connection connection) throws SQLException {

        log.debug("Creating schema for tenant [{}] with name [{}].", tenantId, schemaName);

        try (Statement statement = connection.createStatement()) {
            statement.execute(format("CREATE SCHEMA \"%s\"", schemaName));
        }

        log.info("Successfully created schema [{}] for tenant [{}].", schemaName, tenantId);
    }

    private void setupSchema(String tenantId, String schemaName, Connection connection) throws SQLException, LiquibaseException {

        log.debug("Setting up schema [{}] for tenant [{}].", schemaName, tenantId);

        connection.setSchema(schemaName);
        Database database = getDatabaseInstance(connection);
        Liquibase liquibase = new Liquibase(LIQUIBASE_MASTER_PATH, new ClassLoaderResourceAccessor(), database);
        liquibase.update(new Contexts(), new LabelExpression());

        log.info("Successfully set up schema [{}] for tenant [{}].", schemaName, tenantId);

    }

    @SneakyThrows
    private Database getDatabaseInstance(Connection connection) {

        return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
    }

}
