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
