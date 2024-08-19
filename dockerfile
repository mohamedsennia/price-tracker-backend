# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Install dependencies and Chrome
RUN apk add --no-cache \
    curl \
    bash \
    unzip \
    chromium \
    chromium-chromedriver \
    nss \
    freetype \
    harfbuzz \
    ttf-freefont \
    && rm -rf /var/cache/apk/*

# Set the working directory in the container
WORKDIR /app

# Copy the project's compiled jar file to the container
COPY target/*.jar app.jar

# Set the PATH for ChromeDriver and Chrome
ENV PATH="/usr/bin/:$PATH"
ENV CHROME_BIN="/usr/lib/chromium/chrome"

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Environment variables
ENV DB_Name=priceTracker
ENV DB_Password=root
ENV DB_Port=5432
ENV DB_USER=postgres
ENV Host_Name=host.docker.internal

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
