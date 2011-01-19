package frontier.service;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.entity.CommunityEnterant;
import frontier.entity.Profile2;
import frontier.dto.AppDefDto;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntleaveService {
	public List<BeanMap> results;
	public List<BeanMap> resultsMakeEvent;
	public List<BeanMap> resultsEvent;
	@Resource
	public AppDefDto appDefDto;
	Logger logger = Logger.getLogger(this.getClass().getName());
	public CommunityEnterant ce;
	@Resource
	protected JdbcManager jdbcManager;
	public String cid;
	public Timestamp ts;

	// コミュニティ退会(参加者情報の更新)
	public void updateCommunityEnterant(String cid,String mid,String requiredmsg){		
		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());
		
		/* ■ コミュニティ参加者情報の更新 */
		ce              = new CommunityEnterant();
		ce.cid          = cid;
		ce.mid          = mid;
		ce.joinstatus   = "3";
		ce.requiredmsg  = requiredmsg;
		ce.upddate      = ts;
		ce.updid        = mid;
		
		// コミュニティ参加情報更新
		jdbcManager.updateBySqlFile("/data/updCommunityEnterant", ce).execute();
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
	
	// イベント作成者情報の取得
	public List<BeanMap> selMakeEvent(String mid,String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("cid",cid);
		resultsMakeEvent = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selEventMakeDt.sql",params)
		.getResultList();
		return resultsMakeEvent;		
	}
	
	// イベント参加情報の取得
	public List<BeanMap> selEvent(String mid,String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("cid",cid);
		resultsEvent = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selEventDt.sql",params)
		.getResultList();
		return resultsEvent;		
	}
	
}