# Tournament Scheduler

This is a heuristic approach to solve a tournament scheduling problem respecting
earliest and latest start, minimal and maximal spacing between two games.
These constraints can be violated in order to get a better schedule, but each
violation is penalized exponentially.

## Compiling

Requires Apache Ant and Java Version 8 or above.

```sh
$ ant [options] [targets]

Targets:
  compile   compiles the source code
  doc       generates javadoc
  jar       builds a jar file
  dist      invokes doc and jar
  clean     removes generated files
  run       runs the project
```

## Usage

### Jar

```sh
$ java -jar TournamentScheduler.jar [parameters]
```

### Ant

```sh
$ ant [-Dargs="[parameters]"] run
```

`run` is the default target and can be omitted.


### Parameters

All parameters are optional but must always occur in the following order:

```
name              default value     description
-------------------------------------------------------------------------------------
game_file         input.txt         path to the file with game informations
spacing_file      input-Min.txt     path to the file with spacing informations
time_limit        300               time limit in seconds
neighbor_range    0                 range for the neighborhood generation 
num_shuffle       0                 number of shuffles to find new starting sequence 
```

Using the default values for `neighbor_range` and `num_shuffle` will calculate
them from the number of games.


#### Game file

The game file contains the *game id*, *team 1*, *team 2*, *earliest start*,
*latest start*, *category* and *is final* separated by tabs.

```
1	A	B	0	12	2	0
2	C	D	4	20	2	1
...
```

#### Spacing file

The spacing file contains the *game 1*, *game 2* and *minimal spacing* separated
by tabs.

```
1	2	5
2	4	5
...
```
