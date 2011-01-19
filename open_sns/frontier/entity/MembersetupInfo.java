package frontier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="membersetup_info")
public class MembersetupInfo {

	@Id
	@GeneratedValue
	public String mid;
	@Column(name = "dispnum_mem")
	public Integer dispnumMem;
	@Column(name = "dispnum_com")
	public Integer dispnumCom;
	@Column(name = "dispnum_memdiary")
	public Integer dispnumMemdiary;
	@Column(name = "dispnum_memdiarycmnt")
	public Integer dispnumMemdiarycmnt;
	@Column(name = "dispnum_combbs")
	public Integer dispnumCombbs;
	@Column(name = "dispnum_combbscmnt")
	public Integer dispnumCombbscmnt;
	@Column(name = "dispnum_memupdate")
	public Integer dispnumMemupdate;
	@Column(name = "sortitem_memdiary")
	public String sortitemMemdiary;
	@Column(name = "sortitem_memdiarycmnt")
	public String sortitemMemdiarycmnt;
	@Column(name = "sortitem_combbs")
	public String sortitemCombbs;
	@Column(name = "sortitem_combbscmnt")
	public String sortitemCombbscmnt;
	@Column(name = "sortitem_memupdate")
	public String sortitemMemupdate;
	@Column(name = "dispnum_my")
	public Integer dispnumMy;
	@Column(name = "sortitem_my")
	public String sortitemMy;
	@Column(name = "disppos_myphoto")
	public String dispposMyphoto;
	@Column(name = "disppos_my")
	public String dispposMy;
	@Column(name = "disppos_mem")
	public String dispposMem;
	@Column(name = "disppos_com")
	public String dispposCom;
	@Column(name = "disppos_comupdate")
	public String dispposComupdate;
	@Column(name = "disppos_diaryupdate")
	public String dispposDiaryupdate;
	@Column(name = "disppos_memupdate")
	public String dispposMemupdate;
	@Column(name = "dispnum_calendar")
	public Integer dispnumCalendar;
	@Column(name = "disppos_calendar")
	public String dispposCalendar;
	@Column(name = "disppos_memdiarycmnt")
	public String dispposMemdiarycmnt;
	@Column(name = "disppos_combbscmnt")
	public String dispposCombbscmnt;
	@Column(name = "dispnum_myphoto")
	public Integer dispnumMyphoto;
	@Column(name = "disptype_calendar")
	public String disptypeCalendar;
	@Column(name = "disppos_fshout")
	public String dispposFshout;
	@Column(name = "dispnum_fshout")
	public Integer dispnumFshout;
	@Column(name = "dispnum_group")
	public Integer dispnumGroup;
	@Column(name = "dispnum_followyou")
	public Integer dispnumFollowyou;
	@Column(name = "dispnum_followme")
	public Integer dispnumFollowme;
	@Column(name = "disppos_group")
	public String dispposGroup;
	@Column(name = "disppos_followyou")
	public String dispposFollowyou;
	@Column(name = "disppos_followme")
	public String dispposFollowme;
	

}

