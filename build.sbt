name := "flink-example"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala"    % "1.8.0",
  "org.apache.flink" %% "flink-clients"  % "1.8.0",
  "com.typesafe"     %  "config"         % "1.3.4",
  "ch.qos.logback"   % "logback-classic" % "1.2.3",
  "org.scalatest"    %% "scalatest"      % "3.0.5" % Test
)

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
