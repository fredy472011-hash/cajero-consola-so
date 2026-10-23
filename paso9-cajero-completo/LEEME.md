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
javac -cp ".;*" -d out Principal\*.java Vista\*.java Clases\*.java Sistema\*.java BaseDatos\*.java Service\*.java Interfaces_General\*.java
java  -cp "out;*" Principal.Main
java  -cp "out;*" Principal.Main carrera
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `Principal/Main.java` | Arranca el cajero, o la demo de concurrencia con `.\correr carrera`. |
| `Vista/Cajero.java` | El menú completo, con la demostración de las dos memorias. |
| `Clases/` | Los datos: `Cuenta`, `Billete`. |
| `Sistema/` | Lo de sistemas operativos: `Proceso`, `EstadoProceso`, `Dispensador`, `Bitacora`, `HiloRetiro`. |
| `BaseDatos/` | Hablar con MySQL: `BaseDeDatos`, `BancoBD`, `BilleteDto`. |
| `Service/` | La lógica de negocio (las reglas): `BilleteService`. |
| `Interfaces_General/` | Los contratos: `CrudSimpleInterface`. |

Cada carpeta es un paquete. Los `.class` compilados quedan en la carpeta `out`.

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| `args` | Los parámetros de la línea de comandos. Preguntar por `args.length` antes de usarlos. |
| composición | El Cajero se arma con las piezas que recibe, no las fabrica adentro. |

---

## Pregunta para pensar

**¿Qué hay que mostrarle al profesor?**

**La opción 7 (CANCELAR).** Borra toda la memoria volátil —tarjeta, PIN, monto— y después vuelve a leer el saldo desde MySQL, que quedó intacto. Esa pantalla sola demuestra la diferencia entre los dos tipos de memoria. Y después `.\correr carrera`, que muestra el saldo en −50.000.
