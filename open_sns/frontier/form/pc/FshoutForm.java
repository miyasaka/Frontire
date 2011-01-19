package frontier.form.pc;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class FshoutForm implements Serializable{
    private static final long serialVersionUID = 1L;
    public String mid;
    public String fscomment;
    public Integer fscheck;
    public String chktwitterflg;
    public Integer no;
    public Integer fsno;
	public Integer pgcnt; 
	public Integer offset; 
	public Integer limit; 
	public Integer resultscnt;

    public Integer fsPageFlg;
    public String selmid;
    public String selcomment;
    public Integer selno;
    
    public String selflg;
    // Twitter
    public String Chktwitter;
    public String myScreenName;
    public String userId;

}
