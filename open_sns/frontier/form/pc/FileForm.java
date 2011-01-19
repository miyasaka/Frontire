package frontier.form.pc;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class FileForm implements Serializable {
	private static final long serialVersionUID = 1L;

	public String fileid;
	public Integer historyno;
	public int pgcnt;
	public int offset;
	public Integer pano;
	public String condition;
	public String sortname;
	public int sortcd;
	
	public String strHistoryno;
}
