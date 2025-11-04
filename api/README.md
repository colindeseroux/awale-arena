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

Simplified:

```sh
docker run -d -e SUPABASE_USER="<user>" -e SUPABASE_PASSWORD="<password>" -v ${PWD}/Logs:/app/Logs -v ${PWD}/bots:/root/bots -p 3001:3001 --name awale-arena awale-arena:latest
```

All:

```sh
docker run -d -e SUPABASE_USER="<user>" -e SUPABASE_PASSWORD="<password>" -e CORS_ALLOWED_ORIGINS="<default:*>" -e SANDBOX_COMMAND="<default:nsjail,--disable_proc,--iface_no_lo,--chroot,/tmp/sandbox,--user,nobody,--group,nogroup,--bindmount_ro,/bin,--bindmount_ro,/usr/bin,--bindmount_ro,/lib,--bindmount_ro,/lib64,--bindmount_ro,/usr/lib,--bindmount,/root/bots:/root/bots,--bindmount,/usr/local/openjdk-26:/usr/local/openjdk-26,--env,LD_LIBRARY_PATH=/usr/local/openjdk-26/lib,--rlimit_as,262144000,-->" -e JAVA_PATH="<default:/usr/local/openjdk-26/bin/java" -e PYTHON_PATH="<default:/usr/bin/python3" -v ${PWD}/Logs:/app/Logs -v ${PWD}/bots:/root/bots -p 3001:3001 --name awale-arena awale-arena:latest
```

---

## Debug

All logs are stored in files in Logs.
