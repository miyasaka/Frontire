package frontier.form.m;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class ScheduleForm implements Serializable{
    private static final long serialVersionUID = 1L;

    public String mid;
    public String cid;
    public String calendarDay;
    public String historyDay;
    public int pgcnt;
    public int offset;
	public Integer resultscnt; 
    public boolean searchFlg;
    public String yyyymmdd;

    public String nickname;
    public Integer sno;
    
    @Required
    @Maxlength(maxlength=100)
	public String title;
	
    @Maxlength(maxlength=10000)
	public String detail;
    
    public String titlecolor;
    
	public String startyear;
	public String startmonth;
	public String startday;
	public String starttime;
	public String startminute;
	public String endtime;
	public String endminute;
	
	public String publevel;
	
	public String friendstatus;
	public String pageid;
	public String status;
	public String chk01;
	//表示タイプ
	public String defDisptypeCalendar;
        
	public void reset(){
		chk01 = "";
	}
    
    //何もしないダミーメソッド
    //resetされたくない場合に使用する。
    public void dummy(){
    	
    }
    
    public void clear(){
		title=null;
		detail=null;
		starttime=null;
		startminute=null;
		endtime=null;
		endminute=null;
		titlecolor=null;
		publevel=null;
		pageid=null;
    }
}
