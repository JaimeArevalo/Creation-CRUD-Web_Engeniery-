# Sistema de Gestión de Tarjetas y Transacciones

Este es un sistema web para gestionar tarjetas de crédito y sus transacciones. El proyecto está construido con React en el frontend y Java Spring Boot en el backend.

## Características

- Gestión de tarjetas de crédito (CRUD)
- Gestión de transacciones (CRUD)
- Interfaz de usuario moderna y responsiva
- Validación de formularios
- Paginación de resultados
- Notificaciones de operaciones
- Tema claro/oscuro automático

## Tecnologías Utilizadas

### Frontend
- React 18
- TypeScript
- Material-UI
- React Router
- React Query
- React Hook Form
- Axios
- Vite

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

## Requisitos Previos

- Node.js 18 o superior
- Java 17 o superior
- MySQL 8 o superior
- Maven 3.8 o superior

## Instalación

1. Clonar el repositorio:
```bash
git clone <url-del-repositorio>
cd <nombre-del-directorio>
```

2. Configurar el backend:
```bash
cd backend
mvn clean install
```

3. Configurar el frontend:
```bash
cd frontend
npm install
```

4. Configurar la base de datos:
- Crear una base de datos MySQL llamada `card_system`
- Actualizar las credenciales en `backend/src/main/resources/application.properties`

## Ejecución

1. Iniciar el backend:
```bash
cd backend
mvn spring-boot:run
```

2. Iniciar el frontend:
```bash
cd frontend
npm run dev
```

3. Acceder a la aplicación:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api

## Estructura del Proyecto

```
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── types/
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── package.json
│   └── vite.config.ts
└── README.md
```

## Contribución

1. Fork el repositorio
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

