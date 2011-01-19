package frontier.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.h2.util.StringUtils;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.PhotoComment;
import frontier.form.pc.PhotoForm;


public class PhotoService {

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected JdbcManager jdbcManager;

	//フォトアルバム一覧検索
	public List<BeanMap> selPhotoList(String mid,int offset){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("limit", appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX);
		//改ページテスト用
		//params.put("limit", 2);
		params.put("offset", offset);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPhotoAlbumList",params)
					.getResultList();
		
	}

	//フォトアルバムの総数取得
	public long cntPhotoList(String mid){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		
		return jdbcManager.getCountBySqlFile("/data/selPhotoAlbumList", params);
	}
	
	//フォトアルバムの表紙検索
	public List<BeanMap> selPhotoCover(String mid,Integer ano){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("ano", ano);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPhotoAlbumCover",params)
					.getResultList();
		
	}
	
	//フォトアルバム閲覧の個別の写真検索
	public List<BeanMap> selPhoto(String mid,Integer ano,int offset){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("ano", ano);
		params.put("limit", appDefDto.FP_MY_PHOTO_PGMAX);
		//改ページテスト用
		//params.put("limit", 5);
		params.put("offset", offset);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPhotoList",params)
					.getResultList();
		
	}
	
	//フォトアルバム閲覧の個別の写真総数検索
	public long cntPhoto(String mid,Integer ano){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("ano", ano);
		
		//SQL実行
		return jdbcManager.getCountBySqlFile("/data/selPhotoList", params);
		
	}
	
	/**
	 * フォトアルバムのコメント検索
	 * @param photoForm
	 * @param cnoList
	 * @return List<BeanMap>
	 */
	public List<BeanMap> selPhotoComment(PhotoForm photoForm, List<String> cnoList){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid",      photoForm.pmid);
		params.put("ano",      photoForm.pano);
		params.put("cno",      cnoList);
		params.put("memberId", userInfoDto.memberId);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPhotoCommentList",params)
					.getResultList();
		
	}
	
	/**
	 * 共通 配列からリスト型へ変換
	 * @param strArray String[]
	 * @return List<String>
	 */
	public List<String> arrayToList(String[] strArray){
		List<String> rtnList = new ArrayList<String>();
		rtnList = null;
		if(strArray != null){
			rtnList = Arrays.asList(strArray);
		}
		return rtnList;
	}
	
	/**
	 * フォトアルバムコメント削除 削除実行
	 * @param photoForm
	 */
	public void deletePhotoComment(PhotoForm photoForm){
		// 実行
		jdbcManager.updateBatchBySqlFile("/data/updPhotoCommentDelflg", setEntityList(photoForm))
		.execute();
	}
	
	/**
	 * フォトアルバムコメント削除 エンティティリスト生成
	 * @param photoForm
	 * @return List<PhotoComment>
	 */
	public List<PhotoComment> setEntityList(PhotoForm photoForm){
		List<PhotoComment> rtnList = new ArrayList<PhotoComment>();
		List<String> cnoList = generateCnoList(photoForm);
		if(cnoList != null && cnoList.size() > 0){
			Integer cnt = 0;
			Timestamp ts = new Timestamp((new java.util.Date()).getTime());
			for (String strCno : cnoList){
				Integer intCno = convInt(strCno);
				if(intCno > 0){
					PhotoComment pc = new PhotoComment();
					pc.mid     = photoForm.pmid;
					pc.ano     = photoForm.pano;
					pc.cno     = intCno;
					pc.updid   = userInfoDto.memberId;
					pc.upddate = ts;
					rtnList.add(cnt, pc);
					cnt ++;
				}
			}
		}
		return rtnList;
	}
	
	/**
	 * フォトアルバムコメント削除 有効コメントNOリスト取得
	 * @param photoForm
	 * @return List<String>
	 */
	public List<String> generateCnoList(PhotoForm photoForm){
		List<BeanMap> validList = new ArrayList<BeanMap>();
		List<String> paramList = photoForm.cnoList;
		List<String> rtnList = new ArrayList<String>();
		if(paramList != null && paramList.size() > 0){
			Map<String,Object> params = new HashMap<String,Object>();
			
			// パラメータ設定
			params.put("mid",      photoForm.pmid);
			params.put("ano",      photoForm.pano);
			params.put("cno",      photoForm.cnoList);
			params.put("memberId", userInfoDto.memberId);
			
			validList = jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selPhotoComment", params)
						.getResultList();
			if(validList != null && validList.size() > 0){
				for (BeanMap beanMap : validList){
					if(!paramList.contains(beanMap.get("cno"))){
						rtnList.add(beanMap.get("cno").toString());
					}
				}
			}
		}
		return rtnList;
	}
	
	/**
	 * 共通 型判定
	 * @param str
	 * @return Integer
	 */
	public Integer convInt(String str){
		Integer rtnInt = 0;
		try{
			if(!StringUtils.isNullOrEmpty(str)){
				rtnInt = Integer.valueOf(str);
			}
		}catch (NumberFormatException e){
			e.printStackTrace();
		}
		return rtnInt;
	}
	
	/**
	 * 写真詳細 写真詳細取得
	 * @param photoForm
	 * @return 
	 */
	public List<BeanMap> selPhotoViewphoto(PhotoForm photoForm){
		Map<String,Object> params = new HashMap<String,Object>();
		// パラメータセット
		params.put("mid", photoForm.pmid);
		params.put("ano", photoForm.pano);
		params.put("fno", convInt(photoForm.strFno));
		// SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPhotoViewphoto", params)
					.getResultList();
	}
	
	//フォトアルバムコメントのMax値を取得
	public List<BeanMap> selMaxCno(String mid,Integer ano){
		//検索条件設定
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mid", mid);
		params.put("ano", ano);
		
		return jdbcManager
		.selectBySqlFile(BeanMap.class, "/data/selMaxPhotoCno",params)
		.getResultList();
		
	}
	
	//フォトコメントの登録処理
	public void insPhotoComment(String mid,Integer ano,Integer cno,String comment,String myid){
		//登録用のエンティティ
		PhotoComment p = new PhotoComment();
		
		//パラメータ設定
		p.mid = mid;
		p.ano = ano;
		p.cno = cno;
		p.comment = comment;
		p.entid = myid;
		p.updid = myid;
		
		//フォトアルバム登録
		jdbcManager.updateBySqlFile("/data/insPhotoComment", p)
		.execute();	
	}
	
	//写真を1枚だけ検索
	public List<BeanMap> selPhotoIndividual(String mid,Integer ano,Integer fno){
		Map<String,Object> params = new HashMap<String,Object>();
		//パラメータ設定
		params.put("mid", mid);
		params.put("ano", ano);
		params.put("fno", fno);
		
		//SQL実行
		return jdbcManager
					.selectBySqlFile(BeanMap.class, "/data/selPhotoIndividual",params)
					.getResultList();
		
	}
	
	//フォトアルバムを<img>タグに変換
	public String replacePhotoAlbum(String txt,String mid){
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\[p:([0-9]+:[0-9]+:[0-9]+)\\]");
		Matcher m = p.matcher(txt);
		String img = "";
		
		//mixiに合わせるときに備えてとりあえずコメントアウト
		//String tag1 = "<s:link href=\"/pc/diary/list/m000000019\" target=\"_blank\"><img src=\"" + appDefDto.FP_CMN_CONTENTS_ROOT;
		//String tag2 = "\" /></s:link>\n";
		
		//画像がある場合の変数
		final String imgTag = "<a href=\"javascript:void(0);\" title=\"ALT\" onclick=\"ff_viewBigimg('"
							+ appDefDto.FP_CMN_CONTENTS_ROOT
							+ "PIC640');\">"
							+ "<img src=\"" + appDefDto.FP_CMN_CONTENTS_ROOT
							+ "PICsize\" alt=\"ALT\"/></a>";
		//画像がない場合の変数
		final String noImgTag = "<img src=\"/images/batu-red.gif\" alt=\"画像がありません\" />";
		
		while(m.find()){ // 検索(find)し、マッチする部分文字列がある限り繰り返す
			String[] partStr = m.group(1).split(":");
			
			//アルバムNo,フォトNoを取得
			Integer ano = Integer.parseInt(partStr[0]);
			Integer fno = Integer.parseInt(partStr[1]);
			Integer ipx = Integer.parseInt(partStr[2]);
			
			//意図しないサイズの選択の場合
			if(ipx!=480 && ipx!=240 && ipx!=180 && ipx!=120 && ipx!=76 && ipx!=60 && ipx!=42){
				ano = 0;
				fno = 0;
			}
			
			//フォトを検索
			List<BeanMap> lbm = selPhotoIndividual(mid, ano, fno);

			if (lbm.size()==0){
				//画像が存在しない場合
				img = noImgTag;
			} else {
				//画像が存在する場合
				//ディレクトリを置換
				String path120 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic120");
				String path640 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic640");
				String path480 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic480");
				String path240 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic240");
				String path180 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic180");
				String path76 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic76");
				String path60 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic60");
				String path42 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic42");

				
				
				//文字列を置換
				///選択された画像のサイズによって置換
				if(ipx == 480){
					img = imgTag.replaceAll("PICsize", path480);
				} else if(ipx == 240){
					img = imgTag.replaceAll("PICsize", path240);
				}else if(ipx == 180){
					img = imgTag.replaceAll("PICsize", path180);
				}else if(ipx == 120){
					img = imgTag.replaceAll("PICsize", path120);
				}else if(ipx == 76){
					img = imgTag.replaceAll("PICsize", path76);
				}else if(ipx == 60){
					img = imgTag.replaceAll("PICsize", path60);
				}else if(ipx == 42){
					img = imgTag.replaceAll("PICsize", path42);
				}else{
					img = imgTag.replaceAll("PICsize", "9999");
				}
				img = img.replaceAll("ALT", (String)lbm.get(0).get("picname"));
				img = img.replaceAll("PIC640", path640);
			}

			//マッチした部分へ置換
			m.appendReplacement(sb, img);
	  }
		
		//文字列連結
		m.appendTail(sb);
		
		return sb.toString();
	}
	
	//フォトアルバムを<img>タグに変換
	public String replaceMPhotoAlbum(String txt,String mid){
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\[p:([0-9]+:[0-9]+:[0-9]+)\\]");
		Matcher m = p.matcher(txt);
		String img = "";
		
		//mixiに合わせるときに備えてとりあえずコメントアウト
		//String tag1 = "<s:link href=\"/pc/diary/list/m000000019\" target=\"_blank\"><img src=\"" + appDefDto.FP_CMN_CONTENTS_ROOT;
		//String tag2 = "\" /></s:link>\n";
		
		//画像がある場合の変数
		final String imgTag = "<a href=\""+appDefDto.FP_CMN_CONTENTS_ROOT+"PICsize\" title=\"ALT\" >[画像]</a>";
		//画像がない場合の変数
		final String noImgTag = "[画像]";
		
		while(m.find()){ // 検索(find)し、マッチする部分文字列がある限り繰り返す
			String[] partStr = m.group(1).split(":");
			
			//アルバムNo,フォトNoを取得
			Integer ano = Integer.parseInt(partStr[0]);
			Integer fno = Integer.parseInt(partStr[1]);
			Integer ipx = Integer.parseInt(partStr[2]);
			
			//意図しないサイズの選択の場合
			if(ipx!=480 && ipx!=240 && ipx!=180 && ipx!=120 && ipx!=76 && ipx!=60 && ipx!=42){
				ano = 0;
				fno = 0;
			}
			
			//フォトを検索
			List<BeanMap> lbm = selPhotoIndividual(mid, ano, fno);

			if (lbm.size()==0){
				//画像が存在しない場合
				img = noImgTag;
			} else {
				//画像が存在する場合
				//ディレクトリを置換
				String path120 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic120");
				String path640 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic640");
				String path480 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic480");
				String path240 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic240");
				String path180 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic180");
				String path76 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic76");
				String path60 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic60");
				String path42 = ((String)lbm.get(0).get("pic")).replaceAll("dir", "pic42");

				
				
				//文字列を置換
				///選択された画像のサイズによって置換
//				if(ipx == 480){
//					img = imgTag.replaceAll("PICsize", path480);
//				} else if(ipx == 240){
//					img = imgTag.replaceAll("PICsize", path240);
//				}else if(ipx == 180){
					img = imgTag.replaceAll("PICsize", path180);
//				}else if(ipx == 120){
//					img = imgTag.replaceAll("PICsize", path120);
//				}else if(ipx == 76){
//					img = imgTag.replaceAll("PICsize", path76);
//				}else if(ipx == 60){
//					img = imgTag.replaceAll("PICsize", path60);
//				}else if(ipx == 42){
//					img = imgTag.replaceAll("PICsize", path42);
//				}else{
//					img = imgTag.replaceAll("PICsize", "9999");
//				}
				img = img.replaceAll("ALT", (String)lbm.get(0).get("picname"));
				img = img.replaceAll("PIC640", path640);
			}

			//マッチした部分へ置換
			m.appendReplacement(sb, img);
	  }
		
		//文字列連結
		m.appendTail(sb);
		
		return sb.toString();
	}
	
	/**
	 * 既読未読FLGの更新
	 * @param photoForm
	 * @param coverResults
	 */
	public void updPhotoCommentReadflg(PhotoForm photoForm, List<BeanMap> coverResults){
		if(coverResults != null
				&& userInfoDto.memberId.equals(photoForm.pmid)
				&& !"0".equals(coverResults.get(0).get("unreadcnt"))){
			// データ登録
			PhotoComment pc = new PhotoComment();
			pc.mid = userInfoDto.memberId;
			pc.ano = photoForm.pano;
			// SQL実行
			jdbcManager.updateBySqlFile("/data/updPhotoCommentReadflg", pc)
			.execute();	
		}
	}
}
