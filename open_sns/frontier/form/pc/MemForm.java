package frontier.form.pc;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.EmailType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.UrlType;

@Component(instance = InstanceType.SESSION)
public class MemForm implements Serializable{
    private static final long serialVersionUID = 1L;
    public String mid;
    public String vmid;
    public String clicktype;
    public int pgcnt;
    public int offset;
    public Integer resultscnt;
    
    public Integer setflg;
    public String setfdomain;
    public String setgid;
    
    public Integer followCnt;
    public Integer followerCnt;
    
    public String cid;
    public String frommid;
}
