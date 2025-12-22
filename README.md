# Expense Control Java

Sistema de control de gastos desarrollado con Java, JSF, PrimeFaces y MySQL.

## Características

- **Backend**: Java 8 (1.8) con JPA/Hibernate
- **Frontend**: JSF 2.2 con PrimeFaces 8.0
- **Base de datos**: MySQL 8.0
- **Servidor**: Compatible con Tomcat 8.5
- **Autenticación**: Sistema de login con encriptación de contraseñas
- **CRUD de usuarios**: Gestión completa de usuarios con roles
- **Interfaz responsiva**: Diseño moderno con PrimeFaces

## Estructura del Proyecto

```
expense_control_java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/expensecontrol/
│   │   │       ├── controller/     # Controladores JSF
│   │   │       ├── dao/            # Acceso a datos
│   │   │       ├── model/          # Entidades JPA
│   │   │       ├── service/        # Lógica de negocio
│   │   │       └── util/           # Utilidades
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml # Configuración JPA
│   │   └── webapp/
│   │       ├── admin/              # Páginas de administración
│   │       ├── error/              # Páginas de error
│   │       ├── resources/css/      # Estilos CSS
│   │       ├── template/           # Plantillas JSF
│   │       ├── WEB-INF/           # Configuración web
│   │       ├── dashboard.xhtml     # Dashboard principal
│   │       └── login.xhtml         # Página de login
└── target/
    └── expense-control.war         # Archivo WAR generado
```

## Configuración de Base de Datos

La aplicación está configurada para conectarse a:

- **Host**: networklaguna.com
- **Base de datos**: networkla_expensesctl
- **Usuario**: networkla_expnctl
- **Contraseña**: mipassword$123

### Esquema de Base de Datos

La aplicación creará automáticamente la tabla `users` con la siguiente estructura:

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);
```

## Despliegue en Tomcat 8.5

### Requisitos

- Java 8 (1.8)
- Apache Tomcat 8.5
- MySQL 8.0
- Acceso a la base de datos configurada

### Pasos de Despliegue

1. **Generar el archivo WAR**:
   ```bash
   mvn clean package
   ```

2. **Copiar el WAR a Tomcat**:
   ```bash
   cp target/expense-control.war $TOMCAT_HOME/webapps/
   ```

3. **Iniciar Tomcat**:
   ```bash
   $TOMCAT_HOME/bin/startup.sh
   ```

4. **Acceder a la aplicación**:
   ```
   http://localhost:8080/expense-control/
   ```

### Usuario Administrador por Defecto

Al iniciar la aplicación por primera vez, se crea automáticamente un usuario administrador:

- **Usuario**: admin
- **Contraseña**: admin123

## Funcionalidades

### Sistema de Autenticación
- Login con usuario y contraseña
- Encriptación de contraseñas con BCrypt
- Gestión de sesiones
- Redirección automática según permisos

### Gestión de Usuarios (Solo Administradores)
- Crear nuevos usuarios
- Editar información de usuarios existentes
- Activar/desactivar usuarios
- Eliminar usuarios
- Asignar roles (ADMIN/USER)
- Validación de datos

### Dashboard
- Información del usuario actual
- Estadísticas del sistema
- Accesos rápidos a funcionalidades
- Navegación intuitiva

## Desarrollo

### Compilar el proyecto
```bash
mvn clean compile
```

### Ejecutar tests
```bash
mvn test
```

### Generar WAR
```bash
mvn clean package
```

### Ejecutar con Cargo (Tomcat 8.5)
```bash
mvn cargo:run
```

## Tecnologías Utilizadas

- **Java 8 (1.8)**: Lenguaje de programación
- **Maven 3.9**: Gestión de dependencias y build
- **JSF 2.2**: Framework web
- **PrimeFaces 8.0**: Componentes UI
- **JPA/Hibernate 5.4.32.Final**: ORM
- **MySQL Connector 8.0.33**: Driver de base de datos
- **HikariCP 3.4.5**: Pool de conexiones
- **BCrypt (jBCrypt 0.4)**: Encriptación de contraseñas
- **Weld Servlet 2.4.8.Final**: Implementación CDI
- **SLF4J 1.7 + Logback 1.2**: Logging

## Estructura de Archivos de Configuración

### persistence.xml
Configuración de JPA con conexión a MySQL y pool de conexiones HikariCP.

### web.xml
Configuración del servlet JSF, filtros, y páginas de error.

### faces-config.xml
Configuración de navegación y recursos de JSF.

### beans.xml
Configuración de CDI para inyección de dependencias.

## Personalización

### Cambiar Base de Datos
Editar `src/main/resources/META-INF/persistence.xml` con los nuevos parámetros de conexión.

### Modificar Tema
Cambiar el tema de PrimeFaces en `web.xml`:
```xml
<context-param>
    <param-name>primefaces.THEME</param-name>
    <param-value>nuevo-tema</param-value>
</context-param>
```

### Agregar Nuevas Funcionalidades
1. Crear entidades en `model/`
2. Implementar DAOs en `dao/`
3. Crear servicios en `service/`
4. Desarrollar controladores en `controller/`
5. Diseñar páginas XHTML en `webapp/`

## Soporte

Para soporte técnico o consultas sobre el proyecto, contactar al equipo de desarrollo.

## Licencia

Este proyecto es de uso interno y está sujeto a las políticas de la organización.