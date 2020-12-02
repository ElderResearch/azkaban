package azkaban.webapp.servlet.ceptor;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;

import azkaban.database.AzkabanDataSource;
import azkaban.database.DataSourceUtils;
import azkaban.utils.Props;
import azkaban.webapp.servlet.LoginAbstractAzkabanServlet;

public abstract class AbstractCEPtorServlet extends LoginAbstractAzkabanServlet {
	
	private static final Settings jooqSettings = new Settings().withRenderNameCase(RenderNameCase.LOWER);
	private static final Configuration jooqConfig = new DefaultConfiguration().set(jooqSettings);

	public static Configuration configureCEPtorDataConnection(Props p) {
		return jooqConfig.derive(azkabanToCeptorDataSource(p))
				// if using azkaban's datasource:
				.set(SQLDialect.valueOf(p.getString("database.type").toUpperCase()));
	}
	
	private static AzkabanDataSource azkabanToCeptorDataSource(Props p) {
		Props propsCopy = new Props(p);
		//this is how one would modify the MySQL connection database:
		//propsCopy.put("mysql.database", p.getString("mysql.database") + "_ceptor");
		return DataSourceUtils.getDataSource(propsCopy);
	}
}
