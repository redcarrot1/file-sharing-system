package site.ithinkso.file_sharing_system.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import site.ithinkso.file_sharing_system.domain.User;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final MongoOperations mongoOps;

    public boolean existsByToken(String token) {
        Criteria criteria = Criteria.where("token").is(token);
        Query query = Query.query(criteria);
        return mongoOps.exists(query, User.class);
    }
}
