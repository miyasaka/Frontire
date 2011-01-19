package frontier.form.pc;

import java.io.Serializable;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class PhotoForm implements Serializable {
    private static final long serialVersionUID = 1L;

    public String pmid;
    public int pgcnt;
    public int offset;
    public Integer pano;
    
    //フォトアルバム閲覧用
    public int viewPgcnt;
    public int viewOffset;
    
    //フォｔアルバム詳細用
	@Required(target="addComment")
	@Maxlength(maxlength=2000,target="addComment")    
    public String albumComment;
	@Required(msg=@Msg(key = "errors.deletecheckbox"),target="deleteComment")
	public String cno;
	@Required(msg=@Msg(key = "errors.deletecheckbox"),target="deleteCommentAll")
    public String[] cnoArray;
	public List<String> cnoList;
	public String strFno;
	public Integer fno;
	
    public void reset(){
    	albumComment = "";
    }
}
