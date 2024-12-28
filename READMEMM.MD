# Mareo Envíos - Aplicación de Gestión de Envíos

Este proyecto es una aplicación web para la gestión de envíos de mercadería, diseñada como parte de un ejercicio técnico.
La aplicación utiliza Spring Boot para el backend y Docker para su despliegue y gestión.

## Descripción General

La aplicación "Mareo Envíos" permite gestionar el ciclo de vida de los envíos de mercadería, desde su creación hasta su entrega o cancelación. La aplicación ofrece:

*   Un API REST para procesar envíos de forma concurrente.
*   Un mecanismo de bloqueo para asegurar que los envíos no se procesen de forma simultánea.
*   Registro del estado de cada envío en una tabla.
*   Un endpoint para obtener el estado actual de todos los envíos.
*   Un endpoint para obtener los 3 productos mas solicitados.
*   Manejo de errores.
*   Documentación de la api con Swagger UI.

## Tecnologías Utilizadas

*   **Backend:** Java 17, Spring Boot, Spring Data JPA
*   **Base de datos:** PostgreSQL (ejecutándose en Docker)
*   **Contenedorización:** Docker, Docker Compose
*   **Documentación:** Swagger UI

## Arquitectura

La aplicación se despliega utilizando Docker y Docker Compose. La arquitectura consta de:

*   **Dos instancias de la aplicación:** `mareo-envios` y `task-processor`  ejecutándose en los puertos 8081 y 8082 respectivamente.
*   **Un servicio de base de datos PostgreSQL:** Ejecutándose en el puerto `5432` en el contenedor y accesible desde el puerto `5432` de tu host.

## Comandos Clave

### Gestión de Contenedores Docker

*   **Detener todos los contenedores:**

    docker compose down -v

*   **Construir las imágenes:**


    docker compose build


*   **Levantar los contenedores:**

    docker compose up

*   **Construir y levantar los contenedores (en un solo paso):**

     docker compose up --build


### Acceso a la Aplicación

*   **URLs de Swagger UI:**
    *   `http://localhost:8081/swagger-ui/index.html` (`mareo-envios`)
    *   `http://localhost:8082/swagger-ui/index.html` (`task-processor`)

## Notas Adicionales

*   Es necesario tener Docker y Docker Compose instalados.
*   Los archivos de configuración (`docker-compose.yml`, `application.properties`) se encuentran en la raíz del proyecto.