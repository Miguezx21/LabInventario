# LabInventario

Sistema web para gestionar el inventario de equipos de un laboratorio de Ingeniería de Software
(laptops, monitores, servidores, visores de realidad virtual, licencias y equipo de red).
Los estudiantes consultan qué hay disponible y los administradores mantienen el inventario.

Proyecto de la **Tarea 1: aplicación de Clean Code y MVC**, ampliado con **login, roles,
contraseñas encriptadas y base de datos MySQL**.

## Índice

1. [Características](#características)
2. [Tecnologías](#tecnologías)
3. [Arquitectura (MVC + Clean Code)](#arquitectura-mvc--clean-code)
4. [Requisitos previos](#requisitos-previos)
5. [Instalación y ejecución](#instalación-y-ejecución)
6. [Credenciales de demostración](#credenciales-de-demostración)
7. [Seguridad y login](#seguridad-y-login)
8. [Rutas de la aplicación](#rutas-de-la-aplicación)
9. [Pruebas automáticas](#pruebas-automáticas)
10. [Estructura del proyecto](#estructura-del-proyecto)
11. [Flujo de trabajo con Git](#flujo-de-trabajo-con-git)
12. [Próximos pasos](#próximos-pasos)

## Características

- Inicio de sesión con usuario y contraseña.
- Dos roles: `ADMIN` (crea, edita y elimina equipos) y `ESTUDIANTE` (solo consulta).
- Ninguna sección es accesible sin sesión iniciada, ni escribiendo la URL a mano ni desde otro enlace.
- Contraseñas guardadas con hash **BCrypt** (nunca en texto plano).
- CRUD completo de equipos con validación de formularios y búsqueda por nombre.
- Base de datos **MySQL**, creada automáticamente al primer arranque.
- Datos de demostración precargados (usuarios y 6 equipos).

## Tecnologías

| Capa | Tecnología |
|------|------------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Vistas | Thymeleaf + Bootstrap 5 |
| Persistencia | Spring Data JPA (Hibernate) + MySQL |
| Seguridad | Spring Security 6 + BCrypt |
| Pruebas | JUnit 5, Mockito, Spring Security Test, H2 (solo en tests) |
| Build | Maven |

## Arquitectura (MVC + Clean Code)

```
Navegador ──► Controller ──► Service ──► Repository ──► MySQL
              (C)            (lógica)    (M)
   ▲                                                       │
   └──────────── Vista Thymeleaf (V) ◄── Model ◄───────────┘
```

- **Modelo (M):** entidades `Equipo` y `Usuario`, y sus repositorios Spring Data JPA (sin SQL escrito a mano).
- **Vista (V):** plantillas Thymeleaf en `src/main/resources/templates`.
- **Controlador (C):** `EquipoController`, `AuthController`. Son delgados: solo reciben la petición, llaman al service y devuelven el nombre de la vista.
- **Service:** `EquipoServiceImpl` y `UsuarioServiceImpl` concentran la lógica de negocio.

Decisiones de Clean Code aplicadas:

- Nombres explícitos (`listarTodos`, `buscarPorId`, `eliminarPorId`, `registrar`).
- Inyección de dependencias **por constructor** (campos `final`), lo que permite probar sin levantar Spring.
- Skinny Controller / Fat Service: el controlador no contiene reglas de negocio.
- Validación declarativa en el modelo (`@NotBlank`, `@NotNull`, `@Min`).
- Excepciones de dominio propias (`EquipoNoEncontradoException`, `UsuarioYaExisteException`).
- La seguridad vive en una sola clase (`SecurityConfig`), separada del negocio.

## Requisitos previos

- **JDK 17** o superior (`java -version`).
- **Maven 3.9+** (`mvn -version`).
- **MySQL 8** en ejecución (puerto 3306).

### Instalar MySQL en Windows

1. Descarga **MySQL Installer** desde <https://dev.mysql.com/downloads/installer/>.
2. Instala "MySQL Server" y, cuando lo pida, define la contraseña del usuario `root` (anótala).
3. Verifica que el servicio esté encendido: abre *Servicios* de Windows y busca `MySQL80` en estado *En ejecución*.
4. Opcional: instala **MySQL Workbench** para ver las tablas.

No hace falta crear la base de datos: la aplicación crea `labinventario` sola.

## Instalación y ejecución

1. Clona el repositorio y entra a la carpeta:

   ```bash
   git clone <URL-DEL-REPOSITORIO>
   cd labinventario
   ```

2. Configura tu contraseña de MySQL. Copia el archivo de ejemplo y edítalo:

   ```bash
   cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
   ```

   En `application-local.properties` escribe tu contraseña:

   ```properties
   spring.datasource.username=root
   spring.datasource.password=TU_CONTRASENA_DE_MYSQL
   ```

   Ese archivo está en `.gitignore`, así que tu contraseña no se sube al repositorio.
   También puedes usar las variables de entorno `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` y `DB_PASSWORD`.

3. Ejecuta la aplicación:

   ```bash
   mvn spring-boot:run
   ```

4. Abre <http://localhost:8080>. Te llevará al login.

## Credenciales de demostración

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `admin` | `Admin123` | ADMIN |
| `estudiante` | `Estudiante123` | ESTUDIANTE |

Son solo para la demo y se crean la primera vez que arranca la aplicación.

## Seguridad y login

**Cómo funciona el login**

1. El usuario abre cualquier ruta sin sesión: Spring Security lo redirige a `/login`.
2. Envía usuario y contraseña. `UsuarioDetallesService` busca el usuario en MySQL y Spring Security compara la contraseña escrita contra el hash BCrypt guardado.
3. Si coincide, se crea la sesión y entra a `/equipos`. Si no, vuelve a `/login?error`.
4. "Cerrar sesión" invalida la sesión y vuelve a `/login?logout`.

**Cómo se impide entrar a la sección protegida**

La regla se aplica en el servidor, en cada petición (`SecurityConfig`):

- `/login` y `/error` son las únicas rutas públicas.
- `/equipos/nuevo`, `/equipos/editar/**`, `/equipos/guardar` y `/equipos/eliminar/**` exigen rol `ADMIN`.
- Cualquier otra ruta exige estar autenticado (`anyRequest().authenticated()`).

Por eso no basta con conocer la URL: escribirla a mano, guardarla en favoritos o abrirla desde otro enlace sin sesión solo lleva al login. Un `ESTUDIANTE` que escriba `/equipos/nuevo` recibe **403 Acceso denegado**. Ocultar botones en la vista es solo comodidad visual; la protección real no depende de eso.

**Encriptación de contraseñas**

- Se usa **BCrypt** (`BCryptPasswordEncoder`): hash con sal aleatoria, deliberadamente lento y de un solo sentido.
- La tabla `usuarios` guarda solo `password_hash` (empieza con `$2a$`), nunca la contraseña real.
- Se eligió BCrypt en lugar de MD5 porque MD5 es rápido de romper y ya no se considera seguro para contraseñas.

Para verlo en MySQL:

```sql
USE labinventario;
SELECT username, password_hash, rol FROM usuarios;
```

**Otras medidas**

- Protección **CSRF** activa: los formularios POST llevan un token secreto.
- Eliminar un equipo es `POST` (no `GET`), para que no pueda dispararse desde un enlace externo.
- Los errores devuelven páginas 403 y 404 propias, sin mostrar detalles internos.

## Rutas de la aplicación

| Método | Ruta | Quién puede | Descripción |
|--------|------|-------------|-------------|
| GET | `/login` | Público | Pantalla de inicio de sesión |
| POST | `/login` | Público | Envía las credenciales |
| POST | `/logout` | Sesión iniciada | Cierra sesión |
| GET | `/equipos` | ADMIN, ESTUDIANTE | Lista de equipos (admite `?nombre=`) |
| GET | `/equipos/nuevo` | ADMIN | Formulario de creación |
| GET | `/equipos/editar/{id}` | ADMIN | Formulario de edición |
| POST | `/equipos/guardar` | ADMIN | Crea o actualiza un equipo |
| POST | `/equipos/eliminar/{id}` | ADMIN | Elimina un equipo |

## Pruebas automáticas

```bash
mvn test
```

Las pruebas usan una base H2 en memoria, por lo que **no necesitan MySQL**. Verifican entre otras cosas que:

- sin sesión, `/equipos`, `/equipos/nuevo`, `/equipos/editar/1` y las operaciones POST redirigen a `/login`;
- el login funciona contra el hash BCrypt y rechaza contraseñas incorrectas;
- un `ESTUDIANTE` recibe 403 en las rutas de administración;
- eliminar sin token CSRF o por un enlace GET no es posible;
- la contraseña se guarda como hash BCrypt y no en texto plano.

## Estructura del proyecto

```
src/main/java/com/udla/labinventario/
├── config/       CargadorDatosDemo (usuarios y equipos de ejemplo)
├── controller/   EquipoController, AuthController, InicioController, ManejadorGlobalDeErrores
├── model/        Equipo, Usuario, CategoriaEquipo, EstadoEquipo, RolUsuario
├── repository/   EquipoRepository, UsuarioRepository
├── security/     SecurityConfig, UsuarioDetallesService
└── service/      EquipoService, UsuarioService (+ impl/ y excepciones de dominio)

src/main/resources/
├── application.properties
├── application-local.properties.example
└── templates/    login.html, equipos/*.html, error/403.html, error/404.html
```

## Flujo de trabajo con Git

Se usan commits pequeños con mensajes en formato `tipo: descripción`:

- `feat:` funcionalidad nueva
- `fix:` corrección de un error
- `docs:` documentación
- `test:` pruebas
- `chore:` configuración y mantenimiento

Nunca se suben contraseñas: `application-local.properties` está en `.gitignore`.

## Próximos pasos

- Pantalla de registro y administración de usuarios.
- Préstamo y devolución de equipos por estudiante.
- Exportar el inventario a PDF o Excel.

## Autor

Miguel Ángel Peña Rodríguez, Ingeniería de Software, UDLA.
