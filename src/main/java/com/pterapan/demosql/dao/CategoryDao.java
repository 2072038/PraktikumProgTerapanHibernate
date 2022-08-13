package com.pterapan.demosql.dao;

import com.pterapan.demosql.model.CategoryEntity;
import com.pterapan.demosql.util.MyConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class CategoryDao implements DaoInterface<CategoryEntity> {

    @Override
    public List<CategoryEntity> getData() {
        List<CategoryEntity> clist;

        SessionFactory sf = MyConnection.getSessionFactory();
        Session s = sf.openSession();

        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery(CategoryEntity.class);
        q.from(CategoryEntity.class);

        clist = s.createQuery(q).getResultList();

        s.close();
        return clist;
    }

    @Override
    public void addData(CategoryEntity data) {
        SessionFactory sf = MyConnection.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        try {
            s.save(data);
            t.commit();
        } catch (Exception e) {
            t.rollback();
        }
        s.close();
    }

    @Override
    public int delData(CategoryEntity data) {
        int hasil = 0;
        SessionFactory sf = MyConnection.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        try {
            s.delete(data);
            t.commit();
            hasil = 1;
        } catch (Exception e) {
            t.rollback();
        }
        s.close();
        return hasil;
    }

    @Override
    public int updateData(CategoryEntity data) {
        int hasil = 0;
        SessionFactory sf = MyConnection.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        try {
            s.update(data);
            t.commit();
            hasil = 1;
        } catch (Exception e) {
            t.rollback();
        }
        s.close();
        return hasil;
    }
}
