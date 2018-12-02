package de.uni.oldenburg.dyspuzzle.handler;

import de.uni.oldenburg.dyspuzzle.dataStructures.Data;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// used library:
// https://square.github.io/retrofit/
public interface ITelemetryService {

    // http://srvgvm33.offis.uni-oldenburg.de:8080/1/telemetry
    @POST("telemetry")
    Call<Object> sendTelemetryData(@Body Data data);

}



