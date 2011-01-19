package frontier.action.pc.com;

import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.form.pc.com.ComForm;
import frontier.service.CommunityService;
import frontier.service.EntjoinService;
import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;

public class MlistAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@ActionForm
	@Resource
	public ComForm comForm;
	@Resource
	public CommunityDto communityDto;
	@Resource
	protected CommunityService communityService;
	@Resource
	protected EntjoinService entjoinService;
	public List<BeanMap> results;
	@Resource
	public UserInfoDto userInfoDto;

	public String nickname;

	// ■初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// init処理
		init();
		// 初期値を設定
		comForm.offset = 0;
		comForm.pgcnt = 0;
		comForm.mlistflg = "0";
		// 一覧表示処理へ
		return "sellist";
	}
	
	// ■初期表示（メンバー管理）
	@Execute(validator=false,urlPattern="manage/{cid}")
	public String manage(){
		logger.debug("******* メンバー管理 ************");
		
		// init処理
		init();
		
		//権限の有無チェック
		if(!communityDto.makabletopic.equals("1")){
			//権限がなければエラー
			return "error.jsp";
		}
		
		// 初期値を設定
		comForm.offset = 0;
		comForm.pgcnt = 0;
		comForm.mlistflg = "1";
		// 一覧表示処理へ
		return "sellist";
	}
	
	// ■メンバー削除確認画面初期表示
	@Execute(validator=false,urlPattern="confirm/{mid}")
	public String confirm(){
		//権限の有無チェック
		if(!communityDto.makabletopic.equals("1")){
			//権限がなければエラー
			return "error.jsp";
		}
		
		results = communityService.selectFriend(comForm.mid,communityDto.cid);
		
		nickname = results.get(0).get("nickname").toString();
		
		return "confirm.jsp";
	}

	// ■ページング処理
	@Execute(validator=false,urlPattern="movelist/{cid}/{pgcnt}")
	public String movelist(){
		// init処理
		init();
		// 一覧表示処理へ
		return "sellist";
	}
	
	// 一覧表示処理
	@Execute(validator=false)
	public String sellist(){
		// 件数取得
		comForm.resultscnt = communityService.cntMemList(communityDto.cid);
		// 一覧データ取得
		results = communityService.selectMemList(communityDto.cid, comForm.offset, appDefDto.FP_COM_LIST_MEMMAX);
		
		if(comForm.mlistflg.equals("1")){
			logger.debug("******** メンバー管理画面だよ *********");
			// メンバー管理リスト表示
			return "manage.jsp";
		}
		
		logger.debug("******** メンバー一覧画面だよ *********");
		// 参加メンバーリスト表示
		return "list.jsp";
	}
	
	// init処理
	private void init(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = comForm.cid;
		try {
			// offset設定
			comForm.offset = comForm.pgcnt*appDefDto.FP_COM_LIST_MEMMAX;
		} catch (Exception e) {
			// エラーになった場合は初期値を設定
			comForm.offset = 0;
			comForm.pgcnt = 0;
			// TODO: handle exception
		}
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
	}
	
	
	
	//メンバー削除実行・実行後はメンバー一覧画面へ遷移
	@Execute(validator=false)
	public String exeDelete(){
		// メンバー現在状況チェック （０：申込み、１：参加（承認）、２：拒否、３：脱退）
		results = communityService.selectFriend(comForm.mid,communityDto.cid);
		if(results.get(0).get("joinstatus").toString().equals("1")){
			entjoinService.updateCommunityEnterant(communityDto.cid,comForm.mid,"3",results.get(0).get("requiredmsg").toString());
		}
		
		comForm.mlistflg = "1";
		return manage();
	}
	
	//メンバー削除確認・前の画面に戻る
	@Execute(validator=false)
	public String stop(){
		comForm.mlistflg = "1";
		return manage();
	}

	
}
