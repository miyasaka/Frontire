package frontier.dto;

public class AppDefDto{
	/*-----------------------------------------*/
	// ■ 命名規則
	// パラメタ名 ：
	// Frontier Parameter の略でFP_をつける
	// その後にシステム毎の短縮名称を付ける コミュニティ -> FP_COM_XX
	/*-----------------------------------------*/
	// ■ 共通系(CMN)
		// ホスト名
		public String FP_CMN_HOST_NAME = "wadati.charlie-s.jp";
		// ロゴ画像パス(フルパス)
		public String FP_CMN_LOGOIMG_URL = "http://wadati.charlie-s.jp/images/logo.gif";
		// QR画像パス
		public String FP_CMN_QRIMG = "";
		// 画像やxmlなどのrootパス(内部参照時)
		public String FP_CMN_INNER_ROOT_PATH ="/home/tomcat/www/";
		// スタイルのパス
		public String FP_CMN_COLOR_TYPE = "black";
		// ImageMagick本体パス
		public String FP_CMN_IMAGEMAGICK_DIR = "";
		// 画像ＵＰ時のディレクトリパス
		public String FP_CMN_CONTENTS_DIR  = "/home/tomcat/www/contents/";
		// 画像参照時のルートパス
		public String FP_CMN_CONTENTS_ROOT = "/contents/";
		// i-mode絵文字のxmlパス
		public String FP_CMN_EMOJI_XML_PATH = "//hawaii/tomcat/www/static/xml/emojimap.xml";
		// i-mode絵文字の画像パス
		public String FP_CMN_EMOJI_IMG_PATH = "/images/emoji/";
		// 日記画像格納パス（前半部分）
		public String FP_CMN_DIARY_BEFORE_PATH = "/img/mem/";
		// 日記画像格納パス（後半部分）
		public String FP_CMN_DIARY_AFTER_PATH = "/diary/dir/";
		// フォトアルバム画像格納パス（後半部分）
		public String FP_CMN_PHOTO_AFTER_PATH = "/photo/dir/";
		// 一覧系画面で表示する本文の文字数
		public Integer FP_CMN_LIST_CMNTMAX = 100;
		// ページングのMAX数[1 2 3  ・・・ 10 11 12]
		public Integer FP_CMN_PAGENG_MAX = 10;
		// メンバー一覧画面で表示するMAX数
		public Integer FP_CMN_LIST_MEMMAX = 30;
		// コミュニティ一覧画面で表示するMAX数
		public Integer FP_CMN_LIST_COMMAX = 30;
		// リクエスト確認で表示するMAX数
		public Integer FP_CMN_LIST_REQMAX = 5;
		// 日記の公開権限
		public String[] FP_CMN_DIARY_AUTH1 = {"1","全体に公開"};
//		public String[] FP_CMN_DIARY_AUTH2 = {"2","友人の友人"};
		public String[] FP_CMN_DIARY_AUTH3 = {"2","グループに公開"};
		public String[] FP_CMN_DIARY_AUTH4 = {"9","非公開"};
		// RSS用xmlファイルのディレクトリパス
		public String FP_CMN_RSS_DIR  = "/home/tomcat/www/contents/rss";
		// RSS用xmｌファイルのファイル名
		public String FP_CMN_RSS_FRONTIERNET_XML  = "frontiernetRss.xml";
		public String FP_CMN_RSS_OUTSIDE_XML  = "outsideRss.xml";
		// Frontier Net公開RSSに表示するタイトル
		public String FP_CMN_FRONTIER_NAME = "hawaii Frontier";
		// RSSするxmlファイルに出力する件数
		public Integer FP_CMN_RSS_LISTMAX = 30;
		// ファイルの公開権限
		public String[] FP_CMN_FILE_AUTH_OPEN  = {"1","全体に公開"};
		public String[] FP_CMN_FILE_AUTH_CLOSE = {"9","非公開"};
		// ファイル登録種別
		public String FP_CMN_FILE_REGIST_NEW  = "0";
		public String FP_CMN_FILE_REGIST_EDIT = "1";
		// ファイルUPLOAD時のディレクトリパス
		public String FP_CMN_FILE_DIR = "file/";
		// ファイルUPLOAD時のサイズ下限・上限
		public Integer FP_CMN_FILESIZE_MIN = 0;
		public Integer FP_CMN_FILESIZE_MAX = 999999999;
		// ファイル名の区切り文字
		public String FP_CMN_FILENAME_DELIMITER = "_";
		// ファイル管理画像パス
		public String FP_CMN_FILE_IMAGE_PATH = "/images/file/";
		// FShout一覧で表示する本文の文字数
		public Integer FP_CMN_FSLIST_CMNTMAX = 20;
		// お知らせ一覧で表示するお知らせの数
		public Integer FP_CMN_LIST_NEWSMAX = 30;
		// グループ登録で表示するグループメンバー一覧の数
		public Integer FP_GRP_LIST_MAX = 20;
		// RSSメンバー登録で表示するRSSメンバー一覧の数
		public Integer FP_RSS_MEMBER_LIST_MAX = 20;
		// 画像サイズ
		public Integer[] FP_CMN_PIC_DIRS = {640,480,240,180,120,76,60,42};
		// 全文検索での部分一致への切り替え用の検索数
		//　検索結果がこの数以下の場合は部分一致に切り替わる
		public String FP_CMN_ALL_SEACH_CHANGE_NUM = "2000";
		//■■■■■■■■■■■■■■■■■■■■■■■■ モバイル版 ■■■■■■■■■■■■■■■■■■■■■■■■
		//絵文字変換xmlのパス
		public String[] FP_CMN_M_EMOJI_XML = {"static/xml/emojimap_docomo.xml",
											  "static/xml/emojimap_softbank.xml",
											  "static/xml/emojimap_au.xml"};

	// ■ マイページ系(MY)
		// トップページで表示する一覧のMAX数
		public Integer FP_MY_LIST_PGMAX = 30;
		// トップページで表示する一覧を過去何日分取得するか  ※※ app.dicon管理 ※※
		public Integer FP_MY_DATE_PGMAX = 90;
		// 日記の一覧画面で１ページに表示するMAX数
		public Integer FP_MY_DIARYLIST_PGMAX = 20;
		// フォトアルバムの一覧画面で１ページに表示するMAX数
		public Integer FP_MY_PHOTOALBUMLIST_PGMAX = 10;
		// フォトアルバム閲覧の一覧に表示する写真の最大数
		public Integer FP_MY_PHOTO_PGMAX = 15;
		// 足跡画面の1ページ最大件数
		public Integer FP_MY_HISTORYLIST_PGMAX = 30;
		// 足跡画面全体の最大表示件数
		public Integer FP_MY_HISTORYLIST_ALLMAX = 60;
		// メンバー最新日記一覧で表示する日記の最大件数
		public Integer FP_MY_NEWDIARY_LIST_PGMAX = 30;
		// 参加コミュニティ最新書き込み一覧で表示するﾄﾋﾟｯｸの日付範囲
		public Integer FP_MY_NEWCOMMUNITY_LIST_PGMAX = 30;
		// 最近書き込みしたコミュニティ一覧で表示するﾄﾋﾟｯｸまたはイベントの最大件数
		public Integer FP_MY_NEWCOMMUNITY_COMMENT_LIST_PGMAX = 30;
		// 最近コメントした日記一覧で表示するﾄﾋﾟｯｸの日付範囲
		public Integer FP_MY_NEWDIARY_COMMENT_LIST_PGMAX = 30;
		// メンバー更新情報一覧で表示する最新フォトの最大件数
		public Integer FP_MY_NEWPHOTO_LIST_PGMAX = 30;
		// あなたの更新履歴一覧で表示する最新フォトの日付範囲
		public Integer FP_MY_UPDATEPHOTO_LIST_PGMAX = 10;
		// あなたの更新履歴一覧で表示する最新日記の日付範囲
		public Integer FP_MY_UPDATEDIARY_LIST_PGMAX = 10;
		// カレンダーの開始年
		public Integer FP_MY_CALENDAR_START_PGMAX = 1912;
		// カレンダーの終了年
		public Integer FP_MY_CALENDAR_END_PGMAX = 2030;
		// 検索結果一覧画面で１ページに表示するMAX数
		public Integer FP_MY_SEARCHLIST_PGMAX = 20;
		// ファイル一覧画面で１ページに表示するMAX数
		public Integer FP_MY_FILELIST_PGMAX = 30;
		// トップページで表示するF Shout一覧を過去何日分取得するか 
		public Integer FP_MY_FSHOUTLIST_DATE_PGMAX = 7;
		// FShout一覧画面で１ページに表示するMAX数
		public Integer FP_MY_FSHOUTLIST_PGMAX = 20;
		// 外部FrontierユーザのTOPページで表示する日記一覧件数
		public Integer FP_MY_FNET_DIARY_PGMAX = 30;
		//■■■■■■■■■■■■■■■■■■■■■■■■ モバイル版 ■■■■■■■■■■■■■■■■■■■■■■■■
		// FShoutを表示するMAX数
		public Integer FP_MY_M_FSHOUT_PGMAX = 5;
		// 同志日記を表示するMAX数
		public Integer FP_MY_M_MEMDIARY_PGMAX = 5;
		// ｽｹｼﾞｭｰﾙを表示するMAX数
		public Integer FP_MY_M_SCHEDULE_PGMAX = 2;
		// ｽｹｼﾞｭｰﾙの一覧を表示するMAX数
		public Integer FP_MY_M_SCHEDULELIST_PGMAX = 7;
		// 同志最新日記を表示するMAX数
		public Integer FP_MY_M_MEMDIARYLIST_PGMAX = 20;
		// 日記の一覧画面で１ページに表示するMAX数
		public Integer FP_MY_M_DIARYLIST_PGMAX = 10;
		// F Shout一覧画面で１ページに表示するMAX数
		public Integer FP_MY_M_FSHOUTLIST_PGMAX = 10;

	// ■ メンバー系(MEM)
		// メンバートップ画面で表示するメンバー、コミュニティ一覧のMAX数
		public Integer FP_MEM_TOPLIST_PGMAX = 9;
		// メンバートップ画面で表示する最近の日記一覧のMAX数
		public Integer FP_MEM_NEWDIARY_PGMAX = 5;
		// メンバートップ画面で表示する最近のアルバム一覧のMAX数
		public Integer FP_MEM_NEWALBUM_PGMAX = 5;
		// メンバートップ画面で表示するF Shout一覧のMAX数
		public Integer FP_MEM_FSHOUT_PGMAX = 10;

	// ■ コミュニティ系(COM)
		// コミュニティトップ画面で表示する新着一覧系のMAX数
		public Integer FP_COM_BBSLIST_PGMAX = 5;
		// コミュニティトップ画面で表示するメンバー、コミュニティ一覧のMAX数
		public Integer FP_COM_TOPLIST_PGMAX = 9;
		// 参加メンバー一覧画面で表示するMAX数
		public Integer FP_COM_LIST_MEMMAX = 10;
		// トピック・イベント閲覧画面で表示するコメントのMAX数
		public Integer FP_COM_BBSLIST_CMNTMAX = 20;
		// イベント参加メンバー一覧画面で表示するMAX数
		public Integer FP_COM_EVENTLIST_MEMMAX = 50;
		// トピック・イベント一覧画面で表示するMAX数
		public Integer FP_COM_TOPICLIST_PGMAX = 20;
		
	// ■ Twitter系(TWI)
		// Callbackの遷移先
		public String TWI_RETURN_TO = "/frontier/pc/profile1/";
		// 使用するTwitterアプリの名前
		public String TWI_PROVIDER = "frontier_tweet";
		// dummy用のパスワード
		public String ST_DUMMY_PASSWORD = "dummy";
		// Callbackの遷移先
		public String ST_RETURN_TO = "/frontier/pc/login/";
		public String ST_PROVIDER = "frontier_tweet";
		// 1ページの最大TL表示件数
		public Integer ST_MY_TL_LIST_MAX = 20;
		// 最大取得件数
		public Integer ST_LIST_MAX = 200;
		// Twitterの制限文字数
		public Integer TWI_MAXCOMMENT = 140;
		//バッチでの一覧最大取得件数
		public Integer BAT_TL_LIST_MAX = 200;

	// ■ bit.ly系(BIT)
		// ユーザID
		public String BIT_USER_ID = "charlietest369";
		// APIキー
		public String BIT_API_KEY = "R_226f0e7509dda4a0087050091421b5ff";


}