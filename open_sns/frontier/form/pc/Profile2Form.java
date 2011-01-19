package frontier.form.pc;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class Profile2Form implements Serializable{
    private static final long serialVersionUID = 1L;
    @Required(target="touroku")
    @Maxlength(maxlength=10)
    public String familyname;
    @Required(target="touroku")
    @Maxlength(maxlength=10)
    public String name;
    @Required(target="touroku")
    @Maxlength(maxlength=10)
    public String nickname;
    @Required(target="touroku")
    @Maxlength(maxlength=2)
    public String residenceregion;
    @Required(arg0 = @Arg(key="性別",resource=false),target="touroku")
    @Maxlength(maxlength=1,arg0 = @Arg(key="性別",resource=false),target="touroku")
    public String gender;
    @Required(target="touroku")
    public List<String> interest1;
    @Required(target="touroku")
    @Maxlength(maxlength=10000)
    public String aboutme;

    
 
    public String residenceregionCD;
    public String residencecityCD;
    @Maxlength(maxlength=3,target="touroku")
    public String residencecity;
    @Required(target="touroku")
    @Maxlength(maxlength=4)
    public String yearofbirth;         //誕生日・年
    @Required(target="touroku")
    @Maxlength(maxlength=2)
    public String monthofbirth;        //誕生日・月
    @Maxlength(maxlength=2,arg0=@Arg(key="誕生日(日)",resource=false),target="touroku")
    public String dayofbirth;          //誕生日・日
    @Maxlength(maxlength=1,arg0=@Arg(key="血液型",resource=false),target="touroku")
    public String bloodtype;           //血液型
    public String hometownregionCD;
    @Maxlength(maxlength=3,arg0=@Arg(key="出身地・都道府県",resource=false),target="touroku")
    public String hometownregion;      //出身地・都道府県
    public String hometowncityCD;
    @Maxlength(maxlength=3,arg0=@Arg(key="出身地・市区町村",resource=false),target="touroku")
    public String hometowncity;        //出身地・市区町村
    @Maxlength(maxlength=2,arg0=@Arg(key="職業",resource=false),target="touroku")
    public String occupation;          //職業
    @Maxlength(maxlength=100)
    public String company;             //所属
    @Maxlength(maxlength=2,arg0=@Arg(key="好きなジャンル1",resource=false),target="touroku")
    public String favgenre1;           //好きなジャンル１
    @Maxlength(maxlength=2,arg0=@Arg(key="好きなジャンル2",resource=false),target="touroku")
    public String favgenre2;           //好きなジャンル２
    @Maxlength(maxlength=2,arg0=@Arg(key="好きなジャンル3",resource=false),target="touroku")
    public String favgenre3;           //好きなジャンル３
    @Maxlength(maxlength=100)
    public String favcontents1;        //好きなジャンル内容１
    @Maxlength(maxlength=100)
    public String favcontents2;        //好きなジャンル内容２
    @Maxlength(maxlength=100)
    public String favcontents3;        //好きなジャンル内容３
    @Maxlength(maxlength=1,arg0=@Arg(key="公開レベル：名前",resource=false),target="touroku")
    public String nameLevel;
    @Maxlength(maxlength=1,arg0=@Arg(key="公開レベル：現住所",resource=false),target="touroku")
    public String locationLevel;
    @Maxlength(maxlength=1,arg0=@Arg(key="公開レベル：誕生日",resource=false),target="touroku")
    public String birthdayLevel;
    @Maxlength(maxlength=1,arg0=@Arg(key="公開レベル：生まれた年",resource=false),target="touroku")
    public String ageLevel;
    @Maxlength(maxlength=1,arg0=@Arg(key="公開レベル：出身地",resource=false),target="touroku")
    public String hometownLevel;
    @Maxlength(maxlength=1,arg0=@Arg(key="公開レベル：職業",resource=false),target="touroku")
    public String jobLevel;
    @Maxlength(maxlength=1,arg0=@Arg(key="公開レベル：性別",resource=false),target="touroku")
    public String genderLevel;
    public String dateofbirth;
    
	public Integer startYear;
	public Integer endYear;
	
	public String vmid;
    
    public String pic1;
    public String pic2;
    public String pic3;
    public String picno;
    
    @Binding(bindingType = BindingType.NONE)
	public FormFile picpath;
    
    public String setresidenceregion;
    public String sethometownregion;
}
