package frontier.action.pc;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.SearchcomForm;
import frontier.service.SearchcomService;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

public class SearchcomAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected SearchcomService searchcomService;
	@ActionForm
	@Resource
	protected SearchcomForm searchcomForm;
	public List<BeanMap> categorylist;
	public ActionMessages errors = new ActionMessages();
	public List<BeanMap> results;
	//検索画面で使用する。検索ボタンを押したかどうかを判断する。
	public boolean searchExeFlg;

	// ■初期表示
	@Execute(validator=false)
	public String index(){
		// パラメタ初期化
		reset();
		// init処理
		init();
		//検索未実行に設定
		searchExeFlg = false;
		// 検索画面表示処理へ
		return "result.jsp";
	}
	
	// ■検索ボタン押下
	@Execute(validator=false)
	public String search(){
		// init処理
		init();
		//検索実行に設定
		searchExeFlg = true;		
		// 検索結果を取得
		results = searchcomService.selectCommunities(searchcomForm.communityNameCondition,searchcomForm.communityDescriptionCondition);
		//コミュニティ説明装飾
		resetResults(results,"detail");
		
		// 検索結果一覧表示処理へ
		return "result.jsp";
	}

	// ■コミュニティ作成リンク押下
	@Execute(validator=false)
	public String make(){
		// パラメタ初期化
		reset();

		// init処理
		init();
		// コミュニティ作成画面へ
		return "make.jsp";
	}

	// ■この情報で作成するボタン押下時
	@Execute(validate="chkPic",input="error")
	public String finish() throws IOException, Exception{
		String cid;
		// init処理
		init();
		
		// コミュニティの登録＆画像アップロード
		cid = searchcomService.insertCommunities(
				userInfoDto.memberId,
				searchcomForm.comname,
				searchcomForm.cmnt,
				searchcomForm.category,
				searchcomForm.join,
				searchcomForm.pub,
				searchcomForm.auth,
				searchcomForm.picpath
		);

		// 作成したコミュニティトップへ遷移する
		return "/pc/com/top/" + cid + "/?redirect=true";
	}

	// エラー時の遷移先
	@Execute(validator=false)
	public String error(){
		// init処理
		init();
		// 自画面に戻る
		return "make.jsp";
	}

	// エラーチェック
	public ActionMessages chkPic(){
		FormFile picpath = searchcomForm.picpath;
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
	
	// 初期化処理
	private void reset(){
		searchcomForm.comname  = "";
		searchcomForm.cmnt     = "";
		searchcomForm.category = "";
		searchcomForm.join     = "";
		searchcomForm.pub      = "";
		searchcomForm.auth     = "";
		searchcomForm.communityNameCondition = "";
		searchcomForm.communityDescriptionCondition = "";
	}
	
	// init処理
	private void init(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		// カテゴリのリストを取得
		categorylist = searchcomService.selectItems("category");
	}
	
	//本文装飾
	private void resetResults(List<BeanMap> lbm,String property){
		// 削除対象のタグリスト(改行含)
		String[] deltags = {
				"\\n"
			};		
		
		//装飾タグの削除
		CmnUtility.editcmnt(lbm, property, null, null,null,null,null,null,appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,null,deltags);
		
	}
}