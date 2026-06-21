# Entrega EP3 - Microservicios Productos, Inventario y API Gateway

## Descripción del Proyecto

Este proyecto corresponde al desarrollo de una arquitectura basada en microservicios utilizando Spring Boot. La aplicación permite gestionar productos e inventario mediante servicios independientes conectados a bases de datos relacionales y comunicación REST entre microservicios.

La solución incorpora un API Gateway como punto único de entrada para centralizar el acceso a los servicios, además de documentación mediante Swagger/OpenAPI y despliegue mediante Docker Compose.

---

## Integrantes

* Martín Troncoso
* Jürgen Bormuth
* Maximiliano Díaz
* Maximiliano Cifuentes

---

## Arquitectura del Sistema

El sistema está compuesto por:

* Microservicio Productos
* Microservicio Inventario
* API Gateway
* Base de datos MySQL para Productos
* Base de datos MySQL para Inventario

### Flujo de comunicación

Cliente → API Gateway → Microservicios → Bases de Datos

El API Gateway centraliza todas las solicitudes y las redirige al microservicio correspondiente.

---

## Microservicios Desarrollados

### Productos

Microservicio encargado de administrar la información de productos disponibles en el sistema.

#### Funciones

* Crear productos
* Listar productos
* Buscar productos por ID
* Actualizar productos
* Eliminar productos

#### Endpoint Base

Interno:
http://localhost:8081/productos

Vía Gateway:
http://localhost:9999/productos

#### Estructura de ejemplo

```json
{
  "nombre": "",
  "descripcion": "",
  "precio": 0.0,
  "stock": 15,
  "categoria": "",
  "proveedor": ""
}
```

---

### Inventario

Microservicio encargado de gestionar el stock y disponibilidad de productos.

#### Funciones

* Registrar inventario
* Consultar stock
* Actualizar cantidades
* Eliminar registros de inventario
* Comunicación con microservicio Productos

#### Endpoint Base

Interno:
http://localhost:8082/inventario

Vía Gateway:
http://localhost:9999/inventario

#### Estructura de ejemplo

```json
{
  "id": 1,
  "productoId": 1,
  "stockActual": 10,
  "stockMinimo": 5,
  "ubicacion": "Bodega A"
}
```

---

## Comunicación entre Microservicios

El microservicio Inventario consume información remota del microservicio Productos mediante endpoints REST.

Esta comunicación permite validar la existencia de productos y mantener sincronizada la información relacionada con el inventario.

---

## API Gateway

El API Gateway funciona como punto único de acceso al sistema.

### Puerto

```text
http://localhost:9999
```

### Rutas principales

Productos:

```text
http://localhost:9999/productos
```

Inventario:

```text
http://localhost:9999/inventario
```

Todas las pruebas funcionales pueden realizarse a través del Gateway sin necesidad de acceder directamente a los microservicios.

---

## Endpoints Implementados

### Productos

| Método | Endpoint        |
| ------ | --------------- |
| GET    | /productos      |
| GET    | /productos/{id} |
| POST   | /productos      |
| PUT    | /productos/{id} |
| DELETE | /productos/{id} |

### Inventario

| Método | Endpoint         |
| ------ | ---------------- |
| GET    | /inventario      |
| GET    | /inventario/{id} |
| POST   | /inventario      |
| PUT    | /inventario/{id} |
| DELETE | /inventario/{id} |

---

## Tecnologías Utilizadas

* Java
* Spring Boot
* Spring Cloud Gateway
* Maven
* MySQL
* JPA / Hibernate
* Swagger / OpenAPI
* Docker
* Docker Compose
* Postman
* GitHub

---

## Ejecución del Proyecto

### Bases de Datos

Crear las siguientes bases de datos:

```sql
CREATE DATABASE productos_db;
CREATE DATABASE inventario_db;
```

### Ejecución Manual

1. Levantar el microservicio Productos (Puerto 8081).
2. Levantar el microservicio Inventario (Puerto 8082).
3. Levantar el API Gateway (Puerto 9999).

### Ejecución con Docker

Desde la carpeta raíz del proyecto:

```bash
docker compose up --build
```

Servicios publicados:

| Servicio         | Puerto |
| ---------------- | ------ |
| API Gateway      | 9999   |
| Productos        | 8081   |
| Inventario       | 8082   |
| MySQL Productos  | 3308   |
| MySQL Inventario | 3309   |

Para detener todos los contenedores:

```bash
docker compose down
```

---

## Swagger / OpenAPI

### Swagger UI

Productos:

```text
http://localhost:8081/swagger-ui/index.html
```

Inventario:

```text
http://localhost:8082/swagger-ui/index.html
```

### OpenAPI JSON

Productos:

```text
http://localhost:8081/v3/api-docs
```

Inventario:

```text
http://localhost:8082/v3/api-docs
```

---

## Consideraciones Importantes

### Eliminación de Registros

Se recomienda eliminar registros desde el microservicio Inventario.

Al eliminar desde Inventario, el sistema gestiona correctamente la eliminación relacionada en Productos.

Si se elimina primero desde Productos, será necesario realizar la eliminación correspondiente en Inventario de forma manual.

### AUTO_INCREMENT

Después de eliminar registros, si se desea reutilizar el identificador eliminado para mantener una secuencia ordenada, se puede ejecutar:

```sql
ALTER TABLE productos AUTO_INCREMENT = ID_ELIMINADA;
ALTER TABLE inventario AUTO_INCREMENT = ID_ELIMINADA;
```

Este procedimiento es opcional y se utiliza únicamente con fines de organización visual de los registros.
