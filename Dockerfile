FROM ibmjava:8-jre
ADD /target/scala-2.12/flink-example-*.jar application.jar
ENV INPUT_PATH="./data/input"
ENV OUTPUT_PATH="./data/output"
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8
ENTRYPOINT exec java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar application.jar