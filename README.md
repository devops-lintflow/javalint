# javalint

[![Tag](https://img.shields.io/github/tag/devops-lintflow/javalint.svg)](https://github.com/devops-lintflow/javalint/tags)



## Introduction

*javalint* is a static code checker for Java, forked from https://github.com/javaparser/javaparser-maven-sample.



## Prerequisites

- JDK >= 11
- Maven >= 3.8.6



## Build

```bash
export JAVA_HOME=/path/to/openjdk
export PATH=/path/to/maven/bin:$PATH
export PATH=/path/to/openjdk/bin/:$PATH

mvn clean install -Dhttp.proxyHost=http-proxy -Dhttp.proxyPort=80 -Dhttps.proxyHost=https-proxy -Dhttps.proxyPort=80
```



## Run

```bash
java -jar target/javalint-latest-shaded.jar --file src/main/resources/Loop.java
```



## Usage

```
usage: java -jar javalint-latest-shaded.jar --file FILE [--help]

javalint

optional arguments:
  --file  file name (/path/to/name.java)
  --help  show this help message and exit
```



## License

Project License can be found [here](LICENSE).



## Reference

- [javaparser-ast](https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/index.html)
