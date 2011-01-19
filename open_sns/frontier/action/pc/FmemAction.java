package frontier.action.pc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.FmemForm;
import frontier.service.FmemService;
import frontier.service.GroupService;

public class FmemAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected FmemService fmemService;
	@Resource
	protected GroupService groupService;
	@ActionForm
	@Resource
	public FmemForm fmemForm;
	
	//画面表示用
	public List<BeanMap> resultsList;
	public long resultscnt;
	public String tittleName;
	
	//定数
	final String MEM_TITTLE = "メンバー一覧";
	final String GRP_TITTLE = "グループ一覧";
	final String GRP_MEM_TITTLE = "のメンバー一覧";
	
	
	//グループ・メンバー一覧画面：メンバーボタン、メンバーリンク押下時
	@Execute(validate="checkAuth",input="/pc/top/")
	public String index(){
		//ページカウンタ初期化
		initPageCnt();
		setVisitMemberId();
		
		//メンバー選択状態に設定する
		setType("2");
		
		//メンバー一覧を検索する
		initSearch();
		
		return "index.jsp";
	}
	
	//グループ・メンバー一覧画面：グループリンク押下時
	@Execute(validate="checkAuth",input="/pc/top/")
	public String groupAll(){
		//ページカウンタ初期化
		initPageCnt();
		setVisitMemberId();

		//グループ選択状態に設定する
		setType("1");
		
		//グループ一覧を検索する
		initSearch();
		
		return "index.jsp";
	}
	
	//グループ・メンバー一覧画面：グループ画像押下時（グループが特定される場合）
	@Execute(validate="checkAuth",input="/pc/top/",urlPattern="group/{gid}")
	public String group(){
		//ページカウンタ初期化
		initPageCnt();
		setVisitMemberId();

		//グループ画像選択状態に設定する
		setType("3");

		//特定のグループに属するメンバー一覧を検索する
		initSearch();
		
		return "index.jsp";
	}
	
	//グループ・メンバー一覧画面：次を表示リンク押下時
	@Execute(validator=false,urlPattern="nxtpg/{type}")
	public String nxtpg(){
		//改ページ処理実行
		setFMemberPage(1);

		//F5対策
		return "/pc/fmem/reView?redirect=true";
	}
	
	//グループ・メンバー一覧画面：前を表示リンク押下時
	@Execute(validator=false,urlPattern="prepg/{type}")
	public String prepg(){
		//改ページ処理実行
		setFMemberPage(-1);		

		//F5対策
		return "/pc/fmem/reView?redirect=true";
	}

	//グループ・メンバー一覧画面：改ページ後の再表示
	@Execute(validate="checkAuth",input="/pc/top/")
	public String reView(){
		//検索実行
		initSearch();
		setVisitMemberId();

		return "index.jsp";
	}
	
	//遷移の状態を設定する
	private void setType(String type){
		fmemForm.type = type;
	}
	
	//タイトルを設定する
	private void setTittle(String tittle){
		tittleName = tittle;
	}

    //ページカウンター初期化
    private void initPageCnt(){
    	fmemForm.pgcnt = 0;
    	fmemForm.offset = 0;
    }
	
	//改ページ処理
	private void setFMemberPage(int num){
		try {
			// ページ遷移用の計算
			fmemForm.pgcnt = fmemForm.pgcnt + num;
			fmemForm.offset = fmemForm.pgcnt * appDefDto.FP_CMN_LIST_MEMMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			fmemForm.pgcnt = 1;
			fmemForm.offset = 0;
		}		
	}
    
    //各種検索実行
    private void initSearch(){
    	String flg = fmemForm.type;
    	
    	if(flg.equals("1")){
    		//グループ一覧を検索する
    		resultsList = groupService.selGroup(fmemForm.offset,appDefDto.FP_CMN_LIST_MEMMAX);
    		resultscnt = groupService.cntSelGroup();

    		//タイトルを設定する		
    		setTittle(GRP_TITTLE);
    	}else if(flg.equals("2")){
    		//メンバー一覧を検索する
    		resultsList = fmemService.selFmemList("1",null,fmemForm.offset,appDefDto.FP_CMN_LIST_MEMMAX);
    		resultscnt = fmemService.selCntFmemList("1",null);

    		//タイトルを設定する
    		setTittle(MEM_TITTLE);
    	}else if(flg.equals("3")){
    		//特定のグループに属するメンバー一覧を検索する
    		resultsList = fmemService.selFmemList("2",fmemForm.gid,fmemForm.offset,appDefDto.FP_CMN_LIST_MEMMAX);
    		resultscnt = fmemService.selCntFmemList("2",fmemForm.gid);

    		//タイトルを設定する		
    		setTittle((String)resultsList.get(0).get("gname")+GRP_MEM_TITTLE);
    	}
    }
    
	//ボタン出し分け設定
    private void setVisitMemberId(){
    	userInfoDto.visitMemberId = userInfoDto.memberId;    	
    }
    
    //権限チェック
    public ActionMessages checkAuth(){
    	ActionMessages errors = new ActionMessages();
    	
		// ユーザのタイプ別処理
		if(userInfoDto.membertype.equals("0")){
			// 本Frontierユーザの場合は遷移不可
			//ダミーのエラーメッセージを設定
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required","権限"));
		}
    	
		return errors;
    }

}
