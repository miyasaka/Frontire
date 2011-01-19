package frontier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.common.EmojiUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Diary;
import frontier.form.m.DiaryMForm;
import frontier.form.pc.DiaryForm;

public class DiaryService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	private DiaryCmnService diaryCmnService;
	@Resource
	public UserInfoDto userInfoDto;
	
	//インスタンス変数
	protected List<BeanMap> results;
	
	//日記一覧検索
	public List<BeanMap> selDiaryList(String mid,String month,String day,int offset,List<String> pubdiary,String membertype,List<String> publevel,List<String> appStatusList,Integer limit){
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
					.selectBySqlFile(BeanMap.class, "/data/selDiaryList",params)
					.getResultList();
		
	}
	
	//日記のある日付をリンク表示するために使用する。
	public List<BeanMap> selDiaryMonthList(String mid,String month,List<String> pubdiary,String membertype,List<String> publevel,List<String> appStatusList){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("entdate", month);
		params.put("pubdiary", pubdiary);
		params.put("membertype", membertype);
		params.put("publevel", publevel);
		params.put("appstatus", appStatusList);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selDiaryMonthList",params)
					.getResultList();
			
	}
	
	//日記一覧件数検索
	public long cntDiaryList(String mid,String month,String day,List<String> pubdiary,String membertype,List<String> publevel,List<String> appStatusList){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("searchMonth", month);
		params.put("searchDay", day);
		params.put("pubdiary", pubdiary);
		params.put("membertype", membertype);
		params.put("publevel", publevel);
		params.put("appstatus", appStatusList);
		
		return jdbcManager.getCountBySqlFile("/data/selDiaryList", params);

	}
		
	/**
	 * 日記閲覧検索
	 * PC,モバイル共通
	 */
	public List<BeanMap> selDiaryViewListPC(String mid,int diaryid,List<String> pubdiary,String membertype,List<String> publevel,List<String> appStatusList){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("diaryid", diaryid);
		params.put("pubdiary", pubdiary);	
		params.put("membertype", membertype);
		params.put("publevel", publevel);
		params.put("appstatus", appStatusList);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selDiaryViewPC",params)
					.getResultList();
	}
	
	//日記閲覧（前の日記検索）
	public List<BeanMap> selPreDiary(String mid,int diaryid,List<String> pubdiary,String membertype,List<String> publevel,List<String> appStatusList){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("diaryid", diaryid);
		params.put("pubdiary", pubdiary);		
		params.put("membertype", membertype);
		params.put("publevel", publevel);
		params.put("appstatus", appStatusList);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPreDiary",params)
					.getResultList();
	}
	
	//日記閲覧（次の日記検索）
	public List<BeanMap> selNextDiary(String mid,int diaryid,List<String> pubdiary,String membertype,List<String> publevel,List<String> appStatusList){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("diaryid", diaryid);
		params.put("pubdiary", pubdiary);
		params.put("membertype", membertype);
		params.put("publevel", publevel);
		params.put("appstatus", appStatusList);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selNextDiary",params)
					.getResultList();
	}	
	
	//コメント削除確認検索
	public List<BeanMap> selDeleteCommentList(String mid,int diaryid,List<Integer> comno,String cid){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("diaryid", diaryid);
		params.put("comno", comno);
		params.put("cid", cid);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selDiaryCommentDelete",params)
					.getResultList();
	}
	
	//コメント登録処理
	public void insDiaryComment(String mid,DiaryForm diaryForm,String myid) {
		//コメントのMAX値を取得
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mid", mid);
		params.put("diaryid", diaryForm.diaryId);
		
		results = jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selMaxDiaryComment",params)
					.getResultList();
		
		String pic1;
		String pic2;
		String pic3;
		
		//コメントの登録
		Diary diary = new Diary();	
		diary.mid = mid;
		diary.diaryid = diaryForm.diaryId;
		diary.guest_name = diaryForm.guestname;
		diary.guest_email = diaryForm.guestemail;
		diary.guest_url = diaryForm.guesturl;
		diary.comment = diaryForm.comment;
		diary.comno = (Integer)results.get(0).get("comno");
		//画像ファイル名生成
		//null対策
		if(diaryForm.photo1!=null){
			pic1 = diaryCmnService.getFileName(diaryForm.diaryId, diaryForm.photo1, mid, "1", diary.comno);
		} else {
			pic1 = "";
		}
		//null対策
		if(diaryForm.photo2!=null){
			pic2 = diaryCmnService.getFileName(diaryForm.diaryId, diaryForm.photo2, mid, "2", diary.comno);
		} else {
			pic2 = "";
		}
		//null対策
		if(diaryForm.photo3!=null){
			pic3 = diaryCmnService.getFileName(diaryForm.diaryId, diaryForm.photo3, mid, "3", diary.comno);
		} else {
			pic3 = "";
		}
		
		diary.pic1 = pic1;
		diary.pic2 = pic2;
		diary.pic3 = pic3;
		if(pic1!=null){
			diary.picnote1 = diaryForm.picnote1;
		}
		if(pic2!=null){
			diary.picnote2 = diaryForm.picnote2;
		}
		if(pic3!=null){
			diary.picnote3 = diaryForm.picnote3;
		}
		diary.entid = myid;
		diary.updid = myid;
		
		jdbcManager.updateBySqlFile("/data/insDiaryComment", diary)
		.execute();
		
		//画像アップロード処理
		diaryCmnService.upload(mid, diaryForm.photo1, pic1);
		diaryCmnService.upload(mid, diaryForm.photo2, pic2);
		diaryCmnService.upload(mid, diaryForm.photo3, pic3);
	}
	
	//コメントの外部公開　承認、取消
	public void updDiaryCommentOutside(String mid,DiaryForm diaryForm,String myid,String publevel,String appstatus){
		//更新用のエンティティ
		Diary d = new Diary();
		
		//パラメータ設定
		d.mid = mid;
		d.diaryid = diaryForm.diaryId;
		d.updid = myid;
		d.publevel = publevel;
		d.appstatus = appstatus;
		d.comno = diaryForm.comno;
		
		//外部公開の設定
		jdbcManager.updateBySqlFile("/data/updDiaryCommentOutside", d)
		.execute();	
	}
	
	//コメントの論理削除
	public void updDiaryCommentDelflg(String mid,DiaryForm diaryForm,String myid){
		//変数初期化&定義
		List<Diary> paramList = new ArrayList<Diary>();
		List l = diaryForm.checkCommentNo;
		
		l = CmnUtility.StringToArray(l);
		
		logger.debug(l);
		logger.debug(l.size());
		//エンティティのListにパラメータを格納。
		for (int i=0;i<l.size();i++) {
			Diary d = new Diary();
			//エンティティへの設定
			d.mid = mid;
			d.diaryid = diaryForm.diaryId;
			d.comno = Integer.valueOf(l.get(i).toString());
			d.updid = myid;

			//Listへエンティティの格納
			paramList.add(i, d);
		}
		
		//更新実行
		jdbcManager.updateBatchBySqlFile("/data/updDiaryCommentDelflg", paramList)
					.execute();
	}
	
	//既読未読FLGの更新
	public void updDiaryReadflg(String mid,Integer diaryid){
		//更新用のエンティティ
		Diary d = new Diary();
		
		//パラメータ設定
		d.mid = mid;
		d.diaryid = diaryid;
		
		//フォトアルバム表紙を通常へ更新
		jdbcManager.updateBySqlFile("/data/updDiaryReadflg", d)
		.execute();	
	}
	
	//外部公開の承認・非承認
	public void updDiaryApproval(String mid,Integer diaryid,String myid,String publevel,String appstatus){
		//更新用のエンティティ
		Diary d = new Diary();
		
		//パラメータ設定
		d.mid = mid;
		d.diaryid = diaryid;
		d.updid = myid;
		d.publevel = publevel;
		d.appstatus = appstatus;
		
		//外部公開の設定
		jdbcManager.updateBySqlFile("/data/updDiaryApproval", d)
		.execute();	
	}
	
	//日記Frontier net公開用RSS検索
	public List<BeanMap> selFNetDiaryRss(List<String> appstatus){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("appstatus", appstatus);

		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFNetDiaryRss",params)
					.getResultList();
	}
	
	//日記外部公開用RSS検索
	public List<BeanMap> selOutsideDiaryRss(Integer id,List<String> appstatus){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("id", id);
		params.put("appstatus", appstatus);
		params.put("limit", appDefDto.FP_CMN_RSS_LISTMAX);

		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selOutsideDiaryRss",params)
					.getResultList();
	}

	//外部公開用日記の検索(midを条件にした場合)
	public long selOutPubDiary(String mid){
		return jdbcManager.from(Diary.class)
						.where("pub_level = '0' and mid = ?",mid)
						.getCount();
	}
	
	//外部公開用日記の検索(midとdiaryidを条件にした場合)
	public long selOutPubDiary(String mid,String diaryid){
		return jdbcManager.from(Diary.class)
						.where("pub_level = '0' and comno = '0' and mid = ? and diaryid = ?",mid,diaryid)
						.getCount();
	}
	
	
	//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ モバイル版 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	//コメント登録処理
	public void insDiaryComment(String mid,DiaryMForm diaryForm,String myid) {
		//コメントのMAX値を取得
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mid", mid);
		params.put("diaryid", diaryForm.diaryId);
		
		results = jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selMaxDiaryComment",params)
					.getResultList();
		
		//コメントの登録
		Diary diary = new Diary();	
		diary.mid = mid;
		diary.diaryid = diaryForm.diaryId;
		diary.comment = EmojiUtility.replaceMoblileToPc(diaryForm.comment,
				appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
		diary.comno = (Integer)results.get(0).get("comno");

		diary.entid = myid;
		diary.updid = myid;
		
		jdbcManager.updateBySqlFile("/data/insDiaryComment", diary)
		.execute();

	}
	
	//コメントの論理削除
	public void updDiaryCommentDelflg(String mid,DiaryMForm diaryForm,String myid){
		//変数初期化&定義
		List<Diary> paramList = new ArrayList<Diary>();
		List l = diaryForm.checkCommentNo;
		
		l = CmnUtility.StringToArray(l);
		
		logger.debug(l);
		logger.debug(l.size());
		//エンティティのListにパラメータを格納。
		for (int i=0;i<l.size();i++) {
			Diary d = new Diary();
			//エンティティへの設定
			d.mid = mid;
			d.diaryid = diaryForm.diaryId;
			d.comno = Integer.valueOf(l.get(i).toString());
			d.updid = myid;

			//Listへエンティティの格納
			paramList.add(i, d);
		}
		
		//更新実行
		jdbcManager.updateBatchBySqlFile("/data/updDiaryCommentDelflg", paramList)
					.execute();
	}
	
	/*
	 * Frontier Net公開の日記一覧を取得するSQL
	 * @param domain Frontierドメイン
	 * @param gid グループID
	 * 
	 */
	public List<BeanMap> getDiaryList(String type,String caltype,String entdate,Integer offset,String gid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("type",type);
		params.put("caltype", caltype);
		params.put("entdate", entdate);
		params.put("domain",appDefDto.FP_CMN_HOST_NAME);
		params.put("gid",gid);
		params.put("limit",appDefDto.FP_MY_DIARYLIST_PGMAX);
		params.put("offset",offset);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFDiaryListAll.sql",params)
		.getResultList();
	}
	
	/*
	 * Frontier Net公開の日記総数を取得するSQL
	 * @param domain Frontierドメイン
	 * @param gid グループID
	 * 
	 */	
	public long cntDiaryList(String type,String caltype,String entdate,String gid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("type",type);
		params.put("caltype", caltype);
		params.put("entdate", entdate);
		params.put("domain",appDefDto.FP_CMN_HOST_NAME);
		params.put("gid",gid);

		return jdbcManager.getCountBySqlFile("/data/selFDiaryListAll.sql", params);
	}
		
}
