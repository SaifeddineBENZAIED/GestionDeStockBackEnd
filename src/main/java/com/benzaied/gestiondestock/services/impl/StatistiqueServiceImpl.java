package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.*;
import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.model.*;
import com.benzaied.gestiondestock.services.StatistiqueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;


@Service
@Slf4j
public class StatistiqueServiceImpl implements StatistiqueService {

    private CommandeClientRepository commandeClientRepository;
    private VenteRepository venteRepository;
    private ArticleRepository articleRepository;
    private ClientRepository clientRepository;
    private EntrepriseRepository entrepriseRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private LigneVenteRepository ligneVenteRepository;

    @Autowired
    public StatistiqueServiceImpl(CommandeClientRepository commandeClientRepository, VenteRepository venteRepository, ArticleRepository articleRepository, ClientRepository clientRepository, EntrepriseRepository entrepriseRepository, LigneCommandeClientRepository ligneCommandeClientRepository, LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository, LigneVenteRepository ligneVenteRepository) {
        this.commandeClientRepository = commandeClientRepository;
        this.venteRepository = venteRepository;
        this.articleRepository = articleRepository;
        this.clientRepository = clientRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.ligneVenteRepository = ligneVenteRepository;
    }


    @Override
    public List<ArticleQuantiteDto> getArticlesPlusVendus(Integer idEntreprise) {
        // Implémentez le calcul des articles les plus vendus
        // Utilisez le repository ArticleRepository pour accéder aux données
        if (idEntreprise == null || entrepriseRepository.findById(idEntreprise) == null){
            throw new EntityNotFoundException("Verifier l'ID d'entreprise il doit exister dans la BD et aussi n'est pas null", ErrorCodes.ENTREPRISE_NOT_FOUND);
        }
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByIdEntreprise(idEntreprise);
        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByIdEntreprise(idEntreprise);

        Map<Article, BigDecimal> articleQuantiteMap = new HashMap<>();

        // Parcours des lignes de commande client
        for (LigneCommandeClient ligneCommandeClient : ligneCommandeClients) {
            Article article = ligneCommandeClient.getArticle();
            BigDecimal quantite = ligneCommandeClient.getQuantite();

            articleQuantiteMap.put(article, articleQuantiteMap.getOrDefault(article, BigDecimal.ZERO).add(quantite));
        }

        // Parcours des lignes de vente
        for (LigneVente ligneVente : ligneVentes) {
            Article article = ligneVente.getArticle();
            BigDecimal quantite = ligneVente.getQuantite();

            articleQuantiteMap.put(article, articleQuantiteMap.getOrDefault(article, BigDecimal.ZERO).add(quantite));
        }

        List<ArticleQuantiteDto> articleQuantiteDtoList = new ArrayList<>();

        // Transformation de la Map en liste d'objets ArticleQuantiteDto
        for (Map.Entry<Article, BigDecimal> entry : articleQuantiteMap.entrySet()) {
            Article article = entry.getKey();
            BigDecimal quantiteVendue = entry.getValue();
            ArticleDto articleDto = ArticleDto.fromEntity(article);
            ArticleQuantiteDto articleQuantiteDto = new ArticleQuantiteDto(articleDto, quantiteVendue);
            articleQuantiteDtoList.add(articleQuantiteDto);
        }

        // Tri de la liste par quantité vendue (du plus vendu au moins vendu)
        articleQuantiteDtoList.sort((dto1, dto2) -> dto2.getQuantite().compareTo(dto1.getQuantite()));

        return articleQuantiteDtoList;

    }

    @Override
    public List<FideliteClientDto> getClientsPlusFidels(Integer idEntreprise) {
        // Implémentez le calcul des clients les plus fidèles
        // Utilisez le repository ClientRepository pour accéder aux données
        if (idEntreprise == null || entrepriseRepository.findById(idEntreprise) == null){
            throw new EntityNotFoundException("Verifier l'ID d'entreprise il doit exister dans la BD et aussi n'est pas null", ErrorCodes.ENTREPRISE_NOT_FOUND);
        }
        List<Client> clients = clientRepository.findAllByIdEntreprise(idEntreprise);

        List<FideliteClientDto> magasinClients = new ArrayList<>();
        List<FideliteClientDto> grosClients = new ArrayList<>();

        for (Client client : clients) {
            BigDecimal montantTotalAchat = BigDecimal.ZERO;

            // Parcours des commandes du client
            for (CommandeClient commande : client.getCommandesClient()) {
                // Parcours des lignes de commande de la commande
                for (LigneCommandeClient ligneCommande : commande.getLigneCommandeClients()) {
                    BigDecimal prixUnitaire = ligneCommande.getPrixUnitaire();
                    BigDecimal quantite = ligneCommande.getQuantite();
                    montantTotalAchat = montantTotalAchat.add(prixUnitaire.multiply(quantite));
                }
            }

            // Création d'un DTO pour le client avec le montant total dépensé
            FideliteClientDto fideliteClientDto = new FideliteClientDto(ClientDto.fromEntity(client), montantTotalAchat);

            // Ajout du client dans la liste appropriée en fonction du type de client
            if (client.getTypeClient() == TypeClient.MAGASIN) {
                magasinClients.add(fideliteClientDto);
            } else if (client.getTypeClient() == TypeClient.GROS) {
                grosClients.add(fideliteClientDto);
            }
        }

        // Tri des listes par montant total dépensé (du plus grand au plus petit)
        magasinClients.sort((c1, c2) -> c2.getMontantTotalAchat().compareTo(c1.getMontantTotalAchat()));
        grosClients.sort((c1, c2) -> c2.getMontantTotalAchat().compareTo(c1.getMontantTotalAchat()));

        // Combiner les deux listes triées
        List<FideliteClientDto> allClients = new ArrayList<>();
        allClients.addAll(magasinClients);
        allClients.addAll(grosClients);

        return allClients;

    }

    @Override
    public List<ArticleMontantTotalPeriodeDto> getVenteStatistiqueParPeriode(Integer idEntreprise, Instant dateDebut, Instant dateFin) {
        // Implémentez le calcul des statistiques de vente par période
        // Utilisez le repository VenteRepository pour accéder aux données
        if (idEntreprise == null || entrepriseRepository.findById(idEntreprise) == null){
            throw new EntityNotFoundException("Verifier l'ID d'entreprise il doit exister dans la BD et aussi n'est pas null", ErrorCodes.ENTREPRISE_NOT_FOUND);
        }
        List<CommandeClient> commandesClients = commandeClientRepository.findAllByIdEntrepriseAndDateCommandeBetweenAndEtatCommande(idEntreprise, dateDebut, dateFin, EtatCommande.LIVREE);
        List<Vente> ventes = venteRepository.findAllByIdEntrepriseAndDateVenteBetweenAndEtatCommande(idEntreprise, dateDebut, dateFin, EtatCommande.LIVREE);

        // Create a map to store article quantities
        Map<Article, BigDecimal> articleQuantiteMap = new HashMap<>();

        // Process CommandeClient entities
        for (CommandeClient commande : commandesClients) {
            for (LigneCommandeClient ligneCommande : commande.getLigneCommandeClients()) {
                Article article = ligneCommande.getArticle();
                BigDecimal quantite = ligneCommande.getQuantite();
                articleQuantiteMap.put(article, articleQuantiteMap.getOrDefault(article, BigDecimal.ZERO).add(quantite));
            }
        }

        // Process Vente entities
        for (Vente vente : ventes) {
            for (LigneVente ligneVente : vente.getLigneVentes()) {
                Article article = ligneVente.getArticle();
                BigDecimal quantite = ligneVente.getQuantite();
                articleQuantiteMap.put(article, articleQuantiteMap.getOrDefault(article, BigDecimal.ZERO).add(quantite));
            }
        }

        // Create the result list
        List<ArticleMontantTotalPeriodeDto> result = new ArrayList<>();

        // Populate the result list with ArticleMontantTotalPeriodeDto objects
        for (Map.Entry<Article, BigDecimal> entry : articleQuantiteMap.entrySet()) {
            Article article = entry.getKey();
            BigDecimal quantiteTotalPeriod = entry.getValue();
            ArticleDto articleDto = ArticleDto.fromEntity(article);
            result.add(new ArticleMontantTotalPeriodeDto(articleDto, quantiteTotalPeriod));
        }

        // Sort the result list by total quantity sold (from largest to smallest)
        result.sort((a1, a2) -> a2.getQuantiteTotalPeriod().compareTo(a1.getQuantiteTotalPeriod()));

        return result;
    }

    @Override
    public List<ArticleDateLimiteDto> getArticleParDateLimite(Integer idEntreprise) {
        List<Article> articles = articleRepository.findAllByIdEntreprise(idEntreprise);
        Collections.sort(articles, Comparator.comparing(Article::getDateLimiteConsommation));
        List<ArticleDateLimiteDto> articleDateLimiteDtoList = new ArrayList<>();

        for (Article article : articles) {
            ArticleDto articleDto = ArticleDto.fromEntity(article);
            ArticleDateLimiteDto articleDateLimiteDto =
                    new ArticleDateLimiteDto(articleDto, article.getDateLimiteConsommation());
            articleDateLimiteDtoList.add(articleDateLimiteDto);
        }

        return articleDateLimiteDtoList;
    }
}

