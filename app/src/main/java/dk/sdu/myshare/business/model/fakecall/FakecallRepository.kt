package dk.sdu.myshare.business.model.fakecall

class FakecallRepository {
    private val fakecallClient: FakeApi = RetrofitClientFake().fakeApi

    suspend fun getFakecallDataById(userId: Int): FakecallData? {
        try {
            val fakecallData = fakecallClient.getFakecallById(userId)
            println("From name: $userId got user: $fakecallData")
            return fakecallData

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}