package frontier.action.pc.com;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.EntcomForm;
import frontier.service.CommunityService;
import frontier.service.EntprofileService;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

public class EntprofileAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public CommunityDto communityDto;
	@Resource
	protected CommunityService communityService;
	@ActionForm
	@Resource
	protected EntcomForm entcomForm;
	@Resource
	protected EntprofileService entprofileService;
	public ActionMessages errors = new ActionMessages();
	public List<BeanMap> categorylist;
	public List<BeanMap> results;
	public Map<String, Object> resultscdd;

	// ■初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// init処理
		init();
		// コミュニティ情報のセット
		setComDt();
		// 一覧表示処理へ
		return "join.jsp";
	}
	
	// ■画像の「削除」ボタン押下時
	@Execute(validator=false)
	public String delpic(){
		// init処理
		init();
		// 画像削除処理
		entprofileService.delpic(communityDto.cid,userInfoDto.memberId);
		// コミュニティ情報のセット
		setComDt();
		// 一覧表示処理へ
		return "join.jsp";
	}
	
	// ■「変更する」ボタン押下時
	@Execute(validate="chkPic",input="error")
	public String finish() throws IOException, Exception{
		// init処理
		init();
		// データ更新
		entprofileService.updcommunities(
			communityDto.cid,
			userInfoDto.memberId,
			entcomForm.comname,
			entcomForm.cmnt,
			entcomForm.category,
			entcomForm.join,
			entcomForm.pub,
			entcomForm.auth,
			entcomForm.picpath
		);
		// コミュニティ情報のセット
		setComDt();
		// TOP画面へ遷移
		return "/pc/com/top/"+communityDto.cid;
	}
	
	// ■「削除する」ボタン押下時
	@Execute(validate="chkBbs",input="error")
	public String delconfirm(){
		// init処理
		init();
		// コミュニティ情報のセット
		setComDt();
		// 削除確認画面へ
		return "del.jsp";
	}
	
	// ■確認画面で「やめる」ボタン押下時
	@Execute(validator=false)
	public String cancel(){
		// init処理
		init();
		// コミュニティ情報のセット
		setComDt();
		// TOP画面へ遷移
		return "/pc/com/top/"+communityDto.cid;
	}
	
	// ■確認画面で「削除する」ボタン押下時
	@Execute(validate="chkBbs",input="error")
	public String delete(){
		// init処理
		init();
		// 画像削除処理
		entprofileService.delComAll(communityDto.cid);
		// マイトップページへ
		return "/pc/top/";
	}
	
	// エラー時の遷移先
	@Execute(validator=false)
	public String error(){
		// init処理
		init();
		// 自画面に戻る
		return "join.jsp";
	}

	// エラーチェック(画像)
	public ActionMessages chkPic(){
		FormFile picpath = entcomForm.picpath;
		// 画像パス名の長さが0以上ならチェック
		//null対策
		if(picpath!=null){
			if(picpath.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath,"");
			}
		}
		return errors;
	}

	// エラーチェック(削除時のトピック数のチェック)
	public ActionMessages chkBbs(){
		// init処理
		init();
		// トピック数のチェック
		if(entcomForm.cntbbs!=0){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.com.com"));
		}
		return errors;
	}

	
	// コミュニティ情報の取得＆セット
	private void setComDt(){
		// 編集用、コミュニティ情報の取得
		// ■コミュニティ詳細データの取得(mapオブジェクト)
		resultscdd = communityService.getComDtDetails(communityDto.cid);
		// 各パラメタにセット
		entcomForm.comname    = resultscdd.get("title").toString();
		entcomForm.cmnt       = resultscdd.get("detail").toString();
		entcomForm.category   = resultscdd.get("category1").toString();
		entcomForm.join       = resultscdd.get("joincond").toString();
		entcomForm.pub        = resultscdd.get("publevel").toString();
		entcomForm.auth       = resultscdd.get("makabletopic").toString();
		entcomForm.strpicpath = resultscdd.get("pic").toString();
	}
	
	// init処理
	private void init(){
		// カテゴリのリストを取得
		categorylist = entprofileService.selectItems("category");
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = entcomForm.cid;
		// トピック、イベントの件数を取得
		entcomForm.cntbbs = communityService.cntBbsList(communityDto.cid,"1") + communityService.cntBbsList(communityDto.cid,"2");
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
	}
}