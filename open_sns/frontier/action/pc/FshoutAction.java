package frontier.action.pc;

import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.form.pc.FshoutForm;
import frontier.service.FshoutService;
import frontier.service.MembersService;
import frontier.service.OauthConsumerService;
import frontier.service.TwitterService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

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
	protected FshoutService fshoutService;
	@Resource
	public MembersService membersService;
	@Resource
	protected TwitterService twitterService;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	//twitter
	public Twitter twitter;

	//一覧系変数
	public List<BeanMap> fsnewList;

    //画面表示用
	public String fsNickname;
    public boolean vUser;

	@Execute(validator=false,urlPattern="list/{mid}/{fsPageFlg}")
	public String list(){
		if(fshoutForm.fsPageFlg == 1){
			//メンバー
			userInfoDto.visitMemberId = null;
			userInfoDto.visitMemberId = fshoutForm.mid;
		}

		//初期化
		fshoutForm.offset = 0;
		fshoutForm.pgcnt = 0;
		fshoutForm.chktwitterflg = "";

		if(fshoutForm.mid.equals(userInfoDto.memberId)){
			//エラー画面
			if(!fshoutForm.mid.equals(userInfoDto.memberId) && fshoutForm.fsPageFlg==0){
				return "error.jsp";
			}
		} else if(fshoutForm.mid.equals(userInfoDto.visitMemberId)){
			//エラー画面
			if(fshoutForm.mid.equals(userInfoDto.visitMemberId) && fshoutForm.fsPageFlg!=1){
				return "error.jsp";
			}
		}

		// init処理
		try {
			//初期設定
			initFshout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//共通処理
		initListFshout();
		return "/pc/fshout/list.jsp";
	}

	//自画面からの遷移用
	@Execute(validator=false)
	public String myView(){

		fshoutForm.selflg = "0";


		//初期化処理
		try {
			initFshout();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initListFshout();

		return "/pc/fshout/list.jsp";

	}


	// init処理
	private void initFshout() throws Exception{
		//パラメータのIDが自分かメンバーかを判断する
		if (fshoutForm.mid.equals(userInfoDto.memberId)){
			//画面表示用
			vUser = true;
		} else {
			//画面表示用
			vUser = false;
			//メンバーの場合
			Members members = membersService.getResultById(fshoutForm.mid);
			fsNickname = members.nickname;
		}

		// Twitter利用可能かどうかのチェックをする。
		//    0:未使用、1:Token削除、2:設定済み
		fshoutForm.Chktwitter = twitterService.checkUseTwitter(userInfoDto.memberId);
		// Twitter情報のセット
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

	//FShout一覧画面必須処理
	private void initListFshout(){
		//F Shout一覧検索
		fsnewList = fshoutService.selMemFShoutList("0",fshoutForm.mid,fshoutForm.offset,appDefDto.FP_CMN_FSLIST_CMNTMAX);
		//F Shout一覧件数取得
		fshoutForm.resultscnt = fshoutService.selFShoutCnt("2", fshoutForm.mid);
	}

	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setFshoutPage(1);

		//F5対策
		return "/pc/fshout/myView?redirect=true";
	}

	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setFshoutPage(-1);

		//F5対策
		return "/pc/fshout/myView?redirect=true";
	}

	//FShout一覧改ページ処理
	private void setFshoutPage(int num){
		try {
			// ページ遷移用の計算
			fshoutForm.pgcnt = fshoutForm.pgcnt + num;
			fshoutForm.offset = fshoutForm.pgcnt * appDefDto.FP_CMN_FSLIST_CMNTMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			fshoutForm.pgcnt = 1;
			fshoutForm.offset = 0;
		}
	}

	// F Shout 削除リンク押下時の処理
	@Execute(validator=false)
	public String delfshout(){
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
		return "/pc/fshout/list/" + userInfoDto.memberId + "/0?redirect=true";
	}

}
