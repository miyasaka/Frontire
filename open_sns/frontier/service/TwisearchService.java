package frontier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import frontier.dto.AppDefDto;
import frontier.common.DecorationUtility;

/**
 * Twitter関連のDB処理を行うクラス。
 *
 * @author H.Saikawa
 * @version 1.0
 */
public class TwisearchService{
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;

	/**
	 * Ludiaで利用可能なように検索文字列を整形する
	 * @param word 入力した検索文字列
	 * @param searchPattern 選択した検索パターン(AND検索またはOR検索)
	 * @return 整形後の文字列
	 */
	public String fixLudiaWord(String word,String searchPattern){
		String fixWord = "";
		DecorationUtility du = new DecorationUtility();
		
		//Ludiaで利用出来るように整形
		fixWord = du.fixLudiaWord(word);
		
		//AND検索用に整形
		if(searchPattern.equals("0")){
			fixWord = du.fixLudiaAndWord(fixWord);
		}
		
		return fixWord;
	}
	
	/**
	 * 入力した条件に該当するツイートを検索する
	 * @param mid FrontierのメンバーID
	 * @param searchWord 検索条件(フリーワード)
	 * @param fromYear 年(from)
	 * @param fromMonth 月(from)
	 * @param fromDay 日(from)
	 * @param toYear 年(to)
	 * @param toMonth 月(to)
	 * @param toDay 日(to)
	 * @param userIdList TwitterのユーザIDを含んだリスト
	 * @param limit 最大取得件数
	 * @param offset 最初に取得する行の位置
	 * @return 検索結果
	 */
	public List<BeanMap> selSearchTwitter(String mid,String searchWord,String fromYear,String fromMonth,String fromDay,String toYear,String toMonth,String toDay,List<String> userIdList,Integer limit,Integer offset){
		Map<String,Object> params = new HashMap<String,Object>();
		params = setSearchConditionTweet(mid,searchWord,fromYear,fromMonth,fromDay,toYear,toMonth,toDay,userIdList);

		return jdbcManager.selectBySqlFile(BeanMap.class,"data/bat/selSearchTwitter.sql",params).limit(limit).offset(offset).getResultList();
	}

	/**
	 * 検索条件をMapに設定
	 * @param mid FrontierのメンバーID
	 * @param searchWord 検索条件(フリーワード)
	 * @param fromYear 年(from)
	 * @param fromMonth 月(from)
	 * @param fromDay 日(from)
	 * @param toYear 年(to)
	 * @param toMonth 月(to)
	 * @param toDay 日(to)
	 * @param userIdList TwitterのユーザIDを含んだリスト
	 * @return
	 */
	private Map<String,Object> setSearchConditionTweet(String mid,String searchWord,String fromYear,String fromMonth,String fromDay,String toYear,String toMonth,String toDay,List<String> userIdList){
		Map<String,Object> params = new HashMap<String,Object>();
		if(!searchWord.equals("")){
			params.put("searchword", "*E"+appDefDto.FP_CMN_ALL_SEACH_CHANGE_NUM+",1 " + searchWord);
		}else{
			params.put("searchword",searchWord);
		}

		//ユーザIDの設定
		if(userIdList != null && userIdList.size()!=0){
			//ユーザIDのチェックボックス選択あり
			params.put("searchUserId", userIdList);
		}

		return params;
	}
}
