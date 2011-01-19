package frontier.action.pc;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.PhotoForm;
import frontier.service.PhotoService;

public class PhotoalbumAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected PhotoService photoService;
	
	@ActionForm
	@Resource
	protected PhotoForm photoForm;
	
    //画面表示用
    public String vNickname;
    //フォトアルバム一覧検索結果
    public List<BeanMap> results;
    public long resultscnt;
    public long photoResultscnt;
    public boolean vUser;
    public boolean vFriend;
    //フォトアルバム閲覧（アルバム表紙）
    public List<BeanMap> coverResults;
    //フォトアルバム閲覧（フォト一覧）
    public List<BeanMap> photoResults;
    //フォトアルバム詳細(コメント一覧）
    public List<BeanMap> commentResults;
    
    //フォトアルバム一覧初期表示
	@Execute(validator=false)
	public String list(){
		//初期化処理
		initList();
		
		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//DB検索
		initListPhoto(0);
		
		return "/pc/photoalbum/list.jsp";
	}
	
	//画面再表示用
	@Execute(validator=false)
	public String reList(){
		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//DB検索
		initListPhoto(photoForm.offset);
		
		return "/pc/photoalbum/list.jsp";		
	}
	
	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setPhotoPage(1);
		
		//F5対策
		return "/pc/photoalbum/reList?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setPhotoPage(-1);		
		
		//F5対策
		return "/pc/photoalbum/reList?redirect=true";
	}
	
	//フォトアルバム閲覧画面初期表示用
	@Execute(validator=false,urlPattern="view/{pano}")
	public String view(){
		//初期化処理
		initList();
		
		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//フォトアルバム閲覧必須処理
		initViewPhoto();
		
		return "/pc/photoalbum/view.jsp";
	}

	//フォトアルバム閲覧画面再表示用
	@Execute(validator=false)
	public String reView(){
		
		//フォト系共通初期処理
		try {
			initPhoto();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//フォトアルバム閲覧必須処理
		initViewPhoto();
		
		return "/pc/photoalbum/view.jsp";
	}
	
	//日記閲覧・次を表示リンク押下時
	@Execute(validator=false)
	public String viewNxtpg(){
		//改ページ処理実行
		setPhotoViewPage(1);
		
		//F5対策
		return "/pc/photoalbum/reView?redirect=true";
	}
	
	//日記閲覧・前を表示リンク押下時
	@Execute(validator=false)
	public String viewPrepg(){
		//改ページ処理実行
		setPhotoViewPage(-1);		
		
		//F5対策
		return "/pc/photoalbum/reView?redirect=true";
	}
	
	//フォトアルバム系画面に必要な初期設定
	private void initPhoto() throws Exception{

		//メニュー出しわけ用変数設定
		setVisitMemberId();

		//自分の場合
		vNickname = userInfoDto.nickName;

		//画面表示用
		vUser = true;
		
	}
	
	//メニュー出しわけ用変数設定
	private void setVisitMemberId(){
		userInfoDto.visitMemberId = photoForm.pmid;
	}
	
	//フォトアルバム一覧画面必須処理
	private void initListPhoto(int offset){
		logger.debug(offset);
		results = photoService.selPhotoList(photoForm.pmid, offset);
		resultscnt = photoService.cntPhotoList(photoForm.pmid);
		
		//取得結果編集
		//setEmojiSanitizing("detail","viewDetail",results);
		CmnUtility.editDetail(results, "detailorg", appDefDto.FP_CMN_EMOJI_XML_PATH, appDefDto.FP_CMN_LIST_CMNTMAX, appDefDto.FP_CMN_EMOJI_IMG_PATH);
	}
	
	//フォトアルバム閲覧画面必須処理
	private void initViewPhoto(){
		coverResults = photoService.selPhotoCover(photoForm.pmid, photoForm.pano);
		photoResults = photoService.selPhoto(photoForm.pmid, photoForm.pano,photoForm.viewOffset);
		photoResultscnt = photoService.cntPhoto(photoForm.pmid, photoForm.pano);
		
		//取得結果編集
		setEmojiSanitizing("detail","viewDetail",coverResults);
		
	}

	
	//絵文字装飾とサニタイジング
	private void setEmojiSanitizing(String colmun,String viewColumn,List<BeanMap> results){
		String txt = "";
		
		//絵文字の装飾&サニタイジング
		for (int i=0;i<results.size();i++){
			//コメントを一時格納
			txt = (String)results.get(i).get(colmun);
			
			//サニタイジング
			txt = CmnUtility.htmlSanitizing(txt);
			
			//URLを<a>タグに変換
			txt = CmnUtility.convURL(txt);
			
		    //絵文字装飾
			txt = CmnUtility.replaceEmoji(txt,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
		  	
		  	//BeanMapへ格納
		  	results.get(i).put(viewColumn, txt);
		}		
	}
	
	//フォトアルバム一覧改ページ処理
	private void setPhotoPage(int num){
		try {
			// ページ遷移用の計算
			photoForm.pgcnt = photoForm.pgcnt + num;
			photoForm.offset = photoForm.pgcnt * appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX;
			//photoForm.offset = photoForm.pgcnt * 2;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			photoForm.pgcnt = 1;
			photoForm.offset = 0;
		}		
	}
	
	//フォトアルバム閲覧改ページ処理
	private void setPhotoViewPage(int num){
		try {
			// ページ遷移用の計算
			photoForm.viewPgcnt = photoForm.viewPgcnt + num;
			photoForm.viewOffset = photoForm.viewPgcnt * appDefDto.FP_MY_PHOTO_PGMAX;
			//photoForm.viewOffset = photoForm.viewPgcnt * 5;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			photoForm.viewPgcnt = 1;
			photoForm.viewOffset = 0;
		}		
	}
	
	//フォトアルバム一覧・閲覧初期化処理
	private void initList(){
		photoForm.offset = 0;
		photoForm.pgcnt = 0;
		photoForm.viewOffset = 0;
		photoForm.viewPgcnt = 0;
		photoForm.pmid = userInfoDto.memberId;
			
	}

}
