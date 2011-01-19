package frontier.action.pc;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.form.pc.EntrequestForm;
import frontier.service.EntrequestService;
import frontier.service.FriendinfoCmnService;
import frontier.service.MembersService;

public class EntrequestAction {
    Logger logger = Logger.getLogger(this.getClass().getName());
    
    @Resource
    protected EntrequestService entrequestService;
    
    @Resource
    protected FriendinfoCmnService friendinfoCmnService;
    
    @ActionForm
    @Resource
    protected EntrequestForm entrequestForm;
    
	@Resource
	protected MembersService membersService;
	
	//変数定義
	public AppDefDto appDefDto;
	public UserInfoDto userInfoDto;
	public BeanMap memberphoto;
    private Members members;
	List<BeanMap> FriendList;
	BeanMap Status;
	String FriendStatus = "0";
	public String vNickname;
	public int FriendCnt;
	
	@Execute(validator=false,urlPattern="{reqId}")
	public String index(){
		//ブラウザバック対応
		if(entrequestForm.reqId!=null){
			userInfoDto.visitMemberId=entrequestForm.reqId;
		}
		//メニューリンク以外からの遷移
		if(userInfoDto.memberId.equals(userInfoDto.visitMemberId)){
			logger.debug(userInfoDto.memberId);
			logger.debug(userInfoDto.visitMemberId);
			return "/pc/top/?redirect=true";
		}else{
			//変数初期化
			entrequestForm.comment=null;
			logger.debug(userInfoDto.visitMemberId);
			//ユーザの基本情報の取得
			members = membersService.getResultById(userInfoDto.visitMemberId);
			if (members != null){
				vNickname = members.nickname;
			}
			logger.debug("ニックネーム:"+vNickname);

			//メンバー画像情報取得
			memberphoto = entrequestService.selectMemberPhoto(userInfoDto.visitMemberId);
			//メンバー一覧取得
			try{
				FriendCnt = friendinfoCmnService.selFriend(userInfoDto.visitMemberId).size();
			} catch (Exception e) {
				FriendCnt = 0;
			}
			//同志チェック
			if(getFriendStatus().equals("1")){
				return "member.jsp";
			}
		}
		return "request.jsp";
	}
	
	@Execute(validator=true,input="error")
	public String send(){
		//ブラウザバック対応
		logger.debug("■■■■■■■■■■■■■■■■■■■■■"+entrequestForm.reqId);
		if(!userInfoDto.memberId.equals(entrequestForm.reqId)){
			//友達関係情報ステータスチェック
			Status = entrequestService.selFriendStatus(userInfoDto.memberId,entrequestForm.reqId);
			//データ登録
			if(Status!=null){
				//00:リクエスト中、10:友達、90:リクエスト拒否、91:友達関係解消、92:メンバー退会
				logger.debug("リクエストステータス："+Status.get("friendstatus"));
				if(!Status.get("friendstatus").equals("90")&&!Status.get("friendstatus").equals("91")){
					return "/pc/top/?redirect=true";
				}
			}
			//友達情報登録
			entrequestService.insFriendinfo(userInfoDto.memberId,entrequestForm.comment);
			//友達関係情報登録
			entrequestService.insFriendRelation(userInfoDto.memberId,entrequestForm.reqId);
		}
		
		return "/pc/top/?redirect=true";		
	}
	
	//登録時エラー
	@Execute(validator=false)
	public String error(){
		//ユーザの基本情報の取得
		members = membersService.getResultById(entrequestForm.reqId);
		//ブラウザバック対応
		userInfoDto.visitMemberId=entrequestForm.reqId;
		if (members != null){
			vNickname = members.nickname;
		}

		//メンバー画像情報取得
		memberphoto = entrequestService.selectMemberPhoto(entrequestForm.reqId);
		return "request.jsp";
	}
	
	private String getFriendStatus(){
		//メンバー一覧取得
		FriendList = friendinfoCmnService.selFriend(userInfoDto.memberId);
		for(BeanMap beanMap:FriendList){
			//既にメンバー一覧に追加されている場合

			if(beanMap.get("mid").equals(userInfoDto.visitMemberId)){
				FriendStatus = "1";
			}
		}
		return FriendStatus;
	}
	

}
