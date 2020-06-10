package azkaban.webapp.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jooq.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import azkaban.db.schema.tables.daos.ExecutionMetricsDao;
import azkaban.db.schema.tables.interfaces.IExecutionMetrics;
import azkaban.db.schema.tables.pojos.ExecutionMetrics;
import azkaban.server.session.Session;
import azkaban.webapp.AzkabanWebServer;
import lombok.Getter;
import lombok.val;



public class CEPtorSourceDetailServlet extends LoginAbstractAzkabanServlet{

	private static final Logger logger = LoggerFactory.getLogger(CEPtorSourceDetailServlet.class.getName());
	private static final long serialVersionUID = 1L;
	private ExecutionMetricsDao metricsDao;
	
	@Override
	  public void init(final ServletConfig config) throws ServletException {
	    super.init(config);
	    final AzkabanWebServer server = (AzkabanWebServer) getApplication();

	    
		metricsDao = new ExecutionMetricsDao(server.getJooqConfiguration());
	  }

	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp, Session session)
			throws ServletException, IOException {
		final String EXECUTION_ID_PARAM = "execution_id";
		final String SOURCE_DETAIL_PAGE = "azkaban/webapp/servlet/velocity/CEPtorsourcedetailpage.vm";

		
		final Page page =
		        newPage(req, resp, session,SOURCE_DETAIL_PAGE);
		
		int executionId = getIntParam(req, EXECUTION_ID_PARAM, 0);
		
		if(executionId == 0) {
			//TODO Handle default with no execution ID
		}
		
		
		val results = Lists.transform(metricsDao.fetchByExecutionId(executionId),ExecutionMetricView::new);
		page.add("metrics",results);
		
		logger.info("results: " + results.toString());
		logger.info("exection_id: {}", executionId);
		
		page.render();
		
	}

	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp, Session session)
			throws ServletException, IOException {		
	}

	@Getter
	private static class ExecutionMetricView extends ExecutionMetrics{
		private long metricValue;
		private long timestamp;
		
		public ExecutionMetricView(IExecutionMetrics m) {
			from(m);
			metricValue = m.getValue().longValue();
			timestamp = m.getMetricTime().longValue();
		}

	}
}
