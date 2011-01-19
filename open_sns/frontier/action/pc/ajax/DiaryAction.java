package frontier.action.pc.ajax;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.DiaryForm;
import frontier.service.DiaryCmnService;
import frontier.service.DiaryService;
import frontier.service.FriendinfoCmnService;
import frontier.service.MembersService;
import frontier.service.PhotoService;

public class DiaryAction {
    Logger logger = Logger.getLogger(this.getClass().getName());
	
    @ActionForm
    @Resource
    protected DiaryForm diaryForm;
    
    @Resource
    public UserInfoDto userInfoDto;
    @Resource
    public AppDefDto appDefDto;
    @Resource
    protected DiaryService diaryService;
    @Resource
    protected DiaryCmnService diaryCmnService;
    @Resource
    protected MembersService membersService;
    @Resource
    protected FriendinfoCmnService friendinfoCmnService;
    @Resource
    protected PhotoService photoService;
	// 変数
	public List<BeanMap> results;	//	検索結果
	public Integer resultsCnt;		//	検索結果件数
	public String ViewData;			//	返却タグ格納用
	
	/**
	 * .load
	 * @return　String
	 */
	@Execute(validator=false, urlPattern="loadDataOK/{comno}")
	public String loadDataOK(){
		logger.debug("******** 承認ボタン押した！ **********");
		logger.debug("******** ボタン押した！ **********"+diaryForm.comno);
		logger.debug("******** ボタン押した！ **********"+diaryForm.okng);
		logger.debug("********* 承認だよー ***********"+diaryForm.comno);
		logger.debug("********* 承認だよー ***********"+diaryForm.okng);
		diaryService.updDiaryCommentOutside(diaryForm.mid,diaryForm,userInfoDto.memberId,"0","4");
		ViewData = makeView(diaryForm.comno);
		CmnUtility.ResponseWrite("", "text/html", "windows-31J", "Shift_JIS");
		return null;
	}
	
	/**
	 * .load
	 * @return　String
	 */
	@Execute(validator=false, urlPattern="loadDataNG/{comno}")
	public String loadDataNG(){
		logger.debug("******** 取消ボタン押した！ **********");
		logger.debug("******** ボタン押した！ **********"+diaryForm.comno);
		logger.debug("******** ボタン押した！ **********"+diaryForm.okng);
		logger.debug("********* 取消だよー ***********"+diaryForm.comno);
		logger.debug("********* 取消だよー ***********"+diaryForm.okng);
		diaryService.updDiaryCommentOutside(diaryForm.mid,diaryForm,userInfoDto.memberId,"9","0");
		ViewData = makeView2(diaryForm.comno);
		CmnUtility.ResponseWrite("", "text/html", "windows-31J", "Shift_JIS");
		return null;
	}
	
	//承認ボタン押した
	private String makeView(Integer comno){
		ViewData +="<span class=\"okng\" onclick=\"updData()\"><img src=\"/images/ng.png\" alt=\"取消\" title=\"取消\" /></span>";
		return ViewData;
	}
	
	//取消ボタン押した
	private String makeView2(Integer comno){
		ViewData +="<span class=\"okng\" onclick=\"updData()\"><img src=\"/images/ok.png\" alt=\"承認\" title=\"承認\" /></span>";
		return ViewData;
	}
}