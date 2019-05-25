
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ReadingCSVTest extends WordSpec with Matchers with BeforeAndAfterAll with TestSpecs {

  lazy val flink: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

  "Reading csv " must {
    "read user events as expected for malformed data" in {
    val userEvents = generateUserEventsFromFile(flink, malformedCSVTestDataPath)
      userEvents.collect().sortBy(_.date) shouldBe malformedCSVTestResult.sortBy(_.date)
    }

    "read user events as expected with header" in {
      val userEvents = generateUserEventsFromFile(flink, withHeaderCSVTestDataPath)
      userEvents.collect().sortBy(_.date) shouldBe withHeaderCSVTestResult.sortBy(_.date)

    }
  }



}
