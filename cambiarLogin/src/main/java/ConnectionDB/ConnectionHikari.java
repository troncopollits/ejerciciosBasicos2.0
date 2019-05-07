/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionDB;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;

/**
 *
 * @author artur
 */
public class ConnectionHikari {

    protected static Connection oConnection;
    protected static HikariDataSource oConnectionPool;
    protected static HikariConfig config;

    public static Connection newConnection() throws Exception {

        config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("bitnami");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(10);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setLeakDetectionThreshold(15000);
        config.setConnectionTestQuery("SELECT 1");
        config.setConnectionTimeout(2000);

        oConnectionPool = new HikariDataSource(config);
        return oConnectionPool.getConnection();

    }

    public ConnectionHikari() {

    }
}
