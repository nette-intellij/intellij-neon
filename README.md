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
* ... more to come


## Install
**Stable version** (not-crashing alpha) is available in official plugin repository:

1. Go to File → Settings in PhpStorm
2. Select *Plugins* on the left
3. Click *Browse Repositories* button on the bottom
4. Find *NEON support* and install it
5. Optional: Install also *Nette framework helpers* plugin ;)


**Unstable build** is available [here](http://juzna.cz/intellij-neon.jar).


If you prefer your own build from *master*, open this project in *IntelliJ IDEA* as a *Plugin project*, go to *Build* -> *Prepare for deployment* and install it in your PhpStorm.


## Development
This plugin is in its early development phase, it's not complete at all and it will contain many bugs!
 Be careful if you decide to use it.

Please tell me what **features** you'd like in *Issues tab*.

If you want to contribute, please read [development.md](https://github.com/juzna/intellij-neon/blob/master/development.md)
