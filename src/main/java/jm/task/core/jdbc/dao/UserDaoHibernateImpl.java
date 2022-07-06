package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (Id INT NOT NULL AUTO_INCREMENT, " +
                    "name  varchar(45) NOT NULL, " +
                    "lastName varchar(45) NOT NULL, " +
                    "age INT NOT NULL, PRIMARY KEY (id))").addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.getStatus() == TransactionStatus.ACTIVE
                    || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.getStatus() == TransactionStatus.ACTIVE
                    || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("User " + name + " added successfully");
        } catch (Exception e) {
            if (transaction != null && (transaction.getStatus() == TransactionStatus.ACTIVE
                    || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.getStatus() == TransactionStatus.ACTIVE
                    || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> user = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            user = (List<User>) session.createQuery("FROM User").list();
        } catch (Exception ignored) {
        }
        return user;
    }


    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.getStatus() == TransactionStatus.ACTIVE
                    || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }
}
