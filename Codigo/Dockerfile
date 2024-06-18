FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk maven locales

# Configurar a localidade para pt_BR.UTF-8
RUN locale-gen pt_BR.UTF-8
ENV LANG pt_BR.UTF-8
ENV LANGUAGE pt_BR:pt
ENV LC_ALL pt_BR.UTF-8

# Copiar o código-fonte
COPY back-end .

# Construir a aplicação com Maven e forçar codificação UTF-8
RUN mvn clean install -Dfile.encoding=UTF-8 -e -X

FROM openjdk:17-jdk-slim

EXPOSE 8080

# Copiar o jar gerado para o contêiner final
COPY --from=build target/gerenciador-lehsa-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]