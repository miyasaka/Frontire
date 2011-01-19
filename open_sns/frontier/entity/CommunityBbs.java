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
@Table(name="community_bbs")
public class CommunityBbs {

	@Id
	public String cid;
	@Id
	public Integer bbsid;
	@Id
	public Integer comno;
	public String entrytype;
	public String comment;
	public String title;
	public String pic1;
	public String pic2;
	public String pic3;
	public String picnote1;
	public String picnote2;
	public String picnote3;
	public String delflg;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String mid;
	public String updid;
	public List<String> chkcmnt;

}
