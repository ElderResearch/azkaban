package azkaban.db.ceptor;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Logger;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

import azkaban.utils.Props;

public class CeptorDataSource {
	private Map<Integer, Configuration> jobConfigMap;
	private Props props;
	private static final String PROJECT_DB_FILE_NAME = "Project_";
	private static final Logger logger = Logger.getLogger("CeptorDataSource");
	
	public CeptorDataSource(Props p) {
		this.props = p;
	}
	
	public DSLContext getJobContext(int projectID) throws SQLException{
		String jdbcPath = "jdbc:sqlite:"
				+ props.getString("ceptor.database.path")
				+ "/" + PROJECT_DB_FILE_NAME + projectID + ".db";
		
		Connection conn = DriverManager.getConnection(jdbcPath);

		return new DefaultConfiguration()
				.set(SQLDialect.SQLITE)
				.set(conn).dsl();
	}
}
