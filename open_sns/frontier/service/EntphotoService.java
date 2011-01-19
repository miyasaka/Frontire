package frontier.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.h2.util.StringUtils;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Photo;
import frontier.entity.PhotoAlbum;
import frontier.entity.PhotoComment;
import frontier.form.pc.EntphotoForm;

public class EntphotoService {
	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	
	//フォトアルバムのMax値を取得
	public List<BeanMap> selMaxAno(String mid){
		//検索条件設定
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mid", mid);
		
		return jdbcManager
		.selectBySqlFile(BeanMap.class, "/data/selMaxAno",params)
		.getResultList();
		
	}
	
	//フォトのMax値を取得
	public List<BeanMap> selMaxFno(String mid,Integer ano){
		//検索条件設定
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mid", mid);
		params.put("ano", ano);
		
		return jdbcManager
		.selectBySqlFile(BeanMap.class, "/data/selMaxFno",params)
		.getResultList();
		
	}
	
	//フォトアルバムの新規登録
	public void insPhotoAlbum(String mid,Integer ano,EntphotoForm entphotoForm){
		//登録用のエンティティ
		PhotoAlbum pa = new PhotoAlbum();
		
		//パラメータ設定
		pa.mid = mid;
		pa.ano = ano;
		pa.title = entphotoForm.albumTitle;
		pa.detail = entphotoForm.albumBody;
		pa.publevel = entphotoForm.level;
		pa.entid = mid;
		pa.updid = mid;
		
		//フォトアルバム登録
		jdbcManager.updateBySqlFile("/data/insPhotoAlbum", pa)
		.execute();
	}
	
	//フォトの登録処理
	public void insPhoto(String mid,Integer ano,Integer fno,String photo){
		//登録用のエンティティ
		Photo p = new Photo();
		
		//パラメータ設定
		p.mid = mid;
		p.ano = ano;
		p.fno = fno;
		p.picname = photo.split("/")[6];
		p.pic = photo;
		p.entid = mid;
		p.updid = mid;
		
		//フォトアルバム登録
		jdbcManager.updateBySqlFile("/data/insPhoto", p)
		.execute();	
	}
	
	//フォトアルバムの表紙を通常に変更
	public void updPhotoCoverNormal(String mid,Integer ano,Integer fno){
		//更新用のエンティティ
		Photo p = new Photo();
		
		//パラメータ設定
		p.mid = mid;
		p.ano = ano;
		p.fno = fno;
		p.updid = mid;
		
		//フォトアルバム表紙を通常へ更新
		jdbcManager.updateBySqlFile("/data/updPhotoCoverNormal", p)
		.execute();	
	}	

	//フォトアルバムの表紙を設定
	public void updPhotoCover(String mid,Integer ano,Integer fno){
		//更新用のエンティティ
		Photo p = new Photo();
		
		//パラメータ設定
		p.mid = mid;
		p.ano = ano;
		p.fno = fno;
		p.updid = mid;
		
		//フォトアルバム表紙を通常へ更新
		jdbcManager.updateBySqlFile("/data/updPhotoCover", p)
		.execute();	
	}	
	
	/**
	 * フォトアルバム編集 フォトアルバム詳細取得
	 * @param entphotoForm
	 * @return Map<String,Object>
	 */
	public Map<String,Object> selEntphotoIndex(EntphotoForm entphotoForm){
		Map<String,Object> params = new HashMap<String,Object>();
		// パラメータセット
		params.put("mid", userInfoDto.memberId);
		params.put("ano", entphotoForm.ano);
		// SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selEntphotoIndex", params)
					.getSingleResult();
	}
	
	/**
	 * 共通 型判定
	 * @param str
	 * @return Integer
	 */
	public Integer convInt(String str){
		Integer retInt = 0;
		try{
			if(!StringUtils.isNullOrEmpty(str)){
				retInt = Integer.valueOf(str);
			}
		}catch (NumberFormatException e){
			e.printStackTrace();
		}
		return retInt;
	}
	
	/**
	 * フォトアルバム編集 フォトアルバム編集
	 * @param entphotoForm
	 */
	public void updatePhotoAlbum(EntphotoForm entphotoForm){
		// 現在時間の取得(更新用)
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		// データ登録
		PhotoAlbum pa = new PhotoAlbum();
		pa.mid      = userInfoDto.memberId;
		pa.ano      = entphotoForm.ano;
		pa.title    = entphotoForm.albumTitle;
		pa.detail   = entphotoForm.albumBody;
		pa.publevel = entphotoForm.level;
		pa.updid    = userInfoDto.memberId;
		pa.upddate  = ts;
		
		//フォトアルバム登録
		jdbcManager.updateBySqlFile("/data/updPhotoAlbum", pa)
		.execute();
	}
	
	/**
	 * 共通 全角空白除去
	 * @param str
	 * @return String
	 */
	public String setZenToHan(String str) {
		return str.replaceAll("　", " ").trim().replaceAll(" ", "　");
    }
	
	/**
	 * フォトアルバム削除
	 * @param entphotoForm
	 */
	public void deleteAll(EntphotoForm entphotoForm){
		// コメントの削除
		deleteAllPhotoComment(entphotoForm);
		// 画像の削除
		deleteAllPhoto(entphotoForm);
		// フォトアルバムの削除
		deleteAllPhotoAlbum(entphotoForm);
	}
	
	/**
	 * フォトアルバム削除 フォトアルバム削除
	 * @param entphotoForm
	 */
	public void deleteAllPhotoAlbum(EntphotoForm entphotoForm){
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		// データ削除
		PhotoAlbum pa = new PhotoAlbum();
		pa.mid     = userInfoDto.memberId;
		pa.ano     = entphotoForm.ano;
		pa.upddate = ts;
		pa.updid   = userInfoDto.memberId;
		// 実行
		jdbcManager.updateBySqlFile("/data/updPhotoAlbumDelflg", pa).execute();
	}
	
	/**
	 * フォトアルバム削除 画像の削除
	 * @param entphotoForm
	 */
	public void deleteAllPhoto(EntphotoForm entphotoForm){
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		// データ削除
		Photo p = new Photo();
		p.mid     = userInfoDto.memberId;
		p.ano     = entphotoForm.ano;
		p.upddate = ts;
		p.updid   = userInfoDto.memberId;
		// 実行
		jdbcManager.updateBySqlFile("/data/updPhotoDelflg", p).execute();
	}
	
	/**
	 * フォトアルバム削除 コメントの削除
	 * @param entphotoForm
	 */
	public void deleteAllPhotoComment(EntphotoForm entphotoForm){
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		// データ削除
		PhotoComment pc = new PhotoComment();
		pc.mid     = userInfoDto.memberId;
		pc.ano     = entphotoForm.ano;
		pc.upddate = ts;
		pc.updid   = userInfoDto.memberId;
		// 実行
		jdbcManager.updateBySqlFile("/data/updPhotoCommentDelflg", pc).execute();
	}
	
	/**
	 * 写真編集 写真詳細取得
	 * @param entphotoForm
	 * @return Map<String,Object>
	 */
	public Map<String,Object> selEntphotoEditphoto(EntphotoForm entphotoForm){
		Map<String,Object> params = new HashMap<String,Object>();
		// パラメータセット
		params.put("mid", userInfoDto.memberId);
		params.put("ano", entphotoForm.ano);
		params.put("fno", entphotoForm.fno);
		// SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPhotoViewphoto", params)
					.getSingleResult();
	}
	
	/**
	 * 写真編集 写真編集
	 * @param entphotoForm
	 */
	public void updatePhoto(EntphotoForm entphotoForm, String pic){
		// 現在時間の取得(更新用)
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		// データ登録
		Photo p = new Photo();
		p.mid     = userInfoDto.memberId;
		p.ano     = entphotoForm.ano;
		p.fno     = entphotoForm.fno;
		p.picname = entphotoForm.picname;
		p.pic     = pic;
		p.entid   = userInfoDto.memberId;
		p.updid   = userInfoDto.memberId;
		p.upddate = ts;
		
		//フォトアルバム登録
		jdbcManager.updateBySqlFile("/data/updPhoto", p)
		.execute();
	}
	
	/**
	 * 写真削除 DB更新
	 * @param entphotoForm
	 */
	public void updatePhotoDelflg(EntphotoForm entphotoForm){
		// 現在時間の取得(更新用)
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		// データ更新
		Photo p = new Photo();
		p.mid     = userInfoDto.memberId;
		p.ano     = entphotoForm.ano;
		p.fno     = entphotoForm.fno;
		p.updid   = userInfoDto.memberId;
		p.upddate = ts;
		
		//フォトアルバム登録
		jdbcManager.updateBySqlFile("/data/updPhotoDelflg", p)
		.execute();
	}
	
	/**
	 * 写真編集 写真削除
	 * @param entphotoForm
	 * @param isDelete
	 */
	public void removePhoto(EntphotoForm entphotoForm, boolean isDelete){
		if(isDelete){
			String path = null;
			// フォトアルバム情報再取得
			Map<String,Object> photoMap = new HashMap<String,Object>();
			photoMap = selEntphotoEditphoto(entphotoForm);
			if(photoMap != null){
				path = photoMap.get("pic").toString();
				if(!StringUtils.isNullOrEmpty(path)){
					deletePhoto(path);
				}
			}
		}
	}
	
	/**
	 * ファイルアップロード ファイル削除
	 * @param path
	 */
	public void deletePhoto(String path){
		String photoPathDb = appDefDto.FP_CMN_CONTENTS_DIR + path;
		String[] picsizeArray = {"pic640", "pic480", "pic240", "pic180", "pic120", "pic76", "pic60", "pic42"};
		for (String picsize : picsizeArray) {
			deleteFile(photoPathDb.replaceAll("/dir/", "/" + picsize + "/"));
		}
	}
	
	/**
	 * ファイルアップロード ファイル削除
	 * @param Path
	 */
	public void deleteFile(String Path){
		File file = new File(Path);
		file.delete();
	}
	
	/**
	 * 画像ファイル名取得
	 * @param entphotoForm
	 * @param isPhotoname true：ファイル名 false：ファイルパス
	 * @return String
	 */
	public String getPhotoname(EntphotoForm entphotoForm, boolean isPhotoname){
		String photoname = null;
		photoname
				= appDefDto.FP_CMN_DIARY_BEFORE_PATH+ userInfoDto.memberId + appDefDto.FP_CMN_PHOTO_AFTER_PATH
				+ CmnUtility.stringFormat("0000", entphotoForm.ano)
				+ "_"
				+ CmnUtility.stringFormat("000", entphotoForm.fno)
				+ "_"
				+ CmnUtility.getToday("yyyyMMddHHmmss")
				+ ".jpg";
		if(isPhotoname){
			photoname = photoname.split("/")[6];
		}
		return photoname;
	}
}