package azkaban.webapp.view;

import azkaban.db.schema.tables.interfaces.IExecutionMetrics;
import azkaban.db.schema.tables.pojos.ExecutionMetrics;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString(callSuper = true)
public class ExecutionMetricView extends ExecutionMetrics {
	private static final long serialVersionUID = 2L;
	
	public ExecutionMetricView(IExecutionMetrics m) {
		from(m);
	}

}