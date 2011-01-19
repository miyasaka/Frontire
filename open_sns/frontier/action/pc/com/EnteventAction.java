package frontier.action.pc.com;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.Mid;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.EntbbsForm;
import frontier.service.CommunityService;

public class EnteventAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public CommunityDto communityDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected CommunityService communityService;
	@ActionForm
	@Resource
	protected EntbbsForm entbbsForm;
	public List<BeanMap> result;
	public List<BeanMap> results;
	public List<String> addList;
	public String addflg;
	public String vMode;
	public String mLevel;
	// entrytype:2イベント
	private String entrytype = "2";

	// ■■■■■■　　　　新規処理　　　　■■■■■■
	// ■登録画面・初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// パラメタ初期化
		reset();
		// 新規作成画面・親
		entbbsForm.oyacid   = communityDto.cid;
		entbbsForm.oyabbsid = "";
		entbbsForm.edittype = "0"; // 0:新規 1:編集

		// init処理
		init();
		return "index.jsp";
	}
	
	// ■■■■■■　　　　新規処理(カレンダーから遷移)　　　　■■■■■■
	// ■登録画面・初期表示
	@Execute(validator=false,urlPattern="entry/{cid}/{yearofevent}/{monthofevent}/{dayofevent}",reset="clear")
	public String entry(){
		// パラメタ初期化
		//reset();
		// 新規作成画面・親
		entbbsForm.oyacid   = communityDto.cid;
		entbbsForm.oyabbsid = "";
		entbbsForm.edittype = "0"; // 0:新規 1:編集

		// init処理
		init();
		return "index.jsp";
	}

	// ■この内容で登録するボタン押下時
	@Execute(validate="chkPic",input="errorNew")
	public String insevent() throws IOException, Exception{
		// init処理
		init();

		ArrayList<String> newaddmidList=new ArrayList<String>();

		if(!entbbsForm.selmid.equals("")){
			if(entbbsForm.oyacid.equals(entbbsForm.kocid) && entbbsForm.oyabbsid.equals(entbbsForm.kobbsid)){
				String[] st =  entbbsForm.selmid.toString().split(",");
				for(int i=0;i<st.length;i++){
					newaddmidList.add(st[i]);
				}
			} else {
			}
		}

		// イベントの登録
		communityService.insEvent(
			communityDto.cid,
			entbbsForm.title,
			entbbsForm.comment,
			entbbsForm.picpath1,
			entbbsForm.picpath2,
			entbbsForm.picpath3,
			entbbsForm.picnote1,
			entbbsForm.picnote2,
			entbbsForm.picnote3,
			entbbsForm.yearofevent,
			entbbsForm.monthofevent,
			entbbsForm.dayofevent,
			entbbsForm.eventnote,
			entbbsForm.locationnote,
			entbbsForm.deadlineyear,
			entbbsForm.deadlinemonth,
			entbbsForm.deadlineday,
			newaddmidList
		);

		return "/pc/com/event/"+communityDto.cid;
	}

	// ■■■■■■　　　　編集処理　　　　■■■■■■
	// ■編集画面・初期表示
	@Execute(validator=false,urlPattern="edit/{cid}/{bbsid}")
	public String edit(){
		
	    //表示権限チェック
	    if(check().equals("NG")){
	    	mLevel = "1";
	    	//return "error.jsp";
	    }
	    logger.debug("****** イベント ********"+mLevel);
		
		//編集ではinitの前に設定しないといけない。
		entbbsForm.edittype = "1"; // 0:新規 1:編集
		
		// init処理
		init();
		// リセット
		entbbsForm.selmid   = "";
		entbbsForm.oyacid   = "";
		entbbsForm.oyabbsid = "";
		entbbsForm.offset   = 0;
		entbbsForm.pgcnt    = 0;

		// 編集画面・親
		entbbsForm.oyacid   = communityDto.cid;
		entbbsForm.oyabbsid = entbbsForm.bbsid;

		// イベント参加者一覧データ取得
		results = communityService.selectEventMemList2(communityDto.cid,entbbsForm.bbsid, entbbsForm.offset, appDefDto.FP_COM_EVENTLIST_MEMMAX);
		if(results.size()>0){
			//取得したメンバーＩＤを配列にする
			ArrayList<String> list=new ArrayList<String>();
			for(int i=0;i<results.size();i++){
				list.add(results.get(i).get("mid").toString());
			}
			//配列にしたメンバーＩＤを文字列にする（文字列を,区切りで結合）
			String eventMem = null;
			for(int i=0;i<list.size();i++){
				if(i==0){
					eventMem = list.get(i).toString();
				}else{
					eventMem +=","+list.get(i).toString();
				}
			}
			entbbsForm.selmid = eventMem;
		}

		// 編集用データの取得
		setEventDt();
		// 編集画面へ
		//return "edit.jsp";
		return "index.jsp";
	}
	
	
	// 編集の権限チェック
	public String check(){
		// 掲示板・親情報の取得
		result = communityService.getOyaView(
			communityDto.cid,
			entrytype,
			entbbsForm.bbsid
		);
		
		if(!result.get(0).get("editflg").toString().equals("1")){
			return "NG";
		}
		return "OK";
	}

	

	// ■この内容で編集するボタン押下時
	@Execute(validate="chkPic",input="errorNew")
	public String editevent() throws IOException, Exception{
		String allValFlg = "";
		
		// init処理
		init();

		// 参加者追加にて選択されたメンバーをセット
		// イベント参加メンバーＩＤを配列にする（更新処理用）
		ArrayList<String> addmidList=new ArrayList<String>();
		// 親cidと子cidが同じとき
		if(entbbsForm.oyacid.equals(entbbsForm.kocid) && entbbsForm.oyabbsid.equals(entbbsForm.kobbsid)){
			String[] st =  entbbsForm.selmid.toString().split(",");
			for(int i=0;i<st.length;i++){
				addmidList.add(st[i]);
			}
			// すでに登録しているメンバーの「外す」だけを押したか判断
			if(addmidList.toString().substring(1).length()>1){
				// メンバーをリストから削除（イベント編集のときに使うチェック）
				// イベント参加者一覧データ取得
				results = communityService.selectEventMemList2(communityDto.cid,entbbsForm.bbsid, entbbsForm.offset, appDefDto.FP_COM_EVENTLIST_MEMMAX);
				if(results.size()>0){
					//取得したメンバーＩＤを配列にする
					ArrayList<String> addMemList=new ArrayList<String>();
					for(int i=0;i<results.size();i++){
						addMemList.add(results.get(i).get("mid").toString());
					}

					//20090707 参加済みのメンバーＩＤも素通りさせる
					////参加済みのメンバーを追加メンバーから削除
					////for(int i=0;i<addMemList.size();i++){
					////	addmidList.remove(addMemList.get(i));
					////}
				}
			} else {
				//すでに登録しているメンバーの「外す」だけの場合はリストをクリーン
				addmidList=new ArrayList<String>();
				allValFlg = "1";
			}
		} else {
		}

		// イベントの編集
		communityService.editEvent(
			communityDto.cid,
			entbbsForm.bbsid,
			entrytype,
			0,
			entbbsForm.title,
			entbbsForm.comment,
			entbbsForm.picpath1,
			entbbsForm.picpath2,
			entbbsForm.picpath3,
			entbbsForm.picnote1,
			entbbsForm.picnote2,
			entbbsForm.picnote3,
			entbbsForm.yearofevent,
			entbbsForm.monthofevent,
			entbbsForm.dayofevent,
			entbbsForm.eventnote,
			entbbsForm.locationnote,
			entbbsForm.deadlineyear,
			entbbsForm.deadlinemonth,
			entbbsForm.deadlineday,
			addmidList,
			allValFlg
		);
		return "/pc/com/event/"+communityDto.cid;
	}

	// ■画像の「削除」ボタン押下時(画像1）
	@Execute(validator=false)
	public String delpic1(){
		delpic("1");
		// 編集表示処理へ
		return "index.jsp";
		//return "edit.jsp";
	}

	// ■画像の「削除」ボタン押下時(画像2）
	@Execute(validator=false)
	public String delpic2(){
		delpic("2");
		// 編集表示処理へ
		return "index.jsp";
		//return "edit.jsp";
	}

	// ■画像の「削除」ボタン押下時(画像3）
	@Execute(validator=false)
	public String delpic3(){
		delpic("3");
		// 編集表示処理へ
		return "index.jsp";
		//return "edit.jsp";
	}

	// ■■■■■■　　　　削除処理　　　　■■■■■■
	// ■削除画面・初期表示
	@Execute(validator=false,urlPattern="edit/{cid}/{bbsid}")
	public String delevent(){
		
		// init処理
		//init();
		// 削除確認用データの取得
		//setEventDt();
		// 削除画面へ
		return "del.jsp";
	}

	// ■確認画面で「やめる」ボタン押下時
	@Execute(validator=false)
	public String cancel(){
		// init処理
		init();
		// 編集用データの取得
		setEventDt();
		// 編集画面へ
		return "index.jsp";
		//return "edit.jsp";
	}

	// ■確認画面で「削除する」ボタン押下時
	@Execute(validator=false)
	public String delete(){
		// init処理
		//init();
		// 掲示板削除処理
		communityService.delbbs(communityDto.cid,Integer.valueOf(entbbsForm.bbsid));
		// 一覧表示処理へ
		return "/pc/com/event/"+communityDto.cid+"?redirect=true";
	}

	// 画像削除処理
	private void delpic(String picno){
		// init処理
		init();
		// 画像削除処理
		communityService.delpic(
			communityDto.cid,
			entbbsForm.bbsid,
			entrytype,
			picno
		);
		// コミュニティ情報のセット
		setEventDt();
	}

	// イベント情報の取得＆セット
	private void setEventDt(){
		// 編集用、イベント情報の取得
		// ■イベント情報の取得(mapオブジェクト)
		result = communityService.getEventOyaView(
			communityDto.cid,
			entrytype,
			entbbsForm.bbsid
		);
		if(result.size()==1){
			entbbsForm.title         = result.get(0).get("title").toString();
			entbbsForm.comment       = result.get(0).get("comment").toString();
			entbbsForm.strpicpath1   = result.get(0).get("pic1") != null?result.get(0).get("pic1").toString():"";
			entbbsForm.strpicpath2   = result.get(0).get("pic2") != null?result.get(0).get("pic2").toString():"";
			entbbsForm.strpicpath3   = result.get(0).get("pic3") != null?result.get(0).get("pic3").toString():"";
			entbbsForm.picnote1      = result.get(0).get("picnote1").toString();
			entbbsForm.picnote2      = result.get(0).get("picnote2").toString();
			entbbsForm.picnote3      = result.get(0).get("picnote3").toString();
			entbbsForm.yearofevent   = result.get(0).get("eventYear").toString();
			entbbsForm.monthofevent  = result.get(0).get("eventMonth").toString();
			if(entbbsForm.monthofevent != null && !entbbsForm.monthofevent.equals("")){
				entbbsForm.monthofevent = CmnUtility.stringFormat("##", Integer.parseInt(entbbsForm.monthofevent));			
			}
			entbbsForm.dayofevent    = result.get(0).get("eventDay").toString();
			if(entbbsForm.dayofevent != null && !entbbsForm.dayofevent.equals("")){
				entbbsForm.dayofevent = CmnUtility.stringFormat("##", Integer.parseInt(entbbsForm.dayofevent));			
			}
			entbbsForm.eventnote     = result.get(0).get("eventNote").toString();
			entbbsForm.locationnote  = result.get(0).get("eventareaNote").toString();
			entbbsForm.deadlineyear  = result.get(0).get("limitYear").toString();
			entbbsForm.deadlinemonth = result.get(0).get("limitMonth").toString();
			if(entbbsForm.deadlinemonth != null && !entbbsForm.deadlinemonth.equals("")){
				entbbsForm.deadlinemonth = CmnUtility.stringFormat("##", Integer.parseInt(entbbsForm.deadlinemonth));			
			}
			entbbsForm.deadlineday   = result.get(0).get("limitDay").toString();
			if(entbbsForm.deadlineday != null && !entbbsForm.deadlineday.equals("")){
				entbbsForm.deadlineday = CmnUtility.stringFormat("##", Integer.parseInt(entbbsForm.deadlineday));			
			}
			entbbsForm.eventmaker    = result.get(0).get("eventmaker").toString();
		}
	}

	// エラー時の遷移先(入力、編集用)
	@Execute(validator=false)
	public String errorNew(){
		// init処理
		init();
		// 自画面に戻る
		return "index.jsp";
	}

	// エラーチェック
	public ActionMessages chkPic(){
		ActionMessages errors = new ActionMessages();
		FormFile picpath1 = entbbsForm.picpath1;
		FormFile picpath2 = entbbsForm.picpath2;
		FormFile picpath3 = entbbsForm.picpath3;
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
		
		//**** 日付チェック ********
		//開催日時・必須
		if(entbbsForm.yearofevent.equals("") || entbbsForm.monthofevent.equals("") || entbbsForm.dayofevent.equals("")){
			//開催日時・完全な入力じゃない場合
			if(!entbbsForm.yearofevent.equals("") || !entbbsForm.monthofevent.equals("") || !entbbsForm.dayofevent.equals("")){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.invalid","開催日時"));
			}
		} else {
			//開催日時・日付型
			String checkDate;
			checkDate = entbbsForm.yearofevent;
			checkDate = checkDate.concat(stringFormat("00",Integer.parseInt(entbbsForm.monthofevent)));
			checkDate = checkDate.concat(stringFormat("00",Integer.parseInt(entbbsForm.dayofevent)));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			Date date = null;
			try {
				date = sdf.parse(checkDate);
				//return errors;
			}catch (Exception e){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.invalid","開催日時"));
			}
		}

		if(entbbsForm.deadlineyear.equals("") || entbbsForm.deadlinemonth.equals("") || entbbsForm.deadlineday.equals("")){
			//募集期限・完全な入力じゃない場合
			if(!entbbsForm.deadlineyear.equals("") || !entbbsForm.deadlinemonth.equals("") || !entbbsForm.deadlineday.equals("")){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.invalid","募集期限"));
			}
		} else {
		//募集期限・日付型
			String checkDate2;
			checkDate2 = entbbsForm.deadlineyear;
			checkDate2 = checkDate2.concat(stringFormat("00",Integer.parseInt(entbbsForm.deadlinemonth)));
			checkDate2 = checkDate2.concat(stringFormat("00",Integer.parseInt(entbbsForm.deadlineday)));

			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			sdf2.setLenient(false);
			Date date2 = null;

			try {
				date2 = sdf2.parse(checkDate2);
				//return errors;
			}catch (Exception e){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.invalid","募集期限"));
			}
		}

		return errors;
	}

	//フォーマット設定
	private String stringFormat(String format,int val){
	    DecimalFormat df = new DecimalFormat(format);

		return df.format(val);
	}

	// ■■■■■■■■■■■■■参加メンバー 追加処理 ■■■■■■■■■■■■■■■■■■■■
	// ■初期表示（新規）
	@Execute(validator=false,urlPattern="add/{cid}")
	public String add(){

		// init処理
		init();
		// 初期値を設定
		entbbsForm.offset = 0;
		entbbsForm.pgcnt = 0;

		// 一覧表示処理へ
		return "addlist";
	}

	// ■ページング処理
	@Execute(validator=false,urlPattern="movelist/{cid}/{pgcnt}")
	public String movelist(){

		// init処理
		init();
		// 一覧表示処理へ
		return "addlist";
	}

	// 一覧表示処理
	@Execute(validator=false)
	public String addlist(){
		// 件数取得
		entbbsForm.resultscnt = communityService.cntAddMemList(communityDto.cid);
		// 一覧データ取得
		results = communityService.selectAddMemList(communityDto.cid, entbbsForm.offset, appDefDto.FP_COM_LIST_MEMMAX);
		// イベントへの参加情報取得

		// 参加メンバーリスト表示
		return "add.jsp";
	}

	// 初期化処理
	private void reset(){
		entbbsForm.title         = "";
		entbbsForm.comment       = "";
		entbbsForm.picnote1      = "";
		entbbsForm.picnote2      = "";
		entbbsForm.picnote3      = "";
		entbbsForm.strpicpath1   = "";
		entbbsForm.strpicpath2   = "";
		entbbsForm.strpicpath3   = "";
		entbbsForm.yearofevent   = "";
		entbbsForm.monthofevent  = "";
		entbbsForm.dayofevent    = "";
		entbbsForm.eventnote     = "";
		entbbsForm.locationnote  = "";
		entbbsForm.deadlineyear  = "";
		entbbsForm.deadlinemonth = "";
		entbbsForm.deadlineday   = "";
		entbbsForm.selmid        = "";
		entbbsForm.oyacid        = "";
		entbbsForm.oyabbsid      = "";
		entbbsForm.offset        = 0;
		entbbsForm.pgcnt         = 0;
		entbbsForm.edittype      = "";
		entbbsForm.eventmaker    = "";
		addList                  = null;
	}

	// init処理
	private void init(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = entbbsForm.cid;
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
		// ■件数取得
		entbbsForm.resultscntm = communityService.cntMemList(communityDto.cid);     // 参加メンバー

		try {
			// offset設定
			entbbsForm.offset = entbbsForm.pgcnt*appDefDto.FP_COM_LIST_MEMMAX;
		} catch (Exception e) {
			// エラーになった場合は初期値を設定
			entbbsForm.offset = 0;
			entbbsForm.pgcnt = 0;
			// TODO: handle exception
		}

		//新規、編集の文言設定
		if (entbbsForm.edittype != null){
			if (entbbsForm.edittype.equals("0")){
				vMode = "作成";
			}else if(entbbsForm.edittype.equals("1")){
				vMode = "編集";				
			}
		}
		
	}
}