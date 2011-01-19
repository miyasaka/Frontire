package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;

public class MyupdateService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	public JdbcManager jdbcManager;
	public AppDefDto appDefDto;
	
	public List<BeanMap> selMyUpdatePhotoList(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);

		logger.debug(params.get("day"));
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyUpdatePhotoList.sql",params)
			.limit(appDefDto.FP_MY_UPDATEPHOTO_LIST_PGMAX)
			.getResultList();
	}
	
	public List<BeanMap> selMyUpdateDiaryList(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);

		logger.debug(params.get("day"));
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyUpdateDiaryList.sql",params)
			.limit(appDefDto.FP_MY_UPDATEDIARY_LIST_PGMAX)
			.getResultList();
	}
}
