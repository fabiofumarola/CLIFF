package edu.mit.civic.clavin.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.gazetteer.CountryCode;
import com.berico.clavin.resolver.ResolvedLocation;

/**
 * Once we have selected the candidates, we need to pick what country the document is "about".  This
 * is the most naive "Aboutness" strategy; it just picks the most mentioned country.
 * 
 * @author rahulb
 */
public class FrequencyOfMentionAboutnessStrategy {

    private static final Logger logger = LoggerFactory.getLogger(FrequencyOfMentionAboutnessStrategy.class);

    public static List<CountryCode> select(List<ResolvedLocation> resolvedLocations){
        // count country mentions
        HashMap<CountryCode,Integer> countryCounts = new HashMap<CountryCode,Integer>();
        for (ResolvedLocation resolvedLocation: resolvedLocations){
            if(resolvedLocation.geoname.primaryCountryCode==CountryCode.NULL){
                continue;
            }
            CountryCode country = resolvedLocation.geoname.primaryCountryCode;
            if(!countryCounts.containsKey(country)){
                countryCounts.put(country, 0);
            }
            countryCounts.put(country, countryCounts.get(country)+1);
        }
        // find the most mentioned
        CountryCode primaryCountry = null;        
        for(CountryCode countryCode: countryCounts.keySet()){
            if( (primaryCountry==null) || (countryCounts.get(countryCode) > countryCounts.get(primaryCountry)) ){
                primaryCountry = countryCode;
            }
        }
        logger.info("Found primary country "+primaryCountry);
        // return results
        List<CountryCode> results = new ArrayList<CountryCode>();
        if(primaryCountry!=null) results.add(primaryCountry);
        return results;
    }
    
}