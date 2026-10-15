package com.igor.notificacao.business;

import com.igor.notificacao.business.dto.TarefasDTO;
import com.igor.notificacao.infrastructure.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.xml.transform.sax.TemplatesHandler;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.email.remetente}")
    public String remetente;


    @Value("${envio.email.nomeremetente}")
    private String nomeremetente;


    public void enviaEmail(TarefasDTO dto){         // metodo que envia a notificao do email
        try{
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());

            // seta o remetente
            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeremetente));

            //seta o destinatario
            mimeMessageHelper.setTo(InternetAddress.parse(dto.getEmailUsuario()));

            //seta o assunto do email
            mimeMessageHelper.setSubject("notificacao de tarefa");

            //seta as variaveis para o template
            Context contex = new Context();
            contex.setVariable("nomeTarefa" , dto.getNomeTarefa());
            contex.setVariable("dataEvento" , dto.getDataEvento());
            contex.setVariable("descricao" , dto.getDescricao());

            String template = templateEngine.process("notificacao", contex);

            //indica que é um texto
            mimeMessageHelper.setText(template, true);

            //quem manda o email
            javaMailSender.send(mensagem);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("erro ao enviar o email" , e.getCause());
        }

    }


}
