# Java to .jar

## Step 1

```sh
javac Java.java
```

## Step 2

Change the manifest.

## Step 3

```sh
jar cfm java.jar manifest.txt *.class
```

## Step 4

Just for test.

```sh
echo 'START' | java -jar java.jar 1
```

# Java to executable (in development)

nsjail --disable_proc => Error

## Step 1

Go to wsl.

## Step 2

Choose your's.

```sh
wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.2/graalvm-ce-java17-linux-amd64-22.3.2.tar.gz
tar -xzf graalvm-ce-java17-linux-amd64-22.3.2.tar.gz
sudo mv graalvm-ce-java17-22.3.2 /opt/graalvm
export PATH=/opt/graalvm/bin:$PATH
gu install native-image
```

## Step 3

```sh
javac Java.java
```

## Step 4

```sh
native-image Java
```

## Step 5

Just for test.

```sh
echo 'START' | ./java 1
```
