package frontier.action.pc.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.FileForm;
import frontier.service.FileService;

public class FileAction {
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected FileForm fileForm;
	@Resource
	protected FileService fileService;
	// 変数
	public List<BeanMap> results;	//	検索結果
	public Integer resultsCnt;		//	検索結果件数
	public String ViewData;			//	返却タグ格納用
	
	/**
	 * .load
	 * @return　String
	 */
	@Execute(validator=false, urlPattern="loadData/{sortname}")
	public String loadData(){
		// 並び順をセット
		fileService.setSortcd(fileForm);
		results = fileService.selFileList(fileForm);
		//resultsCnt = fileService.selCntFileList(fileForm);
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
			Map<String,Object> classMap = new HashMap<String,Object>();
			classMap = fileService.setClass(fileForm);
			for (BeanMap beanMap : results){
				// 加工処理
				fileService.setFileDecoration(beanMap);
		// sequence-start start
				if(loopCnt == 0){
					ViewData += "<div class=\"fileMainBoxBody\" id=\"fileMainBoxBody\">";
					ViewData += "<table class=\"fileList\">";
					ViewData += "<tr class=\"finding\">";
					ViewData += "<th class=\"" + classMap.get("class1") + "\" onclick=\"j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/1?'+rand(10));\" title=\"ファイル名\">ファイル名" + classMap.get("arrow1") + "</th>";
					ViewData += "<th class=\"" + classMap.get("class2") + "\" onclick=\"j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/2?'+rand(10));\" title=\"カテゴリ\">カテゴリ" + classMap.get("arrow2") + "</th>";
					ViewData += "<th class=\"" + classMap.get("class3") + "\" onclick=\"j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/3?'+rand(10));\" title=\"種類\">種類" + classMap.get("arrow3") + "</th>";
					ViewData += "<th class=\"" + classMap.get("class4") + "\" onclick=\"j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/4?'+rand(10));\" title=\"更新日\">更新日" + classMap.get("arrow4") + "</th>";
					ViewData += "<th class=\"" + classMap.get("class5") + "\" onclick=\"j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/5?'+rand(10));\" title=\"DL\">DL" + classMap.get("arrow5") + "</th>";
					ViewData += "<th class=\"" + classMap.get("class6") + "\" onclick=\"j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/6?'+rand(10));\" title=\"登録者\">登録者" + classMap.get("arrow6") + "</th>";
					ViewData += "<th class=\"" + classMap.get("class7") + "\" onclick=\"j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/7?'+rand(10));\" title=\"サイズ\">サイズ" + classMap.get("arrow7") + "</th>";
					ViewData += "<th class=\"th8\">&nbsp;</th>";
					ViewData += "</tr>";
				}
		// sequence-start end
			// sequence start
				if(loopCnt % 2 == 0){
					ViewData += "<tr class=\"odd\">";
				}else{
					ViewData += "<tr class=\"even\">";
				}
				ViewData += "<td class=\"corner\">";
				ViewData += "<div style=\"float: left;\"><img src=\"" + appDefDto.FP_CMN_FILE_IMAGE_PATH + beanMap.get("pic") + "\" title=\"" + beanMap.get("ViewJextension") + "\" /></div>";
				ViewData += "<div style=\"float: left;\">&nbsp;<a href=\"/frontier/pc/file/view/" + beanMap.get("fileid") + "\" title=\"" + beanMap.get("ViewTitleorg") + "\">" + beanMap.get("ViewTitle") + "</a></div>";
				ViewData += "</td>";
				ViewData += "<td class=\"center\">" + beanMap.get("ViewCategoryname") + "</td>";
				ViewData += "<td class=\"center\">" + beanMap.get("ViewJextension") + "</td>";
				ViewData += "<td class=\"right\">" + beanMap.get("ViewUpddate") + "</td>";
				ViewData += "<td class=\"right\">" + beanMap.get("download") + "回</td>";
				ViewData += "<td class=\"center\">" + beanMap.get("ViewEntname") + "</td>";
				ViewData += "<td class=\"right\">" + beanMap.get("filesizeCalc") + "</td>";
				ViewData += "<td class=\"corner\">";
				if(fileService.isAuth(beanMap)){
					ViewData += "<a href=\"/frontier/pc/file/download/" + beanMap.get("fileid") + "\" title=\"DLする\"><img src=\"" + appDefDto.FP_CMN_FILE_IMAGE_PATH + "icon_download.png\" alt=\"DLする\" /></a>&nbsp;";
				}
				ViewData += "<a href=\"/frontier/pc/entfile/edit/" + beanMap.get("fileid") + "\" title=\"更新する\"><img src=\"" + appDefDto.FP_CMN_FILE_IMAGE_PATH + "icon_refresh.png\" alt=\"更新する\" /></a>";
				ViewData += "</td>";
				ViewData += "</tr>";
			// sequence end
		// sequence-end start
				if((loopCnt - 1) == results.size()){
					ViewData += "</table>";
					ViewData += "</div>";
				}
		// sequence-end end
				loopCnt ++;
			}
		}
	}
}