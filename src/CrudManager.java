import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class CrudManager {
    
    private Connection conn;

    CrudManager(String url, String user, String password) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        this.conn = DriverManager.getConnection(url, user, password);

    }

    public ResultSet consultarPrematricula (String cedula) throws Exception{

        String consulta = "SELECT * " + //
                        "FROM materia " + //
                        "WHERE codigo IN ( " + //
                        "SELECT requisitos.cod " + //
                        "FROM ( " + //
                        "SELECT " + //
                        "req.id_Materia AS cod, " + //
                        "COUNT(DISTINCT req.id_Prerrequisito) AS cantReq " + //
                        "FROM materiaprerrequisito AS req " + //
                        "GROUP BY req.id_Materia " + //
                        ") AS requisitos " + //
                        "INNER JOIN ( " + //
                        "SELECT " + //
                        "rq.id_Materia, " + //
                        "COUNT(DISTINCT rq.id_Prerrequisito) AS aprobadas " + //
                        "FROM materiaprerrequisito AS rq " + //
                        "INNER JOIN estudiantemateria AS rg " + //
                        "ON rq.id_Prerrequisito = rg.id_Materia " + //
                        "WHERE rg.id_estudiante = '" + cedula +"' " + //
                        "AND rg.calificacion IN ('A', 'B', 'C', 'D') " + //
                        "GROUP BY rq.id_Materia " + //
                        ") AS reqPorMateria " + //
                        "ON requisitos.cod = reqPorMateria.id_Materia " + //
                        "WHERE requisitos.cantReq = reqPorMateria.aprobadas " + //
                        ") " + //
                        "AND codigo NOT IN ( " + //
                        "SELECT id_Materia " + //
                        "FROM estudiantemateria " + //
                        "WHERE calificacion IN ('A', 'B', 'C', 'D') " + //
                        "AND id_Estudiante = '" + cedula + "' " + //
                        ") " + //
                        "AND semestre % 2 = 0 " + //
                        "UNION " + //
                        "SELECT * " + //
                        "FROM materia " + //
                        "WHERE codigo NOT IN ( " + //
                        "SELECT DISTINCT id_Materia " + //
                        "FROM materiaprerrequisito " + //
                        ") " + //
                        "AND codigo NOT IN ( " + //
                        "SELECT id_Materia " + //
                        "FROM estudiantemateria " + //
                        "WHERE calificacion IN ('A', 'B', 'C', 'D') " + //
                        "AND id_Estudiante = '" + cedula + "' " + //
                        ") " + //
                        "AND semestre % 2 = 0;";
        
        return this.conn.createStatement().executeQuery(consulta);

    }

    public ResultSet consultarPrematriculaJson (String cedula) throws Exception{

        String consulta = "SELECT CONCAT('[', GROUP_CONCAT( JSON_OBJECT( " + //
                        "'codigo', codigo, " + //
                        "'denominacion', denominacion, " + //
                        "'tipo', tipo, " + //
                        "'semestre', semestre " + //
                        ") SEPARATOR ','), ']') AS listaJson FROM ( " + //
                        "SELECT * " + //
                        "FROM materia " + //
                        "WHERE codigo IN ( " + //
                        "SELECT requisitos.cod " + //
                        "FROM ( " + //
                        "SELECT " + //
                        "req.id_Materia AS cod, " + //
                        "COUNT(DISTINCT req.id_Prerrequisito) AS cantReq " + //
                        "FROM materiaprerrequisito AS req " + //
                        "GROUP BY req.id_Materia " + //
                        ") AS requisitos " + //
                        "INNER JOIN ( " + //
                        "SELECT " + //
                        "rq.id_Materia, " + //
                        "COUNT(DISTINCT rq.id_Prerrequisito) AS aprobadas " + //
                        "FROM materiaprerrequisito AS rq " + //
                        "INNER JOIN estudiantemateria AS rg " + //
                        "ON rq.id_Prerrequisito = rg.id_Materia " + //
                        "WHERE rg.id_estudiante = '" + cedula +"' " + //
                        "AND rg.calificacion IN ('A', 'B', 'C', 'D') " + //
                        "GROUP BY rq.id_Materia " + //
                        ") AS reqPorMateria " + //
                        "ON requisitos.cod = reqPorMateria.id_Materia " + //
                        "WHERE requisitos.cantReq = reqPorMateria.aprobadas " + //
                        ") " + //
                        "AND codigo NOT IN ( " + //
                        "SELECT id_Materia " + //
                        "FROM estudiantemateria " + //
                        "WHERE calificacion IN ('A', 'B', 'C', 'D') " + //
                        "AND id_Estudiante = '" + cedula + "' " + //
                        ") " + //
                        "AND semestre % 2 = 0 " + //
                        "UNION " + //
                        "SELECT * " + //
                        "FROM materia " + //
                        "WHERE codigo NOT IN ( " + //
                        "SELECT DISTINCT id_Materia " + //
                        "FROM materiaprerrequisito " + //
                        ") " + //
                        "AND codigo NOT IN ( " + //
                        "SELECT id_Materia " + //
                        "FROM estudiantemateria " + //
                        "WHERE calificacion IN ('A', 'B', 'C', 'D') " + //
                        "AND id_Estudiante = '" + cedula + "' " + //
                        ") " + //
                        "AND semestre % 2 = 0) AS prematricula;";
        
        return this.conn.createStatement().executeQuery(consulta);

    }

    public ResultSet consultarRegistro(String cedula) throws Exception{

        String consulta = "";
        return this.conn.createStatement().executeQuery(consulta);

    }

    public void insertarMaterias(String datos) throws Exception{

        String consulta = "INSERT INTO Materia (codigo, denominacion, tipo, semestre) VALUES " + datos + ";";
        this.conn.createStatement().executeUpdate(consulta);

    }

    public void insertarEstudiantes(String datos) throws Exception{

        String consulta = "INSERT INTO Estudiante (cedula, nombre) VALUES " + datos + ";";
        this.conn.createStatement().executeUpdate(consulta);

    }

    public void insertarRegistros(String datos) throws Exception{

    }

    public void cerrarConexion() throws Exception{

        this.conn.close();

    }


}
