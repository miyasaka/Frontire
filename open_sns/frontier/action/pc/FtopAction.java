package frontier.action.pc;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.FrontierUserManagement;
import frontier.entity.Members;
import frontier.form.pc.FtopForm;
import frontier.service.CommonService;
import frontier.service.FtopService;
import frontier.service.GroupService;
import frontier.service.MembersService;
import frontier.service.MlistService;
import frontier.service.TopService;

public class FtopAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@ActionForm
	@Resource
	protected FtopForm ftopForm;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected FtopService ftopService;
	@Resource
	protected CommonService commonService;
	@Resource
	protected MembersService membersService;
	@Resource
	protected GroupService groupService;
	@Resource
	protected TopService topService;
	@Resource
	protected MlistService mlistService;

	// 変数定義
	public Members MemberInfo;
	public List<BeanMap> GroupListTmp;
	public List<BeanMap> GroupList;
	public List<BeanMap> FNetList;
	public List<BeanMap> FDiaryList;
	public List <Object> GMList;
	private String rtnURL;
	public FrontierUserManagement frontierUserManagement;

	// 初期表示
	@Execute(validator=false)
	public String index(){
		// パラメタ初期化
		reset();
		init();
		return rtnURL;
	}

	// グループ選択時
	@Execute(validator=false)
	public String selgroup(){
		// グループ選択処理
		if(ftopForm.groupid.equals("0")){
			// 全件検索時、パラメタ初期化
			reset();
		} else {
			// グループ選択時、フラグセット
			// 0:全件日記検索、1:特定グループの日記検索
			ftopForm.selgrouptype = "1";
		}
		init();
		return rtnURL;
	}

	// init処理
	private void init(){
		// マイページ内の遷移なので訪問メンバーIDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		// ユーザのタイプ別処理
		if(userInfoDto.membertype.equals("0")){
			// 本Frontierユーザの処理
			setUserParams();
		} else if(userInfoDto.membertype.equals("1")){
			// 他Frontierユーザの処理
			setFUserParams();
		}
	}

	// reset処理
	private void reset(){
		ftopForm.groupid = "0";
		// 0:全件日記検索、1:特定グループの日記検索
		ftopForm.selgrouptype = "0";
	}

	// 通常ユーザのパラメタセット処理(通常ユーザ画面へ飛ばす)
	private void setUserParams(){
		rtnURL = "/pc/top/";
	}

	// FrontierNetユーザのパラメタセット処理
	private void setFUserParams(){
		rtnURL = "home.jsp";
		// メンバー情報取得
		MemberInfo = membersService.getResultById(userInfoDto.memberId);
		// Frontier Netデータ取得
		FNetList = topService.selFNet();
		// メンバー日記データ取得
		FDiaryList = ftopService.getDiaryList(
				ftopForm.selgrouptype,
				appDefDto.FP_CMN_HOST_NAME,
				ftopForm.groupid
			);
		//装飾タグの削除
		CmnUtility.editcmnt(
				FDiaryList,
				"comment",
				"pic1",
				"pic2",
				"pic3",
				"picnote1",
				"picnote2",
				"picnote3",
				appDefDto.FP_CMN_EMOJI_XML_PATH,
				appDefDto.FP_CMN_LIST_CMNTMAX,
				appDefDto.FP_CMN_EMOJI_IMG_PATH,
				appDefDto.FP_CMN_CONTENTS_ROOT
			);
		//=======================================
		// ■以下グループ系データの取得、編集
		//=======================================
		// グループ一覧の取得(一時変数に代入)
		GroupListTmp = groupService.selGroup(null);
		// 初期化
		GroupList = new ArrayList<BeanMap>();
		// グループ一覧の詰め直し(メンバーが0のグループは除外)
		if(GroupListTmp.size() > 0){
			// グループ数分ループ
			for(BeanMap f:GroupListTmp){
				// メンバーが居ればリストに詰めなおし
				if((Integer) f.get("joinnumber") > 0){
					GroupList.add(f);
				}
			}
		} else {
			// グループが無ければそのまま代入
			GroupList = GroupListTmp;
		}

		// グループリストの編集(グループ毎にメンバーを配列で追加)
		if(GroupList.size() > 0){
			// リストの初期化
			GMList = new ArrayList<Object>();
			List<Object> addObject;
			// グループ数分ループ
			for(BeanMap f:GroupList){
				List<BeanMap> Mlist;
				addObject = new ArrayList<Object>();
				// グループのメンバーの取得
				Mlist = mlistService.selGroupMemList(
						f.get("gid").toString(),
						appDefDto.FP_CMN_HOST_NAME,
						0,
						appDefDto.FP_MEM_TOPLIST_PGMAX
					);
				// addObject配列にグループ情報と、グループに所属してるメンバー情報を追加
				addObject.add(0,f);
				addObject.add(1,Mlist);
				// リストに追加
				GMList.add(addObject);
			}
		}
	}
}