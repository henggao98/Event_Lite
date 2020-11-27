package uk.ac.man.cs.eventlite.config;

import java.util.List;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapGeocoding {
	
	public static void setLonAndLat(Event event) {
		
		MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
				  .accessToken("pk.eyJ1IjoicmFkaW5hLW1pdGV2YSIsImEiOiJjazk4NGh4YWsxOHloM21ydXltcHh1cHpsIn0.eAuXID0iYkCdIawPI6reNw")
				  .query(event.getVenue().getAddress() +" "+ event.getVenue().getPostcode())
				  .build();
				
		mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
			@Override
			public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
		 
				List<CarmenFeature> results = response.body().features();
		 
				if (results.size() > 0) {
					
			      Point coordinates = results.get(0).center();
			      event.setLongitude(coordinates.longitude());
			      event.setLatitude(coordinates.latitude());
		
				} else {
		           
					//Log.d(TAG, "onResponse: No result found");
		 
				}
			}
		 
			@Override
			public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
				throwable.printStackTrace();
			}
			
		});
	}
		
		public static void setLonAndLat(Venue venue) {
			
			MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
					  .accessToken("pk.eyJ1IjoicmFkaW5hLW1pdGV2YSIsImEiOiJjazk4NGh4YWsxOHloM21ydXltcHh1cHpsIn0.eAuXID0iYkCdIawPI6reNw")
					  .query(venue.getAddress() +" "+ venue.getPostcode())
					  .build();
					
			mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
				@Override
				public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
			 
					List<CarmenFeature> results = response.body().features();
			 
					if (results.size() > 0) {
						
				      Point coordinates = results.get(0).center();
				      venue.setLongitude(coordinates.longitude());
				      venue.setLatitude(coordinates.latitude());
			
					} else {
			           
						//Log.d(TAG, "onResponse: No result found");
			 
					}
				}
			 
				@Override
				public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
					throwable.printStackTrace();
				}
				
			});
	}

}
