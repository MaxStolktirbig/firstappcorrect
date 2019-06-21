package nl.hu.wac.firstapp.webservices;

import nl.hu.wac.firstapp.domain.Country;
import nl.hu.wac.firstapp.service.WorldService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("/countries")
public class WorldResource {
    private WorldService service = new WorldService();


    private JsonObjectBuilder convert2Json(Country c) {
        JsonObjectBuilder json = Json.createObjectBuilder();
        json.add("code", c.getCode())
                .add("iso3", c.getIso3())
                .add("name", c.getName())
                .add("continent", c.getContinent())
                .add("capital", c.getCapital())
                .add("region", c.getRegion())
                .add("surface", c.getSurface())
                .add("population", c.getPopulation())
                .add("gorvernment", c.getGovernment())
                .add("lat", c.getLatitude())
                .add("lng", c.getLongitude());
        return json;
    }
    @GET
    public String test(){
        JsonObjectBuilder allCountries = Json.createObjectBuilder();
        int index = 0;
        ArrayList<String> allCountriesList = new ArrayList<>();
        for(Country c: service.getAllCountries()){
            allCountriesList.add(c.getCode());
        }
        Collections.sort(allCountriesList);
        for(String s: allCountriesList){
            Country c = service.getCountryByCode(s);
            allCountries.add(Integer.toString(index), convert2Json(c));
            index++;
        }
        return allCountries.build().toString();
    }
    @GET
    @Path("/largestsurfaces")
    public String getLargestSufaces(){
        JsonObjectBuilder lagestSufaceJson = Json.createObjectBuilder();
        for(Country c: service.get10LargestSurfaces()){
            lagestSufaceJson.add(c.getCode(), convert2Json(c));
        }
        return lagestSufaceJson.build().toString();
    }

    @GET
    @Path("/largestpopulations")
    public String getLargestPopulation(){
        JsonObjectBuilder lagestPopulationJson = Json.createObjectBuilder();
        for(Country c: service.get10LargestPopulations()){
            lagestPopulationJson.add(c.getCode(), convert2Json(c));
        }
        return lagestPopulationJson.build().toString();
    }


    @GET
    @Path("{id}")
    public String getCountry(@PathParam("id") String id){
        id = id.toUpperCase();
        Country c = service.getCountryByCode(id);
        JsonObject countryJson = convert2Json(c).build();
        return countryJson.toString();
    }

    @DELETE
    @Path("{id}")
    public boolean deleteCountry(@PathParam("id") String id){
        id = id.toUpperCase();
        Country c = service.getCountryByCode(id);
        return service.deleteCountry(c);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public boolean updateCountry(@PathParam("id") String id,
                                 @FormParam("country") String country,
                                 @FormParam("capital") String captital,
                                 @FormParam("population") int population,
                                 @FormParam("region") String region,
                                 @FormParam("surface") double surface){
        System.out.println(captital+country+region+population+surface);
        id = id.toUpperCase();
        Country c = service.getCountryByCode(id);
        c.setName(country);
        c.setCapital(captital);
        c.setPopulation(population);
        c.setRegion(region);
        c.setSurface(surface);
        return service.updateCountry(c);
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public boolean saveCountry(@FormParam("id") String id,
                                 @FormParam("country") String country,
                                 @FormParam("capital") String captital,
                                 @FormParam("population") int population,
                                 @FormParam("region") String region,
                                 @FormParam("surface") double surface){
        System.out.println(captital+country+region+population+surface);
        id = id.toUpperCase();
        Country c = new Country();
        c.setCode(id);
        c.setName(country);
        c.setCapital(captital);
        c.setPopulation(population);
        c.setRegion(region);
        c.setSurface(surface);
        return service.saveCountry(c);
    }

}


