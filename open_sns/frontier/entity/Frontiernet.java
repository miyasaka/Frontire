package frontier.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Frontiernet {

	@Id
	@GeneratedValue
	public Integer id;
	public String network;
	public String rssurl;

}
