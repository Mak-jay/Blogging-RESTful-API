package com.blogapi;

/**
 * Test Suite for Blog API
 * 
 * This class serves as documentation for all test classes in the project.
 * 
 * Test Classes:
 * 
 * Service Layer Tests (Unit Tests):
 * - UserServiceTest: Tests for user registration, authentication, and management
 * - PostServiceTest: Tests for post CRUD operations and search functionality
 * - CategoryServiceTest: Tests for category management operations
 * - CommentServiceTest: Tests for comment CRUD operations
 * - TagServiceTest: Tests for tag management operations
 * 
 * Controller Layer Tests (Unit Tests):
 * - PostControllerTest: Tests for REST API endpoints for posts
 * 
 * Integration Tests (End-to-End Tests):
 * - BlogApiIntegrationTest: Tests complete flow from controller to database
 * 
 * How to run tests:
 * - Run all tests: mvn test
 * - Run specific test class: mvn test -Dtest=UserServiceTest
 * - Run tests by package: mvn test -Dtest="com.blogapi.serviceTests.*"
 * - Run integration tests: mvn test -Dtest="com.blogapi.integrationTests.*"
 * 
 * Test Configuration:
 * - Uses H2 in-memory database for testing
 * - Test profile: application-test.properties
 * - Security is disabled for integration tests
 */
public class TestSuite {
    // This class serves as documentation for the test structure
    // All test classes are automatically discovered by Maven
}
