package frontier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;


public class FriendListService {
	public List<BeanMap> results;
	@Resource
	protected JdbcManager jdbcManager;
	public long count;

	public List<BeanMap> selectFriendList(String mid){
		Logger logger = Logger.getLogger(this.getClass().getName());
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		logger.debug("★★☆☆"+mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selFriendList.sql",params)
		.getResultList();
		return results;
	}

	
	public List<BeanMap> selectFollowerIdList(String mid,Integer diaryid,String fid){
		Logger logger = Logger.getLogger(this.getClass().getName());
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("diaryid",diaryid);
		params.put("fid",fid);
		logger.debug("★★☆☆"+mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selFollowerIdList.sql",params)
		.getResultList();
		return results;
	}
	
	public BeanMap selectFriendInfo(String mid){
		Logger logger = Logger.getLogger(this.getClass().getName());
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		logger.debug("★★☆☆"+mid);
		BeanMap results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selFriendInfo.sql",params)
		.getSingleResult();
		return results;
	}
	
	public BeanMap selectMyFriend(String cid,List<Object> flist){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("idlist",flist);
		BeanMap results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMyFriendInfo.sql",params)
		.getSingleResult();
		return results;
	}
	
	public List<BeanMap> selectHobby(ArrayList<String> cd){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("itemcd",cd);
		params.put("itemid","interest");
		List<BeanMap> results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComItemList.sql",params)
		.getResultList();
		return results;
	}
	
	public long getFriendRequestCount(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendRequestCount",mid).getResultList().size();
	}
	//コミュニティ参加リクエスト情報取得
	public long getCommunityRequestCount(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selCommunityRequestCount",mid).getResultList().size();
	}

	
}
