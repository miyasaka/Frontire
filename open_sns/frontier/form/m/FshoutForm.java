package frontier.form.m;

import java.io.Serializable;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class FshoutForm implements Serializable{
	private static final long serialVersionUID = 1L;
	// 当ページのメンバーID
	public String mid;
	// FShout コメント
	@Required(target="fshout")
	@Maxlength(maxlength=240,target="fshout")
	public String fscomment;
	// 閲覧用 FShout コメント
	public String vfscomment;
	// FShout 相手に確認を求める
	public Integer confirmflg;
	// FShout 公開範囲
	public Integer publevel;
	// Twitterにも投稿
	public Integer twitterflg;
	// FShout メンバーID
	public String fsmid;
	// FShout No
	public Integer fsno;
	// FShout一覧スタート値
	public Integer offset = 0;
	// FShout一覧ページ値
	public Integer pgcnt = 0;
	// FShout要確認Shout数
	public Integer fscommentConfCnt;
	// FShout一覧のページタイプ
	public String fslistpgType;
	// Twitterが利用かどうかチェック用
	public String Chktwitter;

	// ※ここからTwitter系変数
	// RE、RT時の返信元のStatusIDセット用
	public String VVReplySid;
	// ユーザID
	public String userId;
	// ユーザアカウント
	public String myScreenName;
	
	// リセット用メソッド
	// リセット
	public void reset(){
		offset     = 0;
		pgcnt      = 0;
		confirmflg = null;
		publevel   = null;
		twitterflg = null;
	}
	// 全部リセット
	public void resetall(){
		offset           = 0;
		pgcnt            = 0;
		fscommentConfCnt = 0;
		fsmid            = null;
		fsno             = null;
		fscomment        = null;
		vfscomment       = null;
		confirmflg       = null;
		publevel         = null;
		twitterflg       = null;
	}
	// resetmoveリセット(次ページ・前ページリンク押下時のリセット)
	public void resetmove(){
		fscomment        = null;
		vfscomment       = null;
		confirmflg       = null;
		publevel         = null;
		twitterflg       = null;
	}
	// dummyリセット
	public void dummy(){
		// 何もしない
	}
}