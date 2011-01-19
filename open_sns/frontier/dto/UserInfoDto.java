package frontier.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String memberId;
    public String nickName;
	public Timestamp entdate;
	public String strEntdate;
	// 訪問中のメンバーID
    public String visitMemberId;
    //　メンバータイプ
    public String membertype;
    // ユーザエージェント
    public String userAgent;
    // 管理権限
    public String mlevel;
    // ===============================
    //  ■ Frontierユーザ管理テーブル情報
    // ===============================
    // Frontierドメイン
    public String fdomain;
    // 訪問中メンバーのメンバータイプ
    public String visitMemberType;

}
