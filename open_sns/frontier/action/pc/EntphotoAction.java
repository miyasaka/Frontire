package frontier.action.pc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.h2.util.StringUtils;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.EntphotoForm;
import frontier.service.EntphotoService;
import frontier.service.PhotoService;

public class EntphotoAction {
    Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected EntphotoService entphotoService;
	@Resource
	protected PhotoService photoService;
	@Resource
	public AppDefDto appDefDto;

	@ActionForm
	@Resource
	private EntphotoForm entphotoForm;
	
	//画面表示用変数
	//表紙変更画面
	public List<BeanMap> photoResults;
    public long photoResultscnt;
    public List<BeanMap> coverResults;
	public Map<String,Object> results;				//	検索結果
	public String entphotoFinding;					//	Finding
	public String entphotoSubmitName;				//	SubmitName
	public String entphotoSubmitValue;				//	SubmitValue
	public boolean isEdit;							//	
	public boolean isPhotoExists = true;			//	ファイル存在有無
	public ActionMessages errors = new ActionMessages();	//	エラーメッセージ
	public Map<String,Object> editphotoResults;		//	検索結果
	public boolean isEditPhotoExists = true;		//	ファイル存在有無
	public Map<String,Object> delphotoResults;		//	検索結果
	public boolean isDelPhotoExists = true;			//	ファイル存在有無
    
	//インスタンス変数
	private final Integer[] dirs = {640,480,240,180,120,76,60,42};
	
	//フォトアルバム作成画面初期表示
	@Execute(validator=false)
	public String index(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;

		entphotoForm.regist = appDefDto.FP_CMN_FILE_REGIST_NEW;
		init();
		commonProcess();
		return "/pc/entphoto/index.jsp";
	}
	
	/**
	 * フォトアルバム編集 初期表示
	 * @return
	 */
	@Execute(validator=false, urlPattern="edit/{strAno}")
	public String edit(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		entphotoForm.regist = appDefDto.FP_CMN_FILE_REGIST_EDIT;
		entphotoForm.ano = entphotoService.convInt(entphotoForm.strAno);
		initEditEntphoto();
		commonProcess();
		return "/pc/entphoto/index.jsp";
	}
	
	/**
	 * フォトアルバム作成　初期化
	 */
	private void init(){
		entphotoForm.albumTitle = null;
		entphotoForm.albumBody  = null;
		entphotoForm.level      = appDefDto.FP_CMN_DIARY_AUTH1[0];
	}
	
	/**
	 * 共通 共通処理
	 */
	private void commonProcess(){
		setTitle();
	}
	
	/**
	 * 共通 文言等セット
	 */
	private void setTitle(){
		if (!StringUtils.isNullOrEmpty(entphotoForm.regist)){
			// 0: 新規登録
			if (appDefDto.FP_CMN_FILE_REGIST_NEW.equals(entphotoForm.regist)){
				entphotoFinding		= "フォトアルバムを作成する";
				entphotoSubmitName	= "insert";
				entphotoSubmitValue	= "　作　　　成　";
			// 1: 編集
			}else if(appDefDto.FP_CMN_FILE_REGIST_EDIT.equals(entphotoForm.regist)){
				entphotoFinding		= "フォトアルバムを編集する";
				entphotoSubmitName	= "update";
				entphotoSubmitValue	= "　編　　　集　";
			}
			isEdit = appDefDto.FP_CMN_FILE_REGIST_EDIT.equals(entphotoForm.regist);
		}
	}
	
	/**
	 * フォトアルバム編集　フォトアルバム情報取得
	 */
	private void initEditEntphoto(){
		results = entphotoService.selEntphotoIndex(entphotoForm);
		if(results != null){
			// 各パラメタにセット
			entphotoForm.albumTitle = results.get("title").toString();
			entphotoForm.albumBody  = results.get("detail").toString();
			entphotoForm.level      = results.get("publevel").toString();
		}else{
			init();
			isPhotoExists = false;
		}
	}
	
	/**
	 * フォトアルバム編集 フォトアルバム編集
	 * @return
	 */
	@Execute(validate="checkPhoto", input="errIndex")
	public String update(){
		entphotoService.updatePhotoAlbum(entphotoForm);
		return "/pc/photo/view/"+userInfoDto.memberId+"/"+entphotoForm.ano+"?redirect=true";
	}
	
	/**
	 * フォトアルバム作成 フォトアルバム作成
	 * @return
	 */
	@Execute(validate="checkPhoto", input="errIndex")
	public String insert(){
		
		//DB検索結果を取得
		List<BeanMap> cntResults = entphotoService.selMaxAno(userInfoDto.memberId);
		//アルバムNOの最大値取得
		Integer ano = (Integer)cntResults.get(0).get("ano");
		
		//フォトアルバム登録
		entphotoService.insPhotoAlbum(userInfoDto.memberId, ano, entphotoForm);
		
		return "/pc/entphoto/add/"+ano+"?redirect=true";
	}
	/**
	 * フォトアルバム編集 フォトアルバム削除確認
	 * @return
	 */
	@Execute(validator=false)
	public String delete(){
		return "/pc/entphoto/delete.jsp";
	}
	
	/**
	 * フォトアルバム編集 フォトアルバム削除確認
	 * @return
	 */
	@Execute(validator=false)
	public String deletePhotoAlbum(){
		return "/pc/entphoto/delete?redirect=true";
	}
	
	/**
	 * フォトアルバム編集 フォトアルバム削除実行
	 * @return
	 */
	@Execute(validator=false)
	public String execute(){
		entphotoService.deleteAll(entphotoForm);
		return "/pc/photo/list/"+userInfoDto.memberId;
	}
	
	/**
	 * フォトアルバム編集 フォトアルバム削除キャンセル
	 * @return
	 */
	@Execute(validator=false)
	public String stop(){
		return "/pc/entphoto/edit/"+entphotoForm.ano+"?redirect=true";
	}
	
	//フォトアルバム作成画面エラー表示
	@Execute(validator=false)
	public String errIndex(){
		commonProcess();
		return "/pc/entphoto/index.jsp";
	}
	
	//写真の追加画面初期表示
	@Execute(validator=false,urlPattern="add/{ano}")
	public String add(){
		//写真１～５初期化
		clnPhoto();
		
		return "/pc/entphoto/add.jsp";
	}
	
	//写真１～５初期化
	private void clnPhoto(){
		entphotoForm.photo1 = null;
		entphotoForm.photo2 = null;
		entphotoForm.photo3 = null;
		entphotoForm.photo4 = null;
		entphotoForm.photo5 = null;
	}
	
	//写真の追加画面エラー表示
	@Execute(validator=false)
	public String errAdd(){
		return "/pc/entphoto/add.jsp";
	}	
	
	//写真アップロード
	@Execute(validate="checkAdd",input="errAdd")
	public String addition(){
		//変数定義
		String mid = userInfoDto.memberId;
		Integer ano = entphotoForm.ano;
		
		//DB検索結果を取得
		List<BeanMap> lbm = entphotoService.selMaxFno(mid, ano);
		//フォトNoの最大値を取得
		Integer fno = (Integer)lbm.get(0).get("fno");
		//写真１の登録処理
		//null対策
		if(entphotoForm.photo1!=null){
			if (entphotoForm.photo1.getFileSize()!=0){
				fno = sumFno(fno);
				execPhotoUp(mid, ano, fno, entphotoForm.photo1);
			}
		}
		//写真２の登録処理
		//null対策
		if(entphotoForm.photo2!=null){
			if (entphotoForm.photo2.getFileSize()!=0){
				fno = sumFno(fno);
				execPhotoUp(mid, ano, fno, entphotoForm.photo2);
			}
		}
		
		//写真３の登録処理
		//null対策
		if(entphotoForm.photo3!=null){
			if (entphotoForm.photo3.getFileSize()!=0){
				fno = sumFno(fno);
				execPhotoUp(mid, ano, fno, entphotoForm.photo3);
			}
		}
		
		//写真４の登録処理
		//null対策
		if(entphotoForm.photo4!=null){
			if (entphotoForm.photo4.getFileSize()!=0){
				fno = sumFno(fno);
				execPhotoUp(mid, ano, fno, entphotoForm.photo4);
			}
		}
		
		//写真５の登録処理
		//null対策
		if(entphotoForm.photo5!=null){
			if (entphotoForm.photo5.getFileSize()!=0){
				fno = sumFno(fno);
				execPhotoUp(mid, ano, fno, entphotoForm.photo5);
			}
		}
		return "/pc/photo/view/"+userInfoDto.memberId+"/"+entphotoForm.ano+"?redirect=true";
	}
	
	//キャンセルボタン押下（写真の追加画面）
	@Execute(validator=false)
	public String cancel(){
		return "/pc/photo/view/"+userInfoDto.memberId+"/"+entphotoForm.ano+"?redirect=true";
	}
	
	//表紙変更画面初期表示
	@Execute(validator=false,urlPattern="change/{cano}")
	public String change(){
		
		//初期化処理
		initCover();
		
		//DB検索処理
		initNeedCover();
		
		return "/pc/entphoto/cover.jsp";
	}
	
	//表紙変更リンクを押下した場合
	@Execute(validator=false,urlPattern="updCover/{cfno}")
	public String updCover(){

		//既存の表紙を通常に変更する
		entphotoService.updPhotoCoverNormal(userInfoDto.memberId, entphotoForm.cano, entphotoForm.cfno);

		//表紙を変更する
		entphotoService.updPhotoCover(userInfoDto.memberId, entphotoForm.cano, entphotoForm.cfno);

		return "/pc/entphoto/reChange?redirect=true";
	}
	
	//表紙変更画面再表示
	@Execute(validator=false)
	public String reChange(){
		//DB検索処理
		initNeedCover();
		
		return "/pc/entphoto/cover.jsp";
	}	
	
	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setCoverPage(1);
		
		//F5対策
		return "/pc/entphoto/reChange?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setCoverPage(-1);		
		
		//F5対策
		return "/pc/entphoto/reChange?redirect=true";
	}
	
	//フォトアルバムのDB登録と画像アップロード実行メソッド
	private void execPhotoUp(String mid,Integer ano,Integer fno,FormFile photo){
		//ファイルパス生成
		String photoName = getFileName(ano,fno,photo);
		//画像アップロード
		upload(mid,photo,photoName.split("/")[6]);
		//DB登録
		entphotoService.insPhoto(mid, ano, fno, photoName);	
	}
	
	//アルバムNoを最大値+1する
	private Integer sumFno(Integer fno){
		return ++fno;
	}
	
	//画像ファイル名の作成
	private String getFileName(Integer ano,Integer fno,FormFile photo){
		String picName = null;
	    String today = CmnUtility.getToday("yyyyMMddHHmmss");
	    String path = appDefDto.FP_CMN_DIARY_BEFORE_PATH + userInfoDto.memberId + appDefDto.FP_CMN_PHOTO_AFTER_PATH;
	    
	    //画像ファイル名の作成
	    picName = path + CmnUtility.stringFormat("0000", ano) +"_"+ CmnUtility.stringFormat("000", fno) +"_"+today+".jpg";

		return picName;
	}
	
	//画像アップロード
	private void upload(String mid,FormFile photo,String photoName){
		//ディレクトリ
		String pDir = "photo";
		
		//出力先ディレクトリ
		String dir = appDefDto.FP_CMN_CONTENTS_DIR +"img/mem/"+mid;
		//ディレクトリ作成
		CmnUtility.makeDir(dir,"photo");
		
		//画像パス生成
		String photoPath640 = dir + "/" + pDir + "/pic640/" + photoName;
		
		// 画像ファイルのアップロード(ベースはpic640フォルダ)
		CmnUtility.uploadFile(photoPath640, photo);
		
		// ファイルのコピー&リサイズ(ディレクトリ数分ループ)
		for (Integer i : dirs) {
			try {
				CmnUtility.resize(dir + "/" + pDir + "/" + "pic" + i.toString() + "/" + photoName,i,photoPath640,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//フォトアルバム写真アップロード入力チェック
	public ActionMessages checkAdd(){
		ActionMessages errors = new ActionMessages();
    	FormFile photo1 = entphotoForm.photo1;
    	FormFile photo2 = entphotoForm.photo2;
    	FormFile photo3 = entphotoForm.photo3;
       	FormFile photo4 = entphotoForm.photo4;
       	FormFile photo5 = entphotoForm.photo5;
       	//ファイル必須チェック（関連）
       	if(photo1!=null && photo2!=null && photo3!=null && photo4!=null && photo5!=null)
       	if(photo1.getFileName().length()==0 && photo2.getFileName().length()==0 && photo3.getFileName().length()==0 && photo4.getFileName().length()==0 && photo5.getFileName().length()==0){
       		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.appoint","画像"));
       	} else {
       		//ファイルサイズとファイル形式チェック
       		//null対策
       		if(photo1!=null){
	       		if (photo1.getFileName().length()>0){
	       			CmnUtility.checkPhotoFile(errors, photo1, "1");
	       		}
       		}
       		//null対策
       		if(photo2!=null){
	       		if (photo2.getFileName().length()>0){
	       			CmnUtility.checkPhotoFile(errors, photo2, "2");
	       		}
       		}
       		//null対策
       		if(photo3!=null){
	       		if (photo3.getFileName().length()>0){
	       			CmnUtility.checkPhotoFile(errors, photo3, "3");
	       		}
       		}
       		//null対策
       		if(photo4!=null){
	       		if (photo4.getFileName().length()>0){
	       			CmnUtility.checkPhotoFile(errors, photo4, "4");
	       		}
       		}
       		//null対策
       		if(photo5!=null){
	       		if (photo5.getFileName().length()>0){
	       			CmnUtility.checkPhotoFile(errors, photo5, "5");
	       		}
       		}
       	}
		return errors;
	}
	
	//表紙変更画面必須処理
	private void initNeedCover(){
		coverResults = photoService.selPhotoCover(userInfoDto.memberId, entphotoForm.cano);
		photoResults = photoService.selPhoto(userInfoDto.memberId, entphotoForm.cano, entphotoForm.offset);
		photoResultscnt = photoService.cntPhoto(userInfoDto.memberId, entphotoForm.cano);
		
	}
	
	//表紙変更画面初期化処理
	private void initCover(){
		entphotoForm.offset = 0;
		entphotoForm.pgcnt = 0;			
	}
	
	//表紙変更画面改ページ処理
	private void setCoverPage(int num){
		try {
			// ページ遷移用の計算
			entphotoForm.pgcnt = entphotoForm.pgcnt + num;
			entphotoForm.offset = entphotoForm.pgcnt * appDefDto.FP_MY_PHOTO_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			entphotoForm.pgcnt = 1;
			entphotoForm.offset = 0;
		}		
	}
	
	/**
	 * 共通 エラーチェック
	 * @return errors
	 */
	public ActionMessages checkPhoto(){
		String title = entphotoService.setZenToHan(entphotoForm.albumTitle);
		String detail = entphotoService.setZenToHan(entphotoForm.albumBody);
		String publevel = entphotoForm.level;
		// タイトルチェック
		// 必須チェック(前後空白行除去)
		if(StringUtils.isNullOrEmpty(title)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required" ,"タイトル"));
		}
		// 説明チェック
		// 必須チェック(前後空白行除去)
		if(StringUtils.isNullOrEmpty(detail)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required" ,"説明"));
		}
		// 公開レベルチェック
		if(!StringUtils.isNullOrEmpty(publevel)){
			// 整合性チェック
			if(!appDefDto.FP_CMN_DIARY_AUTH1[0].equals(publevel)
			&& !appDefDto.FP_CMN_DIARY_AUTH3[0].equals(publevel)
			&& !appDefDto.FP_CMN_DIARY_AUTH4[0].equals(publevel)){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.invalid" ,"公開レベル"));
				entphotoForm.level = appDefDto.FP_CMN_DIARY_AUTH1[0];
			}
		}
		return errors;
	}
	
	/**
	 * 写真編集 初期表示
	 * @return
	 */
	@Execute(validator=false, urlPattern="editphoto/{strAno}/{strFno}")
	public String editphoto(){
		entphotoForm.ano = entphotoService.convInt(entphotoForm.strAno);
		entphotoForm.fno = entphotoService.convInt(entphotoForm.strFno);
		initEntphotoEditphoto();
		return "/pc/entphoto/editphoto.jsp";
	}
	
	/**
	 * 写真編集　写真情報取得
	 */
	private void initEntphotoEditphoto(){
		editphotoResults = entphotoService.selEntphotoEditphoto(entphotoForm);
		if(editphotoResults != null){
			// 各パラメタにセット
			entphotoForm.picPath = editphotoResults.get("pic").toString();
			entphotoForm.picname = editphotoResults.get("picname").toString();
		}else{
			isEditPhotoExists = false;
		}
	}
	
	/**
	 * 写真編集 写真編集
	 * @return
	 */
	@Execute(validate="checkEditphoto", input="errEditphoto")
	public String updatePhoto(){
		String pic = null;
		//null対策
		if(entphotoForm.pic!=null){
			if(!StringUtils.isNullOrEmpty(entphotoForm.pic.getFileName())){
				pic = entphotoService.getPhotoname(entphotoForm, false);
				entphotoService.removePhoto(entphotoForm, true);
				upload(userInfoDto.memberId, entphotoForm.pic, pic.split("/")[6]);
			}
		}
		entphotoService.updatePhoto(entphotoForm, pic);
		return "/pc/photo/viewphoto/"+userInfoDto.memberId+"/"+entphotoForm.ano+"/"+entphotoForm.fno+"?redirect=true";
	}
	
	/**
	 * 写真編集 エラー
	 * @return
	 */
	@Execute(validator=false)
	public String errEditphoto(){
		return "/pc/entphoto/editphoto.jsp";
	}
	
	/**
	 * 写真編集 エラーチェック
	 * @return errors
	 */
	public ActionMessages checkEditphoto(){
		String strPicname = entphotoService.setZenToHan(entphotoForm.picname);
		// タイトルチェック
		// 必須チェック(前後空白行除去)
		if(StringUtils.isNullOrEmpty(strPicname)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required" ,"説明"));
		}
		return errors;
	}
	
	/**
	 * 写真削除確認 初期表示
	 * @return
	 */
	@Execute(validator=false)
	public String delphoto(){
		initEntphotoDelphoto();
		return "/pc/entphoto/delphoto.jsp";
	}
	
	/**
	 * 写真削除 写真削除確認
	 * @return
	 */
	@Execute(validator=false)
	public String deletePhoto(){
		return "/pc/entphoto/delphoto?redirect=true";
	}
	
	/**
	 * 写真削除　写真情報取得
	 */
	private void initEntphotoDelphoto(){
		delphotoResults = entphotoService.selEntphotoEditphoto(entphotoForm);
		if(delphotoResults != null){
			// 各パラメタにセット
			entphotoForm.picPath = delphotoResults.get("pic").toString();
			entphotoForm.picname = delphotoResults.get("picname").toString();
		}else{
			isDelPhotoExists = false;
		}
	}
	
	/**
	 * 写真削除確認 写真削除実行
	 * @return
	 */
	@Execute(validator=false)
	public String deletePhotoExecute(){
		entphotoService.removePhoto(entphotoForm, true);
		upload(userInfoDto.memberId, entphotoForm.pic, entphotoService.getPhotoname(entphotoForm, true));
		entphotoService.updatePhotoDelflg(entphotoForm);
		return "/pc/photo/view/"+userInfoDto.memberId+"/"+entphotoForm.ano+"?redirect=true";
	}
	
	/**
	 * 写真削除確認 キャンセル
	 * @return
	 */
	@Execute(validator=false)
	public String stopPhoto(){
		return "/pc/entphoto/editphoto/"+entphotoForm.ano+"/"+entphotoForm.fno+"?redirect=true";
	}
}