load 5
store @1
store @0

load @1
sub 1
store @1
goto-rel 3 ifz
mul @0
store @0
goto-abs 3

load @0