package frontier.service;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.exception.IORuntimeException;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.entity.Diary;

public class EntdiaryService {
		Logger logger = Logger.getLogger(this.getClass().getName());
		public Diary d;
		
	    public HttpServletRequest request;
	    public ServletContext application;
	    
		@Resource
		public AppDefDto appDefDto;
		
		@Resource
		protected JdbcManager jdbcManager;	
		
		@Resource
		protected DiaryCmnService diaryCmnService;
		
		//変数定義
		public String DIR;
		public String BasePath;
		public String FileName = null;
		public ImageIcon icon;
		public Integer diaryid;
		public String photoName1;
		public String photoName2;
		public String photoName3;
		public String picNote1;
		public String picNote2;
		public String picNote3;
		Integer[] DirList ={640,480,240,180,120,76,60,42};
		
		public ActionMessages errors = new ActionMessages();
		
		public Diary selDiary(String mid,Integer diaryid){
			logger.debug("select");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mid",mid);
			params.put("diaryid",diaryid);
			params.put("comno",0);
			return jdbcManager.from(Diary.class).where(params).getSingleResult();
		}
		
		public List<BeanMap> selDiaryList(String mid,List<String> diaryid){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mid",mid);
			params.put("diaryid",diaryid);
			params.put("comno",0);
			return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selDelDiaryList.sql",params).getResultList();
		}
		
		public void delImage(String mid,Integer diaryid,String pic1,String pic2,String pic3,Integer photoNo,String picnote1,String picnote2,String picnote3){
			String Column = null;
			String ColumnNote = null;
			Diary d = new Diary();
			d.mid=mid;
			d.diaryid=diaryid;
			d.comno=0;
			if(photoNo==1){
				d.pic1=pic1;
				d.pic1=null;
				Column="pic1";
				
				d.picnote1=picnote1;
				d.picnote1=null;
				ColumnNote="picnote1";
			}else if(photoNo==2){
				d.pic2=pic2;
				d.pic2=null;
				Column="pic2";
				
				d.picnote2=picnote2;
				d.picnote2=null;
				ColumnNote="picnote2";
			}else if(photoNo==3){
				d.pic3=pic3;
				d.pic3=null;
				Column="pic3";
				
				d.picnote3=picnote3;
				d.picnote3=null;
				ColumnNote="picnote3";
			}
			if(Column!=null){
				jdbcManager.update(d).includes(Column).execute();
			}
			if(ColumnNote!=null){
				jdbcManager.update(d).includes(ColumnNote).execute();
			}

		}
		
		public void updateDiary(String mid,Integer diaryid,String title,String comment,FormFile photo1,FormFile photo2,FormFile photo3,String pubDiary,String picnote1,String picnote2,String picnote3,String pubLevel){
			//null対策
			if(photo1!=null){
				photoName1=diaryCmnService.getFileName(diaryid, photo1, mid, "1", 0);
			}
			//null対策
			if(photo2!=null){
				photoName2=diaryCmnService.getFileName(diaryid, photo2, mid, "2", 0);
			}
			//null対策
			if(photo3!=null){
				photoName3=diaryCmnService.getFileName(diaryid, photo3, mid, "3", 0);
			}
			
			// 画像説明のセット
			String picnt1 = "";
			String picnt2 = "";
			String picnt3 = "";
			
			//データ更新
			d = selDiary(mid,diaryid);
			d.comment = comment;
			d.title   = title;
			if(photoName1!=null){
				d.pic1    = photoName1;
				picnt1    = picnote1;
			} else {
				if(d.pic1!=null){
					picnt1 = picnote1;
				}
			}
			if(photoName2!=null){
				d.pic2    = photoName2;
				picnt2    = picnote2;
			} else {
				if(d.pic2!=null){
					picnt2 = picnote2;
				}
			}
			if(photoName3!=null){
				d.pic3    = photoName3;
				picnt3    = picnote3;
			} else {
				if(d.pic3!=null){
					picnt3 = picnote3;
				}
			}
			d.picnote1= picnt1;
			d.picnote2= picnt2;
			d.picnote3= picnt3;
			d.upddate = new Timestamp ((new java.util.Date()).getTime());
			d.updid   = mid;
			d.pubDiary=pubDiary;
			
			//現在の値と公開範囲を変更した場合だけ、更新する
			if (!d.publevel.equals(pubLevel)){
				d.publevel=pubLevel;

				if(pubLevel.equals("0")){
					d.appstatus="3";
				}else if(pubLevel.equals("1")){
					d.appstatus="1";
				}else{
					d.appstatus="0";					
				}
			}
			
			jdbcManager.update(d).execute();
		}
		
		//日記削除
		public void deleteDiary(String mid,List<String>checkDiaryId){
			//変数初期化&定義
			List<Diary> paramList = new ArrayList<Diary>();
			int t = checkDiaryId.size();
			int id;
			
			//エンティティのListにパラメータを格納。
			for (int i=0;i<t;i++) {
			
				Diary d = new Diary();
				//エンティティへの設定
				d.mid = mid;
				id = new Integer(checkDiaryId.get(i));
				d.diaryid = id;
				d.comno = 0;
				d.updid = mid;

				//Listへエンティティの格納
				paramList.add(i, d);
			}
			
			//更新実行
			jdbcManager.updateBatchBySqlFile("/data/updDiaryCommentDelflg", paramList)
						.execute();
		}
		
		// 日記登録
		public Integer insertDiaryNew(String mid,String title,String comment,FormFile photo1,FormFile photo2,FormFile photo3,String pubDiary,String picnote1,String picnote2,String picnote3,String pubLevel) {
			//パラメータ設定
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mid",mid);
			params.put("comno",0);
			//日記ID取得
			diaryid = jdbcManager.selectBySqlFile(Diary.class,"/data/selMaxDiaryId.sql",params).getSingleResult().diaryid;
			logger.debug("日記新規");
			logger.debug(diaryid);
			//画像名取得
			//null対策
			if(photo1!=null){
				photoName1=diaryCmnService.getFileName(diaryid, photo1, mid, "1", 0);
			}
			//null対策
			if(photo2!=null){
				photoName2=diaryCmnService.getFileName(diaryid, photo2, mid, "2", 0);
			}
			//null対策
			if(photo3!=null){
				photoName3=diaryCmnService.getFileName(diaryid, photo3, mid, "3", 0);
			}
			//画像説明セット
			if(photoName1!="" && photoName1!=null){
				picNote1 = picnote1;
			}
			if(photoName2!="" && photoName2!=null){
				picNote2 = picnote2;
			}
			if(photoName3!="" && photoName3!=null){
				picNote3 = picnote3;
			}
			
			//データ登録
			d = new Diary();
			d.mid = mid;
			d.diaryid=diaryid;
			d.comno = 0;
			d.comment = comment;
			d.title   = title;
			d.pic1    = photoName1;
			d.pic2    = photoName2;
			d.pic3    = photoName3;
			d.picnote1= picNote1;
			d.picnote2= picNote2;
			d.picnote3= picNote3;
			d.delflg  = "0";
			d.upddate = new Timestamp ((new java.util.Date()).getTime());
			d.entid   = mid;
			d.updid   = mid;
			d.entdate = new Timestamp ((new java.util.Date()).getTime());
			d.readflg="0";
			d.diarycategory = "0";
			d.reportday="";
			d.pubDiary=pubDiary;
			d.publevel=pubLevel;
			
			//選択した公開レベルによって申請状態を登録する。
			if (pubLevel.equals("0")){
				//外部に公開申請
				d.appstatus="3";
			}else if (pubLevel.equals("1")){
				//Frontier Netに公開申請
				d.appstatus="1";
			}else if (pubLevel.equals("9")){
				//公開しない
				d.appstatus="0";
			}

			jdbcManager.insert(d).execute();
			
			return diaryid;
		}
		
		// 画像アップロード開始
		public void upload(String mid,FormFile photo1,FormFile photo2,FormFile photo3,String comno){
			//出力先ディレクトリ
			//Contents ROOT = "http://hawaii/contents";
			//DIR = "http://hawaii/contents/img/mem/XXXXXXX";
			
			//メンバー用ディレクトリパス
			DIR = appDefDto.FP_CMN_CONTENTS_DIR+"img/mem/"+mid;

			//ディレクトリ作成
			CmnUtility.makeDir(DIR,"diary");
			
			//日記用ディレクトリパス
			DIR = DIR+"/diary";
	
			//画像1アップロード処理
			if(photo1.getFileSize()>0){
				FileName = getFileName(photoName1);
				_uploadFile(photo1);
			}
			
			//画像2アップロード処理
			if(photo2.getFileSize()>0){
				FileName = getFileName(photoName2);
				_uploadFile(photo2);
			}
			
			//画像3アップロード処理
			if(photo3.getFileSize()>0){
				FileName = getFileName(photoName3);
				_uploadFile(photo3);
			}
			
			logger.debug("アップロード処理終了");

		}
	    
	    //各サイズ毎の画像を生成
	    protected void _uploadFile(FormFile file){
	    	//ベース画像パス
	    	BasePath = DIR+"/pic640/"+FileName;
	    	//変数定義
	    	String path;
		    //最初のみアップロード
	    	CmnUtility.uploadFile(BasePath,file);
		    //画像情報取得
	    	icon = new ImageIcon(BasePath);
	    	for(Integer i:DirList){
	    		path = DIR+"/pic"+String.valueOf(i)+"/"+FileName;
	    		try {
	    			CmnUtility.resize(path,i,BasePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
	    		logger.debug(path);
	    	}

	    }
	    
		//アップロード画像のファイル名を取得
		private String getFileName(String fname){
			fname = fname.substring(fname.indexOf("dir")+4);
			logger.debug("ファイル名："+fname);
		    return fname;
		}

	    
		//日記のある日付をリンク表示するために使用する。
		public List<BeanMap> selDiaryMonthList(String mid,String month,List<String> pubdiary,String membertype,List<String> publevel){

			//インスタンス変数
			List<BeanMap> results;
			Map<String,Object> params = new HashMap<String,Object>();

			//パラメータ設定
			params.put("mid", mid);
			params.put("entdate", month);
			params.put("pubdiary",pubdiary);
			params.put("membertype", membertype);
			params.put("publevel", publevel);
			
			//SQL実行
			results = jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selDiaryMonthList",params)
						.getResultList();
			return results;		
		}
		
		//日記公開範囲取得
		// メンバーデータ取得
		public List<BeanMap> selPubDiary(String mid){
			List<BeanMap> results;
			Map<String, Object> params = new HashMap<String, Object>();
			// Mapオブジェクトに条件用パラメタを定義
			params.put("mid",mid);
			results = jdbcManager
			.selectBySqlFile(BeanMap.class,"/data/selprofile.sql",params)
			.getResultList();
			return results;		
		}
		
}
