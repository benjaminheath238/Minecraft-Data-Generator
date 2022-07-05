# Minecraft Data Generator

Minecraft data generator (MCDG) is a tool that allows for the quick and simple generation of textures, blockstates, item models, block models and localisation files. MCDG provides this functionality in two programmes that have been combined into one. The first programme allows for the generation of textures (MCTG) the second is responsible for all the other stated features (MCFG).

---

## Installation

MCDG requires [Neutron](https://github.com/benjaminheath238/Neutron) and uses [Apache Maven](https://maven.apache.org/) as the build tool both of which must be installed for the following to function.

1. Download the source code either directly from [github](https://github.com/benjaminheath238/Minecraft-Data-Generator/archive/refs/heads/master.zip) or by using `got clone https://github.com/benjaminheath238/Minecraft-Data-Generator.git`
2. If the code was downloaded from github the zip file must be unzipped
3. Enter the downloaded folder containing the `pom.xml`
4. Enter `mvn clean compile assembly:single` into the shell/terminal
5. Collect the jar from `target/mcdg-X.x-jar-with-dependencies.jar`
6. Run the jar using `java -jar mcdg-X.x-jar-with-dependencies.jar` in the folder containing the mod assets (`resources/assets/`)

---

## General Infomation

MCDG takes in the mod id and the path to a configuration file. This file is then parsed and the result is used in either MCTG or MCFG or both to create the files. The provided config file can contain the following

* sections defined by `[name]`
* sub-sections defined by `[name.subname]` that support unlimited nesting (`[a.b...z...]`)
* simple options defined by `key="value"`
* complex options defined by `name=(key="value", key2="value"...)` that must be all on the same line

Sub-sections and complex options will inherit parent sections simple options but if there is a conflict (options with the same name) the parent's option will not be inheritted. All types of options must be located in a section or sub-section. The naming used for sections and options is undefined and will not effect the output but the registery name is the key used for complex options and will effect output.

---

## MCTG

MCTG allows textures to be created using a system of layers. Layers can be added on top of each other to create more complex textures using only simple and reusable ones.

### Options

* `type` this option is required, it can be either `tile` if it's a block or `item` if it's an item
* `layer` this option is required, it can be either `true` or `false`, _N can be appended to specify the layer index (0 =< N =< max int) and the value can then be the full name of the layer
* `transparent` this option is required if this is a layer, it can either be `true` or `false`
* `path` this option is required, it's the relative path to the texture from `textures/blocks|items/`, `/` should be used on unix systems (Linux and Mac) and `\` should be used on Microsoft Windows systems
* `size` this option is not required and is only needed if the texture is not 16x16
* `texture` this option can be either true or false, it's not required

### Example Config File

```ini
[Layers]
path="/layers/"
layer="true"
transparent="true"

[Layers.Blocks]
type="tile"

layer_0=()
layer_1=()

[Output]
path="/"
layer="false"
transparent="false"

[Output.Blocks]
type="tile"

block_0=(layer_0="layer_0", layer_1="layer_1")
block_1=(layer_0="layer_1")

```

---

## MCFG

MCFG creates block and item models, blockstates and localisation files.

### Options

* `blockstate` this option can be either `true` or `false`, it can only be used on complex options where `type` equals `tile` and is not required
* `model` this option can be either `true` or `false`, it's not required
* `locale` this option can either be `true` or `false`, it's not required
* `type` this option is required, it can either be `tile`, `item`, `itemGroup`
* `path` this option is required, it's the relative path to the texture from `textures/blocks|items/`, `/` should be used on unix systems (Linux and Mac) and `\` should be used Microsoft Windows systems
* `name` this option is not required, it's the name used in the locale files

### Example Config File

```ini
[Blocks]
type="tile"
model="false"
locale="true"
blockstate="false"

block_0=(name="Block #0")
block_1=(name="Block #1", model="true")
```
---

## Licensing
This programme is licensed under the GPLv2 the definition of which can be found in the LICENCE file or at the [website](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html).

MCDG is open source software. Minecraft is a registered trademark of Mojang. MCDG is not an official Minecraft product. It is not approved by or associated with Mojang.
