package azkaban.db.ceptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
	private static final String PROJECT_DB_FILE_NAME = "project_";
	private static final Logger logger = Logger.getLogger("CeptorDataSource");
	
	public CeptorDataSource(Props p) {
		this.props = p;
	}
	
	public Configuration getJobConfiguration(int projectID) {
		return jobConfigMap.computeIfAbsent(projectID, $ -> {
			Connection conn;
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:"
						+ props.getString("ceptor.databaserootpath") 
						+ "/" + PROJECT_DB_FILE_NAME + projectID);
				return new DefaultConfiguration()
						.set(SQLDialect.SQLITE)
						.set(conn);
			} catch (SQLException e) {
				logger.warning("Failed to connect to ceptor output DB for project " + projectID);
				return null;
			}
		});
	}
	
	
	public DSLContext getJobContext(int projectID) {
		return getJobConfiguration(projectID).dsl();
	}
}
