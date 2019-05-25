import me.ycan.flink.model._
import me.ycan.flink.util.Helper.distinctCounter
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class CalculationTests extends WordSpec with Matchers with BeforeAndAfterAll with TestSpecs {

  lazy val flink: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

  val userEventsAllData: DataSet[UserEvent] = generateUserEventsFromFile(flink, allDataPath)

  "Creating Unique Product View counts by ProductId" must {
    "return as expected" in {
      val userEvents = generateUserEventsFromFile(flink, partialDataPath)
      distinctCounter(userEvents.filter(_.eventName == "view"), ProductId).collect().sortBy(_.key) shouldBe q1Result
    }
  }

  "Unique Event counts" must {
    "return as expected" in {
      val userEvents = generateUserEventsFromFile(flink, partialDataPath)
      distinctCounter(userEvents, EventName).collect().sortBy(_.key) shouldBe q2Result
    }
  }

  "Top 5 Users who fulfilled all the events (view,add,remove,click)" must {
    "return as expected" in {
      val result = distinctCounter(userEventsAllData, UserId)
        .setParallelism(1)
        .sortPartition(_.value, Order.DESCENDING)
        .first(5)
        .map(_.key)

      result.collect().sorted shouldBe q3Result
    }
  }

  "All events of #UserId : 47" must {
    "be calculated as expected" in {
      val result = userEventsAllData
        .filter(_.userId == 47)
        .map(r => CountResult(r.eventName, r.productId))

      result.collect().sortBy(_.value) shouldBe q4Result.sortBy(_.value)
    }
  }

  "Product Views of #UserId : 47" must {
    "be calculated as expected" in {
      val result = userEventsAllData
        .filter(e => e.userId == 47 && e.eventName == "view")
        .map(_.productId)
        .distinct()

      result.collect().sorted shouldBe q5Result
    }
  }

}
