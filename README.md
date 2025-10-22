## Prerequisites
* Java 17+

## Installing

```bash
git clone https://github.com/swankygawk/RingBuffer.git
```

## Launching example
```bash
cd RingBuffer/src/main/java/org/ringbuffer
java Example.java
```

## Using
```java
RingBuffer<String> buffer = new RingBuffer<>(10);
buffer.put("s");
String message = buffer.get(); // "s"
```
