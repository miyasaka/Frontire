package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Diary {

	@Id
	public String mid;
	@Id
	public Integer diaryid;
	@Id
	public Integer comno;
	public String title;
	public String comment;
	public String guest_name;
	public String guest_email;
	public String guest_url;
	public String pic1;
	public String pic2;
	public String pic3;
	public String delflg;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String entid;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String readflg;
	public String diarycategory;
	public String reportday;
	public String pubDiary;
	public String picnote1;
	public String picnote2;
	public String picnote3;
	@Column(name = "pub_level")
	public String publevel;
	@Column(name = "app_status")
	public String appstatus;
}
