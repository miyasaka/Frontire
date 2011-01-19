package frontier.form.m;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class TopForm implements Serializable{
    private static final long serialVersionUID = 1L;
    public String mid;
    public String rssurl;
    public String rtnval;
    public Integer rssno;
    
	public String type;
	public Integer viewcnt;
	public String sortcd;
	public String pos;
	public String action;
	public String calendarDay;
	//ﾃﾞﾌｫﾙﾄ件数
	public Integer defScheduleViewCnt;
	public Integer defMemberViewCnt;
	public Integer defCommunityViewCnt;
	public Integer defMemDiaryViewCnt;
	public Integer defMemDiaryCmntViewCnt;
	public Integer defCommunityBbsViewCnt;
	public Integer defCommunityBbsCmntViewCnt;
	public Integer defMemberUpdateViewCnt;
	public Integer defMyUpdateViewCnt;
	//ﾃﾞﾌｫﾙﾄソート順
	public String defCommunitySort;
	public String defCommunityCmntSort;
	public String defMemDiarySort;
	public String defMemDiaryCmntSort;
	public String defMemberUpdateSort;
	public String defMyUpdateSort;
	//デフォルト表示位置
	public String defScheduleViewPos;
	public String defMyPhotoViewPos;
	public String defMyUpdateViewPos;
	public String defMemberViewPos;
	public String defCommunityViewPos;
	public String defCommunityUpdateViewPos;
	public String defCommunityCmntViewPos;
	public String defDiaryUpdateViewPos;
	public String defDiaryCmntViewPos;
	public String defMemberUpdateViewPos;
	//表示タイプ
	public String defDisptypeCalendar;
	
	// FShout コメント
	@Required(target="fshout")
	@Maxlength(maxlength=240,target="fshout")
	public String fscomment;
	// 閲覧用 FShout コメント
	public String vfscomment;
	// FShout 相手に確認を求める
	public Integer confirmflg;
	// FShout 公開範囲
	public Integer publevel;
	// Twitterにも投稿
	public Integer twitterflg;
	// FShout No
	public Integer fsno;
	// FShout要確認Shout数
	public Integer fscommentConfCnt;
	// Twitterが利用かどうかチェック用
	public String Chktwitter;
	
	// ※ここからTwitter系変数
	// RE、RT時の返信元のStatusIDセット用
	public String VVReplySid;
	// ユーザID
	public String userId;
	// ユーザアカウント
	public String myScreenName;

    // リセット用メソッド
	// リセット
    public void reset(){
		confirmflg = null;
		publevel   = null;
		twitterflg = null;
	}
    // 全部リセット
    public void resetall(){
		fscomment  = null;
		vfscomment = null;
		confirmflg = null;
		publevel   = null;
		twitterflg = null;
	}
}