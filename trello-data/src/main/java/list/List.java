package list;

import solutions.bellatrix.data.annotations.Dependency;
import board.Board;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class List extends HttpEntity<String, List> {
    
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
    
    @SerializedName("pos")
    private Double pos;
    
    // Board reference
    @SerializedName("idBoard")
    @Dependency(entityType = Board.class)
    private String idBoard;
    
    // URL fields
    @SerializedName("url")
    private String url;
    
    @SerializedName("shortUrl")
    private String shortUrl;
    
    @SerializedName("shortLink")
    private String shortLink;
    
    // Additional fields
    @SerializedName("subscribed")
    private Boolean subscribed;
    
    @SerializedName("softLimit")
    private String softLimit;
    
    @SerializedName("limits")
    private Object limits;
    
    @Override
    public String getIdentifier() {
        return id;
    }
}
