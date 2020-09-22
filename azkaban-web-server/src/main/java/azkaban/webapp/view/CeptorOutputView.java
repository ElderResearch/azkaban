package azkaban.webapp.view;

import azkaban.db.schema.tables.pojos.CeptorOutput;
import azkaban.db.schema.tables.interfaces.ICeptorOutput;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString(callSuper = true)
public class CeptorOutputView extends CeptorOutput{
	private static final long serialVersionUID = 2L;
	
	public CeptorOutputView(ICeptorOutput a) {
		from(a);
	}
}
