# Mareo Envíos - Aplicación de Gestión de Envíos

Este proyecto es una aplicación web para la gestión de envíos de mercadería, diseñada como parte de un ejercicio técnico.
La aplicación utiliza Spring Boot para el backend y Docker para su despliegue y gestión.

## Descripción General

La aplicación "Mareo Envíos" permite gestionar el ciclo de vida de los envíos de mercadería, desde su creación hasta su entrega o cancelación. La aplicación ofrece:

*   Un API REST para procesar envíos de forma concurrente.
*   Un mecanismo de bloqueo para asegurar que los envíos no se procesen de forma simultánea.
*   Registro del estado de cada envío en una tabla.
*   Un endpoint para obtener el estado actual de todos los envíos.
*  Un endpoint para obtener los 3 productos mas solicitados.
*  Manejo de errores.
*  Documentación de la api con Swagger UI.

## Tecnologías Utilizadas

*   **Backend:** Java 17, Spring Boot, Spring Data JPA
*   **Base de datos:** PostgreSQL (ejecutándose en Docker)
*   **Contenedorización:** Docker, Docker Compose
*   **Balanceador de Carga:** Nginx
*   **Documentación:** Swagger UI

## Arquitectura

La aplicación se despliega utilizando Docker y Docker Compose. La arquitectura consta de:

  **Tres instancias de la aplicación:** Ejecutándose en distintos puertos (8081, 8082 y 8083), pero atendiendo en el puerto interno `8080`.
  **Un servicio de base de datos PostgreSQL:** Ejecutándose en el puerto `5433` en tu host y el `5432` en el contenedor.
  **Un balanceador de carga Nginx:** Distribuye las peticiones entre las tres instancias de la aplicación.

## Comandos Clave

### Gestión de Contenedores Docker

*   **Detener todos los contenedores:**

    docker-compose down

*   **Eliminar el volumen de la base de datos (para iniciar con una base de datos desde cero):**

    docker volume rm mareo-envios-master_postgres_data

*   **Eliminar las imágenes (opcional):**

    docker image rm mareo-envios-master_app1 mareo-envios-master_app2 mareo-envios-master_app3 

*   **Construir las imágenes:**

    docker-compose build --no-cache


*   **Levantar los contenedores:**

    docker-compose up

*   **Construir y levantar los contenedores (en un solo paso):**

    docker-compose up --build


### Acceso a la Aplicación

*   **URL de la aplicación:**
	http://localhost  (o cualquier otro puerto si lo has configurado en el balanceador de carga)
   
*   **URL de Swagger UI:**
    http://localhost/swagger-ui/index.html
    
* **Ver los logs:**

   docker-compose logs -f

## Notas Adicionales

*   Es necesario Docker y Docker Compose instalados.
*   Los archivos de configuración (`docker-compose.yml`, `nginx.conf`, `application.properties`) se encuentran en la raíz del proyecto.

## Estructura del Proyecto
