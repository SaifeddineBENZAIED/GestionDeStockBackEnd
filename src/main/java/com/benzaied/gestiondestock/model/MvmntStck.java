package com.benzaied.gestiondestock.model;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class MvmntStck extends AbstractEntity {
	
	@Column
	private Instant dateMvmnt;
	
	@Column
	private BigDecimal quantite;
	
	@ManyToOne
	@JoinColumn(name="article")
	private Article article;
	
	@Column
	@Enumerated(EnumType.STRING)
	private TypedeMvmntStck typeMvmntStck;

	@Column
	@Enumerated(EnumType.STRING)
	private SourceMvmntStck sourceMvmntStck;

	@Column(name = "entreprise_id")
	private Integer idEntreprise;
	

}
