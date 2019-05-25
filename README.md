# flink example

### Build
Run following command to build the jar with running tests:\
```sbt clean assembly```

If you want, you can skip running test with running following command:\
``` sbt 'set test in assembly := {}' clean assembly```

Create docker image:\
```  docker build -t test-flink:0.0.1 . ```

Run the docker container:\
``` docker run -v $(pwd)/data/input:/data/input -v $(pwd)/data/output:/data/output test-flink:0.0.1 ```

