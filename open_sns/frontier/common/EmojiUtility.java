package frontier.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EmojiUtility {
	/**
	 * モバイルで入力した絵文字をPC用へと置換する。
	 * @param str 絵文字を含む文字
	 * @param emojiXml 絵文字XMLパス
	 * @param agent ユーザエージェント
	 * @return String 絵文字置換した文字列
	 */
	public static String replaceMoblileToPc(String str,String[] emojiXml,String agent,String rootPath){
		NodeList nlist = null;
		String inm = "";
		String unicode = "";
		String fcode = "";
		String fcodeName = "";
		Matcher mt;
		Element nelmnt;

		//xmlファイルの読み込み
		nlist = readXml(emojiXml,agent,rootPath);
		
		//携帯のみ実行
		if (nlist != null){
			for (int i=0; i < nlist.getLength() ; i++) {
				nelmnt = (Element)nlist.item(i);
				inm = nelmnt.getAttribute("id");
				unicode = nelmnt.getAttribute("unicode");
				fcode = nelmnt.getAttribute("frontiercode");
				if(unicode != null && !unicode.equals("")){
					// リストとマッチすればPC用絵文字表記に変換
					mt= Pattern.compile(unicode,Pattern.CASE_INSENSITIVE).matcher(str);
								
					if(agent.indexOf("DoCoMo") == 0){
						str = mt.replaceAll("[i:"+ inm +"]");
					}else if(agent.indexOf("J-PHONE") == 0 || agent.indexOf("Vodafone") == 0 || agent.indexOf("SoftBank") == 0 || agent.indexOf("UP.Browser") == 0 || agent.indexOf("KDDI") == 0){						
						if (fcode != null && !fcode.equals("")){
							//frontiercodeあり
							str = mt.replaceAll("[i:"+ fcode +"]");	
						}else{
							//frontiercodeなし
							//xmlからコードがない場合の代わりとなる文字列を取得
							fcodeName = nelmnt.getAttribute("frontiercodename");
							str = mt.replaceAll(fcodeName);	
						}
						
					}			
				}
			}
		}
		return str;
	}
	
	/*　テスト用
	public static String replaceAU(String str,String[] emojiXml,String agent){
		NodeList nlist = null;
		String inm = "";
		String unicode = "";
		String fcode = "";
		Matcher mt;
		Element nelmnt;
		
		//xmlファイルの読み込み
		nlist = readXml(emojiXml,agent);
		
		char srcChars[] = str.toCharArray();
		String test = "";
		for(int i = 0; i < srcChars.length; i++){
			//test = Integer.toHexString(srcChars[i]);
			//16進数化した1文字
			test = Integer.toHexString(srcChars[i]);
			System.out.println(" !! AUコード= " + test);
			
			//絵文字xmlを繰り返す
			for (int j=0; j < nlist.getLength() ; j++) {
				nelmnt = (Element)nlist.item(j);
				unicode = nelmnt.getAttribute("unicode");
				fcode = nelmnt.getAttribute("frontiercode");
				String charString = new String(new char[] { srcChars[i]});
				//置換対象が存在すれば
				if(unicode != null && unicode.equals(test)){
					str = str.replaceAll(charString, "[i:"+ fcode +"]");
				}
			}
		}
		System.out.println(" !! 答えは= " + str);
		for (int i=0; i < nlist.getLength() ; i++) {
			nelmnt = (Element)nlist.item(i);
			inm = nelmnt.getAttribute("id");
			unicode = nelmnt.getAttribute("unicode");
			fcode = nelmnt.getAttribute("frontiercode");
			if(unicode != null && !unicode.equals("")){
				// リストとマッチすればPC用絵文字表記に変換
				mt= Pattern.compile(unicode,Pattern.CASE_INSENSITIVE).matcher(str);
								
				str = mt.replaceAll("[i:"+ fcode +"]");
				
			}
		}		
		return str;
	}
	*/
	
	/*　テスト用
	public static String replaceAUUtf8(String str,String[] emojiXml,String agent){
		NodeList nlist = null;
		String inm = "";
		String unicode = "";
		String fcode = "";
		Matcher mt;
		Element nelmnt;
		
		//xmlファイルの読み込み
		nlist = readXml(emojiXml,agent);
		
		char srcChars[] = str.toCharArray();
		String test = "";
		
		try {
			//UTF8化
			//byte[] b = str.getBytes("SJIS");
			byte[] b = str.getBytes("windows-31J");
			
			for (int i = 0; i < b.length; i++) {
				//System.out.println(Integer.toString(b[i] & 0xFF, 16));
				test = test + "@" + Integer.toHexString(b[i] & 255);
				}
			
			
			byte bArray[] = str.getBytes("UTF-16LE");
			for( int i = 0 ; i < bArray.length - 1 ; i+=2 ){
			char cCode = (char)(bArray[i] + bArray[i+1]*0x100);
			test = test + "@" + Integer.toHexString(cCode);
			}

			
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return test;
		//絵文字xmlを繰り返す
		for (int j=0; j < nlist.getLength() ; j++) {
			nelmnt = (Element)nlist.item(j);
			unicode = nelmnt.getAttribute("unicode");
			fcode = nelmnt.getAttribute("frontiercode");
			//String charString = new String(new char[] { srcChars[i]});
			//置換対象が存在すれば
			if(unicode != null && !unicode.equals("")){
				str = test.replaceAll(fcode, "[i:"+ fcode +"]");
			}
		}
		
		return str;
		
	}
	*/
	
	/**
	 * PCで入力した絵文字をモバイル用へと置換する。
	 * @param str 絵文字を含む文字
	 * @param emojiXml 絵文字XMLパス
	 * @param agent ユーザエージェント
	 * @return String 絵文字置換した文字列
	*/
	public static String replacePcToMoblile(String str,String[] emojiXml,String agent,String rootPath){
		NodeList nlist = null;
		String rtncmnt = "";
		String inm = "";
		String unicodehtml = "";
		String fcode = "";
		Matcher mt;
		
		//置換文字を含む文字列を代入
		rtncmnt = str;
		
		//キャリア毎に使用する値を変更する
		/*
		if(agent.indexOf("DoCoMo")==0){
			codeStr = "id";
		}else if(agent.indexOf("J-PHONE") == 0 || agent.indexOf("Vodafone") == 0 || agent.indexOf("SoftBank") == 0){
			codeStr = "frontiercode";			
		}else if(agent.indexOf("UP.Browser") == 0 || agent.indexOf("KDDI") == 0){
			codeStr = "frontiercode";			
		}
		*/			
		
		//xmlファイルの読み込み
		nlist = readXml(emojiXml,agent,rootPath);

		if(agent.indexOf("DoCoMo")==0){
			// 絵文字List数分ループ
			//docomoの場合
			for (int i=0; i < nlist.getLength() ; i++) {
				Element nelmnt = (Element)nlist.item(i);
				inm = nelmnt.getAttribute("id");
				unicodehtml = nelmnt.getAttribute("unicodehtml");
				//docomo
				mt= Pattern.compile("\\[i:"+ inm +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
				
				// リストとマッチすればイメージタグに変換
				//docomo
				rtncmnt = mt.replaceAll(unicodehtml);
			}
		}else if(agent.indexOf("J-PHONE") == 0 || agent.indexOf("Vodafone") == 0 || agent.indexOf("SoftBank") == 0){
			// 絵文字List数分ループ
			//softbankの場合
			for (int i=0; i < nlist.getLength() ; i++) {
				Element nelmnt = (Element)nlist.item(i);
				unicodehtml = nelmnt.getAttribute("unicodehtml");
				fcode = nelmnt.getAttribute("frontiercode");
				//softbank
				mt= Pattern.compile("\\[i:"+ fcode +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
				
				// リストとマッチすればイメージタグに変換
				//softbank
				rtncmnt = mt.replaceAll(unicodehtml);
			}		
		}else if(agent.indexOf("UP.Browser") == 0 || agent.indexOf("KDDI") == 0){
			// 絵文字List数分ループ
			//auの場合
			for (int i=0; i < nlist.getLength() ; i++) {
				Element nelmnt = (Element)nlist.item(i);
				inm = nelmnt.getAttribute("id");
				fcode = nelmnt.getAttribute("frontiercode");
				//au
				mt= Pattern.compile("\\[i:"+ fcode +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
				
				// リストとマッチすればイメージタグに変換
				//au
				rtncmnt = mt.replaceAll("<img localsrc=\"" + inm + "\"/>");
			}	
		}
		
		
		
		// iモード絵文字の変換
		// 絵文字List数分ループ
		/*
		for (int i=0; i < nlist.getLength() ; i++) {
			Element nelmnt = (Element)nlist.item(i);
			inm = nelmnt.getAttribute("id");
			unicodehtml = nelmnt.getAttribute("unicodehtml");
			fcode = nelmnt.getAttribute("frontiercode");
			pcCode = nelmnt.getAttribute(codeStr);
			//docomo
			//mt= Pattern.compile("\\[i:"+ inm +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
			//au,softbank
			//mt= Pattern.compile("\\[i:"+ fcode +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
			//全部
			mt= Pattern.compile("\\[i:"+ pcCode +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
			
			// リストとマッチすればイメージタグに変換
			//docomo,softbank
			rtncmnt = mt.replaceAll(unicodehtml);
			//au
			//rtncmnt = mt.replaceAll("<img localsrc=\"" + inm + "\"/>");
		}
		*/

		return rtncmnt;
	}
	
	
	private static NodeList readXml(String[] emojiXml,String agent,String rootPath){
		NodeList nlist = null;
		Document doc = null;
		
		try {
			// ドキュメントビルダーファクトリを生成
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			// ドキュメントビルダーを生成
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			// パースを実行してDocumentオブジェクトを取得(キャリア毎に使用するxmlを変える)
			if(agent.indexOf("DoCoMo")==0){
				doc = builder.parse(new File(rootPath+emojiXml[0]));
			}else if(agent.indexOf("J-PHONE") == 0 || agent.indexOf("Vodafone") == 0 || agent.indexOf("SoftBank") == 0){
				doc = builder.parse(new File(rootPath+emojiXml[1]));
			}else if(agent.indexOf("UP.Browser") == 0 || agent.indexOf("KDDI") == 0){
				doc = builder.parse(new File(rootPath+emojiXml[2]));
			}			
			// ルート要素を取得
			Element root = doc.getDocumentElement();
			// Listに格納
			nlist = root.getElementsByTagName("entry");
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return nlist;
	}
}
