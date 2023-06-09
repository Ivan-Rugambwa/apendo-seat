package kim.almroth.javazeebee;

import kim.almroth.javazeebee.message.dto.UpdateSeatRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

import java.util.UUID;

public interface RetroFitClient {
    @PATCH("api/user/seat/{uuid}")
    Call<Void> updateDbSeat(@Path("uuid") UUID uuid, @Body UpdateSeatRequest updateSeatRequest, @Header("Authorization") String token);
}
