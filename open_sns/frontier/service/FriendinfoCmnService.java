package frontier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

public class FriendinfoCmnService {

	@Resource
	protected JdbcManager jdbcManager;
	
	//友達を調査する。
	public List<BeanMap> selFriend(String mid){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//SQLファイルを友達の友達検索と同一とするために、敢えてList化する。
		List<String> midList = new ArrayList<String>();
		midList.add(0, mid);
		
		//パラメータ設定
		params.put("mid", midList);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFriendSearch",params)
					.getResultList();
			
	}
	
	//友達の友達を調査する。
	public List<BeanMap> selFriend(List<String> mid){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFriendSearch",params)
					.getResultList();
			
	}
	
	
	////以下、グループ概念取り入れによる対応
	//グループかを調査する。
	public List<BeanMap> selGroupMember(String mid){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selGroupMemberSearch",params)
					.getResultList();
			
	}
	
	//フォローしているかを調査する。
	public List<BeanMap> selFollowMember(String mid){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFollowMemberSearch",params)
					.getResultList();
			
	}	
	
	
}
