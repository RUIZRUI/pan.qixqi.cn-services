package cn.qixqi.pan.fs.repository.impl;

import cn.qixqi.pan.fs.model.FolderChildren;
import cn.qixqi.pan.fs.model.FolderLink;
import cn.qixqi.pan.fs.repository.FolderLinkRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FolderLinkRepositoryImpl implements FolderLinkRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public FolderLink findByFolderId(String uid, String folderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(folderId));
        query.addCriteria(Criteria.where("uid").is(uid));
        FolderLink folderLink = mongoTemplate.findOne(query, FolderLink.class);
        return folderLink;
    }

    @Override
    public List<FolderLink> findByUid(String uid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uid").is(uid));
        List<FolderLink> folderLinkList = mongoTemplate.find(query, FolderLink.class);
        return folderLinkList;
    }

    @Override
    public List<FolderLink> findAll(){
        return mongoTemplate.findAll(FolderLink.class);
    }

    @Override
    public FolderLink save(FolderLink folderLink) {
        return mongoTemplate.save(folderLink);
    }

    @Override
    public void update(FolderLink folderLink) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(folderLink.getFolderId()));
        query.addCriteria(Criteria.where("uid").is(folderLink.getUid()));

        Update update = new Update();
        if (folderLink.getFolderName() != null){
            update.set("folderName", folderLink.getFolderName());
        }
        if (folderLink.getParent() != null){
            update.set("parent", folderLink.getParent());
        }
        if (folderLink.getChildren() != null){
            update.set("children", folderLink.getChildren());
        }
        mongoTemplate.updateFirst(query, update, FolderLink.class);
    }

    @Override
    public void deleteByFolderId(String uid, String folderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uid").is(uid));
        query.addCriteria(Criteria.where("_id").is(folderId));
        mongoTemplate.remove(query, FolderLink.class);
    }

    /**
     * ????????????????????????????????????????????????????????????
     * ??????????????????????????????
     * @param folderLink
     * @return ????????????
     */
    @Override
    public long addChildren(FolderLink folderLink) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(folderLink.getFolderId()));
        query.addCriteria(Criteria.where("uid").is(folderLink.getUid()));

        Update update = new Update();
        if (folderLink.getChildren().getFolders() != null){
            update.push("children.folders").each(folderLink.getChildren().getFolders());
        }
        if (folderLink.getChildren().getFiles() != null){
            update.push("children.files").each(folderLink.getChildren().getFiles());
        }
        UpdateResult result = mongoTemplate.updateFirst(query, update, FolderLink.class);
        return result.getModifiedCount();
    }

    /**
     * ????????????????????????????????????????????????????????????
     * ??????????????????????????????
     * @param folderLink
     * @return ????????????
     */
    @Override
    public long deleteChildren(FolderLink folderLink) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(folderLink.getFolderId()));
        query.addCriteria(Criteria.where("uid").is(folderLink.getUid()));

        Update update = new Update();
        update.pullAll("children.folders", folderLink.getChildren().getFolders().toArray());
        update.pullAll("children.files", folderLink.getChildren().getFiles().toArray());

        UpdateResult result = mongoTemplate.updateFirst(query, update, FolderLink.class);
        return result.getModifiedCount();
    }

    /**
     * ????????????????????????????????????????????????????????????
     * ??????????????????????????????
     * ????????????????????????????????????????????????????????????
     * @param folderLink
     * @return ????????????
     */
    @Override
    public long updateChildren(FolderLink folderLink) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(folderLink.getFolderId()));
        query.addCriteria(Criteria.where("uid").is(folderLink.getUid()));

        Update update = new Update();
        if (folderLink.getChildren().getFolders() != null && folderLink.getChildren().getFolders().size() > 0){
            // ???????????????????????????
            query.addCriteria(Criteria.where("children.folders").elemMatch(Criteria.where("folderId").is(
                    folderLink.getChildren().getFolders().get(0).getFolderId())));
            update.set("children.folders.$.folderName", folderLink.getChildren().getFolders().get(0).getFolderName());
        } else if (folderLink.getChildren().getFiles() != null && folderLink.getChildren().getFiles().size() > 0){
            // ????????????????????????
            query.addCriteria(Criteria.where("children.files").elemMatch(Criteria.where("linkId").is(
                    folderLink.getChildren().getFiles().get(0).getLinkId())));
            update.set("children.files.$.linkName", folderLink.getChildren().getFiles().get(0).getLinkName());
        } else {
            return 0;
        }
        UpdateResult result = mongoTemplate.updateFirst(query, update, FolderLink.class);
        return result.getModifiedCount();
    }
}
