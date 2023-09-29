package com.benzaied.gestiondestock.services.email;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.services.ArticleService;
import com.benzaied.gestiondestock.services.MvmntStckService;
import com.benzaied.gestiondestock.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Autowired
    private JavaMailSender mailSender;

    private final ArticleService articleService;

    private final MvmntStckService mvmntStckService;

    private final UtilisateurService utilisateurService;

    private String body;

    private String subject;

    @Autowired
    public EmailSenderService(ArticleService articleService, MvmntStckService mvmntStckService, UtilisateurService utilisateurService) {
        this.articleService = articleService;
        this.mvmntStckService = mvmntStckService;
        this.utilisateurService = utilisateurService;
    }

    public void sendSimpleEmail() {
        List<ArticleDto> articleDtos = articleService.findAll();
        List<UtilisateurDto> utilisateurDtos = utilisateurService.findAll();

        for (ArticleDto articleDto : articleDtos) {

            BigDecimal stockReel = mvmntStckService.stockReelArticle(articleDto.getId());

            Instant currentDate = Instant.now();

            Instant twentyDaysLater = currentDate.plus(20, ChronoUnit.DAYS);

            Instant tenDaysLater = currentDate.plus(10, ChronoUnit.DAYS);

            if (stockReel.equals(BigDecimal.valueOf(10.0)) || stockReel.equals(BigDecimal.valueOf(5.0))){
                for (UtilisateurDto utilisateurDto : utilisateurDtos) {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(mailUsername);
                    message.setTo(utilisateurDto.getEmail());
                    body = "La quantité de l'article (Nom: " + articleDto.getNomArticle() + ", Code: " + articleDto.getCodeArticle() + ") egale à = " +stockReel ;
                    message.setText(body);
                    subject = "Quantité epuisée";
                    message.setSubject(subject);
                    mailSender.send(message);
                }
            }

            if (articleDto.getDateLimiteConsommation().equals(twentyDaysLater) || articleDto.getDateLimiteConsommation().equals(tenDaysLater)) {
                for (UtilisateurDto utilisateurDto : utilisateurDtos) {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(mailUsername);
                    message.setTo(utilisateurDto.getEmail());
                    long daysDifference = ChronoUnit.DAYS.between(currentDate, articleDto.getDateLimiteConsommation());
                    body = "La date limite de consommation de l'article (Nom: " + articleDto.getNomArticle() + ", Code: " + articleDto.getCodeArticle() + ") est dans " +daysDifference+ "jours" ;
                    message.setText(body);
                    subject = "Date limite de consommation !!";
                    message.setSubject(subject);
                    mailSender.send(message);
                }
            }

        }


    }

}
