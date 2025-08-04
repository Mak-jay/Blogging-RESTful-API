# Blogging-RESTful-API

A comprehensive RESTful API for a blogging platform built with Spring Boot 3.5.0, featuring user authentication, role-based access control, advanced search functionality, comment system, and production-ready security features.

## Features

- **User Management**: Three-tier role system (USER, AUTHOR, ADMIN)
- **JWT Authentication**: Secure token-based authentication with configurable expiration
- **Blog Post Management**: Create, read, update, delete posts with categories and tags
- **Comment System**: Full CRUD operations for comments on posts
- **Advanced Search**: Multiple search options for finding posts
- **Security Features**: CORS support, rate limiting, input validation
- **Health Monitoring**: Spring Boot Actuator for health checks
- **PostgreSQL Database**: Robust data persistence
- **Production Ready**: Environment-based configuration, proper error handling

## Security Features

### Environment-Based Configuration
All sensitive configuration is externalized to environment variables:
- Database credentials
- JWT secret key
- Admin credentials
- CORS settings
- Rate limiting configuration

### Security Implementations
- **JWT Authentication**: Configurable expiration and secure secret management
- **CORS Support**: Configurable cross-origin resource sharing
- **Rate Limiting**: Configurable request rate limiting per IP
- **Input Validation**: Comprehensive validation with proper error responses
- **Global Exception Handling**: Consistent error responses across the application
- **Health Checks**: Built-in health monitoring endpoints

## Search Endpoints

The API provides comprehensive search functionality through the following endpoints:

### Basic Search
- `GET /blog/posts/search?query=keyword` - Search posts by title or content

### Category Search
- `GET /blog/posts/search/category?category=categoryName` - Search posts by category

### Tag Search
- `GET /blog/posts/search/tag?tag=tagName` - Search posts by tag

### Author Search
- `GET /blog/posts/search/author?author=authorName` - Search posts by author

### Combined Search
- `GET /blog/posts/search/combined?query=keyword&category=categoryName&tag=tagName&author=authorName` - Search with multiple criteria

All search endpoints support partial matching and are case-insensitive.

## Comment Endpoints

The API provides comprehensive comment functionality:

### Create Comment
- `POST /blog/comments/posts/{postId}` - Create a new comment on a post

### Get Comments
- `GET /blog/comments/posts/{postId}` - Get all comments for a specific post
- `GET /blog/comments/{commentId}` - Get a specific comment by ID
- `GET /blog/comments/users/{userId}` - Get all comments by a specific user

### Update Comment
- `PUT /blog/comments/{commentId}` - Update a comment (only comment author or admin)

### Delete Comment
- `DELETE /blog/comments/{commentId}` - Delete a comment (comment author, post author, or admin)

### Comment Authorization Rules
- **Create**: All authenticated users (USER, AUTHOR, ADMIN)
- **Update**: Comment author or ADMIN only
- **Delete**: Comment author, post author, or ADMIN only
- **Read**: All authenticated users

## Health Monitoring

The application includes Spring Boot Actuator for monitoring:
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

## Authentication

All endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Environment Configuration

Copy `env.example` to `.env` and configure the following environment variables:

### Required Variables
- `DATABASE_URL` - PostgreSQL connection URL
- `DATABASE_USERNAME` - Database username
- `DATABASE_PASSWORD` - Database password
- `JWT_SECRET` - Secret key for JWT signing (at least 256 bits)

### Optional Variables
- `JWT_EXPIRATION` - JWT token expiration time in milliseconds (default: 3600000)
- `CORS_ALLOWED_ORIGINS` - Comma-separated list of allowed origins
- `RATE_LIMIT_ENABLED` - Enable/disable rate limiting (default: true)
- `RATE_LIMIT_REQUESTS_PER_MINUTE` - Rate limit per minute (default: 100)

## Getting Started

### Development
1. Ensure PostgreSQL is running on localhost:5432
2. Create a database named `blogAPI`
3. Copy `env.example` to `.env` and configure your environment variables
4. Run the application: `mvn spring-boot:run`

### Production Deployment
1. Set all required environment variables
2. Use a strong JWT secret (at least 256 bits)
3. Configure proper CORS origins for your domain
4. Set up a reverse proxy (nginx) for additional security
5. Use HTTPS in production
6. Monitor application health via `/actuator/health`

## API Documentation

For detailed API documentation, refer to the controller classes in the `src/main/java/com/blogapi/controller` package.

## Security Best Practices

1. **Never commit sensitive data** - Use environment variables for all secrets
2. **Use strong JWT secrets** - Generate a cryptographically secure random string
3. **Configure CORS properly** - Only allow necessary origins
4. **Monitor rate limits** - Adjust based on your application needs
5. **Use HTTPS in production** - Always encrypt traffic
6. **Regular security updates** - Keep dependencies updated
7. **Monitor logs** - Watch for suspicious activity
