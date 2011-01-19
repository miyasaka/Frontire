package frontier.service;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.entity.CommunityEnterant;
import frontier.dto.AppDefDto;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntjoinService {
	public List<BeanMap> results;
	public List<BeanMap> resultsEnt;
	@Resource
	public AppDefDto appDefDto;
	Logger logger = Logger.getLogger(this.getClass().getName());
	public CommunityEnterant ce;
	@Resource
	protected JdbcManager jdbcManager;
	public String cid;
	public Timestamp ts;

	// コミュニティ参加(参加者情報の登録)
	public void insertCommunities(String cid,String mid, String requiredmsg, String joinstatus){
		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());
		
		/* ■ コミュニティ参加者情報の登録 */
		ce              = new CommunityEnterant();
		ce.cid          = cid;
		ce.mid          = mid;
		ce.joinstatus   = joinstatus;
		ce.requireddate = ts;
		ce.requiredmsg  = requiredmsg;
		ce.entdate      = ts;
		ce.upddate      = ts;
		ce.updid        = mid;
		
		// コミュニティ参加情報登録
		jdbcManager.updateBySqlFile("/data/insCommunityEnterant", ce).execute();
		
	}
	
	// コミュニティ情報の取得
	public List<BeanMap> selCom(String mid,String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("cid",cid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComDt.sql",params)
		.getResultList();
		return results;		
	}
	
	// コミュニティ参加者情報の取得
	public List<BeanMap> selComEnt(String mid,String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("cid",cid);
		resultsEnt = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selCommunityEnterantDt.sql",params)
		.getResultList();
		return resultsEnt;		
	}
	
	// コミュニティ参加(参加者情報の更新)
	public void updateCommunityEnterant(String cid,String mid,String joinstatus,String requiredmsg){		
		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());
		
		/* ■ コミュニティ参加者情報の更新 */
		ce              = new CommunityEnterant();
		ce.cid          = cid;
		ce.mid          = mid;
		ce.joinstatus   = joinstatus;
		ce.requiredmsg  = requiredmsg;
		ce.upddate      = ts;
		ce.updid        = mid;
		
		// コミュニティ参加情報更新
		jdbcManager.updateBySqlFile("/data/updCommunityEnterant", ce).execute();
	}
}