Как прото работает на верхнем уровне?

- для эффективного сжатия каждое поле тегируется числом и идентифициром этим тегом
- наиболее часто используемые тегируйте меньшим числом (от 1 до 16), так они занимают меньше места
- представление в ассоциативном массиве (игнорит поля по умолчанию)
- НЕЛЬЗЯ МЕНЯТЬ ТИП ПОЛЯ
- НЕЛЬЗЯ МЕНЯТЬ ТЭГ ПОЛЯ
- УДАЛЕННЫЙ ТЭГ НУЖНО РЕЗЕРВИРОВАТЬ
- номера тэгов с 1 до 2^29 - 1 
- proto reserved tags 19000-19999

```
tag_number:stored_value
1:sam
2:false (or nothing чтобы экономить место)
3:22
```

## Любое удаленное поле ИЛИ его ТЭГ нужно резервировать чтобы не было проблем со старыми версиями

## Bash
```
./protoc-3.24.0-osx-aarch_64.exe optional.proto --java_out=output
```


![img.png](img/img.png)


![img_1.png](img/img_1.png)


- there are 4 different communication patterns in gRPC

![img_2.png](img/img_2.png)

## Channel and stub

- channel is created once when server is run
- channel should be private
- stub uses channel
- craete stub like a Singleton Bean - it is thread safe

## Sync / Asyc stub
- Синхронный - ожидает ответа
- Асинхронный - register a listener for call back
- only async stub can support all communication paterns
- future stub and blocking stub support only unary and server streaming
## Java21
- до 21 java 1 джава поток = 1 поток ОС => это дорого с точки зрения оперативной памяти
- Создание синхронной заглушки дорого с точки зрения того что мы создаем для него один поток который ожидает ответа
- вызов синхронных блокирующих стабов эффективней в Java21 за счет virtual threads, а иногда даже по перформансу аналогичен скорости как async stub


## CRUD in gRPC 
- **it is action oriented**
- нет никакого GET, POST маппинга

```protobuf
syntax = "proto3";

message Book {};

message BookId {
  int64 id = 1;
}
message Void {}

service BookService {
  rpc GetBook(BookId) returns (Book);
  rpc SaveBook(Book) returns (Book);
  rpc DeleteBook(BookId) returns (Void);
  rpc UpdateBook(Book) returns (Book);
}
```

## Server streaming
- any periodic updates, like weather, stock price, large file download and etc
- клиенту нет необходимости постоянно вызывать сервер
- gRPC is lightweight to use it in Mobile applications

![img.png](img/server-streaming.png)

- gRPC дает гарантию на четкий порядок отправленных сообщений
- если сервер отправляет пачку запросов целиком, быстрее чем клиент успевает обрабатывать - по умолчанию клиент последовательно `one by one sequentially` обрабатывает каждый запрос, 
чтобы распараллелить обработку на стороне клиента можно использовать `ExecutorService`
### Когда использовать стримы, а когда unary call?

#### Stream
- когда рармер ответа неизвестен (сообщение может достигать до 15 ГБ) или большие файлы
- periodic updates (time consuming)
- more efficient when you execute multiple RPC calls
- you need chunks to send data

#### Unary call
- more efficient than streaming RPC
- size is not too big
- operation is not time-consuming
- даже принять моментально getAllAccounts with 100 records это норм для unary call


### Client streaming examples
- Расположение пользователя, геоданные
- IoT устройства (температура, состояние)
- видео стриминг
- загрузка больших файлов

Если клиент со своей стороны прервал стриминг по какой либо ошибке
- хорошее место для коммита - `onCompleted`;
- хорошее место для отката транзакции - `onError`


![img.png](img/client-streaming.png)


### BiDirectional Streaming
- game
- chat
- search response (побуквенный ввод и поиск)
