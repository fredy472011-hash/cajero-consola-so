# Paso 2 — Muchas cuentas: la lista

> **Objetivo:** Guardar muchos objetos juntos y poder buscarlos.

---

## Cómo correrlo

```powershell
.\correr
```

A mano, si preferís:

```bash
javac *.java
java Main
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `Cuenta.java` | Sin cambios respecto del paso 1. |
| `Banco.java` | **NUEVO.** Guarda la lista de cuentas y sabe buscarlas. |
| `Main.java` | Busca cuentas y muestra qué pasa cuando no existe. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| `import` | Trae una clase que vive en otra parte de Java. |
| `List<Cuenta>` | Una lista DE Cuenta. Lo que va entre `< >` dice qué guarda. |
| `ArrayList` | La implementación concreta de la lista. |
| `.add()` | Agrega un elemento al final. |
| `.size()` | Cuántos elementos hay. |
| for mejorado | `for (Cuenta c : cuentas)` recorre toda la lista sin contador. |
| `null` | "No hay objeto acá". Hay que preguntar por null ANTES de usar el objeto. |
| `final` | La variable no va a apuntar a otra lista. La lista sí puede cambiar por dentro. |

---

## Pregunta para pensar

**Si modificás una cuenta que sacaste de la lista con `buscarPorNumero()`, ¿cambia también la que está en la lista?**

Sí, porque es el **mismo objeto**, no una copia. La lista guarda la dirección del objeto en memoria. Corré el Main y mirá la última demostración.
