package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

public class ClistService {
	@Resource
	protected JdbcManager jdbcManager;
	public List<BeanMap> results;
	@Resource
	protected EntprofileService entprofileService;
	
	// コミュニティ一覧取得
	public List<BeanMap> selectClist(String listcid,String seltype,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listcid",listcid);
		params.put("seltype",seltype);
		params.put("limit",limit);
		params.put("offset",offset);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selClist.sql",params)
		.getResultList();
		return results;
	}

	// コミュニティ件数取得
	public Integer cntClist(String listcid,String seltype){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listcid",listcid);
		params.put("seltype",seltype);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selClist.sql",params)
		.getResultList();
		return results.size();
	}
	
	// 管理コミュニティ取得
	public List<BeanMap> seladmClist(String listcid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("admmid",listcid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/seladmClist.sql",params)
		.getResultList();
		return results;
	}
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	// 管理コミュニティ削除
	public void deladmClist(List<BeanMap> clist){
		int memcnt = 0;
		for(BeanMap b:clist){
			memcnt = Integer.parseInt(b.get("memcnt").toString());
			//管理人のみ
			if(memcnt == 1){
				//コミュニティ削除
				entprofileService.delComAll(b.get("cid").toString());
			}
		}
	}
	
	
	
}