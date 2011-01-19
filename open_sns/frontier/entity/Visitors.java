package frontier.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Visitors {

	@Id
	public String mid;
	@Id
	public String visitday;
	@Id
	public String visitmid;
	@Temporal(TemporalType.DATE)
	public Timestamp lastvisittime;
	public Integer visitcount;

}
