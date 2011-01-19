package frontier.action.bat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.arnx.jsonic.JSONException;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.entity.OauthTokensStore;
import frontier.entity.TwitterPostManagement;
import frontier.service.OauthConsumerService;
import frontier.service.TwitterService;

public class GettwitterbatAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	private TwitterService twitterService;
	@Resource
	private OauthConsumerService oauthConsumerService;
	@Resource
	private AppDefDto appDefDto;
	
	public HttpServletRequest hsr;
	//実行する処理を判定するフラグ
	public String exeFlg;
	
	// ==================== //
	//   -- メイン処理 --   //
	// ==================== //
	// ---------------------------------------------- //
	// ■自Frontier -> 他Frontier 処理                //
	//   自分のホストへリクエストがあった場合         //
	//   Frontier Netのデータを取得して更新処理開始   //
	// ---------------------------------------------- //
	@Execute(validator=false,urlPattern="index/{exeFlg}")
	public String index() {
		String reFQDN = "";
		String myFQDN = "";

		// localhost:xxxxへリクエストを投げて処理開始(自サーバ内処理)
		if(hsr != null){
			// 自サーバのIP取得(リクエストを受け取ったIPアドレス)
			myFQDN = hsr.getLocalAddr();
			// リクエスト元のIP取得
			reFQDN = hsr.getRemoteAddr();
		}

		// 値が取得できていて、リクエスト元と自サーバのIPが同じならば処理開始
		// (本当に自分のサーバで投げられたリクエスト(localhost)か確認)
		if(!myFQDN.equals("") && !reFQDN.equals("") && myFQDN.equals(reFQDN)){
			
			if(exeFlg.equals("1")){
				//未取得の自分の発言をtwitterより取得する
				exeMyPost();				
			}else if (exeFlg.equals("2")){
				//未取得のメンションをtwitterより取得する
				exeMention();
			}else if (exeFlg.equalsIgnoreCase("3")){
				//未取得のお気に入りをTwitterより取得する
				exeFavorite();
			}else if (exeFlg.equals("4")){
				//シーケンシャルに全処理を実行する場合
				
				//未取得の自分の発言をtwitterより取得する
				exeMyPost();				
				
				//未取得のメンションをtwitterより取得する
				exeMention();

				//未取得のお気に入りをTwitterより取得する
				exeFavorite();			
			}
		}
		
		return "index.jsp";
	}
	
	//未取得の自分の発言をtwitterより取得する
	private void exeMyPost(){
		//Frontierに登録されているtwitterのユーザ情報を取得する
		List<OauthTokensStore> lots = twitterService.selAllTwitterUser(null,appDefDto.TWI_PROVIDER,"0",null,null,null);
		TwitterUtility tu = new TwitterUtility();
		//実行中のユーザを格納する変数
		List<Integer> userLs = new ArrayList<Integer>();
		
		//取得したユーザの数だけ処理を繰り返す
		for(int i=0;i<lots.size();i++){
			
			if(userLs.contains(lots.get(i).twituserid)){
				//既に処理済みのユーザの場合は後続処理は実施しない
				continue;
			}else{
				//実行中のユーザをリストに追加
				userLs.add(lots.get(i).twituserid);				
			}			
			
			//認証処理
			Twitter twitter = twitterService.setTwitter(null,lots.get(i).accessToken,lots.get(i).tokenSecret,null);
			//twitterから取得した発言一覧格納用
			List<Status> ls = new ArrayList<Status>();
			//登録済みの最小のステータスIDを取得する
			long minStatusId = twitterService.selMinStatusId(lots.get(i).twituserid);
			
			long sId = 0L;

			while(true){
			
				//発言登録を終了するためのフラグ
				String breakFlg = "0";
				
				try{
					if(sId > 0){
						//twitterからステータスID以前のものを取得する
						ls = tu.getUTimelineOld(twitter, lots.get(i).screenname, sId, 1, appDefDto.BAT_TL_LIST_MAX);
					}else{
						//twitterから条件なしで発言を取得する
						ls = tu.getUTimeline(twitter, lots.get(i).screenname, 1, appDefDto.BAT_TL_LIST_MAX);
					}
				} catch (TwitterException e) {
					//エラー原因を取得
					String errCd = tu.getErrorTwitter(e);
					//ログ出力
					checkErr(lots.get(i).twituserid.toString(), errCd);
					e.printStackTrace();						
					break;
				}
				
				//twitterからデータ取得がない場合、処理終了
				if(ls.size()==0){
					break;
				}
				
				for(int j=0;j<ls.size();j++){
					//Frontier DBへの格納処理
					if(twitterService.cntTwitter(ls.get(j).getId())==0){
						//未登録の場合はtwitter_dbへデータ登録
						twitterService.insTwitter(ls.get(j), "system");
					}
					
					//発言のtwitter_post_managementでのデータ状態を取得
					List<TwitterPostManagement> ltpm = twitterService.selTwitterPostManagement(ls.get(j).getId(),lots.get(i).twituserid,null,null,null,null,1,0);

					if(ltpm.size()==0){
						//未登録の場合はtwitter_post_managementへデータ登録
						twitterService.insTwitterPostManagement(ls.get(j).getId(), lots.get(i).twituserid, "0", "0", "1", "0", "0", "system","0");				
					}else if(ltpm.get(0).postflg.equals("0")){
						//登録済の場合はtwitter_post_managementへデータ更新
						twitterService.updTwitterPostManagement(ls.get(j).getId(), lots.get(i).twituserid, null, null, "1", null, null, "system",null, 1, 0);						
					}else{
						//バッチで登録済みの場合
						breakFlg = "1";
						break;
					}
					
					sId = ls.get(j).getId();
					
					if(minStatusId==sId){
						//バッチでの処理は初めてだが既に画面からの操作で登録済みのものが存在する場合
						//過去に遡って全ての発言を取得するには時間がかかり過ぎる
						breakFlg = "1";
						break;						
					}
				}

				if (breakFlg.equals("1")){
					//登録するものがなくなれば処理終了
					break;
				//}else if(ltb.size()==0){
				}else if(minStatusId==-1){
					//バッチ実行時にFrontierのDBには発言が存在しなかった場合
					//過去に遡って全ての発言を取得するには時間がかかり過ぎる
					break;
				}
				
			}
			
		}
		
		return;
	}
	
	//未取得の自分の発言をtwitterより取得する
	private void exeMention(){
		//Frontierに登録されているtwitterのユーザ情報を取得する
		List<OauthTokensStore> lots = twitterService.selAllTwitterUser(null,appDefDto.TWI_PROVIDER,"0",null,null,null);
		TwitterUtility tu = new TwitterUtility();
		//実行中のユーザを格納する変数
		List<Integer> userLs = new ArrayList<Integer>();
		
		//取得したユーザの数だけ処理を繰り返す
		for(int i=0;i<lots.size();i++){
			
			if(userLs.contains(lots.get(i).twituserid)){
				//既に処理済みのユーザの場合は後続処理は実施しない
				continue;
			}else{
				//実行中のユーザをリストに追加
				userLs.add(lots.get(i).twituserid);				
			}			
			
			//認証処理
			Twitter twitter = twitterService.setTwitter(null,lots.get(i).accessToken,lots.get(i).tokenSecret,null);
			//twitterから取得した発言一覧格納用
			List<Status> ls = new ArrayList<Status>();
			//登録済みの最小のステータスIDを取得する
			long minStatusId = twitterService.selMinStatusId(lots.get(i).twituserid);

			long sId = 0L;

			while(true){
			
				//発言登録を終了するためのフラグ
				String breakFlg = "0";
				
				try{
					if(sId > 0){
						//twitterからステータスID以前のものを取得する
						ls = tu.getMentionsOld(twitter, sId, 1, appDefDto.BAT_TL_LIST_MAX);
					}else{
						//twitterから条件なしで発言を取得する
						ls = tu.getMentions(twitter, 1, appDefDto.BAT_TL_LIST_MAX);
					}
				}catch(TwitterException e){
					//エラー原因を取得
					String errCd = tu.getErrorTwitter(e);
					//ログ出力
					checkErr(lots.get(i).twituserid.toString(), errCd);
					e.printStackTrace();						
					break;
				}
				
				//twitterからデータ取得がない場合、処理終了
				if(ls.size()==0){
					break;
				}
				
				for(int j=0;j<ls.size();j++){
					//Frontier DBへの格納処理
					if(twitterService.cntTwitter(ls.get(j).getId())==0){
						//未登録の場合はtwitter_dbへデータ登録
						twitterService.insTwitter(ls.get(j), "system");
					}
					
					//発言のtwitter_post_managementでのデータ状態を取得
					List<TwitterPostManagement> ltpm = twitterService.selTwitterPostManagement(ls.get(j).getId(),lots.get(i).twituserid,null,null,null,null,1,0);

					if(ltpm.size()==0){
						//未登録の場合はtwitter_post_managementへデータ登録
						twitterService.insTwitterPostManagement(ls.get(j).getId(), lots.get(i).twituserid, "0", "0", "0", "1", "0", "system","1");				
					}else if(ltpm.get(0).mentionflg.equals("0")){
						//登録済の場合はtwitter_post_managementへデータ更新
						twitterService.updTwitterPostManagement(ls.get(j).getId(), lots.get(i).twituserid, null, null, null, "1", null, "system","1", 1, 0);						
					}else{
						breakFlg = "1";
						break;
					}
					
					sId = ls.get(j).getId();
					
					if(minStatusId==sId){
						//バッチでの処理は初めてだが既に画面からの操作で登録済みのものが存在する場合
						//過去に遡って全ての発言を取得するには時間がかかり過ぎる
						breakFlg = "1";
						break;						
					}
				}

				if (breakFlg.equals("1")){
					//登録するものがなくなれば処理終了
					break;
				}else if(minStatusId==-1){
					//バッチ実行時にFrontierのDBには発言が存在しなかった場合
					//過去に遡って全ての発言を取得するには時間がかかり過ぎる
					break;
				}
				
			}
			
		}
		
		return;
	}
	
	/**
	 * 未取得のお気に入りをtwitterより取得する
	 */
	private void exeFavorite(){
		//Frontierに登録されているtwitterのユーザ情報を取得する
		List<OauthTokensStore> lots = twitterService.selAllTwitterUser(null,appDefDto.TWI_PROVIDER,"0",null,null,null);
		TwitterUtility tu = new TwitterUtility();
		//実行中のユーザを格納する変数
		List<Integer> userLs = new ArrayList<Integer>();
		
		//コンシューマ情報を取得
		List<BeanMap> lbmConsumer = oauthConsumerService.getConsumerInfo(appDefDto.TWI_PROVIDER);
		
		//取得したユーザの数だけ処理を繰り返す
		for(int i=0;i<lots.size();i++){
			
			if(userLs.contains(lots.get(i).twituserid)){
				//既に処理済みのユーザの場合は後続処理は実施しない
				continue;
			}else{
				//実行中のユーザをリストに追加
				userLs.add(lots.get(i).twituserid);				
			}
			
			//認証処理
			Twitter twitter = twitterService.setTwitter(null,lots.get(i).accessToken,lots.get(i).tokenSecret,null);
			//twitterから取得した発言一覧格納用
			List<Status> ls = new ArrayList<Status>();
			//登録済みの最小のステータスIDを取得する
			long minStatusId = twitterService.selMinStatusId(lots.get(i).twituserid);

			//twitterから取得したお気に入り一覧格納用(2回目以降)
			List<Map<String, String>> lmss = new ArrayList<Map<String,String>>();
			
			long sId = 0L;

			while(true){
			
				//発言登録を終了するためのフラグ
				String breakFlg = "0";
				
				try{
					if(sId > 0){
						//ループの２回目以降の実行の場合
						
						//twitterからステータスID以前のものを取得する
						lmss = tu.getFavoriteOld(lots.get(i).screenname, sId, appDefDto.TWI_PROVIDER, lbmConsumer,lots.get(i));

						//twitterからデータ取得がない場合、処理終了
						if(lmss.size()==0){
							break;
						}
					
						for(int j=0;j<lmss.size();j++){
							//IDを抽出する。BigDecimalからlong型へ変換不能のため
							Object objId = lmss.get(j).get("id");
							long id = Long.parseLong(objId.toString());
							
							//Frontier DBへの格納処理
							if(twitterService.cntTwitter(id)==0){
								try{
									//未登録の場合はtwitter_dbへデータ登録
									twitterService.insTwitter(lmss.get(j), "system");
								}catch(Exception e){
									Object objUser = lmss.get(i).get("user");
									Map<String,String> mss = (Map<String, String>) objUser;
									logger.fatal("FAVORITE INSERT ERROR!!");
									logger.fatal("ERROR TWEET " + id);
									logger.fatal("ERROR MAKE TWEET USER " + mss.get("screen_name"));
									//insertでエラーが発生した場合
									breakFlg = "1";
									break;
								}
							}
							
							//発言のtwitter_post_managementでのデータ状態を取得
							List<TwitterPostManagement> ltpm = twitterService.selTwitterPostManagement(id,lots.get(i).twituserid,null,null,null,null,1,0);

							if(ltpm.size()==0){
								//未登録の場合はtwitter_post_managementへデータ登録
								twitterService.insTwitterPostManagement(id, lots.get(i).twituserid, "0", "1", "0", "0", "1", "system","0");				
							}else if(ltpm.get(0).batchfavoriteflg.equals("0")){
								//登録済の場合はtwitter_post_managementへデータ更新
								twitterService.updTwitterPostManagement(id, lots.get(i).twituserid, null, "1", null, null, "1", "system",null, 1, 0);						
							}else{
								breakFlg = "1";
								break;
							}
							
							sId = id;
							
							if(minStatusId==sId){
								//バッチでの処理は初めてだが既に画面からの操作で登録済みのものが存在する場合
								//過去に遡って全ての発言を取得するには時間がかかり過ぎる
								breakFlg = "1";
								break;						
							}
						}
					}else{
						//ループの１回目の実行の場合
						
						//twitterから条件なしで発言を取得する
						ls = tu.getFavorite(twitter, lots.get(i).screenname, 1);

						//twitterからデータ取得がない場合、処理終了
						if(ls.size()==0){
							break;
						}
						
						for(int j=0;j<ls.size();j++){
							//Frontier DBへの格納処理
							if(twitterService.cntTwitter(ls.get(j).getId())==0){
								try{
									//未登録の場合はtwitter_dbへデータ登録
									twitterService.insTwitter(ls.get(j), "system");
								}catch(Exception e){
									logger.fatal("FAVORITE INSERT ERROR!!");
									logger.fatal("ERROR TWEET " + ls.get(j).getId());
									logger.fatal("ERROR MAKE TWEET USER " + ls.get(j).getUser().getScreenName());
									//insertでエラーが発生した場合
									breakFlg = "1";
									break;
								}
							}
							
							//発言のtwitter_post_managementでのデータ状態を取得
							List<TwitterPostManagement> ltpm = twitterService.selTwitterPostManagement(ls.get(j).getId(),lots.get(i).twituserid,null,null,null,null,1,0);

							if(ltpm.size()==0){
									//未登録の場合はtwitter_post_managementへデータ登録
									twitterService.insTwitterPostManagement(ls.get(j).getId(), lots.get(i).twituserid, "0", "1", "0", "0", "1", "system","0");
							}else if(ltpm.get(0).batchfavoriteflg.equals("0")){
								//登録済の場合はtwitter_post_managementへデータ更新
								twitterService.updTwitterPostManagement(ls.get(j).getId(), lots.get(i).twituserid, null, "1", null, null, "1", "system",null, 1, 0);						
							}else{
								breakFlg = "1";
								break;
							}
							
							sId = ls.get(j).getId();
							
							if(minStatusId==sId){
								//バッチでの処理は初めてだが既に画面からの操作で登録済みのものが存在する場合
								//過去に遡って全ての発言を取得するには時間がかかり過ぎる
								breakFlg = "1";
								break;						
							}
						}
					}
				}catch(TwitterException e){
					//エラー原因を取得
					String errCd = tu.getErrorTwitter(e);
					//ログ出力
					checkErr(lots.get(i).twituserid.toString(), errCd);
					e.printStackTrace();						
					break;
				} catch (JSONException e) {
					//ログ出力
					e.printStackTrace();
					break;
				} catch (IOException e) {
					//ログ出力
					e.printStackTrace();
					break;
				}

				if (breakFlg.equals("1")){
					//登録するものがなくなれば処理終了
					break;
				}else if(minStatusId==-1){
					//バッチ実行時にFrontierのDBには発言が存在しなかった場合
					//過去に遡って全ての発言を取得するには時間がかかり過ぎる
					break;
				}
				
			}
			
		}
		
		return;
	}	
	
	/**
	 * エラー時のログ出力
	 * @param userid エラーが発生した際に処理中のユーザ
	 * @param errCd エラーコード
	 */
	private void checkErr(String userid,String errCd){
		if(errCd.equals("")){
			logger.fatal("AUTH OR APPLICATION ERROR!!");			
		}else if (errCd.equals("1")){
			logger.fatal("TWITTER SERVER ERROR!!");			
		}else if (errCd.equals("2")){
			logger.fatal("API LIMIT OVER!!");			
		}else if (errCd.equals("3")){
			logger.fatal("NO POST ERROE!!");						
		}
		
		logger.fatal("Twitter USER_ID IS " + userid);
	}
}
