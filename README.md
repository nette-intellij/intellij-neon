# Neon in IntelliJ IDEA / PhpStorm

**Current status: stable, basic functions**

[Neon](http://ne-on.org/) is a YAML-like language useful for configuration. It is part of [Nette framework](http://nette.org)
 and it's very nice! You should definitely use it.

This plugin aims to add supports for this language into PhpStorm (or the whole IntelliJ platform).

## Features
* lexer, parser (internal)
* syntax highlighting
* comment/uncomment
* bracket / parenthesis matching
* code completion
* go to class
* ... more to come


Sponsors
------

<a href="https://www.futurerockstars.cz/"><img src=".github/sponsors/future-rockstars.png" alt="FutureRockstars" width="188" height="86"></a>

Does GitHub already have your 💳? Does Nette plugins save you development time? [Send a couple of 💸 a month my way too.](https://github.com/sponsors/mesour) Thank you!

To request an invoice, [contact me](mailto:matous.nemec@mesour.com) through e-mail.


## Install

1. Go to File → Settings in PhpStorm
2. Select *Plugins* on the left
3. Click *Browse Repositories* button on the bottom
4. Find *NEON support* and install it
5. Optional: Install also *Nette framework helpers* plugin ;)


If you prefer your own build from *master*, open this project in *IntelliJ IDEA* as a *Plugin project*, go to *Build* -> *Prepare for deployment* and install it in your PhpStorm.


## Development

Please tell me what **features** you'd like in *Issues tab*.

If you want to contribute, please read [development.md](https://github.com/juzna/intellij-neon/blob/master/development.md)

Building
------------

Plugin uses Gradle to build, but before build you need to install **Grammar-Kit** plugin to Intellij Idea, right click in file explorer to **LatteParser.bnf** located in **com.jantvrdik.intellij.latte**, click to **Generate Parser Code**, then select PSI root to **src/main/gen/com/jantvrdik/intellij/latte/psi** and generated parser as **src/main/gen/com/jantvrdik/intellij/latte/parser/LatteParser.class**

After generating parser files, you need to generate a .flex file from the same .bnf file, right click to file and choose option **Generate JFlex Lexer** and generate **_LatteLexer.flex** in the same folder.

Now you can build plugin using gradle, and it will automatically generate another classes from .flex files, if you want you can help us automate system, so next time no one must do the manual generation work.
```$xslt
$ gradlew build
```

To build `.jar` file to local install to IDE run gradle task `buildPlugin`. `.jar` file with plugin will be located in `build/libs`


Run IDE for testing
-------------------

Create file `local.properties` in project and insert next content with path to IDE directory:

For your current OS see `default IDE paths`: https://www.jetbrains.com/help/idea/tuning-the-ide.html#default-dirs

```
runIdeDirectory = /Users/<user id>/Library/Application Support/JetBrains/Toolbox/apps/PhpStorm/ch-0/201.7223.96/PhpStorm 2019.3 EAP.app/Contents
```

And run gradle task `:runIde` ideally by run configurations in IDEA (it supports debugging).
