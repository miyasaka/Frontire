package frontier.action.pc;

import java.util.List;

import javax.annotation.Resource;

import net.oauth.client.OAuthResponseMessage;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.Profile1Form;
import frontier.service.OauthConsumerService;
import frontier.service.Profile1Service;
import frontier.service.TwitterService;

public class Profile1Action {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	public List<BeanMap> results;
	public List<BeanMap> results2;
	public List<BeanMap> mdomainlist;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected Profile1Form profile1Form;
	@Resource
	protected Profile1Service profile1Service;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	@Resource
	protected TwitterService twitterService;
	
    public ActionMessages errors = new ActionMessages();

    public String mid;
    public String email;
    public String mobileemail;
    public String mobileDomain;
    public String memail;
    public String passwd;
    public String repasswd;
    public String dbpass;
    public String twitterpassword;
    public String dbtwitterpass;
    public String inpass;
    public String diaryLevel;
    
    public String getmid;
    public String getmid2;
    
    //OAuth認証関連
	public boolean isAuthed = false;
	//Twitter利用状況を表すフラグ
	public String twitterFlg;
    
	// ■初期表示
	@Execute(validator=false)
	public String index(){
		//初期化
		email = "";
		mobileemail= "";
		mobileDomain = "";
		memail = "";
		passwd = "";
		repasswd = "";
		dbpass = "";
		twitterpassword = "";
		dbtwitterpass = "";

		//twitter設定確認
		chkUsedTwitter();
		
		// init処理
		init();
		
		// 初期表示時のみＤＢから参照
		email = results.get(0).get("email").toString();
		diaryLevel = results.get(0).get("pubDiary").toString();
		mobileemail = results.get(0).get("memailfront").toString();
		mobileDomain = results.get(0).get("memaildomain").toString();
		profile1Form.dbpass = results.get(0).get("password").toString();
		String pubFshout = (String)results.get(0).get("pubFshout");
		if(pubFshout==null || pubFshout.equals("")){
			//F Shout公開範囲がnullの時は外部公開とみなす
			pubFshout = "0";
		}
		profile1Form.fshoutLevel = pubFshout;
		profile1Form.fshoutFrom = results.get(0).get("target").toString();
		Integer updateFrequency = (Integer)results.get(0).get("updatefrequency");
		if(updateFrequency==null){
			//F Shout更新頻度がnullの時は未設定とみなす
			updateFrequency = 0;
		}
		profile1Form.updateFrequency = updateFrequency.toString();
				
		logger.debug("************ 初期表示 *************");
		
		
		
		return "index.jsp";
	}
    
    /**
     * Auth処理
     * @return
     */
    @Execute(validator=false)
	public String oauth(){
		OAuthResponseMessage result = oauthConsumerService.oauthExecute(appDefDto.TWI_PROVIDER);

        if(result!=null){
        	try{
        		isAuthed = true;
        		return "index.jsp";
        	}catch(Exception e){
        		e.printStackTrace();
        		return "/common/error.jsp";
        	}
        }
		return null;
	}
	
    /**
     * CallBack
     * @return
     */
    @Execute(validator=false)
	public String callBack(){
	
		oauthConsumerService.callBack(twitterService);
		return null;
	}
	
	// init処理
	private void init(){
		//メニューの出しわけ
		userInfoDto.visitMemberId = userInfoDto.memberId;
		
		//マイ情報を取得
		results = profile1Service.selectprofile(userInfoDto.memberId);
		
		// モバイルアドレスドメインのリストを取得
		mdomainlist = profile1Service.selectItems("mdomain");
	}
 
	//入力内容を登録するを押下
	@Execute(validate="checkemail",input="error")
	public String touroku() {
		return updprofile1();
	}
	
	public ActionMessages checkemail(){
		exeEmail();
		exeMemail();
  		return errors;
	}
	
	//新規登録処理
	public String updprofile1(){
		if(profile1Form.passwd==""){
			profile1Form.inpass = profile1Form.dbpass;
		}else{
			profile1Form.inpass = profile1Form.passwd;
		}
				
		//DB更新
		profile1Service.updprofile1(userInfoDto.memberId,profile1Form.email,profile1Form.memail,profile1Form.inpass,profile1Form.diaryLevel,profile1Form.fshoutLevel,profile1Form.fshoutFrom,profile1Form.updateFrequency);
				
		//フォーム初期化
		profile1Form.email = "";
		profile1Form.mobileemail = "";
		profile1Form.mobileDomain = "";
		profile1Form.memail = "";
		profile1Form.diaryLevel = "";
		profile1Form.fshoutLevel = "";
		profile1Form.fshoutFrom = "";
		profile1Form.updateFrequency = "";
		
		return goTop();
	}

	//基本設定処理後、設定変更へ遷移
	public String goTop(){
		
		return "/pc/setup";
	}
	//メールアドレス・ＰＣ重複チェック
	public void exeEmail(){
		
		//入力されたメールアドレスでデータ数チェック
		profile1Form.resultscnt = profile1Service.cntEmail(profile1Form.email);
		
		if(profile1Form.resultscnt > 0){
			results = profile1Service.chkEmail(profile1Form.email);
			getmid = results.get(0).get("mid").toString();
			if(getmid.equals(userInfoDto.memberId)){
				//logger.debug("********* 自分のメールアドレスだよ ***********");
			} else {
				//logger.debug("********* 他の人のメールアドレスだから使えないよ ***********");
	      		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	    		"errors.entry.email"));
			}
		}
	}
	
	//携帯メールアドレスチェック
	public void exeMemail(){
		profile1Form.memail ="";
		if(profile1Form.mobileemail != "" ){
			//logger.debug("********* 左側は入っている ***********");
			if(profile1Form.mobileDomain.equals("0")){
				//logger.debug("********* ドメイン選択されてないよ ***********");
	      		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	    		"errors.profile.mobiledomain"));
			} else {
				//logger.debug("********* ドメイン選択されている ***********");
				profile1Form.memail = profile1Form.mobileemail + "@" + profile1Form.mobileDomain;
				
				//入力されたメールアドレスでデータ数チェック
				profile1Form.resultscnt2 = profile1Service.cntMobileEmail(profile1Form.memail);
				
				if(profile1Form.resultscnt2 > 0){
					//logger.debug("********* この携帯メールアドレスを使っている人がいるよ***********");
					results2 = profile1Service.chkMobileEmail(profile1Form.memail);
					getmid2 = results2.get(0).get("mid").toString();
					if(getmid2.equals(userInfoDto.memberId)){
						//logger.debug("********* 自分の携帯メールアドレスだよ ***********");
					} else {
						//logger.debug("********* 他の人の携帯メールアドレスだから使えないよ ***********");
			      		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			    		"errors.profile.mobileemail"));
					}
				}
			}
		} else {
			if(profile1Form.mobileDomain.equals("0")){

			}else{
				//logger.debug("********* ドメインだけ入力されてるよ***********");
	      		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	    		"errors.profile.mobiletext"));
			}
		}
	}
	
	// エラー時の遷移先
	@Execute(validator=false)
	public String error(){
		// init処理
		//init();
		// 自画面に戻る
		//twitter設定確認
		chkUsedTwitter();
		// モバイルアドレスドメインのリストを取得
		mdomainlist = profile1Service.selectItems("mdomain");
		return "index.jsp";
	}

	/**
	 * 有効なTwitterアカウントが存在すればフラグを立てる
	 * F Shout投稿先のプルダウン項目の制御に使用する
	 */
	private void chkUsedTwitter(){
		//有効なTwitterアカウントを取得する
		List<BeanMap> authLbm = oauthConsumerService.getTokens(userInfoDto.memberId, appDefDto.TWI_PROVIDER, null, null);

		if(authLbm.size()!=0){
			twitterFlg = "2";
		}
	}
}
