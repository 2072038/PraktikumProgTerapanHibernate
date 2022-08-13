package com.pterapan.demosql.dao;

import java.util.List;

public interface DaoInterface<T> {
    List<T> getData();
    void addData(T data);
    int delData(T data);
    int updateData(T data);
}
