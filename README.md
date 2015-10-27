# Tournament Scheduler

This is a heuristic approach to solve a tournament scheduling problem respecting
earliest and latest start, minimal and maximal spacing between two games.
These constraints can be violated in order to get a better schedule, but each
violation is penalized exponentially.

## Usage

```sh
$ java -jar TournamentScheduler.jar [<game_file> <spacing_file> <time_limit>
                                     <neighbor_range> <num_shuffle>]
```

`<game_file>` path to the file with game informations
(**default:** `input.txt`)   
`<spacing_file>` path to the file with spacing informations
(**default:** `intput-Min.txt`)  
`<time_limit>` time limit in seconds
(**default:** 300)   
`<neighbor_range>` range for the neighborhood generation 
(**default:** 0 - calculated from the number of games)  
`<num_shuffle>` number of shuffles done to find a new starting sequence 
(**default:** 0 - calculated from the number of games) 

### Game file

The game file contains the *game id*, *team 1*, *team 2*, *earliest start*,
*latest start*, *category* and *is final* separated by tabs.

```
1	A	B	0	12	2	0
2	C	D	4	20	2	1
...
```

### Spacing file

The spacing file contains the *game 1*, *game 2* and *minimal spacing* separated
by tabs.

```
1	2	5
2	4	5
...
```
