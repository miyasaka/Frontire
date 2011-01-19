package frontier.form.pc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class NewsForm implements Serializable{
    private static final long serialVersionUID = 1L;
    public String informationflg;
    public Integer newsId;
    public Integer pgcnt;
    public Integer offset;
    public List<String> chkList;
    
    @Required
    @Maxlength(maxlength=100)
    public String title;
    @Required
    @Maxlength(maxlength=10000)
    public String detail;
	public String year;
	public String month;
	public String day;
	public String topflg;
	public String pageFlg;
	
    //変数初期化
    public void reset(){
    	title   = "";
    	detail  = "";
    	topflg  = "0";
    	pageFlg = "0"; //0:登録 1:編集
    	chkList = new ArrayList<String>();
    }
    //チェックボックスを外す処理
    public void clear(){
    	topflg = "0";
    }

}
