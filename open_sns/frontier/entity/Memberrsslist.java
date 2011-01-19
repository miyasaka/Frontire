package frontier.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Memberrsslist {

	@Id
	@GeneratedValue
	public String mid;
	@Id
	@GeneratedValue
	public Integer no;
	public Integer sno;
	public String rssurl;

}
