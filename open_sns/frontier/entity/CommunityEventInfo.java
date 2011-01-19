package frontier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="community_event_info")
public class CommunityEventInfo {

	@Id
	@GeneratedValue
	public String cid;
	@Id
	@GeneratedValue
	public Integer bbsid;
	@Column(name = "event_year")
	public String eventYear;
	@Column(name = "event_month")
	public String eventMonth;
	@Column(name = "event_day")
	public String eventDay;
	@Column(name = "event_note")
	public String eventNote;
	@Column(name = "eventarea_cd")
	public String eventareaCd;
	@Column(name = "eventarea_note")
	public String eventareaNote;
	@Column(name = "limit_year")
	public String limitYear;
	@Column(name = "limit_month")
	public String limitMonth;
	@Column(name = "limit_day")
	public String limitDay;

}
