package frontier.action.pc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.annotation.Maxlength;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.service.ClistService;
import frontier.service.CommonService;
import frontier.service.MembersService;

public class SetupAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected MembersService membersService;
	@Resource
	protected ClistService clistService;
	@Resource
	protected CommonService commonService;
	@Resource
	protected HttpSession session;

    public String UserID;
    
    //コミュニティ件数
    public Integer communityCnt = 0;
    //管理コミュニティ一覧
    private List<BeanMap> admclist;
    
	@Maxlength(maxlength=10000,arg0=@Arg(key="退会理由",resource=false))
    public String reason;
    
	// ■初期表示
	@Execute(validator=false)
	public String index(){
		// init処理
		init();
		return "index.jsp";
	}
	// 退会確認
	@Execute(validator=false)
	public String confirm(){
		return "confirm.jsp";
	}
	// 退会完了
	@Execute(validator=true,input="confirm.jsp")
	public String leave(){
		//管理コミュニティ削除
		admclist = clistService.seladmClist(userInfoDto.memberId);
		if(admclist!=null){
			clistService.deladmClist(admclist);
		}

		try{
			//メンバー情報更新
			membersService.updMemberStatus(userInfoDto.memberId,reason);
			//フォロー数更新
			membersService.updListFCnt("0",fids("2"),userInfoDto.memberId);
			//フォロワー数更新
			membersService.updListFCnt("1",fids("3"),userInfoDto.memberId);
			//ｸﾞﾙｰﾌﾟ参加人数更新
			membersService.updListGCnt(commonService.getMidList("4",userInfoDto.memberId),userInfoDto.memberId);
			
		}catch(Exception e){
			
		}
		//セッション破棄
		session.invalidate();
		
		return "leave.jsp";
	}
	// やめる
	@Execute(validator=false)
	public String cansel(){
		return "index";
	}
	
	
	// init処理
	private void init(){
		//メニューの出しわけ
		userInfoDto.visitMemberId = UserID;
		// 参加コミュニティ件数取得
		communityCnt = clistService.cntClist(userInfoDto.memberId,"0");
		
	}
	
	//フォロー/フォロワーID取得
	private List<Object> fids(String type){
		List<BeanMap> FList = new ArrayList<BeanMap>();
		//グループ一覧データ取得
		FList = commonService.getMidList(type,userInfoDto.memberId);
		//同志リスト格納用変数
		List<Object> flist = new ArrayList<Object>();
		for(BeanMap f:FList){
			flist.add(f.get("mid"));
		}
		return flist;
	}


}
