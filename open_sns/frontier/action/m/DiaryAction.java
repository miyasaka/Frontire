package frontier.action.m;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.RequestUtil;

import frontier.common.CmnUtility;
import frontier.common.EmojiUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.form.m.DiaryMForm;
import frontier.service.CommonService;
import frontier.service.DiaryCmnService;
import frontier.service.DiaryService;
import frontier.service.FriendListService;
import frontier.service.FriendinfoCmnService;
import frontier.service.MembersService;
import frontier.service.PhotoService;

public class DiaryAction {
    Logger logger = Logger.getLogger(this.getClass().getName());
	
    @ActionForm
    @Resource
    protected DiaryMForm diaryMForm;
    
    @Resource
    public UserInfoDto userInfoDto;
    @Resource
    public AppDefDto appDefDto;
    @Resource
    protected DiaryService diaryService;
    @Resource
    protected DiaryCmnService diaryCmnService;
    @Resource
    protected MembersService membersService;
    @Resource
    protected FriendinfoCmnService friendinfoCmnService;
    @Resource
    protected PhotoService photoService;
    @Resource
    protected FriendListService friendListService;
    @Resource
    protected CommonService commonService;
    
    //インスタンス変数
    //今日の日付
    public String today = CmnUtility.getToday("yyyyMMdd");
    public List<Map<String,Integer>> cal;
    public Map<String,List<String>> oldCal;
    //日記一覧検索結果
    public List<BeanMap> results;
    public long resultscnt;
    //日記の存在有無
    public boolean existDiary = true;
    //日記閲覧検索結果
    public List<BeanMap> viewResults;
    //カレンダーのリンク表示用
    protected List<BeanMap> monthResults;
    //前の日記用
    private List<BeanMap> preDiaryResults;
    //次の日記用
    private List<BeanMap> nextDiaryResults;
	//public ActionMessages errors = new ActionMessages();
    private Members members;
    private Members myMembers;
    
    //画面表示用
    public String vNickname;
    public String vEntdate;
    public boolean vUser;
    public String vManagementLevel;
    //画像説明
    public String oyaPicnote1;
    public String oyaPicnote2;
    public String oyaPicnote3;
    public String comPicnote1;
    public String comPicnote2;
    public String comPicnote3;
    //コメント件数
    public String cntcomment;
    
   //内部制御用
    private Timestamp entdate;
    private List<String> diaryAuthList;
    private List<String> pubLevelList;
    private List<String> appStatusList;
    //public HttpServletRequest request;

	@Execute(validator=false,urlPattern="list/{mid}")
	public String list(){
		//初期化
		diaryMForm.diaryDay = today;
		setDiaryForm(false,"0");		

		try {
			//初期化
			initDiary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//共通処理
		initListDiary(null,null,0);

		//日記の有無をチェック
		if (results.size()==0){
			existDiary = false;
		}
		return "/m/diary/list.jsp";
	}
	
	//自画面からの遷移用
	@Execute(validator=false)
	public String myView(){

		//初期化処理
		try {
			initDiary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//共通処理
		if(diaryMForm.searchFlg){
			//条件あり検索
			initListDiary(diaryMForm.diaryDay.substring(0, 6),null,diaryMForm.offset);
		}else{
			//全件検索
			initListDiary(null,null,diaryMForm.offset);
		}
		
		return "/m/diary/list.jsp";
		
	}
	
	//「<」を押した場合の処理
	@Execute(validator=false,urlPattern="beforeMonth/{mid}")
	public String beforeMonth(){
		//初期化
		setDiaryForm(true,"1");		
		
		//月の減算
		diaryMForm.diaryDay = CmnUtility.calcCalendar(diaryMForm.diaryDay,-1);
		
		//F5対策
		return "/m/diary/myView?redirect=true";
	}
	
	//「>」を押した場合の処理
	@Execute(validator=false,urlPattern="nextMonth/{mid}")
	public String nextMonth(){
		//初期化
		setDiaryForm(true,"1");		

		//月の加算
		diaryMForm.diaryDay = CmnUtility.calcCalendar(diaryMForm.diaryDay,1);

		//F5対策		
		return "/m/diary/myView?redirect=true";
	}
	
	//過去の日記の月を押した場合の処理
	@Execute(validator=false,urlPattern="searchMonth/{diaryDay}/{mid}")
	public String searchMonth(){

		//初期化処理
		try {
			initDiary();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		//初期化
		setDiaryForm(true,"1");		

		//共通処理
		initListDiary(diaryMForm.diaryDay.substring(0, 6),null,0);
		
		return "/m/diary/list.jsp";
	}
	
	//日付リンクを押した場合の処理
	@Execute(validator=false,urlPattern="searchDay/{diaryDay}/{mid}")
	public String searchDay(){
		
		//初期化処理
		try {
			initDiary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//初期化
		setDiaryForm(true,"2");		

		//共通処理
		initListDiary(null,diaryMForm.diaryDay,0);
		
		return "/m/diary/list.jsp";
	}
	
	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setDiaryPage(1);
		
		//F5対策
		return "/m/diary/myView?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setDiaryPage(-1);		
		
		//F5対策
		return "/m/diary/myView?redirect=true";
	}
		
	//日記削除確認画面への遷移
	//@Execute(validator=false)
	@Execute(validator=true,input="myView")
	public String delete(){
		if (diaryMForm.checkDiaryId == null){
			//チェックボックス未選択
			return "/m/diary/list/"+diaryMForm.mid;
		} else {
			//削除確認画面へ
			return "/m/entdiary/delete";
		}		
	}
	
	//日記閲覧画面初期処理
	@Execute(validator=false,urlPattern="view/{diaryId}/{diaryDay}/{mid}")
	public String view(){
		logger.debug("日記閲覧");
		//初期処理
		diaryMForm.comment = null;
		diaryMForm.picnote1 = null;
		diaryMForm.picnote2 = null;
		diaryMForm.picnote3 = null;
		
		try {
			//初期化
			initDiary();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//DB検索処理
		setViewDB();		
		
		if(viewResults.size()==0){
			return "error.jsp";
		}
		
		
		return "/m/diary/view.jsp";
	}
	
	@Execute(validator=false)
	public String errView() throws IOException{
        //リクエストから、SAStrutsが格納した例外を取得します。
        SizeLimitExceededException e = (SizeLimitExceededException) RequestUtil.getRequest()
                        .getAttribute(S2MultipartRequestHandler.SIZE_EXCEPTION_KEY);
        if(e!=null){
        	ActionMessages errors = new ActionMessages();
        	
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            		"errors.upload.size",new Object[] { e.getActualSize(),e.getPermittedSize()  }));
    		//ActionMessagesUtil.addErrors(request, errors);
        }
		
		try {
			//初期化
			initDiary();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
		//初期処理
		initViewDiary();
		
		//DB検索処理
		setViewDB();
				
		return "/m/diary/view.jsp";
	}
	
	//前の日記を押した場合
	@Execute(validator=false)
	public String preDate(){
		diaryMForm.diaryId = diaryMForm.preDiaryId;
		diaryMForm.diaryDay = diaryMForm.preDiaryDay;
		
		//F5対策
		return "/m/diary/view/"+ diaryMForm.diaryId +"/"+ diaryMForm.diaryDay + "/" + diaryMForm.mid + "/?redirect=true";
	}
	
	//次の日記を押した場合
	@Execute(validator=false)
	public String nextDate(){
		diaryMForm.diaryId = diaryMForm.nextDiaryId;
		diaryMForm.diaryDay = diaryMForm.nextDiaryDay;
		
		//F5対策
		return "/m/diary/view/"+ diaryMForm.diaryId +"/"+ diaryMForm.diaryDay + "/" + diaryMForm.mid + "/?redirect=true";
	}
	
	//書き込みボタンを押した場合
	@Execute(validator=true,input="errView")
	public String comConfirm(){
		logger.debug("日記ID"+diaryMForm.diaryId);
		
		//DB登録
		diaryService.insDiaryComment(diaryMForm.mid,diaryMForm,userInfoDto.memberId);
		
		//F5対策
		return "/m/diary/view/"+ diaryMForm.diaryId +"/"+ diaryMForm.diaryDay + "/" + diaryMForm.mid + "/?redirect=true";
	}
	
	//却下ボタンを押した場合(日記閲覧)
	@Execute(validator=false,urlPattern="rejection/{mid}/{diaryId}")
	public String rejection(){
		//外部公開を却下する
		diaryService.updDiaryApproval(diaryMForm.mid, diaryMForm.diaryId, userInfoDto.memberId, "9", "0");
		
		//F5対策
		return "/m/diary/view/"+ diaryMForm.diaryId +"/"+ diaryMForm.diaryDay + "/" + diaryMForm.mid + "/?redirect=true";		
	}
	
	//日記コメント削除確認画面への遷移
	@Execute(validator=true,input="errView")
	public String delComment(){
		return "/m/diary/confirm/";	
	}
	
	//コメント削除確認（マイ用）
	@Execute(validator=false)
	public String confirm(){
		//初期処理
		initDelete("my");
		
		return "/m/diary/confirm.jsp";
	}
	
	//コメント削除確認（メンバー用）
	@Execute(validator=false,urlPattern="memDelete/{checkCommentNo}")
	public String memDelete(){
		//初期処理
		initDelete("member");
		if(results.size()==0) return "error.jsp";
		
		return "/m/diary/confirm.jsp";	
	}
	
	//コメント削除確認・削除実行
	@Execute(validator=false,reset="dummy")
	public String exeDelete(){
		//コメント削除実行
		diaryService.updDiaryCommentDelflg(diaryMForm.mid, diaryMForm,userInfoDto.memberId);
		
		return "/m/diary/view/"+ diaryMForm.diaryId +"/"+ diaryMForm.diaryDay +"/"+diaryMForm.mid+"/?redirect=true";
	}
	
	//コメント削除確認・前の画面に戻る
	@Execute(validator=false)
	public String stop(){
		return "/m/diary/view/"+ diaryMForm.diaryId +"/"+ diaryMForm.diaryDay + "/" + diaryMForm.mid;
	}
	
	//日記画像閲覧
	@Execute(validator=false)
	public String imgview(){
		logger.debug(diaryMForm.picURL);
		diaryMForm.picURL = diaryMForm.picURL.replace("dir","pic180");
		return "imgview.jsp";
	}
	
	//日記一覧画面必須処理
	private void initListDiary(String month,String day,int offset){
		//ここでID等設定メソッドが必要になる。
		
		//日記一覧検索実行
		results = diaryService.selDiaryList(diaryMForm.mid,month,day,offset,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList,appDefDto.FP_MY_M_DIARYLIST_PGMAX);
		monthResults = diaryService.selDiaryMonthList(diaryMForm.mid,diaryMForm.diaryDay.substring(0, 6),diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);	
		resultscnt = diaryService.cntDiaryList(diaryMForm.mid,month,day,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);

		cmnMakeCalendar();
		
		//装飾タグの削除
		CmnUtility.editcmnt(results, "comment", "pic1", "pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_CONTENTS_ROOT);
	}
	
	//日記一覧初期化処理
	private void setDiaryForm(boolean bl,String flg){
		diaryMForm.pgcnt = 0;
		diaryMForm.offset = 0;
		diaryMForm.searchFlg = bl;
		diaryMForm.diaryId = null;
		diaryMForm.titleFlg = flg;
	}

	//日記一覧改ページ処理
	private void setDiaryPage(int num){
		try {
			// ページ遷移用の計算
			diaryMForm.pgcnt = diaryMForm.pgcnt + num;
			diaryMForm.offset = diaryMForm.pgcnt * appDefDto.FP_MY_M_DIARYLIST_PGMAX;
//			diaryMForm.offset = diaryMForm.pgcnt * 2;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			diaryMForm.pgcnt = 1;
			diaryMForm.offset = 0;
		}		
	}

	//日記閲覧画面初期処理
	private void initViewDiary(){
		//カレンダーの表示
		monthResults = diaryService.selDiaryMonthList(diaryMForm.mid,diaryMForm.diaryDay.substring(0, 6),diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);	

		cmnMakeCalendar();
	}
	
	//日記閲覧DB検索処理
	private void setViewDB(){
		//既読未読FLGの設定
		if(vUser){
			diaryService.updDiaryReadflg(userInfoDto.memberId, diaryMForm.diaryId);
		}
		
		//検索
		viewResults = diaryService.selDiaryViewListPC(diaryMForm.mid, diaryMForm.diaryId,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);
		preDiaryResults = diaryService.selPreDiary(diaryMForm.mid, diaryMForm.diaryId,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);
		nextDiaryResults = diaryService.selNextDiary(diaryMForm.mid, diaryMForm.diaryId,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);
		
		//前の日記の設定
		if (!preDiaryResults.isEmpty()){
			diaryMForm.preDiaryId = (Integer)preDiaryResults.get(0).get("diaryid");
			diaryMForm.preDiaryDay = (String)preDiaryResults.get(0).get("pentdate");
		} else {
			diaryMForm.preDiaryId = null;
		}
		
		//次の日記の設定
		if (!nextDiaryResults.isEmpty()){
			diaryMForm.nextDiaryId = (Integer)nextDiaryResults.get(0).get("diaryid");
			diaryMForm.nextDiaryDay = (String)nextDiaryResults.get(0).get("nentdate");
		} else {
			diaryMForm.nextDiaryId = null;
		}
		
		logger.debug("3");
		
		//本文の装飾
		resetResults(viewResults);
		
		logger.debug("4");
		
//		//画像説明セット
//		oyaPicnote1 = viewResults.get(0).get("picnote1").toString();
//		oyaPicnote2 = viewResults.get(0).get("picnote2").toString();
//		oyaPicnote3 = viewResults.get(0).get("picnote3").toString();
//		if(oyaPicnote1.equals(null) || oyaPicnote1.equals("")){
//			oyaPicnote1 = "画像１";
//		}
//		if(oyaPicnote2.equals(null) || oyaPicnote2.equals("")){
//			oyaPicnote2 = "画像２";
//		}
//		if(oyaPicnote3.equals(null) || oyaPicnote3.equals("")){
//			oyaPicnote3 = "画像３";
//		}
		
		for(BeanMap b:viewResults){
			logger.debug(b.get("comments"));
		}
		
		logger.debug("5");
		
		//コメント件数取得
		if(viewResults.size()>0) cntcomment = viewResults.get(0).get("comments").toString();
		
		logger.debug("6");
	}
	
	//日記閲覧入力チェック
	public ActionMessages checkView(){
		return diaryCmnService.checkFile(diaryMForm);
	}
	
	//共通カレンダー作成
	private void cmnMakeCalendar(){
		//カレンダー生成
		cal = CmnUtility.makeCustomCalendar(diaryMForm.diaryDay,monthResults);
		//過去カレンダー生成
		//oldCal = CmnUtility.makeOldCalendar(entdate);	
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
			
			//URLを<a>タグに変換
			//reComment = CmnUtility.convURL(reComment);
			
			//YouTubeタグ変換
			reComment = CmnUtility.replaceMYoutube(reComment);
			
			//googleMapタグ変換
			reComment = CmnUtility.replaceMGoogleMap(reComment);
			
			//フォトアルバム変換
			reComment = photoService.replaceMPhotoAlbum(reComment,diaryMForm.mid);
			
		    //絵文字装飾
			reComment = EmojiUtility.replacePcToMoblile(reComment,
					appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
//		  	String emojiComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
		  	
		  	//BeanMapへ格納
		  	lbm.get(i).put("viewComment", reComment);
		  	
		  	//lbm.get(i).put("viewComment", sb.toString());		
		}
	}
	
	//日記系画面に必要な初期設定
	private void initDiary() throws Exception{
		diaryAuthList = new ArrayList<String>();
		pubLevelList = new ArrayList<String>();
		appStatusList = new ArrayList<String>();

		//メニュー出しわけ用変数設定
		setVisitMemberId();

		//ユーザの基本情報の取得
		myMembers = membersService.getResultById(userInfoDto.memberId);
		
		//ユーザが存在しない場合はエラー
		if (myMembers == null){
			Exception e = new Exception();
			throw e;			
		}
		
		//日記公開レベルの設定(必ずグループチェックの前で行う)
		if (userInfoDto.membertype.equals("2")){//ユーザー以外
			pubLevelList.add(0,"0");
			appStatusList.add(0,"4");
		} else if (userInfoDto.membertype.equals("1")){//ユーザー
			pubLevelList.add(0,"0");
			pubLevelList.add(1,"1");
			appStatusList.add(0,"4");
			appStatusList.add(1,"2");
		}

		//ユーザ権限設定(必ずグループチェックの前で行う)
		vManagementLevel = myMembers.managementLevel;
		
		//パラメータのIDが自分かグループかを判断する
		if (diaryMForm.mid.equals(userInfoDto.memberId)){//ユーザー
			vNickname = userInfoDto.nickName;
			vEntdate = CmnUtility.dateFormat("yyyyMMdd", userInfoDto.entdate);
			entdate = userInfoDto.entdate;

			diaryAuthList.add(0, appDefDto.FP_CMN_DIARY_AUTH1[0]);
//			diaryAuthList.add(1, appDefDto.FP_CMN_DIARY_AUTH2[0]);
			diaryAuthList.add(1, appDefDto.FP_CMN_DIARY_AUTH3[0]);
			diaryAuthList.add(2, appDefDto.FP_CMN_DIARY_AUTH4[0]);
			
			//画面表示用
			vUser = true;
		} else {//グループ
			
			//画面表示用
			vUser = false;
			
			//グループ&フォロー存在チェック
			if(!Existsgroup(diaryMForm.mid)){
				Exception e = new Exception();
				throw e;	
			}
			
			//グループの場合
			members = membersService.getResultById(diaryMForm.mid);

			if (members != null){
				vEntdate = CmnUtility.dateFormat("yyyyMMdd", members.entdate);
				entdate = members.entdate;
				vNickname = members.nickname;
			} else {
				Exception e = new Exception();
				throw e;
			}
			diaryAuthList.add(0, appDefDto.FP_CMN_DIARY_AUTH1[0]);
			diaryAuthList.add(1, appDefDto.FP_CMN_DIARY_AUTH3[0]);
		}
	}
	
	//グループ検索
	private boolean Existsgroup(String mid){
		boolean flg = false;
		List<BeanMap> GroupList = new ArrayList<BeanMap>();
		List<BeanMap> FList = new ArrayList<BeanMap>();
		//グループ一覧データ取得
		GroupList = commonService.getMidList("1",userInfoDto.memberId);
		if(GroupList.size()>0){
			for(BeanMap f:GroupList){
				if(mid.equals(f.get("mid"))) flg = true;
			}
		}
		//フォロー一覧データ取得
		FList = friendListService.selectFollowerIdList(userInfoDto.memberId,diaryMForm.diaryId,mid);
		if(FList.size()>0){
			for(BeanMap f:FList){
				if(mid.equals(f.get("mid"))) flg = true;
			}
		}
		
		return flg;
	}
		
	//削除確認画面初期処理
	private void initDelete(String owner){
		//メニュー出しわけ用変数設定
		setVisitMemberId();
		
		//DB検索
		results = diaryService.selDeleteCommentList(diaryMForm.mid, diaryMForm.diaryId, diaryMForm.checkCommentNo,owner.equals("my")?"":userInfoDto.memberId);
		
		//絵文字の装飾&サニタイジング
		resetResults(results);
	}
	
	//メニュー出しわけ用変数設定
	private void setVisitMemberId(){
		userInfoDto.visitMemberId = diaryMForm.mid;
	}
	
}
