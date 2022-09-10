# Roll-Table-Maker
A command line application to make and roll nesting TTRPG roll tables. Roll tables are used to randomly generate ideas and content quickly, usefull for improvisation. A roll table is a list of things, each of which have a number of numbers assosiated with it. One rolls a die, finds that number on the roll table, and looks at the thing assosiated with that number. A roll table can use any combination of dice, from a normal six sided die (1d6), to strange combinations like three ten sided dice (3d10). A roll table usually has a catagory. The application comes with the Quest Maker table, which generates quest ideas.

## Beginner's Instructions:
1) Download this Git, unzip it, and extract the main folder from it. Rename the main folder to Roll-Table-Maker
2) Open a terminal, and move the terminal's directory to the Roll-Table-Maker folder.
3) Compile all java files in Roll-Table-Maker. You never need to do steps 1-3 again.
4) Run Table_Terminal.java from the terminal to open the application.
5) Type help to see the commands. Type exit to quit the application.
6) Quest maker is an example table you can roll on. Try typing roll Quest Maker

## Documentation:
Roll-Table-Maker is opperated by typing and entering commands into the terminal. All of those commands are listed below. A [] implies an argument should fill in. {} implies an optional argument.
  ### help {command}
  If no command is given, lists all commands. If a command is given, explains what the command does.
  ### create [table name]
  Creates a new table with the given name. Will ask for a die size, in [quantity]d[size] format. The created table becomes current table.
  ### load [table name]
  Loads a table that exists in the current directory, and print it. Loaded table becomes current table.
  ### print {table name}
  If no table name is given, prints the current table. If a table is given as the second argument, prints that table instead but does not make it the current table.
  ### entry [numbers] [content]
  Changes the result of rolling the [numbers] on the current table to the [content]. [numbers] can be [a single number], or can be [[low]-[high]].
  ### delete [numbers]
  Changes the result of rolling the [numbers] given to nothing in the current table. [numbers] can be a single number, or can be [[low]-[high]].
  ### roll {table name}
  If no table is given, rolls on the current table and shows the result. If a table is given, rolls the dice for that table and shows the result.
  ### die [[quantity]d[size]]
  Changes the die size of the current table. [quantity] is number if dice, [size] is how many faces those dice have. Entries that become out of bounds by reducing the die size will be deleted.
  ### name [new table name]
  Changes the name of the current table to [new table name].
  ### exit
  Stops Roll-Table-Maker and returns to normal terminal functions.
 
 ## Expert Instructions
 Roll Table Maker is a smart application. Here are some extra tips to try out once you've got down the basics.
  ### Sub Tables
  You can roll tables in tables! Thats what Quest Maker does! Anytime an entry has words in (), Roll Table Maker will search for a table with that name, roll on it, and replace the () with what was rolled. This process can go as many layers deep as you like, just be warned that no infinite loops are allowed.
  ### Entry Die Rolling
  If an entry has a die in it, in [quantity]d[size] format, it will roll that die and only print the number! If you want a table that generates the number of something, such as the number of merchants you meet on a road, instead of having a entry for each possible size, you can have a 1d1 sized table, the first and only entry is [quantity]d[size] merchants.
  ### Entry Adding
  If an entry has adding in it, in [number] + [number] format, it will add those numbers together! Roll Table Maker does not accept negative numbers or subtractions. Note that Roll Table Maker rolls the dice in etries first, then adds, allowing you to add rolled dice together.
  ### Deleting Tables and Direct manipulation
  Tables are stored as text files in the Roll-Table-Maker folder. There is no command to delete a table in Roll Table Maker for saftey reasons, but you can delete it manually by opening the folder, finding the text file with the name of the folder you want to delete, and deleting that file. You can also edit those text files directly, which might be faster in some cases.
 
 known bugs:
  help print prints out the help of help die,
  help delete misspelling
