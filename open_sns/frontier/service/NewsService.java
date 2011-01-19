package frontier.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.OrderByItem;
import org.seasar.extension.jdbc.OrderByItem.OrderingSpec;

import frontier.entity.Information;
import frontier.form.pc.NewsForm;



public class NewsService {
	
	@Resource
	protected JdbcManager jdbcManager;
	
	@Resource
	protected NewsForm newsForm;
	
	//お知らせ一覧検索
	public List<Information> selNewsList(Integer offset,Integer limit){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("offset",offset);
		params.put("limit",limit);
			
		return jdbcManager.from(Information.class).offset(offset).limit(limit).orderBy(new OrderByItem("dispdate", OrderingSpec.DESC),new OrderByItem("entdate", OrderingSpec.DESC),new OrderByItem("id", OrderingSpec.DESC)).getResultList();
		
//		return jdbcManager
//				.selectBySqlFile(BeanMap.class,"/data/selNewsList.sql",params).getResultList();
	}
	
	//トップページで表示するお知らせデータ
	public List<BeanMap> selNewsListTop(){
		return jdbcManager.selectBySqlFile(BeanMap.class,"/data/selNewsListTop.sql").getResultList();
	}
	
	//お知らせ閲覧
	public Information selNews(Integer NewsId){
		
		return jdbcManager.from(Information.class).where("id = ?",NewsId).getSingleResult();
		
	}
	
	//お知らせ一覧件数
	public long cntNewsList(){
		return jdbcManager
		.getCountBySqlFile("/data/selNewsList.sql");
	}
	//設定する
	Logger logger = Logger.getLogger(this.getClass().getName());
	public void updNewsDispSetting(List<String> chkList,String mid,Integer offset,Integer limit){
		//変数初期化
		List<Information> paramList = new ArrayList<Information>();
		int id;
		//お知らせ一覧取得
		for(Information i:selNewsList(offset,limit)){
			Information info = new Information();
			info.id = i.id;
			info.topflg = "0";
			info.updid = mid;
			info.upddate = new Timestamp ((new java.util.Date()).getTime());
			paramList.add(info);
		}
		//表示設定を0に更新
		jdbcManager.updateBatch(paramList).includes("topflg","updid","upddate").execute();
		
		//チェックボックスにチェックが入っている場合の処理
		if(chkList.size()>0){
			//初期化
			paramList = new ArrayList<Information>();
			
			for(int i = 0;i<chkList.size();i++){
				Information info = new Information();
				//キー設定
	
				id =  new Integer(chkList.get(i));
				info.id =id;
				info.topflg = "1";
				info.updid = mid;
				info.upddate = new Timestamp ((new java.util.Date()).getTime());
				paramList.add(info);
			}
			//表示設定を1に更新
			jdbcManager.updateBatch(paramList).includes("topflg","updid","upddate").execute();
		}
	}
	//お知らせ更新
	public void updNews(String mid,NewsForm f){
		Information info = new Information();
		//変数セット
		info.id=f.newsId;
		info.title=f.title;
		info.comment=f.detail;
		info.dispdate=f.year+f.month+f.day;
		info.topflg=f.topflg;
		info.upddate=new Timestamp ((new java.util.Date()).getTime());
		info.updid=mid;
		//更新
		jdbcManager.update(info).excludes("id","entid","entdate").execute();
		
	}
	
	//お知らせ登録
	public void insNews(String mid,NewsForm f){
		Information info = new Information();
		Object oid = jdbcManager.selectBySqlFile(BeanMap.class,"/data/selMaxNewsId").getSingleResult().get("newsid");
		//変数セット
		info.id=Integer.parseInt(oid.toString());
		info.title=f.title;
		info.comment=f.detail;
		info.dispdate=f.year+f.month+f.day;
		info.topflg=f.topflg;
		info.upddate=new Timestamp ((new java.util.Date()).getTime());
		info.updid=mid;
		info.entdate=new Timestamp ((new java.util.Date()).getTime());
		info.entid=mid;
		//登録
		jdbcManager.insert(info).execute();
		
	}
	
	//お知らせ削除
	public void delNews(Integer id){
		logger.debug("削除処理");
		Information info = new Information();
		info = selNews(id);
		if(info!=null) jdbcManager.delete(info).execute();
	}
	
	

}
