package frontier.form.m;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.Validwhen;

@Component(instance = InstanceType.SESSION)
public class SetupForm  implements Serializable{
    private static final long serialVersionUID = 1L;
    public String hemail;
    public String hpasswd;
    public String hiddenParam;
    @Required(arg0 = @Arg(key="認証コード", resource=false))
    @IntegerType(msg=@Msg(key="認証コードは半角数字を入力してください。",resource=false),arg0 = @Arg(key="認証コード", resource=false))
    @Maxlength(maxlength=10,arg0 = @Arg(key="認証コード", resource=false))
    public String code;
    @Validwhen(test="(*this* == code)",msg=@Msg(key="認証コードと認証コードﾞ再確認が異なります。", resource = false))
    public String confirm;
}
