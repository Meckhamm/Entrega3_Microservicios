# Proyecto Microservicios - Productos e Inventario

## Descripcion del proyecto

Proyecto desarrollado con arquitectura de microservicios en Spring Boot. El sistema permite administrar productos y controlar el inventario asociado a esos productos mediante dos servicios independientes, comunicacion REST entre microservicios y un API Gateway encargado de centralizar las rutas de acceso.

La evaluacion se enfoca en microservicios funcionales, arquitectura por capas, reglas de negocio, validaciones, comunicacion REST, API Gateway, configuracion por archivos `properties`/`yaml`, Swagger/OpenAPI, pruebas y documentacion.

## Integrantes del equipo

- Martin Troncoso
- Jurgen Bormuth

## Aporte realizado por cada integrante

- Martin Troncoso: desarrollo y pruebas del microservicio de Productos, endpoints CRUD, modelo, repositorio, servicio y documentacion de rutas.
- Jurgen Bormuth: desarrollo y pruebas del microservicio de Inventario, comunicacion REST con Productos mediante OpenFeign, validaciones, manejo de errores y apoyo en documentacion.

> Ajustar esta seccion antes de entregar si el reparto real del equipo fue distinto.

## Arquitectura del proyecto

```text
ProyectoMicroservicios/
  Producto/      Microservicio de productos
  Inventario/    Microservicio de inventario

api-gateway/     Gateway de entrada hacia ambos microservicios
```

## Puertos del sistema

| Servicio | Puerto | URL base |
| --- | ---: | --- |
| API Gateway | 9999 | `http://localhost:9999` |
| Productos | 8081 | `http://localhost:8081` |
| Inventario | 8082 | `http://localhost:8082` |

## Rutas del API Gateway

El gateway enruta las mismas rutas REST de los microservicios:

| Ruta por gateway | Redirige a |
| --- | --- |
| `http://localhost:9999/productos/**` | `http://localhost:8081/productos/**` |
| `http://localhost:9999/inventario/**` | `http://localhost:8082/inventario/**` |

El gateway tambien permite cambiar las URL de destino mediante variables de entorno:

| Variable | Valor por defecto |
| --- | --- |
| `PORT` | `9999` |
| `PRODUCTO_SERVICE_URL` | `http://localhost:8081` |
| `INVENTARIO_SERVICE_URL` | `http://localhost:8082` |

## APIs y endpoints disponibles

### Productos

URL directa: `http://localhost:8081/productos`

URL por gateway: `http://localhost:9999/productos`

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | `/productos` | Lista todos los productos |
| GET | `/productos/{id}` | Busca un producto por ID |
| POST | `/productos` | Crea un producto |
| PUT | `/productos/{id}` | Actualiza un producto |
| DELETE | `/productos/{id}` | Elimina un producto |

Ejemplo JSON para crear producto:

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

### Inventario

URL directa: `http://localhost:8082/inventario`

URL por gateway: `http://localhost:9999/inventario`

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | `/inventario` | Lista todos los registros de inventario |
| GET | `/inventario/{id}` | Busca un registro de inventario por ID |
| POST | `/inventario` | Crea un registro de inventario asociado a un producto existente |
| PUT | `/inventario/{id}` | Actualiza un registro de inventario |
| DELETE | `/inventario/{id}` | Elimina el inventario y el producto relacionado |

Ejemplo JSON para crear inventario:

```json
{
  "productoId": 1,
  "stockActual": 15,
  "stockMinimo": 3,
  "ubicacion": "Bodega Central"
}
```

## Comunicacion REST entre microservicios

El microservicio Inventario consume el microservicio Productos con OpenFeign.

- Cliente: `ProductoClient`
- URL configurada: `http://localhost:8081/productos`
- Uso principal: validar que exista el producto antes de crear inventario y eliminar el producto relacionado cuando se elimina el inventario.

## Enlaces de Swagger / OpenAPI

Swagger/OpenAPI esta habilitado en los microservicios con `springdoc-openapi-starter-webmvc-ui`.

- Productos Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- Inventario Swagger UI: `http://localhost:8082/swagger-ui/index.html`
- Productos OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- Inventario OpenAPI JSON: `http://localhost:8082/v3/api-docs`

## Base de datos

Crear las bases de datos MySQL antes de ejecutar los microservicios:

```sql
CREATE DATABASE productos_db;
CREATE DATABASE inventario_db;
```

Configuracion actual:

| Servicio | Base de datos | Usuario | Password |
| --- | --- | --- | --- |
| Productos | `productos_db` | `root` | sin password |
| Inventario | `inventario_db` | `root` | sin password |

Los archivos de configuracion se encuentran en:

- `Producto/src/main/resources/application.properties`
- `Inventario/src/main/resources/application.properties`
- `api-gateway/src/main/resources/application.yaml`

## Instrucciones para ejecutar

Ejecutar los servicios en este orden:

1. Levantar MySQL y crear las bases de datos `productos_db` e `inventario_db`.
2. Ejecutar Productos:

```bash
cd ProyectoMicroservicios/Producto
mvnw.cmd spring-boot:run
```

3. Ejecutar Inventario:

```bash
cd ProyectoMicroservicios/Inventario
mvnw.cmd spring-boot:run
```

4. Ejecutar API Gateway:

```bash
cd api-gateway
mvnw.cmd spring-boot:run
```

## Instrucciones para ejecutar con Docker

Desde la carpeta raiz del proyecto, donde esta el archivo `docker-compose.yml`, ejecutar:

```bash
docker compose up --build
```

El compose levanta:

| Contenedor | Puerto publicado |
| --- | --- |
| `mysql-productos` | `3308:3306` |
| `mysql-inventario` | `3309:3306` |
| `producto-service` | `8081:8081` |
| `inventario-service` | `8082:8082` |
| `api-gateway` | `9999:9999` |

Para detener los contenedores:

```bash
docker compose down
```

Para detener y eliminar tambien los volumenes de MySQL:

```bash
docker compose down -v
```

## Instrucciones para probar

1. Crear un producto con `POST http://localhost:9999/productos`.
2. Listar productos con `GET http://localhost:9999/productos`.
3. Crear inventario usando el `productoId` creado con `POST http://localhost:9999/inventario`.
4. Listar inventario con `GET http://localhost:9999/inventario`.
5. Probar actualizacion con `PUT http://localhost:9999/productos/{id}` o `PUT http://localhost:9999/inventario/{id}`.
6. Probar eliminacion con `DELETE http://localhost:9999/inventario/{id}`.

## Consideraciones importantes

- No se utiliza Eureka, JWT ni Spring Security porque no son obligatorios para esta evaluacion.
- El gateway enruta por URL directa a cada microservicio.
- Para que Inventario pueda crear registros, primero debe existir el producto en el microservicio Productos.
- Si se elimina un producto directamente desde Productos, el inventario asociado se debe revisar manualmente.
- Se recomienda probar los endpoints por gateway durante la defensa para demostrar la integracion completa.
