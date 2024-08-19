package site.ithinkso.file_sharing_system.learn;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "common")
public class Common {

    @Id
    protected String id;
    private String name;

    @DBRef
    private Common parent;

    protected Common(String id, String name, Common parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }
}
