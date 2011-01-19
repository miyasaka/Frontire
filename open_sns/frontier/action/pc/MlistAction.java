package frontier.action.pc;

import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.form.pc.MlistForm;
import frontier.service.CommonService;
import frontier.service.MembersService;
import frontier.service.MlistService;

public class MlistAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	public List<BeanMap> results;
	public List<BeanMap> resultsList;
	public List<BeanMap> groupList;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected MlistForm mlistForm;
	@Resource
	protected MlistService mlistService;
	@Resource
	protected CommonService commonService;
	@Resource
	protected MembersService membersService;

	public String mid;
	public String myName;
	public String gName;
	public String UserID;
	public String myflg;

	// ■初期表示
	@Execute(validator=false,urlPattern="{vmid}")
	public String index(){
		// 初期値を設定
		mlistForm.offset = 0;
		mlistForm.pgcnt = 0;
		mlistForm.type = "2";
		// init処理
		init();
		// 一覧表示処理へ
		return "list.jsp";
	}

	//フォロー
	@Execute(validator=false,urlPattern="follow/{vmid}")
	public String follow(){
		// 初期値を設定
		mlistForm.offset = 0;
		mlistForm.pgcnt = 0;
		mlistForm.type="2";//フォロー
		// init処理
		init();
		return "list.jsp";
	}

	//フォロワー
	@Execute(validator=false,urlPattern="follower/{vmid}")
	public String follower(){
		// 初期値を設定
		mlistForm.offset = 0;
		mlistForm.pgcnt = 0;
		mlistForm.type="3";//フォロワー
		// init処理
		init();
		return "list.jsp";
	}

	//グループ
	@Execute(validator=false)
	public String group(){
		// 初期値を設定
		logger.debug(mlistForm.domain);
		logger.debug(mlistForm.gid);

		mlistForm.offset = 0;
		mlistForm.pgcnt = 0;
		mlistForm.type="1";//グループ
		// init処理
		init();
		return "list.jsp";
	}

	// ■ページング処理
	@Execute(validator=false,urlPattern="movelist/{vmid}/{pgcnt}")
	public String movelist(){
		// init処理
		init();
		// 一覧表示処理へ
		return "list.jsp";
	}

	// init処理
	private void init(){
		//メニューの出しわけ
		if(mlistForm.vmid!=null) userInfoDto.visitMemberId = mlistForm.vmid;
		//ユーザー情報取得
		Members m = membersService.getResultById(userInfoDto.visitMemberId);
		//文言出しわけ
		if(mlistForm.type.equals("1")){//フォロー
			myName = "所属メンバー";
		}else if(mlistForm.type.equals("2")){//フォロワー
			myName = "フォローしている";
		}else if(mlistForm.type.equals("3")){//フォロワー
			myName = "フォローされている";
		}

		//フォロー解除リンク表示制御
		if(userInfoDto.memberId.equals(mlistForm.vmid)){//マイ
			myflg = "1";

		}else{//グループ
			myflg = "";
		}

		logger.debug("status"+userInfoDto.memberId.equals(mlistForm.vmid));

		try {
			// offset設定
			mlistForm.offset = mlistForm.pgcnt*appDefDto.FP_CMN_LIST_MEMMAX;
		} catch (Exception e) {
			// エラーになった場合は初期値を設定
			mlistForm.offset = 0;
			mlistForm.pgcnt = 0;
			// TODO: handle exception
		}
		//グループが選択されている場合
		if(mlistForm.type.equals("1")){
			//グループ件数取得
			String g = null;
			String d = null;
			try {
				g = CmnUtility.xmlescape(mlistForm.gid);
				d = CmnUtility.xmlescape(mlistForm.domain);
			} catch (Exception e) {
				e.printStackTrace();
			}

			mlistForm.resultscnt = mlistService.cntGroupMemList(g,d);
			// グループリスト取得
			resultsList = mlistService.selGroupMemList(g,d,mlistForm.offset,appDefDto.FP_CMN_LIST_MEMMAX);
			if(mlistForm.resultscnt>0){
				gName = resultsList.get(0).get("gname").toString();
			}

		//フォロー/フォロワー
		}else{
			//フォロー/フォロワー件数取得
			if(mlistForm.type.equals("2")){//フォロー
				mlistForm.resultscnt = m.follownumber;
				// ページング処理の関係で今のところ全件取得を行う 2010/01/19 高際
				resultsList = commonService.getMidList(mlistForm.type,userInfoDto.visitMemberId);
			}else if(mlistForm.type.equals("3")){//フォロワー
				mlistForm.resultscnt = m.followernumber;
				// フォロー/フォロワー一覧データ取得
				resultsList = mlistService.selFollowList(
						userInfoDto.visitMemberId,
						mlistForm.type,
						mlistForm.offset,
						appDefDto.FP_CMN_LIST_MEMMAX
					);
			}

		}
		//グループ一覧取得
		groupList = commonService.getMidList("4",userInfoDto.visitMemberId);
		//results = mlistService.selectMlist(userInfoDto.visitMemberId,"0",mlistForm.offset,appDefDto.FP_CMN_LIST_MEMMAX);
	}

	// ■フォロー解除確認画面初期表示
	@Execute(validator=false,urlPattern="confirm/{mid}")
	public String confirm(){
		results = mlistService.selectFollow(userInfoDto.memberId,mlistForm.mid);
		if(results.size()==0) return "error.jsp";
		return "confirm.jsp";
	}

	//フォロー解除実行・実行後はメンバー一覧画面へ遷移
	@Execute(validator=false)
	public String exeDelete(){
		results = mlistService.selectFollow(userInfoDto.memberId,mlistForm.mid);
		if(results.size()==0) return "error.jsp";
		//フォロー解除
		mlistService.setFollowState("1",userInfoDto.memberId,mlistForm.mid);
		//フォロー/フォロワー数変更
		membersService.updFCnt("1",userInfoDto.memberId,mlistForm.mid);
		return "/pc/mlist/follow/"+mlistForm.vmid+"?redirect=true";
	}

	//フォロー解除確認・前の画面に戻る
	@Execute(validator=false)
	public String stop(){
		logger.debug(mlistForm.vmid);
		return "/pc/mlist/follow/"+mlistForm.vmid+"?redirect=true";
	}

}