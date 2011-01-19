package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="frontier_group")
public class FrontierGroup {

	@Id
	@GeneratedValue
	public String frontierdomain;
	@Id
	@GeneratedValue
	public String gid;
	public String gname;
	public String pic;
	public Integer joinnumber;
	public String delflg;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;

}
