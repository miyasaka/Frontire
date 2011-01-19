package frontier.action.pc.ajax;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.GroupForm;
import frontier.service.GroupService;

public class GroupAction {
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected GroupForm groupForm;
	@Resource
	protected GroupService groupService;
	// 変数
	public List<BeanMap> results;	//	検索結果
	public long resultsCnt;		//	検索結果件数	
	public String ViewData;			//	返却タグ格納用

	/**
	 * .load
	 * @return　String
	 */
	@Execute(validator=false, urlPattern="loadData/{sortname}",reset="resetSearch")
	public String loadData(){
		//ソート項目初期化
		groupForm.pgcnt = 0;
		groupForm.offset = 0;
		
		// 並び順をセット
		results = groupService.selGroupMember(groupForm.gid, groupForm.searchmem, groupForm.r1, groupForm.joincheck, appDefDto.FP_GRP_LIST_MAX, groupForm.offset,groupForm.sortname);
		resultsCnt = groupService.cntGroupMember(groupForm.gid, groupForm.searchmem, groupForm.r1, groupForm.joincheck);

		loadFileList(results);
		CmnUtility.ResponseWrite(ViewData, "text/html", "windows-31J", "Shift_JIS");
		return null;
	}
	
	/**
	 * ファイルリストセット
	 * @param results
	 */
	private void loadFileList(List<BeanMap> results){
		ViewData = "";
		if(results.size() > 0){
			Integer loopCnt = 0;
			for (BeanMap beanMap : results){
				// 加工処理
				groupService.setFileDecoration(beanMap);
		// sequence-start start
				if(loopCnt == 0){
					ViewData += "<div class=\"searchTitle\">";
					ViewData += "<div class=\"search-left\">";
					ViewData += "<label STYLE=\"font-size:10px;padding:0 2px 0 0;\">全て</label><a HREF=\"#\" onclick=\"JavaScript:checkAll('0', 'checkJoin', true);\">参加</a>&nbsp;|&nbsp;<a HREF=\"#\" onclick=\"JavaScript:checkAll('0', 'checkJoin', false);\">不参加</a>";
					ViewData += "</div>";
					ViewData += "<div class=\"search-right\">";
					//ソートした場合は1件目からに戻るので前のxx件は出力する必要がない
					if(resultsCnt > results.size()){
						ViewData += "<a href=\"/frontier/pc/group/nxtpg/\">次の"+appDefDto.FP_GRP_LIST_MAX+"件&gt;&gt;</a>";
					}
					ViewData += "</div>";
					ViewData += "</div>";
					ViewData += "<table>";
					ViewData += "<tr>";
					ViewData += "<th class=\"th1\">参加</th>";
					ViewData += "<th class=\"th2\">管理</th>";
					ViewData += "<th class=\"th3\">名前";
					ViewData += "<label onclick=\"j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/1?'+rand(10));\">";
					if(groupForm.sortname.equals("1")){
						ViewData += "<font color=\"#ff0000\">▲</font></label>";
					}else{
						ViewData += "▲</label>";						
					}
					ViewData += "<label onclick=\"j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/2?'+rand(10));\">";
					if(groupForm.sortname.equals("2")){
						ViewData += "<font color=\"#ff0000\">▼</font></label>";
					}else{
						ViewData += "▼</label>";		
					}
					ViewData += "</th>";
					ViewData += "<th class=\"th4\">PCアドレス";
					ViewData += "<label onclick=\"j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/3?'+rand(10));\">";
					if(groupForm.sortname.equals("3")){
						ViewData += "<font color=\"#ff0000\">▲</font></label>";
					}else{
						ViewData += "▲</label>";						
					}
					ViewData += "<label onclick=\"j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/4?'+rand(10));\">";
					if(groupForm.sortname.equals("4")){
						ViewData += "<font color=\"#ff0000\">▼</font></label>";
					}else{
						ViewData += "▼</label>";		
					}					
					ViewData += "</th>";
					ViewData += "<th class=\"th5\">登録日";
					ViewData += "<label onclick=\"j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/5?'+rand(10));\">";
					if(groupForm.sortname.equals("5")){
						ViewData += "<font color=\"#ff0000\">▲</font></label>";
					}else{
						ViewData += "▲</label>";						
					}
					ViewData += "<label onclick=\"j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/6?'+rand(10));\">";
					if(groupForm.sortname.equals("6")){
						ViewData += "<font color=\"#ff0000\">▼</font></label>";
					}else{
						ViewData += "▼</label>";		
					}
					ViewData += "</th>";
					ViewData += "</tr>";
				}
		// sequence-start end
			// sequence start
				ViewData += "<tr>";
				ViewData += "<td class=\"td1\">";
				ViewData += "<input type=\"checkbox\" value=\"" +beanMap.get("mid") +"\" name=\"checkJoin\"";
				//参加している場合はチェック済みにする。
				if(beanMap.get("joinflg").equals("1")){
					ViewData += " checked";
				}
				ViewData += "></td>";
				ViewData += "<td class=\"td2\">";
				ViewData += "<input type=\"checkbox\" value=\"" +beanMap.get("mid") +"\" name=\"checkAuth\"";
				//管理者の場合はチェック済みにする。
				if(beanMap.get("manageflg").equals("1")){
					ViewData += " checked";
				}
				ViewData += "></td>";
				ViewData += "<td class=\"td3\">";
				ViewData += beanMap.get("viewName");
				ViewData += "</td>";
				ViewData += "<td class=\"td4\">";
				ViewData += beanMap.get("viewEmail");
				ViewData += "</td>";
				ViewData += "<td class=\"td5\">";
				ViewData += beanMap.get("entdate");
				ViewData += "</td>";
				ViewData += "</tr>";
			// sequence end
		// sequence-end start
				if((loopCnt - 1) == results.size()){
					ViewData += "</table>";
				}
		// sequence-end end
				loopCnt ++;
			}
		}
	}
	
}
