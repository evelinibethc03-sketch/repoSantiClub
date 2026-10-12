SELECT *
FROM materia
WHERE codigo IN (
    SELECT requisitos.cod
    FROM (
        SELECT
            req.id_Materia AS cod,
            COUNT(DISTINCT req.id_Prerrequisito) AS cantReq
        FROM materiaprerrequisito AS req
        GROUP BY req.id_Materia
    ) AS requisitos

    INNER JOIN (
        SELECT
            rq.id_Materia,
            COUNT(DISTINCT rq.id_Prerrequisito) AS aprobadas
        FROM materiaprerrequisito AS rq
        INNER JOIN estudiantemateria AS rg
            ON rq.id_Prerrequisito = rg.id_Materia
        WHERE rg.id_estudiante = '8-979-2004'
          AND rg.calificacion IN ('A', 'B', 'C', 'D')
        GROUP BY rq.id_Materia
    ) AS reqPorMateria

        ON requisitos.cod = reqPorMateria.id_Materia

    WHERE requisitos.cantReq = reqPorMateria.aprobadas
)

AND codigo NOT IN (
    SELECT id_Materia
    FROM estudiantemateria
    WHERE calificacion IN ('A', 'B', 'C', 'D')
      AND id_Estudiante = '8-979-2004'
)

AND semestre % 2 = 0

UNION

SELECT *
FROM materia
WHERE codigo NOT IN (
    SELECT DISTINCT id_Materia
    FROM materiaprerrequisito
)

AND codigo NOT IN (
    SELECT id_Materia
    FROM estudiantemateria
    WHERE calificacion IN ('A', 'B', 'C', 'D')
      AND id_Estudiante = '8-979-2004'
)

AND semestre % 2 = 0;