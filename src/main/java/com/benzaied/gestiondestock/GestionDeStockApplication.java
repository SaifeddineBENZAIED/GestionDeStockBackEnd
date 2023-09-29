package com.benzaied.gestiondestock;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing//(auditorAwareRef = "auditAwareImpl")
public class GestionDeStockApplication{

	public static void main(String[] args) {
		SpringApplication.run(GestionDeStockApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner commandLineRunner(
			RolesService rolesService
	){
		return args -> {
			// Create Roles instances
			Roles roleAdmin = new Roles("Admin" , null);
			Roles roleManager = new Roles("Manager" , null);

			rolesService.save(RolesDto.fromEntity(roleAdmin));
			rolesService.save(RolesDto.fromEntity(roleManager));

		};
	}*/

	/*@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	){
		return args -> {
			var admin = RegisterRequest.builder()
					.nom("Admin")
					.prenom("Admin")
					.email("admin@gmail.com")
					.motDePasse("admin12345")
					.roles(ADMIN)
					.build();
			System.out.println("Admin Token : "+service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.nom("Manager")
					.prenom("Manager")
					.email("manager@gmail.com")
					.motDePasse("manager12345")
					.roles(MANAGER)
					.build();
			System.out.println("Manager Token : "+service.register(manager).getAccessToken());
		};
	}*/

}
