# Proyecto Microservicios - Productos e Inventario

## Descripcion

Arquitectura de microservicios desarrollada con Spring Boot para administrar productos e inventario. El sistema usa dos microservicios independientes, bases de datos MySQL separadas, comunicacion REST mediante OpenFeign y un API Gateway como punto unico de entrada.

La entrega refuerza los puntos solicitados en la pauta EFT: patron CSR, JPA, DTOs, Bean Validation, `ResponseEntity`, manejo global de errores, logs con SLF4J, Swagger/OpenAPI, configuracion YAML, Docker Compose y pruebas unitarias con JUnit + Mockito.

## Integrantes

- Martin Troncoso
- Jurgen Bormuth
- Maximiliano Diaz
- Maximiliano Cifuentes

## Servicios

| Servicio | Puerto | Responsabilidad |
| --- | ---: | --- |
| API Gateway | 9999 | Entrada centralizada y rutas hacia microservicios |
| Productos | 8081 | CRUD de productos y reglas de negocio del catalogo |
| Inventario | 8082 | CRUD de inventario y validacion remota de productos |
| MySQL Productos | 3308 | Base `productos_db` |
| MySQL Inventario | 3309 | Base `inventario_db` |

## Rutas del Gateway

| Ruta | Destino |
| --- | --- |
| `http://localhost:9999/productos/**` | `http://localhost:8081/productos/**` |
| `http://localhost:9999/inventario/**` | `http://localhost:8082/inventario/**` |

## Endpoints

### Productos

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | `/productos` | Lista productos |
| GET | `/productos/{id}` | Busca producto por ID |
| POST | `/productos` | Crea producto |
| PUT | `/productos/{id}` | Actualiza producto |
| DELETE | `/productos/{id}` | Elimina producto |

Ejemplo:

```json
{
  "nombre": "Notebook Lenovo",
  "descripcion": "Notebook para oficina",
  "precio": 599990,
  "stock": 15,
  "categoria": "Computacion",
  "proveedor": "Lenovo Chile"
}
```

Regla de negocio: el precio minimo permitido es `100`.

### Inventario

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | `/inventario` | Lista inventario |
| GET | `/inventario/{id}` | Busca inventario por ID |
| GET | `/inventario/producto/{productoId}` | Busca inventario por producto |
| GET | `/inventario/stock-bajo` | Lista registros con stock actual menor o igual al minimo |
| POST | `/inventario` | Crea inventario asociado a un producto existente |
| PUT | `/inventario/{id}` | Actualiza inventario |
| DELETE | `/inventario/{id}` | Elimina solo el registro de inventario |

Ejemplo:

```json
{
  "productoId": 1,
  "stockActual": 15,
  "stockMinimo": 3,
  "ubicacion": "Bodega Central"
}
```

Reglas de negocio:

- El producto debe existir en el microservicio Productos antes de crear inventario.
- Un producto no puede tener dos registros de inventario.
- Borrar inventario no borra el producto, porque pertenecen a responsabilidades distintas.

## Swagger / OpenAPI

- Productos Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- Inventario Swagger UI: `http://localhost:8082/swagger-ui/index.html`
- Productos OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- Inventario OpenAPI JSON: `http://localhost:8082/v3/api-docs`

## Configuracion

Los microservicios usan `application.yml`:

- `ProyectoMicroservicios/Producto/src/main/resources/application.yml`
- `ProyectoMicroservicios/Inventario/src/main/resources/application.yml`
- `api-gateway/src/main/resources/application.yaml`

Variables utiles:

| Variable | Uso |
| --- | --- |
| `PORT` | Puerto del servicio |
| `SPRING_DATASOURCE_URL` | URL JDBC |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Password de base de datos |
| `PRODUCTO_SERVICE_URL` | URL usada por Inventario o Gateway para Productos |
| `INVENTARIO_SERVICE_URL` | URL usada por Gateway para Inventario |

## Ejecucion local

Crear bases de datos si se ejecuta sin Docker:

```sql
CREATE DATABASE productos_db;
CREATE DATABASE inventario_db;
```

Levantar en este orden:

```bash
cd ProyectoMicroservicios/Producto
mvnw.cmd spring-boot:run
```

```bash
cd ProyectoMicroservicios/Inventario
mvnw.cmd spring-boot:run
```

```bash
cd api-gateway
mvnw.cmd spring-boot:run
```

## Ejecucion con Docker

Desde la raiz del repositorio:

```bash
docker compose up --build
```

Detener:

```bash
docker compose down
```

## Pruebas unitarias

Ejecutar por microservicio:

```bash
cd ProyectoMicroservicios/Producto
mvnw.cmd test
```

```bash
cd ProyectoMicroservicios/Inventario
mvnw.cmd test
```

```bash
cd api-gateway
mvnw.cmd test
```

Si Windows usa Java 8 por defecto, configurar un JDK 17 o superior antes de ejecutar:

```powershell
$env:JAVA_HOME='C:\Program Files\JetBrains\IntelliJ IDEA 2026.1\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

## Flujo recomendado para Postman

1. `POST http://localhost:9999/productos`
2. `GET http://localhost:9999/productos`
3. `POST http://localhost:9999/inventario` usando el `id` del producto creado.
4. `GET http://localhost:9999/inventario/producto/{productoId}`
5. `GET http://localhost:9999/inventario/stock-bajo`
6. `PUT http://localhost:9999/productos/{id}` o `PUT http://localhost:9999/inventario/{id}`
7. `DELETE http://localhost:9999/inventario/{id}`

## Puntos para defender

- Controller: recibe requests, valida DTOs con `@Valid` y responde con `ResponseEntity`.
- Service: concentra reglas de negocio, logs y comunicacion remota.
- Repository: usa `JpaRepository` y una consulta JPQL para stock bajo.
- Model: entidades JPA con restricciones de columnas.
- Exception: `@ControllerAdvice` centraliza respuestas de error.
- Tests: JUnit + Mockito prueban reglas clave sin levantar base de datos.
