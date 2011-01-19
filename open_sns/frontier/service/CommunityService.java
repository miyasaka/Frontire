package frontier.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Communities;
import frontier.entity.CommunityBbs;
import frontier.entity.CommunityEventInfo;
import frontier.entity.CommunityEvententryInfo;

public class CommunityService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public CommunityDto communityDto;
	@Resource
	protected JdbcManager jdbcManager;
	public List<BeanMap> results;
	public CommunityBbs c;
	public CommunityEventInfo cei;
	public CommunityEvententryInfo ceei;
	
	public Timestamp ts;
	public Integer[] dirs = {640,480,240,180,120,76,60,42};

	// コミュニティ情報の取得(communityDtoにセット)
	public void getComDt(String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("mid",userInfoDto.memberId);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComDt.sql",params)
		.getResultList();
		// 取得件数が１件の場合、コミュニティ情報を設定
		communityDto.makabletopic = results.get(0).get("makabletopic").toString();
		communityDto.comnm        = results.get(0).get("title").toString();
		communityDto.pic          = results.get(0).get("pic").toString();
		communityDto.joincond     = results.get(0).get("joincond").toString();
	}

	// コミュニティ詳細情報の取得
	public Map<String, Object> getComDtDetails(String cid){
		String detailhtml = "";
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		Map<String, Object> rtnval = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComDtDetails.sql",params)
		.getResultList().get(0);
		// サニタイジング処理、iモード絵文字変換
		detailhtml = rtnval.get("detail").toString();
		detailhtml = CmnUtility.htmlSanitizing(detailhtml);
		detailhtml = CmnUtility.replaceEmoji(detailhtml,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
		detailhtml = CmnUtility.convURL(detailhtml);
		// 追加
		rtnval.put("detailhtml",detailhtml);
		// 1件だけなのでget(0)して返す
		return rtnval;
	}

	// 参加メンバー一覧取得
	public List<BeanMap> selectMemList(String cid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("limit",limit);
		params.put("offset",offset);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selCMlist.sql",params)
		.getResultList();
		return results;
	}

	// 参加メンバー件数取得
	public Integer cntMemList(String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selCMlist.sql",params)
		.getResultList();
		return results.size();
	}

	// イベント、トピック一覧取得
	public List<BeanMap> selectBbsList(
		String cid,
		String entrytype,
		Integer offset,
		Integer limit
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("limit",limit);
		params.put("offset",offset);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsList.sql",params)
		.getResultList();
		return results;
	}
	
	// イベント情報一覧取得
	public List<BeanMap> selectEventList(
			String cid,
			String entrytype,
			Integer offset,
			Integer limit
		){
			Map<String, Object> params = new HashMap<String, Object>();
			// Mapオブジェクトに条件用パラメタを定義
			params.put("cid",cid);
			params.put("entrytype",entrytype);
			params.put("mid",userInfoDto.memberId);
			params.put("limit",limit);
			params.put("offset",offset);
			results = jdbcManager
			.selectBySqlFile(BeanMap.class,"/data/selEventList.sql",params)
			.getResultList();
			return results;
		}

	// イベント、トピック件数取得
	public Integer cntBbsList(String cid,String entrytype){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsList.sql",params)
		.getResultList();
		return results.size();
	}

	// 閲覧・親情報取得
	public List<BeanMap> getOyaView(
		String cid,
		String entrytype,
		String bbsid
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsOyaView.sql",params)
		.getResultList();
		for (BeanMap b : results) {
			// 画像・本文をHTMLタグ化し、put
			b.put("pichtml",CmnUtility.editimg(b,"pic1","pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_CONTENTS_ROOT));
			b.put("cmnthtml",decorateCmnt(CmnUtility.convURL(b.get("comment").toString())));
		}
		//setEmojiSanitizing("comment","cmnthtml",results);
		return results;
	}
	
	// イベント閲覧・親情報取得
	public List<BeanMap> getEventOyaView(
		String cid,
		String entrytype,
		String bbsid
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selEventOyaView.sql",params)
		.getResultList();
		for (BeanMap b : results) {
			// 画像・本文をHTMLタグ化し、put
			b.put("pichtml",CmnUtility.editimg(b,"pic1","pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_CONTENTS_ROOT));
			b.put("cmnthtml",decorateCmnt(CmnUtility.convURL(b.get("comment").toString())));
		}
		//setEmojiSanitizing("comment","cmnthtml",results);
		return results;
	}

	// 閲覧・コメント情報取得
	public List<BeanMap> getCmntView(
		String cid,
		String entrytype,
		String bbsid,
		Integer offset,
		Integer limit
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		// 最新のXX件を取得する方法
		if(limit!=0){
			params.put("offset",cntCmnt(cid,entrytype,bbsid)-offset-limit);
		} else {
			params.put("offset",offset);
		}
		params.put("limit",limit);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsCmntView.sql",params)
		.getResultList();
		for (BeanMap b : results) {
			// 画像・本文をHTMLタグ化し、put
			b.put("pichtml",CmnUtility.editimg(b,"pic1","pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_CONTENTS_ROOT));
			b.put("cmnthtml",decorateCmnt(CmnUtility.convURL(b.get("comment").toString())));
		}
		return results;
	}

	// 削除・選択コメント情報取得
	public List<BeanMap> getChkCmnt(
		String cid,
		String entrytype,
		String bbsid,
		List<String> chkcmnt
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		params.put("chkcmnt",chkcmnt);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsCmnt.sql",params)
		.getResultList();
		for (BeanMap b : results) {
			// 画像・本文をHTMLタグ化し、put
			b.put("pichtml",CmnUtility.editimg(b,"pic1","pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_CONTENTS_ROOT));
			b.put("cmnthtml",decorateCmnt(b.get("comment").toString()));
		}
		return results;
	}

	// 閲覧・コメント数取得
	public Integer cntCmnt(
		String cid,
		String entrytype,
		String bbsid
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		params.put("offset",0);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsCmntView.sql",params)
		.getResultList();
		return results.size();
	}

	// ■■■■■■↓↓↓↓登録・更新・削除系処理↓↓↓↓■■■■■■
	// トピックの登録
	public void insTopic(
		String cid,
		String title,
		String comment,
		FormFile picpath1,
		FormFile picpath2,
		FormFile picpath3,
		String picnote1,
		String picnote2,
		String picnote3
	) throws IOException, Exception{
		// 掲示板IDの取得
		Integer bbsid = Integer.valueOf(jdbcManager
		.selectBySql(String.class, "select coalesce(max(bbsid),0)+1 from community_bbs where cid = ?",cid)
		.getSingleResult());

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());
		String picnm1 = null;
		String picnm2 = null;
		String picnm3 = null;
		// 画像説明のセット
		String picnt1 = null;
		String picnt2 = null;
		String picnt3 = null;
		// 画像のアップロード(画像パス名の長さが0以上ならupload & 画像の説明)
		//null対策
		if(picpath1!=null){
			if(picpath1.getFileName().length()>0){
				picnm1 = picupload(cid,picpath1,bbsid,0,"1");
				picnt1 = picnote1;
			}
		}
		//null対策
		if(picpath2!=null){
			if(picpath2.getFileName().length()>0){
				picnm2 = picupload(cid,picpath2,bbsid,0,"2");
				picnt2 = picnote2;
			}
		}
		//null対策
		if(picpath3!=null){
			if(picpath3.getFileName().length()>0){
				picnm3 = picupload(cid,picpath3,bbsid,0,"3");
				picnt3 = picnote3;
			}
		}
		CommunityBbs c = new CommunityBbs();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.comno     = 0;
		c.entrytype = "1";
		c.title     = title;
		c.comment   = comment;
		c.entdate   = ts;
		c.upddate   = ts;
		c.mid       = userInfoDto.memberId;
		c.updid     = userInfoDto.memberId;
		c.pic1      = picnm1;
		c.pic2      = picnm2;
		c.pic3      = picnm3;
		c.picnote1  = picnt1;
		c.picnote2  = picnt2;
		c.picnote3  = picnt3;
		// トピック登録
		jdbcManager.updateBySqlFile("/data/insCommunityBbs.sql", c).execute();
	}
	
	// イベントの登録
	public void insEvent(
		String cid,
		String title,
		String comment,
		FormFile picpath1,
		FormFile picpath2,
		FormFile picpath3,
		String picnote1,
		String picnote2,
		String picnote3,
		String yearofevent,
		String monthofevent,
		String dayofevent,
		String eventnote,
		String locationnote,
		String deadlineyear,
		String deadlinemonth,
		String deadlineday,
		List<String> admid
	) throws IOException, Exception{
		// 掲示板IDの取得
		Integer bbsid = Integer.valueOf(jdbcManager
		.selectBySql(String.class, "select coalesce(max(bbsid),0)+1 from community_bbs where cid = ?",cid)
		.getSingleResult());
		logger.debug("******** サービス側イベント登録 *********");
		logger.debug("******** イベントpicpath1 *********"+picpath1);

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());

		String picnm1 = null;
		String picnm2 = null;
		String picnm3 = null;
		// 画像説明のセット
		String picnt1 = null;
		String picnt2 = null;
		String picnt3 = null;
		
		// 画像のアップロード(画像パス名の長さが0以上ならupload & 画像の説明)
		//null対策
		if(picpath1!=null){
			if(picpath1.getFileName().length()>0){
				picnm1 = picupload(cid,picpath1,bbsid,0,"1");
				picnt1 = picnote1;
			}
		}
		//null対策
		if(picpath2!=null){
			if(picpath2.getFileName().length()>0){
				picnm2 = picupload(cid,picpath2,bbsid,0,"2");
				picnt2 = picnote2;
			}
		}
		//null対策
		if(picpath3!=null){
			if(picpath3.getFileName().length()>0){
				picnm3 = picupload(cid,picpath3,bbsid,0,"3");
				picnt3 = picnote3;
			}
		}
		
		//月日０埋め変換処理
		String convEventMonth = "";
		String convEventDay = "";
		String convDeadlineMonth = "";
		String convDeadlineDay = "";
		convEventMonth = stringFormat("00",Integer.parseInt(monthofevent));
		convEventDay   = stringFormat("00",Integer.parseInt(dayofevent));
		if(!deadlinemonth.equals("")){
			convDeadlineMonth = stringFormat("00",Integer.parseInt(deadlinemonth));
		}
		if(!deadlineday.equals("")){
			convDeadlineDay   = stringFormat("00",Integer.parseInt(deadlineday));
		}
		
		CommunityBbs c = new CommunityBbs();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.comno     = 0;
		c.entrytype = "2";
		c.title     = title;
		c.comment   = comment;
		c.entdate   = ts;
		c.upddate   = ts;
		c.mid       = userInfoDto.memberId;
		c.updid     = userInfoDto.memberId;
		c.pic1      = picnm1;
		c.pic2      = picnm2;
		c.pic3      = picnm3;
		c.picnote1  = picnt1;
		c.picnote2  = picnt2;
		c.picnote3  = picnt3;

		// コミュニティ掲示板登録
		jdbcManager.updateBySqlFile("/data/insCommunityBbs.sql", c).execute();
		
		CommunityEventInfo cei = new CommunityEventInfo();
		//パラメタを定義
		cei.cid           = cid;
		cei.bbsid         = bbsid;
		cei.eventYear     = yearofevent;
		cei.eventMonth    = convEventMonth;
		cei.eventDay      = convEventDay;
		cei.eventNote     = eventnote;
		cei.eventareaNote = locationnote;
		cei.limitYear     = deadlineyear;
		cei.limitMonth    = convDeadlineMonth;
		cei.limitDay      = convDeadlineDay;

		// コミュニティイベント情報登録
		jdbcManager.updateBySqlFile("/data/insCommunityEventInfo.sql", cei).execute();

		CommunityEvententryInfo ceei = new CommunityEvententryInfo();
		//パラメタを設定
		ceei.cid     = cid;
		ceei.bbsid   = bbsid;
		ceei.mid     = userInfoDto.memberId;
		ceei.entid   = userInfoDto.memberId;
		ceei.entdate = ts;
		ceei.updid   = userInfoDto.memberId;
		ceei.upddate = ts;
		//コミュニティイベント参加者情報登録
		jdbcManager.updateBySqlFile("/data/insCommunityEventEntryInfo.sql", ceei).execute();

		if(admid.size()>0){
			//イベント参加者登録（選択したメンバーの登録）
			for (int i=0;i<admid.size();i++){
				CommunityEvententryInfo ceei2 = new CommunityEvententryInfo();
				// パラメタを定義
				ceei2.cid       = cid;
				ceei2.bbsid     = bbsid;
				ceei2.mid       = admid.get(i);
				ceei2.entid     = admid.get(i);
				ceei2.entdate   = ts;
				ceei2.updid     = admid.get(i);
				ceei2.upddate   = ts;		
				
				// イベント参加情報登録
				jdbcManager.updateBySqlFile("/data/insCommunityEventEntryInfo.sql", ceei2).execute();
			}
		}
	}
	
	
	
	
	
	

	// トピックの編集
	public void editTopic(
		String cid,
		String bbsid,
		String entrytype,
		Integer comno,
		String title,
		String comment,
		FormFile picpath1,
		FormFile picpath2,
		FormFile picpath3,
		String picnote1,
		String picnote2,
		String picnote3
	) throws IOException, Exception{
		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());
		
		Map<String, Object> params = new HashMap<String, Object>();
		// トピック情報の取得
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsOyaView.sql",params)
		.getResultList();

		// 画像パスのセット
		String picnm1 = results.get(0).get("pic1")!=null?results.get(0).get("pic1").toString():null;
		String picnm2 = results.get(0).get("pic2")!=null?results.get(0).get("pic2").toString():null;
		String picnm3 = results.get(0).get("pic3")!=null?results.get(0).get("pic3").toString():null;
		logger.debug("******** picnm1 *********"+picnm1);
		logger.debug("******** picpath1 *********"+picpath1);
		// 画像説明のセット
		String picnt1 = "";
		String picnt2 = "";
		String picnt3 = "";
		if(picnm1 != null){picnt1 = picnote1;}
		if(picnm2 != null){picnt2 = picnote2;}
		if(picnm3 != null){picnt3 = picnote3;}
		// 画像のアップロード(画像パス名の長さが0以上ならupload & 画像の説明)
		//null対策
		if(picpath1!=null){
			if(picpath1.getFileName().length()>0){
				picnm1 = picupload(cid,picpath1,Integer.valueOf(bbsid),0,"1");
				picnt1 = picnote1;
			}
		}
		//null対策
		if(picpath2!=null){
			if(picpath2.getFileName().length()>0){
				picnm2 = picupload(cid,picpath2,Integer.valueOf(bbsid),0,"2");
				picnt2 = picnote2;
			}
		}
		//null対策
		if(picpath3!=null){
			if(picpath3.getFileName().length()>0){
				picnm3 = picupload(cid,picpath3,Integer.valueOf(bbsid),0,"3");
				picnt3 = picnote3;
			}
		}
		logger.debug("★★★★★★★★★★★★★★★");

		CommunityBbs c = new CommunityBbs();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = Integer.valueOf(bbsid);
		c.comno     = comno;
		c.entrytype = entrytype;
		c.title     = title;
		c.comment   = comment;
		c.entdate   = ts;
		c.upddate   = ts;
		c.mid       = userInfoDto.memberId;
		c.updid     = userInfoDto.memberId;
		c.pic1      = picnm1;
		c.pic2      = picnm2;
		c.pic3      = picnm3;
		c.picnote1  = picnt1;
		c.picnote2  = picnt2;
		c.picnote3  = picnt3;

		// トピック情報更新
		jdbcManager
		.updateBySqlFile("/data/updCommunityBbs.sql", c)
		.execute();
	}
	
	// イベントの編集
	public void editEvent(
		String cid,
		String bbsid,
		String entrytype,
		Integer comno,
		String title,
		String comment,
		FormFile picpath1,
		FormFile picpath2,
		FormFile picpath3,
		String picnote1,
		String picnote2,
		String picnote3,
		String yearofevent,
		String monthofevent,
		String dayofevent,
		String eventnote,
		String locationnote,
		String deadlineyear,
		String deadlinemonth,
		String deadlineday,
		List<String> addmidList,
		String allValFlg
	) throws IOException, Exception{
		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());
		
		Map<String, Object> params = new HashMap<String, Object>();
		// イベント情報の取得
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsOyaView.sql",params)
		.getResultList();

		// 画像パスのセット
		String picnm1 = results.get(0).get("pic1")!=null?results.get(0).get("pic1").toString():null;
		String picnm2 = results.get(0).get("pic2")!=null?results.get(0).get("pic2").toString():null;
		String picnm3 = results.get(0).get("pic3")!=null?results.get(0).get("pic3").toString():null;
		// 画像説明のセット
		String picnt1 = "";
		String picnt2 = "";
		String picnt3 = "";
		if(picnm1 != null){picnt1 = picnote1;}
		if(picnm2 != null){picnt2 = picnote2;}
		if(picnm3 != null){picnt3 = picnote3;}
		// 画像のアップロード(画像パス名の長さが0以上ならupload & 画像の説明)
		//null対策
		if(picpath1!=null){
			if(picpath1.getFileName().length()>0){
				picnm1 = picupload(cid,picpath1,Integer.valueOf(bbsid),0,"1");
				picnt1 = picnote1;
			}
		}
		//null対策
		if(picpath2!=null){
			if(picpath2.getFileName().length()>0){
				picnm2 = picupload(cid,picpath2,Integer.valueOf(bbsid),0,"2");
				picnt2 = picnote2;
			}
		}
		//null対策
		if(picpath3!=null){
			if(picpath3.getFileName().length()>0){
				picnm3 = picupload(cid,picpath3,Integer.valueOf(bbsid),0,"3");
				picnt3 = picnote3;
			}
		}
		//月日０埋め変換処理
		String convEventMonth = "";
		String convEventDay = "";
		String convDeadlineMonth = "";
		String convDeadlineDay = "";
		convEventMonth = stringFormat("00",Integer.parseInt(monthofevent));
		convEventDay   = stringFormat("00",Integer.parseInt(dayofevent));
		if(!deadlinemonth.equals("")){
			convDeadlineMonth = stringFormat("00",Integer.parseInt(deadlinemonth));
		}
		if(!deadlineday.equals("")){
			convDeadlineDay   = stringFormat("00",Integer.parseInt(deadlineday));
		}

		CommunityBbs c = new CommunityBbs();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = Integer.valueOf(bbsid);
		c.comno     = comno;
		c.entrytype = entrytype;
		c.title     = title;
		c.comment   = comment;
		c.entdate   = ts;
		c.upddate   = ts;
		c.mid       = userInfoDto.memberId;
		c.updid     = userInfoDto.memberId;
		c.pic1      = picnm1;
		c.pic2      = picnm2;
		c.pic3      = picnm3;
		c.picnote1  = picnt1;
		c.picnote2  = picnt2;
		c.picnote3  = picnt3;

		// イベント情報更新
		jdbcManager
		.updateBySqlFile("/data/updCommunityBbs.sql", c)
		.execute();
		
		CommunityEventInfo cei = new CommunityEventInfo();
		//パラメタを定義
		cei.cid           = cid;
		cei.bbsid         = Integer.valueOf(bbsid);
		cei.eventYear     = yearofevent;
		cei.eventMonth    = convEventMonth;
		cei.eventDay      = convEventDay;
		cei.eventNote     = eventnote;
		cei.eventareaNote = locationnote;
		cei.limitYear     = deadlineyear;
		cei.limitMonth    = convDeadlineMonth;
		cei.limitDay      = convDeadlineDay;

		// コミュニティイベント情報更新
		jdbcManager
		.updateBySqlFile("/data/updCommunityEventInfo.sql", cei)
		.execute();
		
		if(addmidList.size()>0 || allValFlg=="1"){
			// イベント参加者情報削除
			CommunityEvententryInfo delc = new CommunityEvententryInfo();
			// パラメタを定義
			delc.cid       = cid;
			delc.bbsid     = Integer.valueOf(bbsid);
			jdbcManager.updateBySqlFile("/data/delCommunityEventEntryInfo3.sql", delc).execute();

			//イベント参加者（自分）登録
			CommunityEvententryInfo ceei3 = new CommunityEvententryInfo();
			// パラメタを定義
			ceei3.cid       = cid;
			ceei3.bbsid     = Integer.valueOf(bbsid);
			ceei3.mid       = userInfoDto.memberId;
			ceei3.entid     = userInfoDto.memberId;
			ceei3.entdate   = ts;
			ceei3.updid     = userInfoDto.memberId;
			ceei3.upddate   = ts;		
			// イベント参加情報登録（自分）
			jdbcManager.updateBySqlFile("/data/insCommunityEventEntryInfo.sql", ceei3).execute();

			
			//イベント参加者登録（選択したメンバーの登録）
			for (int i=0;i<addmidList.size();i++){
				CommunityEvententryInfo ceei2 = new CommunityEvententryInfo();
				// パラメタを定義
				ceei2.cid       = cid;
				ceei2.bbsid     = Integer.valueOf(bbsid);
				ceei2.mid       = addmidList.get(i);
				ceei2.entid     = addmidList.get(i);
				ceei2.entdate   = ts;
				ceei2.updid     = addmidList.get(i);
				ceei2.upddate   = ts;		
				
				// イベント参加情報登録
				jdbcManager.updateBySqlFile("/data/insCommunityEventEntryInfo.sql", ceei2).execute();
			}
		}
	}

	// コメントの登録
	public void insCmnt(
		String cid,
		Integer bbsid,
		String comment,
		FormFile picpath1,
		FormFile picpath2,
		FormFile picpath3,
		String picnote1,
		String picnote2,
		String picnote3,
		String entrytype
	) throws IOException, Exception{
		// 掲示板IDの取得
		Integer comno = Integer.valueOf(jdbcManager
		.selectBySql(String.class, "select coalesce(max(comno),0)+1 from community_bbs where cid = ? and bbsid = ?",cid,bbsid)
		.getSingleResult());

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());

		String picnm1 = null;
		String picnm2 = null;
		String picnm3 = null;
		//画像説明セット
		String picnt1 = null;
		String picnt2 = null;
		String picnt3 = null;
		
		// 画像のアップロード(画像パス名の長さが0以上ならupload & 画像の説明)
		//null対策
		if(picpath1!=null){
			if(picpath1.getFileName().length()>0){
				picnm1 = picupload(cid,picpath1,bbsid,comno,"1");
				picnt1 = picnote1;
			}
		}
		//null対策
		if(picpath2!=null){
			if(picpath2.getFileName().length()>0){
				picnm2 = picupload(cid,picpath2,bbsid,comno,"2");
				picnt2 = picnote2;
			}
		}
		//null対策
		if(picpath3!=null){
			if(picpath3.getFileName().length()>0){
				picnm3 = picupload(cid,picpath3,bbsid,comno,"3");
				picnt3 = picnote3;
			}
		}

		CommunityBbs c = new CommunityBbs();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.comno     = comno;
		c.entrytype = entrytype;
		c.title     = "";
		c.comment   = comment;
		c.entdate   = ts;
		c.upddate   = ts;
		c.mid       = userInfoDto.memberId;
		c.updid     = userInfoDto.memberId;
		c.pic1      = picnm1;
		c.pic2      = picnm2;
		c.pic3      = picnm3;
		c.picnote1  = picnt1;
		c.picnote2  = picnt2;
		c.picnote3  = picnt3;
		// コミュニティ参加情報登録
		jdbcManager.updateBySqlFile("/data/insCommunityBbs.sql", c).execute();
	}
	
	// イベント参加者の登録
	public void insJoin(
		String cid,
		Integer bbsid
	) throws IOException, Exception{

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());

		CommunityEvententryInfo c = new CommunityEvententryInfo();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.mid       = userInfoDto.memberId;
		c.entid     = userInfoDto.memberId;
		c.entdate   = ts;
		c.updid     = userInfoDto.memberId;
		c.upddate   = ts;		
		
		// コミュニティ参加情報登録
		jdbcManager.updateBySqlFile("/data/insCommunityEventEntryInfo.sql", c).execute();
	}
	
	// イベント参加のキャンセル登録
	public void delJoin(
		String cid,
		Integer bbsid
	) throws IOException, Exception{

		CommunityEvententryInfo c = new CommunityEvententryInfo();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.mid       = userInfoDto.memberId;
		
		// イベント参加のキャンセル
		jdbcManager.updateBySqlFile("/data/delCommunityEventEntryInfo.sql", c).execute();
	}


	// 掲示板削除
	public void delbbs(String cid,Integer bbsid){
		CommunityBbs c = new CommunityBbs();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.mid       = userInfoDto.memberId;
		
		// 掲示板の削除(delflgに1を立てる)
		jdbcManager.updateBySqlFile("/data/delBbs.sql", c).execute();
	}
	
	// コメント削除
	public void delcmnt(String cid,Integer bbsid,List<String> chkcmnt){
		CommunityBbs c = new CommunityBbs();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.chkcmnt   = chkcmnt;
		c.mid       = userInfoDto.memberId;
		// コメントの削除(delflgに1を立てる)
		jdbcManager.updateBySqlFile("/data/delBbsCmnt.sql", c).execute();
	}
	
	// 画像の削除
	public void delpic(
		String cid,
		String bbsid,
		String entrytype,
		String picno
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// トピック情報の取得
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("entrytype",entrytype);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selBbsOyaView.sql",params)
		.getResultList();

		// 現在時間の取得(更新用)
		ts = new Timestamp ((new java.util.Date()).getTime());

		// 画像のセット
		String pic1 = results.get(0).get("pic1")!=null?results.get(0).get("pic1").toString():null;
		String pic2 = results.get(0).get("pic2")!=null?results.get(0).get("pic2").toString():null;
		String pic3 = results.get(0).get("pic3")!=null?results.get(0).get("pic3").toString():null;
		// 画像説明のセット
		String picnote1 = results.get(0).get("picnote1")!=null?results.get(0).get("picnote1").toString():null;
		String picnote2 = results.get(0).get("picnote2")!=null?results.get(0).get("picnote2").toString():null;
		String picnote3 = results.get(0).get("picnote3")!=null?results.get(0).get("picnote3").toString():null;

		
		// 削除する画像パス、画像説明にNULLをセット
		if(picno=="1"){pic1=null;picnote1=null;}
		if(picno=="2"){pic2=null;picnote2=null;}
		if(picno=="3"){pic3=null;picnote3=null;}
		
		/* ■ トピックの更新 */
		// パラメタセット
		c              = new CommunityBbs();
		c.cid          = cid;
		c.bbsid        = Integer.valueOf(bbsid);
		c.entrytype    = entrytype;
		c.comno        = 0;
		c.title        = results.get(0).get("title").toString();
		c.comment      = results.get(0).get("comment").toString();
		c.pic1         = pic1;
		c.pic2         = pic2;
		c.pic3         = pic3;
		c.picnote1     = picnote1;
		c.picnote2     = picnote2;
		c.picnote3     = picnote3;
		c.upddate      = ts;
		c.updid        = userInfoDto.memberId;
		
		// トピック情報更新
		jdbcManager
		.updateBySqlFile("/data/updCommunityBbs.sql", c)
		.execute();
	}

	// 本文の装飾
	private String decorateCmnt(String cmnt){
		//サニタイジング
		cmnt = CmnUtility.htmlSanitizing(cmnt);
		//逆サニタイジング
		cmnt = CmnUtility.reSanitizing(cmnt);
		//URLを<a>タグに変換
		//cmnt = CmnUtility.convURL(cmnt);
		//YouTubeタグ変換
		cmnt = CmnUtility.replaceYoutube(cmnt);
		//googleMapタグ変換
		cmnt = CmnUtility.replaceGoogleMap(cmnt);
		//絵文字装飾
		cmnt = CmnUtility.replaceEmoji(cmnt,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
		return cmnt;
	}

	// 画像のアップロード処理
	public String picupload(
		String cid,
		FormFile picpath,
		Integer bbsid,
		Integer comno,
		String picno
	) throws IOException, Exception{
		// ディレクトリパス
		String Path = appDefDto.FP_CMN_CONTENTS_DIR+"img/com/"+cid;
		// ディレクトリ
		String Dir = "bbs";
		// アップロード画像名取得(yyyymmddhh24miss)
		String picnm = bbsid.toString() + "_" + comno.toString() + "_" + picno + "_" + CmnUtility.getToday("yyyyMMddHHmmss") + ".jpg";
		// 登録用画像パス設定
		String pic = "/img/com/" + cid + "/" + Dir + "/dir/" + picnm;
		// ディレクトリ作成
		CmnUtility.makeDir(Path, Dir);
		// 画像ファイルのアップロード(ベースはpic640フォルダ)
		CmnUtility.uploadFile(Path + "/" + Dir + "/pic640/" + picnm, picpath);
		// ファイルのコピー&リサイズ(ディレクトリ数分ループ)
		for (Integer i : dirs) {
			CmnUtility.resize(Path + "/" + Dir + "/" + "pic" + i.toString() + "/" + picnm,i,Path + "/" + Dir + "/pic640/" + picnm , appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		}
		return pic;
	}
	
	//フォーマット設定
	private String stringFormat(String format,int val){
	    DecimalFormat df = new DecimalFormat(format);

		return df.format(val);
	}
	
	// イベント参加者情報取得
	public List<BeanMap> getEventJoin(
		String cid,
		String bbsid
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("mid",userInfoDto.memberId);
		params.put("bbsid",bbsid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selCommunityEventJoinDt.sql",params)
		.getResultList();
		//setEmojiSanitizing("comment","cmnthtml",results);
		return results;
	}

	
	//絵文字装飾とサニタイジング
	//private void setEmojiSanitizing(String colmun,String viewColumn,List<BeanMap> results){
	//	String txt = "";
	//	
	//	//絵文字の装飾&サニタイジング
	//	for (int i=0;i<results.size();i++){
	//		//コメントを一時格納
	//		txt = (String)results.get(i).get(colmun);
	//		
	//		//サニタイジング
	//		txt = CmnUtility.htmlSanitizing(txt);
	//		
	//		//URLを<a>タグに変換
	//		txt = CmnUtility.convURL(txt);
	//		
	//	    //絵文字装飾
	//		txt = CmnUtility.replaceEmoji(txt,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
	//	  	
	//	  	//BeanMapへ格納
	//	  	results.get(i).put(viewColumn, txt);
	//	}		
	//}
	

	// イベント参加メンバー一覧取得
	public List<BeanMap> selectEventMemList(String cid,String bbsid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("bbsid",bbsid);
		params.put("limit",limit);
		params.put("offset",offset);

		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComEventMlist.sql",params)
		.getResultList();
		return results;
	}
	
	// イベント参加メンバー一覧取得（自分以外）
	public List<BeanMap> selectEventMemList2(String cid,String bbsid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("bbsid",bbsid);
		params.put("mid",userInfoDto.memberId);

		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComEventMlist2.sql",params)
		.getResultList();
		return results;
	}

	// イベント参加メンバー件数取得
	public Integer cntEventMemList(String cid,String bbsid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("bbsid",bbsid);

		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComEventMlist.sql",params)
		.getResultList();
		return results.size();
	}
	
	// 削除・選択メンバー情報取得
	public List<BeanMap> getChkMem(
		String cid,
		String entrytype,
		String bbsid,
		List<String> chkmem
	){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("bbsid",bbsid);
		params.put("chkmem",chkmem);
		
		logger.debug("************* chkmem *****************"+chkmem);
		
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selEventMem.sql",params)
		.getResultList();
		logger.debug("************* resultsだよ *****************"+results);
		//for (BeanMap b : results) {
		//	// 画像・本文をHTMLタグ化し、put
		//	b.put("pichtml",CmnUtility.editimg(b,"pic1","pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_CONTENTS_ROOT));
		//	b.put("cmnthtml",decorateCmnt(b.get("comment").toString()));
		//}
		return results;
	}
	
	// イベント参加のキャンセル登録
	public void delJoinList(
		String cid,
		Integer bbsid,
		List<String> chkmem
	) throws IOException, Exception{

		CommunityEvententryInfo c = new CommunityEvententryInfo();
		// パラメタを定義
		c.cid       = cid;
		c.bbsid     = bbsid;
		c.chkmem    = chkmem;
		//params.put("chkcmnt",chkcmnt);
		
		// イベント参加のキャンセル
		jdbcManager.updateBySqlFile("/data/delCommunityEventEntryInfo2.sql", c).execute();
	}
	
	// 参加メンバー一覧取得（自分以外）
	public List<BeanMap> selectAddMemList(String cid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("mid",userInfoDto.memberId);
		params.put("limit",limit);
		params.put("offset",offset);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selAddCMlist.sql",params)
		.getResultList();
		return results;
	}

	// 参加メンバー件数取得（自分以外）
	public Integer cntAddMemList(String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("cid",cid);
		params.put("mid",userInfoDto.memberId);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selAddCMlist.sql",params)
		.getResultList();
		return results.size();
	}
	
	// メンバーデータ取得
	public List<BeanMap> selectFriend(String mid,String cid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("cid",cid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selComMem.sql",params)
		.getResultList();
		return results;		
	}
	
	//管理人変更
	public void updateManager(String mid,String cid){
		Communities c = new Communities();
		//キー設定
		c.cid = cid;
		c.admmid = mid;
		c.upddate = new Timestamp ((new java.util.Date()).getTime());
		c.updid = mid;
		jdbcManager.update(c).includes("admmid","upddate","updid").execute();
	}


}