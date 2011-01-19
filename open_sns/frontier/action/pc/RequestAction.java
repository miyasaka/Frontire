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
import frontier.form.pc.RequestForm;
import frontier.service.EntrequestService;
import frontier.service.FriendListService;
import frontier.service.RequestService;

public class RequestAction {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    protected RequestService requestService;

    @Resource
    protected EntrequestService entrequestService;

    @Resource
    protected FriendListService friendListService;

	@ActionForm
	@Resource
	protected RequestForm requestForm;


	//変数定義
	public UserInfoDto userInfoDto;
	public AppDefDto appDefDto;
	public BeanMap memberphoto;
	BeanMap Status;

	public List<BeanMap> results;

	@Execute(validator=false)
	public String index(){

		// 初期値を設定
		requestForm.offset = 0;
		requestForm.pgcnt = 0;
		requestForm.reqFlg = "0";
		// init処理
		init();

		return "mem.jsp";
	}

	@Execute(validator=false,urlPattern="com")
	public String requestCom(){

		// 初期値を設定
		requestForm.offset = 0;
		requestForm.pgcnt = 0;
		requestForm.reqFlg = "1";
		// init処理
		init();

		return "com.jsp";
	}


	@Execute(validator=false)
	public String approval(){
		//データ登録（承認）
		requestService.updComStatus(userInfoDto.memberId,requestForm.reqId,requestForm.reqCid,"1");

		return "/pc/top/?redirect=true";
	}

	@Execute(validator=false)
	public String refusal(){
		//データ登録（拒否）
		requestService.updComStatus(userInfoDto.memberId,requestForm.reqId,requestForm.reqCid,"2");

		return "/pc/top/?redirect=true";
	}


	// ■ページング処理
	@Execute(validator=false)
	public String movelist(){

		// init処理
		init();
		// 一覧表示処理へ
		return "mem.jsp";
	}

	// ■ページング処理（コミュニティリクエスト）
	@Execute(validator=false)
	public String moveComlist(){

		// init処理
		init();
		// 一覧表示処理へ
		return "com.jsp";
	}


	// init処理
	private void init(){
		//メニュー出し分け用変数設定
		userInfoDto.visitMemberId=userInfoDto.memberId;

		try {
			// offset設定
			requestForm.offset = requestForm.pgcnt*appDefDto.FP_CMN_LIST_REQMAX;
		} catch (Exception e) {
			// エラーになった場合は初期値を設定
			requestForm.offset = 0;
			requestForm.pgcnt = 0;
			requestForm.reqFlg = "";
			// TODO: handle exception
		}

		if(requestForm.reqFlg.equals("0")){
			// メンバー参加リクエスト
			// メンバー件数取得
			requestForm.resultscnt = friendListService.getFriendRequestCount(userInfoDto.memberId);
			//追加リクエスト一覧データ取得
			results = requestService.selFriendAddRequestList(userInfoDto.memberId,requestForm.offset,appDefDto.FP_CMN_LIST_REQMAX);
			//本文の装飾
			resetResults(results,"reqcomment");
			resetResults(results,"aboutme");
		} else if(requestForm.reqFlg.equals("1")){
			// コミュニティ参加リクエスト
			// メンバー件数取得
			requestForm.resultscnt = friendListService.getCommunityRequestCount(userInfoDto.memberId);
			//追加リクエスト一覧データ取得
			results = requestService.selComAddRequestList(userInfoDto.memberId,requestForm.offset,appDefDto.FP_CMN_LIST_REQMAX);
			//本文の装飾
			resetResults(results,"requiredmsg");
			resetResults(results,"title");
		}

	}

	//本文装飾
	private void resetResults(List<BeanMap> lbm,String property){

		for (int i=0;i<lbm.size();i++){

			//リクエストメッセージを取得
			String reqComment = (String)lbm.get(i).get(property);

			if(reqComment!=null){
				//サニタイジング
				reqComment = CmnUtility.htmlSanitizing(reqComment);

			    //絵文字装飾
			  	String emojireqComment = CmnUtility.replaceEmoji(reqComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);

			  	//BeanMapへ格納
			  	lbm.get(i).put(property, emojireqComment);
			}

		  	//lbm.get(i).put("viewComment", sb.toString());
		}
	}

}
