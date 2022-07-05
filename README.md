# Minecraft Data Generator

Minecraft data generator (MCDG) is a tool that allows for the quick and simple generation of textures, blockstates, item models, block models and localisation files. MCDG provides this functionality in two programs that have been combined into one. The first program allows for the generation of textures (MCTG) the second is responsible for all the other stated features (MCFG).

---

## Installation

MCDG requires [Neutron](https://github.com/benjaminheath238/Neutron) and uses [Apache Maven](https://maven.apache.org/) as the build tool both of which must be installed for the following to function.

1. Download the source code either directly from [GitHub](https://github.com/benjaminheath238/Minecraft-Data-Generator/archive/refs/heads/master.zip) or by using `got clone https://github.com/benjaminheath238/Minecraft-Data-Generator.git`
2. If the code was downloaded from GitHub the zip file must be unzipped
3. Enter the downloaded folder containing the `pom.xml`
4. Enter `mvn clean compile assembly:single` into the shell/terminal
5. Collect the jar from `target/mcdg-X.x-jar-with-dependencies.jar`
6. Run the jar using `java -jar mcdg-X.x-jar-with-dependencies.jar` in the folder containing the mod assets (`resources/`)

---

## General Infomation

MCDG requires a mod id and a file to function. The file should contain options for what the program is to do for more infomation of what options are allowed see section MCTG and MCFG. The File is parsed from top to bottom and supports the following;

* sections defined using `[name]`
* sub-sections defined using `[name.subname]`
* simple options defined using `key="value"`
* complex options defined using `name=(key="value", key2="value"...)`

Sub-sections and complex options will inherit the simple options of parents but only if it has not been redefined. Sub-sections can be nested any number of times. All options and must be located in a section. The naming of sections and options is not defined and up to the user but the naming used for complex options will be taken as the registry name and used for locating textures and creating files.

---

## MCTG

MCTG allows textures to be created using a system of layers. Layers can be added on top of each other to create more complex textures using only simple and reusable ones.

### Options

| Name          | Value                           | Information                                                     | Type   | Required |
|---------------|---------------------------------|-----------------------------------------------------------------|--------|----------|
| `type`        | `item`, `tile`                  | tiles are blocks                                                | Option | true     |
| `layer`+_N    | `true`, `false`, A section name | If this is a layer, 0 < N < max int                             | Option | true     |
| `path`        | Any String Value                | The relative path to the texture from `textures/blocks\|items/` | Option | true     |
| `transparent` | `true`, `false`                 | Is this layer transparent or opaque                             | Option | false    |
| `texture`     | `true`, `false`                 | Should a texture be created                                     | Task   | false    |
| `size`        | NxN                             | The texture size, 0 < N < max int                               | option | false    |

### Example File

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

| Name         | Value                       | Information                                                      | Type   | Required |
|--------------|-----------------------------|------------------------------------------------------------------|--------|----------|
| `type`       | `item`, `tile`, `itemGroup` | tiles are blocks, itemGroups are tabs                            | Option | true     |
| `model`      | `true`, `false`             | Should a model file be generated                                 | Task   | false    |
| `locale`     | `true`, `false`             | Should a lang key be generated                                   | Task   | false    |
| `blockstate` | `true`, `false`             | Should a blockstate be generated                                 | Task   | false    |
| `path`       | Any String Value            | The relative path to the texture from  `textures/blocks\|items/` | Option | true     |
| `name`       | Any String Value            | The name to be used for localisation                             | option | false    |
### Example File

```ini
[Blocks]
type="tile"
model="false"
locale="true"
blockstate="false"
path="/"

block_0=(name="Block #0", path="/0/")
block_1=(name="Block #1", model="true")
```
---

## Licensing
This software is licensed under the GPLv2 the definition of which can be found in the [LICENCE](LICENSE) file or at the [website](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html).

MCDG is open source software. Minecraft is a registered trademark of Mojang. MCDG is not an official Minecraft product. It is not approved by or associated with Mojang.
