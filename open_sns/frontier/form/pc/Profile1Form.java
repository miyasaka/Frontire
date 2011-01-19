package frontier.form.pc;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.EmailType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.Validwhen;

@Component(instance = InstanceType.SESSION)
public class Profile1Form implements Serializable{
    private static final long serialVersionUID = 1L;
    @Required(arg0 = @Arg(key="PCアドレス", resource=false),target="touroku")
    @Maxlength(maxlength=100,arg0 = @Arg(key="PCアドレス",resource=false),target="touroku")
    @EmailType(arg0 = @Arg(key="PCアドレス",resource=false),target="touroku")
    public String email;
    @Maxlength(maxlength=100,arg0 = @Arg(key="携帯アドレス",resource=false),target="touroku")
    public String mobileemail;
    @Minlength(minlength=6)
    @Maxlength(maxlength=65)
    public String passwd;
    @Minlength(minlength=6)
    @Maxlength(maxlength=65)
    @Validwhen(test="(*this* == passwd)",msg=@Msg(key="ﾊﾟｽﾜｰﾄﾞとﾊﾟｽﾜｰﾄﾞ再確認が異なります。", resource = false))
    public String repasswd;
    
    public String encpass;
    public Integer resultscnt;
    public Integer resultscnt2;
    
    @Required(arg0 = @Arg(key="日記公開範囲",resource=false),target="touroku")
    @Maxlength(maxlength=1,arg0 = @Arg(key="日記公開範囲",resource=false),target="touroku")
    public String diaryLevel;
    
    
    public String mobileDomain;
    public String memail;
    
    public String dbpass;
    public String inpass;
    
    public String fshoutLevel;
    public String fshoutFrom;
    public String updateFrequency;
}
