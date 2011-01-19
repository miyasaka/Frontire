package frontier.action.pc;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.service.ClistService;
import frontier.service.FriendListService;
import frontier.service.FshoutService;
import frontier.service.MemService;
import frontier.service.CommonService;
import frontier.service.MlistService;

public class CheckAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected FriendListService friendListService;
	@Resource
	protected ClistService clistService;
	@Resource
	protected MemService memService;
	@Resource
	protected CommonService commonService;
	@Resource
	protected MlistService mlistService;
	@Resource
	protected FshoutService fshoutService;

	//変数定義
	public AppDefDto appDefDto;
	public UserInfoDto userInfoDto;
	public String cid;
	public Integer resultscnt;
	public List<BeanMap> MyFriendItems;
	public List<BeanMap> CommunityList;
	public List<BeanMap> FollowmeList;
	public List<BeanMap> FollowyouList;
	public List<BeanMap> FShoutList;
	public List<BeanMap> GroupList;
	public List<Object> GMList;
	public List<BeanMap> FNetList;
	//最近の〇〇
	public List<BeanMap> RecentDiary;
	public List<BeanMap> RecentAlbum;
	//権限リスト
	List<String> diaryAuthList;
	//趣味リスト
	List<BeanMap> hobbyList;
	public BeanMap FriendInfo;
	//今日の日付
	public String today = CmnUtility.getToday("yyyyMMddHHmmss");
	//ホスト名取得用変数
	public HttpServletRequest request;
	public String ServerName;
	//ユーザー名
	public String vNickname;
	//プロフィール欄末尾チェック用ステータス
	public String endPos = "1";
	//フォローしてる数
	public Integer followyou;
	//フォローされてる数
	public String followme;
	//FShout No(削除用)
	public Integer fsno;
	
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// 日記、フォトアルバムで表示する権限をセット(全体に公開のみ)
		diaryAuthList = new ArrayList<String>();
		diaryAuthList.add(0, "1");

		//パラメータに自分以外のIDが渡された場合
		if(cid!=userInfoDto.memberId&&!cid.equals(userInfoDto.memberId)){
			return "/pc/mem/"+cid+"?redirect=true";
		}else{
			// メンバー情報取得
			FriendInfo=friendListService.selectFriendInfo(cid);
			if(FriendInfo!=null){
				//セッションに変数を格納
				userInfoDto.visitMemberId=cid;
				//ニックネーム取得
				vNickname = FriendInfo.get("nickname").toString();
				//ホスト名取得
				ServerName =   request.getServerName();
				//年齢表示変換処理
				if(FriendInfo.get("age")!=null){
					String age = FriendInfo.get("age").toString();
					FriendInfo.put("age",age.substring(0,age.indexOf("year")-1)+"歳");
				}
				//趣味リスト変換処理
				if(FriendInfo.get("interest1")!=null){
					FriendInfo.put("interest1", getHobbyList());
				}
				//自己紹介変換処理
				resetResults(FriendInfo,"aboutme");
				//公開権限チェック
				checkPubLevel(FriendInfo.get("pubName"),"pname");
				checkPubLevel(FriendInfo.get("pubGender"),"pgender");
				checkPubLevel(FriendInfo.get("pubResidence"),"presidence");
				checkPubLevel(FriendInfo.get("pubYearofbirth"),"pyearofbirth");
				checkPubLevel(FriendInfo.get("pubDateofbirth"),"pdateofbirth");
				checkPubLevel(FriendInfo.get("pubHometown"),"phometown");
				checkPubLevel(FriendInfo.get("pubOccupation"),"poccupation");
				checkPubLevel(FriendInfo.get("pubDiary"),"pdiary");

				//プロフィール欄末尾チェック
				if(FriendInfo.get("favgenre3")!=null){
					endPos = "4";
				}else if(FriendInfo.get("favgenre2")!=null){
					endPos = "3";
				}else if(FriendInfo.get("favgenre1")!=null){
					endPos = "2";
				}
				//フォローしてる数
				followyou = (Integer) FriendInfo.get("follownumber");
				//フォローされてる数
				followme = FriendInfo.get("followernumber").toString();

				// 参加コミュニティ件数取得
				resultscnt = clistService.cntClist(cid,"0");
				// 参加コミュニティ一覧データ取得
				CommunityList = clistService.selectClist(cid,"0",0,appDefDto.FP_MEM_TOPLIST_PGMAX);
				// 私をフォローリスト取得
				FollowmeList = commonService.getMidListTop("3", cid,appDefDto.FP_MEM_TOPLIST_PGMAX);
				// 私がフォローリスト取得
				FollowyouList = commonService.getMidListTop("2", cid,appDefDto.FP_MEM_TOPLIST_PGMAX);
				// F Shoutリスト取得
				FShoutList = fshoutService.selMemFShoutList("0",cid,0,appDefDto.FP_MEM_FSHOUT_PGMAX);
				// グループリスト取得(全件)
				GroupList = commonService.getMidList("4", cid);
				// 最近の〇〇データ取得
				RecentDiary = memService.selDiaryList(cid,diaryAuthList);
				RecentAlbum = memService.selAlbumList(cid,diaryAuthList);

				// グループリストの編集(グループ毎にメンバーを配列で追加)
				if(GroupList.size() > 0){
					// リストの初期化
					GMList = new ArrayList<Object>();
					List<Object> addObject;
					for(BeanMap f:GroupList){
						List<BeanMap> Mlist;
						addObject = new ArrayList<Object>();
						Mlist = mlistService.selGroupMemList(f.get("gid").toString(),f.get("frontierdomain").toString(),0,appDefDto.FP_MEM_TOPLIST_PGMAX);
						// addObject配列にグループ情報と、グループに所属してるメンバー情報を追加
						addObject.add(0,f);
						addObject.add(1,Mlist);
						// リストに追加
						GMList.add(addObject);
					}
				}
			}else{
				//存在しないユーザーID
				return "/pc/top/?redirect=true";
			}
		}
		return "check.jsp";
	}

	private String getHobbyList(){
		ArrayList<String> list=new ArrayList<String>();
		String[] st =  FriendInfo.get("interest1").toString().split(",");
		for(int i=0;i<st.length;i++){
			list.add(st[i]);
		}
		String hobby = null;
		//趣味リスト取得
		hobbyList = friendListService.selectHobby(list);
		//文字列を,区切りで結合
		for(int i=0;i<hobbyList.size();i++){
			if(i==0){
				hobby = hobbyList.get(i).get("itemname").toString();
			}else{
				hobby +=","+hobbyList.get(i).get("itemname").toString();
			}
		}
		return hobby;

	}

	//本文装飾
	private void resetResults(BeanMap lbm,String property){
		//リクエストメッセージを取得
		String _item = (String)lbm.get(property);
		if(_item!=null){
			//サニタイジング
			_item = CmnUtility.htmlSanitizing(_item);
			//絵文字装飾
			String item = CmnUtility.replaceEmoji(_item,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
			//BeanMapへ格納
			lbm.put(property, item);
		}
	}

	//公開権限チェック
	private void checkPubLevel(Object publevel,String setName){
		if(publevel.equals("1")){
			FriendInfo.put(setName, "全体に公開");
		}else if(publevel.equals("2")){
			FriendInfo.put(setName, "グループに公開");
		}else{
			FriendInfo.put(setName, "非公開");
		}
	}
	
	// F Shout 削除リンク押下時の処理
	@Execute(validator=false)
	public String delfshout(){
		// コメント削除
		try {
			fshoutService.delFSComment(userInfoDto.memberId,fsno);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "/pc/check/" + userInfoDto.memberId + "/?redirect=true";
	}
}