package com.benzaied.gestiondestock.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


import com.benzaied.gestiondestock.model.CommandeClient;
import com.benzaied.gestiondestock.model.EtatCommande;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommandeClientDto {
	
	private Integer id;

	private String codeCC;
	
	private Instant dateCommande;

	private EtatCommande etatCommande;
	
	private ClientDto client;

	private Integer idEntreprise;

	private List<LigneCommandeClientDto> ligneCommandeClients;

	public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
		if (commandeClient == null) {
			return null;
		}

		return CommandeClientDto.builder()
				.id(commandeClient.getId())
				.codeCC(commandeClient.getCodeCC())
				.dateCommande(commandeClient.getDateCommande())
				.etatCommande(commandeClient.getEtatCommande())
				.client(ClientDto.fromEntity(commandeClient.getClient()))
				.idEntreprise(commandeClient.getIdEntreprise())
				/*.ligneCommandeClients(commandeClient.getLigneCommandeClients().stream()
						.map(LigneCommandeClientDto::fromEntity)
						.collect(Collectors.toList())
				)*/
				.build();
	}

	public static CommandeClient toEntity(CommandeClientDto commandeClientDto) {
		if (commandeClientDto == null) {
			return null;
		}

		CommandeClient commandeClient = new CommandeClient();
		commandeClient.setId(commandeClientDto.getId());
		commandeClient.setCodeCC(commandeClientDto.getCodeCC());
		commandeClient.setDateCommande(commandeClientDto.getDateCommande());
		commandeClient.setEtatCommande(commandeClientDto.getEtatCommande());
		commandeClient.setClient(ClientDto.toEntity(commandeClientDto.getClient()));
		commandeClient.setIdEntreprise(commandeClientDto.getIdEntreprise());
		/*commandeClient.setLigneCommandeClients(commandeClientDto.getLigneCommandeClients().stream()
				.map(LigneCommandeClientDto::toEntity)
				.collect(Collectors.toList())
		);*/

		return commandeClient;
	}

	public boolean isCommandeLivree(){
		return EtatCommande.LIVREE.equals(this.etatCommande);
	}


}
