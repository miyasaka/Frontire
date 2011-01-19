package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Twitter {

	@Id
	@GeneratedValue
	public Long statusid;
	public String comment;
	public Integer twituserid;
	public String screenname;
	public String twitname;
	public String pic;
	public Long replytwitstatusid;
	public Integer replytwituserid;
	@Temporal(TemporalType.DATE)
	public Timestamp contributetime;
	public String delflg;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String entid;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;

}
