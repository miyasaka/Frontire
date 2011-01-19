package frontier.action.pc.ajax;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.RssmanageForm;
import frontier.service.CommonService;
import frontier.service.RssmanageService;

public class RssmanageAction {
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected RssmanageService rssmanageService;
	@Resource
	protected CommonService commonService;
	@ActionForm
	@Resource
	public RssmanageForm rssmanageForm;
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
		String gname;
		
		//ソート項目初期化
		rssmanageForm.pgcnt = 0;
		rssmanageForm.offset = 0;
		
		// 並び順をセット
		results = rssmanageService.selRssMemberSearch(rssmanageForm.rssid,rssmanageForm.searchFlg,rssmanageForm.searchword,rssmanageForm.rd01,rssmanageForm.searchgroup,appDefDto.FP_RSS_MEMBER_LIST_MAX,rssmanageForm.offset,rssmanageForm.sortname);
		resultsCnt = rssmanageService.selCntRssMemberSearch(rssmanageForm.rssid,rssmanageForm.searchFlg,rssmanageForm.searchword,rssmanageForm.rd01,rssmanageForm.searchgroup);

		//メンバーが所属しているグループを検索
		for(int i=0;i<results.size();i++){
			gname = "";
			gname = searchRssMemberIns((String)results.get(i).get("mid"));
			
			//検索結果に追加
			results.get(i).put("gname", gname);			
		}
		
		
		loadFileList(results);
		CmnUtility.ResponseWrite(ViewData, "text/html", "windows-31J", "Shift_JIS");
		return null;
	}
	
    //RSSメンバー追加画面：RSSメンバーのグループを検索
    private String searchRssMemberIns(String mid){
    	//メンバーが所属しているグループを検索
        List<BeanMap> mlist = commonService.getMidList("4", mid);

        //グループ名を連結する
        String gname = makeCSV(mlist,"gname");
            	
        //グループ未所属の場合
        if(mlist.size()==0){
        	gname = "所属グループなし";
        }
        
        return gname;
    }
	
    //メンバをカンマ区切りにする
    private String makeCSV(List<BeanMap> lbm,String name){
    	String midStr = "";
    	
		//ニックネームを連結する
		for(int j=0;j<lbm.size();j++){
			if((j+1)==lbm.size()){
				//最後の場合はカンマを付けない
    			midStr = midStr + (String)lbm.get(j).get(name);
			}else{
				//カンマ付ける
    			midStr = midStr + (String)lbm.get(j).get(name) + "、";    				
			}
		}
		
		return midStr;
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
				rssmanageService.setFileDecoration(beanMap);
		// sequence-start start
				if(loopCnt == 0){
					ViewData += "<div class=\"align03\">";
					//ソートした場合は1件目からに戻るので前のxx件は出力する必要がない
					if(resultsCnt > results.size()){
						ViewData += "<a href=\"/frontier/pc/rssmanage/nxtpg/\">次の"+appDefDto.FP_RSS_MEMBER_LIST_MAX+"件&gt;&gt;</a>";
					}
					ViewData += "</div>";
					ViewData += "<table cellspacing=\"0\" border=\"1\" bordercolor=\"#000\" width=\"100%;\" style=\"border-collapse:collapse;\">";					
					ViewData += "<tr>";					
					ViewData += "<th width=\"5%\">&nbsp;</th>";
					ViewData += "<th width=\"40%\">名前";
					ViewData += "<label onclick=\"j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/1?'+rand(10));\">";
					if(rssmanageForm.sortname.equals("1")){
						ViewData += "<font color=\"#ff0000\">▲</font></label>";
					}else{
						ViewData += "▲</label>";						
					}
					ViewData += "<label onclick=\"j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/2?'+rand(10));\">";
					if(rssmanageForm.sortname.equals("2")){
						ViewData += "<font color=\"#ff0000\">▼</font></label>";
					}else{
						ViewData += "▼</label>";		
					}
					ViewData += "</th>";
					ViewData += "<th width=\"40%\">グループ</th>";
					
					ViewData += "<th width=\"15%\">登録日";
					ViewData += "<label onclick=\"j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/3?'+rand(10));\">";
					if(rssmanageForm.sortname.equals("3")){
						ViewData += "<font color=\"#ff0000\">▲</font></label>";
					}else{
						ViewData += "▲</label>";						
					}
					ViewData += "<label onclick=\"j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/4?'+rand(10));\">";
					if(rssmanageForm.sortname.equals("4")){
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
				ViewData += "<td>";
				ViewData += "<input type=\"checkbox\" value=\"" +beanMap.get("mid") +"\" name=\"checkJoin\"";
				//参加している場合はチェック済みにする。
				if(beanMap.get("joinflg").equals("1")){
					ViewData += " checked";
				}
				ViewData += "></td>";
				ViewData += "<td class=\"align04\">";
				ViewData += beanMap.get("viewNickname")+"（"+beanMap.get("viewName")+"）"+beanMap.get("viewStatusname");
				ViewData += "</td>";
				ViewData += "<td class=\"align04\">";
				ViewData += beanMap.get("viewGName");
				ViewData += "</td>";
				ViewData += "<td class=\"align02\">";
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
