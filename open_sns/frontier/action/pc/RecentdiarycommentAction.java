package frontier.action.pc;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.service.RecentdiarycommentService;

public class RecentdiarycommentAction {
	public UserInfoDto userInfoDto;
	public AppDefDto appDefDto;
	
	@Resource
	protected RecentdiarycommentService recentdiarycommentService;

	public List<BeanMap> results;
	
	
	@Execute(validator=false)
	public String index(){
		// マイページ内の遷移なので訪問メンバーIDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		//コミュニティ最新書き込みデータ取得
		results = recentdiarycommentService.selRecebtDiaryCommentList(userInfoDto.memberId);
		
		return "list.jsp";
	}
}
