package frontier.action.pc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.SearchForm;
import frontier.service.SearchService;
import frontier.service.TopService;

public class SearchAction {
    Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	protected SearchService searchService;
	@Resource
	protected TopService topService;
    @Resource
    public AppDefDto appDefDto;
    @Resource
    public UserInfoDto userInfoDto;
    
    @ActionForm
    @Resource
    protected SearchForm searchForm;
	
    public List<BeanMap> results;
    public List<BeanMap> resultstest;

    public long resultscnt;
	
	//ヘッダの検索ボタン押下時
	@Execute(validator=false)
	public String index(){

		//初期処理
		initSearchAction();

		//検索の実行と結果の編集
		if(searchForm.searchtext.replaceAll("　", " ").trim().length() != 0){
			execSearch(0);
		}
		
		return "/pc/search/list.jsp";
	}
	
	//検索結果一覧画面の検索ボタン押下時
	@Execute(validator=false)
	public String searchbtn(){
		
		//テキストボックスの値を詰め替え
		searchForm.searchtext = searchForm.search;
		searchForm.mydtonly = searchForm.searchchk;
		
		//初期処理
		initSearchAction();
		
		//検索の実行と結果の編集
		if(searchForm.searchtext.trim().length() != 0){
			execSearch(0);
		}
		
		return "/pc/search/list.jsp";
	}
	
	//改ページの表示用
	@Execute(validator=false,reset="dummy")
	public String view(){
		//初期処理
		initSearchAction();
		
		//検索の実行
		execSearch(searchForm.offset);	
		
		return "/pc/search/list.jsp";
	}
	
	//次を表示リンク押下時
	@Execute(validator=false,reset="dummy")
	public String nxtpg(){
		//改ページ処理実行
		setPage(1);
		
		//F5対策
		return "/pc/search/view?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false,reset="dummy")
	public String prepg(){
		//改ページ処理実行
		setPage(-1);		
		
		//F5対策
		return "/pc/search/view?redirect=true";
	}
	
	//検索実行と結果の編集メソッド
	private void execSearch(int offset){
		//自分と同じグループのメンバーを取得する
		List<String> gMidList = searchService.makeGroup(userInfoDto.memberId);
		
		String searchWord = searchService.makeSql(searchForm,userInfoDto.memberId);
		results = searchService.selAllList(searchWord,userInfoDto.memberId,searchForm.mydtonly,gMidList,offset);
		
		//F Shoutメンバー名置換処理
		for(int i=0;i<results.size();i++){
			String originalComment = (String)results.get(i).get("comment");
			String replaceComment;
			
			if(results.get(i).get("entrytype").toString().equals("S")){
				//F Shoutの場合はタグをメンバー名に置換する
				replaceComment = replaceFSComment(originalComment);
			}else{
				//その他の場合は、本文をそのまま代入する
				replaceComment = originalComment;
			}
			
			//全ての本文を別キーとしてmapに挿入する
			results.get(i).put("replaceComment", replaceComment);
			
		}
		
		// 削除対象のタグリスト(改行含)
		String[] deltags = {
				"\\n",
				"<span class=(\"|\'|)(large|medium|small)(\"|\'|)>",
				"</span>",
				"<span style=(\"|\'|)color:#([a-zA-Z0-9]+)(\"|\'|)>",
				"<strong>",
				"</strong>",
				"<em>",
				"</em>",
				"<u>",
				"</u>",
				"<del>",
				"</del>",
				"<a ([^>]+)>",
				"</a>",
				"\\[y:([^\"^\\]]+)\\]",
				"\\[gm:([^\"^\\]]+)\\]",
				"\\[p:([0-9]+:[0-9]+:[0-9]+)\\]",
				"<blockquote>",
				"</blockquote>"
			};		
		
		//装飾タグの削除
		CmnUtility.editcmnt(results, "replaceComment", "pic1", "pic2","pic3","picnote1","picnote2","picnote3",appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,null,deltags);
		
		//一覧件数取得
		resultscnt = searchService.cntAllList(searchWord,userInfoDto.memberId,searchForm.mydtonly,gMidList);
	}
	
	//初期処理メソッド
	private void initSearchAction(){
		//メニュー出しわけ用変数設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		//全角スペースを半角にする。前後のスペースを削除する。
		searchForm.searchtext = searchForm.searchtext.replaceAll("　", " ").trim();
	}
	
	//検索結果一覧改ページ処理
	private void setPage(int num){
		try {
			// ページ遷移用の計算
			searchForm.pgcnt = searchForm.pgcnt + num;
			searchForm.offset = searchForm.pgcnt * appDefDto.FP_MY_SEARCHLIST_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			searchForm.pgcnt = 1;
			searchForm.offset = 0;
		}		
	}
	
	//FShoutコメント文字列置換
	private String replaceFSComment(String txt){
		
		String viewFShout = "@xxxx ";

		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\[@:(\\S+)\\]");
		//前半
		Pattern p2 = Pattern.compile("(\\S+)\\,");
		//後半
		Pattern p3 = Pattern.compile("\\,(\\S+)");

		//正規表現実行
		Matcher m = p.matcher(txt);
		

		// 検索(find)し、マッチする部分文字列がある限り繰り返す
		while(m.find()){
			//部分文字列取得
			String partStr = m.group(1);

			//["frontierdomain","短縮されたmid"]より、それぞれを取り出す
			Matcher m2 = p2.matcher(partStr);
			Matcher m3 = p3.matcher(partStr);
			
			//Frontierdomain取得
			String partStr2 = "";
			String tagFdomain = "";
			while(m2.find()){
				//test
				partStr2 = m2.group(1);
				tagFdomain = partStr2;
			}

			//メンバーID取得
			String partStr3 = "";
			String tagMid = "";
			while(m3.find()){
				//test
				partStr3 = m3.group(1);
				tagMid = partStr3;
			}
			
			//Profileデータ取得
			List<BeanMap> profileList = topService.selProfileFShout(tagFdomain,tagMid);

			String getNickName = partStr;
			//String getMid = "";
			//String getFrontierDomain = "";
			//String myFrontierDomain = frontierUserManagement.frontierdomain;
			
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			if(profileList.size() == 1){
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					getNickName = profileList.get(0).get("nickname").toString();
					//getMid = profileList.get(0).get("mid").toString();
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					getNickName = profileList.get(0).get("fnickname").toString();
					//getMid = profileList.get(0).get("fid").toString();
					//getFrontierDomain = profileList.get(0).get("frontierdomain").toString();
				}
				//getNickName = getNickName.replace("\\","\\\\");
				
				//FShoutの可変変数を置換
				FShout = viewFShout.replaceAll("xxxx", getNickName);
				FShout = FShout.replaceAll(" ", "");
				/*
				//final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+( )");
				final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
				//「\」対応
				FShout = FShout.replace("\\","\\\\\\\\");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				*/
				/*
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					FShout = matcher.replaceAll("<a href=\"/frontier/pc/fshout/list/"+getMid+"/1\" title=\"$0\">$0</a>");
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					if(myFrontierDomain.equals(getFrontierDomain)){
						FShout = matcher.replaceAll("<a href=\"http://"+myFrontierDomain+"/frontier/pc/fshout/list/"+getMid+"/1\" title=\"$0\">$0</a>");
					} else {
						FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/pc/openid/auth?cid="+getMid+"&gm=mv&openid="+myFrontierDomain+"/frontier/pc/openidserver\" title=\"$0\">$0</a>");
					}
				}
				*/
					
				//FShout = FShout.replace("\\","\\\\");
				//文字列連結
				m.appendReplacement(sb, FShout);

			}
		}

		//残りの文字列連結
		m.appendTail(sb);

		return sb.toString();

	}
}
