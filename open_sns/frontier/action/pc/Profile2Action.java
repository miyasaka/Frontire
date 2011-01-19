package frontier.action.pc;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.Profile2Form;
import frontier.service.Profile2Service;

public class Profile2Action {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	public List<BeanMap> results;
	public List<BeanMap> results2;
	public List<BeanMap> ResidenceRegionlist;
	public List<BeanMap> ResidenceCitieslist;
	public List<BeanMap> HometownRegionlist;
	public List<BeanMap> HometownCitieslist;
	public List<BeanMap> FavGenrelist;
	public List<BeanMap> interestlist;
	public List<BeanMap> occupationlist;
	public List<BeanMap> yearlist;
	public BeanMap FriendInfo;
	List<String> hobbyList;
	public Map<String,List<String>> oldCal;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected Profile2Form profile2Form;
	@Resource
	protected Profile2Service profile2Service;
	
    public ActionMessages errors = new ActionMessages();

    public String mid;
    public String familyname;          //姓
    public String name;                //名
    public String nickname;            //ニックネーム
    public String residenceregion;     //現住所・都道府県
    public String residencecity;       //現住所・市区町村
    public String gender;              //性別
    public String yearofbirth;         //誕生日・年
    public String monthofbirth;        //誕生日・月
    public String dayofbirth;          //誕生日・日
    public String dateofbirth;         //誕生日・月日
    public String bloodtype;           //血液型
    public String hometownregion;      //出身地・都道府県
    public String hometowncity;        //出身地・市区町村
    public String interest1;           //趣味１
    public String occupation;          //職業
    public String company;             //所属
    public String aboutme;             //自己紹介
    public String favgenre1;           //好きなジャンル１
    public String favgenre2;           //好きなジャンル２
    public String favgenre3;           //好きなジャンル３
    public String favcontents1;        //好きなジャンル内容１
    public String favcontents2;        //好きなジャンル内容２
    public String favcontents3;        //好きなジャンル内容３
    public String nameLevel;
    public String locationLevel;
    public String birthdayLevel;
    public String ageLevel;
    public String hometownLevel;
    public String jobLevel;
    public String genderLevel;
    public String hobby;
    
    public String pic1;
    public String pic2;
    public String pic3;
    public String mainpicno;
    public String vflg;
    //画面表示用
    public String vNickname;
    public boolean vUser;
    //今年の年
    public String toyear = CmnUtility.getToday("yyyy");
    
    //表示対象の年数
    @Resource
    public final int YEAR_COUNT = 1;
    
	// ■初期表示
	@Execute(validator=false)
	public String index(){
		// 初期化
		reset();
		
		// init処理
		init();
		
		// 初期表示時のみＤＢから参照
		familyname = results.get(0).get("familyname").toString();
		name = results.get(0).get("name").toString();
		nickname = results.get(0).get("nickname").toString();
		//residenceregionCD = results.get(0).get("residenceRegion").toString();
		//residencecityCD = results.get(0).get("residenceCity").toString();
		//hometownregionCD = results.get(0).get("hometownRegion").toString();
		//hometowncityCD = results.get(0).get("hometownCity").toString();
		interest1 = results.get(0).get("interest1").toString();
		occupation = results.get(0).get("occupation").toString();
		company = results.get(0).get("company").toString();
		bloodtype = results.get(0).get("bloodtype").toString();
		favgenre1 = results.get(0).get("favgenre1").toString();
		favgenre2 = results.get(0).get("favgenre2").toString();
		favgenre3 = results.get(0).get("favgenre3").toString();
		favcontents1 = results.get(0).get("favcontents1").toString();
		favcontents2 = results.get(0).get("favcontents2").toString();
		favcontents3 = results.get(0).get("favcontents3").toString();
		aboutme = results.get(0).get("aboutme").toString();
		nameLevel = results.get(0).get("pubName").toString();
		locationLevel = results.get(0).get("pubResidence").toString();
		birthdayLevel = results.get(0).get("pubDateofbirth").toString();
		ageLevel = results.get(0).get("pubYearofbirth").toString();
		hometownLevel = results.get(0).get("pubHometown").toString();
		jobLevel = results.get(0).get("pubOccupation").toString();
		genderLevel = results.get(0).get("pubGender").toString();
		yearofbirth = results.get(0).get("yearofbirth").toString();
		monthofbirth = results.get(0).get("dateofmonth").toString();
		if(monthofbirth != null && !monthofbirth.equals("")){
			monthofbirth = CmnUtility.stringFormat("##", Integer.parseInt(monthofbirth));			
		}
		dayofbirth = results.get(0).get("dateofday").toString();
		if(dayofbirth != null && !dayofbirth.equals("")){
			dayofbirth = CmnUtility.stringFormat("##", Integer.parseInt(dayofbirth));			
		}
		
		gender = results.get(0).get("gender").toString();
		//residenceregion = results.get(0).get("residenceRegion").toString();
		//hometownregion = results.get(0).get("hometownRegion").toString();

		profile2Form.residenceregion = results.get(0).get("residenceRegion").toString();	
		profile2Form.residencecity = results.get(0).get("residenceCity").toString();
		profile2Form.hometownregion = results.get(0).get("hometownRegion").toString();
		profile2Form.hometowncity = results.get(0).get("hometownCity").toString();
		
		initAdd();
		
		
		
		return "index.jsp";
	}
    
	// init処理
	private void init(){
		//メニューの出しわけ
		userInfoDto.visitMemberId = userInfoDto.memberId;
		
		//開始年と終了年の初期設定
		setYear();
		
		//マイ情報を取得
		results = profile2Service.selectprofile(userInfoDto.memberId);
		logger.debug("********** results ***********"+results);
		
		// 趣味のリストを取得
		interestlist = profile2Service.selectItems("interest");
		
		// 職業のリストを取得
		occupationlist = profile2Service.selectItems("occupation");
		
		// 好きなジャンルのリストを取得
		FavGenrelist = profile2Service.selectItems("favorite");


	}
	
	// 住所リスト処理
	private void initAdd(){
		// 現住所・都道府県のリストを取得
		ResidenceRegionlist = profile2Service.selectResidenceRegionItems();
		
		// 現住所・市区町村のリストを取得
		ResidenceCitieslist = profile2Service.selectResidenceCitiesItems(profile2Form.residenceregion);		
		
		// 出身地・都道府県のリストを取得
		HometownRegionlist = profile2Service.selectHometownRegionItems();
		
		// 出身地・市区町村のリストを取得
		HometownCitieslist = profile2Service.selectHometownCitiesItems(profile2Form.hometownregion);

		////現在住所、出身地の都道府県をセットパラメータとして保持
		profile2Form.setresidenceregion = profile2Form.residenceregion;
		profile2Form.sethometownregion = profile2Form.hometownregion;
		
	}

	
 
	//入力内容を登録するを押下
	@Execute(validate="checkprofile",input="error")
	public String touroku() {
		//logger.debug("********* 登録ボタン押されたよ ***********");
		//月日のセット
		logger.debug("msg"+appDefDto.FP_MY_CALENDAR_START_PGMAX);
		logger.debug("msg"+(Integer.parseInt(profile2Form.yearofbirth)<appDefDto.FP_MY_CALENDAR_START_PGMAX));
		logger.debug("msg"+Integer.parseInt(profile2Form.yearofbirth));
		
		profile2Form.dateofbirth = profile2Form.monthofbirth + profile2Form.dayofbirth;
		
		return updprofile2();
	}
	
	public ActionMessages checkprofile(){
		
		//**** 日付チェック ********
		String checkDate;
		//logger.debug("\nチェック１！！！！:"+profile2Form.dateofbirth);
		checkDate = profile2Form.yearofbirth;
		checkDate = checkDate.concat(stringFormat("00",Integer.parseInt(profile2Form.monthofbirth)));
		//logger.debug("\nチェック２！！！！:"+checkDate);
		checkDate = checkDate.concat(stringFormat("00",Integer.parseInt(profile2Form.dayofbirth)));
		//logger.debug("\nチェック３！！！！:"+checkDate);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sdf.setLenient(false);
		Date date = null;
		
		//生まれた年範囲チェック

		if(appDefDto.FP_MY_CALENDAR_START_PGMAX>Integer.parseInt(profile2Form.yearofbirth)||profile2Form.endYear<Integer.parseInt(profile2Form.yearofbirth)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.invalid","生まれた年"));
		}
		
		// 趣味のリストを取得
		interestlist = profile2Service.selectItems("interest");
		//趣味値チェック
		List<String> s = profile2Form.interest1;
		try{
			for(int i=0;i<s.size();i++){
				if(Integer.parseInt(s.get(i))<1||Integer.parseInt(s.get(i))>interestlist.size()){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.invalid","趣味"));
					break;
				}
			}
		}catch(Exception e){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.invalid","趣味"));
		}
		
		
		try {
			date = sdf.parse(checkDate);
			return errors;
		}catch (Exception e){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.invalid","日付"));
		}
		//**** 日付チェック ********
		
	
  		return errors;
	}
	
	//フォーマット設定
	private String stringFormat(String format,int val){
	    DecimalFormat df = new DecimalFormat(format);

		return df.format(val);
	}

	//日付連結
	private String concatDate(String year,String month,String day){
		String concateDate;

		concateDate = year;
		concateDate = concateDate.concat(stringFormat("00",Integer.parseInt(month)));
		concateDate = concateDate.concat(stringFormat("00",Integer.parseInt(day)));

		return concateDate;
	}
	
	
	
	private String getHobbyList(){
		ArrayList<String> list=new ArrayList<String>();
		String[] st =  profile2Form.interest1.toString().split(",");
		for(int i=0;i<st.length;i++){
			list.add(st[i]);
		}
		String hobby = null;
		//趣味リスト取得
		hobbyList = profile2Form.interest1;
		////文字列を,区切りで結合
		for(int i=0;i<hobbyList.size();i++){
			if(i==0){
				hobby = hobbyList.get(i).toString();
			}else{
				hobby +=","+hobbyList.get(i).toString();
			}
		}
		return hobby;
	}
	
	
	
	
	//新規登録処理
	public String updprofile2(){
		//趣味を取得
		hobby = getHobbyList();		
		
		
		//日付を連結する。
		String concatDate = concatDate(profile2Form.yearofbirth,profile2Form.monthofbirth,profile2Form.dayofbirth);
		//日付を月日にする
		profile2Form.dateofbirth = concatDate.substring(4,8);

		//DB更新
		//logger.debug("********* 更新しまーす ***********");
		//logger.debug("************* profile2Form.nameLevel ******************" + profile2Form.nameLevel);
		//logger.debug("************* profile2Form.locationLevel ******************" + profile2Form.locationLevel);
		//logger.debug("************* profile2Form.birthdayLevel ******************" + profile2Form.birthdayLevel);
		//logger.debug("************* profile2Form.ageLevel ******************" + profile2Form.ageLevel);
		//logger.debug("************* profile2Form.hometownLevel ******************" + profile2Form.hometownLevel);
		//logger.debug("************* profile2Form.jobLevel ******************" + profile2Form.jobLevel);
		//logger.debug("************* profile2Form.jobdetailsLevel ******************" + profile2Form.jobdetailsLevel);
		//logger.debug("************* profile2Form.genderLevel ******************" + profile2Form.genderLevel);
		//logger.debug("************* profile2Form.yearofbirth ******************" + profile2Form.yearofbirth.toString());
		//logger.debug("************* profile2Form.monthofbirth ******************" + profile2Form.monthofbirth.toString());
		//logger.debug("************* profile2Form.dayofbirth ******************" + profile2Form.dayofbirth.toString());
		//logger.debug("************* profile2Form.dateofbirth ******************" + profile2Form.dateofbirth.toString());
		//logger.debug("************* concatDate ******************" + concatDate.substring(4,8));
		
		profile2Service.updprofile2(userInfoDto.memberId,profile2Form.familyname,profile2Form.name,profile2Form.nickname,profile2Form.residenceregion,profile2Form.residencecity,profile2Form.gender,profile2Form.yearofbirth,profile2Form.dateofbirth,profile2Form.bloodtype,profile2Form.hometownregion,profile2Form.hometowncity,hobby,profile2Form.occupation,profile2Form.company,profile2Form.aboutme,profile2Form.favgenre1,profile2Form.favgenre2,profile2Form.favgenre3,profile2Form.favcontents1,profile2Form.favcontents2,profile2Form.favcontents3,profile2Form.nameLevel,profile2Form.locationLevel,profile2Form.birthdayLevel,profile2Form.ageLevel,profile2Form.hometownLevel,profile2Form.jobLevel,profile2Form.genderLevel);

		
		return goTop();
	}

	//基本設定処理後、設定変更へ遷移
	public String goTop(){
		//セッション書き換え
		userInfoDto.nickName=profile2Form.nickname;
		
		return "/pc/check/"+userInfoDto.memberId;
	}
	
	
	// エラー時の遷移先
	@Execute(validator=false)
	public String error(){
		//logger.debug("************* profile2Form.familyname ******************" + profile2Form.familyname);
		//logger.debug("************* profile2Form.interest1 ******************" + profile2Form.interest1);
		//logger.debug("************* profile2Form.residenceregion ******************" + profile2Form.residenceregion);
		//logger.debug("************* profile2Form.residencecity ******************" + profile2Form.residencecity);
		
		
		// init処理
		init();
		
		// 住所処理
		initAdd();
		
		// 自画面に戻る
		return "index.jsp";
	}

	// リロード時表示
	@Execute(validator=false)
	public String reload(){
		//logger.debug("************* リロード ******************");
		
		//都道府県が変わったら市区町村にnullをセット
		if(!profile2Form.residenceregion.equals(profile2Form.setresidenceregion)){
			//logger.debug("************* 都道府県がちゃうよ ******************");
			profile2Form.residencecity = "";
		}
		if(!profile2Form.hometownregion.equals(profile2Form.sethometownregion)){
			//logger.debug("************* 都道府県がちゃうよ ******************");
			profile2Form.hometowncity = "";
		}
				
		init();
		
		// 住所処理
		initAdd();
		
		return "index.jsp";
		//return "/pc/profile2/redirect=true";
	}
	
	//セッション初期化
	private void reset(){
		familyname        = "";
		name              = "";
		nickname          = "";
		interest1         = "";
		occupation        = "";
		company           = "";
		gender            = "";
		bloodtype         = "";
		favgenre1         = "";
		favgenre2         = "";
		favgenre3         = "";
		favcontents1      = "";
		favcontents2      = "";
		favcontents3      = "";
		aboutme           = "";
		nameLevel         = "";
		locationLevel     = "";
		birthdayLevel     = "";
		ageLevel          = "";
		hometownLevel     = "";
		jobLevel          = "";
		genderLevel       = "";
		yearofbirth       = "";
		monthofbirth      = "";
		dayofbirth        = "";
		//residenceregion   = "";
		//residencecity     = "";
		dateofbirth       = "";
		
		profile2Form.familyname = "";
		profile2Form.name = "";
		profile2Form.nickname = "";
		profile2Form.residenceregion = "";
		profile2Form.gender = "";
		profile2Form.interest1 = null;
		profile2Form.aboutme = "";
		profile2Form.residenceregionCD = "";
		profile2Form.residencecityCD = "";
		profile2Form.residencecity = "";
		profile2Form.yearofbirth = "";
		profile2Form.monthofbirth = "";
		profile2Form.dayofbirth = "";
		profile2Form.bloodtype = "";
		profile2Form.hometownregionCD = "";
		profile2Form.hometownregion = "";
		profile2Form.hometowncityCD = "";
		profile2Form.hometowncity = "";
		profile2Form.occupation = "";
		profile2Form.company = "";
		profile2Form.favgenre1 = "";
		profile2Form.favgenre2 = "";
		profile2Form.favgenre3 = "";
		profile2Form.favcontents1 = "";
		profile2Form.favcontents2 = "";
		profile2Form.favcontents3 = "";
		profile2Form.nameLevel = "";
		profile2Form.locationLevel = "";
		profile2Form.birthdayLevel = "";
		profile2Form.ageLevel = "";
		profile2Form.hometownLevel = "";
		profile2Form.jobLevel = "";
		profile2Form.genderLevel = "";
		profile2Form.dateofbirth = "";
		profile2Form.setresidenceregion = "";
		profile2Form.sethometownregion = "";
	}
	
	//現在日付取得
	private String getToday()  {
		java.util.Date today = null;

		today = new java.util.Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(today);
		                                                    	
	}
	
	//年プルダウン表示制御
	private void setYear(){
		//現在年取得
		String year = getToday().substring(0,4);
		
		//開始年設定
		profile2Form.startYear = Integer.parseInt(year);
		logger.debug("\n開始年！！！！");
		logger.debug(profile2Form.startYear);
		
		//終了年設定
		//Profile2Form.endYear = Integer.parseInt(year) + YEAR_COUNT;
		profile2Form.endYear = Integer.parseInt(year);
		
		return;
	}
	
	// ■マイページ写真の変更
	@Execute(validator=false,urlPattern="edit/{vmid}")
	public String edit(){
		initEdit();
		return "edit.jsp";
	}
	
	private void initEdit(){
		//メニューの出しわけ
		userInfoDto.visitMemberId = profile2Form.vmid;
		//パラメータのIDが自分かメンバーかを判断する
		if (profile2Form.vmid.equals(userInfoDto.memberId)){
			//画面表示用
			vUser = true;
		}else{
			//画面表示用
			vUser = false;
		}
		//マイ情報を取得
		results = profile2Service.selectprofileEdit(userInfoDto.visitMemberId);
		//logger.debug("********** results ***********"+results);
		
		//訪問者フラグセット
		vflg = "";
		if(userInfoDto.memberId.equals(userInfoDto.visitMemberId)){
			vflg = "1";
		} else {
			vNickname = results.get(0).get("nickname").toString();;
		}
		
		pic1 = results.get(0).get("pic1").toString();
		pic2 = results.get(0).get("pic2").toString();
		pic3 = results.get(0).get("pic3").toString();
		mainpicno = results.get(0).get("mainpicno").toString();
		profile2Form.pic1 = results.get(0).get("pic1").toString();
		profile2Form.pic2 = results.get(0).get("pic2").toString();
		profile2Form.pic3 = results.get(0).get("pic3").toString();
	}
	
	// ■メイン写真の変更
	@Execute(validator=false,urlPattern="mainpic/{picno}")
	public String mainpic(){
		//logger.debug("********** メイン画像にするから ***********");
		//メイン写真の変更処理
		profile2Service.updmainpicno(userInfoDto.memberId,profile2Form.picno);
		return "/pc/profile2/edit/" + profile2Form.vmid + "/redirect=true";
	}
	
	// ■写真の削除
	@Execute(validator=false,urlPattern="delpic/{picno}")
	public String delpic(){
		//写真の削除処理
		profile2Service.upddelpic(userInfoDto.memberId,profile2Form.picno);
		return "/pc/profile2/edit/" + profile2Form.vmid + "/redirect=true";
	}
	
	// ■メンバー写真
	@Execute(validator=false,urlPattern="view/{vmid}")
	public String view(){
		initEdit();
		return "edit.jsp";
	}
	
	//画像アップロード
	@Execute(validate="chkpic",input="picerr")
	public String upload() throws IOException, Exception{
		// 画像のアップロード(画像パス名の長さが0以上ならupload)
		//null対策
		if(profile2Form.picpath!=null){
			if(profile2Form.picpath.getFileName().length()>0){
				//logger.debug("********** 画像アップロード処理実行 ***********");
				profile2Service.picupload(userInfoDto.memberId,profile2Form.picpath);
			}
		}
		return "/pc/profile2/edit/" + profile2Form.vmid + "/redirect=true";
	}
	
	public ActionMessages chkpic(){
		//マイ情報を取得
		results = profile2Service.selectprofileEdit(userInfoDto.visitMemberId);
		
		String errflg = "";
		
		String apic1 = results.get(0).get("pic1").toString();
		String apic2 = results.get(0).get("pic2").toString();
		String apic3 = results.get(0).get("pic3").toString();
		if(apic1.equals("")){
			//logger.debug("********** 1の画像がないよ ***********");
		}else{
			if(apic2.equals("")){
				//logger.debug("********** 2の画像がないよ ***********");
			}else{
				if(apic3.equals("")){
					//logger.debug("********** 3の画像がないよ ***********");
				}else{
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.upload.limit"));
					errflg = "1";
				}
			}
		}
		if(errflg.equals("")){
			//logger.debug("********** 画像登録できる状態 ***********");
			FormFile picpath = profile2Form.picpath;
			//logger.debug("********** FormFile picpath ***********"+picpath);
			// 画像パス名の長さが0以上ならチェック
			//null対策
			if(picpath!=null){
				if(picpath.getFileName().length()>0){
					// ファイルサイズ＆タイプチェック
					CmnUtility.checkPhotoFile(errors,picpath,"");
				}
			}
		}
  		return errors;
	}
	
	// エラー時の遷移先
	@Execute(validator=false)
	public String picerr(){
		
		// 自画面に戻る
		return "/pc/profile2/edit/" + profile2Form.vmid;
	}
	
	
}
