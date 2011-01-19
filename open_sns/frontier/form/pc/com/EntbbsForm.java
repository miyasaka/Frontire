package frontier.form.pc.com;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class EntbbsForm implements Serializable {
	private static final long serialVersionUID = 1L;
	@Required
	public String cid;
	public String bbsid;
	@Required
	@Maxlength(maxlength=100)
	public String title;
	@Required
	@Maxlength(maxlength=10000)
	public String comment;
	@Binding(bindingType = BindingType.NONE)
	public FormFile picpath1;
	@Binding(bindingType = BindingType.NONE)
	public FormFile picpath2;
	@Binding(bindingType = BindingType.NONE)
	public FormFile picpath3;
	public String strpicpath1;
	public String strpicpath2;
	public String strpicpath3;
	@Maxlength(maxlength=50)
	public String picnote1;
	@Maxlength(maxlength=50)
	public String picnote2;
	@Maxlength(maxlength=50)
	public String picnote3;
	@Required(target="insevent,editevent")
	@Maxlength(maxlength=4)
	public String yearofevent;
	@Required(target="insevent,editevent")
	@Maxlength(maxlength=2)
	public String monthofevent;
	@Required(target="insevent,editevent")
	@Maxlength(maxlength=2)
	public String dayofevent;
	@Maxlength(maxlength=100)
	public String eventnote;
	@Maxlength(maxlength=100)
	public String locationnote;
	@Maxlength(maxlength=4)
	public String deadlineyear;
	@Maxlength(maxlength=2)
	public String deadlinemonth;
	@Maxlength(maxlength=2)
	public String deadlineday;

	public String edittype;     // 0:新規 1:編集
	
	public String eventmaker;     // 0:他人が立てたイベント 1:自分が立てたイベント

	public Integer pgcnt;
	public Integer offset;
	public Integer resultscnt;
	public Integer resultscntt; // 【TOP用】トピック数
	public Integer resultscnte; // 【TOP用】イベント数
	public Integer resultscntm; // 【TOP用】参加メンバー数
	public String selmid;       // 選択：イベント参加メンバー
	public String oyacid;       // 判別用親ウィンドウのcid
	public String oyabbsid;     // 判別用親ウィンドウのbbsid
	public String kocid;        // イベント参加メンバーウィンドウのcid
	public String kobbsid;      // イベント参加メンバーウィンドウのbbsid
	
	public void clear(){
		title         = "";
		comment       = "";
		picnote1      = "";
		picnote2      = "";
		picnote3      = "";
		strpicpath1   = "";
		strpicpath2   = "";
		strpicpath3   = "";
//		yearofevent   = "";
//		monthofevent  = "";
//		dayofevent    = "";
		eventnote     = "";
		locationnote  = "";
		deadlineyear  = "";
		deadlinemonth = "";
		deadlineday   = "";
		selmid        = "";
		oyacid        = "";
		oyabbsid      = "";
		offset        = 0;
		pgcnt         = 0;
		edittype      = "";
	}
	
}