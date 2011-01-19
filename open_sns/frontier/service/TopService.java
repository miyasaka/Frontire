package frontier.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import twitter4j.Paging;
import frontier.entity.Frontiershout;
import frontier.entity.Memberrsslist;
import frontier.entity.MembersetupInfo;
import frontier.dto.AppDefDto;
import frontier.service.CommonService;

public class TopService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	public JdbcManager jdbcManager;

	public Frontiershout fs;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected CommonService commonService;

	public BeanMap selMyPhoto(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyPhoto.sql",mid)
		.getSingleResult();
	}
	protected List<BeanMap> results;


	public List<BeanMap> selMyUpdateInfo(String mid,Integer limit,Object sortcd){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("limitdate",appDefDto.FP_MY_DATE_PGMAX);
		params.put("sortcd",sortcd);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyUpdateInfo.sql",params)
			.limit(limit)
			.getResultList();
	}

	public BeanMap selDefaultSetting(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selDefaultSetting.sql",mid)
		.getSingleResult();
	}

	public List<BeanMap> selDiaryNewList(String mid,Integer limit,Object sortcd,List<Object> idlist){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("sortcd",sortcd);
		params.put("limitcount",appDefDto.FP_MY_LIST_PGMAX);
		params.put("limitdate",appDefDto.FP_MY_DATE_PGMAX);
		params.put("idlist", idlist);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendNewDiary.sql",params)
			.limit(limit)
			.getResultList();
	}

	//F Shout
	public List<BeanMap> selFShoutList(String mid,Integer limit,Integer cntFlg,Integer fsoffset){
		Map<String, Object> params = new HashMap<String, Object>();
		//cntFlg 0:limit設定無し、1:limit設定有り
		// Mapオブジェクトに条件用パラメタを定義
		// midよりグループ＋フォローしているメンバーのmid取得
		List<BeanMap> lbm = commonService.getMidList("0", mid);
		List<String> midlist = new ArrayList<String>();
		// 最初に自分のmid追加
		midlist.add(mid);
		// 取得したmidリストが0件以上だったらリスト作成
		if(lbm.size() > 0){
			for(BeanMap a:lbm){
				midlist.add(a.get("mid").toString());
			}
		}
		params.put("mid",midlist);
		params.put("limitdate",appDefDto.FP_MY_FSHOUTLIST_DATE_PGMAX);
		if(cntFlg==1){
			params.put("offset",fsoffset);
			return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShout.sql",params)
				.limit(limit)
				.getResultList();
		}else{
			return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShout.sql",params)
			.getResultList();
		}
	}

	//F Shout件数
	public Integer selFShoutListCnt(String mid,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		// midよりグループ＋フォローしているメンバーのmid取得
		List<BeanMap> lbm = commonService.getMidList("0", mid);
		List<String> midlist = new ArrayList<String>();
		// 最初に自分のmid追加
		midlist.add(mid);
		// 取得したmidリストが0件以上だったらリスト作成
		if(lbm.size() > 0){
			for(BeanMap a:lbm){
				midlist.add(a.get("mid").toString());
			}
		}
		params.put("mid",midlist);
		params.put("limitcount",appDefDto.FP_MY_LIST_PGMAX);
		params.put("limitdate",appDefDto.FP_MY_FSHOUTLIST_DATE_PGMAX);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShout.sql",params)
			.limit(limit)
			.getResultList().size();
	}

	//F Shout(最新コメントの時間取得)
	public List<BeanMap> selFShoutMaxList(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		//cntFlg 0:limit設定無し、1:limit設定有り
		// Mapオブジェクトに条件用パラメタを定義
		// midよりグループ＋フォローしているメンバーのmid取得
		List<BeanMap> lbm = commonService.getMidList("0", mid);
		List<String> midlist = new ArrayList<String>();
		// 最初に自分のmid追加
		midlist.add(mid);
		// 取得したmidリストが0件以上だったらリスト作成
		if(lbm.size() > 0){
			for(BeanMap a:lbm){
				midlist.add(a.get("mid").toString());
			}
		}
		params.put("mid",midlist);

		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShoutMaxEntdate.sql",params)
			.getResultList();
	}


	// ■FShout一覧 TOP、自分宛一覧取得(もっと見る対応)
	//   一覧 or 自分宛、PC or 携帯、使い分け・文字装飾込版
	// ------------------------- //
	//     type:                 //
	// ------------------------- //
	//       0:一覧(PC)          //
	//       1:一覧(携帯)        //
	//       2:自分宛(PC)        //
	//       3:自分宛(携帯)      //
	// ------------------------- //
	public List<BeanMap> selMyFShoutTopList(String type,String mid,String period,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("gmidlist",getGList(mid));
		params.put("fmidlist",getFList(mid));
		// キーワードフラグ(0:なし、1:あり)
		String kwdflg = "0";
		// 期間フラグ(0:なし、1:あり)※ここでは期間縛りあり
		String periodflg = "1";
		// 自分宛(2 or 3)の場合、キーワードの追加
		if(type.equals("2") || type.equals("3")){
			kwdflg = "1";
			String kwd = "";
			// 検索条件作成 [@:frontierドメイン,メンバーID]
			try{
				kwd = "[@:" + appDefDto.FP_CMN_HOST_NAME + "," + Integer.valueOf(mid.substring(1)).intValue() + "]";
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 追加
			params.put("kwd",kwd);
		}
		// 追加
		params.put("kwdflg",kwdflg);
		params.put("periodflg",periodflg);
		params.put("period",period);
		//List<BeanMap> lbm = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyFShoutList.sql",params).getResultList();
		// PC、携帯によって装飾タイプを変える 0:PC、1:携帯
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyFShoutListUpdate.sql",params).getResultList();
	}

	// メンバーのフォローしているmidリストを返す
	private List<String> getFList(String mid){
		List<String> flist = new ArrayList<String>();
		// 0件対応
		flist.add("");
		// midよりフォローメンバーID取得
		List<BeanMap> lbmf = commonService.getMidList("2", mid);
		// リスト作成
		for(BeanMap a:lbmf){
			flist.add(a.get("mid").toString());
		}
		return flist;
	}

	// メンバーのグループのmidリストを返す
	private List<String> getGList(String mid){
		List<String> glist = new ArrayList<String>();
		// midよりグループメンバーID取得
		List<BeanMap> lbmg = commonService.getMidList("1", mid);
		// mid追加
		glist.add(mid);
		// リスト作成
		for(BeanMap a:lbmg){
			glist.add(a.get("mid").toString());
		}
		return glist;
	}



	//F Shout(最新コメント取得)
	public List<BeanMap> selFShoutUpdateList(String mid,String maxEntdate){
		Map<String, Object> params = new HashMap<String, Object>();
		//cntFlg 0:limit設定無し、1:limit設定有り
		// Mapオブジェクトに条件用パラメタを定義
		// midよりグループ＋フォローしているメンバーのmid取得
		List<BeanMap> lbm = commonService.getMidList("0", mid);
		List<String> midlist = new ArrayList<String>();
		// 最初に自分のmid追加
		midlist.add(mid);
		// 取得したmidリストが0件以上だったらリスト作成
		if(lbm.size() > 0){
			for(BeanMap a:lbm){
				midlist.add(a.get("mid").toString());
			}
		}
		params.put("mid",midlist);
		params.put("maxentdate",maxEntdate);

		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShoutUpdate.sql",params)
			.getResultList();
	}

	public List<BeanMap> selTopicNewList(String mid,Integer limit,Object sortcd){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("limitdate",appDefDto.FP_MY_DATE_PGMAX);
		params.put("sortcd",sortcd);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewTopicData.sql",params)
			.limit(limit)
			.getResultList();
	}

	// フォトの新着データ取得(グループ＋フォロー)
	public List<BeanMap> selFriendUpdateInfo(List<Object> glist,List<Object> flist,Integer limit,Object sortcd){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listgmid",glist);
		params.put("listfmid",flist);
		params.put("sortcd",sortcd);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFriendUpdateInfo.sql",params)
			.limit(limit)
			.getResultList();
	}

	// 日記の新着コメント
	public List<BeanMap> selNewDiaryCommentList(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewDiaryCommentList.sql",mid)
			.getResultList();
	}

	// フォトの新着コメント
	public List<BeanMap> selNewPhotoCommentList(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewPhotoCommentList.sql",mid)
			.getResultList();
	}

	// 新規フォロー
	public List<BeanMap> selNewFollowList(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFollowList.sql",mid)
			.getResultList();
	}

	//RSSニュースを登録
	public void insRss(String mid,String rssurl) {
		Memberrsslist entryRss = new Memberrsslist();
		entryRss.mid      = mid;
		entryRss.rssurl = rssurl;

		jdbcManager.updateBySqlFile("/data/insMemberRssList.sql", entryRss)
		.execute();
	}

	//RSSニュースデータ取得
	public List<BeanMap> selRss(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMemberRssList.sql",mid)
			.getResultList();
	}

	//RSSニュースの削除
	public void delRss(String mid,Integer no) {
		Memberrsslist rssitem = new Memberrsslist();
		rssitem.mid  = mid;
		rssitem.no = no;

		jdbcManager.updateBySqlFile("/data/delMemberRssList.sql", rssitem)
		.execute();
	}

	//Frontier Netデータ取得
	public List<BeanMap> selFNet(){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFrontierNet.sql")
			.getResultList();
	}

	//プロフィールデータ取得
	public List<BeanMap> selProfile(String mid){

		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		return jdbcManager
			.selectBySqlFile(BeanMap.class,"/data/selprofile.sql",params)
			.getResultList();

	}

	//プロフィールデータ取得(FShout)
	public List<BeanMap> selProfileFShout(String frontierdomain,Object mid){

		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("frontierdomain",frontierdomain);
		return jdbcManager
			.selectBySqlFile(BeanMap.class,"/data/selprofileFShout.sql",params)
			.getResultList();

	}

	//メンバー設定情報取得
	public MembersetupInfo selMemberSetting(String mid){
		return jdbcManager.from(MembersetupInfo.class).id(mid).getSingleResult();
	}

	//RSSデータ取得
	public Memberrsslist selRssData(String mid,Integer no){
		return jdbcManager.from(Memberrsslist.class).where("mid = ? and no = ?",mid,no).getSingleResult();
	}

	public void updateSetting(String mid,String type,Integer count,String sortcd){
		MembersetupInfo mi = new MembersetupInfo();
		String Column =null;
		mi.mid=mid;
		if(type.equals("myPhoto")){
			mi.dispnumMyphoto=count;
			Column="dispnumMyphoto";
		}else if(type.equals("myUpdate")){
			mi.dispnumMy = count;
			Column="dispnumMy";
		}else if(type.equals("myUpdateSort")){
			mi.sortitemMy = sortcd;
			Column="sortitemMy";
		}else if(type.equals("community")){
			mi.dispnumCom = count;
			Column="dispnumCom";
		}else if(type.equals("communityUpdateSort")){
			mi.sortitemCombbs = sortcd;
			Column="sortitemCombbs";
		}else if(type.equals("communityUpdate")){
			mi.dispnumCombbs = count;
			Column="dispnumCombbs";
		}else if(type.equals("diaryUpdate")){
			mi.dispnumMemdiary = count;
			Column="dispnumMemdiary";
		}else if(type.equals("diaryUpdateSort")){
			mi.sortitemMemdiary = sortcd;
			Column="sortitemMemdiary";
		}else if(type.equals("memberUpdate")){
			mi.dispnumMemupdate = count;
			Column="dispnumMemupdate";
		}else if(type.equals("memberUpdateSort")){
			mi.sortitemMemupdate = sortcd;
			Column="sortitemMemupdate";
		}else if(type.equals("schedule")){
			mi.dispnumCalendar = count;
			Column="dispnumCalendar";
		}else if(type.equals("FShout")){
			mi.dispnumFshout = count;
			Column="dispnumFshout";
		}else if(type.equals("group")){
			mi.dispnumGroup = count;
			Column="dispnumGroup";
		}else if(type.equals("followyou")){
			mi.dispnumFollowyou = count;
			Column="dispnumFollowyou";
		}else if(type.equals("followme")){
			mi.dispnumFollowme = count;
			Column="dispnumFollowme";
		}

		if(Column!=null){
			jdbcManager.update(mi).includes(Column).execute();
		}
	}

	public void updatePosition(String mid,String type,String pos){
		//現在の設定情報を取得
		MembersetupInfo mi = selMemberSetting(mid);

		String Column = null;
		String Column2 = null;
		mi.mid = mid;

		// ================= 中央の列 =================
		if(type.equals("communityUpdate")){
			if(mi.dispposDiaryupdate.equals(pos)){
				Column2 = "dispposDiaryupdate";
				mi.dispposDiaryupdate=mi.dispposComupdate;
			}else if(mi.dispposMemupdate.equals(pos)){
				Column2 = "dispposMemupdate";
				mi.dispposMemupdate=mi.dispposComupdate;
			}else if(mi.dispposFshout.equals(pos)){
				Column2 = "dispposFshout";
				mi.dispposFshout=mi.dispposComupdate;
			}
			mi.dispposComupdate = pos;
			Column="dispposComupdate";
		}else if(type.equals("diaryUpdate")){
			if(mi.dispposComupdate.equals(pos)){
				Column2 = "dispposComupdate";
				mi.dispposComupdate=mi.dispposDiaryupdate;
			}else if(mi.dispposMemupdate.equals(pos)){
				Column2 = "dispposMemupdate";
				mi.dispposMemupdate=mi.dispposDiaryupdate;
			}else if(mi.dispposFshout.equals(pos)){
				Column2 = "dispposFshout";
				mi.dispposFshout=mi.dispposDiaryupdate;
			}
			mi.dispposDiaryupdate = pos;
			Column="dispposDiaryupdate";
		}else if(type.equals("memberUpdate")){
			if(mi.dispposComupdate.equals(pos)){
				Column2 = "dispposComupdate";
				mi.dispposComupdate=mi.dispposMemupdate;
			}else if(mi.dispposDiaryupdate.equals(pos)){
				Column2 = "dispposDiaryupdate";
				mi.dispposDiaryupdate=mi.dispposMemupdate;
			}else if(mi.dispposFshout.equals(pos)){
				Column2 = "dispposFshout";
				mi.dispposFshout=mi.dispposMemupdate;
			}
			mi.dispposMemupdate = pos;
			Column="dispposMemupdate";
		}else if(type.equals("FShout")){
			if(mi.dispposDiaryupdate.equals(pos)){
				Column2 = "dispposDiaryupdate";
				mi.dispposDiaryupdate=mi.dispposFshout;
			}else if(mi.dispposMemupdate.equals(pos)){
				Column2 = "dispposMemupdate";
				mi.dispposMemupdate=mi.dispposFshout;
			}else if(mi.dispposComupdate.equals(pos)){
				Column2 = "dispposComupdate";
				mi.dispposComupdate=mi.dispposFshout;
			}
			mi.dispposFshout = pos;
			Column="dispposFshout";

		// ================= 左の列 =================
		}else if(type.equals("schedule")){
			if(mi.dispposMyphoto.equals(pos)){
				Column2 = "dispposMyphoto";
				mi.dispposMyphoto=mi.dispposCalendar;
			}else if(mi.dispposMy.equals(pos)){
				Column2 = "dispposMy";
				mi.dispposMy=mi.dispposCalendar;
			}else if(mi.dispposGroup.equals(pos)){
				Column2 = "dispposGroup";
				mi.dispposGroup=mi.dispposCalendar;
			}else if(mi.dispposCom.equals(pos)){
				Column2 = "dispposCom";
				mi.dispposCom=mi.dispposCalendar;
			}else if(mi.dispposFollowyou.equals(pos)){
				Column2 = "dispposFollowyou";
				mi.dispposFollowyou=mi.dispposCalendar;
			}else if(mi.dispposFollowme.equals(pos)){
				Column2 = "dispposFollowme";
				mi.dispposFollowme=mi.dispposCalendar;
			}
			mi.dispposCalendar = pos;
			Column="dispposCalendar";
		}else if(type.equals("myPhoto")){
			if(mi.dispposMy.equals(pos)){
				Column2 = "dispposMy";
				mi.dispposMy=mi.dispposMyphoto;
			}else if(mi.dispposGroup.equals(pos)){
				Column2 = "dispposGroup";
				mi.dispposGroup=mi.dispposMyphoto;
			}else if(mi.dispposCom.equals(pos)){
				Column2 = "dispposCom";
				mi.dispposCom=mi.dispposMyphoto;
			}else if(mi.dispposCalendar.equals(pos)){
				Column2 = "dispposCalendar";
				mi.dispposCalendar=mi.dispposMyphoto;
			}else if(mi.dispposFollowyou.equals(pos)){
				Column2 = "dispposFollowyou";
				mi.dispposFollowyou=mi.dispposMyphoto;
			}else if(mi.dispposFollowme.equals(pos)){
				Column2 = "dispposFollowme";
				mi.dispposFollowme=mi.dispposMyphoto;
			}
			mi.dispposMyphoto = pos;
			Column="dispposMyphoto";

		}else if(type.equals("myUpdate")){
			if(mi.dispposMyphoto.equals(pos)){
				Column2 = "dispposMyphoto";
				mi.dispposMyphoto=mi.dispposMy;
			}else if(mi.dispposGroup.equals(pos)){
				Column2 = "dispposGroup";
				mi.dispposGroup=mi.dispposMy;
			}else if(mi.dispposCom.equals(pos)){
				Column2 = "dispposCom";
				mi.dispposCom=mi.dispposMy;
			}else if(mi.dispposCalendar.equals(pos)){
				Column2 = "dispposCalendar";
				mi.dispposCalendar=mi.dispposMy;
			}else if(mi.dispposFollowyou.equals(pos)){
				Column2 = "dispposFollowyou";
				mi.dispposFollowyou=mi.dispposMy;
			}else if(mi.dispposFollowme.equals(pos)){
				Column2 = "dispposFollowme";
				mi.dispposFollowme=mi.dispposMy;
			}
			mi.dispposMy = pos;
			Column="dispposMy";

		}else if(type.equals("community")){
			if(mi.dispposMyphoto.equals(pos)){
				Column2 = "dispposMyphoto";
				mi.dispposMyphoto=mi.dispposCom;
			}else if(mi.dispposMy.equals(pos)){
				Column2 = "dispposMy";
				mi.dispposMy=mi.dispposCom;
			}else if(mi.dispposGroup.equals(pos)){
				Column2 = "dispposGroup";
				mi.dispposGroup=mi.dispposCom;
			}else if(mi.dispposCalendar.equals(pos)){
				Column2 = "dispposCalendar";
				mi.dispposCalendar=mi.dispposCom;
			}else if(mi.dispposFollowyou.equals(pos)){
				Column2 = "dispposFollowyou";
				mi.dispposFollowyou=mi.dispposCom;
			}else if(mi.dispposFollowme.equals(pos)){
				Column2 = "dispposFollowme";
				mi.dispposFollowme=mi.dispposCom;
			}
			mi.dispposCom = pos;
			Column="dispposCom";

		}else if(type.equals("group")){
			if(mi.dispposMyphoto.equals(pos)){
				Column2 = "dispposMyphoto";
				mi.dispposMyphoto=mi.dispposGroup;
			}else if(mi.dispposMy.equals(pos)){
				Column2 = "dispposMy";
				mi.dispposMy=mi.dispposGroup;
			}else if(mi.dispposCom.equals(pos)){
				Column2 = "dispposCom";
				mi.dispposCom=mi.dispposGroup;
			}else if(mi.dispposCalendar.equals(pos)){
				Column2 = "dispposCalendar";
				mi.dispposCalendar=mi.dispposGroup;
			}else if(mi.dispposFollowyou.equals(pos)){
				Column2 = "dispposFollowyou";
				mi.dispposFollowyou=mi.dispposGroup;
			}else if(mi.dispposFollowme.equals(pos)){
				Column2 = "dispposFollowme";
				mi.dispposFollowme=mi.dispposGroup;
			}
			mi.dispposGroup = pos;
			Column="dispposGroup";

		}else if(type.equals("followyou")){
			if(mi.dispposMyphoto.equals(pos)){
				Column2 = "dispposMyphoto";
				mi.dispposMyphoto=mi.dispposFollowyou;
			}else if(mi.dispposMy.equals(pos)){
				Column2 = "dispposMy";
				mi.dispposMy=mi.dispposFollowyou;
			}else if(mi.dispposCom.equals(pos)){
				Column2 = "dispposCom";
				mi.dispposCom=mi.dispposFollowyou;
			}else if(mi.dispposCalendar.equals(pos)){
				Column2 = "dispposCalendar";
				mi.dispposCalendar=mi.dispposFollowyou;
			}else if(mi.dispposFollowyou.equals(pos)){
				Column2 = "dispposFollowyou";
				mi.dispposFollowyou=mi.dispposFollowyou;
			}else if(mi.dispposFollowme.equals(pos)){
				Column2 = "dispposFollowme";
				mi.dispposFollowme=mi.dispposFollowyou;
			}
			mi.dispposFollowyou = pos;
			Column="dispposFollowyou";

		}else if(type.equals("followme")){
			if(mi.dispposMyphoto.equals(pos)){
				Column2 = "dispposMyphoto";
				mi.dispposMyphoto=mi.dispposFollowme;
			}else if(mi.dispposMy.equals(pos)){
				Column2 = "dispposMy";
				mi.dispposMy=mi.dispposFollowme;
			}else if(mi.dispposCom.equals(pos)){
				Column2 = "dispposCom";
				mi.dispposCom=mi.dispposFollowme;
			}else if(mi.dispposCalendar.equals(pos)){
				Column2 = "dispposCalendar";
				mi.dispposCalendar=mi.dispposFollowme;
			}else if(mi.dispposFollowyou.equals(pos)){
				Column2 = "dispposFollowyou";
				mi.dispposFollowyou=mi.dispposFollowme;
			}else if(mi.dispposFollowme.equals(pos)){
				Column2 = "dispposFollowme";
				mi.dispposFollowme=mi.dispposFollowme;
			}
			mi.dispposFollowme = pos;
			Column="dispposFollowme";
		}
		//System.out.println(type);

		if(Column!=null&&Column2!=null){
			jdbcManager.update(mi).includes(Column,Column2).execute();
		}
	}

	public void updateRssPosition(String mid,Integer rssno,Integer _rssno){
		//Rssデータ取得
		Memberrsslist preRss = selRssData(mid,rssno);
		int pno = preRss.sno;
		//位置変更対象Rssデータ取得
		Memberrsslist aftRss = selRssData(mid,_rssno);
		int ano = aftRss.sno;

		//表示番号入れ替え
		preRss.sno=ano;
		aftRss.sno=pno;

		jdbcManager.update(preRss).includes("sno").execute();
		jdbcManager.update(aftRss).includes("sno").execute();

	}

	/**
	 * @deprecated
	 * @param mid
	 * @param fscomment
	 * @param fscheck
	 * @param fskoukaihani
	 * @param fsconfirm
	 */
	//コメント登録処理
	public void insFSComment(String mid,String fscomment,Integer fscheck,Integer fskoukaihani,Integer fsconfirm) {

		//コメントのMAX値を取得
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mid", mid);

		results = jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selMaxFShoutComment",params)
					.getResultList();

		//Twitterにも登録チェックボックス
		Integer fscheckVal;
		if(fscheck==2){
			fscheckVal = 1;
		}else{
			fscheckVal = 0;
		}

		//改行コードを削除
		fscomment = fscomment.replaceAll("\n","");
		//コメントの登録
		Frontiershout fshout = new Frontiershout();
		fshout.mid = mid;
		fshout.no = (Integer)results.get(0).get("comno");
		fshout.comment = fscomment;
		fshout.twitter = fscheckVal;
		fshout.entid = mid;
		fshout.updid = mid;
		fshout.demandflg = fsconfirm;
		fshout.pub_level = fskoukaihani;

		jdbcManager.updateBySqlFile("/data/insFShoutComment", fshout)
		.execute();
	}

	// 内容更新
	public void delFSComment(
		String mid,
		Integer no
	) throws IOException, Exception{
		/* ■ FShoutコメント削除 */
		fs              = new Frontiershout();
		fs.updid        = mid;
		fs.mid          = mid;
		fs.no           = no;
		jdbcManager
		.updateBySqlFile("/data/delFShoutComment.sql", fs)
		.execute();
	}

	// FShout一覧取得（メンバー）
	public List<BeanMap> selFShoutListMem(String mid,Integer limit,Integer cntFlg){
		Map<String, Object> params = new HashMap<String, Object>();
		//cntFlg 0:limit設定無し、1:limit設定有り
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("limitcount",appDefDto.FP_MY_LIST_PGMAX);
		params.put("limitdate",appDefDto.FP_MY_FSHOUTLIST_DATE_PGMAX);
		if(cntFlg==1){
			return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFShoutListMemTop.sql",params)
			.limit(limit)
			.getResultList();
		}else{
			return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFShoutListMemTop.sql",params)
			.getResultList();
		}
	}


	//F Shout（サイズ）
	public Integer selFShoutListSize(String mid,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("limitdate",appDefDto.FP_MY_FSHOUTLIST_DATE_PGMAX);
		results = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShout.sql",params)
			.limit(limit)
			.getResultList();
		return results.size();
	}

	// メンバーデータ取得
	public List<BeanMap> selectprofile(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selprofile2.sql",params)
		.getResultList();
		return results;
	}


	//F Shout(自分宛)
	public List<BeanMap> selFShoutListToMe(String mid,Integer limit,Integer cntFlg,Integer fsoffset){
		Map<String, Object> params = new HashMap<String, Object>();
		//cntFlg 0:limit設定無し、1:limit設定有り
		// Mapオブジェクトに条件用パラメタを定義
		// midよりグループ＋フォローしているメンバーのmid取得
		List<BeanMap> lbm = commonService.getMidList("0", mid);
		List<String> midlist = new ArrayList<String>();
		// 最初に自分のmid追加
		midlist.add(mid);
		// 取得したmidリストが0件以上だったらリスト作成
		if(lbm.size() > 0){
			for(BeanMap a:lbm){
				midlist.add(a.get("mid").toString());
			}
		}
		params.put("mid",midlist);
		params.put("limitdate",appDefDto.FP_MY_FSHOUTLIST_DATE_PGMAX);
		params.put("mymid",mid);
		if(cntFlg==1){
			params.put("offset",fsoffset);
			return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShoutToMe.sql",params)
				.limit(limit)
				.getResultList();
		}else{
			return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewFShoutToMe.sql",params)
			.getResultList();
		}
	}

	//　相手から確認を要求されたコメントの確認
	public void updConfirm(
		String mid,
		Integer no,
		String mymid
	) throws IOException, Exception{
		/* ■ FShoutコメント削除 */
		fs              = new Frontiershout();
		fs.updid        = mymid;
		fs.mid          = mid;
		fs.no           = no;
		jdbcManager
		.updateBySqlFile("/data/updFShoutConfirm.sql", fs)
		.execute();
	}

	//Pagingクラス初期化
	public Paging setPaging(Integer page,Integer cnt){
		Paging paging = new Paging(page, cnt);
		return paging;
	}

}