package nl.hu.wac.firstapp.persistence;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDaoImpl extends PostgresBaseDao implements UserDao{
    public String findRoleForUser(String name, String pass){
        PostgresBaseDao pgdao = new PostgresBaseDao();

        try(Connection conn = pgdao.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT role FROM useraccount WHERE username = '%s' AND password = '%s'", name, pass));
            if(rs.next()){
                String role = rs.getString("role");
                System.out.println(role);
                stmt.close();
                conn.close();
                return role;
            }
            else {
                stmt.close();
                conn.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
