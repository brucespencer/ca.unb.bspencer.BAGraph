ca.unb.bspencer.BAGraph
=======================

Barabasi-Albert Graphs: these graphs are created using preferential attachment,
i.e. a new node seeks an existing note to connect to, preferring nodes with 
many existing neighbours. THe probability of new node n to attach to existing node
n_k is P(n_k) = number of edges attatched to n_k / total number of existing edges. 

This creates scale-free graphs, described by only one parameter gamma
Probablilty P(k) that a node has k edges is proportional to k^-gamma.
