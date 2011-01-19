package frontier.action.pc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.h2.util.StringUtils;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.FileDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.FileForm;
import frontier.service.FileService;

public class FileAction {
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public FileDto fileDto;
	@ActionForm
	@Resource
	protected FileForm fileForm;
	@Resource
	protected FileService fileService;
	
	//　変数
	public List<BeanMap> results;							//	検索結果
	public Integer resultsCnt;								//	検索結果件数
	public List<BeanMap> fileSideBoxCategoryList;			//	SideBox
	public Map<String,Object> classMap;						//	class
	public String categoryName;								//	検索結果見出し
	public boolean isPager;									//	ページャ表示制御用
	public boolean isPrev;									//	前の**件表示制御用
	public boolean isNext;									//	次の**件表示制御用
	public ActionMessages errors = new ActionMessages();	//	エラーメッセージ
	
	/**
	 * ファイル一覧 初期表示
	 * @return
	 */
	@Execute(validator=false)
	public String index(){
		initFileList();
		initPager();
		commonProcessList();
		return "list.jsp";
	}
	
	/**
	 * ファイル詳細 初期表示
	 * @return
	 */
	@Execute(validator=false, urlPattern="view/{fileid}")
	public String view(){
		initFileView();
		commonProcessView();
		return "view.jsp";
	}
	
	/**
	 * ファイル一覧 自画面遷移
	 * @return
	 */
	@Execute(validator=false)
	public String list(){
		commonProcessList();
		return "list.jsp";
	}
	
	/**
	 * ファイル一覧 カテゴリ選択
	 * @return
	 */
	@Execute(validator=false, urlPattern="list/{condition}")
	public String selCategory(){
		initPager();
		return "list?redirect=true";
	}
	
	/**
	 * ファイル一覧 前の**件
	 * @return
	 */
	@Execute(validator=false, urlPattern="prev")
	public String prev(){
		setFilePage(-1);
		return "list?redirect=true";
	}
	
	/**
	 * ファイル一覧 次の**件
	 * @return
	 */
	@Execute(validator=false, urlPattern="next")
	public String next(){
		setFilePage(1);
		return "list?redirect=true";
	}
	
	/**
	 * ファイル一覧 ファイルダウンロード
	 * @return
	 */
	@Execute(validate="checkFile", input="errorList", urlPattern="download/{fileid}")
	public String downloadList(){
		fileService.updateFileDownload(fileForm);
		return null;
	}
	
	/**
	 * ファイル詳細 ファイルダウンロード
	 * @return
	 */
	@Execute(validate="checkFile", input="errorView", urlPattern="download/{fileid}/{strHistoryno}")
	public String downloadView(){
		fileService.updateFileDownload(fileForm);
		return null;
	}
	
	/**
	 * ファイル一覧 ファイル一覧取得
	 */
	private void setFileList(){
		results    = fileService.selFileList(fileForm);
		resultsCnt = fileService.selCntFileList(fileForm);
		fileService.setFileDecoration(results, false);
	}
	
	/**
	 * ファイル詳細 ファイル詳細取得
	 */
	private void setFileView(){
		results    = fileService.selFileView(fileForm);
		resultsCnt = results.size();
		fileService.setFileDecoration(results, true);
	}
	
	/**
	 * ファイル一覧 初期化
	 */
	public void initFileList(){
		fileService.initUserInfoDto();
		fileForm.condition    = null;
		fileForm.sortname     = "0";
		// 並び順セット (更新日降順)
		fileForm.sortcd       = 7;
		fileForm.strHistoryno = "1";
	}
	
	/**
	 * ファイル詳細 初期化
	 */
	public void initFileView(){
		fileService.initUserInfoDto();
	}
	
	/**
	 * ファイル一覧 pager 初期化
	 */
	private void initPager(){
		fileForm.pgcnt  = 0;
		fileForm.offset = 0;
	}
	
	/**
	 * ファイル一覧 共通処理
	 * <ul>
	 * <li>カテゴリセット</li>
	 * <li>ファイル一覧取得</li>
	 * <li>カテゴリ取得 (検索結果見出し)</li>
	 * <li>pager 表示判定</li>
	 * <li>クラスセット</li>
	 * </ul>
	 */
	private void commonProcessList(){
		setCategory();
		setFileList();
		setFinding();
		isPager();
		setClass();
	}
	
	/**
	 * ファイル詳細 共通処理
	 * <ul>
	 * <li>カテゴリセット</li>
	 * <li>ファイル詳細取得</li>
	 * </ul>
	 */
	private void commonProcessView(){
		setCategory();
		setFileView();
	}
	
	/**
	 * ファイル一覧 カテゴリ取得 (検索結果見出し)
	 */
	private void setFinding(){
		categoryName = fileService.selCategoryName(fileForm, fileSideBoxCategoryList);
	}
	
	/**
	 * ファイル一覧 クラスセット
	 */
	private void setClass(){
		classMap = fileService.setClass(fileForm);
	}
	
	/**
	 * ファイル一覧 改ページ処理
	 * @param num
	 */
	private void setFilePage(int num){
		try {
			// ページ遷移用の計算
			fileForm.pgcnt  = fileForm.pgcnt + num;
			fileForm.offset = fileForm.pgcnt * appDefDto.FP_MY_FILELIST_PGMAX;
		} catch (Exception e) {
			// 計算できない場合は初期値セット
			fileForm.pgcnt  = 0;
			fileForm.offset = 0;
		}		
	}
	
	/**
	 * ファイル一覧 pager 表示判定
	 */
	private void isPager(){
		isPager = resultsCnt > appDefDto.FP_MY_FILELIST_PGMAX;
		isPrev();
		isNext();
	}
	
	/**
	 * ファイル一覧 pager prev 表示判定
	 */
	private void isPrev(){
		isPrev = fileForm.offset > 0;
	}
	
	/**
	 * ファイル一覧 pager next 表示判定
	 */
	private void isNext(){
		isNext = resultsCnt > (fileForm.offset + results.size());
	}
	
	/**
	 * 共通 カテゴリセット<br>
	 * ※検索条件再セット処理があるので、一覧取得処理の前に実行する
	 * <ul>
	 * <li>カテゴリ取得 (SideBox)</li>
	 * <li>FileDtoセット</li>
	 * </ul>
	 */
	private void setCategory(){
		fileSideBoxCategoryList = fileService.selFileSideBoxCategoryList();
		// FileDtoセット
		fileDto.condition       = fileForm.condition;
		fileDto.isCategory      = fileService.setSelected(fileDto, fileSideBoxCategoryList);
		if(!fileDto.isCategory){
			fileForm.condition = null;
			fileDto.condition  = null;
		}
	}
	
	/**
	 * ファイル一覧 エラー
	 * @return
	 */
	@Execute(validator=false)
	public String errorList(){
		fileForm.strHistoryno = "1";
		commonProcessList();
		return "list.jsp";
	}
	
	/**
	 * ファイル詳細 エラー
	 * @return
	 */
	@Execute(validator=false)
	public String errorView(){
		commonProcessView();
		return "view.jsp";
	}
	
	/**
	 * 共通 エラーチェック
	 * @return errors
	 */
	public ActionMessages checkFile(){
		// 履歴NO取得
		fileForm.historyno = fileService.convInt(fileForm.strHistoryno);
		// 履歴NOが0の場合はエラー
		if(fileForm.historyno == 0){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.file.download"));
		}else{
			// ファイル名取得
			String filename = fileService.selFileName(fileForm);
			// ファイル名が取得できない場合はエラー
			if(StringUtils.isNullOrEmpty(filename)){
				//errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.file.download.auth"));
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.file.download"));
			}else{
				try {
					// ファイルダウンロード実行
					boolean isSuccess = fileService.download(fileForm, filename);
					// ファイルダウンロードに失敗した場合はエラー
					if(!isSuccess){
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.file.download"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return errors;
	}
}