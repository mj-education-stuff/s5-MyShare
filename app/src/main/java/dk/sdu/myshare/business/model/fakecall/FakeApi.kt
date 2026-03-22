package dk.sdu.myshare.business.model.fakecall

import retrofit2.http.GET
import retrofit2.http.Path

interface FakeApi {
    @GET("/users")
    suspend fun getFakecalls(): List<FakecallData>

   @GET("/users/{id}")
   suspend fun getFakecallById(@Path("id") userId: Int): FakecallData
}