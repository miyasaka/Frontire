package frontier.form.pc;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class RequestForm  implements Serializable{
    private static final long serialVersionUID = 1L;
    public String listmid;
    public Integer pgcnt; 
    public Integer offset; 
    public Integer results;
	public long resultscnt; 
	public String mid;
	public Integer cameToUrl;
	public String friendno;
	public String reqId;
	
	public String reqFlg;
	public String reqCid;
	
}
