package frontier.form.pc;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class MlistForm implements Serializable{
    private static final long serialVersionUID = 1L;
    public String listmid;
    public Integer pgcnt; 
    public Integer offset; 
    public String type;
    public Integer results;
	public Integer resultscnt; 
	public String mid;
	public String gid;
	public String domain;
	public String friendno;
	public String vmid;
}
