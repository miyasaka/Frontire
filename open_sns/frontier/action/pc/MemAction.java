package frontier.action.pc;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.FrontierUserManagement;
import frontier.form.pc.MemForm;
import frontier.service.ClistService;
import frontier.service.CommonService;
import frontier.service.DiaryService;
import frontier.service.FriendListService;
import frontier.service.MemService;
import frontier.service.MembersService;
import frontier.service.MlistService;
import frontier.service.FshoutService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class MemAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	protected FriendListService friendListService;
	@Resource
	protected ClistService clistService;
	@Resource
	protected MembersService membersService;
	@Resource
	protected MemService memService;
	@Resource
	protected FshoutService fshoutService;
	@Resource
	protected DiaryService diaryService;
	@Resource
	protected MlistService mlistService;
	@Resource
	protected CommonService commonService;
	@ActionForm
	@Resource
	protected MemForm memForm;

	//変数定義
	public AppDefDto appDefDto;
	public UserInfoDto userInfoDto;
	public String cid;
	public Integer resultscnt;
	public Integer memCnt;
	public Integer followCnt;
	public Integer followerCnt;
	public List<BeanMap> MyFriendItems;
	public List<BeanMap> CommunityList;
	public List<String> friendList;
	public List<BeanMap> GetGetGroupItems;
	public boolean followFlg = false;
	public String fMemType;
	public List<String> getfdomainList;
	public List<String> getgidList;
	public List<BeanMap> GetGroupItems;
	public List<BeanMap> FollowmeList;
	public List<BeanMap> FollowyouList;
	public List<BeanMap> GroupList;
	public List<Object> GMList;
	public List<BeanMap> FNetList;
	public List<BeanMap> FSList;
	public List<BeanMap> FShoutList;

	//最近の〇〇
	public List<BeanMap> RecentDiary;
	//    public List<BeanMap> RecentMovie;
	public List<BeanMap> RecentAlbum;
	//権限リスト
	List<String> diaryAuthList;
	//内部制御用
	private List<String> pubLevelList;
	private List<String> appStatusList;
	//趣味リスト
	List<BeanMap> hobbyList;
	public BeanMap FriendInfo;
	public BeanMap MyFriendInfo;
	public Integer FriendStatus = 1;
	public BeanMap profileList;

	//今日の日付
	public String today = CmnUtility.getToday("yyyyMMddHHmmss");
	//ニックネーム
	public String vNickname;
	//プロフィール欄末尾チェック用ステータス
	public String endPos = "1";

	public FrontierUserManagement frontierUserManagement;

	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		//パラメータに自分のIDが渡された場合
		if(memForm.cid==userInfoDto.memberId||memForm.cid.equals(userInfoDto.memberId)){
			return "/pc/top/?redirect=true";
		}else{
			// メンバー情報取得
			FriendInfo=friendListService.selectFriendInfo(memForm.cid);

			if(FriendInfo!=null){
				//Frontier判断処理
				exefmem();
				if(fMemType.equals("2")){
					return "error.jsp";
				}

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
				//プロフィール欄末尾チェック
				if(FriendInfo.get("favgenre3")!=null){
					endPos = "4";
				}else if(FriendInfo.get("favgenre2")!=null){
					endPos = "3";
				}else if(FriendInfo.get("favgenre1")!=null){
					endPos = "2";
				}
				//セッションに変数を格納
				userInfoDto.visitMemberId=memForm.cid;
				vNickname = FriendInfo.get("nickname").toString();

				// ﾌｫﾛｰしている人数取得
				followCnt = (Integer) FriendInfo.get("follownumber");

				//Frontierユーザ管理情報を取得する
				frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);

				//マイとの関係をチェック
				checkRelation();

				// FShoutデータ取得
				FShoutList = fshoutService.selMemFShoutList("0",memForm.cid,0,appDefDto.FP_MEM_FSHOUT_PGMAX);
				
				//自Frontierのみ実行
				if(fMemType.equals("0")){
					//同Frontier
					// 参加コミュニティ件数取得
					resultscnt = clistService.cntClist(memForm.cid,"0");
					// 参加コミュニティ一覧データ取得
					CommunityList = clistService.selectClist(memForm.cid,"0",0,appDefDto.FP_MEM_TOPLIST_PGMAX);
					//最近の○○データ取得（アルバム）
					RecentAlbum = memService.selAlbumList(memForm.cid,diaryAuthList);
					//最近の〇〇データ取得（メンバー日記）
					RecentDiary = memService.selDiaryList(memForm.cid,diaryAuthList);
				} else {
					//他Frontier
					pubLevelList = new ArrayList<String>();
					appStatusList = new ArrayList<String>();
					pubLevelList.add(0,"0");
					pubLevelList.add(1,"1");
					appStatusList.add(0,"4");
					appStatusList.add(1,"2");

					//最近の〇〇データ取得（メンバー日記）
					RecentDiary = memService.selDiaryListOther(
						memForm.cid,
						null,
						null,
						0,
						diaryAuthList,
						userInfoDto.membertype,
						pubLevelList,
						appStatusList,
						appDefDto.FP_MY_DIARYLIST_PGMAX
					);
				}

				//フォローしている人を確認時の更新
				memService.setFollowConfirm(userInfoDto.memberId,userInfoDto.visitMemberId);
				// 私をフォローリスト取得
				FollowmeList = commonService.getMidListTop("3", memForm.cid,appDefDto.FP_MEM_TOPLIST_PGMAX);
				// 私がフォローリスト取得
				FollowyouList = commonService.getMidListTop("2", memForm.cid,appDefDto.FP_MEM_TOPLIST_PGMAX);
				// グループリスト取得(全件)
				GroupList = commonService.getMidList("4", memForm.cid);
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
				if(userInfoDto.membertype.equals("0")){
					return "/pc/top/?redirect=true";
				}else{
					return "/pc/ftop/?redirect=true";
				}
			}
		}
		return "home.jsp";
	}

	//フォロー解除
	@Execute(validator=false)
	public String release(){

		//Profileデータ取得（フォローした数取得）
		profileList = memService.selProfile(userInfoDto.memberId);
		Integer myfollowCnt = (Integer) profileList.get("follownumber");
		//友人情報取得（フォローされた数取得）
		FriendInfo=friendListService.selectFriendInfo(memForm.frommid);
		Integer memfollowerCnt = (Integer) FriendInfo.get("followernumber");

		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);

		//グループ状態チェック
		checkRelation();
		if(!FriendStatus.equals(3)){
			//フォロー解除
			memService.setFollowState("1",userInfoDto.memberId,memForm.frommid,myfollowCnt,memfollowerCnt);
		}
		return "view";
	}

	//フォローする
	@Execute(validator=false)
	public String follow(){

		//Profileデータ取得（フォローした数取得）
		profileList = memService.selProfile(userInfoDto.memberId);
		Integer myfollowCnt = (Integer) profileList.get("follownumber");
		//友人情報取得（フォローされた数取得）
		FriendInfo=friendListService.selectFriendInfo(memForm.frommid);
		Integer memfollowerCnt = (Integer) FriendInfo.get("followernumber");

		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);

		//グループ状態チェック
		checkRelation();
		if(!FriendStatus.equals(3)){
			//フォローする
			memService.setFollowState("0",userInfoDto.memberId,memForm.frommid,myfollowCnt,memfollowerCnt);
		}
		return "view";
	}

	//画面再読み込み
	@Execute(validator=false)
	public String view(){
		return "/pc/mem/"+memForm.frommid+"?redirect=true";
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

	// 関係チェック
	private void checkRelation(){
		//変数初期化
		List<BeanMap> GroupMemList = new ArrayList<BeanMap>();
		List<BeanMap> FollowerList = new ArrayList<BeanMap>();
		diaryAuthList = new ArrayList<String>();
		MyFriendInfo = new BeanMap();

		//グループメンバ一覧データ取得
		GroupMemList = commonService.getMidList("1",userInfoDto.memberId);
		//権限ﾃﾞﾌｫﾙﾄ値設定
		diaryAuthList.add(0, appDefDto.FP_CMN_DIARY_AUTH1[0]);//全体に公開

		//現在表示されているユーザーとグループであるか
		for(BeanMap beanMap:GroupMemList){
			//グループである場合
			if(userInfoDto.visitMemberId.equals(beanMap.get("mid"))){
				//権限追加
				FriendStatus = 3;
				diaryAuthList.add(1, appDefDto.FP_CMN_DIARY_AUTH3[0]);//グループに公開
				break;
			}
		}
		//フォローされているリスト取得
		FollowerList = commonService.getMidList("3",userInfoDto.visitMemberId);

		//フォローされている人数
		followerCnt = (Integer) FriendInfo.get("followernumber");

		//現在表示されているユーザーをﾌｫﾛｰしているか
		for(BeanMap b:FollowerList){
			//フォローしている場合
			if(userInfoDto.memberId.equals(b.get("mid"))&&frontierUserManagement.frontierdomain.equals(b.get("frontierdomain"))){
				followFlg = true;
			}
		}
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

	//自Frontier他Frontier判断
	@Execute(validator=false)
	private void exefmem(){

		//セッションに変数を格納
		userInfoDto.visitMemberType = FriendInfo.get("membertype").toString();
		fMemType = "";
		if(userInfoDto.membertype.equals("0") && userInfoDto.visitMemberType.equals("0")){
			//同Frontier
			fMemType = "0";
		}else if(userInfoDto.membertype.equals("1") && userInfoDto.visitMemberType.equals("0")){
			//他Frontier
			fMemType = "1";
		}else{
			fMemType = "2";
		}
	}
}