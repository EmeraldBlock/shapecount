# shapecount
Code to find the number of shapes possible in the [alpha release](https://alpha.shapez.io) of the game [shapez.io](https://www.shapez.io).

Below is a summary of the results:

Configurations by depth of manufacturing tree (longest sequence of operations). This ignores quad types and colors.
|Depth|New|Cumulative|
|---|---|---|
|0|1|1|
|1|7|8|
|2|48|56|
|3|1456|1512|
|4|26604|28116|
|5|2576|30692|
|6|10018|40710|
|7|6466|47176|
|8|1192|48368|
|9|64|48432|

Configurations and shapes by number of quads.
|Quads|Configurations|Shapes|
|---|---|---|
|0|0|0|
|1|4|128|
|2|18|18432|
|3|68|2228224|
|4|289|303038464|
|5|1112|37312528384|
|6|3408|3659312136192|
|7|7524|258522671480832|
|8|10729|11796660254408704|
|9|10636|374220981536817152|
|10|7798|8779767473558781952|
|11|4332|156076748686151909376|
|12|1817|2094858373870640955392|
|13|560|20660353362554697809920|
|14|120|141670994486089356410880|
|15|16|604462909807314587353088|
|16|1|1208925819614629174706176|
|Total|48432|1977980178440479560583296|

Out of a theoretical 65536 configurations and 1977985201462558877934081 shapes, 48432 (~73.9%) configurations and 1977980178440479560583296 (~99.9997%) shapes are possible.
