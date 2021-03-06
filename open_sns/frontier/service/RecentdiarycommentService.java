package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;

public class RecentdiarycommentService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	public JdbcManager jdbcManager;
	public AppDefDto appDefDto;
	
	public List<BeanMap> selRecebtDiaryCommentList(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("day", CmnUtility.calcCalendarDay(-appDefDto.FP_MY_NEWDIARY_COMMENT_LIST_PGMAX));

		logger.debug(params.get("day"));
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selRecentDiaryCommentList.sql",params)
			.getResultList();
	}
}
