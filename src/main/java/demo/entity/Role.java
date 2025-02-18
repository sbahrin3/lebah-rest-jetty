package demo.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import qard.entity.LegalCapacityResult;

@Entity
@Table(name="roles")
public class Role {
	
	private @Id @Column(name="id", length=50) String id;
	private @Column(length=100) String name;
	
	public Role() {
		setId(lebah.util.UIDGenerator.getUID());
	}
	
	public Role(String id, String name) {
		setId(id);
		setName(name);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;	
        Role r = (Role) obj;
        return this.id.equals(r.getId());
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
	

}
