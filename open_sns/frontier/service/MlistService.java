package frontier.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.entity.Follow;
import frontier.entity.Friends;
import frontier.entity.Members;

public class MlistService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	public List<BeanMap> results;
	
	// メンバー一覧取得
	public List<BeanMap> selectMlist(String listmid,String seltype,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listmid",listmid);
		params.put("seltype",seltype);
		params.put("limit",limit);
		params.put("offset",offset);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMlist.sql",params)
		.getResultList();
		return results;
	}
	
	// フォロー一覧取得
	public List<BeanMap> selFollowList(String mid,String type,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("type",type);
		params.put("mid",mid);
		params.put("limit",limit);
		params.put("offset",offset);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/getMidList.sql",params)
		.getResultList();
		return results;
	}
	
	// グループ一覧取得
	public List<BeanMap> selGroupMemList(String gid,String domain,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("gid",gid);
		params.put("fdomain",domain);
		params.put("limit",limit);
		params.put("offset",offset);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selGroupMemList.sql",params)
		.getResultList();
	}
	
	// グループ一覧件数取得
	public Integer cntGroupMemList(String gid,String domain){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("gid",gid);
		params.put("fdomain",domain);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selGroupMemList.sql",params)
		.getResultList().size();
	}

	// メンバー友達数取得
	public Integer cntMlist(String listmid,String seltype){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listmid",listmid);
		params.put("seltype",seltype);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMlist.sql",params)
		.getResultList();
		return results.size();
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
	
	// メンバーデータ取得
	public List<BeanMap> selectFollow(String followmid,String followermid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("followmid",followmid);
		params.put("followermid",followermid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selFollow.sql",params)
		.getResultList();
		return results;		
	}

	//フォロー状態変更
	public void setFollowState(String status,String followmid,String followermid){
			//フォロー解除
			Follow fitem = new Follow();
			fitem.updid       = followmid;
			fitem.followmid   = followmid;
			fitem.followermid = followermid;

			jdbcManager.updateBySqlFile("/data/delFollow", fitem)
			.execute();
	}
	
	// メンバー解除
	public void updFriendinfo(String fst,String mid,String fno){
		//logger.debug("********* fst ***********"+fst);
		//logger.debug("********* mid ***********"+mid);
		//logger.debug("********* fno ***********"+fno);
		Friends d = new Friends();
		d.fst = fst;
		d.mid = mid;
		d.fno = fno;
		
		//更新実行
		jdbcManager.updateBatchBySqlFile("/data/updFriendinfoFriendstatus", d)
					.execute();
	}

	
}