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


![img_2.png](img/img_2.png)

## Channel and stub

- channel is created once when server is run
- channel should be private
- stub uses channel
- craete stub like a Singleton Bean - it is thread safe

## Sync / Asyc stub
- Синхронный - ожидает ответа
- Асинхронный - register a listener for call back

## Java21
- до 21 java 1 джава поток = 1 поток ОС => это дорого с точки зрения оперативной памяти
- Создание синхронной заглушки дорого с точки зрения того что мы создаем для него один поток который ожидает ответа
