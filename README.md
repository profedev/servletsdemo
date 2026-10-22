# Demo de inscripciones con JSP y Servlets

Aplicación web educativa de **Programación Avanzada** que permite inscribir alumnos en cursos y consultar las inscripciones de la sesión actual. Su objetivo es mostrar cómo se conectan un formulario HTML, los servlets, las clases Java y las páginas JSP.

## Funcionalidades

- Mostrar un catálogo de tres cursos: Introducción a Java (24 horas), JSP y Servlets (16 horas) y Bases de datos con JDBC (20 horas).
- Registrar una inscripción con nombre, correo electrónico y curso.
- Listar las inscripciones con alumno, correo, curso y fecha.
- Mostrar mensajes de error y confirmación.

Cada repositorio comienza con una inscripción de ejemplo: Ana Demo en JSP y Servlets.

## Tecnologías y configuración

| Elemento | Configuración del proyecto |
| --- | --- |
| Lenguaje | Java 17 |
| Construcción | Maven |
| Empaquetado | WAR |
| API declarada | Jakarta EE `11.0.0-M1`, con alcance `provided` |
| Controladores | Jakarta Servlets |
| Presentación | JSP, HTML y CSS |
| Descriptor web | `web.xml`, versión 6.0 |
| Almacenamiento | Colecciones Java en la sesión HTTP |

La API Jakarta EE se utiliza para compilar y no se incluye en el WAR: el servidor debe proporcionar las APIs que utiliza la aplicación. El código importa `jakarta.servlet`, por lo que requiere un contenedor compatible con ese espacio de nombres.

## Organización del código

Las clases Java están en `src/main/java/edu/profedev/servletsdemo/`.

| Archivo o carpeta | Responsabilidad |
| --- | --- |
| `modelo/Alumno.java` | Representa un alumno: ID, nombre y correo. |
| `modelo/Curso.java` | Representa un curso: ID, nombre y cantidad de horas. |
| `modelo/Inscripcion.java` | Relaciona un alumno con un curso y registra la fecha. |
| `datos/DemoRepository.java` | Mantiene los cursos y las inscripciones en memoria. |
| `servlet/InicioServlet.java` | Prepara el catálogo para mostrar el formulario. |
| `servlet/InscripcionesServlet.java` | Recibe inscripciones y prepara el listado. |
| `src/main/webapp/index.html` | Dirige al navegador hacia `inicio`. |
| `src/main/webapp/WEB-INF/views/formulario.jsp` | Genera el formulario y muestra errores. |
| `src/main/webapp/WEB-INF/views/listado.jsp` | Genera la tabla de inscripciones. |
| `src/main/webapp/css/estilos.css` | Define la presentación visual. |
| `src/main/webapp/WEB-INF/web.xml` | Configura la página inicial y el tiempo de sesión. |
| `pom.xml` | Declara dependencias y configuración de construcción. |

La separación sigue el enfoque Modelo–Vista–Controlador: las entidades representan los datos, los servlets coordinan las peticiones y las JSP generan el HTML. El repositorio concentra el almacenamiento.

## Flujo de ejecución

### 1. Abrir el formulario

1. El navegador accede a la raíz de la aplicación.
2. El servidor entrega `index.html`, que dirige al navegador a `inicio` mediante una etiqueta HTML `meta refresh`.
3. La petición `GET /inicio` ejecuta `InicioServlet.doGet()`.
4. El servlet obtiene la sesión y busca el atributo `repositorio`. Si no existe, crea un `DemoRepository` y lo guarda en esa sesión.
5. Consulta los cursos y los coloca en el atributo de petición `cursos`.
6. Hace un `forward` a `/WEB-INF/views/formulario.jsp`.
7. La JSP recorre los cursos y genera las opciones del formulario.

### 2. Enviar una inscripción

1. El formulario envía `POST /inscripciones` con los parámetros `nombre`, `email` y `cursoId`.
2. `InscripcionesServlet.doPost()` configura UTF-8 y lee los parámetros.
3. Comprueba que nombre y correo no estén vacíos y que se haya recibido `cursoId`.
4. Convierte el identificador a entero y llama a `DemoRepository.inscribir()`.
5. El repositorio busca el curso, crea un `Alumno`, crea una `Inscripcion` con la fecha actual y la agrega a la colección.
6. El servlet responde con una redirección a `/inscripciones?creada=1`.

Si faltan campos, redirige a `/inicio?error=...`. Si el identificador no es numérico o el curso no existe, redirige con el mensaje de curso inválido. Los valores del formulario no se conservan al regresar por un error.

### 3. Mostrar el listado

1. El navegador sigue la redirección y envía `GET /inscripciones?creada=1`.
2. `InscripcionesServlet.doGet()` obtiene el repositorio de la misma sesión.
3. Coloca la lista en el atributo de petición `inscripciones`.
4. Hace un `forward` a `/WEB-INF/views/listado.jsp`.
5. La JSP genera la tabla y, si recibe `creada=1`, muestra la confirmación.

Se aplica el patrón **POST/Redirect/GET**: después del alta se carga una página mediante GET. Así, actualizar el listado no vuelve a enviar el POST anterior. Este patrón no impide por sí mismo dos envíos independientes del formulario.

## Rutas

Las rutas son relativas al contexto de despliegue de la aplicación.

| Método | Ruta | Resultado |
| --- | --- | --- |
| GET | `/` | Página inicial que dirige a `inicio`. |
| GET | `/inicio` | Formulario de inscripción. |
| POST | `/inscripciones` | Valida y registra la inscripción; luego redirige. |
| GET | `/inscripciones` | Listado de inscripciones de la sesión. |

Las JSP están dentro de `WEB-INF`, por lo que el navegador no puede abrirlas directamente. Se accede a ellas mediante los servlets.

## Petición, sesión y navegación

| Concepto | Uso en esta aplicación |
| --- | --- |
| Parámetro de petición | Datos recibidos del formulario o de la URL mediante `getParameter()`. |
| Atributo de petición | Datos que el servlet entrega a la JSP mediante `setAttribute()`. Duran esa petición. |
| Atributo de sesión | El objeto `DemoRepository`, reutilizado entre peticiones de la misma sesión. |
| `forward()` | Transfiere el procesamiento a una JSP en el servidor, conservando la petición y sus atributos. La URL del navegador no cambia. |
| `sendRedirect()` | Indica al navegador que realice una nueva petición. Cambia la URL y los atributos de la petición anterior no se trasladan. |

Los dos servlets buscan el mismo atributo `repositorio` en la sesión. No se utiliza una instancia global compartida entre todos los usuarios. El método privado sin parámetros `repositorio()` que permanece en `InscripcionesServlet` consulta el contexto de aplicación, pero no participa del flujo actual.

## Almacenamiento y duración de los datos

No se utiliza una base de datos. Cada sesión tiene su propia instancia de `DemoRepository` y sus propias inscripciones. Dos pestañas que comparten sesión ven los mismos datos; sesiones distintas tienen repositorios independientes.

`web.xml` establece una expiración de sesión de **30 minutos de inactividad**. Cuando la sesión se pierde o se crea una nueva, se crea otro repositorio con los datos iniciales. No debe considerarse que las inscripciones sobreviven a reinicios o redespliegues.

El archivo `persistence.xml` declara una unidad de persistencia vacía, pero el flujo de la aplicación no utiliza JPA. Las entidades son clases Java comunes, sin anotaciones de persistencia.

## Compilación y ejecución

### Requisitos

- JDK 17.
- Maven instalado o disponible desde el IDE.
- Un servidor con soporte para Jakarta Servlets y JSP compatible con la configuración del proyecto.
- La dependencia `edu.profedev:prueba:1.0-SNAPSHOT` disponible para Maven: está declarada en el `pom.xml` actual.

Si Maven no encuentra `prueba`, se debe instalar ese proyecto en el repositorio local con `mvn install` desde su propia carpeta, o configurar el repositorio que publica el artefacto. Si se determina que la dependencia no se necesita, su eliminación requiere modificar el `pom.xml`.

### Desde Apache NetBeans

1. Abrir la carpeta que contiene `pom.xml` como proyecto Maven.
2. Configurar el JDK 17.
3. Registrar un servidor compatible y seleccionarlo para el proyecto.
4. Ejecutar **Clean and Build** y luego **Run Project**.
5. Acceder a la URL que indique el servidor.

### Desde una terminal

En la carpeta que contiene `pom.xml`:

```bash
mvn clean package
```

Si la construcción finaliza correctamente, genera:

```text
target/servletsdemo-1.0-SNAPSHOT.war
```

Desplegar ese WAR en el servidor y abrir su contexto. Por ejemplo, si se despliega con el nombre del WAR y el servidor escucha en el puerto 8080:

```text
http://localhost:8080/servletsdemo-1.0-SNAPSHOT/inicio
```

Si el IDE o el servidor lo despliega en el contexto raíz, la URL será `http://localhost:8080/inicio`. El archivo `META-INF/context.xml` declara un contexto vacío; confirmar el contexto efectivo en el servidor. `request.getContextPath()` permite que enlaces y formularios se adapten a ese contexto.

## Prueba manual

1. Abrir `/inicio` y comprobar que se muestran los tres cursos.
2. Cargar nombre, correo y un curso; guardar.
3. Verificar que el listado contiene el registro de ejemplo y la nueva inscripción.
4. Actualizar el listado: no debería repetirse el alta anterior.
5. Volver al formulario y crear otra inscripción.
6. Abrir una sesión independiente y comprobar que comienza con el registro de ejemplo.

## Límites de la versión educativa

- El servidor valida presencia de campos y existencia del curso, pero no valida el formato del correo; el formulario utiliza `type="email"` para la validación del navegador.
- Las JSP insertan directamente algunos valores recibidos del usuario y el parámetro `error`; no hay codificación de salida HTML, por lo que existe riesgo de XSS.
- Los contadores de alumnos e inscripciones comienzan en cero aunque el registro de ejemplo tiene ID 1. La primera alta también recibe ID 1, por lo que los identificadores pueden repetirse.
- La inicialización del repositorio se sincroniza sobre la sesión, pero las altas y sus contadores no están protegidos frente a peticiones concurrentes de la misma sesión.
- No hay autenticación, edición, eliminación ni almacenamiento permanente.

Estos límites describen el código actual y deben resolverse antes de usarlo como una aplicación de producción.
