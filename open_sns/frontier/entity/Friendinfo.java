package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Friendinfo {
	
	@Id
	public String friendno;
	public String friendstatus;
	public String reqcomment;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String updid;
}
