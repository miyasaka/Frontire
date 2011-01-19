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
public class EntphotoForm implements Serializable{
    private static final long serialVersionUID = 1L;
	
	@Required(target="insert,update")
	@Maxlength(maxlength=100)
	public String albumTitle;
	@Required(target="insert,update")
	@Maxlength(maxlength=10000)
	public String albumBody;
	public String level;
	public Integer ano;
	
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo1;
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo2;
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo3;

    @Binding(bindingType = BindingType.NONE)
	public FormFile photo4;
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo5;
    
	@Binding(bindingType = BindingType.NONE)
	public FormFile pic;
	
    //表紙変更画面用
    public Integer cano;
    public Integer cfno;
    public int pgcnt;
    public int offset;

	public String regist;
	public String strAno;
	public String strFno;
	public Integer fno;
	@Required(target="updatePhoto")
	@Maxlength(maxlength=100)
	public String picname;
	public String picPath;
    
    public void reset(){
    	albumTitle = "";
    	albumBody = "";
    	level = "1";
    }
}
