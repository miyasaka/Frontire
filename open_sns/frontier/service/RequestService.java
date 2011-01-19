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
import frontier.entity.CommunityEnterant;

public class RequestService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	
	public List<BeanMap> selFriendAddRequestList(String mid,Integer offset,Integer limit){
		
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendAddRequestList.sql",mid)
		.offset(offset)
		.limit(limit)
		.getResultList();
	}
	
	public void updFriendStatus(String mid,String friendno,String status){
		Friendinfo f = new Friendinfo();
		f.friendno = friendno;
		f.friendstatus = status;
		f.upddate = new Timestamp ((new java.util.Date()).getTime());
		f.updid = mid;
		jdbcManager.update(f).includes("friendstatus","upddate","updid").execute();	
	}

	//コミュニティ参加リクエスト一覧取得
	public List<BeanMap> selComAddRequestList(String mid,Integer offset,Integer limit){
		
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selComAddRequestList.sql",mid)
		.offset(offset)
		.limit(limit)
		.getResultList();
	}
	//コミュニティ参加リクエスト（status １：承認、２：拒否）
	public void updComStatus(String mid,String reqId,String reqCid,String status){
		CommunityEnterant c = new CommunityEnterant();
		c.joinstatus = status;
		c.updid = mid;
		c.mid = reqId;
		c.cid = reqCid;
		
		jdbcManager.updateBySqlFile("/data/updCommunityEnterantSt", c)
		.execute();	

		
	}

}
