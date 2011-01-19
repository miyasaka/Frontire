package frontier.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.entity.Follow;
import frontier.entity.FrontierGroup;
import frontier.entity.FrontierGroupJoin;
import frontier.entity.FrontierUserManagement;
import frontier.entity.Members;
import frontier.entity.Visitors;

public class MembersService {
	Logger logger = Logger.getLogger(this.getClass().getName());

  @Resource
  protected JdbcManager jdbcManager;

  public List<BeanMap> results;
  public Visitors v;
  public long count;

  public List<Members> getResultList() {
      return jdbcManager.from(Members.class).getResultList();
  }

  public List<BeanMap> getMemberList(String mid,String nickname,String aboutme) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("mid", mid);
		params.put("nickname", CmnUtility.replaceSql(nickname));
		params.put("aboutme", CmnUtility.replaceSql(aboutme));

		List<BeanMap> results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMemberList.sql",params)
		.getResultList();
      return results;
  }

  public Members getResultById(String id) {
  	return jdbcManager.from(Members.class)
  				.where("mid = ?",id )
  				.getSingleResult();
  }

  public Members getResultByEmailAndPasswd(String mail,String passwd) {
	  	return jdbcManager.from(Members.class)
	  				.where("email = ? and password = ? and status = ?",mail,passwd,"1")
	  				.getSingleResult();
  }

  //フォロー/フォロワー数変更
  public void updFCnt(String status,String followmid,String followermid){
	  Members follow = getResultById(followmid);
	  Members follower = getResultById(followermid);

	  //フォローする
	  if(status.equals("0")){

	  //フォロー解除
	  }else{
		  try{
			  follow.follownumber -=1;
			  follow.upddate = new Timestamp((new java.util.Date()).getTime());
			  follow.updid = followmid;
			  jdbcManager.update(follow).includes("follownumber","upddate","updid").execute();
			  logger.debug(follower);
			  follower.followernumber -=1;
			  follower.upddate = new Timestamp((new java.util.Date()).getTime());
			  follower.updid = followmid;
			  jdbcManager.update(follower).includes("followernumber","upddate","updid").execute();
		  }catch(Exception e){

		  }
	  }


  }

  //退会処理
  public void updMemberStatus(String mid,String reason){
	  Members m = getResultById(mid);
	  m.status = "2";
	  m.followernumber = 0;
	  m.follownumber = 0;
	  m.updid = mid;
	  m.upddate = new Timestamp ((new java.util.Date()).getTime());
	  m.secessionreason=reason;
	  m.secessiondate = new Timestamp ((new java.util.Date()).getTime());
	  jdbcManager.update(m).includes("status","updid","upddate","secessionreason","secessiondate","followernumber","follownumber").execute();
  }

  //フォロー/フォロワー数一括変更
  public void updListFCnt(String type,List<Object> listmid,String mid){
	  //フォロー/フォロワーが居る場合のみ更新
	  if(listmid.size()>0){
		  List<Members> flist = new ArrayList<Members>();
		  List<Members> _flist = new ArrayList<Members>();
		  List<Follow> fwlist = new ArrayList<Follow>();

		  flist = jdbcManager.from(Members.class).where(new SimpleWhere().in("mid",listmid)).getResultList();
		  //フォロー/フォロワー数更新
		  for(Members m:flist){
			  Follow f = new Follow();
			  //値の更新
			  m.updid = mid;
			  m.upddate = new Timestamp((new java.util.Date()).getTime());
			  if(type.equals("0")){
				  //メンバー情報
				  m.followernumber -=1;
				  //フォロー
				  f.followermid=m.mid;
				  f.followmid=mid;
			  }else if(type.equals("1")){
				  //メンバー情報
				  m.follownumber -=1;
				  //フォロワー
				  f.followmid=m.mid;
				  f.followermid=mid;
			  }
			  //フォロー関係削除の更新カラム設定
			  f.updid = mid;
			  f.upddate = new Timestamp((new java.util.Date()).getTime());
			  f.delflg = "1";
			  _flist.add(m);
			  fwlist.add(f);
		  }
		  //フォロー/フォロワー数更新
		  jdbcManager.updateBatch(_flist).execute();
		  //フォロー/フォロワー削除
		  jdbcManager.updateBatch(fwlist).includes("updid","upddate","delflg").execute();
	  }
  }

  //ｸﾞﾙｰﾌﾟ参加人数一括変更
  public void updListGCnt(List<BeanMap> gmap,String mid){
	  //ｸﾞﾙｰﾌﾟに参加している場合のみ更新
	  if(gmap.size()>0){
		  List<FrontierGroupJoin> gjoin = new ArrayList<FrontierGroupJoin>();
		  List<FrontierGroup> glist = new ArrayList<FrontierGroup>();

		  //ｸﾞﾙｰﾌﾟメンバー削除
		  for(BeanMap b:gmap){
			  //ｸﾞﾙｰﾌﾟ参加人数
			  FrontierGroup f = new FrontierGroup();
			  f.frontierdomain=b.get("frontierdomain").toString();
			  f.gid=b.get("gid").toString();
			  f.updid=mid;
			  f.upddate=new Timestamp((new java.util.Date()).getTime());
			  f.joinnumber=Integer.parseInt(b.get("joinnumber").toString())-1;
			  glist.add(f);

			  //ｸﾞﾙｰﾌﾟメンバー
			  FrontierGroupJoin fj = new FrontierGroupJoin();
			  fj.frontierdomain=b.get("frontierdomain").toString();
			  fj.gid=b.get("gid").toString();
			  fj.mid=mid;
			  gjoin.add(fj);
		  }
		  //参加人数更新
		  jdbcManager.updateBatch(glist).includes("updid","upddate","joinnumber").execute();
		  //ｸﾞﾙｰﾌﾟメンバー削除
		  jdbcManager.deleteBatch(gjoin).execute();
	  }
  }

  // 足跡登録or更新
  public void updVisitors(String mid,String vmid){
	// Mapオブジェクトに条件用パラメタを定義
	v = new Visitors();
	v.mid = mid;
	v.visitmid = vmid;
	results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/chkVisitors",v)
		.getResultList();
	if (results.size()==1){
		// 既にデータがあれば更新
		jdbcManager.updateBySqlFile("/data/updVisitors", v).execute();

	}else{
		// データがなければ登録
		jdbcManager.updateBySqlFile("/data/insVisitors", v).execute();
	}
  }

  //最終アクセス日時の更新
  public void updLastAcessDate(String mid){
	  Members members = new Members();

	  members.mid = mid;

	  jdbcManager.updateBySqlFile("/data/updMembersLastaccessdate", members)
	  			 .execute();

  }

  //メンバータイプによるユーザの取得
  public Members getResultByMembertype(String membertype) {
	  	return jdbcManager.from(Members.class)
	  				.where("membertype = ?",membertype )
	  				.getSingleResult();
	  }
	
	// frontierユーザ管理テーブルよりメンバーIDをキーにデータ取得
	public FrontierUserManagement getFrontierUserManagement(String mid) {
				return jdbcManager.from(FrontierUserManagement.class)
				.where("mid = ?",mid )
				.getSingleResult();
	}
	
	//Twitter Flgとアカウント名の更新処理
	public void updTwitterFlg(String mid,String twitteraccount){
		Members m = getResultById(mid);
		m.twitterflg = "1";
		m.twitteraccount = twitteraccount;
		
		jdbcManager.update(m).includes("twitterflg","twitteraccount").execute();
	}
	
	//Twitterアカウント名の更新処理
	public void updTwitterAccount(String mid,String twitteraccount){
		Members m = getResultById(mid);
		m.twitteraccount = twitteraccount;
		
		jdbcManager.update(m).includes("twitteraccount").execute();
	}
	
	//Twitter投稿先の更新処理
	public void updTarget(String mid,String target){
		Members m = getResultById(mid);
		m.target = target;
		
		jdbcManager.update(m).includes("target").execute();
	}
	
	/**
	 * 
	 * @deprecated
	 */
	//TwitterのアカウントとFrontierに登録してあるアカウントを比較し違いがあれば更新する
	public void checkTwitterAccount(String mid,String twitterAccount){
		//メンバーテーブルからTwitterアカウント取得
		Members m = getResultById(mid);
		String frontierAccount = (String)m.twitteraccount;
		
		if(frontierAccount!=null && frontierAccount!=""){
			if(!frontierAccount.equals(twitterAccount)){
				//FrontierのTwitterアカウント更新
				updTwitterAccount(mid,twitterAccount);
			}
		}
	}

	/**
	 * ユーザの情報を1件取得する処理
	 * @param frontierdomain [検索キー]検索対象のドメイン名
	 * @param mid [検索キー]検索対象のメンバーID
	 * @return ユーザの情報（1件)
	 */
	public List<BeanMap> selProfileFShout(String frontierdomain,Object mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("frontierdomain",frontierdomain);
		return jdbcManager
			.selectBySqlFile(BeanMap.class,"data/selprofileFShout.sql",params)
			.getResultList();

	}
}
