package frontier.action.pc;

import java.util.List;
import javax.annotation.Resource;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.h2.util.StringUtils;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.form.pc.PhotoForm;
import frontier.service.MembersService;
import frontier.service.PhotoService;
import frontier.service.CommonService;

public class PhotoAction {
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected MembersService membersService;
	@Resource
	protected PhotoService photoService;
	@Resource
	protected CommonService commonService;

	@ActionForm
	@Resource
	protected PhotoForm photoForm;

    //画面表示用
    public String vNickname;
    //フォトアルバム一覧検索結果
    public List<BeanMap> results;
    public long resultscnt;
    public long photoResultscnt;
    public boolean vUser;
    public boolean vFriend;
    //フォトアルバム閲覧（アルバム表紙）
    public List<BeanMap> coverResults;
    //フォトアルバム閲覧（フォト一覧）
    public List<BeanMap> photoResults;
    //フォトアルバム詳細(コメント一覧）
    public List<BeanMap> commentResults;
	//写真詳細検索結果
	public List<BeanMap> viewphotoResults;
    //権限
	public boolean isAuth;
	public String unauthMsg = null;
	public boolean isPrevViewphoto;
	public boolean isNextViewphoto;

    //内部制御用
    private Members members;
    public ActionMessages errors = new ActionMessages();	//	エラーメッセージ

    //フォトアルバム一覧初期表示
	@Execute(validator=false,urlPattern="list/{pmid}")
	public String list(){
		//初期化処理
		initList();

		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//DB検索
		initListPhoto(0);

		return "/pc/photo/list.jsp";
	}

	//画面再表示用
	@Execute(validator=false)
	public String reList(){
		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//DB検索
		initListPhoto(photoForm.offset);

		return "/pc/photo/list.jsp";
	}

	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setPhotoPage(1);

		//F5対策
		return "/pc/photo/reList?redirect=true";
	}

	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setPhotoPage(-1);

		//F5対策
		return "/pc/photo/reList?redirect=true";
	}

	//フォトアルバム閲覧画面初期表示用
	@Execute(validator=false,urlPattern="view/{pmid}/{pano}")
	public String view(){
		//初期化処理
		initList();

		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//フォトアルバム閲覧必須処理
		initViewPhoto();

		return "/pc/photo/view.jsp";
	}

	//フォトアルバム閲覧画面再表示用
	@Execute(validator=false)
	public String reView(){

		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//フォトアルバム閲覧必須処理
		initViewPhoto();

		return "/pc/photo/view.jsp";
	}

	//日記閲覧・次を表示リンク押下時
	@Execute(validator=false)
	public String viewNxtpg(){
		//改ページ処理実行
		setPhotoViewPage(1);

		//F5対策
		return "/pc/photo/reView?redirect=true";
	}

	//日記閲覧・前を表示リンク押下時
	@Execute(validator=false)
	public String viewPrepg(){
		//改ページ処理実行
		setPhotoViewPage(-1);

		//F5対策
		return "/pc/photo/reView?redirect=true";
	}

	//フォトアルバム詳細画面初期表示
	@Execute(validator=false,urlPattern="detail/{pmid}/{pano}")
	public String detail(){

		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initDetail();
		//フォトアルバム詳細必須処理
		initDetailPhoto();
		// 既読・未読FLG更新
		photoService.updPhotoCommentReadflg(photoForm, coverResults);

		return "/pc/photo/detail.jsp";
	}

	//フォトアルバム詳細画面初期表示
	@Execute(validator=false)
	public String errDetail(){

		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//フォトアルバム詳細必須処理
		initDetailPhoto();

		return "/pc/photo/detail.jsp";
	}

	//フォトアルバム詳細（コメント登録）
	@Execute(validator=true,input="errDetail")
	public String addComment(){

		//DB検索結果を取得
		List<BeanMap> cntResults = photoService.selMaxCno(photoForm.pmid,photoForm.pano);
		//コメントNOの最大値取得
		Integer cno = (Integer)cntResults.get(0).get("cno");

		//フォトアルバムコメント登録
		photoService.insPhotoComment(photoForm.pmid, photoForm.pano, cno, photoForm.albumComment,userInfoDto.memberId);

		return "/pc/photo/detail/"+photoForm.pmid+"/"+photoForm.pano+"?redirect=true";
	}

	/**
	 * フォトアルバム詳細 初期化
	 */
	public void initDetail(){
		photoForm.cnoArray = null;
		photoForm.cnoList  = null;
	}

	/**
	 * フォトアルバム詳細 コメント削除確認 (String)
	 * @return
	 */
	@Execute(validate="checkPhotoComment", input="errDetail", urlPattern="deleteComment/{cno}")
	public String deleteComment(){
		return "/pc/photo/delcomment?redirect=true";
	}

	/**
	 * フォトアルバム詳細 コメント削除確認 (String[])
	 * @return
	 */
	@Execute(validate="checkPhotoComment", input="errDetail")
	public String deleteCommentAll(){
		return "/pc/photo/delcomment?redirect=true";
	}

	/**
	 * フォトアルバムコメント削除確認 初期表示
	 * @return
	 */
	@Execute(validator=false)
	public String delcomment(){
		commonProcessDelete();
		return "/pc/photo/delcomment.jsp";
	}

	/**
	 * フォトアルバムコメント削除 コメントNOリストセット
	 * @param photoForm
	 */
	public void initDelcomment(){
		//photoForm.cnoList  = photoService.arrayToList(photoForm.cnoArray);
		photoForm.cnoArray = null;
	}

	/**
	 * フォトアルバコメント削除確認 共通処理
	 */
	public void commonProcessDelete(){
		initDelcomment();
		commentResults = photoService.selPhotoComment(photoForm, photoForm.cnoList);
		setEmojiSanitizing("comment", "viewComment", commentResults);
	}

	/**
	 * フォトアルバコメント削除確認 削除実行
	 * @return
	 */
	@Execute(validator=false)
	public String deleteCommentExecute(){
		photoService.deletePhotoComment(photoForm);
		return "/pc/photo/detail/" + photoForm.pmid + "/" + photoForm.pano + "?redirect=true";
	}

	/**
	 * フォトアルバコメント削除確認 やめる
	 * @return
	 */
	@Execute(validator=false)
	public String stopComment(){
		return "/pc/photo/detail/" + photoForm.pmid + "/" + photoForm.pano + "?redirect=true";
	}

	/**
	 * フォトアルバムコメント削除 エラーチェック
	 * @return errors
	 */
	public ActionMessages checkPhotoComment(){
		if(!StringUtils.isNullOrEmpty(photoForm.cno)){
			photoForm.cnoArray = photoForm.cno.split(",");
			photoForm.cno      = null;
		}
		photoForm.cnoList = photoService.arrayToList(photoForm.cnoArray);
		List<String> validCnoList = photoService.generateCnoList(photoForm);
		if(validCnoList == null || validCnoList.size() == 0){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.deletecheckbox" ,"コメント"));
		}
		return errors;
	}

	//フォトアルバム系画面に必要な初期設定
	private void initPhoto() throws Exception{

		//メニュー出しわけ用変数設定
		setVisitMemberId();

		//パラメータのIDが自分かメンバーかを判断する
		if (photoForm.pmid.equals(userInfoDto.memberId)){
			//自分の場合
			vNickname = userInfoDto.nickName;

			//画面表示用
			vUser = true;
		} else {
			//画面表示用
			vUser = false;

			//メンバーの場合
			//ユーザの基本情報の取得
			members = membersService.getResultById(photoForm.pmid);
			if (members != null){
				vNickname = members.nickname + "さん";
			} else {
				Exception e = new Exception();
				throw e;
			}

			//グループメンバーのＩＤ取得
			List<BeanMap> lbm = commonService.getMidList("1", userInfoDto.memberId);

			//自分の所属グループのメンバーかを調査する。
			for (int i=0;i<lbm.size();i++){
				if (lbm.get(i).get("mid").equals(photoForm.pmid)){
					//一致すればグループのメンバー
					vFriend = true;
					return;
				}
			}

			//他人の場合
			vFriend = false;

		}

	}

	//メニュー出しわけ用変数設定
	private void setVisitMemberId(){
		userInfoDto.visitMemberId = photoForm.pmid;
	}

	//フォトアルバム一覧画面必須処理
	private void initListPhoto(int offset){
		results = photoService.selPhotoList(photoForm.pmid, offset);
		resultscnt = photoService.cntPhotoList(photoForm.pmid);

		//取得結果編集
		//setEmojiSanitizing("detail","viewDetail",results);
		CmnUtility.editDetail(results, "detailorg", appDefDto.FP_CMN_EMOJI_XML_PATH, appDefDto.FP_CMN_LIST_CMNTMAX, appDefDto.FP_CMN_EMOJI_IMG_PATH);
	}

	//フォトアルバム閲覧画面必須処理
	private void initViewPhoto(){
		coverResults = photoService.selPhotoCover(photoForm.pmid, photoForm.pano);
		photoResults = photoService.selPhoto(photoForm.pmid, photoForm.pano,photoForm.viewOffset);
		photoResultscnt = photoService.cntPhoto(photoForm.pmid, photoForm.pano);

		//取得結果編集
		setEmojiSanitizing("detail","viewDetail",coverResults);

	}

	//フォトアルバム詳細画面必須処理
	private void initDetailPhoto(){
		coverResults = photoService.selPhotoCover(photoForm.pmid, photoForm.pano);
		commentResults = photoService.selPhotoComment(photoForm, null);
		photoResults = photoService.selPhoto(photoForm.pmid, photoForm.pano,0);

		//取得結果編集
		setEmojiSanitizing("detail","viewDetail",coverResults);
		setEmojiSanitizing("comment","viewComment",commentResults);
	}

	//絵文字装飾とサニタイジング
	private void setEmojiSanitizing(String colmun,String viewColumn,List<BeanMap> results){
		String txt = "";

		//絵文字の装飾&サニタイジング
		for (int i=0;i<results.size();i++){
			//コメントを一時格納
			txt = (String)results.get(i).get(colmun);

			//サニタイジング
			txt = CmnUtility.htmlSanitizing(txt);

			//URLを<a>タグに変換
			txt = CmnUtility.convURL(txt);

		    //絵文字装飾
			txt = CmnUtility.replaceEmoji(txt,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);

		  	//BeanMapへ格納
		  	results.get(i).put(viewColumn, txt);
		}
	}

	//フォトアルバム一覧改ページ処理
	private void setPhotoPage(int num){
		try {
			// ページ遷移用の計算
			photoForm.pgcnt = photoForm.pgcnt + num;
			photoForm.offset = photoForm.pgcnt * appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX;
			//photoForm.offset = photoForm.pgcnt * 2;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			photoForm.pgcnt = 1;
			photoForm.offset = 0;
		}
	}

	//フォトアルバム閲覧改ページ処理
	private void setPhotoViewPage(int num){
		try {
			// ページ遷移用の計算
			photoForm.viewPgcnt = photoForm.viewPgcnt + num;
			photoForm.viewOffset = photoForm.viewPgcnt * appDefDto.FP_MY_PHOTO_PGMAX;
			//photoForm.viewOffset = photoForm.viewPgcnt * 5;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			photoForm.viewPgcnt = 1;
			photoForm.viewOffset = 0;
		}
	}

	//フォトアルバム一覧・閲覧初期化処理
	private void initList(){
		photoForm.offset = 0;
		photoForm.pgcnt = 0;
		photoForm.viewOffset = 0;
		photoForm.viewPgcnt = 0;


	}

	/**
	 * 写真詳細 初期表示
	 * @return
	 */
	@Execute(validator=false, urlPattern="viewphoto/{pmid}/{pano}/{strFno}")
	public String viewphoto(){
		try {initPhoto();} catch (Exception e) {e.printStackTrace();}
		commonProcessViewphoto();
		return "/pc/photo/viewphoto.jsp";
	}

	/**
	 * 写真詳細 共通処理
	 */
	private void commonProcessViewphoto(){
		setViewphoto();
		isAuthViewphoto(viewphotoResults);
		isPrevViewphoto();
		isNextViewphoto();
	}

	/**
	 * 写真詳細 写真詳細取得
	 */
	private void setViewphoto(){
		viewphotoResults = photoService.selPhotoViewphoto(photoForm);
	}

	/**
	 * 共通 閲覧権限セット
	 * @param resultsList
	 */
	private void isAuthViewphoto(List<BeanMap> resultsList){
		isAuth = vUser
					|| resultsList != null
					&& (appDefDto.FP_CMN_DIARY_AUTH1[0].equals(resultsList.get(0).get("publevel"))
							|| (vFriend
									&& appDefDto.FP_CMN_DIARY_AUTH3[0].equals(resultsList.get(0).get("publevel"))));
		setUnauthMsg(resultsList);
	}

	/**
	 * 共通 権限エラーメッセージセット
	 * @param resultsList
	 */
	private void setUnauthMsg(List<BeanMap> resultsList){
		if(resultsList != null && !isAuth){
			if(appDefDto.FP_CMN_DIARY_AUTH4[0].equals(resultsList.get(0).get("publevel"))){
				unauthMsg = appDefDto.FP_CMN_DIARY_AUTH4[1]+"のため閲覧することが出来ません。";
			}else if(appDefDto.FP_CMN_DIARY_AUTH3[0].equals(resultsList.get(0).get("publevel"))){
				unauthMsg = appDefDto.FP_CMN_DIARY_AUTH3[1]+"のため閲覧することが出来ません。";
			}
		}
	}

	/**
	 * 写真詳細 prev 表示判定
	 */
	private void isPrevViewphoto(){
		isPrevViewphoto = viewphotoResults != null
							&& (Integer)viewphotoResults.get(0).get("prevfno") > 0;
	}

	/**
	 * 写真詳細 next 表示判定
	 */
	private void isNextViewphoto(){
		isNextViewphoto = viewphotoResults != null
							&& (Integer)viewphotoResults.get(0).get("nextfno") > 0;
	}
}