package frontier.form.pc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class RssmanageForm implements Serializable{
	private static final long serialVersionUID = 1L;

	@Required(target="finsert,fupdate")
	@Maxlength(maxlength=50,target="finsert,fupdate")
	public String pname;
	@Required(target="finsert,fupdate")
	@Maxlength(maxlength=20,target="finsert,fupdate")
	public String fname;
	@Required(target="finsert,fupdate")
	@Maxlength(maxlength=200,target="finsert,fupdate")
	public String contents;
	public String rssid;
	public String searchFlg;
    public List<String> checkJoin;
    public String searchword;
    public String rd01;
    public String searchgroup;
	public Integer offset;
	public Integer pgcnt;
	public String sortname;
	
	public void reset(){
		pname = "";
		fname = "";
		contents = "";
		rssid = "";
		checkJoin = new ArrayList<String>() ;
		searchword = "";
		searchgroup = "";
	}
	
	public void resetSearch(){
		checkJoin = new ArrayList<String>();
	}

	
	public void dummy(){
		
	}
}
