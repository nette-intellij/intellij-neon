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


## Install

1. Go to File → Settings in PhpStorm
2. Select *Plugins* on the left
3. Click *Browse Repositories* button on the bottom
4. Find *NEON support* and install it
5. Optional: Install also *Nette framework helpers* plugin ;)


If you prefer your own build from *master*, open this project in *IntelliJ IDEA* as a *Plugin project*, go to *Build* -> *Prepare for deployment* and install it in your PhpStorm.

## Configuration to enable the plugin

If the plugin doesn't look like it is working, it's probably because it reads the .yml, .yaml as `YAML`files, not as `YAML/Ansible`. To enable it do as follows :

1. Go to XXStorm → Preferences
2. Click on Editor → File Types → YAML/Ansible
3. Add the patterns `*.yaml` and `*.yml`. It will tell you that they are already used, that's right, by `YAML`, so select `Overwrite` (or something like that)

![alt tag](https://image.noelshack.com/fichiers/2017/10/1489001038-capture-d-ecran-2017-03-08-a-20-19-49.png)


## Development

Please tell me what **features** you'd like in *Issues tab*.

If you want to contribute, please read [development.md](https://github.com/juzna/intellij-neon/blob/master/development.md)
