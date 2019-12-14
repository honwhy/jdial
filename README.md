# *com.honey:jdial:1.0-SNAPSHOT*

Inspired by https://mp.weixin.qq.com/s/W9ZKYsnSy8RQbXUjxpr-_Q

run the project,
```
mvn clean compile dependency:properties exec:exec
```
to change command line arguments, modify argument after main class in pom.xml file
```java
<argument>com.honey.Jdial</argument>
<argument>--hostname</argument>
<argument>baidu.com</argument>
<argument>--end-port</argument>
<argument>81</argument>
```
eg.
```java
<argument>com.honey.Jdial</argument>
<argument>--hostname</argument>
<argument>google.com</argument>
<argument>--end-port</argument>
<argument>88</argument>
```