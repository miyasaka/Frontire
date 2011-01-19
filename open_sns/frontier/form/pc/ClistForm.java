package frontier.form.pc;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class ClistForm implements Serializable{
    private static final long serialVersionUID = 1L;
    public String vmid;
    public String listcid;
    public Integer pgcnt; 
    public Integer offset; 
    public Integer results;
	public Integer resultscnt; 
	public String type;
	public Integer viewcnt;
}
