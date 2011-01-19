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
import frontier.dto.AppDefDto;
import frontier.entity.FrontierGroup;
import frontier.entity.FrontierGroupJoin;
import frontier.form.pc.GroupForm;

public class GroupService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	private AppDefDto appDefDto;
	
	//グループ一覧の返却
	public List<BeanMap> selGroup(String gid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		params.put("gid", gid);
		
		//同じFrontierの全てのグループを検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selGroup.sql",params)
							.getResultList();
	}
	
	//グループメンバー+グループ未参加のメンバー一覧の返却
	public List<BeanMap> selGroupMember(String gid,
										String searchtext,
										String r1,
										String joincheck,
										Integer limit,
										Integer offset,
										String sortname){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		params.put("searchword", replaceSql(searchtext));
		params.put("r1", r1);
		params.put("joincheck", joincheck);
		params.put("gid", gid);
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("sortname", sortname);
		
		//グループメンバー+グループ未参加のメンバーを検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selGroupMemberPlus.sql",params)
							.getResultList();
		
	}
	
	/**
	 * ユーザが管理しているグループを取得
	 * @param mid 検索キー(メンバーID)
	 * @return 検索結果一覧
	 */
	public List<BeanMap> selGroupManage(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		params.put("mid", mid);
		
		//ユーザが管理しているグループを取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierGroupManage.sql",params)
							.getResultList();
	}

	/**
	 * ユーザが管理しているグループを取得
	 * @param offset 何ページ目から取得するかの設定値
	 * @param limit 取得件数の最大値
	 * @return 検索結果一覧
	 */
	public List<BeanMap> selGroup(Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		params.put("offset", offset);
		params.put("limit", limit);	
		
		//同じFrontierの全てのグループを検索
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selGroupFmem.sql",params)
							.getResultList();
	}	
	
	/**
	 * ユーザが管理しているグループを取得
	 * @param mid 検索キー(メンバーID)
	 * @return 検索結果一覧
	 */
	public long cntSelGroup(){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		
		//同じFrontierの全てのグループを検索
		return jdbcManager.getCountBySqlFile("/data/selGroupFmem.sql",params);
	}	
	
	//グループメンバー+グループ未参加のメンバー件数の返却
	public long cntGroupMember(String gid,
										String searchtext,
										String r1,
										String joincheck){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		params.put("searchword", replaceSql(searchtext));
		params.put("r1", r1);
		params.put("joincheck", joincheck);
		params.put("gid", gid);
		
		//グループメンバー+グループ未参加のメンバーを検索
		return jdbcManager.getCountBySqlFile("/data/selGroupMemberPlus.sql",params);
		
	}
	
	//グループ名重複チェック
	public long chkGroupName(String name,String gid){
		long rParam = 0;
		
		//新規と更新でチェック方法を切り替える
		if(gid == null || gid.equals("")){
			//新規の場合
			rParam = jdbcManager.from(FrontierGroup.class)
			  					.where("frontierdomain = ? and gname = ? and delflg = '0'",appDefDto.FP_CMN_HOST_NAME,name)
			  					.getCount();
		}else{
			//更新の場合(自分を含まない)
			rParam = jdbcManager.from(FrontierGroup.class)
				.where("frontierdomain = ? and gid != ? and gname = ? and delflg = '0'",appDefDto.FP_CMN_HOST_NAME,gid,name)
				.getCount();
		}
		
		return rParam;
	}
	
	
	//グループ登録
	public void insertGroup(String uid,String gname,FormFile picpath,List<String> num,List<Object> authList,GroupForm groupForm) throws IOException, Exception{
		//グループIDの最大値を取得する。
		List<BeanMap> results = getGid();
		String newgid = "g" + results.get(0).get("newid").toString();
		String absolutePicPass = "";
		
		// 画像のアップロード(画像パス名の長さが0以上ならupload)
		//null対策
		if(picpath!=null){
			if(picpath.getFileName().length()>0){
				absolutePicPass = picupload(newgid,picpath);
			}
		}		
		
		FrontierGroup fg = new FrontierGroup();
		fg.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
		fg.gid = newgid;
		fg.gname = gname;
		fg.pic = absolutePicPass;
		fg.joinnumber = num.size();
		fg.entid = uid;
		fg.updid = uid;
		
		// グループ登録
		jdbcManager.updateBySqlFile("/data/insFrontierGroup.sql", fg).execute();
		
		//グループメンバー登録
		FrontierGroupJoin fgj = new FrontierGroupJoin();
		for(int i=0;i<num.size();i++){
			insertGroupMember(newgid,uid,num.get(i),getManageFlg(authList,num.get(i)),fgj);
		}
		
		//グループに登録された場合、フォロー関係のあった人が同じグループの場合削除する
		FrontierGroupJoin fgj2 = new FrontierGroupJoin();
		for(int i=0;i<num.size();i++){
			deleteFollow(newgid,num.get(i),fgj2,uid);
		}

		//グループに登録された場合、フォロー関係のあった人が同じグループの場合削除する場合もあるのでフォロー数、フォロワ数を更新
		FrontierGroupJoin fgj3 = new FrontierGroupJoin();
		for(int i=0;i<num.size();i++){
			updMemberFollow(uid,num.get(i),fgj3);
		}

		
		
		//登録したグループIDをsessionに格納する
		groupForm.gid = newgid;
		
		
	}
	
	//グループ更新
	public void updateGroup(String uid,
							String gname,
							FormFile picpath,
							List<String> num,
							List<Object> authList,
							String gid,
							List<BeanMap> dbList) throws IOException, Exception{
		
		//グループメンバー登録・更新・削除
		String nowMid  = null;
		String nowJoin = null;
		String nowAuth = null;
		boolean newFlg = false;
		FrontierGroupJoin fgj = new FrontierGroupJoin();
		
		for(int i=0;i<dbList.size();i++){
			//現在画面表示している一覧の人数分繰り返す
			
			nowMid = (String)dbList.get(i).get("mid");
			nowJoin = (String)dbList.get(i).get("joinflg");
			nowAuth = (String)dbList.get(i).get("manageflg");

			if(num.contains(nowMid)){
				//チェックボックスで選択していた。
				if(nowJoin.equals("1")){
					//既に参画->update
					logger.debug("★★更新★★"+nowMid+"★★★");
					updateGroupMember(gid,nowMid,getManageFlg(authList,nowMid),fgj);
				}else{
					//未参画->insert
					logger.debug("★★新規★★"+nowMid+"★★★");
					//新規登録がいた場合フラグを立てる
					newFlg = true;
					insertGroupMember(gid,uid,nowMid,getManageFlg(authList,nowMid),fgj);
				}
			}else if(nowJoin.equals("1")){
				//チェックボックスには含まれていない&参加していた。つまり、メンバーから削除。
				logger.debug("★★削除★★"+nowMid+"★★★");
				deleteGroupMember(gid,nowMid,fgj);
			}
		}
		
		// 画像のアップロード(画像パス名の長さが0以上ならupload)
		String absolutePicPass = null;
		//null対策
		if(picpath!=null){
			if(picpath.getFileName().length()>0){
				absolutePicPass = picupload(gid,picpath);
			}
		}
		
		FrontierGroup fg = new FrontierGroup();
		fg.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
		fg.gid = gid;
		fg.gname = gname;
		fg.pic = absolutePicPass;
		fg.updid = uid;
		
		// グループ更新
		jdbcManager.updateBySqlFile("/data/updFrontierGroup.sql", fg).execute();
		
		//新規メンバー追加をおこなった場合、フォロー数の補正をする
		if(newFlg){
			//グループに登録された場合、フォロー関係のあった人が同じグループの場合削除する
			FrontierGroupJoin fgj2 = new FrontierGroupJoin();
			for(int i=0;i<num.size();i++){
				deleteFollow(gid,num.get(i),fgj2,uid);
			}
			
			//グループに登録された場合、フォロー関係のあった人が同じグループの場合削除する場合もあるのでフォロー数、フォロワ数を更新
			//グループメンバー全員検索
			List<FrontierGroupJoin> fgjList = jdbcManager.from(FrontierGroupJoin.class)
														 .where("frontierdomain = ? and gid = ?",appDefDto.FP_CMN_HOST_NAME,gid)
														 .getResultList();
			
			FrontierGroupJoin fgj3 = new FrontierGroupJoin();
			for(int i=0;i<fgjList.size();i++){
				updMemberFollow(uid,fgjList.get(i).mid,fgj3);
			}			
		}
		
	}
	
	//グループ画像削除
	public void updGroupPic(String gid,String uid){
		FrontierGroup fg = new FrontierGroup();
		fg.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
		fg.gid = gid;
		fg.updid = uid;
		
		// グループ画像削除
		jdbcManager.updateBySqlFile("/data/updFrontierGroupPic.sql", fg).execute();
	}
	
	//グループ削除
	public void delGroup(String gid,String uid){
		FrontierGroup fg = new FrontierGroup();
		
		fg.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
		fg.gid = gid;
		fg.updid = uid;
		
		jdbcManager.updateBySqlFile("/data/delFrontierGroup.sql",fg).execute();
	}
	
	//グループメンバー追加
	private void insertGroupMember(String gid,String uid,String mid,String manageflg,FrontierGroupJoin fgj){
		
			fgj.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
			fgj.gid = gid;
			fgj.mid = mid;
			fgj.manageflg = manageflg;	
			fgj.entid = uid;
			
			// グループメンバー登録
			jdbcManager.updateBySqlFile("/data/insFrontierGroupJoin.sql", fgj).execute();
	}
	
	//グループメンバー更新
	private void updateGroupMember(String gid,String mid,String manageflg,FrontierGroupJoin fgj){
		
			fgj.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
			fgj.gid = gid;
			fgj.mid = mid;
			fgj.manageflg = manageflg;	
			
			// グループメンバー登録
			jdbcManager.updateBySqlFile("/data/updFrontierGroupJoin.sql", fgj).execute();
	}
	
	//グループメンバー削除
	private void deleteGroupMember(String gid,String mid,FrontierGroupJoin fgj){
		fgj.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
		fgj.gid = gid;
		fgj.mid = mid;
		
		// グループメンバー登録
		jdbcManager.updateBySqlFile("/data/delGroupJoin.sql", fgj).execute();		
	}
	
	//フォロー関係削除
	private void deleteFollow(String gid,String mid,FrontierGroupJoin fgj,String uid){
		
			fgj.frontierdomain = appDefDto.FP_CMN_HOST_NAME;
			fgj.gid = gid;
			fgj.mid = mid;
			fgj.entid = uid;
			
			// グループメンバー登録
			jdbcManager.updateBySqlFile("/data/delFollowGroup.sql", fgj).execute();
	}
	
	//メンバーのフォロー数、フォロワー数更新
	private void updMemberFollow(String uid,String mid,FrontierGroupJoin fgj){
		
			fgj.mid = mid;
			fgj.entid = uid;
			
			// グループメンバー登録
			jdbcManager.updateBySqlFile("/data/updMemberFollow.sql", fgj).execute();
	}
	
	//グループID最大値取得
	private List<BeanMap> getGid(){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain", appDefDto.FP_CMN_HOST_NAME);
		
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMaxGid.sql",params)
						  .getResultList();		
	}
	
	// 画像のアップロード処理
	private String picupload(String gid,FormFile picpath) throws IOException, Exception{
		// ディレクトリパス
		String Path = appDefDto.FP_CMN_CONTENTS_DIR+"img/grp/"+gid;
		// ディレクトリ
		String Dir = "img";
		// アップロード画像名取得(yyyymmddhh24miss)
		String picnm = CmnUtility.getToday("yyyyMMddHHmmss") + ".jpg";
		// 登録用画像パス設定
		String pic = "http://"+appDefDto.FP_CMN_HOST_NAME+appDefDto.FP_CMN_CONTENTS_ROOT +"/img/grp/" + gid + "/" + Dir + "/[dir]/" + picnm;
		// ディレクトリ作成
		CmnUtility.makeDir(Path, Dir);
		// 画像ファイルのアップロード(ベースはpic640フォルダ)
		CmnUtility.uploadFile(Path + "/" + Dir + "/pic640/" + picnm, picpath);
		// ファイルのコピー&リサイズ(ディレクトリ数分ループ)
		for (Integer i : appDefDto.FP_CMN_PIC_DIRS) {
			CmnUtility.resize(Path + "/" + Dir + "/" + "pic" + i.toString() + "/" + picnm,i,Path + "/" + Dir + "/pic640/" + picnm,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		}
		
		//登録用の画像パスを返す。
		return pic;
	}
	
	//管理者FLG判定
	private String getManageFlg(List<Object> authList,String mid){
		String manageflg = null;
		if(authList.contains(mid)){
			manageflg = "1";	
		}else{
			manageflg = "0";
		}
		
		return manageflg;
	}
	
	//SQL特殊文字無害化
	private String replaceSql(String word){
		//SQL特殊文字の無害化
		if(word!=null){
			word = word.replaceAll("#", "##");
			word = word.replaceAll("%", "#%");
			word = word.replaceAll("_", "#_");			
		}
		
		return word;
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
		results.put("viewEmail"    , setSanitizing(results.get("email")));
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
