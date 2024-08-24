package site.ithinkso.file_sharing_system.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import site.ithinkso.file_sharing_system.domain.MetaData;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MetaDataRepository {

    private final MongoOperations mongoOps;

    public MetaData save(MetaData storedFile) {
        return mongoOps.insert(storedFile);
    }

    public List<MetaData> saveAll(List<? extends MetaData> storedFiles) {
        return storedFiles.stream()
                .map(this::save)
                .toList();
    }

    public boolean updateEntity(MetaData metaData) {
        Criteria criteria = Criteria.where("id").is(metaData.getId());
        Query query = Query.query(criteria);

        MetaData andReplace = mongoOps.findAndReplace(query, metaData);
        return andReplace != null;
    }

}
