package frontier.service;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;

import frontier.entity.Members;



public class SetupService {
	@Resource
	protected JdbcManager jdbcManager;
	
	public void setCode(String email, String passwd, String code){
		Members members = new Members();
		members = jdbcManager.from(Members.class)
			.where("email = ? and password = ? and status = ?",email,passwd,"1")
				.getSingleResult();
		if(members!=null){
			members.ninsyouid=code;
			members.upddate= new Timestamp ((new java.util.Date()).getTime());
			jdbcManager.update(members).includes("ninsyouid","upddate").execute();
		}
	}
}
