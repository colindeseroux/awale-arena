# Awale Arena API

## Installation

Java 17

---

## To launch locally

### Logger

```sh
mvn install:install-file -Dfile='src/main/libs/logger-0.0.1-SNAPSHOT.jar' -DgroupId='fr.phenix333' -DartifactId=logger -Dversion='0.0.1-SNAPSHOT' -Dpackaging=jar
```

### Launch

```
Run as you like from the command line or ide with SUPABASE_USER and SUPABASE_PASSWORD.
```

### Docker

```sh
docker build -t awale-arena:latest .
```

```sh
docker run -d -e SUPABASE_USER="<user>" -e SUPABASE_PASSWORD="<password>" -v ${PWD}/Logs:/app/Logs -v ${PWD}/bots:/root/bots -p 3001:3001 --name awale-arena awale-arena:latest
```

---

## Debug

All logs are stored in files in Logs.
