package site.ithinkso.file_sharing_system.global;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.domain.MetaData;
import site.ithinkso.file_sharing_system.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("local")
public class InitialData {

    @Value("${file.dir}")
    private String fileDir;
    private final MongoOperations mongoOps;

    @PostConstruct
    public void init() {
        mongoOps.dropCollection(MetaData.class);
        mongoOps.dropCollection(User.class);

        saveUser();

        DirectoryEntity root = saveRootDirectory();
        DirectoryEntity dir1 = saveDirectory(root, "dir1");
        DirectoryEntity dir2 = saveDirectory(root, "dir2");

        DirectoryEntity dir3 = saveDirectory(dir1, "dir3");
        root.increaseDirectoryCount(1);

        saveFile(root, "file1.txt", 100);
        saveFile(root, "file2.txt", 200);
        saveFile(dir1, "file3.txt", 300);
        saveFile(dir2, "file4.txt", 400);
        saveFile(dir3, "file5.txt", 500);

        root.increaseFileInformation(3, 1200);
        dir1.increaseFileInformation(1, 500);

        findAndReplace(root);
        findAndReplace(dir1);
        findAndReplace(dir2);
        findAndReplace(dir3);
    }

    private void findAndReplace(DirectoryEntity directory) {
        Criteria criteria = Criteria.where("id").is(directory.getId());
        Query query = Query.query(criteria);
        mongoOps.findAndReplace(query, directory);
    }

    private void saveUser() {
        User user = new User("asdfasdf");
        mongoOps.save(user);
    }

    private DirectoryEntity saveRootDirectory() {
        DirectoryEntity directory = new DirectoryEntity("root", LocalDateTime.now(), null, "/");
        return mongoOps.save(directory);
    }

    private DirectoryEntity saveDirectory(DirectoryEntity parent, String name) {
        DirectoryEntity directory = new DirectoryEntity(name, LocalDateTime.now(), parent, parent.getDirectoryFullPath() + name + "/");
        parent.addChildren(List.of(directory));
        return mongoOps.save(directory);
    }

    private FileEntity saveFile(DirectoryEntity parent, String name, long byteSize) {
        FileEntity file = new FileEntity(name, LocalDateTime.now(), parent, byteSize, fileDir + name);
        parent.addChildren(List.of(file));

        return mongoOps.save(file);
    }
}
