	import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class
import java.util.Scanner; // Import the Scanner class to read text files

public class Table_Interpreter{

	private static File myObj; //Used to store tables in methods
	private static Scanner myReader; //Used to read txt files
	//private static Table in;

	//Sets up scanner to return lines of txt file.
	private static boolean read_file(String file)
	{
		try
		{
			myObj = new File(file);
			myReader = new Scanner(myObj);
			return true;
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
	}

	//Turns a text file into a returned Table object
	public static Table table_in(String file){
		String content;
		Table in = null;

		if(read_file(file)){

			//Constructs a Table based on txt file name and first line as dice
			if(myReader.hasNextLine()){
				Dice d = Dice.interpret(myReader.nextLine());
					in = new Table(myObj.getName(), d);

			//Takes every line after the first as a new entry
				for(int i = 1; myReader.hasNextLine(); i++){
					content = myReader.nextLine();
					in.insert_content(content, i);
				}
			}

			myReader.close();
			return in;
		}

		return null;
	}

	//Turns a Table object into a text file
	public static void table_out(Table table){
		String name = table.get_name();
		Dice dice = table.get_dice();
		String dice_string = String.valueOf(dice.get_quantity()) + "d" + String.valueOf(dice.get_type());

		//Create File, or find file that already exists
      		try
      		{
	      		FileWriter myWriter = new FileWriter(name);
	      		myWriter.write(dice_string + "\n");
	            for(int i = 1; i <= table.get_size(); i++){
	                myWriter.write(table.get_content(i) + "\n");
	            }
	      		myWriter.close();
	    	}
	    	catch (IOException e) {
	    	}


	}

	/*public static void main(String args[])
	{
		boolean run = true;
		Scanner input = new Scanner(System.in);
		while(run){
			String file = input.nextLine();
			if(file.equals("stop"))
				{	run = false;	}
			else{
				Table in = table_in(file);
				if(in != null){
					in.print_table();
				}
			}
		}
	}*/
}