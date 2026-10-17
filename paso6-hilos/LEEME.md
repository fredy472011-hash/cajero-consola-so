# Paso 6 — Hilos y la condición de carrera

> **Objetivo:** Reproducir el error y después arreglarlo. **Es el paso más importante.**

**Material de cátedra:** Procesos y Threads, secciones 2.3.1 (condiciones de carrera) y 2.3.2 (regiones críticas)

---

## Cómo correrlo

```powershell
.\correr
```

**Correlo 3 o 4 veces seguidas.** La primera demostración a veces falla y a
veces no: esa intermitencia es justamente lo que hay que mostrar.

A mano:

```bash
javac *.java
java Main
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `Cuenta.java` | Modificado: ahora tiene las DOS versiones del retiro. |
| `HiloRetiro.java` | **NUEVO.** Un hilo que retira plata. |
| `Main.java` | Tres demostraciones: el error, la solución y 10 hilos a la vez. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| hilo (thread) | Una línea de ejecución que corre EN PARALELO con las demás. |
| `extends Thread` | Heredar: esta clase ES un Thread y además agrega lo suyo. |
| `run()` | El método que se ejecuta en paralelo. |
| `start()` | Arranca el hilo y vuelve de inmediato. **No llamar a `run()` directamente.** |
| `join()` | Esperar a que ese hilo termine. |
| `@Override` | Avisa que este método reemplaza a uno del padre. Si te equivocás el nombre, no compila. |
| `synchronized` | Pone un candado: un solo hilo a la vez puede entrar. |
| condición de carrera | Dos hilos leen el mismo valor antes de que alguno lo grabe. |
| operación atómica | Una que no se puede partir por la mitad. |
| región crítica | El pedazo de código que hay que proteger. |
| `throws` | Avisa que el método puede lanzar un error y que no lo atrapa acá. |

---

## Pregunta para pensar

**¿Por qué `synchronized` y no un semáforo?**

Porque el problema acá es que **un solo hilo** toque el saldo por vez: eso es exclusión mutua, y para eso existe `synchronized`. Un semáforo sirve para limitar a cuántos dejás pasar (hasta N), que es otro problema. En Java, `synchronized` es un **monitor** (sección 2.3.7).
