package frontier.form.m;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class DiaryMForm implements Serializable{	
    private static final long serialVersionUID = 1L;

    public String mid;
    public String diaryDay;
    //public String calendarDay;
    public int pgcnt;
    public int offset;
    public boolean searchFlg;
    public String picURL;
    
    @Required(msg=@Msg(key = "errors.deletecheckbox"),target="delete")
    public List<String> checkDiaryId;
    
    public Integer diaryId;
    //タイトルの文言出しわけに使用(0:絞りなし1:月まで2:日まで)
    public String titleFlg;
    
    @Required(arg0 = @Arg(key="コメント", resource=false),target="comConfirm")
    @Maxlength(maxlength=2000,arg0 = @Arg(key="コメント", resource=false),target="comConfirm")
    public String comment; 
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo1;
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo2;
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo3;
    
    public Integer preDiaryId;
    public Integer nextDiaryId;
    public String preDiaryDay;
    public String nextDiaryDay;
    
    @Maxlength(maxlength=50)
    public String picnote1;
    @Maxlength(maxlength=50)
    public String picnote2;
    @Maxlength(maxlength=50)
    public String picnote3;
    
    @Required(msg=@Msg(key = "errors.deletecheckbox"),target="delComment")
    public List<Integer> checkCommentNo;
        
    public void reset(){
    	checkDiaryId = null;
    	checkCommentNo = null;
    	//comment = null;
    }
    
    //何もしないダミーメソッド
    //resetされたくない場合に使用する。
    public void dummy(){
    	
    }
}
