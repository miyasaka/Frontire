package frontier.action.pc.com;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.EntbbsForm;
import frontier.service.CommunityService;

public class EnttopicAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public CommunityDto communityDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected CommunityService communityService;
	@ActionForm
	@Resource
	protected EntbbsForm entbbsForm;
	public List<BeanMap> result;
	// entrytype:1 トピック
	private String entrytype = "1";
	public String vMode;
	public String mLevel;

	// ■■■■■■　　　　新規処理　　　　■■■■■■
	// ■登録画面・初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// パラメタ初期化
		reset();
		// 新規作成画面
		entbbsForm.edittype = "0"; // 0:新規 1:編集
		
		// init処理
		init();
		return "index.jsp";
	}

	// ■この内容で登録するボタン押下時
	@Execute(validate="chkPic",input="errorNew")
	public String insfinish() throws IOException, Exception{
		// init処理
		init();
		// トピックの登録
		communityService.insTopic(
			communityDto.cid,
			entbbsForm.title,
			entbbsForm.comment,
			entbbsForm.picpath1,
			entbbsForm.picpath2,
			entbbsForm.picpath3,
			entbbsForm.picnote1,
			entbbsForm.picnote2,
			entbbsForm.picnote3
		);
		return "/pc/com/topic/"+communityDto.cid;
	}

	// ■■■■■■　　　　編集処理　　　　■■■■■■
	// ■編集画面・初期表示
	@Execute(validator=false,urlPattern="edit/{cid}/{bbsid}")
	public String edit(){
		
	    //表示権限チェック
	    if(check().equals("NG")){
	    	mLevel = "1";
	    	//return "error.jsp";
	    }
	    logger.debug("****** イベント ********"+mLevel);
		
		//編集ではinitの前に設定しないといけない。
		entbbsForm.edittype = "1"; // 0:新規 1:編集

		// init処理
		init();
		// 編集用データの取得
		setTopicDt();
		// 編集画面へ
		//return "edit.jsp";
		return "index.jsp";
	}
	
	// 編集の権限チェック
	public String check(){
		// 掲示板・親情報の取得
		result = communityService.getOyaView(
			communityDto.cid,
			entrytype,
			entbbsForm.bbsid
		);
		
		if(!result.get(0).get("editflg").toString().equals("1")){
			return "NG";
		}
		return "OK";
	}

	
	
	// ■この内容で編集するボタン押下時
	@Execute(validate="chkPic",input="errorEdit")
	public String editfinish() throws IOException, Exception{
		// init処理
		init();
		// トピックの編集
		communityService.editTopic(
			communityDto.cid,
			entbbsForm.bbsid,
			entrytype,
			0,
			entbbsForm.title,
			entbbsForm.comment,
			entbbsForm.picpath1,
			entbbsForm.picpath2,
			entbbsForm.picpath3,
			entbbsForm.picnote1,
			entbbsForm.picnote2,
			entbbsForm.picnote3
		);
		return "/pc/com/topic/"+communityDto.cid;
	}

	// ■画像の「削除」ボタン押下時(画像1）
	@Execute(validator=false)
	public String delpic1(){
		delpic("1");
		// 編集表示処理へ
		//return "edit.jsp";
		return "index.jsp";
	}

	// ■画像の「削除」ボタン押下時(画像2）
	@Execute(validator=false)
	public String delpic2(){
		delpic("2");
		// 編集表示処理へ
		//return "edit.jsp";
		return "index.jsp";
	}

	// ■画像の「削除」ボタン押下時(画像3）
	@Execute(validator=false)
	public String delpic3(){
		delpic("3");
		// 編集表示処理へ
		//return "edit.jsp";
		return "index.jsp";
	}

	// ■■■■■■　　　　削除処理　　　　■■■■■■
	// ■削除画面・初期表示
	@Execute(validator=false,urlPattern="edit/{cid}/{bbsid}")
	public String deltopic(){
		// init処理
		init();
		// 削除確認用データの取得
		setTopicDt();
		// 削除画面へ
		return "del.jsp";
	}

	// ■確認画面で「やめる」ボタン押下時
	@Execute(validator=false)
	public String cancel(){
		// init処理
		init();
		// 編集用データの取得
		setTopicDt();
		// 編集画面へ
		//return "edit.jsp";
		return "index.jsp";
	}
	
	// ■確認画面で「削除する」ボタン押下時
	@Execute(validator=false)
	public String delete(){
		// init処理
		init();
		// 掲示板削除処理
		communityService.delbbs(communityDto.cid,Integer.valueOf(entbbsForm.bbsid));
		// 一覧表示処理へ
		return "/pc/com/topic/"+communityDto.cid+"?redirect=true";
	}
	
	// 画像削除処理
	private void delpic(String picno){
		// init処理
		init();
		// 画像削除処理
		communityService.delpic(
			communityDto.cid,
			entbbsForm.bbsid,
			entrytype,
			picno
		);
		// コミュニティ情報のセット
		setTopicDt();
	}
	
	// トピック情報の取得＆セット
	private void setTopicDt(){
		// 編集用、トピック情報の取得
		// ■トピック情報の取得(mapオブジェクト)
		result = communityService.getOyaView(
			communityDto.cid,
			entrytype,
			entbbsForm.bbsid
		);
		if(result.size()==1){
			entbbsForm.title       = result.get(0).get("title").toString();
			entbbsForm.comment     = result.get(0).get("comment").toString();
			entbbsForm.strpicpath1 = result.get(0).get("pic1") != null?result.get(0).get("pic1").toString():"";
			entbbsForm.strpicpath2 = result.get(0).get("pic2") != null?result.get(0).get("pic2").toString():"";
			entbbsForm.strpicpath3 = result.get(0).get("pic3") != null?result.get(0).get("pic3").toString():"";
			entbbsForm.picnote1    = result.get(0).get("picnote1").toString();
			entbbsForm.picnote2    = result.get(0).get("picnote2").toString();
			entbbsForm.picnote3    = result.get(0).get("picnote3").toString();
		}
	}
	
	// エラー時の遷移先(新規用)
	@Execute(validator=false)
	public String errorNew(){
		// init処理
		init();
		// 自画面に戻る
		return "index.jsp";
	}

	// エラー時の遷移先(編集用)
	@Execute(validator=false)
	public String errorEdit(){
		// init処理
		init();
		// 自画面に戻る
		//return "edit.jsp";
		return "index.jsp";
	}
	
	// エラーチェック
	public ActionMessages chkPic(){
		ActionMessages errors = new ActionMessages();
		FormFile picpath1 = entbbsForm.picpath1;
		FormFile picpath2 = entbbsForm.picpath2;
		FormFile picpath3 = entbbsForm.picpath3;
		//null対策
		if(picpath1!=null){
			// 画像パス名の長さが0以上ならチェック
			if(picpath1.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath1,"1");
			}
		}
		//null対策
		if(picpath2!=null){
			// 画像パス名の長さが0以上ならチェック
			if(picpath2.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath2,"2");
			}
		}
		//null対策
		if(picpath3!=null){
			// 画像パス名の長さが0以上ならチェック
			if(picpath3.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath3,"3");
			}
		}
		return errors;
	}
	
	// 初期化処理
	private void reset(){
		entbbsForm.title   = "";
		entbbsForm.comment = "";
		entbbsForm.picnote1      = "";
		entbbsForm.picnote2      = "";
		entbbsForm.picnote3      = "";
		entbbsForm.strpicpath1   = "";
		entbbsForm.strpicpath2   = "";
		entbbsForm.strpicpath3   = "";
		entbbsForm.selmid        = "";
		entbbsForm.oyacid        = "";
		entbbsForm.oyabbsid      = "";
		entbbsForm.offset        = 0;
		entbbsForm.pgcnt         = 0;
		entbbsForm.edittype      = "";
	}
	
	// init処理
	private void init(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = entbbsForm.cid;
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
		
		//新規、編集の文言設定
		if (entbbsForm.edittype != null){
			if (entbbsForm.edittype.equals("0")){
				vMode = "作成";
			}else if(entbbsForm.edittype.equals("1")){
				vMode = "編集";				
			}
		}
	}
}
