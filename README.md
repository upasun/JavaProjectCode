# JavaProjectCode
Routing Project --

MapGraph contains three main methods - and one main helper method

bfs - breadth first search algorithm
djikstra - djikstra's algorithm implementation
aStarSearch - A* algorithm
algoSearch2 - This is the backbone method behind the A* and Djikstra
alorithms, since A* is simply Djikstra with additional weightings.

The SearchTree class is the data structure I use to store intersections
as nodes with parents as I search through the graph. This allows me to
easily trace back to start from goal along my shortest path, and to add
other implementations easily like calculating the weight of each "edge"
in my graph of intersections.

PQcomp - This class is the implements the comparator interface and is
used to determine which node to traverse to next when the PriorityQueue
backing the algoSearch method calls its .poll(). Looking at this code
now, I realize that the constructors were not particularly useful, but I will leave them there in case they find some use in the future.


Text Editor Project --
AutoCompleteDictionaryTrie - used to implement autocomplete

DictionaryBST - used to implement spell check

MarkovTextGeneratorLoL - used to implement markov text generation

NearbyWords - used to offer spelling suggestions for incorrectly spelled
words.
