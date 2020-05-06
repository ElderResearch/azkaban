package azkaban.webapp.servlet;

import azkaban.executor.ExecutableFlow;
import azkaban.executor.ExecutorManagerAdapter;
import azkaban.executor.ExecutorManagerException;
import azkaban.project.ProjectManager;
import azkaban.server.session.Session;
import azkaban.webapp.AzkabanWebServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.format.DateTimeFormat;

public class CEPtorJobHistoryServlet extends LoginAbstractAzkabanServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExecutorManagerAdapter executorManagerAdapter;
	//private ProjectManager projectManager;
	
	@Override
	  public void init(final ServletConfig config) throws ServletException {
	    super.init(config);
	    final AzkabanWebServer server = (AzkabanWebServer) getApplication();
	    this.executorManagerAdapter = server.getExecutorManager();
	    //this.projectManager = server.getProjectManager();
	  }

	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp, Session session)
			throws ServletException, IOException {
		final Page page =
		        newPage(req, resp, session,
		            "azkaban/webapp/servlet/velocity/CEPtorjobhistorypage.vm");
		
		
		
		page.render();
		
	}

	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp, Session session)
			throws ServletException, IOException {		
	}

}
