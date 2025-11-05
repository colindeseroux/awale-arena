FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn install:install-file -Dfile='src/main/libs/logger-0.0.1-SNAPSHOT.jar' -DgroupId='fr.phenix333' -DartifactId='logger' -Dversion='0.0.1-SNAPSHOT' -Dpackaging=jar

RUN mvn clean install -DskipTests

FROM openjdk:26-ea-jdk-slim

WORKDIR /app

RUN apt-get update && \
    apt-get install -y python3 autoconf bison flex gcc g++ git inotify-tools \
                       libprotobuf-dev libnl-route-3-dev libtool make pkg-config protobuf-compiler && \
    git clone https://github.com/google/nsjail.git /tmp/nsjail && \
    cd /tmp/nsjail && make && mv nsjail /usr/local/bin/ && \
    rm -rf /tmp/nsjail /var/lib/apt/lists/*

RUN mkdir -p /app/Logs /root/bots /tmp/sandbox && \
    chmod 777 /app/Logs /root/bots /tmp/sandbox

COPY --from=build /app/target/awale-arena-0.0.1-SNAPSHOT.jar .

COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENV SUPABASE_USER="" \
    SUPABASE_PASSWORD="" \
    CORS_ALLOWED_ORIGINS="*" \
    SANDBOX_COMMAND="nsjail,--disable_proc,--iface_no_lo,--chroot,/tmp/sandbox,--user,nobody,--group,nogroup,--bindmount_ro,/bin,--bindmount_ro,/usr/bin,--bindmount_ro,/lib,--bindmount_ro,/lib64,--bindmount_ro,/usr/lib,--bindmount,/root/bots:/root/bots,--bindmount,/usr/local/openjdk-26:/usr/local/openjdk-26,--env,LD_LIBRARY_PATH=/usr/local/openjdk-26/lib,--rlimit_as,262144000,--" \
    JAVA_PATH="/usr/local/openjdk-26/bin/java" \
    PYTHON_PATH="/usr/bin/python3"

ENTRYPOINT ["/entrypoint.sh"]