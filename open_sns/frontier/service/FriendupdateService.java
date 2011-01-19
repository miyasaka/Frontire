package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;

public class FriendupdateService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	public JdbcManager jdbcManager;
	public AppDefDto appDefDto;
	
	public List<BeanMap> selFriendPhotoList(List<Object> flist){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listmid",flist);
		List<BeanMap> b;
		b = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendPhotoList.sql",params)
		.limit(appDefDto.FP_MY_NEWPHOTO_LIST_PGMAX)
		.getResultList();
		logger.debug("サイズ："+b.size());
		return b;
	}
	
	// フォトの新着データ取得(グループ＋フォロー)
	public List<BeanMap> selFriendUpdateInfo(List<String> glist,List<String> flist,String sortcd,Integer offset){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listgmid",glist);
		params.put("listfmid",flist);
		params.put("sortcd",sortcd);
		params.put("offset", offset);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendUpdateInfo.sql",params)
			.limit(appDefDto.FP_MY_NEWPHOTO_LIST_PGMAX)
			.getResultList();
	}

	// フォトの新着データ総件数取得(グループ＋フォロー)
	public long cntFriendUpdateInfo(List<String> glist,List<String> flist){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listgmid",glist);
		params.put("listfmid",flist);
		
		return jdbcManager.getCountBySqlFile("/data/selFriendUpdateInfo.sql",params);
	}

	
}
