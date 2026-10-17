# Paso 9 — El cajero completo

> **Objetivo:** Todo junto y funcionando. Esto es lo que se presenta.

---

## Cómo correrlo

Lo mismo que el paso 8: la base tiene que estar creada y el `.jar` en
esta carpeta.

El cajero:

```powershell
.\correr
```

La demostración de la condición de carrera:

```powershell
.\correr carrera
```

A mano:

```bash
javac -cp ".;*" *.java
java  -cp ".;*" Main
java  -cp ".;*" Main carrera
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `Cajero.java` | El menú completo, con la demostración de las dos memorias. |
| `Main.java` | Arranca el cajero, o la demo de concurrencia con `java Main carrera`. |
| `(el resto)` | Todas las clases de los pasos anteriores. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| `args` | Los parámetros de la línea de comandos. Preguntar por `args.length` antes de usarlos. |
| composición | El Cajero se arma con las piezas que recibe, no las fabrica adentro. |

---

## Pregunta para pensar

**¿Qué hay que mostrarle al profesor?**

**La opción 7 (CANCELAR).** Borra toda la memoria volátil —tarjeta, PIN, monto— y después vuelve a leer el saldo desde MySQL, que quedó intacto. Esa pantalla sola demuestra la diferencia entre los dos tipos de memoria. Y después `java Main carrera`, que muestra el saldo en −50.000.
