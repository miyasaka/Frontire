package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="frontier_rss_member")
public class FrontierRssMember {

	@Id
	@GeneratedValue
	public Integer id;
	@Id
	@GeneratedValue
	public String mid;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;

}
