package frontier.form.pc;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class FdiaryForm implements Serializable{
	private static final long serialVersionUID = 1L;

	public String diaryDay;
	public Integer pgcnt;
	public Integer offset;
	public String caltype;
	public String searchFlg;
	public String groupid;
}
