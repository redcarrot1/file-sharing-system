package site.ithinkso.file_sharing_system.learn;

import lombok.Getter;

@Getter
public class Sub2 extends Common {

    private long fileSize;

    public Sub2(String id, String name, Common parent, long fileSize) {
        super(id, name, parent);
        this.fileSize = fileSize;
    }
}
