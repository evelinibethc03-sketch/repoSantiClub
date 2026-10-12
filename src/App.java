import java.lang.reflect.InvocationTargetException;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws Exception {
        
        Connection conexion = null;
        Statement declaracion = null;
        ResultSet result;
        String url = "jdbc:MySQL://localhost/base_academica_prog03";
        String user = "root";
        String password = "";
        String consulta = "SELECT *\r\n" + //
                        "FROM materia\r\n" + //
                        "WHERE codigo IN (\r\n" + //
                        "    SELECT requisitos.cod\r\n" + //
                        "    FROM (\r\n" + //
                        "        SELECT\r\n" + //
                        "            req.id_Materia AS cod,\r\n" + //
                        "            COUNT(DISTINCT req.id_Prerrequisito) AS cantReq\r\n" + //
                        "        FROM materiaprerrequisito AS req\r\n" + //
                        "        GROUP BY req.id_Materia\r\n" + //
                        "    ) AS requisitos\r\n" + //
                        "\r\n" + //
                        "    INNER JOIN (\r\n" + //
                        "        SELECT\r\n" + //
                        "            rq.id_Materia,\r\n" + //
                        "            COUNT(DISTINCT rq.id_Prerrequisito) AS aprobadas\r\n" + //
                        "        FROM materiaprerrequisito AS rq\r\n" + //
                        "        INNER JOIN estudiantemateria AS rg\r\n" + //
                        "            ON rq.id_Prerrequisito = rg.id_Materia\r\n" + //
                        "        WHERE rg.id_estudiante = '8-979-2004'\r\n" + //
                        "          AND rg.calificacion IN ('A', 'B', 'C', 'D')\r\n" + //
                        "        GROUP BY rq.id_Materia\r\n" + //
                        "    ) AS reqPorMateria\r\n" + //
                        "\r\n" + //
                        "        ON requisitos.cod = reqPorMateria.id_Materia\r\n" + //
                        "\r\n" + //
                        "    WHERE requisitos.cantReq = reqPorMateria.aprobadas\r\n" + //
                        ")\r\n" + //
                        "\r\n" + //
                        "AND codigo NOT IN (\r\n" + //
                        "    SELECT id_Materia\r\n" + //
                        "    FROM estudiantemateria\r\n" + //
                        "    WHERE calificacion IN ('A', 'B', 'C', 'D')\r\n" + //
                        "      AND id_Estudiante = '8-979-2004'\r\n" + //
                        ")\r\n" + //
                        "\r\n" + //
                        "AND semestre % 2 = 0\r\n" + //
                        "\r\n" + //
                        "UNION\r\n" + //
                        "\r\n" + //
                        "SELECT *\r\n" + //
                        "FROM materia\r\n" + //
                        "WHERE codigo NOT IN (\r\n" + //
                        "    SELECT DISTINCT id_Materia\r\n" + //
                        "    FROM materiaprerrequisito\r\n" + //
                        ")\r\n" + //
                        "\r\n" + //
                        "AND codigo NOT IN (\r\n" + //
                        "    SELECT id_Materia\r\n" + //
                        "    FROM estudiantemateria\r\n" + //
                        "    WHERE calificacion IN ('A', 'B', 'C', 'D')\r\n" + //
                        "      AND id_Estudiante = '8-979-2004'\r\n" + //
                        ")\r\n" + //
                        "\r\n" + //
                        "AND semestre % 2 = 0;";

        System.out.println("-- Iniciando conexion con el servidor --");
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            conexion = DriverManager.getConnection(url, user, password);
            declaracion = conexion.createStatement();
            result = declaracion.executeQuery(consulta);
            while(result.next()){
                System.out.println(result.getString("codigo") + " " + result.getString("denominacion"));
            }
            conexion.close();
            
        } catch (InvocationTargetException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
