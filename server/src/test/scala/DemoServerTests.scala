import com.winton.DemoServer
import com.winton.demo.{MessageRequest, MessageResponse}
import org.scalatest.{AsyncFlatSpec, Matchers}

class DemoServerTests extends AsyncFlatSpec with Matchers {

  val server: DemoServer = new DemoServer(123)

  "getMessage" should "prefix the sent message" in {
    val message = MessageRequest("Testing123")
    server.getMessage(message).map {
      case MessageResponse(body) => body should endWith("Testing123")
    }
  }

}
