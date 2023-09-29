package com.benzaied.gestiondestock.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class FideliteClientDto {

    private ClientDto clientDto;
    private BigDecimal montantTotalAchat;

    public FideliteClientDto(ClientDto clientDto, BigDecimal montantTotalAchat) {
        this.clientDto=clientDto;
        this.montantTotalAchat=montantTotalAchat;
    }
}
