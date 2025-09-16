package base;

import com.google.gson.FieldNamingPolicy;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;
import solutions.bellatrix.data.http.infrastructure.HttpRepository;
import solutions.bellatrix.data.http.infrastructure.JsonConverter;

/**
 * Base repository for all Trello entities
 * Provides common configuration for JSON serialization and HTTP context setup
 * 
 * @param <T> The entity type extending HttpEntity
 */
public abstract class BaseTrelloRepository<T extends HttpEntity<String, T>> extends HttpRepository<T> {
    
    /**
     * Constructor for BaseTrelloRepository
     * 
     * @param entityClass The entity class type
     * @param pathParameter The specific path parameter for this entity type (e.g., "boards", "lists", "cards")
     */
    protected BaseTrelloRepository(Class<T> entityClass, String pathParameter) {
        super(entityClass, createJsonConverter(), () -> createHttpContext(pathParameter));
    }
    
    /**
     * Creates a standardized JsonConverter with Trello API naming conventions
     * Trello API uses snake_case for field names
     * 
     * @return Configured JsonConverter instance
     */
    private static JsonConverter createJsonConverter() {
        return new JsonConverter(builder -> {
            builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        });
    }
    
    /**
     * Creates a standardized HttpContext for Trello API calls
     * 
     * @param pathParameter The specific path parameter for the entity type
     * @return Configured HttpContext instance
     */
    private static HttpContext createHttpContext(String pathParameter) {
        var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
        var httpContext = new HttpContext(httpSettings);
        httpContext.addPathParameter(pathParameter);
        return httpContext;
    }
}
