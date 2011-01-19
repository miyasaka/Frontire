package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Schedule {

	@Id
	public String mid;
	@Id
	public Integer sno;
	public String title;
	public String titlecolor;
	public String startyear;
	public String startmonth;
	public String startday;
	public String starttime;
	public String startminute;
	public String endyear;
	public String endmonth;
	public String endday;
	public String endtime;
	public String endminute;
	public String detail;
	public String newsflg;
	public String publevel;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;

}
