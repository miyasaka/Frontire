package frontier.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.h2.util.StringUtils;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.util.ResponseUtil;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.FileDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Fileinfo;
import frontier.form.pc.FileForm;

public class FileService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public FileDto fileDto;
	@Resource
	protected JdbcManager jdbcManager;	
	
	/**
	 * 共通 UserInfoDtoセット<br>
	 * ※初期表示時のみ実行
	 */
	public void initUserInfoDto(){
		// 訪問中のメンバーIDに自分のメンバーIDをセット
		if(!userInfoDto.memberId.equals(userInfoDto.visitMemberId)){
			userInfoDto.visitMemberId = userInfoDto.memberId;
		}
	}
	
	/**
	 * ファイル一覧 ファイル一覧取得
	 * @param fileForm
	 * @return List<BeanMap>
	 */
	public List<BeanMap> selFileList(FileForm fileForm){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("condition", fileForm.condition);
		params.put("sortcd"   , fileForm.sortcd);
		params.put("limit"    , appDefDto.FP_MY_FILELIST_PGMAX);
		params.put("offset"   , fileForm.offset);
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFileList", params)
					.getResultList();
	}
	
	/**
	 * ファイル一覧 ファイル一覧件数取得
	 * @param fileForm
	 * @return Integer
	 */
	public Integer selCntFileList(FileForm fileForm){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("condition", fileForm.condition);
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFileList", params)
					.getResultList().size();
	}
	
	/**
	 * ファイル詳細 ファイル詳細取得
	 * @param fileForm
	 * @return List<BeanMap>
	 */
	public List<BeanMap> selFileView(FileForm fileForm){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fileid", fileForm.fileid);
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFileView", params)
					.getResultList();
	}
	
	/**
	 * 共通 ファイル情報加工
	 * <ul>
	 * <li>ダウンロード権限判定</li>
	 * <li>ファイルサイズ計算</li>
	 * <li>絵文字装飾</li>
	 * </ul>
	 * @param results 検索結果
	 * @param isEmoji 絵文字装飾実施有無
	 */
	public void setFileDecoration(List<BeanMap> results, boolean isEmoji){
		for (BeanMap beanMap : results){
			beanMap.put("isAuth"      , isAuth(beanMap));
			beanMap.put("filesizeCalc", calculateFilesize((Integer) beanMap.get("filesize")));
			if(isEmoji){
				beanMap.put("explanationhtml", setEmojiSanitizing(beanMap.get("explanation")));
			}
		}
	}
	
	/**
	 * ファイル一覧 Ajax ファイル情報加工 (BeanMap版)
	 * <ul>
	 * <li>ファイルサイズ計算</li>
	 * <li>HTMLタグ無効化</li>
	 * <li>日時フォーマット変換</li>
	 * </ul>
	 * @param results 検索結果
	 */
	public void setFileDecoration(BeanMap results){
		//results.put("isAuth", isAuth(results));
		results.put("filesizeCalc"    , calculateFilesize((Integer) results.get("filesize")));
		results.put("ViewTitle"       , setSanitizing(results.get("title")));
		results.put("ViewTitleorg"    , setSanitizing(results.get("titleorg")));
		results.put("ViewCategoryname", setSanitizing(results.get("categoryname")));
		results.put("ViewJextension"  , setSanitizing(results.get("jextension")));
		results.put("ViewUpddate"     , format(results));
		results.put("ViewEntname"     , setSanitizing(results.get("entname")));
	}
	
	/**
	 * ファイル一覧 Ajax HTMLタグ無効化
	 * @param obj
	 * @return String
	 */
	public String setSanitizing(Object obj){
		String strObj = null;
		if(obj != null){
			strObj = CmnUtility.htmlSanitizing(obj.toString());
		}
		return strObj;
	}
	
	/**
	 * ファイル一覧 Ajax 日時フォーマット変換
	 * @param obj
	 * @return String
	 */
	public String format(BeanMap results){
		String rtnStr = null;
		Object upddate = results.get("upddate");
		Object currentflg = results.get("currentflg");
		if(upddate != null){
			rtnStr = upddate.toString();
			if(rtnStr.length() == 12){
				//String yy = rtnStr.substring(2,4);
				String MM = rtnStr.substring(4,6);
				String dd = rtnStr.substring(6,8);
				String HH = rtnStr.substring(8,10);
				String mm = rtnStr.substring(10,12);
				// 更新日が当日の場合は時分をセット
				if("1".equals(currentflg)){
					rtnStr = HH+":"+mm;
				// 更新日が当日でない場合は月日をセット
				}else{
					rtnStr = MM+"/"+dd;
				}
			}
		}
		return rtnStr;
	}
	
	/**
	 * ファイル一覧 Ajax 並び順セット<br>
	 * <strong>sortname</strong><br>
	 * "1"	FILENAME<br>
	 * "2"	CATEGORYNAME<br>
	 * "3"	JEXTENSION<br>
	 * "4"	UPDDATE<br>
	 * "5"	DOWNLOAD<br>
	 * "6"	NICKNAME<br>
	 * "7"	FILESIZE<br>
	 * <br>
	 * <strong>sortcd</strong><br>
	 * 1	TITLE DESC<br>
	 * 2	TITLE ASC<br>
	 * 3	CATEGORYNAME DESC<br>
	 * 4	CATEGORYNAME ASC<br>
	 * 5	J_EXTENSION DESC<br>
	 * 6	J_EXTENSION ASC<br>
	 * 7	UPDDATE DESC<br>
	 * 8	UPDDATE ASC<br>
	 * 9	DOWNLOAD DESC<br>
	 * 10	DOWNLOAD ASC<br>
	 * 11	NICKNAME DESC<br>
	 * 12	NICKNAME ASC<br>
	 * 13	FILESIZE DESC<br>
	 * 14	FILESIZE ASC<br>
	 * @param fileForm
	 */
	public void setSortcd(FileForm fileForm){
		String sortname = fileForm.sortname;
		Integer sortcd = fileForm.sortcd;
		Integer rtnInt = 0;
		// 初期値は降順をセット
		if(("1").equals(sortname)){if(sortcd !=  1){rtnInt =  1;}else{rtnInt =  2;}}
		if(("2").equals(sortname)){if(sortcd !=  3){rtnInt =  3;}else{rtnInt =  4;}}
		if(("3").equals(sortname)){if(sortcd !=  5){rtnInt =  5;}else{rtnInt =  6;}}
		if(("4").equals(sortname)){if(sortcd !=  7){rtnInt =  7;}else{rtnInt =  8;}}
		if(("5").equals(sortname)){if(sortcd !=  9){rtnInt =  9;}else{rtnInt = 10;}}
		if(("6").equals(sortname)){if(sortcd != 11){rtnInt = 11;}else{rtnInt = 12;}}
		if(("7").equals(sortname)){if(sortcd != 13){rtnInt = 13;}else{rtnInt = 14;}}
		fileForm.sortcd = rtnInt;
	}
	
	/**
	 * ファイル一覧 Ajax クラスセット<br>
	 * 1	FILENAME<br>
	 * 2	CATEGORYNAME<br>
	 * 3	JEXTENSION<br>
	 * 4	UPDDATE<br>
	 * 5	DOWNLOAD<br>
	 * 6	NICKNAME<br>
	 * 7	FILESIZE<br>
	 * 8	none
	 * @param fileForm
	 * @return classMap
	 */
	public Map<String,Object> setClass(FileForm fileForm){
		Integer intSortcd = fileForm.sortcd;
		Map<String,Object> classMap = new HashMap<String,Object>();
		String strSelected = " selected";
		classMap.put("class1", "th1");
		classMap.put("class2", "th2");
		classMap.put("class3", "th3");
		classMap.put("class4", "th4");
		classMap.put("class5", "th5");
		classMap.put("class6", "th6");
		classMap.put("class7", "th7");
		classMap.put("arrow1", "");
		classMap.put("arrow2", "");
		classMap.put("arrow3", "");
		classMap.put("arrow4", "");
		classMap.put("arrow5", "");
		classMap.put("arrow6", "");
		classMap.put("arrow7", "");
		if(intSortcd == 1 || intSortcd == 2){
			classMap.put("class1", classMap.get("class1") + strSelected);
			classMap.put("arrow1", setArrow(intSortcd, 1));
		}else if(intSortcd == 3 || intSortcd == 4){
			classMap.put("class2", classMap.get("class2") + strSelected);
			classMap.put("arrow2", setArrow(intSortcd, 3));
		}else if(intSortcd == 5 || intSortcd == 6){
			classMap.put("class3", classMap.get("class3") + strSelected);
			classMap.put("arrow3", setArrow(intSortcd, 5));
		}else if(intSortcd == 7 || intSortcd == 8){
			classMap.put("class4", classMap.get("class4") + strSelected);
			classMap.put("arrow4", setArrow(intSortcd, 7));
		}else if(intSortcd == 9 || intSortcd == 10){
			classMap.put("class5", classMap.get("class5") + strSelected);
			classMap.put("arrow5", setArrow(intSortcd, 9));
		}else if(intSortcd == 11 || intSortcd == 12){
			classMap.put("class6", classMap.get("class6") + strSelected);
			classMap.put("arrow6", setArrow(intSortcd, 11));
		}else if(intSortcd == 13 || intSortcd == 14){
			classMap.put("class7", classMap.get("class7") + strSelected);
			classMap.put("arrow7", setArrow(intSortcd, 13));
		}
		return classMap;
	}
	
	/**
	 * ファイル一覧 Ajax 矢印セット
	 * @param sortcd
	 * @param intCompare
	 * @return String
	 */
	public String setArrow(Integer sortcd, Integer intCompare){
		String rtnStr = "";
		if(sortcd == intCompare){
			rtnStr = "<span>▼</span>";
		}else{
			rtnStr = "<span>▲</span>";
		}
		return rtnStr; 
	}
	
	/**
	 * 共通 ダウンロード権限判定
	 * @param beanMap
	 * @return boolean
	 */
	public boolean isAuth(BeanMap beanMap){
		return userInfoDto.memberId.equals(beanMap.get("updid"))
				|| appDefDto.FP_CMN_FILE_AUTH_OPEN[0].equals(beanMap.get("pubfile"));
	}
	
	/**
	 * 共通 ファイルサイズ計算<br>
	 * ※必要に応じてGB算出処理を追加してください
	 * @param obj
	 * @return String
	 */
	public String calculateFilesize(Object obj){
		String ret = null;
		if(obj != null){
			Integer intObj = (Integer) obj;
			BigDecimal bd = new BigDecimal(intObj / 1024.0);
			if(bd.intValue() < 1024){
				ret = bd.setScale(1, RoundingMode.CEILING) + "KB";
			}else{
				bd = new BigDecimal(bd.intValue() / 1024.0);
				if(bd.intValue() < 1024){
					ret = bd.setScale(1, RoundingMode.CEILING) + "MB";
				}else{
					ret = "-";
				}
			}			
		}
		return ret;
	}
	
	/**
	 * ファイル詳細 絵文字装飾
	 * @param obj
	 * @return String
	 */
	public String setEmojiSanitizing(Object obj){
		String rtnStr = null;
		if(obj != null){
			rtnStr = obj.toString();
			rtnStr = CmnUtility.htmlSanitizing(rtnStr);
			rtnStr = CmnUtility.reSanitizing(rtnStr);
			//rtnStr = CmnUtility.convURL(rtnStr);
			rtnStr = CmnUtility.replaceEmoji(rtnStr, appDefDto.FP_CMN_EMOJI_IMG_PATH, appDefDto.FP_CMN_EMOJI_XML_PATH);
		}
		return rtnStr;
	}
	
	/**
	 * 共通 カテゴリ取得 (SideBox)
	 * @return List<BeanMap>
	 */
	public List<BeanMap> selFileSideBoxCategoryList(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("condition", null);
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selFileSideBoxCategoryList", params)
					.getResultList();
	}
	
	/**
	 * 共通 選択しているカテゴリをセット (SideBox)
	 * @param fileDto
	 * @param fileSideBoxCategoryList
	 * @return boolean
	 */
	public boolean setSelected(FileDto fileDto, List<BeanMap> fileSideBoxCategoryList){
		boolean isCategory = false;
		for (BeanMap beanMap : fileSideBoxCategoryList){
			boolean isSelected = false;
			if(!StringUtils.isNullOrEmpty(fileDto.condition)){
				isSelected = beanMap.get("categoryid").equals(fileDto.condition);
				beanMap.put("isSelected", isSelected);
				if(isSelected){
					isCategory = true;
				}
			}else{
				beanMap.put("isSelected", isSelected);
			}
		}
		return isCategory;
	}
	
	/**
	 * ファイル一覧 カテゴリ取得 (検索結果見出し)
	 * @param fileForm
	 * @param fileSideBoxCategoryList
	 * @return categoryname
	 */
	public String selCategoryName(FileForm fileForm, List<BeanMap> fileSideBoxCategoryList){
		boolean isContains = false;
		String categoryName = null;
		if(!StringUtils.isNullOrEmpty(fileForm.condition)){
			for (BeanMap beanMap : fileSideBoxCategoryList){
				if(beanMap.get("categoryid").equals(fileForm.condition)){
					isContains = true;
					categoryName = beanMap.get("categorynameorg").toString();
					break;
				}
			}
		}
		if(!isContains){
			categoryName = "すべて";
		}
		return categoryName;
	}
	
	/**
	 * 共通 型判定<br>
	 * ※不正な値の場合は0を返却
	 * @param str
	 * @return Integer
	 */
	public Integer convInt(String str){
		Integer rtnInt = 0;
		try{
			if(!StringUtils.isNullOrEmpty(str)){
				rtnInt = Integer.valueOf(str);
			}
		}catch (NumberFormatException e){
			e.printStackTrace();
		}
		return rtnInt;
	}
	
	/**
	 * ファイルダウンロード ファイルダウンロード
	 * @param fileForm
	 * @param filename
	 * @return boolean
	 * @throws Exception
	 */
	public boolean download(FileForm fileForm, String filename) throws Exception{
		boolean isSuccess = false;
		HttpServletResponse res = ResponseUtil.getResponse();
		File file = new File(getFilePath(fileForm, filename));
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			// HTTPヘッダの出力
			res.setContentType("application/octet-stream");
			res.setHeader("Content-disposition", "attachment; filename=\"" +
					new String(filename.getBytes("Shift_JIS"), "ISO-8859-1") + "\"");
			res.setContentLength((int) file.length());
			res.setHeader("Expires", "0");
			res.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
			res.setHeader("Pragma", "private");
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(res.getOutputStream());

			byte buf[] = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
			isSuccess = true;
		} catch (Exception e) {
			// ファイルダウンロード用の出力済みHTTPヘッダをリセット
			res.reset();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
		return isSuccess;
	}
	
	/**
	 * ファイルダウンロード オリジナルファイル名取得
	 * @param fileForm
	 * @return fileName
	 */
	public String selFileName(FileForm fileForm){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fileid"      , fileForm.fileid);
		params.put("historyno"   , fileForm.historyno);
		params.put("updid"       , userInfoDto.memberId);
		params.put("fileAuthOpen", appDefDto.FP_CMN_FILE_AUTH_OPEN[0]);
		Fileinfo fileList = new Fileinfo();
		fileList = jdbcManager
						.selectBySqlFile(Fileinfo.class, "/data/selFileName", params)
						.getSingleResult();
		String fileName = null;
		if(fileList != null){
			fileName = fileList.filename;
		}
		return fileName;
	}
	
	/**
	 * ファイルダウンロード ファイルパス取得
	 * @param fileForm
	 * @param filename
	 * @return Path
	 */
	public String getFilePath(FileForm fileForm, String filename){
		return
			appDefDto.FP_CMN_CONTENTS_DIR
			+ appDefDto.FP_CMN_FILE_DIR
			+ fileForm.fileid
			+ appDefDto.FP_CMN_FILENAME_DELIMITER
			+ fileForm.historyno
			+ appDefDto.FP_CMN_FILENAME_DELIMITER
			+ filename;
	}
	
	/**
	 * ファイルダウンロード ファイルダウンロード数更新
	 * @param fileFrom
	 */
	public void updateFileDownload(FileForm fileForm){
		Fileinfo f = new Fileinfo();
		f.fileid    = fileForm.fileid;
		f.historyno = fileForm.historyno;
		jdbcManager.updateBySqlFile("/data/updFileDownload", f).execute();
	}
}