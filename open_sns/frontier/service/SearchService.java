package frontier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.SearchForm;

public class SearchService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;
    @Resource
    protected FriendinfoCmnService friendinfoCmnService;
	@Resource
	protected CommonService commonService;
	@Resource
	public UserInfoDto userInfoDto;

	//検索実行
	public List<BeanMap> selAllList(String searchWord,String mid,String checkbox,List<String> groupList,int pgcnt){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("searchword", "*E"+appDefDto.FP_CMN_ALL_SEACH_CHANGE_NUM+",1 " + searchWord);
		params.put("grouplist", groupList);
		params.put("groupcnt", groupList.size());
		params.put("checkbox", checkbox);

		return jdbcManager
		.selectBySqlFile(BeanMap.class, "/data/selSearchAll",params)
		.limit(appDefDto.FP_MY_SEARCHLIST_PGMAX)
		.offset(pgcnt)		
		.getResultList();
	}
	
	//全体件数取得
	public long cntAllList(String searchWord,String mid,String checkbox,List<String> groupList){
		
		//全件検索
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("searchword", "*E"+appDefDto.FP_CMN_ALL_SEACH_CHANGE_NUM+",1 " + searchWord);
		params.put("grouplist", groupList);
		params.put("groupcnt", groupList.size());
		params.put("checkbox", checkbox);
		
		return jdbcManager.getCountBySqlFile("/data/selSearchAll.sql",params);		
	}
	
	//検索ワードの設定処理
	public String makeSql(SearchForm searchForm,String mid){
		
		String[] keywordList = setKeyword(searchForm.searchtext);
		return word(keywordList,searchForm.radioOption);
	}
	
	//半角スペース、全角スペースを区切り文字とする
	private String[] setKeyword(String keyword){
		//前後の空白削除
		String word = keyword.trim();
		//全角スペース->半角スペース
		word = word.replaceAll("　", " ");
		
		//SQL特殊文字の無害化
		/*
		word = word.replaceAll("\\\\", "\\\\\\\\");
		word = word.replaceAll("#", "##");
		word = word.replaceAll("%", "#%");
		word = word.replaceAll("_", "#_");
		word = word.replaceAll("'", "''");
		*/
		
		//Ludiaの特殊文字を無害化
		word = word.replaceAll("\\+", "＋");
		word = word.replaceAll("-", "－");
		word = word.replaceAll("OR", "or");
		word = word.replaceAll("\\*", "＊");
		word = word.replaceAll("\\(", "（");
		word = word.replaceAll("\\)", "）");
		word = word.replaceAll("<", "＜");
		word = word.replaceAll(">", "＞");
		word = word.replaceAll("\"", "”");

		//半角スペース毎に文字を区切る
		String[] keywordList = word.split(" ");
		
		return keywordList;
	}
	
	//入力した検索キーワードとあいまいワードの結合
	private String word(String[] keywordList,String radioOption){
		List<BeanMap> aimaiList = new ArrayList<BeanMap>();
		String sqlWord = "";
		
		for(int i=0;i<keywordList.length;i++){
			if(!keywordList[i].equals("")){
					
				//検索キーワードのあいまいワードを取得する
				aimaiList = selAimaiWord(keywordList[i]);

				if(radioOption.equals("1")){
					//and検索の場合の処理
					//&検索の場合はあいまいワード同士はOR
					if(aimaiList.size()>0){
						sqlWord = sqlWord + "(";
					}
					
					sqlWord = sqlWord + " " + keywordList[i];

					//あいまいワードの数だけ検索条件を増やす
					for(int k=0;k<aimaiList.size();k++){
						sqlWord = sqlWord + " OR " + aimaiList.get(k).get("relationkeyword");
					}
					
					//&検索の場合はあいまいワード同士はOR
					if(aimaiList.size()>0){
						sqlWord = sqlWord + ")";
					}
					
					if(i+1!=keywordList.length){
						if(radioOption.equals("1")){
							//andで繋ぐ
							sqlWord = sqlWord + " + ";	
						}
					}
		
				}else{
					//or検索の場合の処理
					sqlWord = sqlWord + " " + keywordList[i];
					
					//あいまいワードの数だけ検索条件を増やす
					for(int k=0;k<aimaiList.size();k++){
						sqlWord = sqlWord + " " + aimaiList.get(k).get("relationkeyword");
					}					
				}
			}				
		}		
						
		return sqlWord;
	}
	
	//あいまいワード検索
	private List<BeanMap> selAimaiWord(String keyword){
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("keyword", keyword);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selMstSearch",params)
					.getResultList();		
	}
	
	//友達一覧作成
	public List<String> makeGroup(String mid){
		//友達検索
		List<BeanMap> lbm = commonService.getMidList("1",userInfoDto.memberId);
		List<String> ls = new ArrayList<String>();
				
		//自分の友達一覧を作成する。
		for (int i=0;i<lbm.size();i++){
			//友達一覧を作成する。
			ls.add(i, (String)lbm.get(i).get("mid"));
		}
		
		//自分のIDを追加する。
		ls.add(ls.size(),mid);

		return ls;
	}
	
}
