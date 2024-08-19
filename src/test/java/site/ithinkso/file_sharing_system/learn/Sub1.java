package site.ithinkso.file_sharing_system.learn;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Sub1 extends Common {

    private long childCount;

    @DBRef(lazy = true)
    private List<Common> children;

    public Sub1(String id, String name, Common parent, long childCount) {
        super(id, name, parent);
        this.childCount = childCount;
        this.children = new ArrayList<>();
    }

    public void addChild(Common common) {
        children.add(common);
    }
}
