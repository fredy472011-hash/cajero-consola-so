# Paso 1 — Una clase y un objeto

> **Objetivo:** Entender qué es una clase, un objeto y por qué los atributos se esconden.

---

## Cómo correrlo

```powershell
.\correr
```

Eso compila y ejecuta de una. Por dentro hace estos dos comandos, que
podés escribir a mano si querés ver qué pasa:

```bash
javac *.java
java Main
```

## Archivos

| Archivo | Qué hace |
|---|---|
| `Cuenta.java` | El molde de una cuenta bancaria: qué datos tiene y qué sabe hacer. |
| `Main.java` | Crea dos cuentas y las hace operar. |

## Lo que aparece nuevo en este paso

| Palabra | Qué significa |
|---|---|
| clase | El molde. Describe qué datos tiene algo y qué sabe hacer. |
| objeto | Una cosa concreta hecha con ese molde. `cuenta1` y `cuenta2` son dos objetos distintos. |
| atributo | Un dato que guarda el objeto: `saldo`, `titular`. |
| `private` | Nadie de afuera puede tocar ese atributo directamente. |
| constructor | El método que corre al crear el objeto. Se llama igual que la clase. |
| `this` | "este objeto". Sirve cuando el atributo y el parámetro se llaman igual. |
| `new` | Fabrica un objeto nuevo. |
| getter | Método que deja LEER un atributo privado, pero no cambiarlo. |
| `long` | Número entero grande. `int` se queda corto para guaraníes. |
| `.equals()` | Para comparar textos. Con `==` se compara otra cosa y falla. |
| `printf` | Imprime con formato: `%s` texto, `%,d` número con puntos, `%n` salto de línea. |

---

## Pregunta para pensar

**¿Por qué `saldo` es `private` y no hay un `setSaldo()`?**

Porque el saldo solo puede cambiar depositando o retirando, y esos métodos controlan las reglas. Si existiera `setSaldo()`, cualquiera podría poner el saldo en lo que quisiera y las reglas no servirían de nada. Eso se llama **encapsulamiento**.
