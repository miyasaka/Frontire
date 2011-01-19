package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Photo {

	@Id
	@GeneratedValue
	public String mid;
	@Id
	@GeneratedValue
	public Integer ano;
	@Id
	@GeneratedValue
	public Integer fno;
	public String picname;
	public String pic;
	public String coverflg;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;

}
