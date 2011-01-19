package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Fileinfo {

	@Id
	public String fileid;
	@Id
	public Integer historyno;
	public String categoryid;
	public String title;
	public String explanation;
	public String version;
	public String content;
	public String filename;
	@Column(name = "e_extension")
	public String EExtension;
	public Integer download;
	public Integer filesize;
	public String pubfile;
	public String entid;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;

}
