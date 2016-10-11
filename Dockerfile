FROM openjdk:8-jre-alpine

ENV JAVA_HOME /usr/lib/jvm/java-8-*/
ENV PORT 8080
ENV ADMIN_PORT 8081
ENV ENABLE_NEWRELIC no

EXPOSE 8080
EXPOSE 8081

WORKDIR /app

ADD target/*.yaml /app/
ADD target/pay-*-allinone.jar /app/
COPY data/* /app/data/
RUN ls -a /app/data/*
ADD docker-startup.sh /app/docker-startup.sh
ADD newrelic/* /app/newrelic/

CMD bash ./docker-startup.sh
