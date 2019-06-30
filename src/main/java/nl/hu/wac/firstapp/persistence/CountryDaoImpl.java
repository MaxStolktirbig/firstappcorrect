package nl.hu.wac.firstapp.persistence;

import nl.hu.wac.firstapp.domain.Country;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CountryDaoImpl implements CountryDao {
    PostgresBaseDao postgresBaseDao = new PostgresBaseDao();


    @Override
    public boolean save(Country country) {




        String executeString = String.format("%s, %s, %s, %s, %s, %s",
                makeSqlText(country.getCode()),
                country.getPopulation(),
                country.getSurface(),
                makeSqlText(country.getCapital()),
                makeSqlText(country.getName()),
                makeSqlText(country.getRegion()));
        System.out.println(executeString);
        try {
            Connection conn = postgresBaseDao.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO public.country(" +
                    " code," +
                    " population," +
                    " surfacearea," +
                    " capital," +
                    " name, " +
                    " region)" +
                    " VALUES ("+executeString+"); COMMIT; ");
            stmt.close();
            conn.close();
            return true;
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
         return false;
    }

    @Override
    public boolean delete(Country country) {
        try{
            Connection conn = postgresBaseDao.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM COUNTRY WHERE CODE = "+makeSqlText(country.getCode())+"; COMMIT;");
            stmt.close();
            conn.close();
            return true;
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Country country) {
        try {
            Connection conn = postgresBaseDao.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("UPDATE public.country SET" +
                    "  population = " + country.getPopulation()+
                    ", surfacearea=" + country.getSurface()+
                    ", capital=" + makeSqlText(country.getCapital())+
                    ", continent=" +  makeSqlText(country.getContinent())+
                    ", governmentform=" + makeSqlText(country.getGovernment())+
                    ", iso3=" +  makeSqlText(country.getIso3())+
                    ", latitude=" +  country.getLatitude()+
                    ", longitude=" + country.getLongitude()+
                    ", name=" + makeSqlText(country.getName())+
                    ", region=" + makeSqlText(country.getRegion())+
                    " where code="+makeSqlText(country.getCode())+"; COMMIT;");
            stmt.close();
            conn.close();
            return true;
        }catch (SQLException sqle){
            sqle.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Country> findAll() {
        try{
            Connection conn = postgresBaseDao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COUNTRY ORDER BY CODE");
            List<Country> returnCtyLst = createCountryList(rs);
            stmt.close();
            conn.close();
            return returnCtyLst;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Country findByCode(String countryCode) {
        try{
            Connection conn = postgresBaseDao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COUNTRY WHERE CODE = '" +countryCode.toUpperCase()+"'");
            rs.next();
            Country returnCty = createCountry(rs);
            stmt.close();
            conn.close();
            return returnCty;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Country> find10LargestPopulations() {
        try{
            Connection conn = postgresBaseDao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COUNTRY ORDER BY POPULATION DESC");
            return createCountryList(rs, 10);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Country> find10LargestSurfaces() {
        try{
            Connection conn = postgresBaseDao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COUNTRY ORDER BY surfacearea DESC");
            return createCountryList(rs, 10);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private Country createCountry(ResultSet rs) {
        try {
            Country returnCoutry = new Country(
                    rs.getString("code"),
                    rs.getString("iso3"),
                    rs.getString("name"),
                    rs.getString("capital"),
                    rs.getString("continent"),
                    rs.getString("region"),
                    rs.getDouble("surfacearea"),
                    rs.getInt("population"),
                    rs.getString("governmentform"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"));
            return returnCoutry;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    private List<Country> createCountryList(ResultSet rs){
        List<Country> countries = new ArrayList<>();
        try {
            while (rs.next()) {
                Country c = createCountry(rs);
                countries.add(c);
            }
            return countries;
        } catch (SQLException sqle){
            sqle.printStackTrace();
            return null;
        }
    }
    private List<Country> createCountryList(ResultSet rs, int iterations){
        List<Country> countries = new ArrayList<>();
        int i = 0;
        try {
            while (rs.next() && i<iterations) {
                Country c = createCountry(rs);
                countries.add(c);
                i++;
            }
            return countries;
        } catch (SQLException sqle){
            sqle.printStackTrace();
            return null;
        }
    }
    private String makeSqlText(String s){
        s.replaceAll("'", "");
        return "'"+s+"'";
    }


}
