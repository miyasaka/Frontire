package frontier.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import twitter4j.TwitterException;
import frontier.entity.Frontiershout;
import frontier.common.CmnUtility;
import frontier.common.DecorationUtility;
import frontier.common.EmojiUtility;
import frontier.service.TopService;
import frontier.service.OauthConsumerService;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FshoutService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	public JdbcManager jdbcManager;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected CommonService commonService;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	@Resource
	protected TopService topService;
	@Resource
	protected MembersService membersService;
	public BeanMap selMyPhoto(String mid){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyPhoto.sql",mid)
		.getSingleResult();
	}
	protected List<BeanMap> results;
	public List<BeanMap> profileList;
	public Frontiershout fs;

	/**
	 * 文中のFrontierアカウント -> ニックネームへ変換
	 * @param getText 変換対象を含む文字列
	 * @return Frontierアカウント -> ニックネームへ変換した文字列
	 */
	public String changeIdToName(String getText){
		// 正規表現の適合調査結果の文字列格納用
		StringBuffer sb = new StringBuffer();

		// 型の定義(正規表現) [@:(Frontierドメイン),(メンバーID)]
		Pattern ptn = Pattern.compile("\\[@:(\\S+)\\,(\\S+)\\]");
		// マッチングクラス生成
		Matcher mt = ptn.matcher(getText);
		// 検索
		while(mt.find()){
			// Frontierのアカウント名変数
			String getFrontierName = "";
			// マッチした型のFrontierドメイン部分(xxxxxxx.ne.jp部分)
			String mtFDomain = mt.group(1);
			// マッチした型のメンバーID部分(mXXXXXXXXXの'm'なし0トルツメ)
			String mtFid = mt.group(2);
			// Frontierのアカウント名取得
			List<BeanMap> lbm = membersService.selProfileFShout(mtFDomain,mtFid);
			if(lbm.size() == 0){
				// 取得できなければそのままの値を設定
				getFrontierName = mt.group(0);
			} else {
				//Frontierドメインのチェック
				if(mtFDomain.equals(appDefDto.FP_CMN_HOST_NAME)){
					//自分のFrontierのユーザ
					getFrontierName = "@"+lbm.get(0).get("nickname").toString();
				}else{
					//他Frontierのユーザ
					getFrontierName = "@"+lbm.get(0).get("fnickname").toString();
				}
			}
			// Frontierのアカウント名の置換を行う
			mt.appendReplacement(sb, getFrontierName);
		}
		// 正規表現の適合調査を行っていない残りの文字シーケンスを引数に指定した文字列バッファに追加
		// ※appendReplacementメソッドと一緒に利用
		mt.appendTail(sb);
		return sb.toString();
		
	}
	
	
	/**
	 * F shoutを登録する。
     * @param mid　登録するユーザのメンバーID
     * @param fscomment 登録する本文
     * @param fscheck 登録するtwitter投稿フラグ
     * @param fskoukaihani 登録する公開レベル
     * @param fsconfirm 登録する確認要求フラグ
	 * @param replyid reply元のID（F ShoutはNO,TwitterはステータスID)。必要ない場合はNULLを指定。
	 * @param replymid reply元のメンバーID(F Shoutの場合のみ設定)。必要ない場合はNULLを指定。
	 * @param statusid Twitter投稿時のステータスID。必要ない場合はNULLを指定。
	 */
	public void insFSComment(String mid,String fscomment,Integer fscheck,Integer fskoukaihani,Integer fsconfirm,Object replyid,String replymid,Object statusid) {
		//Twitterにも登録チェックボックス
		Integer fscheckVal;
		if(fscheck==2){
			fscheckVal = 1;
		}else{
			fscheckVal = 0;
		}

		//F Shoutの登録
		Frontiershout fshout = new Frontiershout();
		fshout.mid = mid;
		//登録するシーケンスNoを取得して設定
		fshout.no = getMaxFshoutNo(mid);
		//改行コードは削除して設定
		fshout.comment = fscomment.replaceAll("\n","");
		fshout.twitter = fscheckVal;
		fshout.entid = mid;
		fshout.updid = mid;
		fshout.demandflg = fsconfirm;
		fshout.pub_level = fskoukaihani;
		if(replyid != null){
			fshout.replyid = Long.parseLong(replyid.toString());			
		}else{
			fshout.replyid = null;			
		}
		fshout.replymid = replymid;
		if(statusid != null){
			fshout.statusid = Long.parseLong(statusid.toString());			
		}else{
			fshout.statusid = null;			
		}
		
		jdbcManager.updateBySqlFile("data/insFShoutComment.sql", fshout)
		.execute();
	}	
	
	/**
     * F shoutを登録する。
     * @deprecated
     * @param mid　登録するユーザのメンバーID
     * @param fscomment 登録する本文
     * @param fscheck 登録するtwitter投稿フラグ
     * @param fskoukaihani 登録する公開レベル
     * @param fsconfirm 登録する確認要求フラグ
     * @return なし
     */
	public void insFSComment(String mid,String fscomment,Integer fscheck,Integer fskoukaihani,Integer fsconfirm) {
		//Twitterにも登録チェックボックス
		Integer fscheckVal;
		if(fscheck==2){
			fscheckVal = 1;
		}else{
			fscheckVal = 0;
		}

		//F Shoutの登録
		Frontiershout fshout = new Frontiershout();
		fshout.mid = mid;
		//登録するシーケンスNoを取得して設定
		fshout.no = getMaxFshoutNo(mid);
		//改行コードは削除して設定
		fshout.comment = fscomment.replaceAll("\n","");
		fshout.twitter = fscheckVal;
		fshout.entid = mid;
		fshout.updid = mid;
		fshout.demandflg = fsconfirm;
		fshout.pub_level = fskoukaihani;

		jdbcManager.updateBySqlFile("/data/insFShoutComment", fshout)
		.execute();
	}

	/**
     * F shoutの最大No+1を取得する。
     * @param mid　[検索キー]投稿を行ったユーザのメンバーID
     * @return 検索条件に該当するF Shoutの最大No+1
     */
	private Integer getMaxFshoutNo(String mid){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mid", mid);

		//検索実行
		List<BeanMap> lb = jdbcManager
					.selectBySqlFile(BeanMap.class, "data/selMaxFShoutComment",params)
					.getResultList();
		
		//実行結果よりF Shoutの最大No+1を取得
		return (Integer)lb.get(0).get("comno");
	}
	
	//FShoutコメント文字列置換(他からも参照されるためpublic)
	/**
	 * FShout中の[@:ドメイン名：メンバーID]をニックネームに置換する処理
	 * パラメータによってリンクの場合とそうでない場合を切り替える
	 * @param type 0 or 2:PC用、1 or 3：携帯用
	 * @param txt 置換対象の文字列
	 * @param makeType 0：リンクなし、1：リンクあり
	 * @return FShout中の[@:ドメイン名：メンバーID]をニックネーム又はリンク付きニックネームに置換した文字列
	 */
	public String replaceFSCommentNew(String type,String txt,String makeType){
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
			profileList = topService.selProfileFShout(tagFdomain,tagMid);
			String getNickName = partStr;
			String getMid = "";
			String getFrontierDomain = "";
			String getMtype = "";
			String myFrontierDomain = userInfoDto.fdomain;
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			if(profileList.size() == 1){
				// 相手のFrontierドメインを取得
				getFrontierDomain = profileList.get(0).get("frontierdomain").toString();
				// 相手のメンバータイプを取得
				getMtype = profileList.get(0).get("membertype").toString();

				if(getMtype.equals("0")){
					// メンバータイプが0:自Frontierの場合
					//自Frontier
					getNickName = profileList.get(0).get("nickname").toString();
					getMid = profileList.get(0).get("mid").toString();
				}else if(getMtype.equals("1")){
					// メンバータイプが1:他Frontierの場合
					//他Frontier
					getNickName = profileList.get(0).get("fnickname").toString();
					getMid = profileList.get(0).get("fid").toString();
				}
				getNickName = getNickName.replace("\\","\\\\");
				getNickName = CmnUtility.htmlSanitizing(getNickName);
				//FShoutの可変変数を置換
				FShout = viewFShout.replaceAll("xxxx", getNickName);
				FShout = FShout.replaceAll(" ", "");
				//「\」対応
				FShout = FShout.replace("\\","\\\\\\\\");
				
				final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
				Matcher matcher = convURLLinkPtn.matcher(FShout);

				if(makeType.equals("0")){
					//リンクにする必要がない場合
					FShout = matcher.replaceAll("$0");
				}else if(getMtype.equals("0")){
					// -------------------------------------- //
					// メンバータイプが0:自Frontierの場合     //
					// -------------------------------------- //
					// リンク先の設定 type 0,2:PC、1,3:携帯
					if(type.equals("0") || type.equals("2")){
						// PC
						FShout = matcher.replaceAll("<a href=\"/frontier/pc/fshout/list/"+getMid+"/1\">$0</a>");
					} else {
						// 携帯
						FShout = matcher.replaceAll("<a href=\"/frontier/m/fshout/"+getMid+"\">$0</a>");
					}
				}else if(getMtype.equals("1")){
					// -------------------------------------- //
					// メンバータイプが1:他Frontierの場合     //
					// -------------------------------------- //
					if(myFrontierDomain.equals(getFrontierDomain)){
						// ------------------------------------------ //
						// 自分のFrontierDomainと同じドメインの場合   //
						// ------------------------------------------ //
						// リンク先の設定 type 0,2:PC、1,3:携帯
						if(type.equals("0") || type.equals("2")){
							// PC
							FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/pc/fshout/list/"+getMid+"/1\">$0</a>");
						} else {
							// 携帯
							FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/m/fshout/"+getMid+"\">$0</a>");
						}
					} else {
						// リンク先の設定 type 0,2:PC、1,3:携帯
						if(type.equals("0") || type.equals("2")){
							// PC
							FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/pc/openid/auth?cid="+getMid+"&gm=mv&openid="+myFrontierDomain+"/frontier/pc/openidserver\" title=\"$0\">$0</a>");
						} else {
							// 携帯(携帯の場合はリンクなし)
							FShout = matcher.replaceAll("$0");
						}
					}
				}
				//文字列連結
				m.appendReplacement(sb, FShout);
			}
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}
	
	//プロフィールデータ取得
	public List<BeanMap> selProfile(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		return jdbcManager
			.selectBySqlFile(BeanMap.class,"/data/selprofile.sql",params)
			.getResultList();
	}

	// 内容更新
	public void delFSComment(
		String mid,
		Integer no
	) throws IOException, Exception{
		/* ■ FShoutコメント削除 */
		fs       = new Frontiershout();
		fs.mid   = mid;
		fs.updid = mid;
		fs.no    = no;
		jdbcManager
		.updateBySqlFile("/data/delFShoutComment.sql", fs)
		.execute();
	}

	
	// FShout一覧取得（メンバー）
	public List<BeanMap> selFShoutListMem(String mid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("limit",limit);
		params.put("offset",offset);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFShoutListMem.sql",params).getResultList();
	}

	// ■メンバーのFShout一覧取得(共通処理として他でも使用)
	//   PC、携帯使い分け・文字装飾込版(type: 0:PC,1:携帯)
	public List<BeanMap> selMemFShoutList(String type,String mid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("publevel",getFShoutPublevel(mid));
		params.put("limit",limit);
		params.put("offset",offset);
		List<BeanMap> lbm = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMemFShoutList.sql",params).getResultList();
		// PC、携帯によって装飾タイプを変える 0:PC、1:携帯
		decorationFSComment(type,lbm);
		return lbm;
	}


	// ■FShout一覧 TOP、自分宛一覧取得(もっと見る対応)
	//   一覧 or 自分宛、PC or 携帯、使い分け・文字装飾込版
	// ------------------------- //
	//     type:                 //
	// ------------------------- //
	//       0:一覧(PC)          //
	//       1:一覧(携帯)        //
	//       2:自分宛(PC)        //
	//       3:自分宛(携帯)      //
	// ------------------------- //
	public List<BeanMap> selMyFShoutTopList(String type,String mid,String period,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("gmidlist",getGList(mid));
		params.put("fmidlist",getFList(mid));
		params.put("limit",limit);
		params.put("offset",offset);
		// キーワードフラグ(0:なし、1:あり)
		String kwdflg = "0";
		// 期間フラグ(0:なし、1:あり)※ここでは期間縛りあり
		String periodflg = "1";
		// 自分宛(2 or 3)の場合、キーワードの追加
		if(type.equals("2") || type.equals("3")){
			kwdflg = "1";
			String kwd = "";
			// 検索条件作成 [@:frontierドメイン,メンバーID]
			try{
				kwd = "[@:" + appDefDto.FP_CMN_HOST_NAME + "," + Integer.valueOf(mid.substring(1)).intValue() + "]";
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 追加
			params.put("kwd",kwd);
		}
		// 追加
		params.put("kwdflg",kwdflg);
		params.put("periodflg",periodflg);
		params.put("period",period);
		List<BeanMap> lbm = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyFShoutList.sql",params).getResultList();
		// PC、携帯によって装飾タイプを変える 0:PC、1:携帯
		decorationFSComment(type,lbm);
		return lbm;
	}

	// ■FShout一覧、自分宛一覧取得(共通処理として他でも使用)
	//   一覧 or 自分宛、PC or 携帯、使い分け・文字装飾込版
	// ------------------------- //
	//     type:                 //
	// ------------------------- //
	//       0:一覧(PC)          //
	//       1:一覧(携帯)        //
	//       2:自分宛(PC)        //
	//       3:自分宛(携帯)      //
	// ------------------------- //
	public List<BeanMap> selMyFShoutList(String type,String mid,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("gmidlist",getGList(mid));
		params.put("fmidlist",getFList(mid));
		params.put("limit",limit);
		params.put("offset",offset);
		// キーワードフラグ(0:なし、1:あり)
		String kwdflg = "0";
		// 期間フラグ(0:なし、1:あり)※ここでは期間縛りなし
		String periodflg = "0";
		// 自分宛(2 or 3)の場合、キーワードの追加
		if(type.equals("2") || type.equals("3")){
			kwdflg = "1";
			String kwd = "";
			// 検索条件作成 [@:frontierドメイン,メンバーID]
			try{
				kwd = "[@:" + appDefDto.FP_CMN_HOST_NAME + "," + Integer.valueOf(mid.substring(1)).intValue() + "]";
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 追加
			params.put("kwd",kwd);
		}
		// 追加
		params.put("kwdflg",kwdflg);
		params.put("periodflg",periodflg);
		List<BeanMap> lbm = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMyFShoutList.sql",params).getResultList();
		// PC、携帯によって装飾タイプを変える 0:PC、1:携帯
		decorationFSComment(type,lbm);
		return lbm;
	}

	// FShout件数取得（メンバー）
	public Integer cntBbsListMem(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selFShoutListMem.sql",params)
		.getResultList();
		return results.size();
	}

	// FShout件数取得(NEW)
	// ------------------------------------------------------------ //
	//   type (取得内容)                                            //
	// ------------------------------------------------------------ //
	//     0: 自分＋グループメンバー＋フォローメンバーのF Shout数   //
	//     1: 0の内容＋自分宛のF Shout数                            //
	//     2: １人のメンバーのF Shout数(中で関係を判別)             //
	//     3: 自分が確認すべきF Shout数                             //
	// ------------------------------------------------------------ //
	public Integer selFShoutCnt(String type,String mid){
		Map<String, Object> params = new HashMap<String, Object>();

		// Mapオブジェクトに条件用パラメタを定義
		params.put("type",type);
		params.put("mid",mid);

		if(type.equals("0") || type.equals("1") || type.equals("3")){
			// 0 or 1 の場合
			// グループメンバー一覧、フォローメンバー一覧パラメタを追加
			params.put("gmidlist",getGList(mid));
			params.put("fmidlist",getFList(mid));
			// 1:自分宛、3:自分が確認すべきF Shout取得の場合、検索条件の作成＆追加
			if(type.equals("1") || type.equals("3")){
				String kwd = "";
				// 検索条件作成 [@:frontierドメイン,メンバーID]
				try{
					kwd = "[@:" + appDefDto.FP_CMN_HOST_NAME + "," + Integer.valueOf(mid.substring(1)).intValue() + "]";
				} catch (Exception e) {
					// TODO: handle exception
				}
				// 追加
				params.put("kwd",kwd);
			}
		} else if(type.equals("2")) {
			// 2の場合
			// 公開権限パラメタの追加
			params.put("plevel",getFShoutPublevel(mid));
		}

		// FShout数取得
		List<BeanMap> rtnlbm = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFShoutCnt.sql",params).getResultList();
		Integer cntfs = 0;
		try{
			// 取得数を返す(型変換)
			cntfs = Integer.valueOf(rtnlbm.get(0).get("cnt").toString()).intValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cntfs;
	}

	//メンバー設定情報取得
	public Frontiershout FshoutInfo(String mid,Integer no){
		return jdbcManager.from(Frontiershout.class).id(mid,no).getSingleResult();
	}

	// FShout最新のコメント取得
	public List<BeanMap> selFShoutMyFS(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selFShoutMyFS.sql",params)
			.getResultList();
	}

	// FShoutコメント取得（1件だけ）
	public List<BeanMap> selFShoutOne(String mid,Integer fsno){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("fsno",fsno);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selFShoutOne.sql",params)
		.getResultList();
		return results;
	}

	//FShout一覧用、本文装飾用処理
	//FShoutコメント文装飾(PC,携帯)
	private void decorationFSComment(String type,List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String getComment = (String)lbm.get(i).get("comment");
			//セット
			String reComment = getComment;
			reComment = reComment.replace("\\\\","\\");
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//FShoutコメント文字列置換
			reComment = replaceFSComment(type,reComment);
			//FShoutコメント絵文字置換
			if(type.equals("0") || type.equals("2")){
				// PC版絵文字変換
				reComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);

				//JavaScript用本文(RT本文)
				String jsComment = CmnUtility.escJavaScript(getComment); // JavaScript対応(文字列置換)
				//RE用変数
				String memberTag = "";
				// ------------- //
				// RE、RT用処理  //
				// ------------- //
				//Fドメイン、Fid取得
				String getFdomain = (String)lbm.get(i).get("frontierdomain");
				String getFid     = (String)lbm.get(i).get("fid");
				//メンバータグ作成
				try{
					memberTag = "[@:" + getFdomain + "," + Integer.valueOf(getFid.substring(1)).intValue() + "]";
				} catch (Exception e) {
					// TODO: handle exception
				}
				//装飾 -> RT [@:frontierドメイン,メンバーID] 本文
				jsComment = "RT " + memberTag + " " + jsComment;
				// ------------- //
				// RE、RT用処理  //
				// ------------- //
				
				//JavaScript閲覧用本文
				String jsCommentView = replaceFSCommentNickName(getComment); // ニックネーム置換
				jsCommentView = CmnUtility.escJavaScript(jsCommentView); // JavaScript対応(文字列置換)

				//BeanMapへ格納(PC版のみ)
				lbm.get(i).put("memberTag", memberTag);
				lbm.get(i).put("jsComment", jsComment);
				lbm.get(i).put("jsCommentView", jsCommentView);
			} else {
				// 携帯版絵文字変換
				reComment = EmojiUtility.replacePcToMoblile(reComment,appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
			}
			//BeanMapへ格納
			lbm.get(i).put("viewComment", reComment);
		}
	}

	/**
	 * このメソッドは最終的に削除する。
	 * replaceFSCommentNew()を使う
	 */
	public String replaceFSCommentNickName(String txt){
		String viewFShout = "@xxxx";
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
				partStr2 = m2.group(1);
				tagFdomain = partStr2;
			}

			//メンバーID取得
			String partStr3 = "";
			String tagMid = "";
			while(m3.find()){
				partStr3 = m3.group(1);
				tagMid = partStr3;
			}

			//Profileデータ取得
			profileList = membersService.selProfileFShout(tagFdomain,tagMid);
			String getNickName = partStr;
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			if(profileList.size() == 1){
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					getNickName = profileList.get(0).get("nickname").toString();
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					getNickName = profileList.get(0).get("fnickname").toString();
				}
				getNickName = getNickName.replace("\\","\\\\");
				getNickName = CmnUtility.htmlSanitizing(getNickName);
				
				//FShoutの可変変数を置換
				FShout = viewFShout.replaceAll("xxxx", getNickName);
				FShout = FShout.replaceAll(" ", "");
				//「\」対応
				FShout = FShout.replace("\\","\\\\\\\\");
				
				final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				FShout = matcher.replaceAll("$0");
				//文字列連結
				m.appendReplacement(sb, FShout);
			}
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

	//FShoutコメント文字列置換(他からも参照されるためpublic)
	/**
	 * このメソッドは最終的に削除する。
	 * replaceFSCommentNew()を使う
	 */
	public String replaceFSComment(String type,String txt){
		String viewFShout = "@xxxx ";
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\[@:(\\S+)\\]");
		//前半
		Pattern p2 = Pattern.compile("(\\S+)\\,");
		//後半
		Pattern p3 = Pattern.compile("\\,(\\S+)");
		// URL置換処理
		txt = CmnUtility.convURL(txt);
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
			profileList = topService.selProfileFShout(tagFdomain,tagMid);
			String getNickName = partStr;
			String getMid = "";
			String getFrontierDomain = "";
			String getMtype = "";
			String myFrontierDomain = userInfoDto.fdomain;
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			if(profileList.size() == 1){
				// 相手のFrontierドメインを取得
				getFrontierDomain = profileList.get(0).get("frontierdomain").toString();
				// 相手のメンバータイプを取得
				getMtype = profileList.get(0).get("membertype").toString();

				if(getMtype.equals("0")){
					// メンバータイプが0:自Frontierの場合
					//自Frontier
					getNickName = profileList.get(0).get("nickname").toString();
					getMid = profileList.get(0).get("mid").toString();
				}else if(getMtype.equals("1")){
					// メンバータイプが1:他Frontierの場合
					//他Frontier
					getNickName = profileList.get(0).get("fnickname").toString();
					getMid = profileList.get(0).get("fid").toString();
				}
				getNickName = getNickName.replace("\\","\\\\");
				getNickName = CmnUtility.htmlSanitizing(getNickName);
				//FShoutの可変変数を置換
				FShout = viewFShout.replaceAll("xxxx", getNickName);
				FShout = FShout.replaceAll(" ", "");
				//final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+( )");
				final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
				//「\」対応
				FShout = FShout.replace("\\","\\\\\\\\");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				if(getMtype.equals("0")){
					// -------------------------------------- //
					// メンバータイプが0:自Frontierの場合     //
					// -------------------------------------- //
					// リンク先の設定 type 0,2:PC、1,3:携帯
					if(type.equals("0") || type.equals("2")){
						// PC
						FShout = matcher.replaceAll("<a href=\"/frontier/pc/fshout/list/"+getMid+"/1\">$0</a>");
					} else {
						// 携帯
						FShout = matcher.replaceAll("<a href=\"/frontier/m/fshout/"+getMid+"\">$0</a>");
					}
				}else if(getMtype.equals("1")){
					// -------------------------------------- //
					// メンバータイプが1:他Frontierの場合     //
					// -------------------------------------- //
					if(myFrontierDomain.equals(getFrontierDomain)){
						// ------------------------------------------ //
						// 自分のFrontierDomainと同じドメインの場合   //
						// ------------------------------------------ //
						// リンク先の設定 type 0,2:PC、1,3:携帯
						if(type.equals("0") || type.equals("2")){
							// PC
							FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/pc/fshout/list/"+getMid+"/1\">$0</a>");
						} else {
							// 携帯
							FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/m/fshout/"+getMid+"\">$0</a>");
						}
					} else {
						// リンク先の設定 type 0,2:PC、1,3:携帯
						if(type.equals("0") || type.equals("2")){
							// PC
							FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/pc/openid/auth?cid="+getMid+"&gm=mv&openid="+myFrontierDomain+"/frontier/pc/openidserver\" title=\"$0\">$0</a>");
						} else {
							// 携帯(携帯の場合はリンクなし)
							FShout = matcher.replaceAll("$0");
						}
					}
				}
				//FShout = FShout.replace("\\","\\\\");
				//文字列連結
				m.appendReplacement(sb, FShout);
			}
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

	// FShoutの確認状況チェック(conftype変数追加)
	// --------------------------- //
	// conftype(確認状況用変数)    //
	// --------------------------- //
	//    0:何もなし               //
	//    1:未確認  (自分->相手)   //
	//    2:確認済み(自分->相手)   //
	//    3:未確認  (相手->自分)   //
	//    4:確認済み(相手->自分)   //
	// --------------------------- //
	public void chkconfirmFSComment(List<BeanMap> lbm){
		// データ数分ループ
		for (int i=0;i<lbm.size();i++){
			String conftype = "0";
			//データから取得
			String getcomment    = (String)lbm.get(i).get("comment");
			String getdemandflg  = (String)lbm.get(i).get("demandflg");
			String getconfirmflg = (String)lbm.get(i).get("confirmflg");
			String getmid        = (String)lbm.get(i).get("mid");
			String chkfstagflg   = "0";
			String getprofilemid = "";

			// 確認要求フラグが１の時は処理実行(それ以外は処理をスルー)
			if(getdemandflg.equals("1")){
				// ========================================== //
				// 内容の先頭にメンバータグがあるか調べる     //
				// ========================================== //
				Pattern p = Pattern.compile("\\[@:(\\S+)\\]");
				//前半
				Pattern p2 = Pattern.compile("(\\S+)\\,");
				//後半
				Pattern p3 = Pattern.compile("\\,(\\S+)");
				//正規表現実行
				Matcher m = p.matcher(getcomment);
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
					profileList = topService.selProfileFShout(tagFdomain,tagMid);
					// 取得したmidの存在チェック
					if(profileList.size() == 1){
						//プロフィールよりmidを取得
						getprofilemid = profileList.get(0).get("mid").toString();
						//タグチェック
						String chkfstag = "[@:"+partStr2+","+partStr3+"]";
						if(getcomment.length()>chkfstag.length()){
							if(getcomment.substring(0,chkfstag.length()).equals(chkfstag)){
								//コメントの一文字目から指定の長さまでの文字列がメンバーのタグであればフラグに"1"を立てる
								chkfstagflg = "1";
							}
						}
					}
					break;
				}

				// ========================================== //
				// 確認状況のチェック                         //
				// ========================================== //
				// 内容の先頭にメンバータグがある場合、処理を行う
				if(chkfstagflg.equals("1")){
					// 自分が自分以外の誰かに確認要求
					if(getmid.equals(userInfoDto.memberId) && !getprofilemid.equals(userInfoDto.memberId)){
						// 未確認
						if(getconfirmflg.equals("0")){
							conftype = "1";
						// 確認済み
						} else {
							conftype = "2";
						}

					// 自分以外の誰かが自分に確認要求
					} else if (!getmid.equals(userInfoDto.memberId) && getprofilemid.equals(userInfoDto.memberId)){
						// 未確認
						if(getconfirmflg.equals("0")){
							conftype = "3";
						// 確認済み
						} else {
							conftype = "4";
						}
					}
				}
			}
			// BeanMapへconftypeを格納
			lbm.get(i).put("conftype", conftype);
		}
	}

	// 与えられたmidより公開範囲のリストを返す(F Shout用)
	private List<String> getFShoutPublevel(String mid){
		List<String> plevel = new ArrayList<String>();
		List<String> gmidlist = new ArrayList<String>();
		// 予め「外部公開」は追加しておく
		plevel.add(0,"0"); // 外部公開
		
		// ログインユーザのメンバータイプチェック(0:自Frontier、1:他Frontier、2:ゲスト)
		if(userInfoDto.membertype.equals("0")){
			// 0:自Frontier時の処理
			plevel.add(0,"1"); // Frontier Net公開
			plevel.add(0,"2"); // ﾏｲFrontier公開
	
			// midよりグループメンバーID取得
			gmidlist = getGList(mid);
	
			// 与えられたmidと自分のmidを比較
			if(mid.equals(userInfoDto.memberId)){
				// 与えられたのmidと自分のmidが同じ場合(自分の画面)
				plevel.add(0,"3"); // ｸﾞﾙｰﾌﾟ公開
			} else {
				// それ以外(他メンバーの画面)
				// グループメンバー数分ループ
				for(int i=0;i<gmidlist.size();i++){
					if(gmidlist.get(i).equals(userInfoDto.memberId)){
						// 同じグループの場合は公開権限追加
						plevel.add(0,"3"); // ｸﾞﾙｰﾌﾟ公開
					}
				}
			}
		} else if(userInfoDto.membertype.equals("1")){
			// 1:他Frontier時の処理
			plevel.add(0,"1"); // Frontier Net公開
			
		}
		return plevel;
	}

	// メンバーのフォローしているmidリストを返す
	private List<String> getFList(String mid){
		List<String> flist = new ArrayList<String>();
		// 0件対応
		flist.add("");
		// midよりフォローメンバーID取得
		List<BeanMap> lbmf = commonService.getMidList("2", mid);
		// リスト作成
		for(BeanMap a:lbmf){
			flist.add(a.get("mid").toString());
		}
		return flist;
	}

	// メンバーのグループのmidリストを返す
	private List<String> getGList(String mid){
		List<String> glist = new ArrayList<String>();
		// midよりグループメンバーID取得
		List<BeanMap> lbmg = commonService.getMidList("1", mid);
		// mid追加
		glist.add(mid);
		// リスト作成
		for(BeanMap a:lbmg){
			glist.add(a.get("mid").toString());
		}
		return glist;
	}
	
	// Twitterへ投稿時のコメント変換処理
	public String replaceFSCommentToTwitter(String txt){
		String viewFShout = "@xxxx";
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
			profileList = topService.selProfileFShout(tagFdomain,tagMid);
			String getNickName = partStr;
			String FShout = "";

			// 取得したmidが存在すればニックネームセット
			if(profileList.size() == 1){
				Object ta;
				// Twitterアカウントを取得(Object)
				ta = profileList.get(0).get("twitteraccount");
				// もしTwitterアカウントがNULLかブランクだったらタグ削除
				if(ta == null || ta.toString().equals("")){
					//FShoutの可変変数を削除
					FShout = viewFShout.replaceAll("@xxxx","");
				} else {
					getNickName = ta.toString();
					getNickName = getNickName.replace("\\","\\\\");
					//FShoutの可変変数を置換
					FShout = viewFShout.replaceAll("xxxx", getNickName);
				}
				FShout = FShout.replaceAll(" ", "");
				//final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+( )");
				final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
				//「\」対応
				FShout = FShout.replace("\\","\\\\\\\\");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				FShout = matcher.replaceAll("$0");
				//文字列連結
				m.appendReplacement(sb, FShout);
			}
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}
}
