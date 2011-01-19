package frontier.form.pc.com;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class EventForm implements Serializable{
	private static final long serialVersionUID = 1L;
	@Required
	public String cid;
	public String bbsid;
	public String comno;
	@Required(arg0=@Arg(key="コメント",resource=false),target="finish")
	@Maxlength(maxlength=2000,arg0=@Arg(key="コメント",resource=false),target="finish")
	public String comment;
	@Binding(bindingType = BindingType.NONE)
	public FormFile picpath1;
	@Binding(bindingType = BindingType.NONE)
	public FormFile picpath2;
	@Binding(bindingType = BindingType.NONE)
	public FormFile picpath3;
	public Integer pgcnt; 
	public Integer offset; 
	public Integer limit; 
	public Integer resultscnt;
	public String allflg;
	public List<String> chkcmnt;
    @Maxlength(maxlength=50)
    public String picnote1;
    @Maxlength(maxlength=50)
    public String picnote2;
    @Maxlength(maxlength=50)
    public String picnote3;
    public List<String> chkmem;
}