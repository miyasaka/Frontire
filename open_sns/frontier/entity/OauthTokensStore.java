package frontier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="oauthtokensstore")
public class OauthTokensStore {

	@Id
	@Column(name = "tokenId")
	public String   tokenId;
    public String   mid;
	@Column(name = "consumerId")
	public String   consumerId;
	@Column(name = "requestToken")
	public String   requestToken;
	@Column(name = "accessToken")
	public String   accessToken;
	@Column(name = "tokenSecret")
	public String   tokenSecret;
	public String   delflg;
	public Integer  twituserid;
	public String   screenname;
	public String   twitname;
	public String   pic;
	public String   useflg;
}
