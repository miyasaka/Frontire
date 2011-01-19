package frontier.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.entity.Photo;
import frontier.entity.Profile2;
import frontier.form.pc.Profile2Form;


public class Profile2Service {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	protected JdbcManager jdbcManager;
	public List<BeanMap> results;
	public List<BeanMap> results2;

	public long count;
	@Resource
	public AppDefDto appDefDto;
	public String DIR;
	public String BasePath;
	public String FileName = null;
	public ImageIcon icon;
	public String photoName1;
	public String photoName2;
	public String photoName3;
	Integer[] DirList ={640,480,240,180,120,76,60,42};
	
	public String Path;
	public String Dir;
	public String picnm = "";
	public String pic = "";
	public Integer[] dirs = {640,480,240,180,120,76,60,42};

	
	
	public ActionMessages errors = new ActionMessages();
	
	

	@ActionForm
	@Resource
	protected Profile2Form profile2Form;
	
	// 現住所・都道府県の取得
	public List<BeanMap> selectResidenceRegionItems(){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstRegionItems")
		.getResultList();
		return results;
	}
	
	// 現住所・市区町村の取得
	public List<BeanMap> selectResidenceCitiesItems(String itemid){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstCitiesItems",itemid)
		.getResultList();
		return results;
	}
	
	// 出身地・都道府県の取得
	public List<BeanMap> selectHometownRegionItems(){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstRegionItems")
		.getResultList();
		return results;
	}
	
	// 出身地・市区町村の取得
	public List<BeanMap> selectHometownCitiesItems(String itemid){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstCitiesItems",itemid)
		.getResultList();
		return results;
	}
	
	// 共通項目の取得
	public List<BeanMap> selectItems(String itemid){
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selMstComItems",itemid)
		.getResultList();
		return results;
	}	
	
	//プロフィール更新処理
	public void updprofile2(String mid,String familyname,String name,String nickname,String residenceregion,String residencecity,String gender,String yearofbirth,String dateofbirth,String bloodtype,String hometownregion,String hometowncity,String interest1,String occupation,String company,String aboutme,String favgenre1,String favgenre2,String favgenre3,String favcontents1,String favcontents2,String favcontents3,String nameLevel,String locationLevel,String birthdayLevel,String ageLevel,String hometownLevel,String jobLevel,String genderLevel) {
		
		//好きな○○のどちらかに入力がない場合はどちらも登録しない
		if((favgenre1.equals("0")||favgenre1.equals("")) || favcontents1.equals("")){
			favgenre1="0";
			favcontents1="";
		}
		if((favgenre2.equals("0")||favgenre2.equals("")) || favcontents2.equals("")){
			favgenre2="0";
			favcontents2="";
		}
		if((favgenre3.equals("0")||favgenre3.equals("")) || favcontents3.equals("")){
			favgenre3="0";
			favcontents3="";
		}

		
		//プロフィールの更新
		Profile2 profileitem = new Profile2();	
		profileitem.mid             = mid;
		profileitem.familyname      = familyname;
		profileitem.name            = name;
		profileitem.nickname        = nickname;
		profileitem.residenceregion = residenceregion;
		profileitem.residencecity   = residencecity;
		profileitem.gender          = gender;
		profileitem.yearofbirth     = yearofbirth;
		profileitem.dateofbirth     = dateofbirth;
		profileitem.bloodtype       = bloodtype;
		profileitem.hometownregion  = hometownregion;
		profileitem.hometowncity    = hometowncity;
		profileitem.interest1       = interest1;
		profileitem.occupation      = occupation;
		profileitem.company         = company;
		profileitem.aboutme         = aboutme;
		
		jdbcManager.updateBySqlFile("/data/updProfile2Members", profileitem)
		.execute();
		
		//プロフィールの更新２
		Profile2 profileitem2 = new Profile2();	
		profileitem2.mid          = mid;
		profileitem2.favgenre1    = favgenre1;
		profileitem2.favgenre2    = favgenre2;
		profileitem2.favgenre3    = favgenre3;
		profileitem2.favcontents1 = favcontents1;
		profileitem2.favcontents2 = favcontents2;
		profileitem2.favcontents3 = favcontents3;
		
		jdbcManager.updateBySqlFile("/data/updProfile2MemberAddInfo", profileitem2)
		.execute();	
		
		//プロフィールの更新３	
		Profile2 profileitem3 = new Profile2();	
		profileitem3.mid             = mid;
		profileitem3.nameLevel       = nameLevel;
		profileitem3.locationLevel   = locationLevel;
		profileitem3.birthdayLevel   = birthdayLevel;
		profileitem3.ageLevel        = ageLevel;
		profileitem3.hometownLevel   = hometownLevel;
		profileitem3.jobLevel        = jobLevel;
		profileitem3.genderLevel     = genderLevel;		
		
		jdbcManager.updateBySqlFile("/data/updProfile2MemberItemInfo", profileitem3)
		.execute();

	}
	
	// メンバーデータ取得
	public List<BeanMap> selectprofile(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selprofile2.sql",params)
		.getResultList();
		return results;		
	}

	// 写真の変更メンバーデータ取得
	public List<BeanMap> selectprofileEdit(String mid){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selprofile2Edit.sql",params)
		.getResultList();
		return results;		
	}
	
	
	//メイン写真更新処理
	public void updmainpicno(String mid,String mainpicno) {
		//メイン写真の更新
		Profile2 profileitem = new Profile2();	
		profileitem.mid             = mid;
		profileitem.mainpicno       = mainpicno;
		//logger.debug("********** メイン写真の変更するよ ***********");
		//logger.debug("********** mid ***********"+mid);
		//logger.debug("********** mainpicno ***********"+mainpicno);
		
		jdbcManager.updateBySqlFile("/data/updProfile2Mainpic", profileitem)
		.execute();
	}
	
	//写真削除処理
	public void upddelpic(String mid,String picno) {
		logger.debug("************* ４４４４４４４４４４４４４４ ******************");
		results = selectprofileEdit(mid);
		
		// 画像のセット
		String pic1 = results.get(0).get("pic1")!=null?results.get(0).get("pic1").toString():"";
		String pic2 = results.get(0).get("pic2")!=null?results.get(0).get("pic2").toString():"";
		String pic3 = results.get(0).get("pic3")!=null?results.get(0).get("pic3").toString():"";
		String mainpicno = results.get(0).get("mainpicno")!=null?results.get(0).get("mainpicno").toString():"";
		
		//メイン写真を削除した際のメイン写真を他写真にセット
		if(mainpicno.equals(picno)){
			if(picno.equals("1")){
				if(!pic2.equals("")){
					//logger.debug("********** 画像２ある ***********");
					mainpicno = "2";
				}else if(pic2.equals("")){
					//logger.debug("********** 画像２ない ***********");
					if(!pic3.equals("")){
						mainpicno = "3";
					}else{
						mainpicno = "";
					}
				}
			} else if(picno.equals("2")){
				if(!pic1.equals("")){
					//logger.debug("********** 画像１ある ***********");
					mainpicno = "1";
				}else if(pic1.equals("")){
					//logger.debug("********** 画像１ない ***********");
					if(!pic3.equals("")){
						mainpicno = "3";
					}else{
						mainpicno = "";
					}
				}
			} else if(picno.equals("3")){
				if(!pic1.equals("")){
					//logger.debug("********** 画像１ある ***********");
					mainpicno = "1";
				}else if(pic1.equals("")){
					//logger.debug("********** 画像１ない ***********");
					if(!pic2.equals("")){
						mainpicno = "2";
					}else{
						mainpicno = "";
					}
				}
			}
		}
		
		
		// 削除する画像パスにNULLをセット
		if(picno.equals("1")){pic1=null;}
		if(picno.equals("2")){pic2=null;}
		if(picno.equals("3")){pic3=null;}
		
		//logger.debug("********** 写真削除するよ ***********");
		//logger.debug("********** picno ***********"+picno);
		//logger.debug("********** mainpicno ***********"+mainpicno);
		//logger.debug("********** mid ***********"+mid);
		//logger.debug("********** pic1 ***********"+pic1);
		//logger.debug("********** pic2 ***********"+pic2);
		//logger.debug("********** pic3 ***********"+pic3);
		//写真削除
		Profile2 profileitem = new Profile2();	
		profileitem.mid  = mid;
		profileitem.pic1 = pic1;
		profileitem.pic2 = pic2;
		profileitem.pic3 = pic3;
		profileitem.mainpicno = mainpicno;
		
		jdbcManager.updateBySqlFile("/data/updProfile2Delpic", profileitem)
		.execute();
	}
	
	// 画像のアップロード処理
	public void picupload(String mid,FormFile picpath) throws IOException, Exception{
		logger.debug("************* ３３３３３３３３３３３３ ******************");
		results = selectprofileEdit(mid);
		
		String imgno = "";
		
		// 画像のセット
		String pic1 = results.get(0).get("pic1")!=null?results.get(0).get("pic1").toString():"";
		String pic2 = results.get(0).get("pic2")!=null?results.get(0).get("pic2").toString():"";
		String pic3 = results.get(0).get("pic3")!=null?results.get(0).get("pic3").toString():"";
		String mainpicno = results.get(0).get("mainpicno")!=null?results.get(0).get("mainpicno").toString():"";

		if(pic1.equals("")){
			//logger.debug("********** 1の画像がないよ ***********");
			imgno = "1";
		}else{
			if(pic2.equals("")){
				//logger.debug("********** 2の画像がないよ ***********");
				imgno = "2";
			}else{
				if(pic3.equals("")){
					//logger.debug("********** 3の画像がないよ ***********");
					imgno = "3";
				}else{
					logger.debug("********** 画像は全部埋まってるよ ***********");
					return;
				}
			}
		}
		//logger.debug("********** imgno ***********"+imgno);
		
		//画像が一枚もない場合は画像１をメイン画像としてセット
		if(pic1.equals("") && pic2.equals("") && pic3.equals("")){
			//logger.debug("********** 画像が全くない ***********");
			mainpicno = "1";
		}
		
		// ディレクトリパス
		Path = appDefDto.FP_CMN_CONTENTS_DIR+"img/mem/"+mid;
		//logger.debug("********** 11111111111 ***********"+Path);
		// ディレクトリ
		Dir = "img";
		//logger.debug("********** 2222222222 ***********"+Dir);
		// アップロード画像名取得(yyyymmddhh24miss)
		picnm = imgno+"_"+CmnUtility.getToday("yyyyMMddHHmmss") + ".jpg";
		//logger.debug("********** 3333333333 ***********"+picnm);
		// 登録用画像パス設定
		pic = "/img/mem/" + mid + "/" + Dir + "/dir/" + picnm;
		//logger.debug("********** 44444444444 ***********"+pic);
		// ディレクトリ作成
		CmnUtility.makeDir(Path, Dir);
		//logger.debug("********** 55555555555 ***********");
		// 画像ファイルのアップロード(ベースはpic640フォルダ)
		CmnUtility.uploadFile(Path + "/" + Dir + "/pic640/" + picnm, picpath);
		//logger.debug("********** 66666666666 ***********");
		// ファイルのコピー&リサイズ(ディレクトリ数分ループ)
		for (Integer i : dirs) {
			CmnUtility.resize(Path + "/" + Dir + "/" + "pic" + i.toString() + "/" + picnm,i,Path + "/" + Dir + "/pic640/" + picnm,appDefDto.FP_CMN_IMAGEMAGICK_DIR);
			//logger.debug("********** 7777777777777 ***********");
		}
		
		
		// 画像の再セット
		if(pic1.equals("")){
			pic1 = pic;
		}else{
			if(pic2.equals("")){
				pic2 = pic;
			}else{
				pic3 = pic;			
			}
		}
		
		//logger.debug("********** pic1 ***********"+pic1);
		//logger.debug("********** pic2 ***********"+pic2);
		//logger.debug("********** pic3 ***********"+pic3);
		
		//写真更新
		Profile2 profileitem = new Profile2();	
		profileitem.mid  = mid;
		profileitem.pic1 = pic1;
		profileitem.pic2 = pic2;
		profileitem.pic3 = pic3;
		profileitem.mainpicno = mainpicno;
		
		jdbcManager.updateBySqlFile("/data/updProfile2Delpic", profileitem)
		.execute();
		
		
	}
	
	
}
