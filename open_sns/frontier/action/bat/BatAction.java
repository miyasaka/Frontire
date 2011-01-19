package frontier.action.bat;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.service.BatService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BatAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public BatService batService;
	// 変数定義
	private String myFQDN;
	private String reFQDN;
	private Integer fnId;
	private String fnNetwork;
	private String fnLastdate;
	public HttpServletRequest hsr;
	public String getText;

	// ==================== //
	//   -- メイン処理 --   //
	// ==================== //
	// ---------------------------------------------- //
	// ■自Frontier -> 他Frontier 処理                //
	//   自分のホストへリクエストがあった場合         //
	//   Frontier Netのデータを取得して更新処理開始   //
	// ---------------------------------------------- //
	@Execute(validator=false)
	public String index() {
		List<BeanMap> lbm = new ArrayList<BeanMap>();
		getText = "";
		reFQDN = "";

		// localhost:xxxxへリクエストを投げて処理開始(自サーバ内処理)
		if(hsr != null){
			// 自サーバのIP取得(リクエストを受け取ったIPアドレス)
			myFQDN = hsr.getLocalAddr();
			// リクエスト元のIP取得
			reFQDN = hsr.getRemoteAddr();
		}

		// 値が取得できていて、リクエスト元と自サーバのIPが同じならば処理開始
		// (本当に自分のサーバで投げられたリクエスト(localhost)か確認)
		if(!myFQDN.equals("") && !reFQDN.equals("") && myFQDN.equals(reFQDN)){
			// Frontier Net テーブルよりホスト名を取得し、XMLで返す。
			lbm = batService.selFNet();
			// Frontier Net数分ループ
			for(BeanMap i:lbm){
				String fdomain = "";
				String furl = "";
				// Frontier Domainの取得
				fdomain = i.get("network").toString();
				// リクエスト先URLの設定
				furl = "http://" + fdomain + "/frontier/bat/bat/putFdt";
				// リクエストにパラメタ付与(リクエスト元ドメイン情報)
				furl = furl + "?fdomain=" + appDefDto.FP_CMN_HOST_NAME;
				// 他FrontierからXMLデータを取得＆DB登録・更新
				try {
					getXmlAndUpdDB(furl);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			getText += "<result>Success</result>";
		} else {
			getText += "<result>Error</result>";
		}
		return "index.jsp";
	}

	// ---------------------------------------------- //
	// ■他Frontier -> 自Frontier 処理                //
	//   FrontierNetのホスト名でリクエストが          //
	//   があった場合、データを取得してXMLで返す      //
	// ---------------------------------------------- //
	@Execute(validator=false)
	public String putFdt() {
		List<BeanMap> lbm = new ArrayList<BeanMap>();
		String reIP = "";
		getText = "";
		reFQDN = "";
		String fnflg = "0";
		// 自サーバのホスト名取得
		myFQDN = appDefDto.FP_CMN_HOST_NAME;
		// リクエスト元のFQDN取得
		if(hsr != null){
			// リクエスト元のIP取得
			reIP = hsr.getRemoteAddr();
			// リクエストパラメタより取得
			reFQDN = hsr.getParameter("fdomain");
		}

		// Frontier Net テーブルよりホスト名を取得し、取得したホスト名と一致するかチェック
		lbm = batService.selFNet();
		for(BeanMap i:lbm){
			if(reFQDN.equals(i.get("network").toString())){
				String getIP = "";
				// 一致したらドメインよりIPを逆引きして比較
				try {
					getIP = InetAddress.getByName(reFQDN).getHostAddress();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(reIP.equals(getIP)){
					// リクエスト元のIPとドメインから逆引きしたIPが同じものであればフラグを立てる
					fnflg = "1";
					// --------------------------- //
					// Frontier Net情報の取得      //
					// --------------------------- //
					// id取得
					fnId = (Integer)i.get("id");
					// network取得
					fnNetwork = i.get("network").toString();
					// 最終取得日時取得
					fnLastdate = i.get("lastdate").toString();
					// 処理を抜ける
					break;
				}
			}
		}

		// XML作成開始(xml宣言)
		getText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n";

		// フラグが立っていれば処理開始
		if(fnflg.equals("1")){
			// Frontier Net 最終取得日時カラムの更新(バッチで最後に更新処理を流した日付)
			batService.updFNet(fnId, fnNetwork);
			// 大外のタグ
			getText += "<frontier>" + "\n";
			// ドメイン
			getText += "\t" + "<domain>" + myFQDN + "</domain>" + "\n";
			// メンバー情報タグの生成
			getText += getMembersTag(fnLastdate);
			// フォロー関係情報タグの生成
			getText += getFollowTag(fnNetwork,fnLastdate);
			// Frontier Shoutタグの生成
			getText += getFrontierShoutTag(fnLastdate,fnNetwork);
			// 大外のタグ閉じ
			getText += "</frontier>";
		} else {
			// エラー
			getText += "<error/>";
		}
		// xmlを返す
		return "index.jsp";
	}

	// ==================== //
	//    -- function --    //
	// ==================== //
	// 与えられたURLよりXMLを取得してDB登録・更新を行う
	private void getXmlAndUpdDB(String url){
		// 変数定義
		NodeList mlist  = null; // メンバー情報用
		NodeList flist  = null; // フォロー関係情報用
		NodeList fslist = null; // Frontier Shout用
		String xfdomain = "";   // ドメイン

		// XMLよりドメイン、メンバー情報、フォロー関係情報、Frontier Shoutの取得
		try {
			// ドキュメントビルダーファクトリを生成
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			// ドキュメントビルダーを生成
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			// パースを実行してDocumentオブジェクトを取得
			Document doc = builder.parse(url);
			// ルート要素を取得
			Element root = doc.getDocumentElement();
			// domain取得(domain)タグの最初の子ノード(テキストノード)の値を取得
			xfdomain = root.getElementsByTagName("domain").item(0).getChildNodes().item(0).getNodeValue();
			// ------------------------- //
			//    テーブル情報の取得     //
			// ------------------------- //
			// メンバー情報タグ取得
			mlist = root.getElementsByTagName("members");
			// フォロー関係情報タグ取得
			flist = root.getElementsByTagName("follow");
			// Frontier Shoutタグ取得
			fslist = root.getElementsByTagName("frontiershout");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 【メインのDB更新処理】
		// メンバータグのList<BeanMap>化 & 更新処理
		updMembers(xfdomain,chgTagToList("members",mlist));
		// フォロータグのList<BeanMap>化 & 更新処理
		updFollow(xfdomain,chgTagToList("follow",flist));
		// Frontier ShoutタグのList<BeanMap>化 & 更新処理
		updFrontierShout(chgTagToList("frontiershout",fslist));

		// 【処理終了後の処理】
		// 退会したメンバーのフォロー関係情報削除(更新)
		batService.delFollow();
		// メンバー情報・フォロー数、フォローワー数の更新
		batService.updMembersFollownumber();
	}

	// タグのList化
	//   ※共通の記述が多いのでメソッドを１つに統合
	private List<BeanMap> chgTagToList(String table, NodeList nl){
		List<BeanMap> lbm  = new ArrayList<BeanMap>();
		// タグ内のデータがあれば処理開始
		if(nl.getLength() == 1){
			// Elementにキャスト
			Element el = (Element)nl.item(0);
			// データの取得(dtタグ)
			NodeList nldt = el.getElementsByTagName("dt");
			// データ数の取得
			long nldtcnt = nldt.getLength();
			// データ数分ループ
			for(int i=0;i<nldtcnt;i++){
				BeanMap b = new BeanMap();
				if(table.equals("members")){
					// ------------------------ //
					//   membersテーブル        //
					// ------------------------ //
					// Elementにキャストして、タグ名指定で値取得
					String xmid      = ((Element)nldt.item(i)).getElementsByTagName("mid").item(0).getTextContent();
					String xpic      = ((Element)nldt.item(i)).getElementsByTagName("pic").item(0).getTextContent();
					String xnickname = ((Element)nldt.item(i)).getElementsByTagName("nickname").item(0).getTextContent();
					String xstatus   = ((Element)nldt.item(i)).getElementsByTagName("status").item(0).getTextContent();
					// BeanMap に変数を設定
					b.put("mid",xmid);
					b.put("pic",xpic);
					b.put("nickname",xnickname);
					b.put("status",xstatus);
				} else if(table.equals("follow")){
					// ------------------------ //
					//   followテーブル         //
					// ------------------------ //
					// Elementにキャストして、タグ名指定で値取得
					String xfollowmid   = ((Element)nldt.item(i)).getElementsByTagName("followmid").item(0).getTextContent();
					String xfollowermid = ((Element)nldt.item(i)).getElementsByTagName("followermid").item(0).getTextContent();
					String xconfirmflg  = ((Element)nldt.item(i)).getElementsByTagName("confirmflg").item(0).getTextContent();
					String xentdate     = ((Element)nldt.item(i)).getElementsByTagName("entdate").item(0).getTextContent();
					String xdelflg      = ((Element)nldt.item(i)).getElementsByTagName("delflg").item(0).getTextContent();
					// BeanMap に変数を設定
					b.put("followmid",xfollowmid);
					b.put("followermid",xfollowermid);
					b.put("confirmflg",xconfirmflg);
					b.put("entdate",xentdate);
					b.put("delflg",xdelflg);
				} else if(table.equals("frontiershout")){
					// ------------------------ //
					//   frontiershoutテーブル  //
					// ------------------------ //
					// Elementにキャストして、タグ名指定で値取得
					String xfdomain    = ((Element)nldt.item(i)).getElementsByTagName("fdomain").item(0).getTextContent();
					String xfid        = ((Element)nldt.item(i)).getElementsByTagName("fid").item(0).getTextContent();
					String xno         = ((Element)nldt.item(i)).getElementsByTagName("no").item(0).getTextContent();
					String xentdate    = ((Element)nldt.item(i)).getElementsByTagName("entdate").item(0).getTextContent();
					String xcomment    = ((Element)nldt.item(i)).getElementsByTagName("comment").item(0).getTextContent();
					String xtwitter    = ((Element)nldt.item(i)).getElementsByTagName("twitter").item(0).getTextContent();
					String xdemandflg  = ((Element)nldt.item(i)).getElementsByTagName("demandflg").item(0).getTextContent();
					String xconfirmflg = ((Element)nldt.item(i)).getElementsByTagName("confirmflg").item(0).getTextContent();
					String xpubLevel   = ((Element)nldt.item(i)).getElementsByTagName("pubLevel").item(0).getTextContent();
					String xdelflg     = ((Element)nldt.item(i)).getElementsByTagName("delflg").item(0).getTextContent();
					// BeanMap に変数を設定
					b.put("fdomain",xfdomain);
					b.put("fid",xfid);
					b.put("no",xno);
					b.put("entdate",xentdate);
					b.put("comment",xcomment);
					b.put("twitter",xtwitter);
					b.put("demandflg",xdemandflg);
					b.put("confirmflg",xconfirmflg);
					b.put("pubLevel",xpubLevel);
					b.put("delflg",xdelflg);
				}
				// List<BeanMap>に追加
				lbm.add(b);
			}
		}
		return lbm;
	}

	// ---------------------------- //
	// メンバー情報関連データの更新 //
	// ---------------------------- //
	private void updMembers(String fdomain,List<BeanMap> lbm){
		// サイズチェック
		if(lbm.size()>0){
			// 更新データが1件以上あれば処理開始
			// データ数分ループ
			for(BeanMap i:lbm){
				List<BeanMap> lbmm = new ArrayList<BeanMap>();
				// Frontierユーザ管理テーブルをチェック
				lbmm = batService.chkFrontierUserManagement(fdomain,i.get("mid").toString());
				// 存在チェックをして登録 or 更新処理開始
				if(lbmm.size() == 1){
					// --------------------------- //
					// データがあれば更新処理      //
					// --------------------------- //
					// メンバーID取得
					String mid = lbmm.get(0).get("mid").toString();
					// 更新用パラメタセット
					String updpic      = i.get("pic").toString();
					String updnickname = i.get("nickname").toString();
					String updstatus   = i.get("status").toString();
					// メンバー情報関連の更新
					batService.updMembers(
						mid,
						updpic,
						updnickname,
						updstatus
					);
				} else {
					// --------------------------- //
					// データが無ければ登録処理    //
					// --------------------------- //
					// 登録用パラメタセット
					String insfid      = i.get("mid").toString();
					String inspic      = i.get("pic").toString();
					String insnickname = i.get("nickname").toString();
					String insstatus   = i.get("status").toString();
					// メンバー情報関連の登録
					batService.insMembers(
						fdomain,
						insfid,
						inspic,
						insnickname,
						insstatus
					);
				}
			}
		}
	}

	// ---------------------------- //
	//    フォロー関係情報の更新    //
	// ---------------------------- //
	private void updFollow(String fdomain,List<BeanMap> lbm){
		// サイズチェック
		if(lbm.size()>0){
			// 更新データが1件以上あれば処理開始
			// データ数分ループ
			for(BeanMap i:lbm){
				List<BeanMap> lbmm = new ArrayList<BeanMap>();
				List<BeanMap> lbmf = new ArrayList<BeanMap>();
				String followmid = i.get("followmid").toString();
				String fid = i.get("followermid").toString();
				// Frontierユーザ管理テーブルからフォローされたmid取得(確実にあること前提の処理)
				lbmm = batService.chkFrontierUserManagement(fdomain,fid);
				String followermid = lbmm.get(0).get("mid").toString();
				// followテーブルのデータ存在チェック
				lbmf = batService.chkFollow(followmid,followermid);
				// 存在チェックをして登録 or 更新処理開始
				if(Integer.valueOf(lbmf.get(0).get("cnt").toString()) == 1){
					// --------------------------- //
					// データがあれば更新処理      //
					// --------------------------- //
					// 更新用パラメタセット
					String updconfirmflg = i.get("confirmflg").toString();
					String upddelflg     = i.get("delflg").toString();
					// followデータの更新
					batService.updFollow(
						followmid,
						followermid,
						updconfirmflg,
						upddelflg
					);
				} else {
					// --------------------------- //
					// データが無ければ登録処理    //
					// --------------------------- //
					// 登録用パラメタセット
					String insentdate    = i.get("entdate").toString();
					String insconfirmflg = i.get("confirmflg").toString();
					String insdelflg     = i.get("delflg").toString();
					// followデータの登録
					batService.insFollow(
						followmid,
						followermid,
						insentdate,
						insconfirmflg,
						insdelflg
					);
				}
			}
		}
	}

	// ---------------------------- //
	//     Frontier Shoutの更新     //
	// ---------------------------- //
	private void updFrontierShout(List<BeanMap> lbm){
		// サイズチェック
		if(lbm.size()>0){
			// 更新データが1件以上あれば処理開始
			// データ数分ループ
			for(BeanMap i:lbm){
				List<BeanMap> lbmm = new ArrayList<BeanMap>();
				List<BeanMap> lbmfs = new ArrayList<BeanMap>();
				String fdomain = i.get("fdomain").toString();
				String fid = i.get("fid").toString();
				Integer no = Integer.valueOf(i.get("no").toString()).intValue();
				// Frontierユーザ管理テーブルからmid取得(確実にあること前提の処理)
				lbmm = batService.chkFrontierUserManagement(fdomain,fid);
				String mid = lbmm.get(0).get("mid").toString();
				// Frontier Shoutテーブルのデータ存在チェック
				lbmfs = batService.chkFrontierShout(mid,no);
				// 存在チェックをして登録 or 更新処理開始
				if(Integer.valueOf(lbmfs.get(0).get("cnt").toString()) == 1){
					// --------------------------- //
					// データがあれば更新処理      //
					// --------------------------- //
					// 更新用パラメタセット
					String updconfirmflg = i.get("confirmflg").toString();
					String upddelflg     = i.get("delflg").toString();
					// Frontier Shoutデータの更新
					batService.updFrontierShout(
						mid,
						no,
						updconfirmflg,
						upddelflg
					);
				} else {
					// --------------------------- //
					// データが無ければ登録処理    //
					// --------------------------- //
					// 登録用パラメタセット
					String inscomment    = i.get("comment").toString();
					Integer instwitter   = Integer.valueOf(i.get("twitter").toString()).intValue();
					String insentdate    = i.get("entdate").toString();
					Integer insdemandflg = Integer.valueOf(i.get("demandflg").toString()).intValue();
					Integer inspubLevel  = Integer.valueOf(i.get("pubLevel").toString()).intValue();
					String insconfirmflg = i.get("confirmflg").toString();
					String insdelflg     = i.get("delflg").toString();
					// Frontier Shoutデータの登録
					batService.insFrontierShout(
						mid,
						no,
						inscomment,
						instwitter,
						insentdate,
						insdemandflg,
						inspubLevel,
						insconfirmflg,
						insdelflg
					);
				}
			}
		}
	}

	// メンバー情報タグの生成
	private String getMembersTag(String lastdate){
		// ----------------------------------------
		// 生成タグレイアウト
		// ----------------------------------------
		// <members>
		//   <dt>
		//     <mid>[メンバーID]</mid>
		//     <pic>[画像パス(フル)]</pic>
		//     <nickname>[ニックネーム]</nickname>
		//     <status>[メンバーステータス]</status>
		//   </dt>
		//           ・
		//           ・
		// </members>
		// ----------------------------------------
		List<BeanMap> lbm  = new ArrayList<BeanMap>();
		// メンバー情報の取得
		lbm = batService.selMem(lastdate);
		String rtnVal = "";
		// メンバータグ開始
		rtnVal += "\t" + "<members>" + "\n";
		// データ数分ループ
		for(BeanMap i:lbm){
			String xmlnickname = "";
			// 出力項目のエスケープ
			try {
				xmlnickname = CmnUtility.xmlescape(i.get("nickname").toString());
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			// 画像パスの設定(フルパス)
			String picpath = "";
			if(i.get("pic") != null && !i.get("pic").toString().equals("")){
				picpath = "http://" + appDefDto.FP_CMN_HOST_NAME + "/" + appDefDto.FP_CMN_CONTENTS_ROOT + i.get("pic").toString();
			}
			rtnVal += "\t\t"   + "<dt>"                                                    + "\n";
			rtnVal += "\t\t\t" + "<mid>"      + i.get("mid").toString()    + "</mid>"      + "\n";
			rtnVal += "\t\t\t" + "<pic>"      + picpath                    + "</pic>"      + "\n";
			rtnVal += "\t\t\t" + "<nickname>" + xmlnickname                + "</nickname>" + "\n";
			rtnVal += "\t\t\t" + "<status>"   + i.get("status").toString() + "</status>"   + "\n";
			rtnVal += "\t\t"   + "</dt>"                                                   + "\n";
		}
		// メンバータグ終了
		rtnVal += "  </members>" + "\n";
		return rtnVal;
	}

	// フォロー関係情報タグの生成
	private String getFollowTag(String frontierdomain,String lastdate){
		// ----------------------------------------
		// 生成タグレイアウト
		// ----------------------------------------
		// <follow>
		//   <dt>
		//     <followmid>[フォローしたメンバーID(fid)]</followmid>
		//     <followermid>[フォローされたメンバーID]</followermid>
		//     <confirmflg>[確認FLG]</confirmflg>
		//     <delflg>[削除FLG]</delflg>
		//   </dt>
		//           ・
		//           ・
		// </follow>
		// ----------------------------------------
		List<BeanMap> lbm  = new ArrayList<BeanMap>();
		// フォロー関係情報の取得
		lbm = batService.selFollow(frontierdomain,lastdate);
		String rtnVal = "";
		// フォロータグ開始
		rtnVal += "\t" + "<follow>" + "\n";
		// データ数分ループ
		for(BeanMap i:lbm){
			List<BeanMap> lbmf  = new ArrayList<BeanMap>();
			// Frontierユーザ管理テーブルよりドメイン、midをキーにfidを取得
			lbmf = batService.selFid(frontierdomain,i.get("followmid").toString());
			// フォローしたIDにfidをセット
			String followmid = lbmf.get(0).get("fid").toString();
			rtnVal += "\t\t"   + "<dt>"                                                               + "\n";
			rtnVal += "\t\t\t" + "<followmid>"   + followmid                       + "</followmid>"   + "\n";
			rtnVal += "\t\t\t" + "<followermid>" + i.get("followermid").toString() + "</followermid>" + "\n";
			rtnVal += "\t\t\t" + "<confirmflg>"  + i.get("confirmflg").toString()  + "</confirmflg>"  + "\n";
			rtnVal += "\t\t\t" + "<entdate>"     + i.get("entdate").toString()     + "</entdate>"     + "\n";
			rtnVal += "\t\t\t" + "<delflg>"      + i.get("delflg").toString()      + "</delflg>"      + "\n";
			rtnVal += "\t\t"   + "</dt>"                                                              + "\n";
		}
		// フォロータグ終了
		rtnVal += "\t" + "</follow>" + "\n";
		return rtnVal;
	}

	// Frontier Shoutタグの生成
	private String getFrontierShoutTag(String lastdate,String fdomain){
		// ----------------------------------------
		// 生成タグレイアウト
		// ----------------------------------------
		// <frontiershout>
		//   <dt>
		//     <domain>[frontierドメイン]</domain>
		//     <fid>[メンバーID(fid)]</fid>
		//     <no>[no]</no>
		//     <entdate>[登録日]</entdate>
		//     <comment>[本文]</comment>
		//     <twitter>[twitter投稿フラグ]</twitter>
		//     <demandflg>[確認要求フラグ]</demandflg>
		//     <confirmflg>[確認FLG]</confirmflg>
		//     <pubLevel>[公開レベル]</pubLevel>
		//     <delflg>[削除FLG]</delflg>
		//   </dt>
		//           ・
		//           ・
		// </frontiershout>
		// ----------------------------------------
		List<BeanMap> lbm  = new ArrayList<BeanMap>();
		// Frontier Shoutの取得
		lbm = batService.selFrontierShout(lastdate,fdomain);
		String rtnVal = "";
		// Frontier Shoutタグ開始
		rtnVal += "\t" + "<frontiershout>" + "\n";
		// データ数分ループ
		for(BeanMap i:lbm){
			String xmlcomment = "";
			// 出力項目のエスケープ
			try {
				xmlcomment = CmnUtility.xmlescape(i.get("comment").toString());
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			rtnVal += "\t\t"   + "<dt>"                                                                + "\n";
			rtnVal += "\t\t\t" + "<fdomain>"    + i.get("frontierdomain").toString() + "</fdomain>"    + "\n";
			rtnVal += "\t\t\t" + "<fid>"        + i.get("fid").toString()            + "</fid>"        + "\n";
			rtnVal += "\t\t\t" + "<no>"         + i.get("no").toString()             + "</no>"         + "\n";
			rtnVal += "\t\t\t" + "<entdate>"    + i.get("entdate").toString()        + "</entdate>"    + "\n";
			rtnVal += "\t\t\t" + "<comment>"    + xmlcomment                         + "</comment>"    + "\n";
			rtnVal += "\t\t\t" + "<twitter>"    + i.get("twitter").toString()        + "</twitter>"    + "\n";
			rtnVal += "\t\t\t" + "<demandflg>"  + i.get("demandflg").toString()      + "</demandflg>"  + "\n";
			rtnVal += "\t\t\t" + "<confirmflg>" + i.get("confirmflg").toString()     + "</confirmflg>" + "\n";
			rtnVal += "\t\t\t" + "<pubLevel>"   + i.get("pubLevel").toString()       + "</pubLevel>"   + "\n";
			rtnVal += "\t\t\t" + "<delflg>"     + i.get("delflg").toString()         + "</delflg>"     + "\n";
			rtnVal += "\t\t"   + "</dt>"                                                               + "\n";
		}
		// フォロータグ終了
		rtnVal += "\t" + "</frontiershout>" + "\n";
		return rtnVal;
	}

}