package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Members {

	@Id
	@GeneratedValue
	public String mid;
	public String email;
	public String nickname;
	@Column(name = "hometown_region")
	public String hometownRegion;
	@Column(name = "hometown_city")
	public String hometownCity;
	@Column(name = "residence_region")
	public String residenceRegion;
	@Column(name = "residence_city")
	public String residenceCity;
	public String yearofbirth;
	public String dateofbirth;
	public String gender;
	public String bloodtype;
	public String occupation;
	public String company;
	public String interest1;
	public String interest2;
	public String interest3;
	public String password;
	public String aboutme;
	public String status;
	@Temporal(TemporalType.DATE)
	public Timestamp entdate;
	public String updid;
	@Temporal(TemporalType.DATE)
	public Timestamp upddate;
	public String familyname;
	public String name;
	public String mobileemail;
	@Temporal(TemporalType.DATE)
	public Timestamp lastaccessdate;
	public String membertype;
	@Column(name = "management_level")
	public String managementLevel;
	public String ninsyouid;
	public String secessionreason;
	@Temporal(TemporalType.DATE)
	public Timestamp secessiondate;
	public String informationflg;
	public Integer follownumber;
	public Integer followernumber;
	public String target;
	public String twitterflg;
	public String twitteraccount;
	public Integer updatefrequency;

}
