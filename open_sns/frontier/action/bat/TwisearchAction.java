package frontier.action.bat;

import frontier.common.CmnUtility;
import frontier.common.DecorationUtility;
import frontier.dto.AppDefDto;
import frontier.service.TwisearchService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;

public class TwisearchAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected TwisearchService twisearchService;
	// リクエストパラメタ格納用変数
	public HttpServletRequest hsr;
	// 変数定義
	public String getText;

	@Execute(validator=false)
	public String index() {
		// リクエストパラメタよりパラメタを取得
		String keywd = hsr.getParameter("keywd");
		String twiId = hsr.getParameter("twiId");

		//これを指定（ここではUTF-8）でエンコードしなおしてあげる
		try {
			keywd = new String(keywd.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<String> ls = new ArrayList<String>();
		// 取得したツイッターアカウントを配列に追加
		ls.add(twiId);
		// 送信時にエンコードした文字列をデコードする
		/*
		try {
			kwdTxt = URLDecoder.decode(keywd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		*/
		// 検索文字列の整形
		keywd = twisearchService.fixLudiaWord(keywd,"0");
		// 検索
		List<BeanMap> lbm = twisearchService.selSearchTwitter("",keywd,"0","0","0","0","0","0",ls,appDefDto.ST_MY_TL_LIST_MAX,0);
		getText="";
		// XML作成開始(xml宣言)
		getText += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
		// 一番大外のタグ
		getText += "<statuses type=\"array\">" + "\n";

		// ループ
		for(int i=0;i<lbm.size();i++){
			BeanMap b = lbm.get(i);
			String cmntTitle = CmnUtility.htmlSanitizing(b.get("comment").toString());
			String cmnt = new DecorationUtility().convURL(b.get("comment").toString());
			String usrnm = CmnUtility.htmlSanitizing(b.get("twitname").toString());
			cmnt = replaceTwiUser(cmnt);
			cmnt = CmnUtility.htmlSanitizing(cmnt);

			getText += "<status>" + "\n";
			getText += "<created_at>" + b.get("createtime") + "</created_at>" + "\n";
			getText += "<id>" + b.get("statusid") + "</id>" + "\n";
			getText += "<text>" + cmntTitle + "</text>" + "\n";
			getText += "<source></source>" + "\n";
			getText += "<truncated></truncated>" + "\n";
			getText += "<favorited>" + b.get("favorite") + "</favorited>" + "\n";
			getText += "<in_reply_to_status_id>" + b.get("replytwitstatusid") + "</in_reply_to_status_id>" + "\n";
			getText += "<in_reply_to_user_id>" + b.get("replytwituserid") + "</in_reply_to_user_id>" + "\n";
			getText += "<in_reply_to_screen_name></in_reply_to_screen_name>" + "\n";
			getText += "<retweet_count></retweet_count>" + "\n";
			getText += "<retweeted></retweeted>" + "\n";

			getText += "<user>" + "\n";
			getText += "<id>" + b.get("twituserid") + "</id>" + "\n";
			getText += "<name>" + usrnm + "</name>" + "\n";
			getText += "<screen_name>" + b.get("screenname") + "</screen_name>" + "\n";
			getText += "<location></location>" + "\n";
			getText += "<description></description>" + "\n";
			getText += "<profile_image_url>" + b.get("pic") + "</profile_image_url>" + "\n";
			getText += "<url></url>" + "\n";
			getText += "<protected></protected>" + "\n";
			getText += "<followers_count></followers_count>" + "\n";
			getText += "<profile_background_color></profile_background_color>" + "\n";
			getText += "<profile_text_color></profile_text_color>" + "\n";
			getText += "<profile_link_color></profile_link_color>" + "\n";
			getText += "<profile_sidebar_fill_color></profile_sidebar_fill_color>" + "\n";
			getText += "<profile_sidebar_border_color></profile_sidebar_border_color>" + "\n";
			getText += "<friends_count></friends_count>" + "\n";
			getText += "<created_at></created_at>" + "\n";
			getText += "<favourites_count></favourites_count>" + "\n";
			getText += "<utc_offset></utc_offset>" + "\n";
			getText += "<time_zone>Tokyo</time_zone>" + "\n";
			getText += "<profile_background_image_url></profile_background_image_url>" + "\n";
			getText += "<profile_background_tile></profile_background_tile>" + "\n";
			getText += "<profile_use_background_image></profile_use_background_image>" + "\n";
			getText += "<notifications></notifications>" + "\n";
			getText += "<geo_enabled></geo_enabled>" + "\n";
			getText += "<verified></verified>" + "\n";
			getText += "<following></following>" + "\n";
			getText += "<statuses_count></statuses_count>" + "\n";
			getText += "<lang>ja</lang>" + "\n";
			getText += "<contributors_enabled></contributors_enabled>" + "\n";
			getText += "<follow_request_sent></follow_request_sent>" + "\n";
			getText += "<listed_count></listed_count>" + "\n";
			getText += "<show_all_inline_media></show_all_inline_media>" + "\n";
			getText += "<is_translator></is_translator>" + "\n";
			getText += "</user>" + "\n";

			getText += "<geo/>" + "\n";
			getText += "<coordinates/>" + "\n";
			getText += "<place/>" + "\n";
			getText += "<contributors/>" + "\n";
			getText += "</status>" + "\n";
		}
		getText += "</statuses>" + "\n";
		return "index.jsp";
	}

	//Twitterコメント文字列置換（メンバー名リンク化対応）
	private String replaceTwiUser(String txt){
		String userNm = "@xxxx";
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("@(\\w+)");
		//正規表現実行
		Matcher m = p.matcher(txt);
		// 検索(find)し、マッチする部分文字列がある限り繰り返す
		while(m.find()){
			//部分文字列取得
			String partStr = m.group(1);
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			FShout = userNm.replaceAll("xxxx", partStr);
			final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
			//「\」対応
			Matcher matcher = convURLLinkPtn.matcher(FShout);
			//「:」排除　※ユーザー名は英数字と'_'が使えます。
			partStr = partStr.replace(":","");
			//自Frontier
			FShout = matcher.replaceAll("<a href=\"http://twitter.com/"+partStr+"\">@"+partStr+"</a>");
			//文字列連結
			m.appendReplacement(sb, FShout);
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

}