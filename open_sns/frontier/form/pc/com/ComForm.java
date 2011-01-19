package frontier.form.pc.com;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;

@Component(instance = InstanceType.SESSION)
public class ComForm implements Serializable{
	private static final long serialVersionUID = 1L;
	public String cid;
	public String bbsid;
	public Integer pgcnt; 
	public Integer offset; 
	public Integer resultscnt;
	public Integer resultscntt; // 【TOP用】トピック数
	public Integer resultscnte; // 【TOP用】イベント数
	public Integer resultscntm; // 【TOP用】参加メンバー数
	
    @Maxlength(maxlength=10000)
	public String comment;
	public String reqComId;
	public String mlistflg;
	public String mid;
}