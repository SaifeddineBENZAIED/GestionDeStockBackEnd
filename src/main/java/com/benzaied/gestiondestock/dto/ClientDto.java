package com.benzaied.gestiondestock.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.benzaied.gestiondestock.model.Client;
import com.benzaied.gestiondestock.model.Roles;
import com.benzaied.gestiondestock.model.TypeClient;
import com.benzaied.gestiondestock.token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientDto {
	
	private Integer id;

	private String nom;
	
	private String prenom;
	
	private AdresseDto adresse;
	
	private String image;
	
	private String email;

	private String motDePasse;
	
	private String numTelephone;

	private TypeClient typeClient;

	@Builder.Default
	private Roles role = Roles.USER;

	private Integer idEntreprise;

	@JsonIgnore
	private List<Token> tokens;

	@JsonIgnore
	private List<CommandeClientDto> commandesClient;


	public static ClientDto fromEntity(Client client) {
		if (client == null) {
			return null;
		}

		return ClientDto.builder()
				.id(client.getId())
				.nom(client.getNom())
				.prenom(client.getPrenom())
				.adresse(AdresseDto.fromEntity(client.getAdresse()))
				.image(client.getImage())
				.email(client.getEmail())
				.motDePasse(client.getMotDePasse())
				.numTelephone(client.getNumTelephone())
				.typeClient(client.getTypeClient())
				.idEntreprise(client.getIdEntreprise())
				.tokens(client.getTokens())
				/*.commandesClient(client.getCommandesClient().stream()
						.map(CommandeClientDto::fromEntity)
						.collect(Collectors.toList())
				)*/
				.build();
	}

	public static Client toEntity(ClientDto clientDto) {
		if (clientDto == null) {
			return null;
		}

		Client client = new Client();
		client.setId(clientDto.getId());
		client.setNom(clientDto.getNom());
		client.setPrenom(clientDto.getPrenom());
		client.setAdresse(AdresseDto.toEntity(clientDto.getAdresse()));
		client.setImage(clientDto.getImage());
		client.setEmail(clientDto.getEmail());
		client.setMotDePasse(clientDto.getMotDePasse());
		client.setNumTelephone(clientDto.getNumTelephone());
		client.setTypeClient(clientDto.getTypeClient());
		client.setIdEntreprise(clientDto.getIdEntreprise());
		client.setTokens(clientDto.getTokens());
		/*client.setCommandesClient(clientDto.getCommandesClient().stream()
				.map(CommandeClientDto::toEntity)
				.collect(Collectors.toList())
		);*/

		return client;
	}

}
