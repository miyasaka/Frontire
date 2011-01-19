package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.dto.AppDefDto;

public class FtopService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	public AppDefDto appDefDto;

	/*
	 * Frontier Net公開の日記一覧を取得するSQL
	 */
	public List<BeanMap> getDiaryList(String type,String domain,String gid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("type",type);
		params.put("domain",domain);
		params.put("gid",gid);
		params.put("limit",appDefDto.FP_MY_FNET_DIARY_PGMAX);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFDiaryList.sql",params)
		.getResultList();
	}
}