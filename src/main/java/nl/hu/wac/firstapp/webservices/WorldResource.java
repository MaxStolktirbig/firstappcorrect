package nl.hu.wac.firstapp.webservices;

import nl.hu.wac.firstapp.domain.Country;
import nl.hu.wac.firstapp.service.WorldService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/countries")
public class WorldResource {
    WorldService service = new WorldService();
    private JsonObjectBuilder convert2Json(Country c) {
        JsonObjectBuilder json = Json.createObjectBuilder();
        json.add("code", c.getCode())
                .add("iso3", c.getIso3())
                .add("name", c.getName())
                .add("continent", c.getContinent())
                .add("capital", c.getCapital())
                .add("region", c.getRegion())
                .add("suface", c.getSurface())
                .add("population", c.getPopulation())
                .add("gorvernment", c.getGovernment())
                .add("lat", c.getLatitude())
                .add("lng", c.getLongitude());
        return json;
    }
    @GET
    public String test(){
        return "hello world";
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

}


