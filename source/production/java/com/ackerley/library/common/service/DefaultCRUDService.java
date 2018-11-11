package com.ackerley.library.common.service;

import com.ackerley.library.common.entity.BaseEntity;
import com.ackerley.library.common.persistence.CRUDMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ackerley on 2018/5/20.
 * 泛型还需提升理解；半抄半模仿...
 */
public abstract class DefaultCRUDService<M extends CRUDMapper<T>, T extends BaseEntity> extends BaseService{
    @Autowired
    protected M mapper;

    //C、U 统合 单条新entity的insert 与 单条旧entity的update；新、旧 以getIsNewRecord()分辨(即isNewRecord||id==null)...
    @Transactional
    public int saveOne(T entity){
        if(entity.getIsNewRecord()){
            entity.preCreate();
            return mapper.createOne(entity);
        } else {
            entity.preUpdate();
            return mapper.updateOneByID(entity);
        }
    }

    //R
    @Transactional
    public T retrieveOne(String ID){
        return mapper.retrieveOneByID(ID);
    }

    @Transactional
    public T retrieveOne(T entityWithID){
        return mapper.retrieveOneByID(entityWithID);
    }

    @Transactional
    public List<T> retrieveList(T entityFilter){
        return mapper.retrieveList(entityFilter);
    }

    //D
    @Transactional
    public int deleteOne(T entityWithID){
        return mapper.deleteOneByID(entityWithID);
    }
}
