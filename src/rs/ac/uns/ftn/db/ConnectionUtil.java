package rs.ac.uns.ftn.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionUtil {
	private static HikariConfig hikariConfig = new HikariConfig();
	private static HikariDataSource hikariDS;

	static {
		hikariConfig.setJdbcUrl(ConnectionParams.LOCAL_CONNECTION_STRING);
		hikariConfig.setUsername(ConnectionParams.USERNAME);
		hikariConfig.setPassword(ConnectionParams.PASSWORD);
		hikariConfig.setMaximumPoolSize(10);
		hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariDS = new HikariDataSource(hikariConfig);
	}

	private ConnectionUtil() {
	}

	public static Connection getConnection() throws SQLException {
		return hikariDS.getConnection();
	}
}