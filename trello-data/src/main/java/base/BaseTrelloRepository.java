package base;

import com.google.gson.FieldNamingPolicy;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;
import solutions.bellatrix.data.http.infrastructure.HttpRepository;
import solutions.bellatrix.data.http.infrastructure.JsonConverter;

public abstract class BaseTrelloRepository<T extends HttpEntity<String, T>> extends HttpRepository<T> {

    protected BaseTrelloRepository(Class<T> entityClass, String pathParameter) {
        super(entityClass,
                new JsonConverter(builder -> {
                    builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
                }),
                () -> {
                    var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
                    var httpContext = new HttpContext(httpSettings);
                    httpContext.addPathParameter(pathParameter);
                    return httpContext;
                });
    }
}
