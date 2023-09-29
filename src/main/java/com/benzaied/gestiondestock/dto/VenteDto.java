package com.benzaied.gestiondestock.dto;

import java.time.Instant;
import java.util.List;

import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.model.Vente;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VenteDto {
	
	private Integer id;

	private String codeVente;
	
	private Instant dateVente;
	
	private String commentaire;

	private EtatCommande etatCommande;

	private final String typeClient = "PASSAGER";

	private Integer idEntreprise;

	private List<LigneVenteDto> ligneVentes;

	public static VenteDto fromEntity(Vente vente) {
		if (vente == null) {
			return null;
		}

		return VenteDto.builder()
				.id(vente.getId())
				.codeVente(vente.getCodeVente())
				.dateVente(vente.getDateVente())
				.commentaire(vente.getCommentaire())
				.etatCommande(vente.getEtatCommande())
				.idEntreprise(vente.getIdEntreprise())
				.build();
	}

	public static Vente toEntity(VenteDto venteDto) {
		if (venteDto == null) {
			return null;
		}

		Vente vente = new Vente();
		vente.setId(venteDto.getId());
		vente.setCodeVente(venteDto.getCodeVente());
		vente.setDateVente(venteDto.getDateVente());
		vente.setCommentaire(venteDto.getCommentaire());
		vente.setEtatCommande(venteDto.getEtatCommande());
		vente.setIdEntreprise(venteDto.getIdEntreprise());

		return vente;
	}

	public boolean isCommandeLivree(){
		return EtatCommande.LIVREE.equals(this.etatCommande);
	}

}
