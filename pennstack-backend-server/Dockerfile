FROM openjdk:8-alpine

ARG APPLICATION_DIR=/pennstack-backend
ARG JAR_FILE
ADD target/${JAR_FILE} ${APPLICATION_DIR}/${JAR_FILE}

RUN mkdir -p ${APPLICATION_DIR}
WORKDIR ${APPLICATION_DIR}
EXPOSE 8002

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=America/New_York -jar *.jar