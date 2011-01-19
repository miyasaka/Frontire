package frontier.action.pc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.h2.util.StringUtils;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.RequestUtil;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.DiaryDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Diary;
import frontier.form.pc.DiaryForm;
import frontier.form.pc.EntdiaryForm;
import frontier.service.EntdiaryService;
import frontier.service.PhotoService;

public class EntdiaryAction {
	@Resource
	public AppDefDto appDefDto;
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	public UserInfoDto userInfoDto;
	
	@Resource
	public DiaryDto diaryDto;
	
	@ActionForm
	@Resource
	protected EntdiaryForm entdiaryForm;
	
    @Resource
    protected DiaryForm diaryForm;
	
	@Resource
	protected EntdiaryService entdiaryService;
	
	@Resource
	protected PhotoService photoService;
	
	public ActionMessages errors = new ActionMessages();
	
    public HttpServletRequest request;
    //今日の日付
    public String today = CmnUtility.getToday("yyyyMMdd");
    public List<Map<String,Integer>> cal;
    public Map<String,List<String>> oldCal;
    //日記の存在有無
    public boolean existDiary = true;
    //カレンダーのリンク表示用
    protected List<BeanMap> monthResults;
    //日記削除一覧検索結果
    public List<BeanMap> results;
    //マイプロフィール検索結果
    public List<BeanMap> result;
    //日記遷移元判別文字
    public String _linkFrom;
    //画像説明
    public String oyaPicnote1;
    public String oyaPicnote2;
    public String oyaPicnote3;
    //文言
    public String entDiaryTitle;	//title
    public String entDiaryFinding;	//見出し
    //文言 *サブミットボタン
    public String entDiarySubmitName;	//name
    public String entDiarySubmitValue;	//value
    //変数設定
    public String pubDiaryLevel;
 
    //日記作成
	@Execute(validator=false)
	public String index() throws IOException {
        //リクエストから、SAStrutsが格納した例外を取得します。
        SizeLimitExceededException e = (SizeLimitExceededException) RequestUtil.getRequest()
                        .getAttribute(S2MultipartRequestHandler.SIZE_EXCEPTION_KEY);
        if(e!=null){
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            		"errors.upload.size",new Object[] { e.getActualSize(),e.getPermittedSize()  }));
    		ActionMessagesUtil.addErrors(request, errors);
        }
		//フォーム初期化
		entdiaryForm.title=null;
		entdiaryForm.comment=null;
		entdiaryForm.pubDiary=null;
		entdiaryForm.picnote1=null;
		entdiaryForm.picnote2=null;
		entdiaryForm.picnote3=null;
		entdiaryForm.insPubLevel="9";
		//新規作成・編集判定FLGセット
		entdiaryForm.entDiaryEditType = "0";
		//画像初期化
		entdiaryForm.pic1=null;
		entdiaryForm.pic2=null;
		entdiaryForm.pic3=null;
		entdiaryForm.photo1=null;
		entdiaryForm.photo2=null;
		entdiaryForm.photo3=null;

		
		//日記公開範囲設定
		exePubDiary();
		
		//共通処理
		initListDiary(null,null,0);

		return "index.jsp";
	}
	
	//日記編集
	@Execute(validator=false)
	public String edit(){
		//編集対象日記データ取得
	    Diary entity = new Diary();
		entity = entdiaryService.selDiary(userInfoDto.memberId,entdiaryForm.diaryid);
		//Formにデータをコピー
		if(entity!=null){
			Beans.copy(entity, entdiaryForm)
				.includes("title","comment","pic1","pic2","pic3","entdate","pubDiary","picnote1","picnote2","picnote3","appstatus")
				.sqlDateConverter("yyyy年MM月dd日","entdate")
				.execute();
			
			//変数名をDB名と違う風に設定しているので別で設定
			entdiaryForm.insPubLevel = entity.publevel;
			//新規作成・編集判定FLGセット
			entdiaryForm.entDiaryEditType = "1";
			
			//日記公開範囲設定
			exePubDiary();
			//この画面用に詰めなおし
			entdiaryForm.pubDiary = entity.pubDiary;
			
		}else{
			return "/pc/diary/list/?redirect=true";
		}
		
		//共通処理
		initListDiary(null,null,0);
		//日記削除用変数設定
		List<String> dellist = new ArrayList<String>();
		dellist.add(String.valueOf(entdiaryForm.diaryid));
		entdiaryForm.checkDiaryId=dellist;
		

		
		//return "edit.jsp";
		return "index.jsp";
	}
	
	//フォトアルバム呼び出し
	@Execute(validator=false)
	public String linkPhotoAlbum(){
		return "/pc/photoalbum/list/?redirect=true";
	    
	}
	
	//日記更新処理
	@Execute(validate="checkFile",input="error")
	public String editDiary(){
		//データ更新
		entdiaryService.updateDiary(
				userInfoDto.memberId,
				entdiaryForm.diaryid,
				entdiaryForm.title,
				entdiaryForm.comment,
				entdiaryForm.photo1,
				entdiaryForm.photo2,
				entdiaryForm.photo3,
				entdiaryForm.pubDiary,
				entdiaryForm.picnote1,
				entdiaryForm.picnote2,
				entdiaryForm.picnote3,
				entdiaryForm.insPubLevel
		);
		
		//画像アップロード
		//null対策
		if(entdiaryForm.photo1!=null || entdiaryForm.photo2!=null || entdiaryForm.photo3!=null){
			upload();
		}
		//フォーム初期化
		entdiaryForm.linkFrom="";
		entdiaryForm.checkDiaryId=null;

		//登録した日記の絶対パスを作成
		String diaryUrl = request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/diary/view/" + entdiaryForm.diaryid.toString() + "/" + entdiaryForm.diaryDay + "/" + userInfoDto.memberId;
		
		//bit.lyを使ったURLの短縮
		String shortUrl = CmnUtility.makeBitLy(appDefDto, diaryUrl);
		
		if(shortUrl.equals("NG")){
			return "/pc/common/error.jsp";
		}
		
		//sessionに設定
		diaryDto.fscomment = entdiaryForm.title + " " + shortUrl;
		
		return "/pc/top/index/?redirect=true";		
		
		//return "/pc/diary/list/"+userInfoDto.memberId+"?redirect=true";
	}
	
	//画像削除処理
	@Execute(validator=false)
	public String delImage(){
		entdiaryService.delImage(userInfoDto.memberId,entdiaryForm.diaryid,entdiaryForm.pic1,entdiaryForm.pic2,entdiaryForm.pic3,entdiaryForm.photoNo,entdiaryForm.picnote1,entdiaryForm.picnote2,entdiaryForm.picnote3);
		
		return "edit";
	}
	
	//日記登録処理
	@Execute(validate="checkFile",input="error")
	public String entry(){
		//データ登録
		logger.debug(entdiaryForm.pubDiary);
		
		Integer intDiayrId = entdiaryService.insertDiaryNew(
											userInfoDto.memberId,
											entdiaryForm.title,
											entdiaryForm.comment,
											entdiaryForm.photo1,
											entdiaryForm.photo2,
											entdiaryForm.photo3,
											entdiaryForm.pubDiary,
											entdiaryForm.picnote1,
											entdiaryForm.picnote2,
											entdiaryForm.picnote3,
											entdiaryForm.insPubLevel
											);
		//画像アップロード
		//null対策
		if(entdiaryForm.photo1!=null || entdiaryForm.photo2!=null || entdiaryForm.photo3!=null){
			upload();
		}

		//登録した日記の絶対パスを作成
		String diaryUrl = request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/diary/view/" + intDiayrId.toString() + "/" + entdiaryForm.diaryDay + "/" + userInfoDto.memberId;
		
		//bit.lyを使ったURLの短縮
		String shortUrl = CmnUtility.makeBitLy(appDefDto, diaryUrl);
		
		if(shortUrl.equals("NG")){
			return "/pc/common/error.jsp";
		}
		
		//sessionに設定
		diaryDto.fscomment = entdiaryForm.title + " " + shortUrl;
		
		return "/pc/top/index/?redirect=true";
		//return "/pc/diary/list/"+userInfoDto.memberId+"?redirect=true";
	}
	
	//日記削除確認
	@Execute(validator=false)
	public String delete(){
		if(entdiaryForm.linkFrom!=null){
			if(entdiaryForm.linkFrom=="edit"||entdiaryForm.linkFrom.equals("edit")){
				//日記削除用変数設定
				List<String> dellist = new ArrayList<String>();
				dellist.add(String.valueOf(entdiaryForm.diaryid));
				entdiaryForm.checkDiaryId=dellist;
			}
		}
		results = entdiaryService.selDiaryList(userInfoDto.memberId,entdiaryForm.checkDiaryId);

		if(entdiaryForm.linkFrom!=null){
			if(entdiaryForm.linkFrom=="edit"||entdiaryForm.linkFrom.equals("edit")){
				//画像説明セット
				oyaPicnote1 = results.get(0).get("picnote1").toString();
				oyaPicnote2 = results.get(0).get("picnote2").toString();
				oyaPicnote3 = results.get(0).get("picnote3").toString();
				if(oyaPicnote1.equals(null) || oyaPicnote1.equals("")){
					oyaPicnote1 = "画像１";
				}
				if(oyaPicnote2.equals(null) || oyaPicnote2.equals("")){
					oyaPicnote2 = "画像２";
				}
				if(oyaPicnote3.equals(null) || oyaPicnote3.equals("")){
					oyaPicnote3 = "画像３";
				}
				
				//編集画面からの遷移
				resetResults(results);
				//フォーム初期化
				_linkFrom = entdiaryForm.linkFrom;
				entdiaryForm.linkFrom="";
				return "delete.jsp";
			}
		}
		//編集画面以外からの遷移
		CmnUtility.editcmnt(results, "comment", "pic1", "pic2", "pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_CONTENTS_ROOT);
		return "delList.jsp";
	}
	
	//日記削除処理
	@Execute(validator=false)
	public String delDiary(){
		entdiaryService.deleteDiary(userInfoDto.memberId,entdiaryForm.checkDiaryId);
		//チェックボックス値初期化
		entdiaryForm.checkDiaryId=null;
		return "/pc/diary/list/"+userInfoDto.memberId+"?redirect=true";
	}
	
	//日記削除キャンセル
	@Execute(validator=false)
	public String cansel(){
		logger.debug("link");
		logger.debug(entdiaryForm.linkFrom);
		if(entdiaryForm.linkFrom!=null){
			//編集画面へ戻る
			if(entdiaryForm.linkFrom=="edit"||entdiaryForm.linkFrom.equals("edit")){
				entdiaryForm.linkFrom="";
				return "edit?redirect=true";				
			}
		}
		//一覧へ戻る
		return "/pc/diary/list/"+userInfoDto.memberId+"?redirect=true";
	}
	
	//登録時エラー
	@Execute(validator=false)
	public String error(){
		//共通処理
		initListDiary(null,null,0);
		
		//値保持
		String kpPubDiary;
		kpPubDiary = entdiaryForm.pubDiary;
		//標準の公開設定
		exePubDiary();
		//詰めなおし
		entdiaryForm.pubDiary = kpPubDiary;
		
		return "index.jsp";
	}
	
	//画像チェック->その他チェックすることが増えたので2009/7/3時点ではエラーチェックメソッド
	public ActionMessages checkFile(){
   	
		//公開範囲のチェック
		chkPubLevel();
		//null対策
		if(entdiaryForm.photo1!=null){
			errcheck(entdiaryForm.photo1,"1");
		}
		//null対策
		if(entdiaryForm.photo2!=null){
			errcheck(entdiaryForm.photo2,"2");
		}
		//null対策
		if(entdiaryForm.photo3!=null){
			errcheck(entdiaryForm.photo3,"3");
		}

		return errors;
	}
	
	private void errcheck(FormFile photo,String no){
    	if(photo.getFileName().length()>0){
        	if(photo.getFileSize()==0){
            	//ファイルサイズチェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        				"errors.upload.other",new Object[] {no}));
            }else if(!photo.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo.getContentType().equalsIgnoreCase("image/jpeg")){
            //ファイル形式チェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        		"errors.upload.type",new Object[] {no}));
        	}
    	}
	}
	
	//公開範囲のチェック
	private void chkPubLevel(){
		String cPubLevel = entdiaryForm.insPubLevel;
		String cPubDiary = entdiaryForm.pubDiary;
		
		//ラジオボタンの存在チェック
		if(!cPubLevel.equals("0") && !cPubLevel.equals("1") && !cPubLevel.equals("9")){
       		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.invalid","公開状況"));						
		}
		
		//外部公開は全体に公開のものしかできない
		if(!cPubDiary.equals("1") && cPubLevel.equals("0")){
       		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.publevelrelation","外部"));			
		}else if (!cPubDiary.equals("1") && cPubLevel.equals("1")){
       		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.publevelrelation","Frontier Net"));						
		}
	}
	
	//日記カレンダー設定処理
	private void initListDiary(String month,String day,int offset){
		//menu.jsp切り替え
		userInfoDto.visitMemberId=userInfoDto.memberId;
	    //日記権限
		List<String> pubDiary = new ArrayList<String>();
		//日記公開レベル(ダミーで設定しておく)
		List<String> pubLevelList = new ArrayList<String>();		
		//権限追加
		pubDiary.add("0");
		pubDiary.add("1");
		pubDiary.add("2");
		pubDiary.add("3");
		pubDiary.add("4");
		//日記日付取得
		entdiaryForm.diaryDay = today;
		diaryForm.diaryDay = today;
		//カレンダー日付検索実行
		monthResults = entdiaryService.selDiaryMonthList(userInfoDto.memberId,entdiaryForm.diaryDay.substring(0, 6),pubDiary,userInfoDto.membertype,pubLevelList);	
		//カレンダー生成
		cmnMakeCalendar();
		//文言セット
		setTitle(entdiaryForm.entDiaryEditType);
	}

	
	//共通カレンダー作成
	private void cmnMakeCalendar(){
		//カレンダー生成
		cal = CmnUtility.makeCustomCalendar(entdiaryForm.diaryDay,monthResults);
		//過去カレンダー生成
		//logger.debug("カレンダー："+userInfoDto.entdate);
		//oldCal = CmnUtility.makeOldCalendar(userInfoDto.entdate);	
	}

	//画像アップロード
	private void upload(){
		if(entdiaryForm.photo1.getFileSize()>0||entdiaryForm.photo2.getFileSize()>0||entdiaryForm.photo3.getFileSize()>0){
			entdiaryService.upload(userInfoDto.memberId,entdiaryForm.photo1,entdiaryForm.photo2,entdiaryForm.photo3,"000");
		}
	}
	
	//本文装飾
	private void resetResults(List<BeanMap> lbm){
		
		for (int i=0;i<lbm.size();i++){
			
			//本文を取得
			String reComment = (String)lbm.get(i).get("comment");

			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
					
			//逆サニタイジング
			reComment = CmnUtility.reSanitizing(reComment);
			
			//YouTubeタグ変換
			reComment = CmnUtility.replaceYoutube(reComment);
			
			//googleMapタグ変換
			reComment = CmnUtility.replaceGoogleMap(reComment);
			
			//フォトアルバム変換
			reComment = photoService.replacePhotoAlbum(reComment,userInfoDto.memberId);
			
		    //絵文字装飾
		  	String emojiComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
		  	
		  	//BeanMapへ格納
		  	lbm.get(i).put("viewComment", emojiComment);
		  	
		  	//lbm.get(i).put("viewComment", sb.toString());
		
		}
	}
	
	/**
	 * 新規作成・編集を判定し文言をセットする
	 * @param editType	新規作成・編集判定FLG
	 */
	private void setTitle(String editType){
		if (!StringUtils.isNullOrEmpty(editType)){
			//0:新規作成
			if (editType.equals("0")){
				entDiaryTitle		= "[frontier]";
				entDiaryFinding		= "日記内容";
				entDiarySubmitName	= "entry";
				entDiarySubmitValue	= "作成する";
			//1:編集
			}else if(editType.equals("1")){
				entDiaryTitle		= "[frontier] 日記を編集する";
				entDiaryFinding		= "日記の編集";
				entDiarySubmitName	= "editDiary";
				entDiarySubmitValue	= "編集する";
			}
		}
	}
	
	//日記公開範囲設定
	private void exePubDiary(){
		result = entdiaryService.selPubDiary(userInfoDto.memberId);
		entdiaryForm.pubDiary = result.get(0).get("pubDiary").toString();
		//標準の公開設定
		pubDiaryLevel = result.get(0).get("pubDiary").toString();

	}
	
}
