package frontier.form.pc;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.EmailType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.Validwhen;

@Component(instance = InstanceType.SESSION)
public class EntryForm implements Serializable{
    private static final long serialVersionUID = 1L;
    @Required
    @Maxlength(maxlength=10)
    public String familyname;
    @Required
    @Maxlength(maxlength=10)
    public String name;
 
    @Required
    @Maxlength(maxlength=10)    
    public String nickname;
    @Required
    @Maxlength(maxlength=100)
    @EmailType
    public String email;
    @Required
    @Minlength(minlength=6)
    @Maxlength(maxlength=65)
    public String passwd;
    @Required
    @Minlength(minlength=6)
    @Maxlength(maxlength=65)
    @Validwhen(test="(*this* == passwd)",msg=@Msg(key="ﾊﾟｽﾜｰﾄﾞとﾊﾟｽﾜｰﾄﾞ再確認が異なります。", resource = false))
    public String repasswd;    
    
    public Integer resultscnt;
    public String encpass;
    public String newmid;
    
   @Required
    @Maxlength(maxlength=50)
    public String group;

    
}
