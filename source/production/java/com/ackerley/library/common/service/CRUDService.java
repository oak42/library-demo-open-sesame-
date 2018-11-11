package com.ackerley.library.common.service;

import com.ackerley.library.common.entity.BaseEntity;
import com.ackerley.library.common.persistence.CRUDMapper;

import java.util.List;

/**
 * Created by ackerley on 2018/5/20.
 */
public interface CRUDService<M extends CRUDMapper<T>, T extends BaseEntity> {

    int saveOne(T entity);

    T retrieveOne(String ID);

    T retrieveOne(T entityWithID);

    List<T> retrieveList(T entityFilter);

    int deleteOne(T entityWithID);

}
