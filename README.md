## Assets

Image or Sound from these projects or the Internet:

* gtkmonop-0.3.0
* kapitalist-0.4
* monopolie-0.9.7

## Install preconditions

* Java 1.8.0 or newer
* Maven 2.2.1 or newer

## Game Features

* Random map

## Game Rules

1. Monopoly-like game rule
2. Land Tax: each land $400
3. House Tax: each house $200, hotel is $800
4. Question Mark event:
	a. give player money
	b. pay money
	c. forward steps
	d. go to start point or CKS Memorial Hall
	e. stop once
	f. ...
5. Go to hospital will pay $1000
6. Toll is 20% of land value
7. Toll is double each house
8. Toll is 1600% if land have hotel
9. Player can build hotel if 3 houses on the land
10. If player buy all same color land, toll is double
11. Toll free if land owner is in jail
12. Player exceed start point will give $2000, but $0 if on the start point
13. Go to jail stop 3 turns

## How to build and run

to build:

	mvn package

to run:

	mvn exec:java
	# or
	java -jar target/random-big-rich-man-*.jar

