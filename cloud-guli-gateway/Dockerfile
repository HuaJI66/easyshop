FROM openjdk:8u342
ADD target/cloud-guli-gateway-0.0.1-SNAPSHOT.jar /
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800 \
    SW_AGENT_NAME=cloud-guli-gateway
EXPOSE 6000
CMD java -jar cloud-guli-gateway-0.0.1-SNAPSHOT.jar
