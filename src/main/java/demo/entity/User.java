package demo.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	private @Id @Column(name="id", length=50) String id;
	private @Column(name="email", length=50) String email;
	private @Column(name="password", length=10) String password;
	private @Column(name="full_name", length=50) String fullName;
	private @Column(name="identification_number", length=50) String identificationNumber;
	
    private @ManyToMany(fetch = FetchType.EAGER) 
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    ) List<Role> roles;
	
	public User() {
		setId(lebah.util.UIDGenerator.getUID());
	}
	
	public String toString() {
		return this.fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	public List<Role> getRoles() {
		if ( roles == null ) roles = new ArrayList<>();
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;	
        User u = (User) obj;
        return this.id.equals(u.getId());
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }


}
