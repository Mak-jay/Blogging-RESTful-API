# Blog API Testing Guide

This guide explains the comprehensive testing strategy implemented for the Blog API project.

## Test Structure

### 1. Service Layer Tests (Unit Tests)
Located in `src/test/java/com/blogapi/serviceTests/`

- **UserServiceTest**: Tests user registration, authentication, and role management
- **PostServiceTest**: Tests post CRUD operations, search, and filtering
- **CategoryServiceTest**: Tests category management operations
- **CommentServiceTest**: Tests comment CRUD operations and user associations
- **TagServiceTest**: Tests tag management operations

### 2. Controller Layer Tests (Unit Tests)
Located in `src/test/java/com/blogapi/controllerTests/`

- **PostControllerTest**: Tests REST API endpoints for posts with security

### 3. Integration Tests (End-to-End Tests)
Located in `src/test/java/com/blogapi/integrationTests/`

- **BlogApiIntegrationTest**: Tests complete flow from controller to database

## Running Tests

### Prerequisites
Make sure you have the following dependencies in your `pom.xml`:
- `spring-boot-starter-test`
- `spring-security-test`
- `h2` (for in-memory database)

### Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests by package
mvn test -Dtest="com.blogapi.serviceTests.*"

# Run integration tests only
mvn test -Dtest="com.blogapi.integrationTests.*"

# Run with detailed output
mvn test -Dtest=UserServiceTest -Dsurefire.useFile=false

# Run tests and generate coverage report
mvn test jacoco:report
```

## Test Configuration

### Test Profile
The tests use the `test` profile with configuration in `src/test/resources/application-test.properties`:

- H2 in-memory database
- Security disabled for integration tests
- Debug logging enabled
- JWT test configuration

### Test Annotations

#### Service Tests
```java
@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    private Repository repository;
    
    @InjectMocks
    private ServiceImpl service;
}
```

#### Controller Tests
```java
@WebMvcTest(Controller.class)
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private Service service;
}
```

#### Integration Tests
```java
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
}
```

## Test Patterns

### 1. Arrange-Act-Assert Pattern
```java
@Test
void testMethod_Success() {
    // Arrange - setup test data and mocks
    when(repository.findById(1L)).thenReturn(Optional.of(testEntity));
    
    // Act - call the method under test
    Response response = service.getById(1L);
    
    // Assert - verify the expected behavior
    assertNotNull(response);
    assertEquals(expectedValue, response.getValue());
}
```

### 2. Testing Success and Failure Scenarios
```java
@Test
void getById_Success() {
    // Test successful retrieval
}

@Test
void getById_NotFound() {
    // Test not found scenario
}
```

### 3. Mocking Dependencies
```java
@Mock
private UserRepository userRepository;

@Mock
private PasswordEncoder passwordEncoder;

@InjectMocks
private UserServiceImpl userService;
```

## Test Coverage Areas

### Service Layer Coverage
- ✅ CRUD operations (Create, Read, Update, Delete)
- ✅ Business logic validation
- ✅ Error handling and exceptions
- ✅ Data transformation and mapping
- ✅ Search and filtering functionality

### Controller Layer Coverage
- ✅ HTTP method handling (GET, POST, PUT, DELETE)
- ✅ Request/response mapping
- ✅ Status code validation
- ✅ Security annotations
- ✅ Input validation

### Integration Layer Coverage
- ✅ End-to-end API calls
- ✅ Database interactions
- ✅ Complete request/response cycles
- ✅ Cross-component integration

## Best Practices

### 1. Test Naming
- Use descriptive test method names
- Follow the pattern: `methodName_scenario_expectedResult`
- Example: `createUser_ValidData_Success()`

### 2. Test Organization
- Group related tests together
- Use `@BeforeEach` for common setup
- Keep tests independent and isolated

### 3. Mocking Strategy
- Mock external dependencies (repositories, external services)
- Don't mock the class under test
- Use `@InjectMocks` for dependency injection

### 4. Assertions
- Use specific assertions (assertEquals, assertNotNull, etc.)
- Verify mock interactions when relevant
- Test both positive and negative scenarios

### 5. Test Data
- Create realistic test data
- Use builders or factory methods for complex objects
- Keep test data minimal but complete

## Common Test Scenarios

### 1. CRUD Operations
```java
@Test
void create_Success()
@Test
void getById_Success()
@Test
void getById_NotFound()
@Test
void update_Success()
@Test
void delete_Success()
```

### 2. Search and Filter
```java
@Test
void searchByQuery_Success()
@Test
void filterByCategory_Success()
@Test
void getByAuthor_Success()
```

### 3. Security and Authorization
```java
@Test
@WithMockUser(roles = "ADMIN")
void adminOnlyEndpoint_Success()

@Test
@WithMockUser(roles = "USER")
void userEndpoint_Success()
```

## Troubleshooting

### Common Issues

1. **Test Database Issues**
   - Ensure H2 dependency is included
   - Check application-test.properties configuration

2. **Security Test Issues**
   - Add `@WithMockUser` annotations
   - Include spring-security-test dependency

3. **Mock Issues**
   - Verify `@Mock` and `@InjectMocks` annotations
   - Check mock setup in `@BeforeEach`

4. **Integration Test Issues**
   - Ensure `@Transactional` annotation
   - Check database configuration

### Debugging Tests
```bash
# Run with debug output
mvn test -X

# Run specific test with verbose output
mvn test -Dtest=UserServiceTest -Dsurefire.useFile=false

# Run tests and show console output
mvn test -Dspring.profiles.active=test
```

## Next Steps

1. **Add More Controller Tests**
   - AuthController tests
   - CategoryController tests
   - CommentController tests
   - TagController tests

2. **Add Repository Tests**
   - Test custom query methods
   - Test database constraints

3. **Add Performance Tests**
   - Load testing for endpoints
   - Database performance tests

4. **Add Security Tests**
   - Authentication tests
   - Authorization tests
   - JWT token validation

5. **Add API Documentation Tests**
   - Test OpenAPI/Swagger documentation
   - Validate API contracts

## Test Metrics

Track your test coverage and quality:
- Line coverage: Aim for >80%
- Branch coverage: Aim for >70%
- Test execution time: Keep under 30 seconds
- Test reliability: 100% pass rate

This comprehensive testing strategy ensures your Blog API is robust, reliable, and maintainable.
