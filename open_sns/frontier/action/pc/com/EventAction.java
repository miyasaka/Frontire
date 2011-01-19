package frontier.action.pc.com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.EventForm;
import frontier.service.CommunityService;
import frontier.common.CmnUtility;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.taglibs.standard.extra.spath.Path;

public class EventAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public CommunityDto communityDto;
	// getでコミュニティIDが渡ってきた場合、設定
	public String cid;
	// entrytype:2 イベント
	private String entrytype = "2";
	public List<BeanMap> result;  // 単体用
	public List<BeanMap> results;
	public List<BeanMap> resultsEvent;
	public List<BeanMap> eventjoin;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected CommunityService communityService;
	@ActionForm
    @Resource
    protected EventForm eventForm;
    //今日の日付
    public String today = CmnUtility.getToday("yyyyMMdd");
    
    public String btnlevel;
   
    public String comment;
    public String listflg;
    
    public List<String> keepchkMemList;

	
	// ■初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// init処理
		init();
		eventForm.offset = 0;
		eventForm.pgcnt = 0;
		// 一覧表示処理へ
		return "sellist?redirect=true";
	}
	
	// ■次を見るリンク押下時
	@Execute(validator=false,urlPattern="nxtpg/{cid}")
	public String nxtpg(){
		try {
			// ページ遷移用の計算
			eventForm.pgcnt = eventForm.pgcnt + 1;
			eventForm.offset = eventForm.pgcnt * appDefDto.FP_COM_TOPICLIST_PGMAX;
		} catch (Exception e) {
			// 計算できない場合は初期値セット
			eventForm.pgcnt = 1;
			eventForm.offset = 0;
			// TODO: handle exception
		}
		// 一覧表示処理へ
		return "sellist?redirect=true";
	}
	
	// ■前を見るリンク押下時
	@Execute(validator=false,urlPattern="prepg/{cid}")
	public String prepg(){
		try {
			// ページ遷移用の計算
			eventForm.pgcnt = eventForm.pgcnt - 1;
			eventForm.offset = eventForm.pgcnt * appDefDto.FP_COM_TOPICLIST_PGMAX;
		} catch (Exception e) {
			// 計算できない場合は初期値セット
			eventForm.pgcnt = 1;
			eventForm.offset = 0;
			// TODO: handle exception
		}
		// 一覧表示処理へ
		return "sellist?redirect=true";
	}
	
	// 一覧表示処理
	@Execute(validator=false)
	public String sellist(){
		// 件数取得
		eventForm.resultscnt = communityService.cntBbsList(communityDto.cid,entrytype);
		// 一覧データ取得
		results = communityService.selectEventList(communityDto.cid,entrytype,eventForm.offset,appDefDto.FP_COM_TOPICLIST_PGMAX);
		
		// コメント編集(CmnUtility)
		CmnUtility.editcmnt(results,"comment","pic1","pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_CONTENTS_ROOT);
		return "list.jsp";
	}
	

	
	
	/////////■■■■■■■■■■■■■　　　　トピックアクションから　　　　　　　　■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	// ■■■■■■　　　　閲覧系処理　　　　■■■■■■
	// ■初期表示（閲覧表示・トピックタイトルリンククリック時)
	@Execute(validator=false,urlPattern="view/{cid}/{bbsid}")
	public String view(){
		// init処理
		init();
		// パラメタ初期化
		reset();
		// 閲覧表示処理へ
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// ■全てを表示リンククリック時
	@Execute(validator=false,urlPattern="viewall/{cid}/{bbsid}")
	public String viewall(){
		// init処理
		init();
		// allflgを立てる
		eventForm.allflg = "1";
		// 閲覧表示処理へ
		return "selview/0/0";
	}
	
	// ★★★閲覧表示処理★★★
	@Execute(validator=false,urlPattern="selview/{offset}/{limit}")
	public String selview(){
		// 掲示板・親情報の取得
		result = communityService.getEventOyaView(
			communityDto.cid,
			entrytype,
			eventForm.bbsid
		);
		// 掲示板・コメント情報の取得
		results = communityService.getCmntView(
			communityDto.cid,
			entrytype,
			eventForm.bbsid,
			eventForm.offset,
			eventForm.limit
		);
		// 掲示板・コメント数の取得
		eventForm.resultscnt = communityService.cntCmnt(
			communityDto.cid,
			entrytype,
			eventForm.bbsid
		);

		// コメント編集(CmnUtility)
		// CmnUtility.editcmnt(results,"comment","pic1","pic2","pic3");
		
		// イベント参加情報の取得
		eventjoin = communityService.getEventJoin (
			communityDto.cid,
			eventForm.bbsid
		);
		
		if(result.get(0).get("eventlevel").equals("1")){
			//イベント開催間に合う
			if(result.get(0).get("mid").equals(userInfoDto.memberId)){
				//管理者ユーザー
				btnlevel = "1";
			} else {
				if(eventjoin.size() > 0){
					//イベント参加済み
					btnlevel = "4";
				} else {
					//イベント未参加
					btnlevel = "2";
				}
			}
		} else {
			//イベント終了
			btnlevel = "3";
		}
		
		if(result.get(0).get("eventlimitover").equals("1")){
			//イベント開催日は間に合うが、募集期限を過ぎている
			btnlevel = "5";
		}
		
		return "view.jsp";
	}
	
	
	// ■【登録】コメントのみ書き込むボタン押下時
	@Execute(validate="chkPic",input="error")
	public String finish() throws IOException, Exception{
		// init処理
		init();
		// コメントの登録
		communityService.insCmnt(
			communityDto.cid,
			Integer.valueOf(eventForm.bbsid),
			eventForm.comment,
			eventForm.picpath1,
			eventForm.picpath2,
			eventForm.picpath3,
			eventForm.picnote1,
			eventForm.picnote2,
			eventForm.picnote3,
			entrytype
		);
		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// ■【登録】イベントに参加するボタン押下時
	@Execute(validate="chkPic",input="error")
	public String eventjoin() throws IOException, Exception{
		// init処理
		init();
		// コメントの登録
		communityService.insCmnt(
			communityDto.cid,
			Integer.valueOf(eventForm.bbsid),
			eventForm.comment,
			eventForm.picpath1,
			eventForm.picpath2,
			eventForm.picpath3,
			eventForm.picnote1,
			eventForm.picnote2,
			eventForm.picnote3,
			entrytype
		);
		// イベントの参加
		communityService.insJoin(
				communityDto.cid,
				Integer.valueOf(eventForm.bbsid)
			);		
		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// ■参加をキャンセルするボタン押下時
	@Execute(validate="chkPic",input="error")
	public String eventcancel() throws FileNotFoundException, IOException{
		// init処理
		init();
		//絵文字の装飾&サニタイジング
			//コメントを一時格納
		comment = eventForm.comment;
		//サニタイジング
		comment = CmnUtility.htmlSanitizing(comment);
		
		//逆サニタイジング
		comment = CmnUtility.reSanitizing(comment);
		
		//YouTubeタグ変換
		comment = CmnUtility.replaceYoutube(comment);
		
		//絵文字装飾
		comment = CmnUtility.replaceEmoji(comment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
		
		// 画像ＵＲＬに入力がない場合、画像説明はNULL
		//null対策
		if(eventForm.picpath1!=null){
			if(eventForm.picpath1.toString()==""){
				eventForm.picnote1 = null;
			}
		}
		//null対策
		if(eventForm.picpath2!=null){
			if(eventForm.picpath2.toString()==""){
				eventForm.picnote2 = null;
			}
		}
		//null対策
		if(eventForm.picpath3!=null){
			if(eventForm.picpath3.toString()==""){
				eventForm.picnote3 = null;
			}
		}
		
		// 確認画面へ遷移
		return "del.jsp";
	}
	
	// ■【登録】確認画面で「書き込む」ボタン押下時
	@Execute(validator=false)
	public String cancelevent() throws IOException, Exception{
		
		// init処理
		init();
		// コメントの登録
		communityService.insCmnt(
			communityDto.cid,
			Integer.valueOf(eventForm.bbsid),
			eventForm.comment,
			eventForm.picpath1,
			eventForm.picpath2,
			eventForm.picpath3,
			eventForm.picnote1,
			eventForm.picnote2,
			eventForm.picnote3,
			entrytype
		);
		// イベント参加のキャンセル
		communityService.delJoin(
				communityDto.cid,
				Integer.valueOf(eventForm.bbsid)
			);		

		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// ■確認画面で「キャンセル」ボタン押下時
	@Execute(validator=false)
	public String cancel(){
		// init処理
		init();
		// パラメタ初期化
		//reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}

	// ■自分のコメントを削除するリンク押下時
	@Execute(validator=false,urlPattern="delcmnt/{comno}")
	public String delcmnt(){
		// init処理
		init();
		// パラメタをリストにセットし削除確認画面へ
		eventForm.chkcmnt = new ArrayList<String>();
		eventForm.chkcmnt.add(eventForm.comno);
		// 選択したコメント情報の取得
		results = communityService.getChkCmnt(
			communityDto.cid,
			entrytype,
			eventForm.bbsid,
			eventForm.chkcmnt
		);
		
	    //表示権限チェック
	    if(check().equals("NG")){
	    	return "error.jsp";
	    }
		
		// 削除確認画面へ遷移
		return "confirm.jsp";
	}
	
	// 権限チェック
	public String check(){
		
		// 掲示板・親情報の取得
		result = communityService.getEventOyaView(
			communityDto.cid,
			entrytype,
			eventForm.bbsid
		);
		//管理人 or トピ設立：１、それ以外：０
		logger.debug("******** editflg１ ************"+result.get(0).get("editflg").toString());
		if(result.get(0).get("editflg").toString().equals("1")){
			return "NG";
		}
		if(results.size()!=1){
			return "NG";
		} else {
			if(!results.get(0).get("editflg").toString().equals("1")){
				return "NG";
			}
		}
		return "OK";
	}
	
	// ■チェックされたコメントを削除するリンク押下時
	@Execute(validator=false)
	public String delcmnts(){
		// init処理
		init();
		if(eventForm.chkcmnt == null) {
			// パラメタ初期化
			reset();
			// チェックボックスが選択されていない場合は閲覧画面へ
			return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
		} else {
			// チェックされたコメント情報の取得
			results = communityService.getChkCmnt(
				communityDto.cid,
				entrytype,
				eventForm.bbsid,
				eventForm.chkcmnt
			);
			// 削除確認画面へ遷移
			return "confirm.jsp";
		}
	}
	
	// ■【更新】確認画面で「削除する」ボタン押下時
	@Execute(validator=false)
	public String deletecmnts(){
		// init処理
		init();
		// コメント削除
		communityService.delcmnt(
			communityDto.cid,
			Integer.valueOf(eventForm.bbsid),
			eventForm.chkcmnt
		);
		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// ■■■■■■　　　　共通系処理　　　　■■■■■■
	// エラーチェック(画像)
	public ActionMessages chkPic(){
		ActionMessages errors = new ActionMessages();
		FormFile picpath1 = eventForm.picpath1;
		FormFile picpath2 = eventForm.picpath2;
		FormFile picpath3 = eventForm.picpath3;
		//null対策
		if(picpath1!=null){
			// 画像パス名の長さが0以上ならチェック
			if(picpath1.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath1,"1");
			}
		}
		//null対策
		if(picpath2!=null){
			// 画像パス名の長さが0以上ならチェック
			if(picpath2.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath2,"2");
			}
		}
		//null対策
		if(picpath3!=null){
			// 画像パス名の長さが0以上ならチェック
			if(picpath3.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath3,"3");
			}
		}
		return errors;
	}

	// エラー時の遷移先
	@Execute(validator=false)
	public String error(){
		// init処理
		init();
		// 閲覧表示処理へ
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// 初期化処理
	private void reset(){
		eventForm.pgcnt = 0;
		eventForm.comment = "";
		eventForm.allflg = "";
		eventForm.chkcmnt = null;
		eventForm.picnote1 = null;
		eventForm.picnote2 = null;
		eventForm.picnote3 = null;
		eventForm.chkmem = null;
	}
	
	// init処理
	private void init(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = eventForm.cid;
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
	}
	
	// イベント参加メンバーリスト初期表示
	@Execute(validator=false,urlPattern="mlist/{cid}/{bbsid}")
	public String mlist(){
		// init処理
		mlistinit();
		// 初期値を設定
		eventForm.offset = 0;
		eventForm.pgcnt = 0;
		
		// イベント参加メンバー一覧表示処理へ
		return "selmlist";
	}

	// ■ページング処理
	@Execute(validator=false,urlPattern="movelist/{cid}/{pgcnt}")
	public String movelist(){
		// init処理
		mlistinit();
		// 一覧表示処理へ
		return "selmlist";
	}

	// 一覧表示処理
	@Execute(validator=false)
	public String selmlist(){
		// 件数取得
		eventForm.resultscnt = communityService.cntEventMemList(communityDto.cid,eventForm.bbsid);
		// 一覧データ取得
		results = communityService.selectEventMemList(communityDto.cid,eventForm.bbsid, eventForm.offset, appDefDto.FP_COM_EVENTLIST_MEMMAX);
		// 掲示板・親情報の取得
		result = communityService.getEventOyaView(
			communityDto.cid,
			entrytype,
			eventForm.bbsid
		);
		
		// 参加メンバーリスト表示
		return "mlist.jsp";
	}

	
	
	// ■■■■■■■■■■　イベント参加者管理機能　■■■■■■■■■■■■■■■■■■■■■
	// イベント参加者管理画面
	// ■初期表示
	@Execute(validator=false,urlPattern="manage/{cid}/{bbsid}")
	public String manage(){
		// init処理
		mlistinit();
		// パラメタ初期化
		reset();
		
		// 閲覧表示処理へ
		return "selmanagelist";
	}
	
	// イベント参加者一覧表示処理（自分以外）
	@Execute(validator=false)
	public String selmanagelist(){
		// 件数取得
		eventForm.resultscnt = communityService.cntEventMemList(communityDto.cid,eventForm.bbsid);
		// 一覧データ取得
		results = communityService.selectEventMemList2(communityDto.cid,eventForm.bbsid, eventForm.offset, appDefDto.FP_COM_EVENTLIST_MEMMAX);
		// 掲示板・親情報の取得
		result = communityService.getEventOyaView(
			communityDto.cid,
			entrytype,
			eventForm.bbsid
		);
		
		// 参加メンバー数によって表示を変更
		if(results.size() > 0){
			listflg = "1";
		} else {
			listflg = "0";
		}
		
		// 参加メンバーリスト表示
		return "manage.jsp";
	}
	
	
	// ■参加者からはずすリンク押下時
	@Execute(validator=false)
	public String delmembers(){
		// init処理
		mlistinit();
		if(eventForm.chkmem == null) {
			// パラメタ初期化
			reset();
			// チェックボックスが選択されていない場合は閲覧画面へ
			return "selmanagelist";
		} else {
			// 掲示板・親情報の取得
			result = communityService.getEventOyaView(
				communityDto.cid,
				entrytype,
				eventForm.bbsid
			);
			// チェックされたメンバー情報の取得
			results = communityService.getChkMem(
				communityDto.cid,
				entrytype,
				eventForm.bbsid,
				eventForm.chkmem
			);

			// 削除確認画面へ遷移
			return "dellist.jsp";
		}
	}
	
	// ■【更新】確認画面で「削除する」ボタン押下時
	@Execute(validator=false)
	public String deletemembers() throws IOException, Exception{
		// init処理
		mlistinit();
		// イベント参加のキャンセル
		communityService.delJoinList(
				communityDto.cid,
				Integer.valueOf(eventForm.bbsid),
				eventForm.chkmem
		);
		
		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// ■確認画面で「キャンセル」ボタン押下時
	@Execute(validator=false)
	public String delcancel(){
		// 件数取得
		eventForm.resultscnt = communityService.cntEventMemList(communityDto.cid,eventForm.bbsid);
		// 一覧データ取得
		results = communityService.selectEventMemList2(communityDto.cid,eventForm.bbsid, eventForm.offset, appDefDto.FP_COM_EVENTLIST_MEMMAX);
		// 掲示板・親情報の取得
		result = communityService.getEventOyaView(
			communityDto.cid,
			entrytype,
			eventForm.bbsid
		);
		
		// 参加メンバー数によって表示を変更
		if(results.size() > 0){
			listflg = "1";
		} else {
			listflg = "0";
		}

		// 戻ったときにチェックボックスにチェックが入っていたメンバーにチェックを入れる制御
		keepchkMemList = eventForm.chkmem;
		// チェックボックスの名前の値をリセット
		eventForm.chkmem = null;
		
		// 参加メンバーリスト表示
		return "manage.jsp";
	}

	
	// init処理
	private void mlistinit(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = eventForm.cid;
		try {
			// offset設定
			eventForm.offset = eventForm.pgcnt*appDefDto.FP_COM_EVENTLIST_MEMMAX;
		} catch (Exception e) {
			// エラーになった場合は初期値を設定
			eventForm.offset = 0;
			eventForm.pgcnt = 0;
			// TODO: handle exception
		}
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
	}
	
	
	
	
	
	
}