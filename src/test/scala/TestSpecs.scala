import me.ycan.flink.model.{CountResult, UserEvent}
import me.ycan.flink.util.Converters._
import me.ycan.flink.util.ImplicitOps._
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

trait TestSpecs {

  def generateUserEventsFromFile(flink: ExecutionEnvironment, path: String): DataSet[UserEvent] = {
    flink.readTextFile(path)
      .map(_.convertTo[UserEvent])
      .filter(_.isSuccess)
      .map(_.get)
  }

  private def getPathFromResources(path: String) : String = getClass.getClassLoader.getResource(path).toURI.toString

  val malformedCSVTestDataPath: String = getPathFromResources("test-data/input/malformed-data.csv")
  val withHeaderCSVTestDataPath: String = getPathFromResources("test-data/input/with-header.csv")
  val partialDataPath: String = getPathFromResources("test-data/input/q1-data.csv")
  val allDataPath: String = getPathFromResources("test-data/input/case.csv")

  val malformedCSVTestResult: Seq[UserEvent] = Seq(UserEvent(1535816823,496, "view",13), UserEvent(1536392929,498, "view",23))

  val withHeaderCSVTestResult: Seq[UserEvent] = Seq(UserEvent(1535816823,496,"view",13),
    UserEvent(1536392928,496,"add",69), UserEvent(1536272308,644,"view",16), UserEvent(1536757406,164,"remove",86))

  val q1Result: Seq[CountResult] = Seq(CountResult(496.toString, 1), CountResult(644.toString,1), CountResult(937.toString,1))
  val q2Result: Seq[CountResult] = Seq(CountResult("add", 2), CountResult("click", 2), CountResult("remove",1), CountResult("view", 3))
  val q3Result: Seq[String] = Seq("36", "5", "52", "74", "89")
  val q4Result: Seq[CountResult] = Seq(CountResult("remove",461),
    CountResult("remove",567),
    CountResult("view",649),
    CountResult("view",771),
    CountResult("add",162),
    CountResult("add",866),
    CountResult("add",581),
    CountResult("add",509),
    CountResult("view",421),
    CountResult("remove",553),
    CountResult("remove",274),
    CountResult("view",447),
    CountResult("click",313),
    CountResult("view",154),
    CountResult("add",463),
    CountResult("view",834))

  val q5Result: Seq[Int] = Seq(447, 649, 421, 154, 834, 771).sorted
}
