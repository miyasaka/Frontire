package frontier.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="community_evententry_info")
public class CommunityEvententryInfo {

	@Id
	@GeneratedValue
	public String cid;
	@Id
	@GeneratedValue
	public Integer bbsid;
	@Id
	@GeneratedValue
	public String mid;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public List<String> chkmem;

}
