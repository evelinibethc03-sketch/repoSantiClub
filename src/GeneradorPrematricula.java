import java.sql.ResultSet;

public class GeneradorPrematricula {
    public static void main(String[] args) {

        String url = "jdbc:MySQL://localhost/base_academica_prog03";
        String user = "root";
        String password = "";
        String json = "";
        ResultSet result;

        try {

            CrudManager base = new CrudManager(url, user, password);
            MailBuilder mensajero = new MailBuilder("https://hook.us2.make.com/vxnmcgeqnd8scqv3iwpj1uffuatyxfz3");
            result = base.consultarPrematriculaJson("8-979-2004");
            while (result.next()) {
                json = result.getString("listaJson");
                mensajero.enviarMail(json);
            }
            base.cerrarConexion();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
