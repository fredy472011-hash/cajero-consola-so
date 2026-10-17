# Cajero Automático — versión de consola, paso a paso

Trabajo práctico de **Informática VI — Sistemas Operativos**.

Este proyecto está pensado para **rehacerlo a mano**, no para copiarlo.
Son 9 pasos, cada uno en su carpeta, y **cada uno corre solo**.

---

## La regla

> Cada carpeta es un programa completo e independiente.
> Entrás, compilás, corrés, y funciona. No depende de las otras.

Eso significa que algunos archivos se repiten entre carpetas. Es a propósito:
así podés abrir el paso 5 sin tener que entender el 1 al 4 primero, y podés
romper un paso sin romper los demás.

---

## Cómo trabajar

Para cada paso, en orden:

1. Abrí el `LEEME.md` de la carpeta y leé el **objetivo**
2. Corré el programa y mirá qué hace
3. Leé el código. Los comentarios explican cada palabra nueva
4. **Borralo y escribilo de nuevo de memoria.** Acá es donde se aprende
5. Respondé la **pregunta para pensar** del final

El paso 4 es el que separa a quien leyó de quien entendió.

---

## Los 9 pasos

| # | Carpeta | Qué se aprende | Necesita MySQL |
|---|---|---|---|
| 1 | `paso1-una-clase` | Clases, objetos, atributos privados, constructor | no |
| 2 | `paso2-varias-cuentas` | Listas, buscar, `null` | no |
| 3 | `paso3-menu` | `Scanner`, `while`, `switch`, `try/catch` | no |
| 4 | `paso4-dispensador` | Arreglos, `for` con índice, algoritmo voraz | no |
| 5 | `paso5-procesos` | **Los 5 estados de un proceso**, `enum` | no |
| 6 | `paso6-hilos` | **Condición de carrera y `synchronized`** | no |
| 7 | `paso7-planificador` | **FCFS, SJF, Round Robin** y el Gantt | no |
| 8 | `paso8-base-de-datos` | **JDBC**, memoria de largo plazo | sí |
| 9 | `paso9-cajero-completo` | Todo junto | sí |

Los pasos 1 a 7 no necesitan nada instalado más que Java.

---

## Qué hay que tener instalado

### Para los pasos 1 a 7 — solo Java

**JDK 17 o posterior** (sirve 17, 21 o 25). Hace falta el **JDK**, no el
JRE: el JRE solo ejecuta, no trae `javac`.

> https://adoptium.net/es/temurin/releases/ → Windows x64, JDK, `.msi`

Durante la instalación, en la pantalla de opciones, **tildar las dos**:

- ☑ **Add to PATH**  ← sin esto `javac` no funciona en la terminal
- ☑ **Set JAVA_HOME variable**

Comprobar (los dos tienen que responder):

```bash
java -version
javac -version     # si este falla, falta el PATH o instalaste el JRE
```

### Si dice `'javac' no se reconoce como un comando`

Falta la carpeta `bin` del JDK en la variable **`Path`** de Windows.

**Importante:** son dos variables distintas y hacen cosas distintas.

| Variable | Valor | Para qué |
|---|---|---|
| `JAVA_HOME` | `C:\Program Files\Java\jdk-21` **sin `\bin`** | La usan Maven, Gradle y los IDEs |
| `Path` | debe incluir `%JAVA_HOME%\bin` **con `\bin`** | La usa la terminal para encontrar `javac` |

Poner solo `JAVA_HOME` **no alcanza**: cuando escribís `javac`, Windows
busca en `Path`, no en `JAVA_HOME`.

**Para encontrar dónde quedó instalado el JDK:**

```powershell
Get-ChildItem "C:\Program Files\" -Recurse -Depth 3 -Filter javac.exe -ErrorAction SilentlyContinue | Select-Object -First 3 FullName
```

**Para arreglarlo:**

1. Tecla Windows → "variables de entorno" → *Editar las variables de entorno del sistema*
2. Botón **Variables de entorno…**
3. `JAVA_HOME` = la carpeta del JDK, **sin** `\bin`
4. En `Path` → **Nuevo** → agregar `%JAVA_HOME%\bin`
5. **Cerrar VS Code entero y volver a abrirlo.** No alcanza con abrir otra
   pestaña de terminal: las ventanas abiertas se quedan con las variables
   viejas.

**Solo para probar en la terminal actual, sin reiniciar nada:**

```powershell
$env:Path = "C:\Program Files\Java\jdk-21\bin;" + $env:Path
javac -version
```

Eso vale únicamente para esa ventana.

### Si dice `UnsupportedClassVersionError`

```
Main has been compiled by a more recent version of the Java Runtime
(class file version 69.0), this version of the Java Runtime only
recognizes class file versions up to 52.0
```

Hay **dos Java instalados**: uno compila y otro ejecuta.

Los numeros son versiones de Java:

| class file version | Java |
|---|---|
| 52.0 | Java 8 |
| 61.0 | Java 17 |
| 65.0 | Java 21 |
| 69.0 | Java 25 |

El mensaje de arriba dice: lo compilo Java 25, pero lo ejecuta Java 8.

**Por que pasa.** Oracle instala una carpeta
`C:\Program Files\Common Files\Oracle\Java\javapath` que **solo tiene
`java.exe`**, sin `javac.exe`, y suele quedar primera en el `Path`.
Entonces `javac` no la encuentra ahi y sigue buscando hasta el JDK nuevo,
pero `java` si la encuentra y se queda con el viejo.

**Comprobar cual se usa:**

```powershell
where.exe java
where.exe javac
java -version
```

`where.exe` lista todas las rutas encontradas. **Gana la primera.**

**Arreglarlo:** en las variables de entorno, dentro de `Path`, subir
`%JAVA_HOME%\bin` por encima de la linea de Oracle con el boton **Subir**.
O borrar la linea de Oracle, que es de un Java 8 viejo.

Despues cerrar VS Code entero y comprobar que `java -version` y
`javac -version` digan **la misma version**.

**Solo para la terminal actual:**

```powershell
$env:Path = "C:\Program Files\Java\jdk-25\bin;" + $env:Path
```

---

### Para los pasos 8 y 9 — además MySQL

**MySQL 8 Server**

> https://dev.mysql.com/downloads/installer/ → opción **Server only**
> Anotá la contraseña de `root`: hace falta después.

**El driver JDBC** (`mysql-connector-j-9.4.0.jar`)

> https://dev.mysql.com/downloads/connector/j/ → **Platform Independent**

Ya está copiado dentro de `paso8-base-de-datos/` y `paso9-cajero-completo/`.
Si usás otra versión, cambiá el nombre en los comandos.

**Crear la base** (una sola vez):

```bash
mysql -u root -p < sql/esquema.sql
```

**Si tu `root` tiene otra contraseña**, en PowerShell antes de ejecutar:

```powershell
$env:DB_PASSWORD = "tu_contrasena"
```

Las credenciales **no están escritas en el código**: se leen de variables
de entorno. En un repositorio público, una contraseña escrita en el código
queda publicada para siempre.

---

## Cómo correr un paso

### La forma corta

Cada carpeta tiene un `correr.bat` que compila y ejecuta de una:

```powershell
cd paso1-una-clase
.\correr
```

En Linux o Mac: `bash correr.sh`

Si hay un error de compilación se detiene y lo dice, en vez de ejecutar
una versión vieja del programa.

### En VS Code: una sola tecla

Abrí cualquier archivo del paso y apretá **Ctrl+Shift+B**.

Compila y ejecuta la carpeta de ese archivo. Si estás en `paso5-procesos/Proceso.java`,
corre el paso 5.

### Los comandos de verdad

El `.bat` no hace magia: son estos dos.

**Pasos 1 a 7:**

```bash
javac *.java
java Main
```

**Pasos 8 y 9** (hay que incluir el driver):

```bash
javac -cp ".;mysql-connector-j-9.4.0.jar" *.java
java  -cp ".;mysql-connector-j-9.4.0.jar" Main
```

En Linux o Mac, el separador del classpath es `:` en lugar de `;`.

> `javac` **no dice nada cuando sale bien.** El silencio significa que compiló.
> Solo habla si hay errores.

> **`-cp` es el classpath**: dónde buscar las clases. El `.` significa
> "también en esta carpeta". Si lo olvidás en el paso 8, el error es
> `No suitable driver found for jdbc:mysql://...`

---

## Los temas de la materia y dónde se ven

| Tema | Paso | Cómo se demuestra |
|---|---|---|
| Procesos y sus 5 estados | 5 | La bitácora imprime cada cambio con la hora |
| Entrada/salida bloqueante | 5 | El retiro pasa a `BLOQUEADO` 1,8 segundos |
| Concurrencia | 6 | Dos hilos retiran al mismo tiempo |
| Condición de carrera | 6 | El saldo queda en **−50.000** |
| Exclusión mutua | 6 | `synchronized` lo corrige |
| Planificación de CPU | 7 | FCFS, SJF, prioridad y Round Robin comparados |
| Inanición | 7 | SJF deja esperando a las tareas largas |
| Memoria volátil | 9 | CANCELAR borra tarjeta, PIN y monto |
| Memoria de largo plazo | 8 y 9 | El saldo en MySQL sobrevive al cierre |
| Recursos limitados | 4 | El dispensador rechaza montos que no puede formar |

---

## Las dos cosas que hay que mostrar en la defensa

**1. La condición de carrera** (paso 6, o `java Main carrera` en el paso 9)

```
Saldo inicial: Gs. 150.000
  Cajero-2 pidio Gs. 100.000  ->  SE LO DIO
  Cajero-1 pidio Gs. 100.000  ->  SE LO DIO
Saldo final:   Gs. -50.000
```

El banco entregó 200.000 teniendo 150.000. Aparecieron Gs. 50.000 de la nada.
Después, con `synchronized`, el mismo escenario da el resultado correcto.

**2. Las dos memorias** (paso 9, opción 7)

CANCELAR borra tarjeta, PIN y monto en curso. Después vuelve a leer el saldo
desde MySQL: quedó intacto. Una memoria se borró entera, la otra no se tocó.

---

## Preguntas que puede hacer el profesor

| Pregunta | Dónde está la respuesta |
|---|---|
| ¿Por qué `synchronized` y no un semáforo? | `LEEME.md` del paso 6 |
| ¿Qué significa que un proceso esté BLOQUEADO? | `LEEME.md` del paso 5 |
| ¿Por qué se rechaza un retiro de 30.000? | `LEEME.md` del paso 4 |
| ¿Dónde está exactamente la memoria volátil? | `Cajero.java` del paso 9 |
| ¿Qué política de planificación es mejor? | Última pantalla del paso 7 |
| ¿Qué es la inyección SQL y cómo la evitan? | `BancoBD.java` del paso 8 |

---

## Cuentas de prueba

| Tarjeta | PIN | Saldo inicial |
|---|---|---|
| 1001 | 1234 | Gs. 2.500.000 |
| 1002 | 4321 | Gs. 1.800.000 |
| 1003 | 0000 | Gs. 30.000 |

Para volver los saldos al estado original, correr de nuevo:

```bash
mysql -u root -p < sql/esquema.sql
```
