package frontier.dto;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class FileDto implements Serializable {
	private static final long serialVersionUID = 1L;
	public String fileid;
	public String condition;
	public boolean isCategory;
}