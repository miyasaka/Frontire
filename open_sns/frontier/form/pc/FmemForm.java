package frontier.form.pc;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class FmemForm implements Serializable {
	private static final long serialVersionUID = 1L;

	public String type;
	public String gid;
	public Integer offset;
	public Integer pgcnt;
}
