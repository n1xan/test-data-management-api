# Test Data Management Module

This module provides a clean, package-based structure for managing test data with the BELLATRIX framework. Each entity type is organized in its own package for better separation of concerns and scalability.

## Project Structure

```
test-data-management-module/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── board/                    # Board entity package
│   │   │   │   ├── Board.java           # Board entity class
│   │   │   │   ├── BoardRepository.java # Board repository
│   │   │   │   └── BoardRepositoryFactory.java # Board factory
│   │   │   ├── card/                     # Card entity package
│   │   │   │   ├── Card.java            # Card entity class
│   │   │   │   ├── CardRepository.java  # Card repository
│   │   │   │   └── CardRepositoryFactory.java # Card factory
│   │   │   └── [entity]/                 # Additional entity packages
│   │   └── resources/
│   │       ├── application.properties
│   │       └── testFrameworkSettings.test.json
│   └── test/
│       └── java/
│           ├── board/
│           │   └── BoardTest.java       # Board tests
│           └── [entity]/
│               └── [Entity]Test.java    # Additional entity tests
└── README.md
```

## Package Structure Benefits

### 1. **Separation of Concerns**
- Each entity type has its own package
- Related classes (Entity, Repository, Factory) are grouped together
- Easy to locate and maintain entity-specific code

### 2. **Scalability**
- Easy to add new entities by creating new packages
- No namespace conflicts between different entity types
- Clear organization for large projects with many entities

### 3. **Maintainability**
- Changes to one entity don't affect others
- Clear package boundaries make refactoring easier
- Better code organization and readability

## Adding New Entities

To add a new entity (e.g., `List`), follow this pattern:

### 1. Create Entity Package
```
src/main/java/list/
├── List.java
├── ListRepository.java
└── ListRepositoryFactory.java
```

### 2. Create Entity Class
```java
package list;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class List extends HttpEntity<String, List> {
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    // Add other fields based on your API
    
    @Override
    public String getIdentifier() {
        return id;
  }
}
```

### 3. Create Repository Class
```java
package list;

import com.google.gson.FieldNamingPolicy;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpRepository;
import solutions.bellatrix.data.http.infrastructure.JsonConverter;

public class ListRepository extends HttpRepository<List> {
    public ListRepository() {
        super(List.class, new JsonConverter(builder -> {
            builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }), () -> {
            var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
            var httpContext = new HttpContext(httpSettings);
            httpContext.addPathParameter("lists"); // API endpoint
            return httpContext;
        });
    }
}
```

### 4. Create Factory Class
```java
package list;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ListRepositoryFactory {
    INSTANCE;

    private final Map<Class<?>, Class<? extends Repository>> repositories = new ConcurrentHashMap<>();
    private ListRepository listRepository;

    public void registerListRepository() {
        repositories.put(List.class, ListRepository.class);
        RepositoryFactory.INSTANCE.registerRepository(List.class, ListRepository.class);
    }

    public ListRepository getListRepository() {
        if (listRepository == null) {
            listRepository = (ListRepository) SingletonFactory.getInstance(ListRepository.class);
        }
        return listRepository;
    }

    public List createDefaultList() {
        return List.builder()
                .name("Default Test List " + System.currentTimeMillis())
                .build();
    }
}
```

### 5. Create Test Class
```java
package list;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class ListTest {
    private ListRepositoryFactory listFactory;
    private String testListId;
    
    @BeforeEach
    public void setUp() {
        ListRepositoryFactory.INSTANCE.registerListRepository();
        listFactory = ListRepositoryFactory.INSTANCE;
    }
    
    @AfterEach
    public void tearDown() {
        if (testListId != null) {
            try {
                List listToDelete = listFactory.createDefaultList();
                listToDelete.setId(testListId);
                listToDelete.delete();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }

    @Test
    public void listLifecycleTest() {
        // Test implementation
    }
}
```

## Environment Variables

The project uses environment variables for secure credential management:

```bash
# Windows PowerShell
$env:API_KEY="your_api_key_here"
$env:API_TOKEN="your_api_token_here"

# Linux/Mac
export API_KEY="your_api_key_here"
export API_TOKEN="your_api_token_here"
```

## Running Tests

```bash
# Run all tests
mvn test -pl test-data-management-module

# Run specific entity tests
mvn test -Dtest=board.* -pl test-data-management-module
mvn test -Dtest=card.* -pl test-data-management-module

# Run specific test method
mvn test -Dtest=board.BoardTest#boardLifecycleTest -pl test-data-management-module
```

## Configuration

Update `testFrameworkSettings.test.json` to match your API:

```json
{
  "httpSettings": {
    "baseUrl": "https://api.your-api.com",
    "timeout": 30000,
    "authentication": {
      "method": "QueryParameters",
      "options": [
        {
          "type": "QueryParameters",
          "insertionOrder": "start",
          "key": "{env_api_key}",
          "token": "{env_api_token}"
        }
      ]
    }
  }
}
```

This structure provides a clean, scalable foundation for test data management with any REST API while maintaining security and best practices.