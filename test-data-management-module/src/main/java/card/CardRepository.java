package card;

import com.google.gson.FieldNamingPolicy;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpRepository;
import solutions.bellatrix.data.http.infrastructure.JsonConverter;

public class CardRepository extends HttpRepository<Card> {
    
    public CardRepository() {
        super(Card.class, new JsonConverter(builder -> {
            builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }), () -> {
            var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
            var httpContext = new HttpContext(httpSettings);
            httpContext.addPathParameter("cards");
            return httpContext;
        });
    }
}
