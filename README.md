# TechFix Solutions - Sistema de Gestión de Órdenes de Servicio Técnico
**Curso:** Pruebas de Software (8vo Ciclo - UTP)  
**Entrega:** Avance de Proyecto Final 2 (APF2)  
**Gestor de Proyecto:** Apache Maven (`pom.xml`)  
**Repositorio Oficial:** [https://github.com/Edisonkz/techFix-Pruebas-de-Software](https://github.com/Edisonkz/techFix-Pruebas-de-Software)

---

## 1. Requisitos Previos

* **Java JDK 21 LTS** (Verificar en terminal con `java -version`).
* **MySQL Server 8.0** o superior (con MySQL Workbench).
* **IDE:** Apache NetBeans 19+, Visual Studio Code o IntelliJ IDEA.

---

## 2. Configuración de Base de Datos MySQL

### Paso 1: Crear la Base de Datos en MySQL Workbench
1. Abrir **MySQL Workbench** y conectarse a la instancia local (`localhost:3306`).
2. Abrir una pestaña de consulta SQL y ejecutar:
   ```sql
   CREATE DATABASE IF NOT EXISTS db_techfix;
   ```
3. *(Automático)* No necesitas crear tablas ni cargar scripts manualmente. Spring Data JPA e Hibernate crearán las tablas relacionales e insertarán los **33 registros de prueba** al iniciar la aplicación.

### Paso 2: Credenciales de Base de Datos
El proyecto está configurado con las siguientes credenciales en `src/main/resources/application.yaml`:
* **Host / Puerto:** `localhost:3306`
* **Base de Datos:** `db_techfix`
* **Usuario:** `root`
* **Contraseña:** Configurada localmente (o definir variable de entorno `DB_PASSWORD`)

---

## 3. Ejecución en Apache NetBeans (Maven Nativo)

El proyecto está configurado con **Apache Maven (`pom.xml`)**:

1. **Abrir el proyecto:**
   * En NetBeans: `File` $\rightarrow$ `Open Project` (o `Ctrl + Shift + O`).
   * Seleccionar la carpeta: `C:\Users\Edison\Downloads\techFix`.
   * NetBeans lo reconocerá con el ícono naranja de **Maven (`m`)**.
2. **Iniciar la Aplicación:**
   * Abrir `src/main/java/.../TechfixApplication.java`.
   * Clic derecho $\rightarrow$ **Run File** (o presionar `Shift + F6` / Botón verde de **Play** / `F6`).
3. **Ejecutar Pruebas Unitarias e Integración:**
   * Abrir cualquier archivo de prueba (ej: `ClienteServiceTest.java` o `OrdenRestControllerIntegrationTest.java`).
   * Clic derecho $\rightarrow$ **Test File** (o presionar `Ctrl + F6`).
   * Para correr toda la suite: Clic derecho sobre el proyecto `techfix` $\rightarrow$ **Test** (o `Alt + F6`).

---

## 4. Acceso al Frontend y Credenciales de Prueba

Una vez iniciada la aplicación en el puerto **`8080`**:

* **URL de Login:** [http://localhost:8080/login](http://localhost:8080/login)
* **Página de Inicio:** [http://localhost:8080/](http://localhost:8080/)

### Credenciales de Acceso:
| Perfil / Rol | Usuario | Contraseña | Vistas y Permisos |
| :--- | :--- | :--- | :--- |
| **Administrador** | **`admin`** | **`admin123`** | Acceso total: Dashboard ([`:8080/dashboard`](http://localhost:8080/dashboard)), Clientes, Equipos y Órdenes. |
| **Técnico Especialista** | **`tecnico`** | **`tecnico123`** | Diagnóstico técnico, atención de órdenes asignadas y cambios de estado. |
| **Supervisor QA** | **`supervisor`** | **`admin123`** | Acceso combinado (Administrador y Técnico). |

### Módulos con Datos Cargados:
* **Dashboard:** [http://localhost:8080/dashboard](http://localhost:8080/dashboard) (Métricas de órdenes pendientes, en proceso y listas).
* **Clientes:** [http://localhost:8080/clientes](http://localhost:8080/clientes) (8 clientes con DNI y datos de contacto).
* **Equipos:** [http://localhost:8080/equipos](http://localhost:8080/equipos) (10 equipos laptop/PC vinculados a clientes).
* **Órdenes:** [http://localhost:8080/ordenes](http://localhost:8080/ordenes) (10 órdenes con estados PENDIENTE, EN_PROCESO, LISTO y CANCELADO).

---

## 5. Datos de Prueba Precargados (SecurityDataLoader)

Al arrancar la aplicación por primera vez, `SecurityDataLoader.java` inicializa automáticamente:
* **2 Roles:** `ROLE_ADMIN`, `ROLE_TECNICO`
* **3 Usuarios:** `admin`, `tecnico`, `supervisor`
* **8 Clientes (RF-07):** Carlos Torres (`71234567`), Jorge Ramírez (`72345678`), Ana Gómez (`73456789`), Luis Huamán (`74567890`), María Quispe (`75678901`), Pedro Fernández (`76789012`), Rosa Díaz (`77890123`), Miguel Romero (`78901234`).
* **10 Equipos (RF-08):** Dell Inspiron 15 (`SN-DL-99`), HP Pavilion 14 (`SN-HP-14`), Lenovo ThinkPad E14 (`SN-LN-14`), ASUS ROG Strix (`SN-AS-01`), Acer Swift 3 (`SN-AC-03`), HP ProDesk 400 (`SN-HP-40`), Apple MacBook Air M1 (`SN-AP-M1`), Dell Latitude 5420 (`SN-DL-54`), Lenovo IdeaCentre 3 (`SN-LN-IC`), Toshiba Tecra A50 (`SN-TB-01`).
* **10 Órdenes de Servicio (RF-03 / RF-05):** Distribuidas en estados `PENDIENTE`, `EN_PROCESO`, `LISTO`, `ENTREGADO` y `CANCELADO`.

---

## 6. Mapeo de Evidencias para el Documento APF2

| Integrante Responsable | Sección Word | Evidencia Requerida | Instrucción de Captura |
| :--- | :--- | :--- | :--- |
| **Edison Sivipaucar** | **8.2 (Figura 3)** | Pruebas Unitarias en el IDE | Ejecutar tests en `service/` (`ClienteServiceTest`, etc.) con barra verde de JUnit. |
| **Edison Sivipaucar** | **8.4 (Figura 4)** | Reporte de Cobertura JaCoCo | Abrir en el navegador el reporte HTML de cobertura (`>80%`). |
| **Edison Sivipaucar** | **9.1 (Figura 5)** | Base de Datos en MySQL Workbench | Captura de MySQL Workbench mostrando las 5 tablas con datos en `db_techfix`. |
| **Edison Sivipaucar** | **9.2 (Caja 9.2)**| MockMvc Integración Web | Ejecutar `OrdenRestControllerIntegrationTest.java` en NetBeans (3 tests en verde). |
| **Edison Sivipaucar** | **9.3 (Caja 9.3)**| Transaccionalidad `@Transactional`| Ejecutar `RepositoryIntegrationTest.java` en NetBeans (14 tests en verde). |
| **Cristian Gavino** | **10.2 (Figura 6)**| Pipeline CI/CD GitHub Actions | Subir con `git push origin main` y capturar flujo en verde en `github.com/.../actions`. |
| **Cristian Gavino** | **11.2 (Figura 7)**| Pruebas de API en Postman | Con la app corriendo en `:8080`, ejecutar Postman Runner con la colección. |
| **Kenyi Tumbalobos** | **12.2 (Figura 8)**| Rendimiento en Apache JMeter | Correr prueba de carga de 50 usuarios en JMeter contra `localhost:8080`. |
| **Kenyi Tumbalobos** | **13.2 (Figura 9)**| Seguridad con OWASP ZAP | Escaneo automatizado en ZAP contra `localhost:8080` mostrando 0 alertas críticas. |
