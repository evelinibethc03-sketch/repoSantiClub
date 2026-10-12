import java.net.URI;
import java.net.URL;

import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

public class MailBuilder {
    
    private HttpsURLConnection webhook;

    MailBuilder (String link) throws Exception{
        
        URL enlace = new URI(link).toURL();
        this.webhook = (HttpsURLConnection) enlace.openConnection();

    }

    public void enviarMail(String json) throws Exception {

        this.webhook.setRequestMethod("POST");
        this.webhook.setRequestProperty(
            "Content-Type", "application/json; charset=UTF-8"
        );
        this.webhook.setRequestProperty("Accept", "application/json");
        this.webhook.setDoOutput(true);

        try (OutputStream flujoSaliente = this.webhook.getOutputStream()) {
            flujoSaliente.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int codigo = this.webhook.getResponseCode();

        System.out.println("Código HTTP: " + codigo);
        System.out.println("Respuesta: " + this.webhook.getResponseMessage());

        InputStream flujoEntrante =
            (codigo >= 200 && codigo < 400)
            ? this.webhook.getInputStream()
            : this.webhook.getErrorStream();

        if (flujoEntrante != null) {
            try (BufferedReader lector = new BufferedReader(
                    new InputStreamReader(flujoEntrante, StandardCharsets.UTF_8))) {

                String linea;
                while ((linea = lector.readLine()) != null) {
                    System.out.println(linea);
                }
            }
        }

        this.webhook.disconnect();

    }

}
