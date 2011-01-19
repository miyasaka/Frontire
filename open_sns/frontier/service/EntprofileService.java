package frontier.service;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import frontier.entity.Communities;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.sql.Timestamp;

public class EntprofileService {
	@Resource
	public AppDefDto appDefDto;
	Logger logger = Logger.getLogger(this.getClass().getName());
	public Communities c;
	@Resource
	protected JdbcManager jdbcManager;
	public List<BeanMap> results;
	public String cid;
	public Timestamp ts;
	public String Path;
	public String Dir;
	public String picnm = "";
	public String pic = "";
	public Integer[] dirs = {640,480,240,180,120,76,60,42};

	// 画像の削除
	public void delpic(String cid,String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		// コミュニティ情報の取得
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComDtDetails.sql",params)
		.getResultList();

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());

		/* ■ コミュニティの更新 */
		// パラメタセット(コミュニティ)
		c              = new Communities();
		c.cid          = cid;
		c.title        = results.get(0).get("title").toString();
		c.detail       = results.get(0).get("detail").toString();
		c.category1    = results.get(0).get("category1").toString();
		c.joincond     = results.get(0).get("joincond").toString();
		c.publevel     = results.get(0).get("publevel").toString();
		c.makabletopic = results.get(0).get("makabletopic").toString();
		c.pic          = "";
		c.upddate      = ts;
		c.updid        = mid;

		jdbcManager
		.updateBySqlFile("/data/updCommunities.sql", c)
		.execute();
	}

	// 内容更新
	public void updcommunities(
		String cid,
		String mid,
		String title,
		String detail,
		String category1,
		String joincond,
		String publevel,
		String makabletopic,
		FormFile picpath
	) throws IOException, Exception{
		//null対策
		if(picpath!=null && picpath.getFileName().length()>0){
			// 画像パスが入力されていたらアップロード処理
			picupload(cid,picpath);
		} else {
			// 入力されていなければ、同じ値で更新
			Map<String, Object> params = new HashMap<String, Object>();
			// Mapオブジェクトに条件用パラメタを定義
			params.put("cid",cid);
			// コミュニティ情報より、画像パスの取得
			pic = jdbcManager
			.selectBySqlFile(BeanMap.class,"/data/selComDtDetails.sql",params)
			.getResultList().get(0).get("pic").toString();
		}

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());

		/* ■ コミュニティの更新 */
		// パラメタセット(コミュニティ)
		c              = new Communities();
		c.cid          = cid;
		c.title        = title;
		c.detail       = detail;
		c.category1    = category1;
		c.joincond     = joincond;
		c.publevel     = publevel;
		c.makabletopic = makabletopic;
		c.pic          = pic;
		c.upddate      = ts;
		c.updid        = mid;

		jdbcManager
		.updateBySqlFile("/data/updCommunities.sql", c)
		.execute();

	}

	// コミュニティ削除(関連項目を含む全て)
	public void delComAll(String cid){
		// コミュニティ情報の削除
		jdbcManager.updateBySqlFile("/data/delCommunityBbs.sql", cid).execute();
		jdbcManager.updateBySqlFile("/data/delCommunityEnterant.sql", cid).execute();
		jdbcManager.updateBySqlFile("/data/delCommunityEventEntry.sql", cid).execute();
		jdbcManager.updateBySqlFile("/data/delCommunityEvent.sql", cid).execute();
		jdbcManager.updateBySqlFile("/data/delCommunities.sql", cid).execute();
	}

	// 画像のアップロード処理
	public void picupload(String cid,FormFile picpath) throws IOException, Exception{
		// ディレクトリパス
		Path = appDefDto.FP_CMN_CONTENTS_DIR+"img/com/"+cid;
		// ディレクトリ
		Dir = "img";
		// アップロード画像名取得(yyyymmddhh24miss)
		picnm = CmnUtility.getToday("yyyyMMddHHmmss") + ".jpg";
		// 登録用画像パス設定
		pic = "/img/com/" + cid + "/" + Dir + "/dir/" + picnm;
		// ディレクトリ作成
		CmnUtility.makeDir(Path, Dir);
		// 画像ファイルのアップロード(ベースはpic640フォルダ)
		CmnUtility.uploadFile(Path + "/" + Dir + "/pic640/" + picnm, picpath);
		// ファイルのコピー&リサイズ(ディレクトリ数分ループ)
		for (Integer i : dirs) {
			CmnUtility.resize(Path + "/" + Dir + "/" + "pic" + i.toString() + "/" + picnm,i,Path + "/" + Dir + "/pic640/" + picnm,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		}
	}

	// 共通項目の取得
	public List<BeanMap> selectItems(String itemid){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstComItems",itemid)
		.getResultList();
		return results;
	}
}