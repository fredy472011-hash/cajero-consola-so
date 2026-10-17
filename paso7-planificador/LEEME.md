# Paso 7 — El planificador de CPU

> **Objetivo:** Resolver con el programa los MISMOS ejercicios que hacés a mano en el Excel.

**Material de cátedra:** Procesos y Threads, sección 2.5 (Planificación)

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
| `Tarea.java` | **NUEVO.** Una tarea con su llegada, duración y prioridad. |
| `Planificador.java` | **NUEVO.** Las cuatro políticas y el diagrama de Gantt. |
| `Main.java` | Resuelve el Caso 3 y el Caso 5 del Excel. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| FCFS | Primero en llegar, primero en ser servido. El más justo, no el más rápido. |
| SJF | Primero el trabajo más corto. Da el mejor promedio de espera. |
| Round Robin | Cada uno corre un quantum y **vuelve al final de la fila**. |
| quantum | El tiempo máximo que puede correr seguido. Es un máximo, no una obligación. |
| inanición | Una tarea larga nunca se ejecuta porque siempre llegan cortas. |
| `continue` | Salta a la siguiente vuelta del bucle sin terminar la actual. |
| clase anidada | Una clase escrita adentro de otra, porque no se usa en ningún otro lado. |
| método burbuja | El ordenamiento más simple: compara vecinos y los intercambia. |
| sobrecarga | Dos métodos con el mismo nombre pero distintos parámetros. |
| `this(...)` | Llama a otro constructor de la misma clase. |

---

## Pregunta para pensar

**¿Los números del programa coinciden con los de tu Excel?**

Tienen que coincidir. Caso 3: ejecución **3, 9, 2, 12, 8** (promedio 6,80) y espera **0, 4, 0, 7, 3** (promedio 2,80). Caso 5: ejecución **21, 11, 2, 13, 18** (promedio 13) y espera **14, 8, 0, 9, 13** (promedio 8,8). Que el cálculo a mano y el programa den lo mismo es la mejor prueba para la defensa.
