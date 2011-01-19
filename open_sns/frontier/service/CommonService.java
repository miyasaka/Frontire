package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import frontier.entity.FrontierUserManagement;


// 共通処理サービス
public class CommonService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;

	/**
	 * グループ、フォローしている・されているメンバーの
	 * ＩＤ、ニックネーム、画像パスリストを返す(最終ログイン日順)
	 * type
	 *   0: グループ＋自分がフォローしているメンバー
	 *   1: グループのメンバー
	 *   2: 自分がフォローしているメンバー
	 *   3: 自分をフォローしているメンバー
	 *   4: 所属グループリスト(ドメイン、ID、グループ名、画像パス、参加メンバー数)
	 */
	public List<BeanMap> getMidList(String type,String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("type",type);
		params.put("mid",mid);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/getMidList.sql",params)
		.getResultList();
	}
	
	/**
	 * グループ、フォローしている・されているメンバーの
	 * ＩＤ、ニックネーム、画像パスリストを返す(最終ログイン日順) ※ＴＯＰの表示制限数分version
	 * type
	 *   0: グループ＋自分がフォローしているメンバー
	 *   1: グループのメンバー
	 *   2: 自分がフォローしているメンバー
	 *   3: 自分をフォローしているメンバー
	 *   4: 所属グループリスト(ドメイン、ID、グループ名、画像パス、参加メンバー数)
	 */
	public List<BeanMap> getMidListTop(String type,String mid,Integer limitnum){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("type",type);
		params.put("mid",mid);
		params.put("limit", limitnum);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/getMidList.sql",params)
		.getResultList();
	}
	
	/**
	 * メンバーIDよりFrontierユーザ管理テーブルの情報を取得する
	 * 一人限定
	 */
	public FrontierUserManagement getFrontierUserManagement(String mid){
	  	return jdbcManager.from(FrontierUserManagement.class)
			.where("mid = ?",mid)
			.getSingleResult();

	}
	
	/**
	 * Frontierドメイン、FrontierIDよりFrontierユーザ管理テーブルの情報を取得する
	 * 一人限定
	 */
	public FrontierUserManagement getFrontierUserManagement(String fdomain,String fid){
	  	return jdbcManager.from(FrontierUserManagement.class)
			.where("frontierdomain = ? and fid = ?",fdomain,fid)
			.getSingleResult();

	}
	
	//Frontier Netデータ取得
	public List<BeanMap> selFNet(){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierNet.sql")
			.getResultList();
	}
}