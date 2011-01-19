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
import frontier.entity.FrontierRssInfo;
import frontier.entity.Members;
import frontier.form.pc.RssmanageForm;
import frontier.service.CommonService;
import frontier.service.GroupService;
import frontier.service.MembersService;
import frontier.service.RssmanageService;

public class RssmanageAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected MembersService membersService;
	@Resource
	protected RssmanageService rssmanageService;
	@Resource
	protected CommonService commonService;
	@Resource
	protected GroupService groupService;
	@ActionForm
	@Resource
	public RssmanageForm rssmanageForm;
	
	//インスタンス変数
	private Members members;
	private FrontierRssInfo frontierRssInfo;
	//画面表示用
	public List<BeanMap> rssList;
	public List<BeanMap> rssEntryList;
	public List<BeanMap> rssMember;
	public List<BeanMap> groupList;
	public String vRssMember;
	public String vRssName;
	public boolean vMode;
    //メンバーの件数
    public long resultscnt;
	
	@Execute(validate="checkAuth",input="/common/auth.jsp")
	public String index(){
		initIndex();
		
		return "list.jsp";
	}

	//新規登録の場合
	@Execute(validate="checkAuth",input="/common/auth.jsp")
	public String newentry(){
    	//ボタン出し分け設定
    	setVisitMemberId();

    	//初期設定処理
    	searchRssMember("");
    	
		return "entry.jsp";
	}

	//更新の場合
	@Execute(validate="checkAuth",input="/common/auth.jsp",urlPattern="updentry/{rssid}")
	public String updentry(){
    	//ボタン出し分け設定
    	setVisitMemberId();
		
    	//DB読み込み、セッション設定
    	initUpdentry();
    	
		return "entry.jsp";
	}

	
	//RSS設定形式登録画面：入力エラー時再表示の場合
	@Execute(validate="checkAuth",input="/common/auth.jsp",reset="dummy")
	public String errEntry(){
    	//ボタン出し分け設定
    	setVisitMemberId();
		
    	//初期設定処理
    	searchRssMember(rssmanageForm.rssid);
    	
		return "entry.jsp";
	}
	
	//RSS設定新規登録
	@Execute(validate="checkEntry",input="errEntry")
	public String finsert(){
		//RSS設定情報登録
		Integer id = rssmanageService.insFrontierRssInfo(rssmanageForm.pname, rssmanageForm.fname, rssmanageForm.contents, userInfoDto.memberId);
		
		return "memberadd/"+id+"/?redirect=true";
	}
	
	//RSS設定更新
	@Execute(validate="checkEntry",input="errEntry",reset="dummy")
	public String fupdate(){
		//RSS設定情報登録
		rssmanageService.updFrontierRssInfo(rssmanageForm.pname, rssmanageForm.fname, rssmanageForm.contents, userInfoDto.memberId,rssmanageForm.rssid);
		
		return "index?redirect=true";
	}
	
	//RSSメンバー追加
	@Execute(validate="checkAuth",input="/common/auth.jsp",urlPattern="memberadd/{rssid}")
	public String memberadd(){
		//初回検索モードにする
		rssmanageForm.searchFlg = "0";
		//ニックネームをデフォルト選択とする
		rssmanageForm.rd01 = "1";
		
    	//改ページに備えて初期化
    	initPageCnt();
		
		//画面表示に必要な処理の実行
		initMemberIndex();
		
		return "add.jsp";
	}
	
	//RSSメンバー追加:検索ボタン押下
	@Execute(validator=false,reset="dummy")
	public String search(){
		//通常検索モードにする
		rssmanageForm.searchFlg = "1";

    	//改ページに備えて初期化
    	initPageCnt();
		
		//画面表示に必要な処理の実行
		initMemberIndex();		
		
		return "add.jsp";
	}
	
	//RSSメンバー追加:登録ボタン押下
	@Execute(validator=false,reset="resetSearch")	
	public String memberAdd(){
		//通常検索モードにする
		rssmanageForm.searchFlg = "1";
		
		//現在の検索条件で検索する
    	searchRssMember(); 
		
    	//RSSメンバーの登録・削除処理の実行
		rssmanageService.addRssMember(userInfoDto.memberId, rssmanageForm.rssid, rssmanageForm.checkJoin, rssMember);
		
		return "index?redirect=true";
	}
	
	//RSSメンバー追加:次を表示リンク押下時
	@Execute(validator=false,reset="dummy")
	public String nxtpg(){
		//改ページ処理実行
		setRssMemberPage(1);
		
		//F5対策
		return "/pc/rssmanage/reSearch?redirect=true";
	}
	
	//RSSメンバー追加:前を表示リンク押下時
	@Execute(validator=false,reset="dummy")
	public String prepg(){
		//改ページ処理実行
		setRssMemberPage(-1);		
		
		//F5対策
		return "/pc/rssmanage/reSearch?redirect=true";
	}
	
	//RSSメンバー追加:再表示
	@Execute(validator=false,reset="dummy")
	public String reSearch(){
		//画面表示に必要な処理の実行
		initMemberIndex();		
		
		return "add.jsp";
	}

	//RSSファイル削除確認：詳細から遷移
	@Execute(validator=false,reset="dummy")
	public String confirm(){
		//ボタン出しわけ設定
		setVisitMemberId();
		
		//遷移元設定
		vMode = true;
		
		return "delete.jsp";
	}
	
	//RSSファイル削除確認：一覧から遷移
	@Execute(validate="checkAuth",input="/common/auth.jsp",urlPattern="confirmlist/{rssid}",reset="dummy")
	public String confirmlist(){
		//ボタン出しわけ設定
		setVisitMemberId();
		
		//遷移元設定
		vMode = false;

		return "delete.jsp";		
	}
	
	//RSS削除確認：削除ボタン押下
	@Execute(validator=false,reset="dummy")
	public String delete(){
		//RSSメンバー削除実行
		rssmanageService.deleteFrontierRssMember(rssmanageForm.rssid, null);
		
		//RSSファイル設定削除実行
		rssmanageService.deleteFrontierRssInfo(rssmanageForm.rssid);
		
		return "index?redirect=true";
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

    //RSS設定形式登録画面：関連チェック
    public ActionMessages checkEntry(){
    	ActionMessages errors = new ActionMessages();

    	//パターン名重複チェック
    	if(rssmanageService.chkPatternName(rssmanageForm.pname,rssmanageForm.rssid)>=1){
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.rss.repetition","パターン名"));
    	}    	

    	//ファイル名重複チェック
    	if(rssmanageService.chkFileName(rssmanageForm.fname,rssmanageForm.rssid)>=1){
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.rss.repetition","ファイル名"));
    	}
    	
    	if(rssmanageForm.fname!=null && !rssmanageForm.fname.equals("")){
    		//ファイル名登録文字チェック（半角英数字と_のみ）
			if (rssmanageForm.fname.matches("[0-9a-zA-Z_]+")) {
				//logger.debug("************ アカウントが半角英数のみです。 ****************");
			}else{
			    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			    "errors.rss.letterclass","ファイル名"));
			}
    	}
    	
    	return errors;
    }
    
	//ボタン出し分け設定
    private void setVisitMemberId(){
    	userInfoDto.visitMemberId = userInfoDto.memberId;    	
    }
    
    
    //RSS設定情報一覧初期処理
    private void initIndex(){
    	Integer id = null;
    	String midStr = null;
    	
    	//ボタン出し分け設定
    	setVisitMemberId();
    	
    	//RSS設定情報一覧を検索
    	rssList = rssmanageService.selRssInfoAll();
    	
    	//上で取得したRSS設定情報一覧からメンバーを検索する
    	//SQL1個では取得できないので仕方なくループする
    	for(int i=0;i<rssList.size();i++){
    		//初期化
    		midStr = "";
    		
    		id = (Integer)rssList.get(i).get("id");
    		
    		//RSSメンバーを検索
    		List<BeanMap> lbm = rssmanageService.selRssMemberOne(id);
    		
    		//ニックネームを連結する
    		midStr = makeCSV(lbm,"nickname");
    		
    		//メンバー未設定の場合
    		if(lbm.size()==0){
    			midStr = "メンバが設定されていません。メンバ変更リンクより設定して下さい。";
    		}
    		
    		//RSS設定情報一覧にメンバーを追加する
    		rssList.get(i).put("nickname", midStr);
    	}
    	
    }
    
    //RSS設定形式登録画面：更新時の初期設定
    private void initUpdentry(){
        //RSS設定情報を検索する
        frontierRssInfo = rssmanageService.selFrontierRssInfoOne(rssmanageForm.rssid);
        	
        //セッションに設定
        rssmanageForm.pname = frontierRssInfo.patternname;
        rssmanageForm.fname = frontierRssInfo.filename;
        rssmanageForm.contents = frontierRssInfo.detail;
        	
    	//RSSメンバーを検索
        searchRssMember(rssmanageForm.rssid);		   	
    }
    
    //RSS設定形式登録画面：更新時のRSSメンバーを検索
    private void searchRssMember(String rid){
    	if(rssmanageForm.rssid!=null && !rssmanageForm.rssid.equals("")){
           	//RSSメンバーを検索
            rssEntryList = rssmanageService.selRssMemberOne(Integer.parseInt(rid));

            //ニックネームを連結する
            vRssMember = makeCSV(rssEntryList,"nickname");
            	
        	//メンバー未設定の場合
        	if(rssEntryList.size()==0){
        		vRssMember = "未設定";
        	}    		
    	}else{
    		vRssMember = "未設定";
    	}
    }
    
    //RSSメンバー追加画面：RSSメンバーを検索
    private void initMemberIndex(){
    	String gname;
    	
    	//ボタン出し分け設定
    	setVisitMemberId();
    	
    	//RSSパターン名を取得
    	frontierRssInfo = rssmanageService.selFrontierRssInfoOne(rssmanageForm.rssid);
    	vRssName = frontierRssInfo.patternname;
    	
    	//グループを検索
    	groupList = groupService.selGroup(null);
    	
		//RSSメンバーを検索
    	searchRssMember();
		
		//初期化
		rssmanageForm.checkJoin = new ArrayList<String>();
		
		//メンバーが所属しているグループを検索
		for(int i=0;i<rssMember.size();i++){
			gname = "";
			gname = searchRssMemberIns((String)rssMember.get(i).get("mid"));
			
			//検索結果に追加
			rssMember.get(i).put("gname", gname);
			
			//参加者をsessionに詰める
			if(rssMember.get(i).get("joinflg").equals("1")){
				rssmanageForm.checkJoin.add((String)rssMember.get(i).get("mid"));
			}else{
				//検索結果と数を合わせるために参加していない場合も空白を設定しておく
				rssmanageForm.checkJoin.add("");
			}
		}
    	
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
    
    //RSSメンバー追加画面：RSSメンバーを検索
    private void searchRssMember(){
		//RSSメンバーを検索
		rssMember = rssmanageService.selRssMemberSearch(rssmanageForm.rssid,rssmanageForm.searchFlg,rssmanageForm.searchword,rssmanageForm.rd01,rssmanageForm.searchgroup,appDefDto.FP_RSS_MEMBER_LIST_MAX,rssmanageForm.offset,rssmanageForm.sortname);
		//RSSメンバーの全件を取得する。
		resultscnt = rssmanageService.selCntRssMemberSearch(rssmanageForm.rssid,rssmanageForm.searchFlg,rssmanageForm.searchword,rssmanageForm.rd01,rssmanageForm.searchgroup);
    }
    
    //ページカウンター初期化
    private void initPageCnt(){
    	rssmanageForm.pgcnt = 0;
    	rssmanageForm.offset = 0;
    	rssmanageForm.sortname = null;
    }

	//RSSメンバー一覧改ページ処理
	private void setRssMemberPage(int num){
		try {
			// ページ遷移用の計算
			rssmanageForm.pgcnt = rssmanageForm.pgcnt + num;
			rssmanageForm.offset = rssmanageForm.pgcnt * appDefDto.FP_RSS_MEMBER_LIST_MAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			rssmanageForm.pgcnt = 1;
			rssmanageForm.offset = 0;
		}		
	}
}
