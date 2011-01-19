package frontier.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.h2.util.StringUtils;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Fileinfo;
import frontier.entity.MstCategory;
import frontier.form.pc.EntfileForm;

public class EntfileService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;	
	
	/**
	 * ファイル更新 ファイル詳細取得
	 * @param entfileForm
	 * @return Map<String,Object>
	 */
	public Map<String,Object> selEntfileIndex(EntfileForm entfileForm){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fileid", entfileForm.fileid);
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selEntfileIndex", params)
					.getSingleResult();
	}
	
	/**
	 * ファイル更新 権限判定
	 * @param results
	 * @return boolean
	 */
	public boolean isEdit(Map<String,Object> results){
		return userInfoDto.memberId.equals(results.get("entid"));
	}
	
	/**
	 * 共通 カテゴリ取得 (プルダウン)
	 * @return　List<BeanMap>
	 */
	public List<BeanMap> selMstCategoryList(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("categoryid", null);
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selMstCategory", params)
					.getResultList();
	}
	
	/**
	 * ファイル登録 ファイル登録
	 * @param entfileFrom
	 */
	public void insertFile(EntfileForm entfileForm){
		// ファイルIDセット
		String fileid = jdbcManager
							.selectBySqlFile(Fileinfo.class, "/data/selNextFileid")
							.getSingleResult().fileid;
		// 履歴NOセット
		Integer historyno = 1;
		// カテゴリIDセット
		setCategoryid(entfileForm);
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		
		// ファイルアップロード
		Map<String,Object> uploadMap = new HashMap<String,Object>();
		uploadMap.put("fileid"   , fileid);
		uploadMap.put("historyno", historyno);
		uploadMap.put("filename" , entfileForm.filename);
		upload(uploadMap, entfileForm);
		
		Fileinfo f = new Fileinfo();
		f.fileid      = fileid;
		f.historyno   = historyno;
		f.categoryid  = entfileForm.categoryid;
		f.title       = entfileForm.filetitle;
		f.explanation = entfileForm.explanation;
		f.version     = entfileForm.version;
//		f.content     = null;
		f.filename    = entfileForm.filename.getFileName();
		f.EExtension  = getExtension(entfileForm.filename.getFileName());
		f.download    = 0;
		f.pubfile     = entfileForm.pubfile;
		f.filesize    = entfileForm.filename.getFileSize();
		f.entid       = userInfoDto.memberId;
		f.entdate     = ts;
		f.updid       = userInfoDto.memberId;
		f.upddate     = ts;
		
		jdbcManager.insert(f).execute();
		// ファイルIDをActionFormにセット
		entfileForm.fileid = fileid;
	}
	
	/**
	 * ファイル更新 ファイル更新
	 * @param entfileFrom
	 */
	public void updateFile(EntfileForm entfileForm){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fileid", entfileForm.fileid);
		// 履歴NOセット
		Integer historyno = jdbcManager
								.selectBySqlFile(Fileinfo.class, "/data/selNextHistoryno", params)
								.getSingleResult().historyno;
		// カテゴリIDセット
		setCategoryid(entfileForm);
		// 公開範囲セット
		setPubfile(entfileForm);
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		
		// ファイルアップロード
		Map<String,Object> uploadMap = new HashMap<String,Object>();
		uploadMap.put("fileid"   , entfileForm.fileid);
		uploadMap.put("historyno", historyno);
		upload(uploadMap, entfileForm);
		
		Fileinfo f = new Fileinfo();
		f.fileid      = entfileForm.fileid;
		f.historyno   = historyno;
		f.categoryid  = entfileForm.categoryid;
		f.title       = entfileForm.filetitle;
		f.explanation = entfileForm.explanation;
		f.version     = entfileForm.version;
//		f.content     = null;
		f.filename    = entfileForm.filename.toString();
		f.EExtension  = getExtension(entfileForm.filename.getFileName());
		f.pubfile     = entfileForm.pubfile;
		f.filesize    = entfileForm.filename.getFileSize();
		f.upddate     = ts;
		f.updid       = userInfoDto.memberId;
		
		jdbcManager.updateBySqlFile("/data/insFileHistory", f).execute();
		jdbcManager.updateBySqlFile("/data/updFile", f).execute();
	}
	
	/**
	 * 共通 カテゴリ登録
	 * @param entfileForm
	 */
	public void setCategoryid(EntfileForm entfileForm){
		if(!StringUtils.isNullOrEmpty(entfileForm.inputcategory)){
			// カテゴリ名重複チェック
			List<BeanMap> categoryList = selMstCategoryList();
			boolean isContains = false;
			for (BeanMap beanMap : categoryList){
				if(beanMap.get("categoryname").equals(entfileForm.inputcategory)){
					entfileForm.categoryid = beanMap.get("categoryid").toString();
					isContains = true;
					break;
				}
			}
			// 未登録の場合は新規登録
			if(!isContains){
				insertMstCategory(entfileForm);
			}
		}
	}
	
	/**
	 * 共通 カテゴリID取得
	 * @return categoryid
	 */
	public String selNextCategoryid(){
		return jdbcManager
					.selectBySql(String.class, "SELECT CASE WHEN MAX(CATEGORYID) IS NULL THEN '001' ELSE LPAD(TO_NUMBER(MAX(CATEGORYID), '000')+1,3,'0') END AS CATEGORYID FROM MST_CATEGORY")
					.getSingleResult();
	}
	
	/**
	 * 共通 カテゴリ新規登録
	 * @param entfileFrom
	 */
	public void insertMstCategory(EntfileForm entfileForm){
		// カテゴリIDセット
		String categoryid = selNextCategoryid();
		Timestamp ts = new Timestamp((new java.util.Date()).getTime());
		MstCategory mc = new MstCategory();
		mc.categoryid   = categoryid;
		mc.categoryname = entfileForm.inputcategory;
		mc.entid        = userInfoDto.memberId;
		mc.entdate      = ts;
		mc.updid        = userInfoDto.memberId;
		mc.upddate      = ts;
		jdbcManager.insert(mc).execute();
		// カテゴリIDをActionFormにセット
		entfileForm.categoryid = categoryid;
	}
	
	/**
	 * 共通 拡張子取得<br>
	 * ※「.」は含まない
	 * @param filename
	 * @return String
	 */
	public String getExtension(String filename){
		String rtnStr = null;
		if(!StringUtils.isNullOrEmpty(filename)){
			int point = filename.lastIndexOf(".");
			if(point != -1){
				// 文字列を小文字に
				rtnStr = filename.substring(point + 1).toLowerCase();
			}
		}
		return rtnStr;
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
	 * ファイル更新 公開範囲取得
	 * @param entfileForm
	 */
	public void setPubfile(EntfileForm entfileForm){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fileid", entfileForm.fileid);
		Map<String,Object> fileList = new HashMap<String,Object>();
		fileList = jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selEntfilePubfile", params)
					.getSingleResult();
		// 公開範囲セット (更新権限がない場合はDB値をセット)
		if(fileList != null	&& !isEdit(fileList)){
			entfileForm.pubfile = fileList.get("pubfile").toString();
		}
	}
	
	/**
	 * ファイルアップロード オリジナルファイル名取得<br>
	 * ※取得できない場合はnullを返却
	 * @param fileid
	 * @param historyno
	 * @return filename
	 */
	public String selFileName(Object fileid, Object historyno){
		String fileName = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fileid"      , fileid);
		params.put("historyno"   , historyno);
		params.put("updid"       , null);
		params.put("fileAuthOpen", null);
		Fileinfo fileList = new Fileinfo();
		fileList = jdbcManager
					.selectBySqlFile(Fileinfo.class, "/data/selFileName", params)
					.getSingleResult();
		if(fileList != null){
			fileName = fileList.filename;
		}
		return fileName;
	}
	
	/**
	 * ファイルアップロード ファイルアップロード<br>
	 * ※DB更新前に行わないと履歴がとれないので注意
	 * @param uploadMap
	 * @param entfileForm 
	 */
	public void upload(Map<String,Object> uploadMap, EntfileForm entfileForm){
		String Path = appDefDto.FP_CMN_CONTENTS_DIR + appDefDto.FP_CMN_FILE_DIR;
		CmnUtility.cmnMakeDir(Path);
		// 更新時はムーブ処理を行う (リネームしてコピー後、元ファイルを削除)
		if(appDefDto.FP_CMN_FILE_REGIST_EDIT.equals(entfileForm.regist)){
			String orgFileName = selFileName(uploadMap.get("fileid"), 1);
			String input = generateFileName(uploadMap.get("fileid"), 1, orgFileName);
			String output = generateFileName(uploadMap.get("fileid"), uploadMap.get("historyno"), orgFileName);
			CmnUtility.copy(Path + input, Path + output);
			deleteFile(Path + input);
		}
		// ファイルアップロード
		CmnUtility.uploadFile(Path + generateFileName(uploadMap.get("fileid"), 1, entfileForm.filename), entfileForm.filename);
	}
	
	/**
	 * ファイルアップロード ファイル名生成<br><br>
	 * 命名規則<br>
	 * fileid_historyno_filename
	 * @param fileid
	 * @param historyno
	 * @param filename
	 * @return filename
	 */
	public String generateFileName(Object fileid, Object historyno, Object filename){
		return
			fileid
			+ appDefDto.FP_CMN_FILENAME_DELIMITER
			+ historyno
			+ appDefDto.FP_CMN_FILENAME_DELIMITER
			+ filename;
	}
	
	/**
	 * ファイルアップロード ファイル削除
	 * @param Path
	 */
	public void deleteFile(String Path){
		File file = new File(Path);
		file.delete();
	}
}