package luxe.chaos.choss.auth.service;


import luxe.chaos.choss.entity.Bucket;

import java.util.List;

public interface BucketService {


    void add(Bucket bucket);

    void delete(String id);

    void update(Bucket bucket);

    Bucket getById(String id);

    List<Bucket> getByUserId(String userId);

    List<Bucket> getByTokenId(String tokenId);
}
