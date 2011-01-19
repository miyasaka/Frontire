package frontier.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="community_enterant")
public class CommunityEnterant {

	@Id
	public String cid;
	@Id
	public BigDecimal sno;
	public String mid;
	public String joinstatus;
	@Temporal(TemporalType.DATE)
	public Timestamp requireddate;
	public String requiredmsg;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String updid;

}
