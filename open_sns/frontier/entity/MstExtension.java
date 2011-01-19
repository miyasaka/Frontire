package frontier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mst_extension")
public class MstExtension {

	@Id
	@Column(name = "e_extension")
	public String EExtension;
	@Column(name = "j_extension")
	public String JExtension;
	public String pic;

}
