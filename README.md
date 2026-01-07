# Sistema de Denuncias Contra la Violencia de Género

## Descripción

API REST desarrollada con Spring Boot siguiendo los principios de Arquitectura Hexagonal y Domain-Driven Design (DDD) para la gestión de denuncias de violencia contra las mujeres.

## Características Técnicas

- **Arquitectura:** Hexagonal + DDD
- **Autenticación:** JWT
- **Documentación:** OpenAPI 3.0 - Swagger
- **Persistencia:** JPA con PostgreSQL
- **Mapeo:** MapStruct
- **Validación:** Bean Validation

## Tecnologías

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA
- MapStruct
- Lombok
- JWT
- OpenAPI 3.0

## Instalación

1. Clonar el repositorio:
2. Configurar la base de datos en `application.yml`
    ```yml
      datasource:
        url: tu_url_de_bd
        username: tu_usuario_de_bd
        password: tu_contraseña_de_bd
    ```
3. Configurar el Secreto de JWT
     ```yml
      jwt:
        secret: tu_secreto_jwt
        expiration: expiración_en_milisegundos
    ```
4. Ejecutar:
    ```bash
    mvn spring-boot:run
    ```

5. Acceder a la documentación:
   `http://localhost:8080/swagger-ui.html`

## Endpoints

### Autenticación
| Método | Endpoint                | Descripción                                | Acceso  |
|:-------|:------------------------|:-------------------------------------------|:--------|
| `POST` | `/api/v1/auth/register` | Registrar un nuevo usuario y obtener token | Público |
| `POST` | `/api/v1/auth/login`    | Iniciar sesión y obtener token JWT         | Público |

### Usuarios
| Método | Endpoint                | Descripción                                 | Acceso        |
|:-------|:------------------------|:--------------------------------------------|:--------------|
| `POST` | `/api/v1/users/admin`   | Registrar una nueva cuenta de administrador | Admin         |
| `GET`  | `/api/v1/users/profile` | Obtener perfil del usuario autenticado      | Autenticado   |
| `PUT`  | `/api/v1/users/{id}`    | Actualizar información de perfil por ID     | Usuario/Admin |

### Denuncias
| Método  | Endpoint                           | Descripción                                 | Acceso        |
|:--------|:-----------------------------------|:--------------------------------------------|:--------------|
| `POST`  | `/api/v1/complaints`               | Crear una nueva denuncia                    | Víctima       |
| `GET`   | `/api/v1/complaints/my-complaints` | Listar denuncias del usuario actual         | Víctima       |
| `GET`   | `/api/v1/complaints`               | Listar todas las denuncias del sistema      | Admin         |
| `GET`   | `/api/v1/complaints/{id}`          | Obtener detalles de una denuncia específica | Víctima/Admin |
| `PATCH` | `/api/v1/complaints/{id}/status`   | Actualizar el estado de una denuncia        | Admin         |
| `POST`  | `/api/v1/complaints/{id}/evidence` | Subir archivos como evidencia (Multipart)   | Víctima/Admin |

### Centros de Apoyo
| Método   | Endpoint                                             | Descripción                          | Acceso  |
|:---------|:-----------------------------------------------------|:-------------------------------------|:--------|
| `POST`   | `/api/v1/support-centers/create`                     | Registrar un nuevo centro de apoyo   | Admin   |
| `PUT`    | `/api/v1/support-centers/{id}/edit`                  | Actualizar datos de un centro        | Admin   |
| `GET`    | `/api/v1/support-centers`                            | Listar todos los centros registrados | Público |
| `GET`    | `/api/v1/support-centers/recommendations/{district}` | Buscar centros por distrito          | Público |
| `DELETE` | `/api/v1/support-centers/{id}/delete`                | Eliminar un centro del sistema       | Admin   |

### Analíticas
| Método | Endpoint                                    | Descripción                      | Acceso  |
|:-------|:--------------------------------------------|:---------------------------------|:--------|
| `GET`  | `/api/v1/analytics/complaints-by-date`      | Estadísticas por rango de fechas | Admin   |
| `GET`  | `/api/v1/analytics/complaints-by-type`      | Denuncias agrupadas por tipo     | Admin   |
| `GET`  | `/api/v1/analytics/complaints-by-status`    | Denuncias agrupadas por estado   | Admin   |
| `GET`  | `/api/v1/analytics/average-resolution-time` | Tiempo promedio de resolución    | Admin   |

## Documentación de la API
La documentación interactiva está disponible en:

- Swagger UI:
  `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON:
  `http://localhost:8080/v3/api-docs`

### Contacto y Redes
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/bayala06/)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/bag0699)