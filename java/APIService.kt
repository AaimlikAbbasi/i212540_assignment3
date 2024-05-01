import com.example.assignment2.Notification.Myresponse
import com.example.assignment2.Notification.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAASXZWnZc:APA91bFxEiVWTU6AV0TrvWIVvtNeSxOQjZOM1me6XalbWynXAnIKqz8LHvKsfQgFNk-Ts5yacwRrU3aZjeqo-avU0q_S7jgOScsqrZfPheD-VXZ4U7RzSEUHKj_Pbwnp3-RNt3yPqJzV"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender): Call<Myresponse>
}
