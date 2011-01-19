package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="photo_comment")
public class PhotoComment {

	@Id
	@GeneratedValue
	public String mid;
	@Id
	@GeneratedValue
	public Integer ano;
	@Id
	@GeneratedValue
	public Integer cno;
	public String comment;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;

}
