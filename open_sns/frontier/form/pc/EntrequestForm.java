package frontier.form.pc;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;

@Component(instance = InstanceType.SESSION)
public class EntrequestForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Maxlength(maxlength=10000,arg0=@Arg(key="メッセージ",resource=false),target="send")
	public String comment;
    public String reqId;
}
