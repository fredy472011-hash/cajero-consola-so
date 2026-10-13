# Paso 8 — La memoria de largo plazo (MySQL)

> **Objetivo:** Que los datos sobrevivan al cierre del programa.

**Material de cátedra:** Intro 1.5.3 (Gestión de memoria) y 1.5.5 (Ficheros)

---

## Cómo correrlo

```bash
# Una sola vez, crear la base:
mysql -u root -p < ../sql/esquema.sql

# Compilar y ejecutar CON el driver:
javac -cp ".;mysql-connector-j-9.4.0.jar" *.java
java  -cp ".;mysql-connector-j-9.4.0.jar" Main

# Correlo 2 o 3 veces seguidas
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `BaseDeDatos.java` | **NUEVO.** La conexión a MySQL. |
| `BancoBD.java` | **NUEVO.** Reemplaza a `Banco`, pero con los mismos métodos. |
| `Main.java` | Retira, graba, y vuelve a leer desde cero. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| JDBC | La forma estándar de Java para hablar con cualquier base de datos. |
| driver | `java.sql` es solo la interfaz. El `.jar` es quien sabe hablar MySQL. |
| `-cp` | El classpath: dónde buscar las clases. `.` es esta carpeta. En Windows se separa con `;`. |
| `Connection` | La conexión abierta con la base. |
| `PreparedStatement` | Una consulta con huecos `?`. **Protege de inyección SQL.** |
| `ResultSet` | El resultado de un SELECT. `rs.next()` avanza a la fila siguiente. |
| `executeQuery()` | Para SELECT. Devuelve un ResultSet. |
| `executeUpdate()` | Para INSERT, UPDATE y DELETE. Devuelve cuántas filas cambió. |
| try-with-resources | `try (Connection c = ...)` cierra solo lo que abrió, aunque haya error. |
| inyección SQL | Armar el SQL pegando texto deja que alguien cambie la consulta. **Nunca hacerlo.** |
| clave foránea | Obliga a que el dato exista en la otra tabla. La base se protege sola. |

---

## Pregunta para pensar

**¿Cuál es la línea exacta donde el retiro deja de poder perderse?**

`banco.actualizarSaldo(...)`. Antes de esa línea, el retiro vive solo en RAM y un corte de luz lo borraría. Después de esa línea, ya no.
