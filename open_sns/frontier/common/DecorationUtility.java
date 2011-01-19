package frontier.common;

import java.io.File;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 装飾系の処理を行うクラス。
 * 
 * @author H.Saikawa
 * @version 1.0
 */
public class DecorationUtility {
	Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * URLを<a>タグにして表示する処理<br>
	 * 下記の場合のみ対応<br>
	 * ・改行直後のURLに対応<br>
	 * ・半角スペースの後のURLに対応<br>
	 * ・URLのみの文字列に対応<br>
	 * @param moji 置換対象文字列
	 * @return 置換後の文字列
	 */
	public String convURL(String moji){

		//URLの正規表現文字列
		final String p = "(((\\n(http://|https://))|( (http://|https://))|^(http://|https://)){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+)";
		
		StringBuffer sb = new StringBuffer();
		//生成される<a>タグ
		String link = "<a href=\"XXX\" target=\"_blank\">XXX</a>";

		//matcherオブジェクトの取得
		Matcher matcher = CmnUtility.makeRegularExpress(p, moji);
		
		// 検索(find)し、マッチする部分文字列がある限り繰り返す
		while(matcher.find()){
			//部分文字列取得
			String partStr = matcher.group(1);
			//部分文字列からlinkに不必要な文字列を削除
			String partStr2 = partStr.replaceAll(" ", "");
			String linkStr = link.replaceAll("XXX", partStr2).replaceAll("\r\n","").replaceAll("\r","").replaceAll("\n","");
			//文字列連結
			matcher.appendReplacement(sb, " " + linkStr);

		}

		//残りの文字列連結
		matcher.appendTail(sb);
		
		return sb.toString();

	}
	
	/**
	 * 文字列を指定文字数でカットする
	 * @param txt 対象文字列
	 * @param i 有効な文字数の上限（ここで指定した文字数より大きい部分は削除する）
	 * @return 削除後の文字列
	 */
	public String cutString(String txt,int i){
		if(txt.length() > i){
			txt = txt.substring(0,i);
		}
		return txt;
	}

	/**
	 * LudiaでのAND検索用に単語と単語の間に「+」を付加する
	 * @param word　対象文字列
	 * @return　LudiaでのAND検索用に整形した文字列
	 */
	public String fixLudiaAndWord(String word){
		String fixWord = "";
		
		//半角スペース毎に文字を区切る
		String[] fixWordList = word.split(" ");
		
		//半角スペースで区切られた単語の数だけLudiaのAND検索文字「+」を付加する
		for(int i=0;i<fixWordList.length;i++){
			fixWord = fixWord + fixWordList[i];
			
			//単語と単語の間に[+]を付加する
			if((i+1)!=fixWordList.length){
				fixWord = fixWord + " + ";
			}
		}
		
		return fixWord;
	}
	
	/**
	 * Ludiaで扱えるように与えられた文字を整形する
	 * @param word 文字
	 * @return 整形後の文字
	 */
	public String fixLudiaWord(String word){
		String fixWord = "";

		//全角スペース->半角スペース
		fixWord = word.replaceAll("　", " ");
		//前後の空白削除
		fixWord = fixWord.trim();
		//Ludiaの特殊文字を無害化
		fixWord = fixWord.replaceAll("\\+", "＋");
		fixWord = fixWord.replaceAll("-", "－");
		fixWord = fixWord.replaceAll("OR", "or");
		fixWord = fixWord.replaceAll("\\*", "＊");
		fixWord = fixWord.replaceAll("\\(", "（");
		fixWord = fixWord.replaceAll("\\)", "）");
		fixWord = fixWord.replaceAll("<", "＜");
		fixWord = fixWord.replaceAll(">", "＞");
		fixWord = fixWord.replaceAll("\"", "”");		
		
		return fixWord;
	}
		
	/**
	 * HTMLサニタイジング処理
	 * @param moji サニタイジング対象の文字列
	 * @return String サニタイジング後の文字列
	 */
	public String htmlSanitizing(String moji){
		String rmoji = moji;

		//サニタイジング
		rmoji = rmoji.replaceAll("&",  "&amp;");
		rmoji = rmoji.replaceAll("<",  "&lt;");
		rmoji = rmoji.replaceAll(">",  "&gt;");
		rmoji = rmoji.replaceAll("\"", "&quot;");

		return rmoji;
	}
	
	/**
	 * パラメータで渡した文字列からUTF8で変換不能な文字を除外する
	 * @param mozi チェック対象の文字列
	 * @return 変換不能な文字を除いた文字列
	 */
	public String replaceCharacter(String mozi){
		String result = "";
		char c;
		
		for (int i = 0; i < mozi.length(); i++) {
			//文字列中の一文字を取得
			c = mozi.charAt(i);
			if(!Integer.toString((int)c, 16).equals("0")){
				//対象文字がUTF8で変換可能なコード値の場合は有効な文字として保存
				result = result + c;
			}
			
		}
		
		return result;
	}
	
	/**
	 * 絵文字タグ→画像変換
	 * @param comment 置換対象の文字列
	 * @param CmnEmojiXmlPath 絵文字リストのxmlパス
	 * @return String 置換後の文字列
	 */
	public String replaceEmoji(String txt,String CmnEmojiImgPath,String CmnEmojiXmlPath){
		NodeList nlist = null;
		String rtncmnt = txt;
		String inm = "";
		String ialt = "";
		Element nelmnt = null;

		// i-mode絵文字xmlファイルの読み込み&リストの作成
		try {
			// ドキュメントビルダーファクトリを生成
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			// ドキュメントビルダーを生成
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			// パースを実行してDocumentオブジェクトを取得
			Document doc = builder.parse(new File(CmnEmojiXmlPath));
			// ルート要素を取得
			Element root = doc.getDocumentElement();
			// Listに格納
			nlist = root.getElementsByTagName("entry");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// iモード絵文字の変換
		// 絵文字List数分ループ
		for (int i=0; i < nlist.getLength() ; i++) {
			nelmnt = (Element)nlist.item(i);
			inm = nelmnt.getAttribute("id");
			ialt = nelmnt.getAttribute("name");
			Matcher mt= Pattern.compile("\\[i:"+ inm +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
			// リストとマッチすればイメージタグに変換
			rtncmnt = mt.replaceAll("<img src=\'" + CmnEmojiImgPath + inm + ".gif\' alt=\'" + ialt + "\'/>");
		}

		return rtncmnt;
	}
	
	/**
	 * 数値を指定の文字へとフォーマット設定する
	 * @param format 変換するフォーマット指定
	 * @param val 変換対象の数値
	 * @return String
	 */
	public String stringFormat(String format,int val){
		DecimalFormat df = new DecimalFormat(format);

		return df.format(val);
	}
}
