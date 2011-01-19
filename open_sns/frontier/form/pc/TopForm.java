package frontier.form.pc;

import java.io.Serializable;
import java.util.List;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class TopForm implements Serializable{
	private static final long serialVersionUID = 1L;
	public String mid;
	public String rssurl;
	public String rtnval;
	public Integer rssno;
	public Integer _rssno;
	public String fscomment;
	public Integer fscheck;
	public Integer fskoukaihani;
	public Integer fsConfirm;
	public Integer fsNewListcnt;
	public Integer no;
	public Integer resultscnt;
	public String type;
	public Integer viewcnt;
	public String sortcd;
	public String pos;
	public String action;
	public String calendarDay;
	public String pfdomain;
	public String pfmid;
	public String pmid;
	public Integer fsCntResult;
	public Integer setFsCntResult;
	public Integer fsListFlg;
	public Integer fsConfirmMe;
	public String fstype;
	public Integer fsoffset;
	public Integer fsHyoujiCnt;
	public String ukey;
	public Integer updateAutoSec;
	public String vtype;
	public String vcmnt;
	public String vcmntview;
	public String vrepid;
	public String kptVal;
	public String kptVtype;
	//Twitterもっと読むに必要
	public Integer twPgOffset;
	public Long twMaxOldId;
	public Integer twPgFlg;
	public boolean tlMoreFlg;
	//Twitter更新処理に必要
	public Integer twUpdPgOffset;
	public Long twMaxNewId;
	//Twitter検索に必要
	public String twSearcTxt;
	//Twitter「フォロー」「フォロワー」改ページ
	public Long twFoCngPgPre;
	public Long twFoCngPgNxt;
	//public List<BeanMap> kptFsNewList;
	public List<BeanMap> kptFsUpdateList;
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
	public Integer defMyPhotoViewCnt;
	public Integer defFShoutViewCnt;
	public Integer defGroupViewCnt;
	public Integer defFollowmeViewCnt;
	public Integer defFollowyouViewCnt;
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
	public String defFShoutViewPos;
	public String defGroupViewPos;
	public String defFollowmeViewPos;
	public String defFollowyouViewPos;
	//表示タイプ
	public String defDisptypeCalendar;
	//Twitter
	public Integer myTlPage;
	public String userId;
	public String oldUserId;
	public long setTwId;
	public String setTwDelId;
	public Integer favoritePage;
	public String favoriteId;
	public Integer oneTlPage;
	public String screenName;
	public Integer cntGapCnt = 0;
	//public Integer memInfoFlg;
	public String myScreenName;
	public String folScreenName;
	public Integer twlistid;
	public Integer nxtpreflg;
	public Integer nxtflg;
	public Integer preflg;
	public String twfollowflg;
	public Boolean gapFlg = false; // ギャップ判定用フラグ(デフォルトはfalse)
	// 画面からのパラメタセット用
	public String VVFShoutTxt;  // 文字数カウント用
	public String VVKeywd;      // 検索項目(キーワード)
	public String VVAndOr;      // 検索項目(And Or)
	public String VVYYYYFrom;   // 検索項目(年 From)
	public String VVMMFrom;     // 検索項目(月 From)
	public String VVDDFrom;     // 検索項目(日 From)
	public String VVYYYYTo;     // 検索項目(年 To)
	public String VVMMTo;       // 検索項目(月 To)
	public String VVDDTo;       // 検索項目(日 To)
	public List<String> VVAId;  // 検索項目(アカウントIDリスト)
	public Integer VVPgcnt;     // 検索用変数(ページ数)
	public Integer VVOffset;    // 検索用変数(offset)
	public String VVMostNewSid; // 新着取得用ステータスID
	public String VVMostOldSid; // ギャップ取得用ステータスID
	public String VVSelMenuBtn; // プロフィールメニューのどのボタンが押されているか
	public String VVRepId;      // RE、RT時の返信元のIDセット(FShout、Twitter兼用)
	public String VVGapSidTo;   // ギャップ取得のStatusId to～
	public String VVGapSidFrom; // ギャップ取得のStatusId ～from
	public String VVTargetSid;  // ターゲットのステータスID
	public String VVLCursor;    // リストのページ遷移用パラメタ
	public String VVListId;     // リストID
	public String VVListNm;     // リスト名
	public String VVListDc;     // リスト説明
}