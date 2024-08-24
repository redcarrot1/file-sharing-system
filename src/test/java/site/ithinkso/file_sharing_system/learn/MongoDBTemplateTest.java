package site.ithinkso.file_sharing_system.learn;

import com.mongodb.client.MongoClients;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@ExtendWith(SpringExtension.class)
class MongoDBTemplateTest {

    @Autowired
    private MongoOperations mongoOps;

    @TestConfiguration
    static class Config {
        @Bean
        public MongoOperations mongoOperations() {
            return new MongoTemplate(MongoClients.create(), "test");
        }
    }

    @AfterEach
    void tearDown() {
        mongoOps.dropCollection(Common.class);
    }

    @Test
    @DisplayName("insert and find")
    void test() {
        // given
        mongoOps.insert(new Common("id", "data", null));

        // when
        Common result = mongoOps.query(Common.class)
                .matching(where("name").is("data"))
                .firstValue();

        // then
        assertThat(result.getId()).isEqualTo("id");
        assertThat(result.getName()).isEqualTo("data");
    }

    @Test
    @DisplayName("insert and find with null id")
    void test6() {
        // given
        mongoOps.insert(new Common(null, "data", null));

        // when
        Common result = mongoOps.query(Common.class)
                .matching(where("name").is("data"))
                .firstValue();

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("data");
    }

    @Test
    @DisplayName("object의 @Document(collection=) 보다 직접 collection 이름을 지정한게 우선순위가 높다.")
    void test2() {
        // given
        String collectionName = "testCollection";
        mongoOps.insert(new Common("id", "data", null), collectionName);

        // when
        Common result = mongoOps.query(Common.class)
                .inCollection(collectionName)
                .matching(where("name").is("data"))
                .firstValue();

        // then
        assertThat(result.getId()).isEqualTo("id");
        assertThat(result.getName()).isEqualTo("data");

        // tearDown
        mongoOps.dropCollection(collectionName);
    }

    @Test
    @DisplayName("상속관계 도메인 객체 저장 및 조회")
    void test3() {
        // given
        String id = "id1";
        String name = "name1";
        mongoOps.insert(new Sub1(id, name, null, 0));

        // when
        Sub1 directoryEntity = mongoOps.findById(id, Sub1.class);

        // then
        assertThat(directoryEntity.getId()).isEqualTo(id);
        assertThat(directoryEntity.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("List 타입으로 id reference 저장 및 조회")
    void test4() {
        // given
        Sub1 root = new Sub1("id1", "name1", null, 2);

        Sub1 child1 = new Sub1("id2", "name2", null, 1);
        Sub2 child2 = new Sub2("id3", "name3", null, 100);
        root.addChild(child1);
        root.addChild(child2);

        Sub2 child3 = new Sub2("id4", "name4", null, 100);
        child1.addChild(child3);

        saveDirectory(root);

        // when
        Sub1 rootDirectory = mongoOps.findById("id1", Sub1.class);

        // then
        List<Common> children = rootDirectory.getChildren();
        assertThat(children).hasSize(2)
                .extracting("id")
                .containsExactlyInAnyOrder("id2", "id3");

        Sub1 childDirectory = (Sub1) children.get(0);
        List<Common> child = childDirectory.getChildren();
        assertThat(child).hasSize(1)
                .extracting("id")
                .containsExactlyInAnyOrder("id4");
    }

    @Test
    @DisplayName("업데이트 쿼리")
    void test5() {
        // given
        mongoOps.insert(new Common("id1", "data1", null));

        String after = "newData";
        Query query = new Query(Criteria.where("id").is("id1"));
        Update update = new Update().set("name", after);

        // when
        UpdateResult updateResult = mongoOps.updateFirst(query, update, Common.class);

        // then
        assertThat(updateResult.getModifiedCount()).isEqualTo(1);
        List<Common> users = mongoOps.findAll(Common.class);
        assertThat(users).hasSize(1)
                .extracting("name")
                .containsExactly(after);
    }


    private void saveDirectory(Common common) {
        if (common instanceof Sub2) {
            mongoOps.save(common);
            return;
        }

        Sub1 directory = (Sub1) common;
        List<Common> children = directory.getChildren();
        if (children != null) {
            for (Common child : children) {
                saveDirectory(child);
            }
        }
        mongoOps.save(common);
    }
}
