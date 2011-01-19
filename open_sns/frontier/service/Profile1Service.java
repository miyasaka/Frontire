package frontier.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frontier.common.CmnUtility;
import frontier.entity.Profile1;
import frontier.form.pc.Profile1Form;


public class Profile1Service {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	public List<BeanMap> results;
	public List<BeanMap> results2;

	public long count;

	@ActionForm
	@Resource
	protected Profile1Form profile1Form;
	
	// 共通項目の取得
	public List<BeanMap> selectItems(String itemid){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstComItems",itemid)
		.getResultList();
		return results;
	}	
	
	// メールアドレス使用数取得
	public Integer cntEmail(String email){
		Profile1 d = new Profile1();
		d.email = email;
		
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selEmail.sql",d)
		.getResultList();
		//logger.debug("********* results.size() ***********"+results.size());
		return results.size();
	}
	
	// メールアドレス・ＰＣ重複取得
	public List<BeanMap> chkEmail(String email){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("email",email);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selEmail.sql",params)
		.getResultList();
		return results;		
	}

	// 携帯メールアドレス使用数取得
	public Integer cntMobileEmail(String memail){
		Profile1 d = new Profile1();
		d.mobileemail = memail;
		
		results2 = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMobileEmail.sql",d)
		.getResultList();
		//logger.debug("********* results.size() ***********"+results.size());
		return results2.size();
	}
	
	// 携帯メールアドレス重複取得
	public List<BeanMap> chkMobileEmail(String memail){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mobileemail",memail);
		results2 = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMobileEmail.sql",params)
		.getResultList();
		return results2;		
	}
	
	
	//基本設定更新処理
	public void updprofile1(String mid,String email,String memail,String passwd,String diaryLevel,String fshoutLevel,String fshoutFrom,String updatefrequency) {
		//パスワード暗号化
	   	String p = null;
    	if(profile1Form.passwd == ""){
        		p = profile1Form.dbpass;
    	} else {
    	   	try{
        		p = CmnUtility.getDigest(profile1Form.passwd);
        	}catch(Exception e){}
    	}
    	profile1Form.encpass = p;
    	
    	/*2010/2/26
		//twitterパスワード暗号化
	   	String tp = null;
    	if(profile1Form.twitterpassword == ""){
    		tp = profile1Form.dbtwitterpass;
    	} else {
		   	try{
	    		//tp = CmnUtility.getDigest(profile1Form.twitterpassword);
	    		tp = CmnUtility.angou("setup",profile1Form.twitterpassword);
	    	}catch(Exception e){}
	    }
	    */
    	
    	/*2010/2/26
    	logger.debug("********** サービス側 *************");
    	logger.debug("********** サービス側twitteraccount *************"+twitteraccount);
    	//FShoutユーザー名が入っていない場合はtwitterパスワードをNULL
		if(twitteraccount==""){
			logger.debug("********** サービス側パスワードをNULLにするよー *************");
			tp = "";
		}
    	logger.debug("********** サービス側tp *************"+tp);
		*/
    	
    	/*2010/2/26
    	//チェックボックス制御
    	String tflg = null;
    	tflg = twitterflg;
    	if(twitterflg==""||twitterflg==null||twitterflg.equals("")){
    		tflg = "0";
    	}
    	*/
    	
		//基本設定の更新
		Profile1 profileitem = new Profile1();	
		profileitem.mid             = mid;
		profileitem.email           = email;
		profileitem.mobileemail     = memail;
		profileitem.password        = p;
		//profileitem.twitteraccount  = twitteraccount;
		profileitem.fshoutFrom      = fshoutFrom;
		//更新頻度は秒単位にして登録する
		profileitem.updatefrequency = Integer.parseInt(updatefrequency);
		
		/*2010/2/26
		profileitem.twitterpassword = tp;
		profileitem.twitterflg      = tflg;
		*/
		
		jdbcManager.updateBySqlFile("/data/updProfile1Members", profileitem)
		.execute();
		
		//基本設定の更新２
		Profile1 profileitem2 = new Profile1();	
		profileitem2.mid         = mid;
		profileitem2.diaryLevel  = diaryLevel;
		profileitem2.fshoutLevel = fshoutLevel;
		
		jdbcManager.updateBySqlFile("/data/updProfile1MemberItemInfo", profileitem2)
		.execute();

	}
	
	// メンバーデータ取得
	public List<BeanMap> selectprofile(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selprofile.sql",params)
		.getResultList();
		return results;		
	}

	
	
}
