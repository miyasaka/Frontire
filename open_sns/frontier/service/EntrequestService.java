package frontier.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.entity.Friendinfo;
import frontier.entity.Friendrelation;



public class EntrequestService {
    Logger logger = Logger.getLogger(this.getClass().getName());
    
	@Resource
	protected JdbcManager jdbcManager;
	
	//変数定義
	String friendno = null;
	
	public BeanMap selectMemberPhoto(String cid){
		BeanMap results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMemberPhoto.sql",cid)
		.getSingleResult();
		return results;
	}
	
	public void insFriendinfo(String uid,String comment){
		//エンティティ設定
		Friendinfo fi = new Friendinfo();
		//FRIENDNO取得
		friendno = jdbcManager.selectBySqlFile(Friendinfo.class,"/data/selMaxFriendNo.sql").getSingleResult().friendno;
		//データ登録
		
		logger.debug(friendno);
		fi.friendno=friendno;	
		fi.friendstatus="00";
		fi.reqcomment=comment;
		fi.entdate=new Timestamp ((new java.util.Date()).getTime());
		fi.upddate=new Timestamp ((new java.util.Date()).getTime());
		fi.updid=uid;
		jdbcManager.insert(fi).execute();
	}
	
	public void insFriendRelation(String uid,String cid){
		//エンティティ設定
		Friendrelation fr = new Friendrelation();
		//FRIENDNO取得
		friendno = jdbcManager.selectBySqlFile(Friendrelation.class,"/data/selMaxFriendNo.sql").getSingleResult().friendno;
		//データ登録
		fr.mid = uid;//送信側
		fr.requestid="0";
		fr.friendno=friendno;	
		jdbcManager.insert(fr).execute();
		fr.mid = cid;//受信側
		fr.requestid="1";
		jdbcManager.insert(fr).execute();
		
	}
	
	public BeanMap selFriendStatus(String mid,String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("cid",cid);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendStatus.sql",params).getSingleResult();
	}
}
