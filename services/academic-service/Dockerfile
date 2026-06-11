# Estágio 1: Build e Compilação
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven e o código fonte
COPY pom.xml .
COPY src ./src

# Compila o projeto gerando o arquivo .jar (pulando os testes por enquanto)
RUN mvn clean package -DskipTests

# Estágio 2: Execução leve
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o arquivo .jar gerado no estágio anterior para a imagem final
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta interna padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
