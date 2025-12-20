# Configuración de Eclipse para Expense Control

Este documento describe cómo importar y configurar el proyecto Expense Control en Eclipse IDE.

## Requisitos Previos

1. **Eclipse IDE for Enterprise Java and Web Developers** (versión 2021-06 o superior)
2. **Java 11 JDK** instalado y configurado
3. **Apache Tomcat 9.0** instalado
4. **Maven** integrado en Eclipse (incluido por defecto)

## Plugins Necesarios

Asegúrate de tener instalados los siguientes plugins en Eclipse:

- **Maven Integration for Eclipse (m2e)**
- **Eclipse Java EE Developer Tools**
- **JSF Tools (Web Tools Platform)**
- **JPA Tools**
- **CDI Tools**

## Pasos para Importar el Proyecto

### 1. Importar como Proyecto Maven Existente

1. Abre Eclipse IDE
2. Ve a `File` → `Import...`
3. Selecciona `Maven` → `Existing Maven Projects`
4. Haz clic en `Next`
5. En `Root Directory`, navega hasta la carpeta del proyecto `expense_control_java`
6. Eclipse debería detectar automáticamente el archivo `pom.xml`
7. Asegúrate de que el proyecto esté seleccionado
8. Haz clic en `Finish`

### 2. Configurar el JDK

1. Clic derecho en el proyecto → `Properties`
2. Ve a `Java Build Path` → `Libraries`
3. Expande `Modulepath` o `Classpath`
4. Si ves una versión incorrecta de JRE, selecciónala y haz clic en `Remove`
5. Haz clic en `Add Library...` → `JRE System Library` → `Next`
6. Selecciona `Workspace default JRE` o configura Java 11
7. Haz clic en `Finish` → `Apply and Close`

### 3. Configurar Apache Tomcat

1. Ve a `Window` → `Preferences`
2. Navega a `Server` → `Runtime Environments`
3. Haz clic en `Add...`
4. Selecciona `Apache Tomcat v9.0` → `Next`
5. En `Tomcat installation directory`, navega hasta tu instalación de Tomcat 9
6. Asegúrate de que el JRE sea Java 11
7. Haz clic en `Finish` → `Apply and Close`

### 4. Configurar el Servidor en el Proyecto

1. Clic derecho en el proyecto → `Properties`
2. Ve a `Project Facets`
3. Verifica que las siguientes facetas estén habilitadas:
   - Java: 11
   - Dynamic Web Module: 4.0
   - JavaServer Faces: 2.2
   - JPA: 2.2
   - CDI (Contexts and Dependency Injection): 2.0

### 5. Configurar Deployment Assembly

1. En las propiedades del proyecto, ve a `Deployment Assembly`
2. Verifica que estén configuradas las siguientes entradas:
   - `src/main/webapp` → `/`
   - `Maven Dependencies` → `/WEB-INF/lib`
   - `src/main/java` → `/WEB-INF/classes`
   - `src/main/resources` → `/WEB-INF/classes`

### 6. Actualizar Dependencias Maven

1. Clic derecho en el proyecto → `Maven` → `Reload Projects`
2. Si hay errores, clic derecho → `Maven` → `Update Project...`
3. Marca `Force Update of Snapshots/Releases`
4. Haz clic en `OK`

## Configuración de Base de Datos

### 1. Configurar Conexión a Base de Datos

1. Ve a `Window` → `Show View` → `Other...`
2. Busca y selecciona `Data Source Explorer`
3. Clic derecho en `Database Connections` → `New...`
4. Selecciona `MySQL` → `Next`
5. Configura los siguientes parámetros:
   - **Host**: networklaguna.com
   - **Database**: networkla_expensesctl
   - **Username**: networkla_expnctl
   - **Password**: mipassword$123
   - **Port**: 3306

### 2. Verificar Configuración JPA

1. En las propiedades del proyecto, ve a `JPA`
2. Verifica que la plataforma sea `Hibernate 2.2`
3. El archivo `persistence.xml` debería estar en `src/main/resources/META-INF/`

## Ejecutar la Aplicación

### 1. Crear Servidor Tomcat en Eclipse

1. Ve a la vista `Servers` (si no está visible: `Window` → `Show View` → `Servers`)
2. Clic derecho en el área vacía → `New` → `Server`
3. Selecciona `Apache Tomcat v9.0`
4. Haz clic en `Next`
5. Agrega el proyecto `expense-control` a la lista `Configured`
6. Haz clic en `Finish`

### 2. Ejecutar la Aplicación

1. Clic derecho en el servidor Tomcat → `Start`
2. Una vez iniciado, abre un navegador web
3. Ve a: `http://localhost:8080/expense-control/`
4. Deberías ver la página de login de la aplicación

## Estructura del Proyecto

```
expense_control_java/
├── .project                    # Configuración del proyecto Eclipse
├── .classpath                  # Configuración del classpath
├── .settings/                  # Configuraciones específicas de Eclipse
├── pom.xml                     # Configuración Maven
├── src/
│   ├── main/
│   │   ├── java/              # Código fuente Java
│   │   │   └── com/expensecontrol/
│   │   │       ├── controller/    # Controladores JSF
│   │   │       ├── dao/          # Data Access Objects
│   │   │       ├── entity/       # Entidades JPA
│   │   │       ├── service/      # Servicios de negocio
│   │   │       └── config/       # Configuraciones
│   │   ├── resources/         # Recursos (persistence.xml, etc.)
│   │   └── webapp/           # Contenido web (XHTML, CSS, JS)
│   └── test/                 # Tests unitarios
└── target/                   # Archivos compilados (generado por Maven)
```

## Solución de Problemas Comunes

### Error: "Project facet Java version 11 is not supported"
- Asegúrate de tener Java 11 JDK instalado
- Ve a `Window` → `Preferences` → `Java` → `Installed JREs`
- Agrega Java 11 JDK si no está presente

### Error: "The superclass javax.servlet.http.HttpServlet was not found"
- Verifica que Tomcat 9.0 esté configurado correctamente
- Revisa que el proyecto tenga la faceta "Dynamic Web Module" habilitada

### Error: "Maven Dependencies not found"
- Clic derecho en el proyecto → `Maven` → `Update Project`
- Marca "Force Update of Snapshots/Releases"

### Error de conexión a base de datos
- Verifica que los parámetros de conexión en `persistence.xml` sean correctos
- Asegúrate de que el servidor de base de datos esté accesible

## URLs de la Aplicación

Una vez ejecutándose en Tomcat:

- **Login**: http://localhost:8080/expense-control/login.xhtml
- **Dashboard**: http://localhost:8080/expense-control/dashboard.xhtml
- **Gestión de Usuarios**: http://localhost:8080/expense-control/users.xhtml

## Credenciales por Defecto

- **Usuario**: admin
- **Contraseña**: admin123