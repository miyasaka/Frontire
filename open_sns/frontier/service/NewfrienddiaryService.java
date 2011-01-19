package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;

public class NewfrienddiaryService {
	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	protected AppDefDto appDefDto;
	
	//フォトアルバム一覧検索
	public List<BeanMap> selDiaryList(String mid){	
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("startDay", CmnUtility.calcCalendarDay(appDefDto.FP_MY_NEWDIARY_LIST_PGMAX));
		params.put("endDay", CmnUtility.getToday("yyyyMMdd"));
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFriendNewDiaryMore",params)
					.getResultList();
		
	}
	
	//メンバー日記一覧検索
	public List<BeanMap> selDiaryNewList(String mid,Object sortcd,List<String> idlist,Integer offset){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("sortcd",sortcd);
		params.put("idlist", idlist);
		params.put("offset", offset);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendNewDiaryList.sql",params)
			.limit(appDefDto.FP_MY_NEWDIARY_LIST_PGMAX)
			.getResultList();
	}
	
	//メンバー日記一覧全件取得
	public long cntDiaryNewList(String mid,List<String> idlist){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("idlist", idlist);
		return jdbcManager.getCountBySqlFile("/data/selFriendNewDiaryList.sql",params);
	}
}
