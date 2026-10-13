# Paso 4 — El dispensador de billetes

> **Objetivo:** Entender por qué hay montos que se rechazan aunque haya saldo.

---

## Cómo correrlo

```bash
javac *.java
java Main
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `Dispensador.java` | **NUEVO.** Los casetes y el algoritmo de cambio. |
| `Cajero.java` | Modificado: ahora el retiro pasa por el dispensador. |
| `Main.java` | Muestra primero una demostración del algoritmo. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| arreglo (array) | Una fila de casilleros de tamaño fijo. `int[] a = {10, 20};` |
| índice | `a[0]` es el primero. **Se empieza a contar en cero.** |
| `.length` | Cuántos casilleros tiene el arreglo. Sin paréntesis, no es un método. |
| `static final` | Constante compartida por toda la clase. Por costumbre va en MAYÚSCULAS. |
| for clásico | `for (int i = 0; i < n; i++)` cuando hace falta el índice. |
| división entera | `250000 / 100000` da **2**, no 2,5. Java tira los decimales entre enteros. |
| cast `(int)` | Convierte de un tipo a otro: `(int) unLong`. |
| `Math.min()` | Devuelve el menor de dos números. |
| `StringBuilder` | Para ir pegando texto de a pedazos sin crear un String nuevo cada vez. |
| algoritmo voraz | En cada paso agarra lo más grande que puede y nunca se arrepiente. |

---

## Pregunta para pensar

**¿Por qué se rechaza un pedido de Gs. 30.000 si la cuenta tiene millones?**

Porque el cajero solo tiene billetes de 50.000 y 100.000. No existe ninguna combinación que dé 30.000. Un cajero real falla igual: el saldo es una condición, pero no la única.
