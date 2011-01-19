package frontier.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Communities {

	@Id
	public String cid;
	public String admmid;
	public String title;
	public String detail;
	public String category1;
	public String category2;
	public String category3;
	public String joincond;
	public String publevel;
	public String makabletopic;
	public String pic;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String updid;

}
