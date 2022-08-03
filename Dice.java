public class Dice{

	private int quantity; //number of dice
	private int type;	  //size of dice

	/*Dice constructor. Dice rolls must be between 1 and 10,000,
	  or quantity and type are both set to 1 */
	public Dice (int x, int y)
	{
		if((x < 1 || y < 1) || ((x * y) > 16383))
		{
			quantity = 1;
			type = 1;
		}
		else
		{
			quantity = x;
			type = y;
		}
	}

	//Get methods
	public int get_quantity()
	{	return quantity;	}

	public int get_type()
	{	return type;	}

	//Rolls the dice given.
	public int roll()
	{
		int total = 0;
		int lquantity = this.get_quantity();
		int ltype = this.get_type();

		for(int i = 0; i < lquantity; i++)
		{ total += (int)(Math.random() * ltype + 1); }
		return total;
	}

	//Same as get_quantity, with a different name for human useage
	public int min_roll()
	{	return quantity;	}

	//Returns the maximum roll that could be produced by a given dice
	public int max_roll()
	{
		return (this.get_quantity() * this.get_type());
	}

	/*tests if a given string is formatted correctly in quantitydtype format,
	  or xdy format for short. Does not check for valid min max roll sizes*/
	public static boolean is_formated(String xdy)
	{
		boolean d_count = false;
		for(int i = 0; i < xdy.length(); i++){
			if(!Character.isDigit(xdy.charAt(i)))
			{ 
				if((xdy.charAt(i)) == 'd'){
					if(d_count) { return false; }
					else if((i == xdy.length() - 1) || (i == 0)){return false;}
					else{ d_count = true; }
				}
				else{ return false; }
			}
		}
		return d_count;
	}

	/*Takes a string, converts the string to a
	  dice object and returns that object.
	  Always use is_formated on string
	  first or will throw an error */
	public static Dice interpret(String xdy)
	{
		int x, y;
		String quantity_holder, type_holder;
		int d = xdy.indexOf("d");
		quantity_holder = xdy.substring(0, d);
		x = Integer.parseInt(quantity_holder);
		type_holder = xdy.substring(d + 1);
		y = Integer.parseInt(type_holder);
		Dice f = new Dice(x, y);
		return f;
	}

}