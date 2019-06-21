package nl.hu.wac.firstapp.persistence;

public interface UserDao {
    String findRoleForUser(String name, String pass);
}
