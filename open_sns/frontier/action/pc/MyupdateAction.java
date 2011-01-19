package frontier.action.pc;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.service.FriendListService;
import frontier.service.MyupdateService;

public class MyupdateAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected FriendListService friendListService;
	@Resource
	protected MyupdateService myupdateService;
	
	
	public List<BeanMap> PhotoList;
	public List<BeanMap> DiaryList;
	
	@Execute(validator=false)
	public String index(){
		//最新フォトデータ取得
		PhotoList = myupdateService.selMyUpdatePhotoList(userInfoDto.memberId);
		//タイトル名置換処理
		for(BeanMap b:PhotoList){
			b.put("stitle",CmnUtility.substrByte(b.get("title").toString(),80));
		}
		//最新日記データ取得
		DiaryList = myupdateService.selMyUpdateDiaryList(userInfoDto.memberId);
		//タイトル名置換処理
		for(BeanMap b:DiaryList){
			b.put("stitle",CmnUtility.substrByte(b.get("title").toString(),80));
		}
		return "list.jsp";
	}
}
