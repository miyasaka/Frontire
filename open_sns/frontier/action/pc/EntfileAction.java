package frontier.action.pc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.h2.util.StringUtils;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.FileDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.EntfileForm;
import frontier.service.EntfileService;
import frontier.service.FileService;

public class EntfileAction {
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public FileDto fileDto;
	@ActionForm
	@Resource
	protected EntfileForm entfileForm;
	@Resource
	protected EntfileService entfileService;
	@Resource
	protected FileService fileService;
	
	//　変数
	public Map<String,Object> results;						//	検索結果
	public List<BeanMap> fileSideBoxCategoryList;			//	SideBox
	public List<BeanMap> categoryList;						//	PullDown
	public String entfileFinding;							//	Finding
	public String entfileSubmitName;						//	SubmitName
	public String entfileSubmitValue;						//	SubmitValue
	public boolean isFileEdit;								//	公開範囲の編集可否
	public boolean isFileExists = true;						//	ファイル存在有無
	public String categoryidStyle;							//	カテゴリスタイル (プルダウン)
	public String inputcategoryStyle;						//	カテゴリスタイル (テキスト)
	public final String displayNone = "display: none;";		//	display: none;
	public ActionMessages errors = new ActionMessages();	//	エラーメッセージ
	
	/**
	 * ファイル登録　初期表示
	 * @return
	 */
	@Execute(validator=false)
	public String index(){
		initEntfile();
		initEntfileForm();
		commonProcess();
		return "index.jsp";
	}
	
	/**
	 * ファイル更新　初期表示
	 * @return
	 */
	@Execute(validator=false, urlPattern="edit/{fileid}")
	public String edit(){
		initEditEntfile();
		setEditEntfile();
		commonProcess();
		return "index.jsp";
	}
	
	/**
	 * ファイル登録　ファイル登録
	 * @return
	 */
	@Execute(validate="checkFile", input="error")
	public String insert(){
		entfileService.insertFile(entfileForm);
		return "/pc/file/view/" + entfileForm.fileid + "?redirect=true";
	}
	
	/**
	 * ファイル更新　ファイル更新
	 * @return
	 */
	@Execute(validate="checkFile", input="error")
	public String update(){
		entfileService.updateFile(entfileForm);
		return "/pc/file/view/" + entfileForm.fileid + "?redirect=true";
	}
	
	/**
	 * ファイル登録 初期化
	 */
	public void initEntfile(){
		fileService.initUserInfoDto();
		// 登録種別に登録をセット
		entfileForm.regist = appDefDto.FP_CMN_FILE_REGIST_NEW;
		// 公開範囲を編集可とする
		entfileForm.oyaflg = true;
	}
	
	/**
	 * ファイル更新 初期化
	 */
	public void initEditEntfile(){
		fileService.initUserInfoDto();
		// 登録種別に更新をセット
		entfileForm.regist = appDefDto.FP_CMN_FILE_REGIST_EDIT;
	}
	
	/**
	 * 共通　フォーム初期化
	 */
	private void initEntfileForm(){
		entfileForm.categoryid    = "";
		entfileForm.inputcategory = "";
		entfileForm.filetitle     = "";
		entfileForm.explanation   = "";
		entfileForm.filename      = null;
		entfileForm.version       = "";
		// 公開範囲の初期値を全体に公開とする
		entfileForm.pubfile       = appDefDto.FP_CMN_DIARY_AUTH1[0];
	}
	
	/**
	 * ファイル更新　ファイル情報取得
	 */
	private void setEditEntfile(){
		results = entfileService.selEntfileIndex(entfileForm);
		if(results != null){
			entfileForm.categoryid    = results.get("categoryid").toString();
			entfileForm.inputcategory = null;
			entfileForm.filetitle     = results.get("title").toString();
			entfileForm.explanation   = results.get("explanation").toString();
			entfileForm.filename      = null;
			entfileForm.version       = results.get("version").toString();
			entfileForm.pubfile       = results.get("pubfile").toString();
			// 公開範囲の編集可否をセット
			entfileForm.oyaflg        = entfileService.isEdit(results);
		}else{
			initEntfileForm();
			isFileExists = false;
		}
	}
	
	/**
	 * 共通 共通処理
	 * <ul>
	 * <li>文言セット</li>
	 * <li>カテゴリスタイルセット</li>
	 * <li>カテゴリ取得 (SideBox)</li>
	 * <li>FileDtoセット</li>
	 * </ul>
	 */
	private void commonProcess(){
		setTitle();
//		setCategoryList();
		setCategoryInputType();
		fileSideBoxCategoryList = fileService.selFileSideBoxCategoryList();
		fileDto.isCategory      = fileService.setSelected(fileDto, fileSideBoxCategoryList);
	}
	
	/**
	 * 共通 文言セット
	 */
	private void setTitle(){
		if (!StringUtils.isNullOrEmpty(entfileForm.regist)){
			// 登録の場合
			if (appDefDto.FP_CMN_FILE_REGIST_NEW.equals(entfileForm.regist)){
				entfileFinding     = "ファイル登録";
				entfileSubmitName  = "insert";
				entfileSubmitValue = "　登　　　録　";
			// 更新の場合
			}else if(appDefDto.FP_CMN_FILE_REGIST_EDIT.equals(entfileForm.regist)){
				entfileFinding     = "ファイル更新";
				entfileSubmitName  = "update";
				entfileSubmitValue = "　更　　　新　";
			}
			// 公開範囲の編集可否をセット
			isFileEdit = entfileForm.oyaflg;
		}
	}
	
	/**
	 * 共通 カテゴリ取得(PullDown)
	 */
	private void setCategoryList(){
		categoryList = entfileService.selMstCategoryList();
	}
	
	/**
	 * 共通 カテゴリスタイルセット
	 */
	private void setCategoryInputType(){
		if(StringUtils.isNullOrEmpty(entfileForm.categoryid)
				&& !StringUtils.isNullOrEmpty(entfileForm.inputcategory)){
			categoryidStyle    = displayNone;
			inputcategoryStyle = "";
		}else{
			categoryidStyle    = "";
			inputcategoryStyle = displayNone;
		}
	}
	
	/**
	 * 共通 エラー
	 * @return
	 */
	@Execute(validator=false)
	public String error(){
		commonProcess();
		return "index.jsp";
	}
	
	/**
	 * 共通 エラーチェック
	 * @return errors
	 */
	public ActionMessages checkFile(){
		String categoryid    = entfileForm.categoryid;
		String inputcategory = entfileService.setZenToHan(entfileForm.inputcategory);
		String filetitle     = entfileService.setZenToHan(entfileForm.filetitle);
		String explanation   = entfileService.setZenToHan(entfileForm.explanation);
		FormFile formFile    = entfileForm.filename;
		String pubfile       = entfileForm.pubfile;
		// カテゴリチェック
		// 必須チェック (プルダウン&入力エリア)
		if(StringUtils.isNullOrEmpty(categoryid) && StringUtils.isNullOrEmpty(inputcategory)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required" ,"カテゴリ"));
		}else if(!StringUtils.isNullOrEmpty(categoryid)){
			setCategoryList();
			boolean isContains = false;
			// 整合性チェック
			for (BeanMap beanMap : categoryList){
				if(beanMap.get("categoryid").equals(categoryid)){
					isContains = true;
					break;
				}
			}
			if(!isContains){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.invalid" ,"カテゴリ"));
			}
		}
		// ファイル名(タイトル)チェック
		// 必須チェック (前後空白行除去)
		if(StringUtils.isNullOrEmpty(filetitle)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required" ,"ファイル名"));
		}
		// 説明チェック
		// 必須チェック (前後空白行除去)
		if(StringUtils.isNullOrEmpty(explanation)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required" ,"説明"));
		}
		// アップロードファイルチェック
		// 必須チェック
		if(formFile.getFileName().length() == 0){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.appoint" ,"ファイル"));
		}else{
			// 下限サイズチェック 
			if(formFile.getFileSize() <= appDefDto.FP_CMN_FILESIZE_MIN){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.file.other"));
			// 上限サイズチェック
			}else if(formFile.getFileSize() > appDefDto.FP_CMN_FILESIZE_MAX){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.file.size.over", appDefDto.FP_CMN_FILESIZE_MAX));
			}
		}
		// 公開範囲チェック
		if(!StringUtils.isNullOrEmpty(pubfile)){
			// 整合性チェック
			if(!appDefDto.FP_CMN_FILE_AUTH_OPEN[0].equals(pubfile)
					&& !appDefDto.FP_CMN_FILE_AUTH_CLOSE[0].equals(pubfile)){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.invalid" ,"公開範囲"));
			}
		}
		return errors;
	}
}