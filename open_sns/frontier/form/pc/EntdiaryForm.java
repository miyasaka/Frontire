package frontier.form.pc;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.Validwhen;

@Component(instance = InstanceType.SESSION)
public class EntdiaryForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Required
    @Maxlength(maxlength=100)
	public String title;
	
    @Required
    @Maxlength(maxlength=10000)
	public String comment;
    
    //カレンダー用
    public String diaryDay;
    public String calendarDay;
    public int pgcnt;
    public int offset;
    public boolean searchFlg;
    public Integer diaryid;
    //日記削除用
    public List<String> checkDiaryId;
    public String linkFrom;
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile photo1;

    @Binding(bindingType = BindingType.NONE)
	public FormFile photo2;

    @Binding(bindingType = BindingType.NONE)
	public FormFile photo3;
    
    public String pic1;
    public String pic2;
    public String pic3;

	public String entdate;
    public Integer photoNo;
	
    public String pubDiary;
    @Maxlength(maxlength=50)
    public String picnote1;
    @Maxlength(maxlength=50)
    public String picnote2;
    @Maxlength(maxlength=50)
    public String picnote3;
    
    @Required
    public String insPubLevel;
    public String appstatus;
    
    //新規作成・編集画面統合化
    /**
     * 新規作成・編集判定FLG<br>
     * 0:新規作成 1:編集
     **/
    public String entDiaryEditType;
}

