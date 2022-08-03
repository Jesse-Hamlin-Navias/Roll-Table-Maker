import java.io.*;
import java.util.Scanner; // Import the Scanner class to read text files

public class Table_Terminal{

	private Table table;
	private String[] token;
	private String command;
	private String input;

	public Table_Terminal ()
	{
		table = null;
		token = null;
		command = null;
		input = null;
	}
	/*Prints table to terminal.
	  If consecutive non empty entrys have the same content,
	  printed as (beggining entry)-(Final Entry)*/
	private static void print_table(Table table)
    {
    	if((table.get_dice()).max_roll() == 1 && table.get_content(1) != ""){
    		System.out.println(table.get_name() + ":");
    		System.out.println(table.get_content(1));
    	}
    	else
    	{
	        String last_content = "";
	        int combined_entry = 0;
	        boolean combined = false;
	        System.out.println(table.get_name() + " " + (table.get_dice()).get_quantity() + "d" + (table.get_dice()).get_type());

	        Dice dice = table.get_dice();
	        for(int i = 1; i <= (dice.max_roll() - dice.min_roll() + 1); i++)
	        {
	        	//Empty content entrys are treated as unique
	            if((table.get_content(i)).equals(""))
	            {
	                System.out.println((i + dice.min_roll() - 1) + ": ");               
	            }
	            //Prepares consecutive entries that are not unique
	            else if((table.get_content(i)).equals(table.get_content(i + 1)))
	            {
	                if(!combined)
	                {
	                    combined_entry = i - 1 + dice.min_roll();
	                    combined = true;
	                }
	            }
	            /*Prints consecutive entries once running out of consecutive entries,
				  or prints out the unique entry.*/
	            else{
	                if(combined)
	                {
	                    combined = false;
	                    System.out.println((combined_entry) + "-" + (i + dice.min_roll() - 1) + ": " + table.get_content(i));
	                }
	                else{
	                    last_content = table.get_content(i);
	                    System.out.println((i + dice.min_roll() - 1) + ": " + table.get_content(i));
	                }
	            }
	        }
    	}
    }

    private void command_help()
    {
		if(token.length == 1)
		{	System.out.println("Commands: help [command], create [table name], load [table name], print, entry [numbers] [content], delete [numbers], roll [optional table], die [quantity]d[size], name [name], exit");	}
		else
		{
			switch (token[1])
			{
				case "help":
					System.out.println("help [command]: Lists all commands and what they do.");
					System.out.println("Commands: help [command], create [table name], load [table name], print, die [die size], name [name], entry [numbers] [content], delete [numbers], roll, exit");
					break;
				case "create":
					System.out.println("create [table name]: Creates a new table with the given name. Will ask for a die size, in [quantity]d[size] format. Created table becomes current table.");
					break;
				case "load":
					System.out.println("load [table name]: Loads a table that exists in the current directory, and print it. Loaded table becomes current table.");
					break;
				case "print":
					System.out.println("print: Prints the current table. If a table is given as the second argument, prints that table instead without making it the current table.");
				case "die":
					System.out.println("die [die size]: Changes the die size of the current table. [quantity] is number if dice, [size] is how many faces those dice have.");
					break;
				case "name":
					System.out.println("name [table name]: Changes the name of the current table.");
					break;
				case "entry":
					System.out.println("entry [numbers] [content]: Changes the result of rolling the [numbers] given to the [content] in the current table. [numbers] can be a single number, or can be [low]-[high].");
					break;
				case "delete":
					System.out.println("delete [numbers]: Changes the result of rolle the [numbers] given to nothing in the current table. [numbers] can be a single number, or can be [low]-[high].");
					break;
				case "roll":
					System.out.println("roll: Rolls the die for the given table and shows the result. If no table is given, rolls the current table. Automatically rolls dice in entrys and adds numbers together.");
					break;
				case "exit":
					System.out.println("exit: Stops the Table Terminal.");
					break;
				default:
					System.out.println("Not a valid command. Things in brakets [] is information the user must fill in.");
			}
		}
	}

	private void command_print()
	{
		if(token.length == 1){
			if(table == null)
			{ System.out.println("> No table loaded 1"); }
			else { print_table(table); }
		}
		else
		{
			File file = new File(input.substring(6));
			if(!file.exists())
			{	System.out.println("> Failed to find table");	}
			else
			{
				Table holder = null;
				holder = Table_Interpreter.table_in(input.substring(6));
				print_table(holder);
			}
		}
	}

	private void command_roll()
	{
		if(token.length == 1 && (table != null)){
			System.out.println(table.roll_table());
		}
		else if(token.length == 1){
			System.out.println("> No table loaded 2");
		}
		else
		{
			File file = new File(input.substring(5));
			if(!file.exists())
			{	System.out.println("> Failed to find table");	}
			else
			{
				Table holder = null;
				holder = Table_Interpreter.table_in(input.substring(5));
				System.out.println(holder.roll_table());
			}
		}
	}

	private void command_create(Scanner s)
	{
		if(this.token.length > 1)
		{
			String name = input.substring(7);
			File file = new File(name);
			if(file.exists())
			{
				System.out.println("> Table already exists. Try $ name [name]");
			}
			else
			{
				boolean dice_run = true;
				Dice dice;
				while(dice_run){
					System.out.print("Dice: ");
					String d = s.nextLine();
					if(Dice.is_formated(d)){
						dice = Dice.interpret(d);
						dice_run = false;
						table = new Table(name, dice);
						Table_Interpreter.table_out(table);
						//table = Table_Interpreter.table_in(name);
						if(table != null)
						{
							System.out.println("> Successfully created table");
							print_table(table);
						}
					}
					else{
						System.out.println("> Incorrectly Formated");
					}
				}
			}
		}
		else
		{System.out.println("> Please type a name after the create command");}
	}

	private void command_load()
	{
		if(this.token.length > 1)
		{
			File file = new File(input.substring(5));
			if((table != null) && (input.substring(5)).equals(table.get_name()))
			{
				System.out.println("> Table already loaded");
			}
			else if(!file.exists())
			{	System.out.println("> Failed to find table");	}
			else
			{
				table = Table_Interpreter.table_in(input.substring(5));
				System.out.println("> Successfully loaded table");

				print_table(table);
			}
		}
		else
		{System.out.println("> Please type a Table after the load command");}
	}

	private void command_die()
	{
		if(table == null)
			{ System.out.println("> No table loaded"); }
		else if(this.token.length > 1)
		{				
			String d = input.substring(4);
			Dice dice;
			if(Dice.is_formated(d)){
				dice = Dice.interpret(d);
				table.change_dice(dice);
				Table_Interpreter.table_out(table);
				System.out.println("> Successfully changed dice");
				print_table(table);
			}
			else{
				System.out.println("> Incorrectly Formated. Format [quantity]d[size].");
			}
		}
		else
		{System.out.println("> Please type a [quantity]d[type] after the die command");}
	}

	private void command_name()
	{
		if(table == null)
			{ System.out.println("> No table loaded"); }
		else if(this.token.length > 1)
		{
			String name = input.substring(5);
			File check = new File(name);
			if(name.equals(table.get_name()))
			{ System.out.println("> Table already named that"); }
			else if(check.exists())
			{ System.out.println("> You are attempting to overwrite another Table"); }
			else{
				File old_file = new File(table.get_name());
				table.change_name(name);
				Table_Interpreter.table_out(table);
				table = Table_Interpreter.table_in(name);
				old_file.delete();
				System.out.println("> Successfully changed name");
				print_table(table);
			}
		}
		else
		{System.out.println("> Please type a new Table name after the name command");}
	}

	private void command_entry()
	{
		if(table == null)
			{ System.out.println("> No table loaded"); }
		else if(this.token.length > 2)
		{
			if(token[1].indexOf("-") != -1 && ((token[1].substring(0, token[1].indexOf("-"))).matches("\\d*") && ((token[1].substring(token[1].indexOf("-") + 1))).matches("\\d*"))){
				int beg = Integer.parseInt(token[1].substring(0, token[1].indexOf("-")));
				int end = Integer.parseInt(token[1].substring(token[1].indexOf("-") + 1));
				if(beg < end){
					for( ; beg <= end; beg++){
						table.insert_content(input.substring(7 + token[1].length()), beg  - (table.get_dice()).min_roll() + 1);
					}
				}
				else{
					for( ; end <= beg; end++){
						table.insert_content(input.substring(7 + token[1].length()), end  - (table.get_dice()).min_roll() + 1);
					}
				}
				Table_Interpreter.table_out(table);
				 
				print_table(table);
			}
			else if(token[1].matches("\\d*"))
			{
				int entry = Integer.parseInt(token[1]);
				table.insert_content(input.substring(7 + token[1].length()), entry - (table.get_dice()).min_roll() + 1);
				Table_Interpreter.table_out(table);
				 
				print_table(table);
			}
			else{
				System.out.println("> Incorrectly formated");
			}
		}
		else
		{System.out.println("> Please type a number and result after the entry command");}
	}

	private void command_delete()
	{
		if(table == null)
			{ System.out.println("> No table loaded"); }
		else if(this.token.length == 2)
		{			
			if(token[1].indexOf("-") != -1 && ((token[1].substring(0, token[1].indexOf("-"))).matches("\\d*") && ((token[1].substring(token[1].indexOf("-") + 1))).matches("\\d*"))){
				int beg = Integer.parseInt(token[1].substring(0, token[1].indexOf("-")));
				int end = Integer.parseInt(token[1].substring(token[1].indexOf("-") + 1));
				for( ; beg <= end; beg++){
					table.delete_content(beg  - (table.get_dice()).min_roll() + 1);
				}
				Table_Interpreter.table_out(table);
				 
				print_table(table);
			}
			else if(token[1].matches("\\d*"))
			{
				int entry = Integer.parseInt(token[1]);
				table.delete_content(entry - (table.get_dice()).min_roll() + 1);
				Table_Interpreter.table_out(table);
				 
				print_table(table);
			}
			else{
				System.out.println("> Incorrectly formated");
			}
		}
		else
		{System.out.println("> Please type a number after the delete command");}
	}

    /*Manages the command system for the user of the terminal.
      The commands attempt to catch input that would cause errors
      before errors can occur.*/
	public static void main(String args[])
	{
		System.out.println("Commands: help [command], create [table name], load [table name], entry [numbers] [content], delete [numbers], roll [optional table], die [quantity]d[size], name [name], exit");
		boolean run = true;
		Table_Terminal t = new Table_Terminal();
		Scanner s = new Scanner(System.in);
		while(run){
			System.out.println();
			System.out.print("$ ");
			t.input = s.nextLine();
			t.token = t.input.split("\\s+");
			t.command = t.token[0];

			switch (t.command)
			{
				case "die":
					t.command_die();
					break;
				case "name":
					t.command_name();
					break;
				case "entry":
					t.command_entry();
					break;
				case "delete":
					t.command_delete();
					break;
				case "help":
					t.command_help();
					break;
				case "print":
					t.command_print();
					break;
				case "roll":
					t.command_roll();
					break;
				case "exit":
					run = false;
					break;
				case "create":
					t.command_create(s);
					break;
				case "load":
					t.command_load();
					break;
				default:
					System.out.println("> Invalid command");
			}
		}
	}
}