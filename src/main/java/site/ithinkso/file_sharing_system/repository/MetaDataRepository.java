package site.ithinkso.file_sharing_system.repository;

import com.mongodb.client.result.DeleteResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import site.ithinkso.file_sharing_system.domain.MetaData;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class MetaDataRepository {

    private final MongoOperations mongoOps;

    <T> Optional<T> findById(String id, Class<T> entityClass) {
        T entity = mongoOps.findById(id, entityClass);
        return Optional.ofNullable(entity);
    }

    MetaData save(MetaData storedFile) {
        return mongoOps.insert(storedFile);
    }

    List<MetaData> saveAll(List<? extends MetaData> storedFiles) {
        return storedFiles.stream()
                .map(this::save)
                .toList();
    }

    boolean updateEntity(MetaData metaData) {
        Criteria criteria = Criteria.where("id").is(metaData.getId());
        Query query = Query.query(criteria);

        MetaData andReplace = mongoOps.findAndReplace(query, metaData);
        return andReplace != null;
    }

    boolean deleteById(String id) {
        Criteria criteria = Criteria.where("id").is(id);
        Query query = Query.query(criteria);

        DeleteResult result = mongoOps.remove(query, MetaData.class);
        return result.wasAcknowledged();
    }

}
