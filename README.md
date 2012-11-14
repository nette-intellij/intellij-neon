# Neon in IntelliJ IDEA / PhpStorm

[Neon](http://ne-on.org/) is a YAML-like language useful for configuration. It is part of [Nette framework](http://nette.org)
 and it's very nice! You should definitely use it.

This plugin aims to add suppors for this language into PhpStorm (or the whole IntelliJ platform).

## Features
* lexer, parser (internal)
* syntax highlighting
* comment/uncomment
* bracket / parenthesis matching
* code completion
* ... more to come

## Install

Open project in *IntelliJ IDEA* as a *Plugin project*, go to *Build* -> *Prepare for deployment* and install in your PHP Storm.
Experimental build available [here](http://juzna.cz/intellij-neon.jar). When it reaches a stable state, it'll be available in plugin repository.

## Development

This plugin is in its early development phase, it's not complete at all and it will contain many bugs!
 Be careful if you decide to use it.

To be able to compile it, add these external libraries to your project: PHP, YAML, CSS - all from plugin dir of your PhpStorm installation.
You may also need to add jUnit 4 to run tests.


Please tell me what **features** you'd like in *Issues tab*.
