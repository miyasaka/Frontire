package frontier.action.m;

import frontier.common.CmnUtility;
import frontier.common.DecorationUtility;
import frontier.common.EmojiUtility;
import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.FrontierUserManagement;
import frontier.entity.Members;
import frontier.form.m.FshoutForm;
import frontier.service.FshoutService;
import frontier.service.MembersService;
import frontier.service.OauthConsumerService;
import frontier.service.TopService;
import frontier.service.TwitterService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class FshoutAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected FshoutForm fshoutForm;
	@Resource
	protected TwitterService twitterService;
	@Resource
	protected MembersService membersService;
	@Resource
	protected FshoutService fshoutService;
	@Resource
	protected TopService topService;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	//twitter
	public Twitter twitter;

	//一覧系変数
	protected Members MemberInfo;
	public List<BeanMap> profileList;
	public List<BeanMap> fsList;
	public List<BeanMap> fsDt;
	public List<BeanMap> MemberItemInfo;
	public Integer fsCnt;
	public String vNickname;
	// F Shoutデフォルト投稿先
	private String dtarget;
	// F Shoutデフォルト公開範囲
	public String dpublevel;

	// ■F Shout一覧(個別)
	@Execute(validator=false,urlPattern="{mid}",reset="resetall")
	public String index(){
		// ページタイプを0:ユーザ別F Shout一覧をセット
		fshoutForm.fslistpgType = "0";
		init();
		// Formパラメタセット
		setform();
		return "list.jsp";
	}

	// ■F Shout一覧
	@Execute(validator=false,reset="resetall")
	public String list(){
		// 自分の一覧を表示するためメンバーIDをセット
		fshoutForm.mid = userInfoDto.memberId;
		// ページタイプを0:F Shout一覧をセット
		fshoutForm.fslistpgType = "1";
		init();
		// Formパラメタセット
		setform();
		return "list.jsp";
	}

	// ■自分宛一覧
	@Execute(validator=false,reset="resetall")
	public String my(){
		// 自分の一覧を表示するためメンバーIDをセット
		fshoutForm.mid = userInfoDto.memberId;
		// ページタイプを1:F Shout自分宛一覧をセット
		fshoutForm.fslistpgType = "2";
		init();
		// Formパラメタセット
		setform();
		return "list.jsp";
	}

	// 次へリンク押下時
	@Execute(validator=false,reset="resetmove")
	public String nxt(){
		// 開始値(offset)の計算
		try {
			// ページ足し算
			fshoutForm.pgcnt += 1;
			// 開始値計算
			fshoutForm.offset = fshoutForm.pgcnt * appDefDto.FP_MY_M_FSHOUTLIST_PGMAX;
		} catch (Exception e) {
			// TODO: handle exception
		}
		init();
		// Formパラメタセット
		setform();
		return "list.jsp";
	}

	// 前へリンク押下時
	@Execute(validator=false,reset="resetmove")
	public String pre(){
		// 開始値(offset)の計算
		try {
			// ページ引き算
			if(fshoutForm.pgcnt > 0){
				// 0ページより大きければ計算
				fshoutForm.pgcnt -= 1;
			} else {
				// それ以外は0を設定
				fshoutForm.pgcnt = 0;
			}
			// 開始値計算
			fshoutForm.offset = fshoutForm.pgcnt * appDefDto.FP_MY_M_FSHOUTLIST_PGMAX;
		} catch (Exception e) {
			// TODO: handle exception
		}
		init();
		// Formパラメタセット
		setform();
		return "list.jsp";
	}


	// エラー時の処理
	@Execute(validator=false,reset="reset")
	public String rtnErr(){
		init();
		return "list.jsp";
	}

	// =================================== //
	//  ▼▼▼   F Shout関連処理    ▼▼▼ //
	// =================================== //
	// Shout!(投稿)
	@Execute(validator=true,input="rtnErr")
	public String fshout(){
		// Twitterへ登録時の結果取得用変数
		Status rtnStts = null;
		// Twitterへ登録した時に採番されたステータスIDセット用変数
		Object rtnSid = null;
		// 登録前のパラメタチェック(null or not程度)

		// 相手に確認を求める
		if(fshoutForm.confirmflg==null){
			fshoutForm.confirmflg=0;
		}
		// Twitterにも投稿
		if(fshoutForm.twitterflg==null){
			fshoutForm.twitterflg=0;
		}

		// Twitterへの登録処理(Twitterにも登録にチェックが入っていたら登録)
		if(fshoutForm.twitterflg==2){
			twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
			// Twitterへの登録処理
			String twitCmnt = fshoutForm.fscomment;  // 取得したコメント
			String replySid = fshoutForm.VVReplySid; // リプライ元のStatusID
			// デコード
			try {
				twitCmnt = URLDecoder.decode(twitCmnt, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// ========================== //
			//   ◆Twitterへの投稿処理◆  //
			// ========================== //
			// Twitterアカウントを持つメンバーのIDをTwitterアカウント名に変換
			twitCmnt = chgFidtoTid(twitCmnt);
			// 140文字で切り取り
			twitCmnt = new DecorationUtility().cutString(twitCmnt,140);
			// Twitterへ登録
			try {
				rtnStts = new TwitterUtility().entryStatus(twitter, twitCmnt,(Object)replySid);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 登録結果が取得出来ればFrontierDBへ登録を行う
			if(rtnStts != null){
				// ========================== //
				// ◆FrontierDBへの投稿処理◆ //
				// ========================== //
				// Twitterの発言のステータスID
				long tsid = rtnStts.getId();
				// FrontierShout用変数にセット
				rtnSid = (Object)tsid;
				// TwitterDBに登録
				twitterService.insTwitter(rtnStts,userInfoDto.memberId);
				// Twitter投稿管理テーブルに登録
				twitterService.insTwitterPostManagement(tsid,Integer.valueOf(fshoutForm.userId).intValue(),"1","0","0","0","0",userInfoDto.memberId,"0");
			}
		}

		// ----------------- //
		// コメントの装飾    //
		// ----------------- //
		// 改行コード削除
		fshoutForm.fscomment = fshoutForm.fscomment.replaceAll("\r\n","");

		// 絵文字装飾
		fshoutForm.fscomment = EmojiUtility.replaceMoblileToPc(
			fshoutForm.fscomment,
			appDefDto.FP_CMN_M_EMOJI_XML,
			userInfoDto.userAgent,
			appDefDto.FP_CMN_INNER_ROOT_PATH
		);

		// 返信元の値の設定
		if(fshoutForm.mid == null || fshoutForm.fsno == null || fshoutForm.mid.equals("") || fshoutForm.fsno.equals("")){
			// midとfsnoのどちらかの値が設定されて居ない場合は両方リセット
			fshoutForm.mid = null;
			fshoutForm.fsno = null;
			
		}
		// Shoutを登録
		fshoutService.insFSComment(
			userInfoDto.memberId,
			fshoutForm.fscomment,
			fshoutForm.twitterflg,
			fshoutForm.publevel,
			fshoutForm.confirmflg,
			(Object)fshoutForm.fsno,
			fshoutForm.mid,
			rtnSid
		);

		// Formパラメタリセット
		resetform();
		return setrtnurl() + "/redirect=true";
	}

	// F Shout 内容確認リンク押下時の処理
	@Execute(validator=false,urlPattern="confirm/{fsmid}/{fsno}")
	public String confirm(){
		// コメント更新
		try {
			topService.updConfirm(fshoutForm.fsmid,fshoutForm.fsno,userInfoDto.memberId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return setrtnurl() + "/redirect=true";
	}

	// F Shout 削除リンク押下時の処理
	@Execute(validator=false,urlPattern="del/{fsno}")
	public String del(){
		List<BeanMap> lb;    // データ格納用変数
		String twitFlg = null;      // Twitterへも投稿したか
		String twitStatusid = null; // Twitterへ投稿した際のステータスID
		// メンバーID、NOよりF Shoutデータ取得
		lb = fshoutService.selFShoutOne(userInfoDto.memberId,fshoutForm.fsno);
		// データが1件だけなら値をセット、それ以外ならスルー
		if(lb.size() == 1){
			if(lb.get(0).get("twitter") != null){
				twitFlg = lb.get(0).get("twitter").toString();
			}
			if(lb.get(0).get("statusid") != null){
				twitStatusid = lb.get(0).get("statusid").toString();
			}
		}
		// コメント削除
		try {
			fshoutService.delFSComment(userInfoDto.memberId,fshoutForm.fsno);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ツイート削除
		if(twitFlg.equals("1") && twitStatusid != null && !twitStatusid.equals("")){
			// Twitterへ投稿したフラグが立っていて、ステータスIDも取得出来たらツイート削除処理
			// Twitter、Frontierからツイート削除
			twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
			// ステータスIDをlong型へ変換
			long sid = Long.parseLong(twitStatusid);
			// Twitterサイトよりデータの削除
			try {
				new TwitterUtility().deleteStatus(twitter,sid);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// DB更新(削除フラグを立てる)
			twitterService.updTwitter(sid,fshoutForm.userId);
		}
		return setrtnurl() + "/redirect=true";
	}

	// F Shout 返信リンク押下時の処理
	@Execute(validator=false,urlPattern="re/{fsmid}/{fsno}",reset="resetall")
	public String re(){
		String rtnTxtView = "";
		// ページタイプが0:ユーザ別F Shout一覧だったら1をセット
		if(fshoutForm.fslistpgType.equals("0")){
			fshoutForm.fslistpgType = "1";
			// 自分の一覧を表示するためメンバーIDをセット
			fshoutForm.mid = userInfoDto.memberId;
		};
		// RE用 値取得処理
		fshoutForm.fscomment = getREtag(fshoutForm.fsmid);
		// --------------------------- //
		// 閲覧用テキスト置換処理      //
		// --------------------------- //
		// F Shoutコメント取得(mid、noより)
		rtnTxtView = getShout(fshoutForm.fsmid,fshoutForm.fsno);
		// リンクなしニックネーム置換
		rtnTxtView = fshoutService.replaceFSCommentNickName(rtnTxtView);
		// HTMLサニタイジング
		rtnTxtView = CmnUtility.htmlSanitizing(rtnTxtView);
		// 携帯用絵文字変換
		rtnTxtView = EmojiUtility.replacePcToMoblile(
			rtnTxtView,
			appDefDto.FP_CMN_M_EMOJI_XML,
			userInfoDto.userAgent,
			appDefDto.FP_CMN_INNER_ROOT_PATH
		);
		fshoutForm.vfscomment = rtnTxtView;
		// midに自分のIDをセット
		fshoutForm.mid = userInfoDto.memberId;
		init();
		// Formパラメタセット
		setform();
		// 戻り先は自分のFShout一覧画面へ
		return "list.jsp";
	}

	// F Shout RTリンク押下時の処理
	@Execute(validator=false,urlPattern="rt/{fsmid}/{fsno}",reset="resetall")
	public String rt(){
		// ページタイプが0:ユーザ別F Shout一覧だったら1をセット
		if(fshoutForm.fslistpgType.equals("0")){
			fshoutForm.fslistpgType = "1";
			// 自分の一覧を表示するためメンバーIDをセット
			fshoutForm.mid = userInfoDto.memberId;
		};
		// RT用 値取得処理
		fshoutForm.fscomment = getRTtag(fshoutForm.fsmid,fshoutForm.fsno);
		// midに自分のIDをセット
		fshoutForm.mid = userInfoDto.memberId;
		init();
		// Formパラメタセット
		setform();
		// 戻り先は自分のFShout一覧画面へ
		return "list.jsp";
	}

	// -------------------------------- //
	// **********  function  ********** //
	// -------------------------------- //
	// リターン先を設定
	private String setrtnurl(){
		String rtnurl = "";
		if(fshoutForm.fslistpgType.equals("0")){
			rtnurl = fshoutForm.mid;
		} else if(fshoutForm.fslistpgType.equals("1")){
			rtnurl = "list";
		} else if(fshoutForm.fslistpgType.equals("2")){
			rtnurl = "my";
		}
		return rtnurl;
	}

	// メンバータグ生成処理
	private String getREtag(String getmid){
		// リターン用変数
		String rtnval = "";
		Integer mid_int = null;
		// メンバーIDよりFrontierユーザ管理テーブル情報を取得
		FrontierUserManagement fum = new FrontierUserManagement();
		fum = membersService.getFrontierUserManagement(getmid);
		// データが取得できれば処理開始
		if(fum != null){
			// メンバーIDの型変換(String -> Integer)
			try {
				mid_int = Integer.valueOf(fum.mid.substring(1)).intValue();
			} catch (Exception e) {
				// TODO: handle exception
			}
			// RT形式に組み立て
			rtnval += "[@:" + fum.frontierdomain + "," + mid_int + "]";
		}
		return rtnval;
	}
	// RTタグ生成処理
	private String getRTtag(String getmid,Integer getfsno){
		// リターン用変数
		String rtnval = "";
		// メンバータグ取得
		rtnval = getREtag(getmid);
		// 取得したメンバータグが空じゃなければRTなど装飾
		if(!rtnval.equals("")){
			rtnval = " RT " + rtnval + " ";
		}
		// 特定Shoutコメント取得＆セット
		rtnval += getShout(getmid,getfsno);;
		return rtnval;
	}

	// 特定Shoutコメント取得処理
	private String getShout(String getmid,Integer getfsno){
		// リターン用変数
		String rtnval = "";
		// メンバーID、NOよりF Shoutデータ取得
		fsDt = fshoutService.selFShoutOne(getmid,getfsno);
		// データが1件だけなら値をセット、それ以外ならスルー
		if(fsDt.size() == 1){
			rtnval = fsDt.get(0).get("comment").toString();
		}
		return rtnval;
	}
	// -------------------------------- //
	// **********  function  ********** //
	// -------------------------------- //
	// =================================== //
	//  ▲▲▲   F Shout関連処理    ▲▲▲ //
	// =================================== //

	// =================================== //
	//  ▼▼▼   Twitter関連処理    ▼▼▼ //
	// =================================== //
	// 文中のFrontierアカウント -> Twitterアカウントへ変換
	private String chgFidtoTid(String getText){
		// 正規表現の適合調査結果の文字列格納用
		StringBuffer sb = new StringBuffer();

		// 型の定義(正規表現) [@:(Frontierドメイン),(メンバーID)]
		Pattern ptn = Pattern.compile("\\[@:(\\S+)\\,(\\S+)\\]");
		// マッチングクラス生成
		Matcher mt = ptn.matcher(getText);
		// 検索
		while(mt.find()){
			// Twitterアカウント名変数
			String getTwitScreenName = "";
			// マッチした型のFrontierドメイン部分(xxxxxxx.ne.jp部分)
			String mtFDomain = mt.group(1);
			// マッチした型のメンバーID部分(mXXXXXXXXXの'm'なし0トルツメ)
			String mtFid = mt.group(2);
			// mXXXXXXXXX型に変換
			mtFid = new DecorationUtility().stringFormat("m000000000",Integer.valueOf(mtFid).intValue());
			// Twitterアカウント名取得
			getTwitScreenName = oauthConsumerService.editMid(mtFDomain,mtFid);
			if(getTwitScreenName == null){
				// 取得できなければブランクを設定
				getTwitScreenName = "";
			} else {
				// 取得出来れば「@」を付与
				getTwitScreenName = "@"+getTwitScreenName;
			}
			// Twitterアカウントの置換を行う
			mt.appendReplacement(sb, getTwitScreenName);
		}
		// 正規表現の適合調査を行っていない残りの文字シーケンスを引数に指定した文字列バッファに追加
		// ※appendReplacementメソッドと一緒に利用
		mt.appendTail(sb);
		return sb.toString();
	}
	// =================================== //
	//  ▲▲▲   Twitter関連処理    ▲▲▲ //
	// =================================== //

	// init処理
	private void init(){
		// メンバーIDをセット
		String getMid = fshoutForm.mid;
		// 訪問メンバーIDにFormのメンバーIDを設定
		userInfoDto.visitMemberId = getMid;
		// メンバー情報取得
		MemberInfo = membersService.getResultById(getMid);

		dtarget = MemberInfo.target;
		vNickname = MemberInfo.nickname;

		// メンバー公開情報取得(自分の情報取得のためuserInfoDto.memberIdをキーとする)
		MemberItemInfo = topService.selectprofile(userInfoDto.memberId);
		// F Shout公開範囲
		dpublevel = MemberItemInfo.get(0).get("pubFshout").toString();

		// Twitter利用可能かどうかのチェックをする。
		//    0:未使用、1:Token削除、2:設定済み
		fshoutForm.Chktwitter = twitterService.checkUseTwitter(userInfoDto.memberId);

		// 自分が確認すべきF Shout数の取得
		fshoutForm.fscommentConfCnt = fshoutService.selFShoutCnt("3",userInfoDto.memberId);

		// タイプ別処理
		// 0:ユーザ別F Shout一覧取得
		if(fshoutForm.fslistpgType.equals("0")){
			// F Shout件数取得
			fsCnt = fshoutService.selFShoutCnt("2",getMid);
			// F Shoutデータ取得
			fsList = fshoutService.selMemFShoutList("1",getMid,fshoutForm.offset,appDefDto.FP_MY_M_FSHOUTLIST_PGMAX);

		// 1:F Shout一覧取得
		} else if(fshoutForm.fslistpgType.equals("1")){
			// F Shout件数取得
			fsCnt = fshoutService.selFShoutCnt("0",getMid);
			// F Shoutデータ取得
			fsList = fshoutService.selMyFShoutList("1",userInfoDto.memberId,fshoutForm.offset,appDefDto.FP_MY_M_FSHOUTLIST_PGMAX);

		// 2:自分宛一覧取得
		} else if(fshoutForm.fslistpgType.equals("2")){
			// F Shout件数取得
			fsCnt = fshoutService.selFShoutCnt("1",getMid);
			// F Shoutデータ取得
			fsList = fshoutService.selMyFShoutList("3",userInfoDto.memberId,fshoutForm.offset,appDefDto.FP_MY_M_FSHOUTLIST_PGMAX);

		}
		// 確認状況チェック
		fshoutService.chkconfirmFSComment(fsList);
	}

	// Formパラメタセット処理
	private void setform(){
		// メンバーがデフォルトで設定している値をセットする
		// Twitterにも投稿する(0:F Shoutのみ、1:Twitterのみ、2:F Shout + Twitter)
		if(dtarget.equals("1") || dtarget.equals("2")){
			fshoutForm.twitterflg = 2;
		}
		// 公開範囲の設定(0:外部、1:Frontier Netまで、2:ﾏｲFrontierまで、3:ｸﾞﾙｰﾌﾟまで)
		// 型が違うのでやむを得ず下記方法で処理。
		if(dpublevel.equals("1")){
			fshoutForm.publevel=1;
		}else if(dpublevel.equals("2")){
			fshoutForm.publevel=2;
		}else if(dpublevel.equals("3")){
			fshoutForm.publevel=3;
		}else{
			fshoutForm.publevel=0;
		}
		
		List<BeanMap> lbTwitter;
		// ユーザのTwitter情報を取得
		lbTwitter = oauthConsumerService.getTokens(userInfoDto.memberId,appDefDto.TWI_PROVIDER,"1",null);
		// Twitter設定していて、情報が取得出来たらパラメタの設定を行う
		if(fshoutForm.Chktwitter.equals("2") && lbTwitter != null){
			// メインに使用しているTwitterのアカウント名、ユーザIDの取得
			fshoutForm.myScreenName = lbTwitter.get(0).get("screenname").toString();
			fshoutForm.userId       = lbTwitter.get(0).get("twituserid").toString();
		}
	}

	// Formパラメタリセット処理
	private void resetform(){
		fshoutForm.mid        = null;
		fshoutForm.fsno       = null;
		fshoutForm.fscomment  = null;
		fshoutForm.vfscomment = null;
		fshoutForm.twitterflg = null;
		fshoutForm.publevel   = null;
		fshoutForm.confirmflg = null;
	}
}