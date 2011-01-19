package frontier.service;

import frontier.dto.AppDefDto;
import frontier.entity.Frontiernet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

public class FrontiernetService {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	public AppDefDto appDefDto;

	// FQDNを検索条件に検索
	public List<Frontiernet> getFrontiernetList(String fqdn){
		return jdbcManager.from(Frontiernet.class)
							.where("network = ?",fqdn)
							.getResultList();
	}
	
	// Frontier Net公開の日記検索
	// メンバーIDリストをキーに日記データ取得
	public List<BeanMap> getFDiary(List<Object> midlist){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("midlist",midlist);
		params.put("limit",appDefDto.FP_CMN_RSS_LISTMAX);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFNetDiary.sql",params)
		.getResultList();
		
	}

	//Frontierドメインとmidからユーザ情報を検索
	//openid認証の際に利用する
	public List<BeanMap> getOpenId(String domain,String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", domain);
		params.put("fid", mid);
		
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selOpenId.sql",params)
		.getResultList();		
	}
	
}
