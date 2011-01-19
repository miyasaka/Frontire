package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.dto.AppDefDto;

public class FmemService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	private AppDefDto appDefDto;
	
	/**
	 * Frontierdomainをキーにしてメンバーを検索
	 * @param type 実行するSQLの判別
	 * @param gid グループID(検索キー)
	 * @param offset 何ページ目から取得するかの設定値
	 * @param limit 取得件数の最大値
	 * @return メンバー検索結果一覧
	 */
	public List<BeanMap> selFmemList(String type,String gid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		params.put("type", type);
		params.put("gid", gid);
		params.put("offset", offset);
		params.put("limit", limit);	
		
		//メンバーを検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFmemList.sql",params)
							.getResultList();
	}
	
	/**
	 * Frontierdomainをキーにしてメンバーの全件数を取得
	 * @return 検索結果の総件数
	 */
	public long selCntFmemList(String type,String gid){
		
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		params.put("type", type);
		params.put("gid", gid);
		
		//メンバーを検索
		return jdbcManager.getCountBySqlFile("/data/selFmemList.sql",params);
	}
}
