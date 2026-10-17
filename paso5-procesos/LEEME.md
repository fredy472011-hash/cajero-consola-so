# Paso 5 — Procesos y estados (acá empieza la materia)

> **Objetivo:** Ver en pantalla cómo un proceso recorre los cinco estados.

**Material de cátedra:** Procesos y Threads, sección 2.1.5 (Estados de los procesos)

---

## Cómo correrlo

```powershell
.\correr
```

A mano:

```bash
javac *.java
java Main
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `EstadoProceso.java` | **NUEVO.** Los 5 estados, como un `enum`. |
| `Bitacora.java` | **NUEVO.** Imprime cada cambio con la hora exacta. |
| `Proceso.java` | **NUEVO.** Una transacción vista como proceso del sistema operativo. |
| `Main.java` | Corre tres procesos y muestra la tabla final. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| `enum` | Un tipo que solo puede valer una de unas opciones fijas. Si te equivocás, no compila. |
| `static` | Pertenece a la CLASE, no a cada objeto. Hay una sola copia compartida. |
| PID | El número único de cada proceso. Igual que en Linux o Windows. |
| `Thread.sleep()` | Detiene la ejecución los milisegundos que se le diga. |
| `InterruptedException` | El error que puede lanzar `sleep` si otro hilo lo interrumpe. |
| operador ternario | `condicion ? siEsCierto : siEsFalso`. Un `if` corto que devuelve un valor. |
| `LocalTime` | Guarda una hora. `LocalTime.now()` da la de este instante. |

---

## Pregunta para pensar

**¿Qué significa que un proceso esté BLOQUEADO?**

Que está esperando una operación de entrada/salida y **no consume CPU**. Mientras el retiro espera al dispensador (1,8 segundos), la CPU podría estar atendiendo a otro proceso. En este paso todavía no lo hace: eso llega en el paso 6.
