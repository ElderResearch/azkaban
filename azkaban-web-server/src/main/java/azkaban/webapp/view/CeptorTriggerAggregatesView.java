package azkaban.webapp.view;

import azkaban.db.schema.tables.interfaces.ICeptorTriggerAggregates;
import azkaban.db.schema.tables.pojos.CeptorTriggerAggregates;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString(callSuper = true)
public class CeptorTriggerAggregatesView extends CeptorTriggerAggregates{
	private static final long serialVersionUID = 2L;
	
	public CeptorTriggerAggregatesView(ICeptorTriggerAggregates a) {
		from(a);
	}
}
