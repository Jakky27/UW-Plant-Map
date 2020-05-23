package edu.uw.cs403.plantmap.backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SQLBootstrapper {
    private static final Set<String> REQUIRED_TABLES = Set.of("plant", "submission");

    private SQLConnectionPool pool;

    public SQLBootstrapper(SQLConnectionPool pool) {
        this.pool = pool;
    }

    public void createTablesOnFirstRun() {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            PreparedStatement getTablesStatement = conn.prepareStatement(
                    "select schema_name(t.schema_id) as schema_name,\n" +
                    "       t.name as table_name,\n" +
                    "       t.create_date,\n" +
                    "       t.modify_date\n" +
                    "from sys.tables t\n" +
                    "order by schema_name,\n" +
                    "         table_name;");

            Set<String> tables = resultsToTableSet(getTablesStatement.executeQuery());
            System.out.println("Tables found: " + tables);

            if (tables.equals(REQUIRED_TABLES))
                System.out.println("No database bootstrapping needed.");
            else
                runBootstrapSql(conn);

        } catch(SQLException e) {
            throw new RuntimeException("Failed to bootstrap database.");
        } finally {
            if (conn != null) {
                try {
                    pool.returnConnection(conn);
                } catch (Exception ignored) {

                }
            }
        }
    }

    private void runBootstrapSql(Connection conn) throws SQLException {
        System.out.println("Running bootstrap table creation schema.");

        String schema = new BufferedReader(
                new InputStreamReader(
                        this.getClass().getResourceAsStream("bootstrap.sql"),
                        StandardCharsets.US_ASCII))
                .lines()
                .collect(Collectors.joining("\n"));

        PreparedStatement bootstrapStatement = conn.prepareStatement(schema);
        bootstrapStatement.execute();
    }

    private Set<String> resultsToTableSet(ResultSet tableResults) throws SQLException {
        Set<String> tables = new TreeSet<>();

        while (tableResults.next()) {
            tables.add(tableResults.getString("table_name"));
        }

        return tables;
    }
}
