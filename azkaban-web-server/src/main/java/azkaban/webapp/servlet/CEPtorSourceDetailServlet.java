package azkaban.webapp.servlet;

import azkaban.db.schema.tables.daos.ExecutionMetricsDao;
import azkaban.db.schema.tables.interfaces.IExecutionMetrics;
import azkaban.db.schema.tables.pojos.ExecutionMetrics;
import azkaban.executor.ExecutableFlow;
import azkaban.executor.ExecutorManagerAdapter;
import azkaban.executor.ExecutorManagerException;
import azkaban.project.ProjectManager;
import azkaban.server.session.Session;
import azkaban.webapp.AzkabanWebServer;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.format.DateTimeFormat;

import com.google.common.collect.Lists;



public class CEPtorSourceDetailServlet extends LoginAbstractAzkabanServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExecutionMetricsDao metricsDao;
	
	@Override
	  public void init(final ServletConfig config) throws ServletException {
	    super.init(config);
	    final AzkabanWebServer server = (AzkabanWebServer) getApplication();
	    //this.executorManagerAdapter = server.getExecutorManager();
	   
	    //this.projectManager = server.getProjectManager();
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
			// Handle default with no execution ID
		}
		
		
		page.add("metrics",Lists.transform(metricsDao.fetchByExecutionId(executionId),
				ExecutionMetricView::new));
		
		page.render();
		
	}

	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp, Session session)
			throws ServletException, IOException {		
	}

	@Getter
	private static class ExecutionMetricView extends ExecutionMetrics{
		private String color;
		
		public ExecutionMetricView(IExecutionMetrics m) {
			from(m);
			color = m.getValue().intValue() > 10 ? "#FFFFFF" : "#ABCDEF";
		}

	}
}
