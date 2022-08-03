public class Table {
    
    private Dice dice; //dice of table. Max size can be derived from this.
    private int size; //current number of entrys, first to last
    private int s_size; //current number of table calls
    private Entry first; //First node of a table
    private Entry last; //last node of a table
    private Entry s_first;
    private Entry s_last;
    private String name; //Name of table

    public class Entry{
        String content;
        Entry next;
    }

    //Create an empty Table with name and dice
    public Table(String name, Dice dice){
        this.name = name;
        this.dice = dice;
        size = 0;
        first = new Entry();
        last = new Entry();
        s_first = null;
        s_last = null;
        s_size = 0;
    }

    //get methods
    public int get_size(){
        return size;
    }

    public Dice get_dice(){
        return dice;
    }

    public String get_name(){
        return name;
    }

    //Changes name of table object
    public void change_name(String name){
    	this.name = name;
    }

    /*Changes dice of table object.
      If new dice would result in a smaller table
      than the current size, cuts off all entries
      that are too big*/
    public void change_dice(Dice dice){
        int range = (dice.max_roll() - dice.min_roll()) + 1;
    	if(range < this.size)
    	{
    		//Changes the size of the table
    		Entry i = first;
            for(int count = 1; count < range; count++)
            	{ i = i.next; }

            Entry search = i.next;
            i.next = null;
            last = i;
            this.size = range;

            //Removes from the search list all tables that are no longer referenced by the table
            while(search != null)
            {
                this.delete_search(search);
                search = search.next;
            }

    	}
    	this.dice = dice;
        //this.print_search();
    }

    /*Insert a string at an entry x, starting at 1.
      If entry number is too big but within dice
      range of table, automatically increases table size*/
    public void insert_content(String content, int x){
        Dice dice = this.get_dice();
        //Adds entries to the end of the table until x is reached, unless x is out of bounds
        if(x <= (dice.max_roll() - dice.min_roll() + 1) && (this.size < x))
        {
            if(this.size == 0)
            {
                first.next = null;
                first.content = "";
                this.size++;
            }

            if((this.size == 1) && (x > 1))
            {
                last.next = null;
                first.next = last;
                last.content = "";
                this.size++;
            }

        	while(this.size < x)
        	{
                Entry oldlast = last;
                last = new Entry();
                last.next = null;
                oldlast.next = last;
                last.content = "";
                this.size++;
        	}

        }

        //Change entry at given x to given content
        if((x <= this.size) && (x > 0))
        {
            Entry i = first;
            for(int count = 1; count < x; count++){
                i = i.next;
            }
            this.delete_search(i);

            //Checks if there are references to other tables in the new content, and adds them to s_first list if so
	    	int index = 0;
	    	while(index < content.length())
	    	{
		    	int start = content.indexOf("(", index) + 1;
		    	int end = content.indexOf(")", index);
		    	if(((start != -1) && (end != -1)) && (start < end))
		    	{
			    	String sub_table = content.substring(start, end);
			    	index = end + 1;
                    //This feels suspect to me
                    if(this.s_size == 0)
                    {
                    	s_first = new Entry();
                        this.s_first.content = sub_table;
                        s_first.next = null;
                        this.s_size++;
                    }
                    else if(this.s_size == 1)
                    {
                    	s_last = new Entry();
                        s_last.next = null;
                        s_first.next = s_last;
                        s_last.content = sub_table;
                        this.s_size++;
                    }
                    else
                    {
                        Entry s_oldlast = s_last;
                        s_last = new Entry();
                        s_last.next = null;
                        s_oldlast.next = s_last;
                        s_last.content = sub_table;
                        this.s_size++;
                    }
		    	}
		    	else
		    	{	index = content.length() + 1;
		    	}
	    	}

            i.content = content;
        }
    }

    //compare should contain one entry, with content of name of the table searching from, pointint to null
    public boolean search(Entry compare){
        Entry s_pointer = this.s_first;
        int x = 0;
        while(x < this.s_size)
        {
            Table sub_table = Table_Interpreter.table_in(s_pointer.content);
            if(sub_table != null)
            {
                Entry pointer = compare;
                while(pointer.next != null)
                {
                    if(pointer.content.equals(sub_table.get_name()))
                    {   return false;   }
                    else
                    {   pointer = pointer.next; }
                }
                if((pointer.content != null) && pointer.content.equals(sub_table.get_name()))
                {   return false;   }
                else
                {
                    Entry old_c = compare;
                    compare = new Entry();
                    compare.content = sub_table.get_name();
                    compare.next = old_c;
                    if(!sub_table.search(compare))
                    {   return false;   }
                    else
                    {
                        if (compare.content != null)
                        {   compare = compare.next; }// delete first node
                    }
                }
            }
            s_pointer = s_pointer.next;
            x++;
        }
        return true;
    }

    public void print_search(){
        System.out.println("S_list " + this.name + ": ");
        Entry pointer = s_first;
        int x = 0;
        while(x < this.s_size){
            System.out.print(pointer.content + ", ");
            pointer = pointer.next;
            x++;
        }
        System.out.println("(" + this.s_size + ")");
    }

    /*Searches an entry's content and deletes any Table
      calls that appear in that content from the search list*/
    private void delete_search(Entry search){
        int index = 0;
        while(index < search.content.length())
        {
            int start = search.content.indexOf("(", index) + 1;
            int end = search.content.indexOf(")", index);
            if(((start != -1) && (end != -1)) && (start < end))
            {
                String sub_table = search.content.substring(start, end);
                index = end + 1;

                if(s_first.content.equals(sub_table))
                {   
                	s_first = s_first.next;
                	this.s_size--;
                }
                else if(this.s_size > 0)
                {
                    Entry pointer = s_first;
                    while(pointer.next != null){
                        if((pointer.next.content).equals(sub_table)){
                            Entry holder = pointer.next;
                            pointer.next = holder.next;
                            this.s_size--;
                        }
                        else
                        {   pointer = pointer.next; }
                    }
                }
            }
            else
            {   index = search.content.length();   }
        }
    }

    /*Returns the content at the entry x, starting at 1
      Returns an empty string if out of bounds*/
    public String get_content(int x){
        if((x <= this.size) && (x > 0))
        {
            Entry i = first;
            for(int count = 1; count < x; count++){
                i = i.next;
            }
            String content = i.content;
            return content;
        }
        else{	return "";	}
    }

    /*Replaces the content at entry x, starting at 1,
      with an emtpy string*/
    public void delete_content(int x){
        if((x <= this.size) && (x > 0))
        {
            Entry i = first;
            for(int count = 1; count < x; count++){
                i = i.next;
            }

            this.delete_search(i);
            i.content = "";
        }
        //this.print_search();
    }

    /*Rolls the table dice and gets entry of the roll.
      Automatically rolls dice in the content of the
      given entry, and adds numbers together after
      rolling dice*/
    public String roll_table(){
        Entry compare = new Entry();
        compare.content = this.get_name();
        compare.next = null;
        if(!this.search(compare))
        {   return "Error: Your tables create an infinite loop somewhere!";    }
    	String s = this.get_content(this.dice.roll() - this.dice.min_roll() + 1);
    	String[] words = s.split("\\s+");
    	String sentance = "";

    	if(!words[0].isEmpty())
    	{
	    	for(int i = 0; i < words.length; i++){
	            //Rolls dice in content
	            String sub_name = "";
	            if(words[i].charAt(0) == '(')
	            {
	                for(int j = i; j < words.length; j++)
	                {
	                    sub_name += words[j] + " ";
	                    if(words[j].charAt(words[j].length() - 1) == ')')
	                    {   j = words.length;   }
	                }
		            if(sub_name.charAt(sub_name.length() - 2) == ')')
		            {
		            	sub_name = sub_name.substring(1, sub_name.length() -2);
		                Table sub_table = Table_Interpreter.table_in(sub_name);
		                if(sub_table != null)
		                {   
		                    String sub_s = sub_table.roll_table();

		                    String[] sub_words = sub_s.split("\\s+");

		                    int sub_name_count = sub_name.split("\\s+").length;

		                    String[] holder = new String[words.length - sub_name_count + sub_words.length];
		                    
		                    for(int j = 0; j < i; j++)
		                    {	holder[j] = words[j];	}
		                    for(int j = 0; j < sub_words.length; j++)
		                    {	holder[j + i] = sub_words[j];	}
		                	for(int j = i + sub_name_count; j < words.length; j++)
		                	{	holder[j - sub_name_count + sub_words.length] = words[j];	}	

		                    words = new String[holder.length];
		                    for(int j = 0; j < words.length; j++)
		                    {   words[j] = holder[j];   }

		                }
		            }
	        	}

	        	if(Dice.is_formated(words[i]))
	    		{
	    			words[i] = String.valueOf(Dice.interpret(words[i]).roll());
	    		}

	            //Adds numbers together
	    		if(i > 1)
	    		{
		    		if((words[i - 2].matches("\\d*") && words[i-1].equals("+")) && words[i].matches("\\d*"))
		    		{
		    			words[i - 2] = String.valueOf(Integer.parseInt(words[i - 2]) + Integer.parseInt(words[i]));
		    			for(int j = i - 1; j < words.length - 2; j++)
		    			{	words[j] = words[j + 2];	}
		    			String[] holder = new String[words.length - 2];
		    			for(int j = 0; j < holder.length; j++)
		    			{	holder[j] = words[j];	}
		    			words = new String[holder.length];
		    			for(int j = 0; j < words.length; j++)
		    			{	words[j] = holder[j];	}
		    			i = i - 2;
		    		}
		    	}
	    	}
	        /*Turns the words array into a string.
	          This process will replace all multi spaces with one space*/
	    	for(int i = 0; i < words.length; i++){
	    		if(i == 0)
	    		{	sentance += words[i];	}
	    		else
	    		{	sentance += " " + words[i];	}
	    	}
    	}
    	return sentance;
    }

    /*public static void main (String[] args)
    {
    	int c = 7;
        Dice dice = new Dice(1, c);
    	Table test = new Table("Test 1d" + c, dice);

        int range = dice.max_roll() - dice.min_roll() + 1;
        for(int i = 1; i <= range; i++){
            test.insert_content((i + ": Thing 1d" + i), i);
        }

        test.print_table();
       	System.out.println(test.roll_table());

    	test.delete_content(1);
    	test.insert_content("Rewrite", 2);
        test.print_table();

        Dice dice2 = new Dice (1, 3);
    	test.change_dice(dice2);
    	test.insert_content((4) + ": Thing", (4));
    	test.change_name("Test 1d" + range);
    	test.print_table();
        Table_Interpreter.table_out(test);
    }*/

}