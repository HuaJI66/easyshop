FROM anapsix/alpine-java:8_server-jre_unlimited
ADD target/cloud-guli-gateway-0.0.1-SNAPSHOT.jar /
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=oap:11800 \
    SW_AGENT_NAME=cloud-guli-gateway
EXPOSE 6000
CMD java -jar cloud-guli-gateway-0.0.1-SNAPSHOT.jar
