package frontier.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.JdbcManager;

import twitter4j.TwitterException;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.entity.Diary;
import frontier.entity.Members;
import frontier.form.pc.DiaryForm;
import frontier.form.m.DiaryMForm;

public class DiaryCmnService {
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;
	  
	/**
	 * DB格納用、画像ファイル名生成
	 * @param diaryid 日記ID
	 * @param photo   画像入力エリアの値
	 * @param mid 　　　  メンバーID
	 * @param picno   画像NO
	 * @param no      コメントNO
	 * @return String
	 */
	public String getFileName(int diaryid,FormFile photo,String mid,String picno,int no){
		String picName = null;
	    String today = CmnUtility.getToday("yyyyMMddHHmmss");
	    String path = appDefDto.FP_CMN_DIARY_BEFORE_PATH + mid + appDefDto.FP_CMN_DIARY_AFTER_PATH;
	    
	    //画像の入力がある場合のみ生成
	    if (photo.getFileSize()>0) {
	    	picName = path + diaryid +"_"+ CmnUtility.stringFormat("000", no) +"_"+picno+"_"+today+".jpg";
	    }

		return picName;
	}
	
	//画像アップロード
	public void upload(String mid,FormFile photo,String photoName){
		//出力先ディレクトリ
		String dir = appDefDto.FP_CMN_CONTENTS_DIR +"img/mem/"+mid;
		//ディレクトリ作成
		CmnUtility.makeDir(dir,"diary");
		dir = dir+"/diary";
		
		//画像アップロード処理
		//null対策
		if(photo!=null){
			if(photo.getFileSize()>0){
				String fileName = photoName.substring(photoName.indexOf("dir")+4);
				uploadFile(photo,dir,fileName);
			}
		}
	}
	
    private void uploadFile(FormFile file,String dir,String fileName){
	    //各サイズ毎に画像を生成
    	String basePath = dir+"/pic640/"+fileName;
	    String path = dir+"/pic640/"+fileName;
	    //最初のみアップロード
	    CmnUtility.uploadFile(path,file);
	    
	    path = dir+"/pic480/"+fileName;
//	    CmnUtility.copy(basePath,path);
	    try {
			CmnUtility.resize(path,480,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    path = dir+"/pic240/"+fileName;
//	    CmnUtility.copy(basePath,path);
	    try {
			CmnUtility.resize(path,240,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    path = dir+"/pic180/"+fileName;
//	    CmnUtility.copy(basePath,path);
	    try {
			CmnUtility.resize(path,180,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    path = dir+"/pic120/"+fileName;
//	    CmnUtility.copy(basePath,path);
	    try {
			CmnUtility.resize(path,120,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    path = dir+"/pic76/"+fileName;
//	    CmnUtility.copy(basePath,path);
	    try {
			CmnUtility.resize(path,76,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    path = dir+"/pic60/"+fileName;
//	    CmnUtility.copy(basePath,path);
	    try {
			CmnUtility.resize(path,60,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    path = dir+"/pic42/"+fileName;
//	    CmnUtility.copy(basePath,path);
	    try {
			CmnUtility.resize(path,42,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //最後に元となる画像をリサイズ
	    try {
			CmnUtility.resize(basePath,640,basePath,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
	//画像ファイルチェック
	public ActionMessages checkFile(DiaryForm diaryForm){
		ActionMessages errors = new ActionMessages();
    	FormFile photo1 = diaryForm.photo1;
    	FormFile photo2 = diaryForm.photo2;
    	FormFile photo3 = diaryForm.photo3;
		
    	//null対策
    	if(diaryForm.photo1!=null){
	    	if(photo1.getFileName().length()>0){
	        	if(photo1.getFileSize()==0){
	            	//ファイルサイズチェック
	        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        				"errors.upload.other",new Object[] {"1"}));
	            }else if(!photo1.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo1.getContentType().equalsIgnoreCase("image/jpeg")){
	            //ファイル形式チェック
	        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        		"errors.upload.type",new Object[] {"1"}));
	        	}
	    	}
    	}
    	
    	//画像２
    	//null対策
    	if(diaryForm.photo2!=null){
	    	if(photo2.getFileName().length()>0){
	        	if(photo2.getFileSize()==0){
	            	//ファイルサイズチェック
	        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        				"errors.upload.other",new Object[] {"2"}));
	            }else if(!photo2.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo2.getContentType().equalsIgnoreCase("image/jpeg")){
	            //ファイル形式チェック
	        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        		"errors.upload.type",new Object[] {"2"}));
	        	}
	    	}
    	}
    	
    	//画像３
    	//null対策
    	if(diaryForm.photo3!=null){
	    	if(photo3.getFileName().length()>0){
	        	if(photo3.getFileSize()==0){
	            	//ファイルサイズチェック
	        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        				"errors.upload.other",new Object[] {"3"}));
	            }else if(!photo3.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo3.getContentType().equalsIgnoreCase("image/jpeg")){
	            //ファイル形式チェック
	        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        		"errors.upload.type",new Object[] {"3"}));
	        	}
	    	}
    	}
		
		return errors;
	}
	
	
	/**
     * 日記を一件だけ検索する。
     * @param mid メンバーID
     * @param diaryid    日記ID 
     * @return Diaryクラス（日記一件の検索結果）
     */  
	  public Diary getResultByDiary(String mid,Integer diaryid) {
		  	return jdbcManager.from(Diary.class)
		  				.where("mid = ? and diaryid = ? and comno = 0",mid,diaryid)
		  				.getSingleResult();
	  }
	
	//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ モバイル版 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	//画像ファイルチェック
	public ActionMessages checkFile(DiaryMForm diaryForm){
		ActionMessages errors = new ActionMessages();
    	FormFile photo1 = diaryForm.photo1;
    	FormFile photo2 = diaryForm.photo2;
    	FormFile photo3 = diaryForm.photo3;
		
    	if(photo1.getFileName().length()>0){
        	if(photo1.getFileSize()==0){
            	//ファイルサイズチェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        				"errors.upload.other",new Object[] {"1"}));
            }else if(!photo1.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo1.getContentType().equalsIgnoreCase("image/jpeg")){
            //ファイル形式チェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        		"errors.upload.type",new Object[] {"1"}));
        	}
    	}
    	
    	//画像２
    	if(photo2.getFileName().length()>0){
        	if(photo2.getFileSize()==0){
            	//ファイルサイズチェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        				"errors.upload.other",new Object[] {"2"}));
            }else if(!photo2.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo2.getContentType().equalsIgnoreCase("image/jpeg")){
            //ファイル形式チェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        		"errors.upload.type",new Object[] {"2"}));
        	}
    	}
    	
    	//画像３
    	if(photo3.getFileName().length()>0){
        	if(photo3.getFileSize()==0){
            	//ファイルサイズチェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        				"errors.upload.other",new Object[] {"3"}));
            }else if(!photo3.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo3.getContentType().equalsIgnoreCase("image/jpeg")){
            //ファイル形式チェック
        		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        		"errors.upload.type",new Object[] {"3"}));
        	}
    	}
		
		return errors;
	}
}
