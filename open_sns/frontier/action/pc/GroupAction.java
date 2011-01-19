package frontier.action.pc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.RequestUtil;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.form.pc.GroupForm;
import frontier.service.GroupService;
import frontier.service.MembersService;

public class GroupAction {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    public AppDefDto appDefDto;
    @Resource
    public UserInfoDto userInfoDto;
    @Resource
    protected GroupService groupService;
    @Resource
    protected MembersService membersService;
    @ActionForm
    @Resource
    public GroupForm groupForm;
    
    private Members members;
    //エラー
	public ActionMessages errors = new ActionMessages();
    //グループ一覧
    public List<BeanMap> groupList;
    //グループ一件
    public List<BeanMap> groupOne;
    //グループメンバー一覧
    public List<BeanMap> groupMemberList;
    //登録・編集の文言
    public String mode;
    //画像ある・なしのCSS
    public boolean picFlg;
    public String picLeft;
    public String picRight;
    //メンバーの件数
    public long resultscnt;
    
    //グループ一覧表示
    @Execute(validate="checkAuth",input="/common/auth.jsp")
    public String index(){
    	
    	initIndex();
    	
    	return "list.jsp";
    }
    
    //グループ新規登録
    @Execute(validate="checkAuth",input="/common/auth.jsp")
    public String entry(){
    	//visitMemberIdの設定
    	setVisitMemberId();
    	
    	groupForm.gid = null;
    	//画像サイズ超過を拾う
    	overPicSize();
    	
    	//初期処理
    	initEntry(true);
    	
    	//グループメンバー検索(ここでは未参加の人のみ検索される)
    	//改ページに備えて初期化
    	initPageCnt();
    	searchGroupMember(true);
    	
    	return "entry.jsp";
    }
    
    //グループ編集
    @Execute(validate="checkAuth",input="/common/auth.jsp",urlPattern="update/{gid}")
    public String update(){
    	//visitMemberIdの設定
    	setVisitMemberId();
    	
    	//画像サイズ超過を拾う
    	overPicSize();
    	
    	//グループ情報検索
    	setGroupInfo();
    	
    	//初期処理
    	initEntry(true);
    	
    	//グループメンバー検索(参加者+未参加者)
    	//改ページに備えて初期化
    	initPageCnt();
    	searchGroupMember(true);
    	
    	return "entry.jsp";
    }
    
    //登録ボタン押下
    @Execute(validate="check",input="reView")
    public String insert(){
    	
    	//グループ登録
    	try {
			groupService.insertGroup(userInfoDto.memberId,groupForm.gname,groupForm.picpath,groupForm.checkJoin,groupForm.checkAuth,groupForm);
		} catch (IOException e) {
			e.printStackTrace();
			return "/common/error.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			return "/common/error.jsp";
		}
    	
    	//更新に飛ばす
    	return "update/"+groupForm.gid+"?redirect=true";
    }
    
    //グループ更新
    @Execute(validate="check",input="reView")
    public String updateGroup(){
    	
    	//グループ更新
    	try {
    		List<BeanMap> paramList = groupService.selGroupMember(groupForm.gid, groupForm.searchmem, groupForm.r1, groupForm.joincheck, appDefDto.FP_GRP_LIST_MAX, groupForm.offset,groupForm.sortname);

			groupService.updateGroup(userInfoDto.memberId,groupForm.gname,groupForm.picpath,groupForm.checkJoin,groupForm.checkAuth,groupForm.gid,paramList);
		} catch (IOException e) {
			e.printStackTrace();
			return "/common/error.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			return "/common/error.jsp";
		}
    	
    	return "update/"+groupForm.gid+"?redirect=true";
    }
    
    //画像削除
    @Execute(validator=false)
    public String delPic(){
    	//グループ画像削除実行
    	groupService.updGroupPic(groupForm.gid, userInfoDto.memberId);
    	
    	return "update/"+groupForm.gid+"?redirect=true";
    }
    
    //検索
    @Execute(validator=false)
    public String search(){
    	//visitMemberIdの設定
    	setVisitMemberId();
    	
    	//初期処理
    	initEntry(false);
    	
    	//グループメンバー検索(参加者+未参加者)
    	initPageCnt();
    	searchGroupMember(true);
    	
    	return "entry.jsp";
    }
    
	//次を表示リンク押下時
	@Execute(validator=false,reset="resetSearch")
	public String nxtpg(){
		//改ページ処理実行
		setGroupPage(1);
		
		//F5対策
		return "/pc/group/reSearchView?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false,reset="resetSearch")
	public String prepg(){
		//改ページ処理実行
		setGroupPage(-1);		
		
		//F5対策
		return "/pc/group/reSearchView?redirect=true";
	}
    
	//画面再表示用メソッド（入力エラー）
	@Execute(validator=false,reset="dummy")
	public String reView(){
    	//初期処理
    	initEntry(true);
    	
    	//グループメンバー検索(参加者+未参加者)
    	searchGroupMember(false);
    	
		return "entry.jsp";
	}
	
	
	//画面再表示用メソッド（検索）
	@Execute(validator=false,reset="resetSearch")
	public String reSearchView(){
    	//visitMemberIdの設定
    	setVisitMemberId();
		
    	//初期処理
    	initEntry(false);
    	
    	//グループメンバー検索(参加者+未参加者)
    	searchGroupMember(true);
    	
		return "entry.jsp";
	}
	
	//削除確認画面へ
	@Execute(validate="checkAuth",input="/common/auth.jsp")
	public String confirm(){
    	//visitMemberIdの設定
    	setVisitMemberId();
		
		return "confirm.jsp";
	}
	
	//グループ削除
	@Execute(validator=false)
	public String delete(){
		
		//グループ削除実行
		groupService.delGroup(groupForm.gid, userInfoDto.memberId);
		
		return "index?redirect=true";
	}
	
	//グループ削除確認やめるボタン
	@Execute(validator=false)
	public String stop(){
		return "update/"+groupForm.gid+"?redirect=true";
	}
	
    //グループ一覧初期処理
    private void initIndex(){
    	//ボタン出し分け設定
    	userInfoDto.visitMemberId = userInfoDto.memberId;
    	
    	//グループ一覧検索
    	groupList = groupService.selGroup(null);
    }
    
    //グループ登録・編集初期処理
    private void initEntry(boolean sFlg){
    	//文言の判定
    	if(groupForm.gid == null){
    		mode = "登録";
    		//画像なし
    		picLeft = "input-left-no-pic";
    		picRight = "input-right-no-pic";
    		picFlg = false;
    	}else{
    		mode = "編集";
    		if(groupForm.pic==null || groupForm.pic.equals("")){
        		//画像なし
        		picLeft = "input-left-no-pic";
        		picRight = "input-right-no-pic";
        		picFlg = false;
        	}else{
        		//画像あり
        		picLeft = "input-left-pic";
        		picRight = "input-right-pic";
        		picFlg = true;    			
    		}
    	}
    	
    	//検索条件を初期化するかどうかを設定する
    	if(sFlg){
    		//初期化する。
    		groupForm.r1 = "1";
    	}
    }
    
    //グループ情報をsessionに設定
    private void setGroupInfo(){
    	//パラメータgidを条件にグループ情報を検索する
    	groupOne = groupService.selGroup(groupForm.gid);

    	//検索結果が存在する場合、値をsessionに詰める
    	if(groupOne != null){
    		groupForm.gname = (String)groupOne.get(0).get("gname");
    		groupForm.pic = (String)groupOne.get(0).get("pic");
    		groupForm.joinnumber = (Integer)groupOne.get(0).get("joinnumber");
    	}
    }
    
    //グループメンバー検索
    private void searchGroupMember(boolean exeFlg){
    	groupMemberList = groupService.selGroupMember(groupForm.gid, groupForm.searchmem, groupForm.r1, groupForm.joincheck, appDefDto.FP_GRP_LIST_MAX, groupForm.offset,groupForm.sortname);

    	//検索結果が存在する場合、参加者と管理者をsessionに詰める
    	//入力エラーの場合は行わない
    	if(groupMemberList != null){
    		//通常と入力エラーの場合で比較対象を変更する
    		//通常：１、入力エラー：チェックボックスの値
    		if(exeFlg){
    			//通常
        		for(int i=0;i<groupMemberList.size();i++){
        			//参加者をsessionに詰める
        			if(groupMemberList.get(i).get("joinflg").equals("1")){
        				groupForm.checkJoin.add((String)groupMemberList.get(i).get("mid"));
        			}else{
        				//検索結果と数を合わせるために参加していない場合もdummyを設定しておく
        				groupForm.checkJoin.add("");
        			}
        			
        			//管理者をsessionに詰める
        			if(groupMemberList.get(i).get("manageflg").equals("1")){
        				groupForm.checkAuth.add((String)groupMemberList.get(i).get("mid"));
        			}else{
        				//検索結果と数を合わせるために管理者でない場合もdummyを設定しておく
        				groupForm.checkAuth.add("");
        			}			
        		}    			
    		}else{
    			//入力エラー
    			String localMid = "";
    			
    			//参加チェックボックスを比較ように詰め替え
    			List<String> lsj = new ArrayList<String>();
    			for(int i=0;i<groupForm.checkJoin.size();i++){
    				lsj.add(groupForm.checkJoin.get(i));
    			}
    			//初期化
    			groupForm.checkJoin = new ArrayList<String>();
    			
    			//権限チェックボックスを比較できるように詰め替え
    			List<Object> lsa = new ArrayList<Object>();
    			for(int i=0;i<groupForm.checkAuth.size();i++){
    				lsa.add(groupForm.checkAuth.get(i));
    			}
    			//初期化
    			groupForm.checkAuth = new ArrayList<Object>();
    			
        		for(int i=0;i<groupMemberList.size();i++){
        			localMid = (String)groupMemberList.get(i).get("mid");

        			//参加者をsessionに詰める
        			if(lsj.contains(localMid)){
        				groupForm.checkJoin.add(localMid);        				
        			}else{
        				//検索結果と数を合わせるために参加していない場合もdummyを設定しておく
        				groupForm.checkJoin.add("");        				        				
        			}
        			
        			//管理者をsessionに詰める
        			if(lsa.contains(localMid)){
        				groupForm.checkAuth.add(localMid);        				
        			}else{
        				//検索結果と数を合わせるために参加していない場合もdummyを設定しておく
        				groupForm.checkAuth.add("");        				        				
        			}
        		}
    		}

    	}
    	
    	//検索対象にHITする全メンバーの件数を取得すう
    	resultscnt = groupService.cntGroupMember(groupForm.gid, groupForm.searchmem, groupForm.r1, groupForm.joincheck);
    }
    
    //管理者チェックボックスをMapに詰め替える
    private Map<Object,Object> setAuthMap(){
    	Map<Object,Object> authMap = new HashMap<Object,Object>();
    	
    	if(authMap == null){
        	//管理者チェックボックスをMapに詰め替える
    		//管理者がいる場合のみ
        	for(int i=0;i<groupForm.checkAuth.size();i++){
        		authMap.put(groupForm.checkAuth.get(i), groupForm.checkAuth.get(i));
        	}    		
    	}
    	
    	return authMap;
    }
    
    //ページカウンター初期化
    private void initPageCnt(){
    	groupForm.pgcnt = 0;
    	groupForm.offset = 0;
    	groupForm.sortname = null;
    }
    
	//グループメンバー一覧改ページ処理
	private void setGroupPage(int num){
		try {
			// ページ遷移用の計算
			groupForm.pgcnt = groupForm.pgcnt + num;
			groupForm.offset = groupForm.pgcnt * appDefDto.FP_GRP_LIST_MAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			groupForm.pgcnt = 1;
			groupForm.offset = 0;
		}		
	}
    
	//ボタン制御用visitMemberId設定処理
	private void setVisitMemberId(){
    	//ボタン出し分け設定
    	userInfoDto.visitMemberId = userInfoDto.memberId;
	}
	
    //権限チェック
    public ActionMessages checkAuth(){
    	ActionMessages errors = new ActionMessages();
    	
		//ﾒﾝﾊﾞｰ情報の取得
		members = membersService.getResultById(userInfoDto.memberId);
		
		//権限がない人はエラー
		if(!members.managementLevel.equals("1")){
			//ダミーのエラーメッセージを設定
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required","権限"));
		}
    	
		return errors;
    }
    
    //グループ登録・更新入力チェック
    public ActionMessages check(){
    	//グループ名重複チェック
    	if(groupService.chkGroupName(groupForm.gname,groupForm.gid)>=1){
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.group.repetition"));
    	}
    	
    	//未参加ユーザを管理者にしていないかのチェック
    	if(groupForm.checkJoin==null && groupForm.checkAuth==null){
        	//未設定は何もしない。
    	} else if(groupForm.checkJoin==null && groupForm.checkAuth.size()>0){
    		//管理者のみにチェックがある。
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.group.joinauth"));
    	} else if(groupForm.checkJoin.size()< groupForm.checkAuth.size()){
    		//権限選択の方がが多い。
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.group.joinauth"));    		
    	}else if(groupForm.checkJoin.size()>0 && groupForm.checkAuth.size()>0){
    		for(int i=0;i<groupForm.checkAuth.size();i++){
    			boolean errorFlg = true;
    			for(int j=0;j<groupForm.checkJoin.size();j++){
    				//ID同士が一緒かどうかを調べる
    				if(groupForm.checkAuth.get(i).equals(groupForm.checkJoin.get(j))){
    					errorFlg =false;
    					break;
    				}
    			}
				if(errorFlg){
					//値が不正
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.group.joinauth")); 
					break;
				}
    		}
    	}
    	
		FormFile picpath = groupForm.picpath;
		// 画像パス名の長さが0以上ならチェック
		//null対策
		if(picpath!=null){
			if(picpath.getFileName().length()>0){
				// ファイルサイズ＆タイプチェック
				CmnUtility.checkPhotoFile(errors,picpath,"");
			}
		}
		return errors;    	
    }
    
    //画像サイズ超過を拾う
    public void overPicSize(){
        //リクエストから、SAStrutsが格納した例外を取得します。
        SizeLimitExceededException e = (SizeLimitExceededException) RequestUtil.getRequest()
                        .getAttribute(S2MultipartRequestHandler.SIZE_EXCEPTION_KEY);
        if(e!=null){
        	ActionMessages errors = new ActionMessages();
        	
        	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            		"errors.upload.size",new Object[] { e.getActualSize(),e.getPermittedSize()  }));
    		//ActionMessagesUtil.addErrors(request, errors);
        }
    }
   
}
