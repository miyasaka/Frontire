package frontier.form.pc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class GroupForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Required(target="insert,updateGroup")
	@Maxlength(maxlength=50,target="insert,updateGroup")
	public String gname;
    @Binding(bindingType = BindingType.NONE)
	public FormFile picpath;
    public List<String> checkJoin;
    public List<Object> checkAuth;
	public String gid;
	public String searchmem;
	public String r1;
	public String joincheck;
	public Integer offset;
	public Integer pgcnt;
	public String pic;
	public Integer joinnumber;
	public String sortname;
	
	public void reset(){
		gname = null;
		checkJoin = new ArrayList<String>() ;
		checkAuth = new ArrayList<Object>();
		joincheck = null;
		searchmem = null;
	}
	
	public void resetSearch(){
		checkJoin = new ArrayList<String>();
		checkAuth = new ArrayList<Object>();
	}
	
	public void dummy(){
		
	}
}
