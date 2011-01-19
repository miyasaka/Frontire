package frontier.service;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import frontier.entity.Communities;
import frontier.entity.CommunityEnterant;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class SearchcomService {
	@Resource
	public AppDefDto appDefDto;
	Logger logger = Logger.getLogger(this.getClass().getName());
	public Communities c;
	public CommunityEnterant ce;
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

	// 共通項目の取得
	public List<BeanMap> selectItems(String itemid){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstComItems",itemid)
		.getResultList();
		return results;
	}

	// コミュニティ登録
	public String insertCommunities(
			String mid,
			String title,
			String detail,
			String category1,
			String joincond,
			String publevel,
			String makabletopic,
			FormFile picpath
		) throws IOException, Exception{
		// コミュニティIDの生成(シーケンスからナンバーを取得する)
		cid = jdbcManager
			.selectBySql(String.class, "select lpad(nextval('seq_cid'),9,'0')")
			.getSingleResult();
		cid = "c" + cid;
		
		// 画像のアップロード(画像パス名の長さが0以上ならupload)
		//null対策
		if(picpath!=null){
			if(picpath.getFileName().length()>0){
				picupload(cid,picpath);
			}
		}

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());

		/* ■ コミュニティの登録 */
		// パラメタセット(コミュニティ)
		c              = new Communities();
		c.cid          = cid;
		c.admmid       = mid;
		c.title        = title;
		c.detail       = detail;
		c.category1    = category1;
		c.joincond     = joincond;
		c.publevel     = publevel;
		c.makabletopic = makabletopic;
		c.pic          = pic;
		c.entdate      = ts;
		c.upddate      = ts;
		c.updid        = mid;

		// コミュニティ登録
		jdbcManager.insert(c).execute();

		/* ■ コミュニティ参加者情報の登録 */
		ce              = new CommunityEnterant();
		ce.cid          = cid;
		ce.sno          = new BigDecimal("0");
		ce.mid          = mid;
		ce.joinstatus   = "1";
		ce.requireddate = ts;
		ce.requiredmsg  = "×";
		ce.entdate      = ts;
		ce.upddate      = ts;
		ce.updid        = mid;
		
		// コミュニティ参加者情報登録
		jdbcManager.insert(ce).execute();
		
		// 作成したコミュニティのＩＤを返す
		return cid;
	}

	// コミュニティ検索
	public List<BeanMap> selectCommunities(String title,String detail){
		// 後に検索用に使用予定。。
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("title", CmnUtility.replaceSql(title));
		params.put("detail", CmnUtility.replaceSql(detail));
		
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/searchCommunities.sql",params)
		.getResultList();
		return results;
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
}