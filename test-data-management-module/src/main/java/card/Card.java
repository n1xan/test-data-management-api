package card;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Card extends HttpEntity<String, Card> {
    
    // Core identification fields
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("desc")
    private String description;
    
    // Status fields
    @SerializedName("closed")
    private Boolean closed;
    
    @SerializedName("due")
    private String due;
    
    @SerializedName("dueComplete")
    private Boolean dueComplete;
    
    // Board and list references
    @SerializedName("idBoard")
    private String idBoard;
    
    @SerializedName("idList")
    private String idList;
    
    // URL fields
    @SerializedName("url")
    private String url;
    
    @SerializedName("shortUrl")
    private String shortUrl;
    
    @SerializedName("shortLink")
    private String shortLink;
    
    @Override
    public String getIdentifier() {
        return id;
    }
}
