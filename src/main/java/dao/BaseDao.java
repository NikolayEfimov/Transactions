package dao;

import javax.persistence.EntityManager;

import static db.Database.createEntityManager;

public class BaseDao<T> {

    public T create(T entity) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    public void update(T entity) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
        em.close();
    }
}
