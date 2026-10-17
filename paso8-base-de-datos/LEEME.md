# Paso 8 — La memoria de largo plazo (MySQL)

> **Objetivo:** Que los datos sobrevivan al cierre del programa.

**Material de cátedra:** Intro 1.5.3 (Gestión de memoria) y 1.5.5 (Ficheros)

---

## Cómo correrlo

**Una sola vez**, crear la base y sus tablas:

```powershell
mysql -u root -p < ../sql/esquema.sql
```

Si tu contraseña de MySQL no es `root`, una sola vez también:

```powershell
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "tu_clave", "User")
```

y cerrar VS Code entero para que tome la variable.

Después, cada vez:

```powershell
.\correr
```

**Correlo 2 o 3 veces seguidas**: el saldo baja en cada corrida y el
historial crece, aunque el programa se cierre entre medio.

A mano, si querés ver los comandos completos:

```bash
javac -cp ".;*" *.java
java  -cp ".;*" Main
```

El `*` significa *"cualquier `.jar` de esta carpeta"*, así no importa qué
versión del driver hayas bajado. En Linux o Mac el separador es `:` en vez
de `;`.

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
