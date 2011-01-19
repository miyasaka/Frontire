package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ScheduleJoinInfo {

	@Id
	public String mid;
	@Id
	public Integer sno;
	@Id
	public String joinmid;
	
	public String delflg;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
}
