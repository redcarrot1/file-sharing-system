package site.ithinkso.file_sharing_system.domain;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Getter
public class User {

    private String id;

    private String token;

    public User(String token) {
        this.token = token;
    }
}
