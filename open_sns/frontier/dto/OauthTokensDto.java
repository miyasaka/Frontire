package frontier.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class OauthTokensDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // tokenをConsumerIdでMapで保存
    public HashMap<String,Map<String,String>> oauthTokens;
}
