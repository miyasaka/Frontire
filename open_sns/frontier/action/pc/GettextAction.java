package frontier.action.pc;

import frontier.dto.AppDefDto;
import frontier.entity.FrontierUserManagement;
import frontier.entity.Frontiernet;
import frontier.service.CommonService;
import frontier.service.FrontiernetService;
import frontier.common.CmnUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;

public class GettextAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected CommonService commonService;
	@Resource
	protected FrontiernetService frontiernetService;

	// 変数定義
	public String getUrl;
	public String getText;

	public String ftype;
	public String fdomain;
	public String fmid;

	// ■単純にHTMLのテキストデータを返す処理(XMLデータの取得)
	@Execute(validator=false)
	public String index() {
		getText = "";
		if ( getUrl != null && getUrl != ""){
			try {
				URL url;
				// 全パラメータが設定されているか確認
				if (
					ftype   != null && ftype   != "" &&
					fdomain != null && fdomain != "" &&
					fmid    != null && fmid    != ""
				){
					// パラメタが存在すれば付与してデータ取得
					url = new URL(getUrl + "?ftype=" + ftype + "&fdomain=" + fdomain + "&fmid=" + fmid);
				} else {
					// 存在しなければそのまま投げる
					url = new URL(getUrl);
				}
				//URL url = new URL("http://headlines.yahoo.co.jp/rss/rps_dom.xml");
				Object content = url.getContent();
				if( content instanceof InputStream ) {
					BufferedReader reader = new BufferedReader(
					new InputStreamReader( (InputStream)content , "UTF-8" ) );
					String line;
					getText = "";
					while( ( line = reader.readLine() ) != null ) {
						getText += line + "\n";
					}
					reader.close();
				} else {
					System.out.println( "This content is " + content.toString() );
				}
			} catch( ArrayIndexOutOfBoundsException e ) {
				System.err.println( "Usage: java URLTextContent URL" );
				//System.exit(-1);
			} catch( IOException e ) {
				System.err.println( "Network error!" );
				//System.exit(-1);
			}
		}
		return "index.jsp";
	}

	// ========================================
	//  ▼▼▼ マイトップページ用の処理 ▼▼▼
	// ========================================
	// 【URLに付与するパラメタ】
	//   ftype:(予測されるので数字にはせず)
	//      frontiernet … Frontier Net用のRSSを返す(トップページの右側)
	//      followlist  … 自分をフォローしているメンバーIDのリストを返す(XML)
	//   fdomain: ドメイン
	//   fmid: メンバーID
	// ----------------------------------------

	// ■RSS形式でテキストデータを返す処理(Frontier Net)
	@Execute(validator=false)
	public String outside() {
		getText = "";
		// xmlの宣言
		getText += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

		// 全パラメータが設定されているか確認
		if (
			ftype   != null && ftype   != "" &&
			fdomain != null && fdomain != "" &&
			fmid    != null && fmid    != ""
		){
			// Frontier Net に登録されているFrontierか、ドメインチェック
			List<Frontiernet> fn = frontiernetService.getFrontiernetList(fdomain);
			// 登録されていれば処理開始
			if(fn.size() == 1) {
				// タイプを判別
				if (ftype.equals("frontiernet")){
					// ====================================
					//   Frontier Net 用XML出力処理
					// ====================================
					// XML形式
					//   RSS2.0を参照

					// RSS2.0型のxml設定
					getText += "<rss xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\" xmlns:admin=\"http://webns.net/mvcb/\" version=\"2.0\">\n";
					// タグ開始
					getText += "\t<channel>\n";
					getText += "\t\t<title>" + appDefDto.FP_CMN_FRONTIER_NAME + "</title>\n";
					getText += "\t\t<link>http://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/openid/auth</link>\n";
					// 画像パス用(Frontier Net 専用)
					getText += "\t\t<description>" + appDefDto.FP_CMN_LOGOIMG_URL + "</description>\n";
					getText += "\t\t<dc:language>ja</dc:language>\n";

					// フォローメンバーリスト格納用変数
					List<Object> ls = new ArrayList<Object>();
					// フォローメンバーの日記リスト格納用変数
					List<BeanMap> lb = new ArrayList<BeanMap>();

					// メンバー管理テーブルを検索
					FrontierUserManagement fum = commonService.getFrontierUserManagement(fdomain,fmid);
					// NULLでなければ処理実行
					if(fum != null){

						// フォローメンバーのIDリストを取得
						List<BeanMap> bm = commonService.getMidList("2",fum.mid);

						// フォローメンバーが居れば処理スタート
						if(bm.size()>0){
							// フォローメンバーリストの配列を作成
							for(BeanMap f:bm){
								ls.add(f.get("mid"));
							}
						}else{
							// データが無い場合、ブランクを追加
							ls.add("");
						}
						// フォローメンバーIDリストより日記情報の取得
						lb = frontiernetService.getFDiary(ls);
						// 取得した日記リストがあれば処理スタート
						if(lb.size()>0){
							for(BeanMap f:lb){
								String xmltitle = "";
								String xmlnickname = "";
								// 出力項目のエスケープ
								try {
									xmltitle = CmnUtility.xmlescape(f.get("title"));
									xmlnickname = CmnUtility.xmlescape(f.get("nickname"));
								} catch (Exception e) {
									// TODO 自動生成された catch ブロック
									e.printStackTrace();
								}

								// 開始
								getText += "\t\t<item>\n";
								// title
								getText += "\t\t\t<title>" + xmltitle + "</title>\n";
								// link
								getText += "\t\t\t<link>";
								getText += "http://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/openid/auth/";
								getText += "?did=" + f.get("diaryid") + "&amp;gm=dv";
								getText += "&amp;cday=" + f.get("entdate").toString().substring(0,8) + "&amp;dmid=" + f.get("mid");
								getText += "</link>\n";
								// description
								getText += "\t\t\t<description>" + xmlnickname + "</description>\n";
								// pubDate
								getText += "\t\t\t<pubDate></pubDate>\n";
								// comment
								getText += "\t\t\t<comment>";
								getText += "http://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/openid/auth/?gm=mv&amp;cid=" + f.get("mid");
								getText += "</comment>\n";
								// 終了
								getText += "\t\t</item>\n";
							}
						}else{
							// データが無い場合
						}
					}else{
						// メンバーＩＤから一意のデータが取得できない場合
					}
					// タグ終了
					getText += "\t</channel>\n";
					getText += "</rss>";
				} else if (ftype.equals("followlist")){
					// ====================================
					//   フォローメンバーID リストのXML出力処理
					// ====================================
					// XML形式
					// ------------------------------------
					//   <flist>
					//     <domain>[ドメイン]</domain>
					//     <midlist>
					//       <mem>
					//         <mid>[メンバーID]</mid>
					//         <nickname>[ニックネーム]</nickname>
					//         <pic>[画像パス]</pic>
					//       </mem>
					//                  ・
					//                  ・
					//                  ・
					//       <mem>
					//         <mid>[メンバーID]</mid>
					//         <nickname>[ニックネーム]</nickname>
					//         <pic>[画像パス]</pic>
					//       </mem>
					//     </midlist>
					//   </flist>
					// ------------------------------------

					// タグ開始
					getText += "<flist>\n";
					getText += "\t<domain>" + appDefDto.FP_CMN_HOST_NAME + "</domain>\n";
					getText += "\t<midlist>\n";

					// メンバー管理テーブルを検索
					FrontierUserManagement fum = commonService.getFrontierUserManagement(fdomain,fmid);
					// NULLでなければ処理実行
					if(fum != null){
						// フォローメンバーのIDリストを取得
						List<BeanMap> bm = commonService.getMidList("2",fum.mid);
						// フォローメンバーが居れば処理スタート
						if(bm.size()>0){

							// フォローメンバーリストのループ
							for(BeanMap f:bm){
								String xmlnickname = "";
								// 出力項目のエスケープ
								try {
									xmlnickname = CmnUtility.xmlescape(f.get("nickname"));
								} catch (Exception e) {
									// TODO 自動生成された catch ブロック
									e.printStackTrace();
								}
								String picpath = "";
								if(!f.get("pic").equals("") && f.get("pic") != null){
									picpath = "http://" + appDefDto.FP_CMN_HOST_NAME + "/" + appDefDto.FP_CMN_CONTENTS_ROOT + f.get("pic");
								}
								
								getText += "\t\t<mem>";
								getText += "\t\t\t<mid>" + f.get("mid") + "</mid>\n";
								getText += "\t\t\t<nickname>" + xmlnickname + "</nickname>\n";
								getText += "\t\t\t<pic>" + picpath + "</pic>\n";
								getText += "\t\t</mem>";
							}

						}else{
							// データが無い場合
						}
					}else{
						// データが無い場合
					}

					// タグ終了
					getText += "\t</midlist>\n";
					getText += "</flist>\n";

				} else {
					// 与えられたパラメタが不正な場合
					rtnErrXml("エラー");
				}
			} else {
				// Frontier Netに登録されていないFrontierドメインの場合
				rtnErrXml("エラー");
			}
		} else {
			// 与えられたパラメタが不足している場合
			rtnErrXml("エラー");
		}
		return "index.jsp";
	}

	// 何らかのエラーでXMLファイルが出力できない時の処理
	private void rtnErrXml(String txt){
		getText += "<error>" + txt + "</error>\n";
	}
}