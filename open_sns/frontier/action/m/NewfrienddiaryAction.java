package frontier.action.m;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.service.CommonService;
import frontier.service.FriendListService;
import frontier.service.TopService;

public class NewfrienddiaryAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected TopService topService;
	@Resource
	protected FriendListService friendListService;
	@Resource
	protected CommonService commonService;
	
	public List<BeanMap> results;
	public List<BeanMap> GroupList;
	
	//同志最新日記画面初期表示
	@Execute(validator=false)
	public String index(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		
		// 新着日記データ取得
		results = topService.selDiaryNewList(userInfoDto.memberId,appDefDto.FP_MY_M_MEMDIARYLIST_PGMAX,"01",groupids());
	
		return "/m/newfrienddiary/list.jsp";
	}
	
	private List<Object> groupids(){
		List<BeanMap> GroupList = new ArrayList<BeanMap>();
		//グループ一覧データ取得
		GroupList = commonService.getMidList("1",userInfoDto.memberId);
		//同志リスト格納用変数
		List<Object> glist = new ArrayList<Object>();
		//同志0対策
		glist.add(userInfoDto.memberId);
		for(BeanMap f:GroupList){
			glist.add(f.get("mid"));
		}
		return glist;
	}
	

}
