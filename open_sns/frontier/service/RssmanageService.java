package frontier.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.entity.FrontierGroup;
import frontier.entity.FrontierGroupJoin;
import frontier.entity.FrontierRssInfo;
import frontier.entity.FrontierRssMember;
import frontier.entity.Members;

public class RssmanageService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;

	/**
	 * RSS一覧の返却
	 * @return RSS設定情報検索結果一覧
	 */
	public List<BeanMap> selRssInfoAll(){
		//RSS設定情報を検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierRssInfoAll.sql")
							.getResultList();
	}
	
	/**
	 * RSSメンバーの返却
	 * @param id 検索キー
	 * @return RSSメンバー検索結果一覧
	 */
	public List<BeanMap> selRssMemberOne(Integer id){
		
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("id", id);
		
		//RSSメンバーを検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierRssMember.sql",params)
							.getResultList();
	}
	
	/**
	 * RSS設定情報の検索
	 * @param id 検索キー
	 * @return RSS設定情報検索結果（１件）
	 */
	public FrontierRssInfo selFrontierRssInfoOne(String id) {
		return jdbcManager.from(FrontierRssInfo.class)
							.where("id = ?",id)
							.getSingleResult();
	}
	
	/**
	 * RSSメンバーの検索
	 * @param id 検索キー(ID)
	 * @param flg 画面遷移の状態を示す
	 * @return RSSメンバー検索結果一覧
	 */
	public List<BeanMap> selRssMemberSearch(String id,String flg,String searchword,String rflg,String searchgroup,Integer limit,Integer offset,String sortname){
		
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("id", id);
		params.put("searchFlg", flg);
		params.put("searchword", searchword);
		params.put("r1", rflg);
		params.put("searchgroup", searchgroup);
		params.put("offset", offset);
		params.put("limit", limit);	
		params.put("sortname", sortname);
		
		//RSSメンバーを検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierRssMemberPlus.sql",params)
							.getResultList();
	}
	
	/**
	 * RSSメンバーの総件数取得
	 * @param id 検索キー(ID)
	 * @param flg 画面遷移の状態を示す
	 * @return 検索結果の総件数
	 */
	public long selCntRssMemberSearch(String id,String flg,String searchword,String rflg,String searchgroup){
		
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("id", id);
		params.put("searchFlg", flg);
		params.put("searchword", searchword);
		params.put("r1", rflg);
		params.put("searchgroup", searchgroup);
		
		//RSSメンバーを検索
		return jdbcManager.getCountBySqlFile("/data/selFrontierRssMemberPlus.sql",params);
	}
	
	/**
	 * 特定のメンバーが設定されているRSSファイルの検索
	 * @param mid 検索キー(メンバーID)
	 * @return 検索結果一覧
	 */
	public List<BeanMap> selFrontierRssMemberId(String mid,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid", mid);
		params.put("limit", limit);	

		//RSSファイルを検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierRssMemberId.sql",params)
							.getResultList();
	}
	
	
	/**
	 * RSS設定情報の新規登録
	 * @param pname パターン名
	 * @param fname ファイル名
	 * @param contents 内容
	 * @param id 検索キー
	 * @return ID
	 */
	public Integer insFrontierRssInfo(String pname,String fname,String contents,String mid){
		//IDの最大値を取得
		Integer id = getMaxId();
		
		FrontierRssInfo fri = new FrontierRssInfo();
		fri.id = id;
		fri.patternname = pname;
		fri.filename = fname;
		fri.detail = contents;
		fri.entid = mid;
		fri.updid = mid;
		
		//RSS設定情報登録
		jdbcManager.updateBySqlFile("/data/insFrontierRssInfo.sql",fri).execute();
		
		return id;
	}
	
	/**
	 * RSSメンバーの新規登録
	 * @param id ID
	 * @param mid メンバーID
	 * @param loginid ログイン者のメンバーID
	 * @return なし
	 */
	public void insertFrontierRssMember(String id,String mid,String loginid){
		FrontierRssMember frm = new FrontierRssMember();
		frm.id = Integer.parseInt(id);
		frm.mid = mid;
		frm.entid = loginid;
		frm.updid = loginid;
		
		// RSSメンバー登録
		jdbcManager.updateBySqlFile("/data/insFrontierRssMember.sql", frm).execute();
	}
	
	/**
	 * RSS設定情報の更新
	 * @param pname パターン名
	 * @param fname ファイル名
	 * @param contents 内容
	 * @param mid 更新者
	 * @param rssid 検索キー
	 * @return なし
	 */
	public void updFrontierRssInfo(String pname,String fname,String contents,String mid,String rssid){
		FrontierRssInfo fri = new FrontierRssInfo();
		fri.patternname = pname;
		fri.filename = fname;
		fri.detail = contents;
		fri.updid = mid;
		fri.id = Integer.parseInt(rssid);
		
		//RSS設定情報登録
		jdbcManager.updateBySqlFile("/data/updFrontierRssInfo.sql",fri).execute();
	}
	
	/**
	 * RSSメンバーの削除
	 * @param id ID
	 * @param mid メンバーID
	 * @param loginid ログイン者のメンバーID
	 * @return なし
	 */
	public void deleteFrontierRssMember(String id,String mid){
		FrontierRssMember frm = new FrontierRssMember();
		frm.id = Integer.parseInt(id);
		frm.mid = mid;
		
		// RSSメンバー削除
		jdbcManager.updateBySqlFile("/data/delFrontierRssMember.sql", frm).execute();
	}
	
	/**
	 * RSS設定の削除
	 * @param id ID
	 * @return なし
	 */
	public void deleteFrontierRssInfo(String id){
		FrontierRssInfo fri = new FrontierRssInfo();
		fri.id = Integer.parseInt(id);
		
		// RSSメンバー削除
		jdbcManager.updateBySqlFile("/data/delFrontierRssInfo.sql", fri).execute();		
	}
	
	/**
	 * RSSメンバー追加：最初に呼ばれる
	 * @param loginid ログイン者のメンバーID
	 * @param rssid ID
	 * @param num 現在画面で選択されている参加者の数
	 * @param dbList 検索結果一覧
	 * @return なし
	 */
	public void addRssMember(String loginid,
							String rssid,
							List<String> num,
							List<BeanMap> dbList){
		
		//RSSメンバー登録・削除
		String nowMid  = null;
		String nowJoin = null;
		
		for(int i=0;i<dbList.size();i++){
			//現在画面表示している一覧の人数分繰り返す
			
			nowMid = (String)dbList.get(i).get("mid");
			nowJoin = (String)dbList.get(i).get("joinflg");

			if(num.contains(nowMid)){
				//チェックボックスで選択していた。
				if(nowJoin.equals("1")){
					//既に参画->何もしない
					logger.debug("★★更新★★"+nowMid+"★★★");
				}else{
					//未参画->insert
					logger.debug("★★新規★★"+nowMid+"★★★");
					insertFrontierRssMember(rssid, nowMid, loginid);
				}
			}else if(nowJoin.equals("1")){
				//チェックボックスには含まれていない&参加していた。つまり、メンバーから削除。
				logger.debug("★★削除★★"+nowMid+"★★★");
				deleteFrontierRssMember(rssid, nowMid);
			}
		}
	}
	
	/**
	 * パターン名重複チェック
	 * @param name 検索キー
	 * @param rssid 検索キー
	 * @return 0:重複なし、1:重複あり
	 */
	public long chkPatternName(String name,String rssid){
		long rParam = 0;
		
		//新規と更新でチェック方法を切り替える
		if(rssid == null || rssid.equals("")){
			//新規の場合
			rParam = jdbcManager.from(FrontierRssInfo.class)
			  					.where("patternname = ?",name)
			  					.getCount();
		}else{
			//更新の場合(自分を含まない)
			rParam = jdbcManager.from(FrontierRssInfo.class)
				.where("id != ? and patternname = ?",rssid,name)
				.getCount();
		}
		
		return rParam;
	}
	
	/**
	 * ファイル名重複チェック
	 * @param name 検索キー
	 * @param rssid 検索キー
	 * @return 0:重複なし、1:重複あり
	 */
	public long chkFileName(String name,String rssid){
		long rParam = 0;
		
		//新規と更新でチェック方法を切り替える
		if(rssid == null || rssid.equals("")){
			//新規の場合
			rParam = jdbcManager.from(FrontierRssInfo.class)
			  					.where("filename = ?",name)
			  					.getCount();
		}else{
			//更新の場合(自分を含まない)
			rParam = jdbcManager.from(FrontierRssInfo.class)
				.where("id != ? and filename = ?",rssid,name)
				.getCount();
		}
		
		return rParam;
	}
	
	/**
	 * IDの最大値取得
	 * @param なし
	 * @return IDの最大値
	 */
	public Integer getMaxId(){
		Map<String, Object> params = new HashMap<String, Object>();
		
		List<BeanMap> lbm = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierRssInfoMaxId.sql",params)
						  .getResultList();
		
		return (Integer)lbm.get(0).get("id");
	}
	
	/**
	 * ファイル一覧 Ajax ファイル情報加工 (BeanMap版)
	 * <ul>
	 * <li>HTMLタグ無効化</li>
	 * </ul>
	 * @param results 検索結果
	 */
	public void setFileDecoration(BeanMap results){
		//nullの場合nullという文字がでるのでその対策
		if(results.get("name")==null){
			results.put("viewName"       , "");			
		}else{
			results.put("viewName"       , setSanitizing(results.get("name")));			
		}
		if(results.get("statusname")==null){
			results.put("viewStatusname"       , "");			
		}else{
			results.put("viewStatusname"       , setSanitizing(results.get("statusname")));			
		}
		results.put("viewNickname"    , setSanitizing(results.get("nickname")));		
		results.put("viewGName"    , setSanitizing(results.get("gname")));
	}
	
	/**
	 * ファイル一覧 Ajax HTMLタグ無効化
	 * @param obj
	 * @return String
	 */
	public String setSanitizing(Object obj){
		String strObj = null;
		if(obj != null){
			strObj = CmnUtility.htmlSanitizing(obj.toString());
		}
		return strObj;
	}
}
