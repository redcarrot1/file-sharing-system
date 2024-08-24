package site.ithinkso.file_sharing_system.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "metadata")
public class MetaData {

    @Id
    protected String id;
    private String name;
    private Structure structure;
    private LocalDateTime createdAt;

    @DBRef(lazy = true)
    private DirectoryEntity parent;

    protected MetaData(String name, Structure structure, LocalDateTime createdAt, DirectoryEntity parent) {
        this.name = name;
        this.structure = structure;
        this.createdAt = createdAt;
        this.parent = parent;
    }
}
