# Paso 3 — El menú: ahora decide el usuario

> **Objetivo:** Leer del teclado y armar el flujo real de un cajero.

---

## Cómo correrlo

```bash
javac *.java
java Main
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `Cuenta.java` | Sin cambios. |
| `Banco.java` | Sin cambios. |
| `Cajero.java` | **NUEVO.** Toda la lógica del cajero: tarjeta, PIN, menú. |
| `Main.java` | Quedó cortito: solo arma las piezas y arranca. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| `Scanner` | Lee lo que el usuario escribe. `new Scanner(System.in)`. |
| `.nextLine()` | Espera a que escriba y apriete Enter. |
| `.trim()` | Saca los espacios de los costados. |
| `while (true)` | Bucle infinito. Se corta con `break` o `return`. |
| `switch` | Compara una variable contra varios valores. Más claro que muchos `if`. |
| `break` | Sale del `switch` o del bucle. **Si te lo olvidás en un `case`, sigue al de abajo.** |
| `default` | Lo que pasa si no coincide con ningún `case`. |
| `try` / `catch` | "Intentá esto; si explota, hacé esto otro en vez de caerte". |
| `NumberFormatException` | El error que da `Long.parseLong()` cuando el texto no es un número. |

---

## Pregunta para pensar

**¿Por qué `Main` quedó tan corto y toda la lógica está en `Cajero`?**

Porque si mañana el cajero fuera una pantalla gráfica o una página web, `Cajero` casi no cambiaría: solo cambiaría quién le pide los datos al usuario. Separar la lógica de la presentación es lo que permite cambiar una sin romper la otra.
