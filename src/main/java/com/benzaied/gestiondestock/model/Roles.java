package com.benzaied.gestiondestock.model;


import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.benzaied.gestiondestock.model.Permission.*;

/*@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class Roles extends AbstractEntity {

	@Column(name = "rolename")
	private String roleName;

	@ManyToMany(mappedBy = "roles")
	private List<Utilisateur> utilisateurs;

}*/
@RequiredArgsConstructor
public enum Roles {
	
	USER(Collections.emptySet()),

	ADMIN(
			Set.of(
					ADMIN_READ,
					ADMIN_UPDATE,
					ADMIN_CREATE,
					ADMIN_DELETE,
					MANAGER_READ,
					MANAGER_UPDATE,
					MANAGER_CREATE,
					MANAGER_DELETE
			)
	),

	MANAGER(
			Set.of(
					MANAGER_READ,
					MANAGER_UPDATE,
					MANAGER_CREATE,
					MANAGER_DELETE
			)
	);

	@Getter
	private final Set<Permission> permissions;

	public List<SimpleGrantedAuthority> getUserAuthorities(){
		var authorities = getPermissions()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toList());

		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

		return authorities;
	}

}
