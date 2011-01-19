package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.dto.AppDefDto;

public class HistoryService {
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;
	
	//足跡一覧検索
	public List<BeanMap> selVisitorsList(String mid,int offset){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("limit", appDefDto.FP_MY_HISTORYLIST_PGMAX);
		params.put("offset", offset);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selVisitorsList",params)
					.getResultList();
		
	}
	
	//足跡一覧件数検索
	public long cntVisitorsList(String mid){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		
		return jdbcManager.getCountBySqlFile("/data/selVisitorsList", params);

	}
}
