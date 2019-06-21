package nl.hu.wac.firstapp;

public class testing {
    public static void main(String[] args){
        String teststring = "SQL UPDATE public.country SET  population = 68000, surfacearea=442.0, capital='Saint John's', continent='North America', governmentform='Constitutional Monarchy', iso3='ATG', latitude=17.12, longitude=-61.85, name='Antigua and Barbuda', region='Caribbean' where code = 'AG'. Expected  char";

        for(int i = 0; i<teststring.length(); i++){
            char c = teststring.charAt(i);
            System.out.println(i+": "+c);
        }
    }
}
