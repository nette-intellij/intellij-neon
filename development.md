# Development
This page contains explains how to compile the plugin, how it works and how to contribute to it.

(Note I'm not a super cool Java expert, so some things may not be absolutely correct. If you know a better way, please fix
and send us a pull request.)



## Basics
This projects contains Java code, obviously. It's structured like a Maven project, i.e. source code is under `src/main/java`
and static resources like `plugin.xml` are under `src/main/resources`. Tests are under `src/test` and are separated into
Java code (`java/`) and static assets (`data/`). For development you'll also need some external libraries, which would
probably resist in `lib/` dir (though it is not on GitHub. IDEA will download the libs for you).



## Dependencies
First, you need *Java SDK* (get it from Oracle) and *PhpStorm SDK* (as *IntelliJ IDEA Plugin SDK*; use your local installation
of PhpStorm).

Second, you'll need to add some more stuff from PhpStorm, which is not in its default SDK. That are PHP, YAML, CSS.
Take these from plugin dir of your PhpStorm installation.
(Note: wouldn't it may be better to add these into the SDK itself? It may be easier, but dunno if it's clean way.)

Third, may also need to add *jUnit 4* and/or *testng* to run tests.



## Anatomy of the plugin
*(TODO)*

- Lexer
- Parser
- PSI
- all the cool features


### Lexer
Lexer is written using JFlex - it's in a .flex file, which is then compiled into a Java class. To be able to make changes in
 the source .flex file, install *jflex* and also download *JFlex support* plugin for IDEA and configure it under settings.

Lexer is heavily tested. Code of tests is inspired in [properties plugin](https://github.com/JetBrains/intellij-community/tree/master/plugins/properties)
 from JetBrains and they're pretty simple - each test consists of a) sample code and b) list of expected tokens.


### Parser
Parsing is greatly described in [Custom Language](http://confluence.jetbrains.net/display/IDEADEV/Developing+Custom+Language+Plugins+for+IntelliJ+IDEA)
 documentation page.

Parser should be heavily tested as well, test cases again inspired from *properties*. Here each test case consists of two parts:
 a) a sample code and b) syntax tree of this code dumped to a text file. Should be pretty straightforward.
