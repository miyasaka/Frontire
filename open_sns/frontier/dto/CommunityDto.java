package frontier.dto;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class CommunityDto implements Serializable {
    private static final long serialVersionUID = 1L;
    // コミュニティID
    public String cid;
    // コミュニティ名
    public String comnm;
    // コミュニティ画像
    public String pic;
    // トピック作成権限
    public String makabletopic;
    // 参加条件
    public String joincond;
}