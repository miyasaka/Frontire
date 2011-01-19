package frontier.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.dto.AppDefDto;
import frontier.entity.Follow;
import frontier.entity.Members;

public class MemService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;
	//日記一覧検索
	public List<BeanMap> selDiaryList(String mid,List<String> pubdiary){
		Map<String,Object> params = new HashMap<String,Object>();

		//パラメータ設定
		params.put("mid", mid);
		params.put("pubdiary", pubdiary);

		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selRecentDiary",params)
					.limit(appDefDto.FP_MEM_NEWDIARY_PGMAX)
					.getResultList();

	}

	//日記一覧検索（他Frontier用）
	public List<BeanMap> selDiaryListOther(
		String mid,
		String month,
		String day,
		int offset,
		List<String> pubdiary,
		String membertype,
		List<String> publevel,
		List<String> appStatusList,
		Integer limit
	){
		Map<String,Object> params = new HashMap<String,Object>();

		//パラメータ設定
		params.put("mid", mid);
		params.put("searchMonth", month);
		params.put("searchDay", day);
		params.put("limit", limit);
		params.put("pubdiary", pubdiary);
		params.put("membertype", membertype);
		params.put("publevel", publevel);
		params.put("appstatus", appStatusList);
		//params.put("limit", 2);

		params.put("offset", offset);

		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selDiaryListMemTop",params)
					.getResultList();

	}

	//フォロー状態変更
	public void setFollowState(String status,String followmid,String followermid,Integer follownumber,Integer followernumber){

		Follow f = new Follow();
		f.followmid = followmid;
		f.followermid = followermid;
		f.confirmflg = "0";
		f.entid = followmid;
		f.entdate = new Timestamp((new java.util.Date()).getTime());
		f.updid = followmid;
		f.upddate = new Timestamp((new java.util.Date()).getTime());
		f.delflg = "0";
		//フォローする
		if(status.equals("0")){
			//delete & insert にて登録
			//delete
			jdbcManager.delete(f).execute();
			//insert
			jdbcManager.insert(f).execute();
			//フォローした、された数を+1
			follownumber = follownumber+1;
			followernumber = followernumber+1;
		//フォロー解除
		}else{
			//フォローした数（自分）更新
			Follow fitem = new Follow();
			fitem.updid       = followmid;
			fitem.followmid   = followmid;
			fitem.followermid = followermid;

			jdbcManager.updateBySqlFile("/data/delFollow", fitem)
			.execute();
			//jdbcManager.delete(f).execute();
			//フォローした、された数を-1
			follownumber = follownumber-1;
			followernumber = followernumber-1;
		}
		
		//フォローした数（自分）更新
		Members followitem = new Members();
		followitem.mid            = followmid;
		followitem.updid            = followmid;
		followitem.follownumber   = follownumber;

		jdbcManager.updateBySqlFile("/data/updFollowMem", followitem)
		.execute();
		//フォローされている数（メンバー）更新
		Members followeritem = new Members();
		followeritem.mid            = followermid;
		followeritem.updid            = followmid;
		followeritem.followernumber = followernumber;
		jdbcManager.updateBySqlFile("/data/updFollowerMem", followeritem)
		.execute();
	}

	public List<BeanMap> selAlbumList(String mid,List<String> publevel){
		Map<String,Object> params = new HashMap<String,Object>();

		//パラメータ設定
		params.put("mid", mid);
		params.put("publevel", publevel);

		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selRecentAlbum",params)
					.limit(appDefDto.FP_MEM_NEWALBUM_PGMAX)
					.getResultList();
	}

	public List<BeanMap> getMemList(String type,String mid,String fdomain,String gid){
		Map<String,Object> params = new HashMap<String,Object>();

		//パラメータ設定
		params.put("frontierdomain", fdomain);
		params.put("gid", gid);

		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selMemGroup",params)
					.limit(appDefDto.FP_MEM_NEWALBUM_PGMAX)
					.getResultList();
	}

	//プロフィールデータ取得
	public BeanMap selProfile(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		BeanMap results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selprofile.sql",params)
		.getSingleResult();
		return results;
	}

	//フォロー状態変更
	public void setFollowConfirm(String mid,String vmid){

		Follow f = new Follow();
		f.followmid = vmid;
		f.followermid = mid;
		//フォローした数（自分）、フォローされている数（メンバー）更新

		jdbcManager.updateBySqlFile("/data/updFollow", f)
		.execute();
	}
}