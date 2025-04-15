## Frameworks used:

As per instruction to keep the solution simple,
I didn't use spring, springboot or any kind of REST controller.
I used only maven to add junit libraries and add any kind of project structure.
I made one class containing the logic of the task andone with tests.


## Input and output:

Class _**MatchManager**_ contains the logic of the task. Its methods accept _String_ objects as arguments.
I assumed these objects should have the same structure as in example in pdf file.
List of matches returned by  _summaryOfMatchesByTotalScore()_ is a single _String_ too (again,
in the format similar to pdf example).
I ignored punctation signs like **_1._**, **_2._**, **a.**, **b.**  itp.
To make sure input is in proper format I check it against the pattern _[\w ]+ [–|-] [\w ]+: \d+ [–|-] \d+_.
I am using two different dashes, because both were used in pdf example. Surely an important part of the task.

The was no information on how to structure arguments to methods _startGame()_ and _finishGame()_,
so I kept names of teams as two arguments.




## Approach to empty data:

I assume scoreboard holds active matches only.
Therefore, after invoking _finishGame()_ for a match it is removed from a scoreboard
and from the system, as we do not have any use for it.

Initially, the scoreboard (i.e. list of active matches) is empty.
It is empty after last match is finished too.
For empty list of active matches method _summaryOfMatchesByTotalScore()_ returns empty _String_ object.


## Updating data:

Updating score is done by passing new score as a single _String_ to _updateScore()_.
Previous score is discarded. This method is idempotent,
which means invoking it many times with the same argument will have the same effect as invoking it once.


## Special cases:

To keep solution simple the only restrictions on names I impose is for them not to be empty or blank,
as it would make summary look weird. This restriction is applied on starting game only,
as finishing with improper team names would result with _IllegalStateException_ for match not found.

I do not impose any restrictions on having the same team in more than one match
(even though it seems very unlikely). I accept odd possibility that two teams have the same name.
It is possible to have two teams play two matches in the same time,
as long as they are on different positions (home team vs away team).
It is not possible to have two teams to play on the same positions in two matches,
because we would not be able to tell them apart during scoring and finishing.