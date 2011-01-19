package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Frontiershout {
	@Id
	@GeneratedValue
	public String mid;
	@Id
	@GeneratedValue
	public Integer no;
	public String comment;
	public Integer twitter;
	public String entid;
	public String updid;
	public String confirmflg;
	public String delflg;
	public Integer demandflg;
	public Integer pub_level;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public Long replyid;
	public String replymid;
	public Long statusid;
}
