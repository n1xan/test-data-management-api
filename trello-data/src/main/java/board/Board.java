package board;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Board extends HttpEntity<String, Board> {
    
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
    
    @SerializedName("pinned")
    private Boolean pinned;
    
    @SerializedName("starred")
    private Boolean starred;
    
    @SerializedName("subscribed")
    private Boolean subscribed;
    
    // URL fields
    @SerializedName("url")
    private String url;
    
    @SerializedName("shortUrl")
    private String shortUrl;
    
    @SerializedName("shortLink")
    private String shortLink;
    
    // Organization and creator
    @SerializedName("idOrganization")
    private String idOrganization;
    
    @SerializedName("idMemberCreator")
    private String idMemberCreator;

    // Required identity fields
    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public void setIdentifier(String id) {
        this.id = id;
    }
}
