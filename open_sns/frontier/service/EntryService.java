package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.entity.Entry;
import frontier.form.pc.EntryForm;

public class EntryService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	public List<BeanMap> results;
	public String Newmid;
	public AppDefDto appDefDto;

	public long count;

	@ActionForm
	@Resource
	protected EntryForm entryForm;	
	// メールアドレス使用数取得
	public Integer cntEmail(String email){
		//logger.debug("********* email ***********"+email);
		Entry d = new Entry();
		d.email = email;
		
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selEmail.sql",d)
		.getResultList();
		//logger.debug("********* results.size() ***********"+results.size());
		return results.size();
	}
	
	// メンバーＩＤの発行
	public List<BeanMap> newMid(){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selNextmid.sql")
		.getResultList();
		
		return results;
	}

	
	//メンバー登録処理
	public void insMember(String newmid
			             ,String nickname
			             ,String email
			             ,String passwd
			             ,String getFrontierDomain
			             ,String getGid
			             ,String familyname
			             ,String name
			             ) {
		//パスワード暗号化
	   	String p = null;
    	try{
    		p = CmnUtility.getDigest(entryForm.passwd);
    	}catch(Exception e){}
    	entryForm.encpass = p;
    	//メンバーＩＤ発行
    	String Newmid = "m"+newmid;
    	//logger.debug("********* Newmid ***********"+Newmid);
		//メンバーの登録１
		Entry entry = new Entry();	
		entry.mid        = Newmid;
		entry.nickname   = nickname;
		entry.familyname = familyname;
		entry.name       = name;
		entry.email      = email;
		entry.password   = p;
		
		jdbcManager.updateBySqlFile("/data/insMembers", entry)
		.execute();
		
		//メンバーの登録２
		Entry entry2 = new Entry();	
		entry2.mid    = Newmid;
		
		jdbcManager.updateBySqlFile("/data/insMemberiteminfo", entry2)
		.execute();
		
		//メンバーの登録３
		Entry entry3 = new Entry();	
		entry3.mid    = Newmid;

		jdbcManager.updateBySqlFile("/data/insMembersphotoinfo", entry3)
		.execute();
		
		//メンバーの登録４
		Entry entry4 = new Entry();	
		entry4.mid    = Newmid;

		jdbcManager.updateBySqlFile("/data/insMemberaddinfo", entry4)
		.execute();
		
		//メンバーの登録５
		Entry entry5 = new Entry();	
		entry5.mid    = Newmid;

		jdbcManager.updateBySqlFile("/data/insMemberSetupInfo", entry5)
		.execute();

		//メンバーの登録６（グループ参加者）
		Entry entry6 = new Entry();	
		entry6.mid            = Newmid;
		entry6.frontierdomain = getFrontierDomain;
		entry6.gid            = getGid;
		entry6.manageflg      = 0;
		entry6.entid          = Newmid;

		jdbcManager.updateBySqlFile("/data/insFrontierGroupJoin", entry6)
		.execute();
		
		//グループの参加人数更新（グループ）
		Entry entry7 = new Entry();	
		entry7.frontierdomain = getFrontierDomain;
		entry7.gid            = getGid;
		entry7.updid          = Newmid;

		jdbcManager.updateBySqlFile("/data/updGroupJoinNumber", entry7)
		.execute();
		
		//Frontierユーザー管理
		Entry entry8 = new Entry();
		entry8.frontierdomain = getFrontierDomain;
		entry8.fid = Newmid;
		entry8.mid = Newmid;
		entry8.entid = Newmid;
		entry8.updid = Newmid;
		
		jdbcManager.updateBySqlFile("/data/insFrontierUserManagement.sql",entry8).execute();

	}
	
	// メンバーデータ取得
	public List<BeanMap> selectFriend(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selFriend.sql",params)
		.getResultList();
		return results;		
	}

	//メンバー登録処理（Open ID認証の場合）
	public void insMemberOP(String newmid
			             ,String mid
			             ,String pic
			             ,String nickname
			             ,String email
			             ,String passwd
			             ,String openid
			             ,String domain
			             ) {
		//パスワード暗号化
	   	//String p = null;
    	//try{
    	//	p = CmnUtility.getDigest(entryForm.passwd);
    	//}catch(Exception e){}
    	//entryForm.encpass = p;
    	entryForm.encpass = passwd;
    	//メンバーＩＤ発行
    	//String Newmid = "m"+newmid;
    	//logger.debug("********* Newmid ***********"+Newmid);
    	
		
		//Frontierユーザー管理
		Entry entry0 = new Entry();
		//entry0.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
		entry0.frontierdomain = domain;
		entry0.fid = mid;
		entry0.mid = newmid;
		entry0.nickname = nickname;
		entry0.pic = pic;
		entry0.entid = newmid;
		entry0.updid = newmid;
		
		jdbcManager.updateBySqlFile("/data/insFrontierUserManagement.sql",entry0).execute();
    	
		
		//メンバーの登録１
		Entry entry = new Entry();	
		entry.mid      = newmid;
		entry.nickname = nickname;
		entry.email    = email;
		//entry.password = p;
		entry.password = passwd;
		entry.openid = openid;
		
		
		jdbcManager.updateBySqlFile("/data/insMembersOpen", entry)
		.execute();
		
		//メンバーの登録２
		Entry entry2 = new Entry();	
		entry2.mid    = newmid;
		
		jdbcManager.updateBySqlFile("/data/insMemberiteminfo", entry2)
		.execute();
		
		//メンバーの登録３
		Entry entry3 = new Entry();	
		entry3.mid    = newmid;

		jdbcManager.updateBySqlFile("/data/insMembersphotoinfo", entry3)
		.execute();
		
		//メンバーの登録４
		Entry entry4 = new Entry();	
		entry4.mid    = newmid;

		jdbcManager.updateBySqlFile("/data/insMemberaddinfo", entry4)
		.execute();
		
		//メンバーの登録５
		Entry entry5 = new Entry();	
		entry5.mid    = newmid;

		jdbcManager.updateBySqlFile("/data/insMemberSetupInfo", entry5)
		.execute();

		
	}
	
	//メンバー登録処理（Open ID認証の場合）
	public void updMemberOP(String fid
			             ,String memberId
			             ,String pic
			             ,String nickname
			             ,String domain
			             ) {
  	
		
		//Frontierユーザー管理
		Entry entry = new Entry();
		entry.frontierdomain = domain;
		entry.fid = fid;
		entry.nickname = nickname;  
		entry.pic = pic;
		entry.updid = memberId;
		
		jdbcManager.updateBySqlFile("/data/updFrontierUserManagement.sql",entry).execute();
		
	}
	
	// グループ名で検索
	public Integer cntGroup(String group){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("gname",group);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selGroupName.sql",params)
		.getResultList();
		return results.size();
	}
	
	// グループ一覧取得
	public List<BeanMap> selGroupList(String group){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("gname",group);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selGroupName.sql",params)
			.getResultList();
	}
	
	//OpenIdのみ更新
	public void updOpenID(String mid,String openid){
		//OpenIdのみ更新
		Entry entry = new Entry();	
		entry.mid      = mid;
		entry.openid = openid;		
		
		jdbcManager.updateBySqlFile("/data/updOpenId.sql", entry)
		.execute();		
	}
}
