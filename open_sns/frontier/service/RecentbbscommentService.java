package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;

public class RecentbbscommentService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	public JdbcManager jdbcManager;
	public AppDefDto appDefDto;
	
	public List<BeanMap> selRecebtBbsCommentList(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("day", CmnUtility.calcCalendarDay(-appDefDto.FP_MY_NEWCOMMUNITY_COMMENT_LIST_PGMAX));

		logger.debug(params.get("day"));
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selRecentBbsCommentList.sql",params)
			.getResultList();
	}
	
	//トピックまたはイベントを取得
	public List<BeanMap> selTopicNewList(String mid,String sortcd,Integer offset){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("sortcd",sortcd);
		params.put("offset", offset);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewTopicData.sql",params)
			.limit(appDefDto.FP_MY_NEWCOMMUNITY_COMMENT_LIST_PGMAX)
			.getResultList();
	}
	
	//トピックまたはイベントの全件取得
	public long cntTopicNewList(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		return jdbcManager.getCountBySqlFile("/data/selNewTopicData.sql",params);
	}

}
