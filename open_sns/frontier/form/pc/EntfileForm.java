package frontier.form.pc;

import java.io.Serializable;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class EntfileForm implements Serializable {
	private static final long serialVersionUID = 1L;
	public String categoryid;
	@Maxlength(maxlength=100)
	public String inputcategory;
	@Required
	@Maxlength(maxlength=100)
	//public String title;
	public String filetitle;
	@Required
	@Maxlength(maxlength=10000)
	public String explanation;
	@Required
	@Binding(bindingType = BindingType.NONE)
	public FormFile filename;
	@Maxlength(maxlength=10)
	public String version;
	@Required
	public String pubfile;
	
	public String fileid;
	public Integer historyno;
	public boolean oyaflg;
	
	public String regist;
}
