# flink example

### Requirements to be able to Build
- sbt (simple building tool), main building tool for Scala Project
- Docker (modern abstraction way of virtualization), to build and run the project


### Build
Run following command to build the jar with running tests:\
```sbt clean assembly```

If you want, you can skip running test with running following command:\
``` sbt 'set test in assembly := {}' clean assembly```

Create docker image:\
```  docker build -t flink-example:0.0.1 . ```

### Run
Run the docker container:\
``` docker run -v $(pwd)/data/input:/data/input -v $(pwd)/data/output:/data/output flink-example:0.0.1 ```

- There is input data in the `/data/input` folder, to run the same jobs with different data change the path (`$(pwd)/data/input`) when running Docker container.
- Also, you may want to store resultant data in another folder, to be able to do this; change the `$(pwd)/data/output` part with the path you want.

### Notes

Since the application is configured for using the memory that is assigned to docker container (see the last line of Dockerfile: `-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap`)
Flink Standalone [local environment] runner is selected.


