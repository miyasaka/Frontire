package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="twitter_post_management")
public class TwitterPostManagement {

	@Id
	public Long statusid;
	@Id
	public Integer twituserid;
	public String readflg;
	public String favoriteflg;
	public String postflg;
	public String mentionflg;
	public String batchfavoriteflg;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String entid;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String realmentionflg;
}
