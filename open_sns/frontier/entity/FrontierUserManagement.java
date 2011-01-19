package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="frontier_user_management")
public class FrontierUserManagement {

	@Id
	@GeneratedValue
	public String frontierdomain;
	@Id
	@GeneratedValue
	public String fid;
	public String mid;
	public String nickname;
	public String pic;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;

}
