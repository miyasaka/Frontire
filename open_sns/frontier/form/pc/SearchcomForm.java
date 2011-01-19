package frontier.form.pc;

import java.io.Serializable;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class SearchcomForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Required
	@Maxlength(maxlength=15)
	public String comname;

	@Required
	@Maxlength(maxlength=10000)
	public String cmnt;

	@Required
	public String category;

	@Required
	public String join;

	@Required
	public String pub;

	@Required
	public String auth;

    @Binding(bindingType = BindingType.NONE)
	public FormFile picpath;
    
    public String communityNameCondition;
    public String communityDescriptionCondition;
    
}