# Solución al Desafío de Directores de Cine

[English Version of this README](README-EN.md)

Este proyecto presenta una solución robusta y bien arquitectada para el [Desafío de Desarrollador Back-End de Domus](challenge.md). Es un ejercicio 100% académico que demuestra prácticas modernas de desarrollo en Java y Spring Boot.

La tarea principal es construir un endpoint de API REST que, dado un umbral numérico, devuelva una lista ordenada de directores que hayan dirigido más películas que el umbral especificado. Los datos de las películas se obtienen de una API externa, lo que requiere un manejo inteligente de la paginación y una comunicación resiliente.

## Descripción del Desafío
Para una comprensión detallada del enunciado original del problema, por favor, consulta el archivo [challenge.md](challenge.md).

## Características Clave

*   **Programación Reactiva**: Construido con Spring WebFlux para operaciones no bloqueantes y asíncronas, asegurando alta concurrencia y un uso eficiente de los recursos.
*   **Paginación Inteligente**: La API externa de películas se consume de manera eficiente utilizando el operador `expand` de Reactor para obtener todas las páginas concurrentemente, optimizando la recuperación de datos.
*   **Cliente de API Externo Robusto**: Implementado con `WebClient` y configurado con tiempos de espera de conexión y respuesta para prevenir el agotamiento de recursos y mejorar la resiliencia frente a servicios externos lentos o que no responden.
*   **Arquitectura por Capas**: Clara separación de responsabilidades entre las capas de Controlador, Servicio, Cliente, Mapper, Dominio y DTO, promoviendo la mantenibilidad y escalabilidad.
*   **Principio de Inversión de Dependencias (DIP)**: El `DirectorService` depende de la interfaz `MovieApiClient`, lo que permite intercambiar fácilmente las implementaciones del cliente de API sin afectar la lógica de negocio.
*   **Configuración Fuertemente Tipada**: Las URLs base de la API externa se gestionan utilizando `@ConfigurationProperties`, proporcionando una configuración organizada y segura en cuanto a tipos.
*   **Mapeo Eficiente de Objetos**: Utiliza MapStruct para la generación en tiempo de compilación de mappers entre DTOs y objetos de dominio, eliminando código repetitivo y mejorando el rendimiento.
*   **Comportamiento Orientado al Dominio**: Encapsula la lógica de negocio dentro del modelo de dominio (por ejemplo, `Movie::hasDirector`), adhiriéndose al principio "Tell, Don't Ask".
*   **Manejo Integral de Errores**: Un manejador de excepciones global (`GlobalExceptionHandler`) proporciona respuestas de error consistentes y amigables para el usuario en caso de entradas inválidas.
*   **Colecciones Inmutables**: Aprovecha `Stream.toList()` de Java 16 para crear listas inmutables, mejorando la seguridad y predictibilidad del código.
*   **Estrategia de Pruebas Exhaustiva**: Incluye pruebas unitarias para los componentes principales (Cliente, Servicio, Mapper, Dominio, Manejador de Excepciones) y pruebas de integración para el endpoint de la API, asegurando una alta calidad y fiabilidad del código.
*   **Documentación OpenAPI (Swagger)**: Documentación de API generada automáticamente para una fácil comprensión y consumo del endpoint.

## Tecnologías Utilizadas

*   **Java 17**: La versión principal del lenguaje.
*   **Spring Boot 3.4.12**: Framework para construir la aplicación.
*   **Spring WebFlux**: Para el modelo de programación reactiva.
*   **Project Reactor**: Base para los flujos reactivos.
*   **Lombok**: Reduce el código repetitivo (getters, setters, constructores).
*   **MapStruct 1.5.5.Final**: Generador de código para el mapeo de objetos.
*   **SpringDoc OpenAPI 2.2.0**: Integra Swagger UI para la documentación de la API.
*   **Gradle**: Herramienta de automatización de construcción.
*   **JUnit 5 y Mockito**: Para pruebas.
*   **Reactor Test**: Para probar flujos reactivos.

## Diseño Arquitectónico y Mejoras (Aspectos de Seniority)

Esta solución va más allá de simplemente cumplir con los requisitos del desafío al incorporar varias mejores prácticas y patrones de diseño indicativos de un enfoque de desarrollo senior:

*   **Clara Separación de Responsabilidades**: Cada componente (Controlador, Servicio, Cliente, Mapper, Dominio, DTO) tiene una única responsabilidad bien definida.
*   **Robustez y Resiliencia**: La implementación de tiempos de espera y la propagación adecuada de errores en el `WebClient` aseguran que la aplicación pueda manejar con gracia las fallas de servicios externos.
*   **Testeabilidad**: La arquitectura está diseñada para una máxima testeabilidad, permitiendo pruebas unitarias aisladas de la lógica de negocio, mappers y comportamiento del cliente, junto con pruebas de integración para la capa de la API.
*   **Mantenibilidad y Legibilidad**: El uso de MapStruct, Lombok y Javadoc, combinado con una estructura de código limpia, hace que la base de código sea fácil de entender, mantener y extender.
*   **Escalabilidad**: La naturaleza reactiva de WebFlux y el manejo eficiente de la paginación son fundamentales para construir microservicios escalables.
*   **Gestión de la Configuración**: La externalización de la configuración con `@ConfigurationProperties` permite una implementación flexible en diferentes entornos sin cambios en el código.

## Diagramas UML

Para una mejor comprensión de la arquitectura y el flujo de la aplicación, se incluyen los siguientes diagramas UML:

### Diagrama de Componentes
![Diagrama de Componentes](docs/images/Movie_Director_Challenge___Component_Diagram__Clean.png)

### Diagrama de Secuencia
![Diagrama de Secuencia](docs/images/Movie_Director_Challenge___Sequence_Diagram.png)

## Cómo Ejecutar

1.  **Prerrequisitos**: Asegúrate de tener Java 17 instalado.
2.  **Clona el repositorio**:
    ```bash
    git clone https://github.com/your-username/MovieDirectorChallenge.git
    cd MovieDirectorChallenge
    ```
3.  **Ejecuta la aplicación usando Gradle**:
    ```bash
    ./gradlew bootRun
    ```
    La aplicación se iniciará en `http://localhost:8080`.

## Uso de la API

La aplicación expone un único endpoint REST:

### Obtener Directores por Umbral de Conteo de Películas

Recupera una lista de directores que han dirigido más películas que el umbral especificado.

*   **URL**: `/api/directors`
*   **Método**: `GET`
*   **Parámetro de Consulta**:
    *   `threshold` (obligatorio): Un número entero que representa el número mínimo de películas que un director debe tener (estrictamente mayor que este valor).
        *   **Example**: `?threshold=3`
        *   **Validation**:
            *   Los valores no numéricos resultarán en un `400 Bad Request`.
            *   Los valores negativos devolverán una lista vacía de directores.

*   **Ejemplo de Solicitud**:
    ```
    GET http://localhost:8080/api/directors?threshold=3
    ```

*   **Ejemplo de Respuesta Exitosa (200 OK)**:
    ```json
    {
        "directors": [
            "Clint Eastwood",
            "M. Night Shyamalan",
            "Martin Scorsese",
            "Pedro Almodóvar",
            "Quentin Tarantino",
            "Woody Allen"
        ]
    }
    ```

*   **Ejemplo de Respuesta de Error (400 Bad Request)**:
    ```json
    {
        "error": "Threshold must be a number"
    }
    ```

## Pruebas

Para ejecutar todas las pruebas (unitarias y de integración):

```bash
./gradlew test
```

## Documentación de Swagger

Accede a la documentación interactiva de la API (Swagger UI) en:

```
http://localhost:8080/webjars/swagger-ui/index.html
```

## Contribución

Si deseas contribuir a este proyecto, sigue estos pasos:
1.  Haz un fork del repositorio.
2.  Crea una nueva rama para tu contribución.
3.  Realiza tus cambios y mejoras.
4.  Envía una solicitud de extracción a la rama principal.

## Licencia

Este proyecto es puramente académico y desarrollado como una solución al Desafío de Desarrollador Back-End de Domus.

## Contacto

Para cualquier pregunta o sugerencia, no dudes en ponerte en contacto con Pablo.
