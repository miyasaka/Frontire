package frontier.action.pc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.arnx.jsonic.JSON;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.RequestUtil;

import de.nava.informa.core.ChannelFormat;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.exporters.RSS_2_0_Exporter;
import de.nava.informa.impl.basic.ChannelBuilder;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.DiaryDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Diary;
import frontier.entity.FrontierUserManagement;
import frontier.entity.Members;
import frontier.form.pc.DiaryForm;
import frontier.service.CommonService;
import frontier.service.DiaryCmnService;
import frontier.service.DiaryService;
import frontier.service.FriendinfoCmnService;
import frontier.service.GroupService;
import frontier.service.MembersService;
import frontier.service.PhotoService;
import frontier.service.RssmanageService;

public class DiaryAction {
    Logger logger = Logger.getLogger(this.getClass().getName());
	
    @ActionForm
    @Resource
    protected DiaryForm diaryForm;
    
    @Resource
    public UserInfoDto userInfoDto;
    @Resource
    public AppDefDto appDefDto;
    @Resource
    public DiaryDto diaryDto;
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
    private HttpServletRequest request;
    @Resource
    protected CommonService commonService;
    @Resource
    protected GroupService groupService;
    @Resource
    protected RssmanageService rssmanageService;

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
    public String cntcommentOutside;
    public FrontierUserManagement frontierUserManagement;
    
   //内部制御用
    private Timestamp entdate;
    private List<String> diaryAuthList;
    private List<String> pubLevelList;
    private List<String> appStatusList;
    //public HttpServletRequest request;
    
    //外部日記制御用
    private String volDiary;

	@Execute(validator=false,urlPattern="list/{mid}")
	public String list(){
		//初期化
		diaryForm.diaryDay = today;
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

		return "/pc/diary/list.jsp";
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
		if(diaryForm.searchFlg){
			//条件あり検索
			initListDiary(diaryForm.diaryDay.substring(0, 6),null,diaryForm.offset);
		}else{
			//全件検索
			initListDiary(null,null,diaryForm.offset);
		}
		
		return "/pc/diary/list.jsp";
		
	}
	
	//「<」を押した場合の処理
	@Execute(validator=false,urlPattern="beforeMonth/{mid}")
	public String beforeMonth(){
		//初期化
		setDiaryForm(true,"1");		
		
		//月の減算
		diaryForm.diaryDay = CmnUtility.calcCalendar(diaryForm.diaryDay,-1);
		
		//F5対策
		return "/pc/diary/myView?redirect=true";
	}
	
	//「>」を押した場合の処理
	@Execute(validator=false,urlPattern="nextMonth/{mid}")
	public String nextMonth(){
		//初期化
		setDiaryForm(true,"1");		

		//月の加算
		diaryForm.diaryDay = CmnUtility.calcCalendar(diaryForm.diaryDay,1);

		//F5対策		
		return "/pc/diary/myView?redirect=true";
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
		initListDiary(diaryForm.diaryDay.substring(0, 6),null,0);
		
		return "/pc/diary/list.jsp";
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
		initListDiary(null,diaryForm.diaryDay,0);
		
		return "/pc/diary/list.jsp";
	}
	
	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setDiaryPage(1);
		
		//F5対策
		return "/pc/diary/myView?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setDiaryPage(-1);		
		
		//F5対策
		return "/pc/diary/myView?redirect=true";
	}
		
	//日記削除確認画面への遷移
	//@Execute(validator=false)
	@Execute(validator=true,input="myView")
	public String delete(){
		if (diaryForm.checkDiaryId == null){
			//チェックボックス未選択
			return "/pc/diary/list/"+diaryForm.mid;
		} else {
			//削除確認画面へ
			return "/pc/entdiary/delete";
		}		
	}
	
	//日記閲覧画面初期処理
	@Execute(validator=false,urlPattern="view/{diaryId}/{diaryDay}/{mid}")
	public String view(){
		//初期処理
		diaryForm.comment  = null;
		diaryForm.picnote1 = null;
		diaryForm.picnote2 = null;
		diaryForm.picnote3 = null;
		diaryForm.guestname  = null;
		diaryForm.guestemail = null;
		diaryForm.guesturl   = null;
		
		try {
			//初期化
			initDiary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initViewDiary();
		
		//logger.debug("******** membertype ***********"+userInfoDto.membertype);
		if(userInfoDto.membertype.equals("2")){
			//logger.debug("******** チェック！！！ ***********");
			//外部公開日記の有無
			if(volDiary.equals("1")){
				//logger.debug("******** 出直し！！！ ***********");
				return "/pc/login/?redirect=true";
			}
		}

		//DB検索処理
		setViewDB();		
		
		return "/pc/diary/view.jsp";
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
				
		return "/pc/diary/view.jsp";
	}
	
	//前の日記を押した場合
	@Execute(validator=false)
	public String preDate(){
		diaryForm.diaryId = diaryForm.preDiaryId;
		diaryForm.diaryDay = diaryForm.preDiaryDay;
		
		//F5対策
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay + "/" + diaryForm.mid + "/?redirect=true";
	}
	
	//次の日記を押した場合
	@Execute(validator=false)
	public String nextDate(){
		diaryForm.diaryId = diaryForm.nextDiaryId;
		diaryForm.diaryDay = diaryForm.nextDiaryDay;
		
		//F5対策
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay + "/" + diaryForm.mid + "/?redirect=true";
	}
	
	//書き込みボタンを押した場合
	@Execute(validate="checkView",input="errView")
	public String comConfirm(){
		logger.debug("日記ID"+diaryForm.diaryId);
		
		//DB登録
		diaryService.insDiaryComment(diaryForm.mid,diaryForm,userInfoDto.memberId);
		
		//F5対策
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay + "/" + diaryForm.mid + "/?redirect=true";
	}
	
	//却下ボタンを押した場合(日記閲覧)
	@Execute(validator=false,urlPattern="rejection/{mid}/{diaryId}")
	public String rejection(){
		//外部公開を却下する
		diaryService.updDiaryApproval(diaryForm.mid, diaryForm.diaryId, userInfoDto.memberId, "9", "0");
		
		//F5対策
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay + "/" + diaryForm.mid + "/?redirect=true";		
	}
	
	//Frontier net公開承認ボタンを押した場合(日記閲覧)
	@Execute(validator=false,urlPattern="fApproval/{mid}/{diaryId}")
	public String fApproval(){
		//Frontier net公開を承認する
		diaryService.updDiaryApproval(diaryForm.mid, diaryForm.diaryId, userInfoDto.memberId, "1", "2");

		//F5対策
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay + "/" + diaryForm.mid + "/?redirect=true";		
	}
	
	//外部公開承認ボタンを押した場合(日記閲覧)
	@Execute(validator=false,urlPattern="approval/{mid}/{diaryId}")
	public String approval(){
		//外部公開を承認する
		diaryService.updDiaryApproval(diaryForm.mid, diaryForm.diaryId, userInfoDto.memberId, "0", "4");

		//外部公開用xmlファイル出力
		initOutsideRss();
		
		//F5対策
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay + "/" + diaryForm.mid + "/?redirect=true";		
	}
	
	//日記コメント削除確認画面への遷移
	@Execute(validator=true,input="errView")
	public String delComment(){
		return "/pc/diary/confirm/";	
	}
	
	//コメント削除確認（マイ用）
	@Execute(validator=false)
	public String confirm(){
		//初期処理
		initDelete("my");
		
		return "/pc/diary/confirm.jsp";
	}
	
	//コメント削除確認（メンバー用）
	@Execute(validator=false,urlPattern="memDelete/{checkCommentNo}")
	public String memDelete(){
		//初期処理
		initDelete("member");
		if(results.size()==0) return "error.jsp";
		
		return "/pc/diary/confirm.jsp";	
	}
	
	//コメント削除確認・削除実行
	@Execute(validator=false,reset="dummy")
	public String exeDelete(){
		//コメント削除実行
		diaryService.updDiaryCommentDelflg(diaryForm.mid, diaryForm,userInfoDto.memberId);
		
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay +"/"+diaryForm.mid+"/?redirect=true";
	}
	
	//コメント削除確認・前の画面に戻る
	@Execute(validator=false)
	public String stop(){
		return "/pc/diary/view/"+ diaryForm.diaryId +"/"+ diaryForm.diaryDay + "/" + diaryForm.mid;
	}
	
	//日記閲覧：F Shoutへ投稿するリンク
	@Execute(validator=false)
	public String goshout(){
		//日記のURLを生成
		String diaryUrl = "http://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/diary/view/" + diaryForm.diaryId.toString() +"/" + diaryForm.diaryDay + "/" + diaryForm.mid;
		
		//bit.lyを使ったURLの短縮
		String shortUrl = CmnUtility.makeBitLy(appDefDto, diaryUrl);
		
		if(shortUrl.equals("NG")){
			return "/pc/common/error.jsp";
		}

		//タイトルを取得するため日記を検索
		Diary diary = diaryCmnService.getResultByDiary(diaryForm.mid, diaryForm.diaryId);
		String title = diary.title;
		
		//sessionに設定
		diaryDto.fscomment = title + " " + shortUrl;
		
		return "/pc/top/index/?redirect=true";
	}
	
	//日記一覧画面必須処理
	private void initListDiary(String month,String day,int offset){
		//ここでID等設定メソッドが必要になる。
		
		//日記一覧検索実行
		results = diaryService.selDiaryList(diaryForm.mid,month,day,offset,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList,appDefDto.FP_MY_DIARYLIST_PGMAX);
		monthResults = diaryService.selDiaryMonthList(diaryForm.mid,diaryForm.diaryDay.substring(0, 6),diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);	
		resultscnt = diaryService.cntDiaryList(diaryForm.mid,month,day,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);

		cmnMakeCalendar();
		
		//装飾タグの削除
		CmnUtility.editcmnt(results, "comment", "pic1", "pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_CONTENTS_ROOT);
	}
	
	//日記一覧初期化処理
	private void setDiaryForm(boolean bl,String flg){
		diaryForm.pgcnt = 0;
		diaryForm.offset = 0;
		diaryForm.searchFlg = bl;
		diaryForm.diaryId = null;
		diaryForm.titleFlg = flg;
	}

	//日記一覧改ページ処理
	private void setDiaryPage(int num){
		try {
			// ページ遷移用の計算
			diaryForm.pgcnt = diaryForm.pgcnt + num;
			diaryForm.offset = diaryForm.pgcnt * appDefDto.FP_MY_DIARYLIST_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			diaryForm.pgcnt = 1;
			diaryForm.offset = 0;
		}		
	}
	
	//日記閲覧画面初期処理
	private void initViewDiary(){
		//カレンダーの表示
		monthResults = diaryService.selDiaryMonthList(diaryForm.mid,diaryForm.diaryDay.substring(0, 6),diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);
		
    	//日記の場合はmidを必ず持っている。
	    String mid = request.getParameter("mid");
	    if(userInfoDto.membertype.equals("2")){
    		long diaryNum = 0;
    		//外部公開日記を検索する
    		if(request.getParameter("diaryId") == null){
    			//日記一覧に遷移する場合
    			diaryNum = diaryService.selOutPubDiary(mid);
    		}else{
    			//日記閲覧に遷移する場合
    			diaryNum = diaryService.selOutPubDiary(mid, request.getParameter("diaryId"));
    		}
    		if(diaryNum > 0){
    			volDiary = "0";
    		}else{
    			volDiary = "1";
    		}
    	}
		
		cmnMakeCalendar();
		
		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);
		
	}
	
	//日記閲覧DB検索処理
	private void setViewDB(){
		//既読未読FLGの設定
		if(vUser){
			diaryService.updDiaryReadflg(userInfoDto.memberId, diaryForm.diaryId);
		}
		
		//検索
		viewResults = diaryService.selDiaryViewListPC(diaryForm.mid, diaryForm.diaryId,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);
		preDiaryResults = diaryService.selPreDiary(diaryForm.mid, diaryForm.diaryId,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);
		nextDiaryResults = diaryService.selNextDiary(diaryForm.mid, diaryForm.diaryId,diaryAuthList,userInfoDto.membertype,pubLevelList,appStatusList);
		
		//前の日記の設定
		if (!preDiaryResults.isEmpty()){
			diaryForm.preDiaryId = (Integer)preDiaryResults.get(0).get("diaryid");
			diaryForm.preDiaryDay = (String)preDiaryResults.get(0).get("pentdate");
		} else {
			diaryForm.preDiaryId = null;
		}
		
		//次の日記の設定
		if (!nextDiaryResults.isEmpty()){
			diaryForm.nextDiaryId = (Integer)nextDiaryResults.get(0).get("diaryid");
			diaryForm.nextDiaryDay = (String)nextDiaryResults.get(0).get("nentdate");
		} else {
			diaryForm.nextDiaryId = null;
		}
		
		//本文の装飾
		resetResults(viewResults);
		
		//画像説明セット
		oyaPicnote1 = viewResults.get(0).get("picnote1").toString();
		oyaPicnote2 = viewResults.get(0).get("picnote2").toString();
		oyaPicnote3 = viewResults.get(0).get("picnote3").toString();
		if(oyaPicnote1.equals(null) || oyaPicnote1.equals("")){
			oyaPicnote1 = "画像１";
		}
		if(oyaPicnote2.equals(null) || oyaPicnote2.equals("")){
			oyaPicnote2 = "画像２";
		}
		if(oyaPicnote3.equals(null) || oyaPicnote3.equals("")){
			oyaPicnote3 = "画像３";
		}
		
		//コメント件数取得
		//一般ユーザ
		logger.debug("********* userInfoDto.membertype ***********"+userInfoDto.membertype);
		if(userInfoDto.membertype.equals("0") || userInfoDto.membertype.equals("1")){
			cntcomment = viewResults.get(0).get("comments").toString();
		} else {
			cntcommentOutside = viewResults.get(0).get("commentsoutside").toString();
		}
	}
	
	//日記閲覧入力チェック
	public ActionMessages checkView(){
		return diaryCmnService.checkFile(diaryForm);
	}
	
	//共通カレンダー作成
	private void cmnMakeCalendar(){
		//カレンダー生成
		cal = CmnUtility.makeCustomCalendar(diaryForm.diaryDay,monthResults);
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
			reComment = CmnUtility.convURL(reComment);
			
			//YouTubeタグ変換
			reComment = CmnUtility.replaceYoutube(reComment);
			
			//googleMapタグ変換
			reComment = CmnUtility.replaceGoogleMap(reComment);
			
			//フォトアルバム変換
			reComment = photoService.replacePhotoAlbum(reComment,diaryForm.mid);
			
		    //絵文字装飾
		  	String emojiComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
		  	
		  	//BeanMapへ格納
		  	lbm.get(i).put("viewComment", emojiComment);
		  	
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
		if (userInfoDto.membertype.equals("2")){
			pubLevelList.add(0,"0");
			appStatusList.add(0,"4");
		} else if (userInfoDto.membertype.equals("1")){
			pubLevelList.add(0,"0");
			pubLevelList.add(1,"1");
			appStatusList.add(0,"4");
			appStatusList.add(1,"2");
		}

		//日記外部・FNet公開承認権限設定(必ずグループチェックの前で行う)
		setVManagementLevel();
		
		//基本権限
		diaryAuthList.add(0, appDefDto.FP_CMN_DIARY_AUTH1[0]);
		
		//パラメータのIDが自分かメンバーかを判断する
		if (diaryForm.mid.equals(userInfoDto.memberId)){
			//自分の場合
			vNickname = userInfoDto.nickName;
			vEntdate = CmnUtility.dateFormat("yyyyMMdd", userInfoDto.entdate);
			entdate = userInfoDto.entdate;

			diaryAuthList.add(1, appDefDto.FP_CMN_DIARY_AUTH3[0]);
			diaryAuthList.add(2, appDefDto.FP_CMN_DIARY_AUTH4[0]);
			
			//画面表示用
			vUser = true;
		} else {
			//画面表示用
			vUser = false;
			
			//メンバーの場合
			members = membersService.getResultById(diaryForm.mid);

			if (members != null){
				vEntdate = CmnUtility.dateFormat("yyyyMMdd", members.entdate);
				entdate = members.entdate;
				vNickname = members.nickname;
			} else {
				Exception e = new Exception();
				throw e;
			}
			
			//ここで同じグループかを検索
			List<BeanMap> lbm = commonService.getMidList("1", userInfoDto.memberId);
			
			//同じグループかを調査する。
			for (int i=0;i<lbm.size();i++){
				logger.debug(lbm.get(i).get("mid")+":"+diaryForm.mid);
				if (lbm.get(i).get("mid").equals(diaryForm.mid)){
					//一致すればグループ
					diaryAuthList.add(1, appDefDto.FP_CMN_DIARY_AUTH3[0]);
					return;
				}
			}
			
		}
	}
		
	//削除確認画面初期処理
	private void initDelete(String owner){
		//メニュー出しわけ用変数設定
		setVisitMemberId();
		
		//DB検索
		results = diaryService.selDeleteCommentList(diaryForm.mid, diaryForm.diaryId, diaryForm.checkCommentNo,owner.equals("my")?"":userInfoDto.memberId);
		
		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);
		
		//絵文字の装飾&サニタイジング
		resetResults(results);
	}
	
	//メニュー出しわけ用変数設定
	private void setVisitMemberId(){
		userInfoDto.visitMemberId = diaryForm.mid;
	}
	
	//外部公開用のRSSファイル初期設定処理
	private void initOutsideRss(){
		String title;
		List<String> appStatusList = new ArrayList<String>();
		//外部公開する日記の申請状況
		appStatusList.add(0, "4");
		
		//外部公開日記を書いた人のファイル出力形式を取得する。
		List<BeanMap> rssId = rssmanageService.selFrontierRssMemberId(diaryForm.mid,appDefDto.FP_CMN_RSS_LISTMAX);
		
		//設定されている出力形式の数だけRSSファイルを生成する
		for(int i=0;i<rssId.size();i++){
			
			//RSS出力用日記の検索
			List<BeanMap> rssResults = diaryService.selOutsideDiaryRss((Integer)rssId.get(i).get("id"),appStatusList);

			//RSS出力用の日記が存在すればxmlファイルを作成する
			if (!rssResults.isEmpty()){
				//タイトル設定
				title = (String)rssId.get(i).get("patternname");
				
				//RSSファイル出力
				makeRssFile(rssResults,"outside",(String)rssId.get(i).get("filename")+".xml",title,"http://"+appDefDto.FP_CMN_HOST_NAME+"/frontier/pc/diary/list/"+diaryForm.mid,"ALL");
			}
		}
		
		
		

	}
	
	//xmlファイル作成
	private void makeRssFile(List<BeanMap> results,String subDir,String fileName,String title,String titleUrl,String pubNet) {
		ChannelBuilder builder = new ChannelBuilder();

		// タイトルを指定してRSSフィードを作成
		ChannelIF newChannel = builder.createChannel(title);

		// RSSフィードのバージョンを指定
		newChannel.setFormat(ChannelFormat.RSS_2_0);
		// 言語を指定
		newChannel.setLanguage("ja");
		try {
			String url;
			DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			
			// 配信サイトを指定
			newChannel.setSite(new URL(titleUrl));

			//ここをループする。
			// タイトル、概要、URLを指定して項目を作成
			for (int i=0;i<results.size();i++){
				url = "http://"+appDefDto.FP_CMN_HOST_NAME+"/frontier/pc/diary/view/"+results.get(i).get("diaryid")+"/"+results.get(i).get("entdate").toString().substring(0, 8)+"/"+results.get(i).get("mid");

				/* 外部用とFNet用は区別する必要なし。FNetは別モジュールとなるため。
				if (pubNet.equals("ALL")){
					url = "http://"+appDefDto.FP_CMN_HOST_NAME+"/frontier/pc/diary/view/"+results.get(i).get("diaryid")+"/"+results.get(i).get("entdate").toString().substring(0, 8)+"/"+results.get(i).get("mid");
				}
				else{
					url = "http://"+appDefDto.FP_CMN_HOST_NAME+"/frontier/pc/openid/auth?did="+results.get(i).get("diaryid")+"&amp;cday="+results.get(i).get("entdate").toString().substring(0, 8)+"&amp;dmid="+results.get(i).get("mid")+"&amp;gm=dv";					
				}
				*/

				ItemIF item =
					builder.createItem(
						newChannel,
						(String)results.get(i).get("title"),
						//descriptionにはニックネームを設定する。authorが設定できないため。
						//(String)results.get(i).get("diarycategory"),
						(String)results.get(i).get("nickname"),
						new URL(url));
				// 項目の更新日時を指定
				//item.setDate(new Date());
				try {
					item.setDate(format.parse((String)results.get(i).get("entdate")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				item.setComments(new URL("http://"+appDefDto.FP_CMN_HOST_NAME+"/frontier/pc/diary/list/"+(String)results.get(i).get("mid")));
			
				// 項目をRSSフィードに追加
				newChannel.addItem(item);
				
			}

			//xmlファイル出力用ディレクトリ作成
			CmnUtility.cmnMakeDir(appDefDto.FP_CMN_RSS_DIR);
			CmnUtility.cmnMakeDir(appDefDto.FP_CMN_RSS_DIR+"/"+subDir);
			
			// RSSフィードを出力
			RSS_2_0_Exporter writer =
				new RSS_2_0_Exporter(new File(appDefDto.FP_CMN_RSS_DIR+"/"+subDir+"/"+fileName));
			writer.write(newChannel);
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//日記外部・FNet公開承認権限設定
	private void setVManagementLevel(){
		//ログイン者が管理しているグループを取得する
		List<BeanMap> myManageGroup = groupService.selGroupManage(userInfoDto.memberId);

		if(myManageGroup.size()!=0){
			//初期値は権限なし
			vManagementLevel = "0";	
			
			//日記を表示しているユーザの所属グループを取得する
			List<BeanMap> memberGroup = commonService.getMidList("4", diaryForm.mid);
			if(memberGroup.size()!=0){
				String myGid;
				String memGid;
				
				//自分が管理しているグループが日記を表示しているユーザの所属グループと一致するかチェック
				for(int i=0;i<myManageGroup.size();i++){
					myGid = (String)myManageGroup.get(i).get("gid");
					
					for(int j=0;j<memberGroup.size();j++){
						memGid = (String)memberGroup.get(j).get("gid");
						
						if(myGid.equals(memGid)){
							//一致していれば、権限ありとする
							vManagementLevel = "1";	
							break;
						}
					}
					
					if(vManagementLevel.equals("1")){
						//一致していれば、これ以上繰り返す必要がないのでループを脱出する
						break;
					}
				}
			}else{
				//日記を表示しているユーザがグループに所属していない場合は、権限なしとする
				vManagementLevel = "0";				
			}
		}else{
			//管理しているグループが1件もない場合は、権限なしとする
			vManagementLevel = "0";
		}		
	}
	
}
