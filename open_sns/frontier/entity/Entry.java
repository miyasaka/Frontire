package frontier.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Entry {
	public String mid;
	public String fid;
    public String nickname;
    public String pic;
    public String email;
    public String password;
    public String openid;
    public String frontierdomain;
    public String gid;
    public String familyname;
    public String name;
    public Integer manageflg;
    public String entid;
    public String updid;
}
