package dk.sdu.myshare.business.model.fakecall

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientFake {
    private val BASE_URL = "https://fake-json-api.mock.beeceptor.com"

    val client: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val fakeApi: FakeApi by lazy {
        client.create(FakeApi::class.java)
    }
}