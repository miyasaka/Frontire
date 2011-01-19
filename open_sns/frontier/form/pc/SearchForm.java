package frontier.form.pc;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class SearchForm implements Serializable {
    private static final long serialVersionUID = 1L;
	
	public String searchtext;
	public String mydtonly;
	//検索結果一覧画面の検索テキストボックス
	public String search;
	public String searchchk;
	public String radioOption;
	//改ページ用の変数
    public int pgcnt;
    public int offset;
	
    public void reset(){
    	mydtonly = null;
    	searchchk = null;
    	pgcnt = 0;
    	offset = 0;
    	radioOption = "1";
    }
    
    //何もしないダミーメソッド
    //resetされたくない場合に使用する。
    public void dummy(){
    	
    }
}
