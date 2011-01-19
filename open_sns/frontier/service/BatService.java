package frontier.service;

import frontier.entity.Follow;
import frontier.entity.Frontiernet;
import frontier.entity.Frontiershout;
import frontier.entity.Members;
import frontier.entity.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

public class BatService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	public JdbcManager jdbcManager;

	// 登録・更新者用のID
	private String batId = "system";

	// ================= //
	//   -- SELECT --    //
	// ================= //
	// Frontier Netデータ取得(全件)
	public List<BeanMap> selFNet(){
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/selFrontierNet.sql").getResultList();
	}

	// メンバー情報テーブルの取得
	public List<BeanMap> selMem(String lastdate){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("lastdate",lastdate);
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/selMembers.sql",params).getResultList();
	}

	// フォロー関係情報テーブルの取得
	public List<BeanMap> selFollow(String frontierdomain,String lastdate){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("frontierdomain",frontierdomain);
		params.put("lastdate",lastdate);
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/selFollow.sql",params).getResultList();
	}

	// Frontier Shoutテーブルの取得
	public List<BeanMap> selFrontierShout(String lastdate,String fdomain){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("lastdate",lastdate);
		params.put("fdomain",fdomain);
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/selFrontiershout.sql",params).getResultList();
	}

	// Frontier Shoutテーブルからfid取得
	public List<BeanMap> selFid(String fdomain,String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("fdomain",fdomain);
		params.put("mid",mid);
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/selFid.sql",params).getResultList();
	}

	// Frontierメンバー管理テーブルの存在チェック
	public List<BeanMap> chkFrontierUserManagement(String fdomain,String fid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("fdomain",fdomain);
		params.put("fid",fid);
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/chkFrontierUserManagement.sql",params).getResultList();
	}

	// Frontier Shoutテーブルの存在チェック
	public List<BeanMap> chkFrontierShout(String mid,Integer no){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("no",no);
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/chkFrontierShout.sql",params).getResultList();
	}

	// followテーブルの存在チェック
	public List<BeanMap> chkFollow(String followmid,String followermid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("followmid",followmid);
		params.put("followermid",followermid);
		// データ取得
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/chkFollow.sql",params).getResultList();
	}

	// メンバーID発行
	private String newMid(){
		List<BeanMap> lbm = jdbcManager.selectBySqlFile(BeanMap.class,"data/selNextmid.sql").getResultList();
		return lbm.get(0).get("mid").toString();
	}

	// ================= //
	//   -- UPDATE --    //
	// ================= //
	// Frontier Net 最終取得日時の更新
	public void updFNet(Integer id,String network){
		Frontiernet f = new Frontiernet();
		f.id = id;
		f.network = network;
		// 更新
		jdbcManager.updateBySqlFile("data/bat/updFrontierNet.sql", f).execute();
	}

	// メンバー情報関連の更新
	public void updMembers(
		String mid,
		String pic,
		String nickname,
		String status
	){
		// メンバー情報セット
		Members m = new Members();
		m.mid    = mid;
		m.status = status;
		m.updid  = batId;

		// Frontierユーザー管理セット
		Entry e = new Entry();
		e.mid      = mid;
		e.nickname = nickname;
		e.pic      = pic;
		e.updid    = batId;

		// 更新
		jdbcManager.updateBySqlFile("data/bat/updMembers.sql"                , m).execute(); // 【更新】メンバー情報
		jdbcManager.updateBySqlFile("data/bat/updFrontierUserManagement.sql" , e).execute(); // 【更新】Frontierユーザ管理
	}

	// followテーブルの更新
	public void updFollow(
		String followmid,
		String followermid,
		String confirmflg,
		String delflg
	){
		// Frontier Shoutセット
		Follow f = new Follow();
		f.followmid   = followmid;
		f.followermid = followermid;
		f.confirmflg  = confirmflg;
		f.entid       = batId;
		f.updid       = batId;
		f.delflg      = delflg;

		// 更新
		jdbcManager.updateBySqlFile("data/bat/updFollow.sql",f).execute();
	}

	// Frontier Shoutテーブルの更新
	public void updFrontierShout(
		String mid,
		Integer no,
		String confirmflg,
		String delflg
	){
		// Frontier Shoutセット
		Frontiershout f = new Frontiershout();
		f.mid        = mid;
		f.no         = no;
		f.updid      = batId;
		f.confirmflg = confirmflg;
		f.delflg     = delflg;

		// 更新
		jdbcManager.updateBySqlFile("data/bat/updFrontierShout.sql",f).execute();
	}

	// followテーブルの削除(更新)
	public void delFollow(){
		// Frontier Shoutセット
		Follow f = new Follow();
		f.updid = batId;

		// 更新
		jdbcManager.updateBySqlFile("data/bat/delFollow.sql",f).execute();
	}

	// メンバー情報・フォロー数、フォローワー数の更新
	public void updMembersFollownumber(){
		// 更新
		jdbcManager.updateBySqlFile("data/bat/updMembersFollownumber.sql").execute();
	}

	// ================= //
	//   -- INSERT --    //
	// ================= //
	// メンバー情報関連の登録
	public void insMembers(
		String fdomain,
		String fid,
		String pic,
		String nickname,
		String status
	) {
		//メンバーID発行
		String insmid = "m" + newMid();

		// メンバー情報セット
		Members m = new Members();
		m.mid        = insmid;
		m.email      = "dummy";  // 他Frontierユーザなので固定文字列
		m.nickname   = nickname;
		m.password   = "dummy";  // 他Frontierユーザなので固定文字列
		m.status     = status;
		m.updid      = batId;
		m.membertype = "1";      // 他Frontierユーザなので1:他Frontierユーザ

		// Frontierユーザー管理セット
		Entry ef = new Entry();
		ef.frontierdomain = fdomain;
		ef.fid            = fid;
		ef.mid            = insmid;
		ef.nickname       = nickname;
		ef.pic            = pic;
		ef.entid          = batId;
		ef.updid          = batId;

		// それ以外(テーブルだけ作成)のセット
		Entry eo = new Entry();
		eo.mid = insmid;

		// 登録
		jdbcManager.updateBySqlFile("data/bat/insMembers.sql"                ,m).execute();  // 【登録】メンバー情報
		jdbcManager.updateBySqlFile("data/insMemberiteminfo.sql"             ,eo).execute(); // 【登録】メンバー情報公開設定
		jdbcManager.updateBySqlFile("data/insMembersphotoinfo.sql"           ,eo).execute(); // 【登録】メンバー画像情報
		jdbcManager.updateBySqlFile("data/insMemberaddinfo.sql"              ,eo).execute(); // 【登録】メンバー追加情報
		jdbcManager.updateBySqlFile("data/insMemberSetupInfo.sql"            ,eo).execute(); // 【登録】メンバー設定情報
		jdbcManager.updateBySqlFile("data/insFrontierUserManagement.sql.sql" ,ef).execute(); // 【登録】Frontierユーザ管理
	}

	// followテーブルの登録
	public void insFollow(
		String followmid,
		String followermid,
		String entdate,
		String confirmflg,
		String delflg
	){
		// entdate をtimestamp型に変換
		Timestamp tsentdate = null;
		try {
			tsentdate = new Timestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(entdate).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// followセット
		Follow f = new Follow();
		f.followmid   = followmid;
		f.followermid = followermid;
		f.confirmflg  = confirmflg;
		f.entid       = batId;
		f.entdate     = tsentdate;
		f.updid       = batId;
		f.delflg      = delflg;

		// 登録
		jdbcManager.updateBySqlFile("data/bat/insFollow.sql",f).execute();
	}

	// Frontier Shoutテーブルの登録
	public void insFrontierShout(
		String mid,
		Integer no,
		String comment,
		Integer twitter,
		String entdate,
		Integer demandflg,
		Integer pubLevel,
		String confirmflg,
		String delflg
	){
		// entdate をtimestamp型に変換
		Timestamp tsentdate = null;
		try {
			tsentdate = new Timestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(entdate).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Frontier Shoutセット
		Frontiershout f = new Frontiershout();
		f.mid        = mid;
		f.no         = no;
		f.comment    = comment;
		f.twitter    = twitter;
		f.entid      = batId;
		f.entdate    = tsentdate;
		f.updid      = batId;
		f.demandflg  = demandflg;
		f.pub_level  = pubLevel;
		f.confirmflg = confirmflg;
		f.delflg     = delflg;

		// 登録
		jdbcManager.updateBySqlFile("data/bat/insFrontierShout.sql",f).execute();
	}
}