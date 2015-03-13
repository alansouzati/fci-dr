# Introduction #

Dataset is a transactional database consisting of list of transactions and each of them containing a finite set of items. Dimensionality reduction is the process of finding a set of new items (factor-items) which is considerably smaller than the original set. These factor-items aims to comprise full or nearly full information about the original elements. This algorithm has been originally proposed by Petr Krajca, Jan Outrata, and Vilem Vychodil `[`1`]`.

In `[`1`]`, Krajca et al. show that frequent closed itemset can be used to efficiently find factor-items and thus accomplishing data dimensionality reduction. Also, if only frequent closed itemsets is not enough, they propose an on-demand solution to find additional factor-items until a configurable approximation degree is achieved.

To identify frequent closed itemsets this project also implements the LCM algorithm `[`2`]` originally proposed by Uno et al. in 2004. If you are interested just in LCM, there is a jar file in download section (the explanation is out of scope of this project). [Here](http://code.google.com/p/lcmplusplus/) has a similar implementation in C++ for LCM.

# Author #

Alan Souza, Department of Applied Informatics, [Universidade Federal do Rio Grande do Sul](http://www.inf.ufrgs.br/en/).

# Download #

FCI-DR is free software; you can redistribute it and/or modify it under the terms of the Apache License 2.0. FCI-DR is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See Apache License 2.0 the for more details.

# Requeriments #

> Java 5 or higher. Please refer to [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html) to download java.

# Usage #

```
 java -jar fcm-dr.jar <path_to_your_dataset> approx_degree min_sup
```

# Example #

Create a file with the following dataset. Each line represents a transaction, and each column is a transaction item.

```
 1 2 4 6 7 8
 3 5 9 
 1 2 7
 0 1 2 4 7 9
 1 2 4 6 7 8
 0 3 4 5 9
```

Run the following command:

```
 java -jar fcm-dr.jar test.dat 0.94 1.0
```

Test.dat is the path to the dataset, 0.94 is 94% of approximation degree, and 1.0 is 1% of minimum support. This minimum support is used by the LCM algorithm to retrieve the frequent closed itemsets.

Both the approximation degree and minimum support are optional. If not set, approximation
degree will be set to 1.0 (100%) and minimum support is going to be set to 1.0 (1%).

The output is going to be the factorized dataset as a new file into the same location as the original dataset. In the above example, the new file is named "test\_factorized.dat". More specifically, the bellow outputs show the factor-items list and the whole factorized dataset, respectively.

```
Factor-items:

1 = {1 2 7}

2 = {1 2 4 6 7 8}

3 = {3 5 9}

4 = {0 4 9}
```

```
Factorized-transactions:

1 2 
3 
1 
1 4 
1 2 
3 4 
```

# Real-world datasets #

[Mushroom](http://fimi.ua.ac.be/data/mushroom.dat),
[T10I4D100K](http://fimi.ua.ac.be/data/T10I4D100K.dat),
[T10I4D100K](http://fimi.ua.ac.be/data/T10I4D100K.dat)

Additional datasets can be found at: http://fimi.ua.ac.be/data/, http://archive.ics.uci.edu/ml/, http://kdd.ics.uci.edu/

# References #

`[`1`]` P. Krajca, J. Outrata, and V. Vychodil. Using frequent closed itemsets for data dimensionality reduction. Data Mining, IEEE International Conference on, 0:1128–1133, 2011.

`[`2`]` T. Uno, M. Kiyomi, and H. Arimura. Lcm ver. 2: Efficient mining algorithms for frequent/closed/maximal itemsets. In FIMI, 2004.