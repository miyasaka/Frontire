package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="frontier_group_join")
public class FrontierGroupJoin {

	@Id
	@GeneratedValue
	public String frontierdomain;
	@Id
	@GeneratedValue
	public String gid;
	@Id
	@GeneratedValue
	public String mid;
	public String manageflg;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;

}
