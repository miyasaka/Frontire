package frontier.action.pc.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.TopicForm;
import frontier.service.CommunityService;
import frontier.common.CmnUtility;

public class TopicAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public CommunityDto communityDto;
	// getでコミュニティIDが渡ってきた場合、設定
	public String cid;
	// entrytype:1 トピック
	private String entrytype = "1";
	public List<BeanMap> result;  // 単体用
	public List<BeanMap> results; // 複数用
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected CommunityService communityService;
	@ActionForm
    @Resource
    protected TopicForm topicForm;

	// ■■■■■■　　　　一覧系処理　　　　■■■■■■
	// ■初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// init処理
		init();
		topicForm.offset = 0;
		topicForm.pgcnt = 0;
		// 一覧表示処理へ
		return "sellist?redirect=true";
	}

	// ■次を見るリンク押下時
	@Execute(validator=false,urlPattern="nxtpg/{cid}")
	public String nxtpg(){
		try {
			// ページ遷移用の計算
			topicForm.pgcnt = topicForm.pgcnt + 1;
			topicForm.offset = topicForm.pgcnt * appDefDto.FP_COM_TOPICLIST_PGMAX;
		} catch (Exception e) {
			// 計算できない場合は初期値セット
			topicForm.pgcnt = 1;
			topicForm.offset = 0;
			// TODO: handle exception
		}
		// 一覧表示処理へ
		return "sellist?redirect=true";
	}

	// ■前を見るリンク押下時
	@Execute(validator=false,urlPattern="prepg/{cid}")
	public String prepg(){
		try {
			// ページ遷移用の計算
			topicForm.pgcnt = topicForm.pgcnt - 1;
			topicForm.offset = topicForm.pgcnt * appDefDto.FP_COM_TOPICLIST_PGMAX;
		} catch (Exception e) {
			// 計算できない場合は初期値セット
			topicForm.pgcnt = 1;
			topicForm.offset = 0;
			// TODO: handle exception
		}
		// 一覧表示処理へ
		return "sellist?redirect=true";
	}

	// ★★★一覧表示処理★★★
	@Execute(validator=false)
	public String sellist(){
		// 件数取得
		topicForm.resultscnt = communityService.cntBbsList(communityDto.cid,entrytype);
		// 一覧データ取得
		results = communityService.selectBbsList(
			communityDto.cid,
			entrytype,
			topicForm.offset,
			appDefDto.FP_COM_TOPICLIST_PGMAX
		);
		// コメント編集(CmnUtility)
		CmnUtility.editcmnt(results,"comment","pic1","pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_CONTENTS_ROOT);
		return "list.jsp";
	}

	// ■■■■■■　　　　閲覧系処理　　　　■■■■■■
	// ■初期表示（閲覧表示・トピックタイトルリンククリック時)
	@Execute(validator=false,urlPattern="view/{cid}/{bbsid}")
	public String view(){
		// init処理
		init();
		// パラメタ初期化
		reset();
		// 閲覧表示処理へ
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}

	// ■全てを表示リンククリック時
	@Execute(validator=false,urlPattern="viewall/{cid}/{bbsid}")
	public String viewall(){
		// init処理
		init();
		// allflgを立てる
		topicForm.allflg = "1";
		// 閲覧表示処理へ
		return "selview/0/0";
	}

	// ■次を表示・前を表示リンククリック時
	@Execute(validator=false,urlPattern="movepg/{cid}/{bbsid}/{pgcnt}")
	public String movepg(){
		// init処理
		init();
		// パラメタ初期化
		reset();
		// 計算
		try {
			topicForm.offset = topicForm.pgcnt*appDefDto.FP_COM_BBSLIST_CMNTMAX;
		} catch (Exception e) {
			topicForm.offset = 0;
			// TODO: handle exception
		}
		// 閲覧表示処理へ
		return "selview/"+topicForm.offset+"/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}

	// ■自分のコメントを削除するリンク押下時
	@Execute(validator=false,urlPattern="delcmnt/{comno}")
	public String delcmnt(){
		
		// init処理
		init();
		// パラメタをリストにセットし削除確認画面へ
		topicForm.chkcmnt = new ArrayList<String>();
		topicForm.chkcmnt.add(topicForm.comno);

		// 選択したコメント情報の取得
		results = communityService.getChkCmnt(
			communityDto.cid,
			entrytype,
			topicForm.bbsid,
			topicForm.chkcmnt
		);
		
	    //表示権限チェック
	    if(check().equals("NG")){
	    	return "error.jsp";
	    }

		// 削除確認画面へ遷移
		return "confirm.jsp";
	}
	
	// 権限チェック
	public String check(){
		// 掲示板・親情報の取得
		result = communityService.getOyaView(
			communityDto.cid,
			entrytype,
			topicForm.bbsid
		);
		//管理人 or トピ設立：１、それ以外：０
		if(result.get(0).get("editflg").toString().equals("1")){
			return "NG";
		}
		if(results.size()!=1){
			return "NG";
		} else {
			if(!results.get(0).get("editflg").toString().equals("1")){
				return "NG";
			}
		}
		return "OK";
	}
	
	// ■チェックされたコメントを削除するリンク押下時
	@Execute(validator=false)
	public String delcmnts(){
		// init処理
		init();
		if(topicForm.chkcmnt == null) {
			// パラメタ初期化
			reset();
			// チェックボックスが選択されていない場合は閲覧画面へ
			return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
		} else {
			// チェックされたコメント情報の取得
			results = communityService.getChkCmnt(
				communityDto.cid,
				entrytype,
				topicForm.bbsid,
				topicForm.chkcmnt
			);
			// 削除確認画面へ遷移
			return "confirm.jsp";
		}
	}
	
	// ■確認画面で「キャンセル」ボタン押下時
	@Execute(validator=false)
	public String cancel(){
		// init処理
		init();
		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}

	// ■【登録】コメントを追加するボタン押下時
	@Execute(validate="chkPic",input="error")
	public String finish() throws IOException, Exception{
		// init処理
		init();
		// コメントの登録
		communityService.insCmnt(
			communityDto.cid,
			Integer.valueOf(topicForm.bbsid),
			topicForm.comment,
			topicForm.picpath1,
			topicForm.picpath2,
			topicForm.picpath3,
			topicForm.picnote1,
			topicForm.picnote2,
			topicForm.picnote3,
			entrytype
		);
		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}

	// ■【更新】確認画面で「削除する」ボタン押下時
	@Execute(validator=false)
	public String deletecmnts(){
		// init処理
		init();
		// コメント削除
		communityService.delcmnt(
			communityDto.cid,
			Integer.valueOf(topicForm.bbsid),
			topicForm.chkcmnt
		);
		// パラメタ初期化
		reset();
		// 閲覧画面へ戻る
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}

	// ★★★閲覧表示処理★★★
	@Execute(validator=false,urlPattern="selview/{offset}/{limit}")
	public String selview(){
		// 掲示板・親情報の取得
		result = communityService.getOyaView(
			communityDto.cid,
			entrytype,
			topicForm.bbsid
		);
		// 掲示板・コメント情報の取得
		results = communityService.getCmntView(
			communityDto.cid,
			entrytype,
			topicForm.bbsid,
			topicForm.offset,
			topicForm.limit
		);
		// 掲示板・コメント数の取得
		topicForm.resultscnt = communityService.cntCmnt(
			communityDto.cid,
			entrytype,
			topicForm.bbsid
		);
		// コメント編集(CmnUtility)
		// CmnUtility.editcmnt(results,"comment","pic1","pic2","pic3");
		return "view.jsp";
	}
	
	// ■■■■■■　　　　共通系処理　　　　■■■■■■
	// エラーチェック(画像)
	public ActionMessages chkPic(){
		ActionMessages errors = new ActionMessages();
		FormFile picpath1 = topicForm.picpath1;
		FormFile picpath2 = topicForm.picpath2;
		FormFile picpath3 = topicForm.picpath3;
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

	// エラー時の遷移先
	@Execute(validator=false)
	public String error(){
		// init処理
		init();
		// 閲覧表示処理へ
		return "selview/0/"+appDefDto.FP_COM_BBSLIST_CMNTMAX;
	}
	
	// 初期化処理
	private void reset(){
		topicForm.pgcnt = 0;
		topicForm.comment = "";
		topicForm.allflg = "";
		topicForm.chkcmnt = null;
		topicForm.picnote1 = null;
		topicForm.picnote2 = null;
		topicForm.picnote3 = null;
	}

	// init処理
	private void init(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = topicForm.cid;
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
	}
}