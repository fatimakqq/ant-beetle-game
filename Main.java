//Fatima Khalid
//fxk200007
//import tools to build game
import java.io.*;
import java.util.Scanner;

public class Main
{
  public static void main (String[]args) throws IOException
  {
    //Create a 10x10 grid
    Creature[][] grid = new Creature[10][10];

    //Create scanner object for console input
    Scanner consoleIn = new Scanner (System.in);

    //prompt user for initial grid filename
    String fileName;		//to hold file name
      fileName = consoleIn.next ();	//set file name to what user enters

    File inFile = new File (fileName);	//to open file
    Scanner in = new Scanner (inFile);	//to read from file

      FillGrid (inFile, in, grid);	//Fill array with initial 


    char antOutChar;		//Prompt user for character to represent ant in output, store in antOutChar
      antOutChar = (consoleIn.next ().charAt (0));

    char beetleOutChar;		//Prompt user for character to represent beetle in output, store in beetleOutChar
      beetleOutChar = (consoleIn.next ().charAt (0));

    int totalTurns = 0;		//Prompt user for number of turns, store in totalTurns
      totalTurns = consoleIn.nextInt ();

    char direction; //To hold the direction
    String distances;
    
    //game begins!
    for (int turn = 1; turn <= totalTurns; turn++)	//iterates once per turn
    {
        //reset all beetle and ant turns
        for (int j = 0; j < 10; j++)
    	{
    	    for (int i = 0; i < 10; i++)
    	      {
    	          if(grid[i][j] != null)
    	          {
    	            (grid[i][j]).setMovedStatus(false); //no creatures should be marked as "moved"
    	            (grid[i][j]).setIsOffspring(false); //no creatures should be marked as offspring of a previous creature
    	          }
    	            
    	      }
    	}
        
    	//BEETLES MOVE ------------------------------------------------------------
    	for (int col = 0; col < 10; col++) //iterate through grid one space at a time, columns left to right and rows top to bottom
    	{
    	    for (int row = 0; row < 10; row++)
    	      {
        	    if ((grid[row][col]) == null) //if it's an empty space, continue
        		  {
        		    continue;
        		  }
    		    if (((grid[row][col]).getType ()) == 'B')	//beetle found! time to move
    		      {
    		          //System.out.println("beetle at " + row + ", " + col + "starvetimer is at "+((Beetle)(grid[row][col])).getStarveTimer());//edit
        		    distances = BeetleGetDistances (grid, row, col); //get distances of beetle to ants in all directions

        		    if(distances.equals("0000")) //if no ants in orthogonal direction
        		    {   
        		        //rebuild string to get distances from edge instead
        		        distances = "";
        		        
        		        //distance to north edge is ROW number
        		        distances = distances.concat( (Integer.toString(row)) );
        		        //distance to east edge is 9 minus COLUMN number
        		        distances = distances.concat( (Integer.toString(9-col)) );
        		        //distance to south edge is 9 minus ROW number
        		        distances = distances.concat( (Integer.toString(9-row)) );
        		        //distance to west edge is COLUMN number
        		        distances = distances.concat( (Integer.toString(col)) );
        		         
        		        
        		        distances = distances.concat("d"); //d for "edge" - indicates that we'll be moving towards farthest edge
        		        
        		    }
        		    
        		    else //ants in orthogonal direction
        		    {
        		        distances = distances.concat("a"); //a for "ant" - indicates we'll be moving towards nearest ant
        		    }
        		    distances = BeetleMoveTieBreaker(grid, row, col, distances); //checks for ties and updates distances accordingly
        		    
        		    
    		        direction = (grid[row][col]).Move(distances); //get best direction for beetle to move
    		          
    		          int newX = 0, newY = 0; //coordinates of where the beetle will move
    		          boolean ateAnt = false;
    		          
    		          //MOVE NORTH
    		          if(direction == 'N' && (grid[row][col]).getMovedStatus() == false) //if northbound beetle hasn't moved this turn
    		          {
    		              
    		              (grid[row][col]).setMovedStatus(true); //Mark that the beetle has moved
    		              
    		              //new coordinates are right above the beelte
    		              newX = row-1; 
    		              newY = col;
    		              
    		              if(newX<10 && newX >=0) //if the destination is within the grid
    		              {
    		                  if( (grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'A') //if an ant is present at destination, eat it!
        		              {
        		                  grid[newX][newY] = null;
        		                  ateAnt = true;
        		                  ((Beetle)(grid[row][col])).setStarveTimer(5); //mark that beetle ate this turn
        		              }
        		              
        		              //if beetle present at destination, don't move this turn
        		              else if((grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'B')
        		              {
        		                  continue;
        		              }
        		              
        		              //move beetle to destination
        		              grid[newX][newY] = grid[row][col];
        		              grid[row][col] = null; 
    		                  
    		              }
    		              
    		          }
    		          
    		          //MOVE EAST
    		          else if(direction == 'E' && (grid[row][col]).getMovedStatus() == false) //if eastbound beetle hasn't moved this turn
    		          {
    		              (grid[row][col]).setMovedStatus(true); //mark it has moved
    		              
    		              //new coordinates will be to the right of beetle
    		              newX = row;
    		              newY = col+1;
    		              
    		              if(newY<10 && newY >=0) //if destination is within grid 
    		              {
        		              if( (grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'A') //if an ant is present at destination, eat it!
        		              {
        		                  grid[newX][newY] = null;
        		                  ateAnt = true;
        		                  ((Beetle)(grid[row][col])).setStarveTimer(5); //mark that beetle ate this turn
        		              }
        		              
        		              //if beetle present at destination, don't move 
        		              else if((grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'B')
        		              {
        		                  continue;
        		              }
        		              //move beetle
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        	
    		              }
    
    		          }
    		          
    		          //MOVE SOUTH
    		          else if(direction == 'S'&& (grid[row][col]).getMovedStatus() == false) //if southbound beetle hasn't moved this turn
    		          {
    		              (grid[row][col]).setMovedStatus(true); //mark it has moved 
    		              //destination will be directly below beetle
    		              newX = row+1;
    		              newY = col;
    		          
        		          if(newX<10 && newX >=0 ) //if destination is within grid
        		          {
        		              if( (grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'A') //if an ant is present at destination, eat it!
        		              {
        		                  grid[newX][newY] = null;
        		                  ateAnt = true;
        		                  ((Beetle)(grid[row][col])).setStarveTimer(5); //mark that beetle ate this turn
        		              }
        		              
        		              //if beetle present at destination, do not move
        		              else if((grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'B')
        		              {
        		                  continue;
        		              }
        		              //move beetle
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        		              
        		          }
    		              
    		              
    		          }
    		          
    		          //MOVE WEST
    		          else if(direction == 'W'&& (grid[row][col]).getMovedStatus() == false) //if westbound beetle hasn't moved this turn
    		          {
    		              (grid[row][col]).setMovedStatus(true); //mark it has moved
    		              //destination will be to the left of beetle
    		              newX = row;
    		              newY = col-1;
    		              
        		          if(newY<10 && newY >=0) //if detination within bounds
        		          {
            		           if( (grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'A') //if an ant is present at destination, eat it!
            		              {
            		                  grid[newX][newY] = null;
            		                  ateAnt = true;
            		                  ((Beetle)(grid[row][col])).setStarveTimer(5); //mark that beetle ate this turn
            		              }
            		              //if beetle present at destination, do not move
            		              else if((grid[newX][newY])!= null && (grid[newX][newY]).getType() == 'B')
            		              {
            		                  continue;
            		              }
            		              //move beetle
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
            		              
            		              
            		      }
    		          }
    		          
    		          if(!ateAnt && (newX!=0 || newY!=0) )//if no ant eaten, note
    		          {
        		          ((Beetle)(grid[newX][newY])).setStarveTimer((((Beetle)(grid[newX][newY])).getStarveTimer()) - 1);//count down on starve timer by one
    		          }
    		          
    		          
    		      }
    
    	      }
        }
        
        //ANTS MOVE ------------------------------------------------------------
        for (int col = 0; col < 10; col++) //iterate through grid one space at a time, columns left to right and rows top to bottom
    	{
    	    for (int row = 0; row < 10; row++)
    	      {
    	          if ((grid[row][col]) == null)	//if it's an empty space, continue
        		  {
        		    continue;
        		  }
    		      if (((grid[row][col]).getType ()) == 'A') //ant found! time to move
    		      {
    		      
            		    distances = AntGetDistances (grid, row, col); //get distances to beetles in orthogonal directions
            		    
            		    if(distances.equals("0000")) //if no beetles in orthogonal directions
            		    {   
            		        distances = distances.concat("x"); //mark with 'x' for "don't move!"
            		    }
            		    
            		    else //ant has beetles in orthogonal directions
            		    {
            		        distances = distances.concat("m"); //mark with 'm' for "move!"
            		    }
            		    
            		    if(distances.charAt(4) == 'm') //if beetles present in orthogonal directions, check for ties
            		    {
            		        distances = AntMoveTieBreaker(grid, row, col, distances); //check for ties and update distances accordingly
                        }
        		    
    		          direction = (grid[row][col]).Move(distances); //get final direction to move 

    		          //ants move------------------------- 
    		          int newX = 0, newY = 0; //destination coordinates
    		          
    		          if(distances.charAt(4) == 'm') //if running away from nearest beetle
    		          {
        		          //RUN FROM NORTH
        		          if(direction == 'N' && (grid[row][col]).getMovedStatus() == false)
        		          {
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination
        		              newX = row+1;
        		              newY = col;
        		           
        		              if(newX<10 && newX >=0 && grid[newX][newY] == null) //move only if in bounds and space empty
        		              {
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        		              }
        		              
        		          }
        		          //RUN FROM EAST
        		          else if(direction == 'E' && (grid[row][col]).getMovedStatus() == false)
        		          {
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination
        		              newX = row;
        		              newY = col-1;
        		              
        		              if(newY<10 && newY >=0 && grid[newX][newY] == null) //move only if in bounds and space empty
        		              {
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        		              }
    
        		          }
        		          //RUN FROM SOUTH
        		          else if(direction == 'S'&& (grid[row][col]).getMovedStatus() == false)
        		          {
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination
        		              newX = row-1;
        		              newY = col;
        		           
        		              if(newX<10 && newX >=0&& grid[newX][newY] == null) //move only if in bounds and space empty
        		              {
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        		              }
        		              
        		          }
        		          //RUN FROM WEST
        		          else if(direction == 'W'&& (grid[row][col]).getMovedStatus() == false)
        		          {
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination
        		              newX = row;
        		              newY = col+1;
        		           
        		              if(newY<10 && newY >=0 && grid[newX][newY] == null) //move only if in bounds and space empty
        		              {
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        		              }
        		          }
    		          }
    		          else //moving TOWARDS something (an empty path or farthest beetle) 
    		          {
    		              //destination coordinates
        		          newX = 0;
        		          newY = 0;
        		          //MOVE NORTH
        		          if(direction == 'N' && (grid[row][col]).getMovedStatus() == false)
        		          {
        		              
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination
        		              newX = row-1;
        		              newY = col;
        		              
        		              if(newX<10 && newX >=0 && grid[newX][newY] == null) //if destination empty and in bounds
        		              {
            		              //move ant
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        		              }
        		              
        		          }
        		          
        		          //MOVE EAST
        		          else if(direction == 'E' && (grid[row][col]).getMovedStatus() == false)
        		          {
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination
        		              newX = row;
        		              newY = col+1;
        		              
        		            
            		          if(newY<10 && newY >=0 && grid[newX][newY] == null) //if destination empty and in bounds
        		              {
            		              //move ant
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
        		              }
            	
        		          }
        
        		          //MOVE SOUTH
        		          else if(direction == 'S'&& (grid[row][col]).getMovedStatus() == false)
        		          {
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination coordinates
        		              newX = row+1;
        		              newY = col;
        		          
            		          if(newX<10 && newX >=0 && grid[newX][newY] == null ) //if destination in bounds and empty
            		          {
            		              //move ant
                		          grid[newX][newY] = grid[row][col];
                		          grid[row][col] = null;
            		              
            		          }
        		              
        		              
        		          }
        		          
        		          //MOVE WEST
        		          else if(direction == 'W'&& (grid[row][col]).getMovedStatus() == false)
        		          {
        		              (grid[row][col]).setMovedStatus(true);
        		              //calculate destination 
        		              newX = row;
        		              newY = col-1;
        		              
            		          if(newY<10 && newY >=0 && grid[newX][newY] == null) //if destination empty and within bounds
            		          {
                	
            		              grid[newX][newY] = grid[row][col];
            		              grid[row][col] = null;
                		              
                		      }
        		          }
    		          }
    		              
    		       }
    		          
    		  }
    	}
    	
    	//BEETLES STARVE ------------------------------------------------------------
    	for (int col = 0; col < 10; col++)//iterate through grid one space at a time, columns left to right and rows top to bottom
        {
        	    for (int row = 0; row < 10; row++)
        	      {
        	          if ((grid[row][col]) == null)	//if it's an empty space, continue
            		  {
            		    continue;
            		  }
        		      if (((grid[row][col]).getType ()) == 'B') //beetle found
        		      {

        		          if(turn >= 5) //if it's been at least 5 turns, begin checking for starving
        		          {
            		          boolean dead = ((Beetle)(grid[row][col])).Starve(); //Check if beetle has starved to death FIX
            		          if(dead) //if it died
            		          {
            		              grid[row][col] = null; //remove from board
            		          }
        		          }
        		          
        		      }
        	      }
        	}
    	
    	//ANTS BREED ------------------------------------------------------------
    	if(turn%3==0) //every 3 turns
    	{
        	for (int col = 0; col < 10; col++)//iterate through grid one space at a time, columns left to right and rows top to bottom
        	{
        	    for (int row = 0; row < 10; row++)
        	      {
        	          if ((grid[row][col]) == null)	//if it's an empty space, continue
            		  {
            		    continue;
            		  }
        		      if (((grid[row][col]).getType ()) == 'A' && !((grid[row][col]).getIsOffspring())) //ant found and it's an original ant! time to breed
        		      {
        		          String breedingSpots = BreedSpaceCheck(grid, row, col); //gather info on empty spots around ant
        		          
        		          direction = (grid[row][col]).Breed(breedingSpots); //get which direction to breed in

        		          if(direction == 'N') //Create offspring ant north of current ant
        		          {
        		              grid[row-1][col] = new Ant ();
        		              (grid[row-1][col]).setIsOffspring(true);
        		          }
        		          else if(direction == 'E')//Create offspring ant east of current ant
        		          {
        		              grid[row][col+1] = new Ant ();
        		              (grid[row][col+1]).setIsOffspring(true);
        		          }
        		          else if(direction == 'S')//Create offspring ant south of current ant
        		          {
        		              grid[row+1][col] = new Ant ();
        		              (grid[row+1][col]).setIsOffspring(true);
        		          }
        		          else if(direction == 'W')//Create offspring ant west of current ant
        		          {
        		              grid[row][col-1] = new Ant ();
        		              (grid[row][col-1]).setIsOffspring(true);
        		          }
        		      }
        	      }
        	}
    	}
    	
    	//BEETLES BREED ------------------------------------------------------------
    	if(turn%8==0) //every 8 turns
    	{
        	for (int col = 0; col < 10; col++)//iterate through grid one space at a time, columns left to right and rows top to bottom
        	{
        	    for (int row = 0; row < 10; row++)
        	      {
        	          if ((grid[row][col]) == null)	//if it's an empty space, continue
            		  {
            		    continue;
            		  }
        		      if (((grid[row][col]).getType ()) == 'B' && !((grid[row][col]).getIsOffspring())) //beetle found and it's an original beetle! time to breed
        		      {
        		          String breedingSpots = BreedSpaceCheck(grid, row, col); //get info on empty spots surrounding beetle
        		          direction = (grid[row][col]).Breed(breedingSpots); //get direction to breed in

        		          if(direction == 'N') //Create offspring beetle north of current one
        		          {
        		              grid[row-1][col] = new Beetle ();
        		              (grid[row-1][col]).setIsOffspring(true);
        		          }
        		          else if(direction == 'E')//Create offspring beetle east of current one
        		          {
        		              grid[row][col+1] = new Beetle ();
        		              (grid[row][col+1]).setIsOffspring(true);
        		          }
        		          else if(direction == 'S')//Create offspring beetle south of current one
        		          {
        		              grid[row+1][col] = new Beetle ();
        		              (grid[row+1][col]).setIsOffspring(true);
        		          }
        		          else if(direction == 'W')//Create offspring beetle west of current one
        		          {
        		              grid[row][col-1] = new Beetle ();
        		              (grid[row][col-1]).setIsOffspring(true);
        		          }
        		      }
        	      }
        	}
    	}
    	
    	PrintGrid(grid, turn, antOutChar, beetleOutChar); //Print state of the board this turn

    }

  }
  public static void FillGrid (File inFile, Scanner in, Creature[][]grid) throws IOException //fills grid with starting positions
  {
    if (inFile.canRead ())	//If file can successfully be read
      {
    	for (int row = 0; row < 10; row++)	//iterates once per row (line)
    	  {
    	    String s = in.nextLine ();	//holds entire row of file (including whitespace) in a string
    	    for (int col = 0; col < 10; col++)
    	      {
        		if (s.charAt (col) == 'a')	//if a is found, create an ant at that index
        		  {
        		    grid[row][col] = new Ant ();
        		  }
        		else if (s.charAt (col) == 'B')	//if B is found, create a beetle at that index
        		  {
        		    grid[row][col] = new Beetle ();
        		  }
    
    	      }
    	  }


      }
  }
  public static String BeetleGetDistances (Creature[][]grid, int row, int col) //fills a string with distances to beetle's orthogonal ants
  {
    String distances = "temp"; //holds distances
    int antDistance = 0; //holds distance to one ant as we traverse to it
    boolean antFound = false; //whether an ant has been found or not

    //Check for ants in the north ----------------------------------------------------
    for (int i = (row - 1); i >= 0; i--)	//start searching from space above beetle up to first row
      {
    	antDistance++; //distance to ant increases
    
    	if ( grid[i][col] != null && ((grid[i][col]).getType()) == 'A' ) //if an ant is found
    	  {
    	    antFound = true; //mark we found an ant
    	    distances = Integer.toString (antDistance);	//add north distance to string
    	    break; //stop searching
    	  }

      }
    if (antFound == false) //if no ant found in north
      distances = "0"; //add 0 at zeroeth index
    //reset conditions for next direction
    antDistance = 0;		
    antFound = false;

    //Check for ants in the east-------------------------------------------------------
    for (int j = (col + 1); j <= 9; j++)	//start searching from space to right of beetle up to last column
      {
    	antDistance++; //increase ant distance
    
    	if (grid[row][j] != null && ((grid[row][j]).getType ()) == 'A')	//if an ant is found
    	  {
    	    antFound = true; 
    	    distances = distances.concat (Integer.toString (antDistance));	//add distance to east ant to distances
    	    break; //stop searching 
    	  }

      }
    if (!antFound) //if no ant found
      distances = distances.concat ("0"); //add a zero at first index
    //reset conditions for next direction
    antDistance = 0;		
    antFound = false;
    
    //Check for ants in the south-------------------------------------------------------
    for (int i = (row + 1); i <= 9; i++)	//start searching from space below  beetle down to bottom row
      {
    	antDistance++; //increase ant distance counter
    
    	if (grid[i][col] != null && ((grid[i][col]).getType ()) == 'A')	//if an ant is found
    	  {
    	    antFound = true; 
    	    distances = distances.concat (Integer.toString (antDistance));	//add distance to south beetle to distances
    	    break; //stop searching
    	  }

      }
    if (!antFound) //if no ant found
      distances = distances.concat ("0");	//add a zero at second index
    //reset conditions for next direction
    antDistance = 0;		
    antFound = false;
    
    //West check-------------------------------------------------------
    for (int j = (col - 1); j >= 0; j--)	//start searching from space left of beetle to first column
      {
    	antDistance++; //increase ant distance counter
    
    	if (grid[row][j] != null && ((grid[row][j]).getType ()) == 'A')	//if an ant is found
    	  {
    	    antFound = true;
    	    distances = distances.concat (Integer.toString (antDistance));	//add distance to west beetle to distances
    	    break; //stop searching
    	  }

      } 
    if (!antFound)//if no ant found
      distances = distances.concat ("0"); //add a zero at third index
    
    return distances; //send modified distances string back!
  }
  public static String AntGetDistances (Creature[][]grid, int row, int col) //fills a string with distances to beetles in orthogonal direction
  {
    String distances = "temp"; //to hold distances
    int beetleDistance = 0;
    boolean beetleFound = false;

    //North check ------------------------------------------------------------
    for (int i = (row - 1); i >= 0; i--)	//search starts on space above ant, ends after checking elemnt in first row
      {
    	beetleDistance++; //increase beetle distance counter
    
    	if ( grid[i][col] != null && ((grid[i][col]).getType()) == 'B' )	//if a beetle is found
    	  {
    	    beetleFound = true;
    	    distances = Integer.toString (beetleDistance);	//save distance to beetle
    	    break;//stop search
    	  }

      }
    if (beetleFound == false) //if beetle not found
      distances = "0"; 
    //reset conditions for next direction
    beetleDistance = 0;		
    beetleFound = false;

    //East check-------------------------------------------------------
    for (int j = (col + 1); j <= 9; j++)	//search starts on space right of ant
      {
    	beetleDistance++; //increase beetle distance counter
    
    	if (grid[row][j] != null && ((grid[row][j]).getType ()) == 'B')	//if an beetle is found
    	  {
    	    beetleFound = true;
    	    distances = distances.concat (Integer.toString (beetleDistance));	//save distance to nearest beetle
    	    break;//stop search
    	  }
      }
    if (!beetleFound) //if no beetle found
      distances = distances.concat("0");
    //reset conditions for next direction
    beetleDistance = 0;		
    beetleFound = false;
    
    //South check-------------------------------------------------------
    for (int i = (row + 1); i <= 9; i++)	//search starts on space below ant
      {
    	beetleDistance++; //increase distance to beetle counter
    
    	if (grid[i][col] != null && ((grid[i][col]).getType ()) == 'B')	//if a beetle is found
    	  {
    	    beetleFound = true;
    	    distances = distances.concat (Integer.toString (beetleDistance));	//save distance to that beetle
    	    break;//stop search
    	  }
      }
    if (!beetleFound) //if no beetle found
      distances = distances.concat("0");
    //reset conditions for next direction
    beetleDistance = 0;		
    beetleFound = false;
    
    //West check-------------------------------------------------------
    for (int j = (col - 1); j >= 0; j--)	//search starts on space left of ant
      {
    	beetleDistance++;//increase beetle distance counter
    
    	if (grid[row][j] != null && ((grid[row][j]).getType ()) == 'B')	//if an beetle is found
    	  {
    	    beetleFound = true;
    	    distances = distances.concat (Integer.toString (beetleDistance));	//save distance to that beetle
    	    break;//stop search
    	  }

      }
    if (!beetleFound) //if no beetle found
      distances = distances.concat("0");
    
    return distances; //send back modified distances
  }
  public static String BreedSpaceCheck(Creature[][]grid, int row, int col) //returns a string that contains status of all orthogonal spaces
  {
      String spaces = ""; //to hold data on surrounding spaces
      
      //Check north
      if(row-1 >= 0 && grid[row-1][col] == null) //if space above creature is empty, mark with x
        spaces = spaces.concat("x");
      else if(row-1>=0 && grid[row-1][col]!= null)//if space above creature is occupied, mark with o
        spaces = spaces.concat("o");
      else if(row-1<0) //if no space at all in bounds
        spaces = spaces.concat("o"); //mark as occupied
      //Check east
      if(col+1 < 10 && grid[row][col+1] == null) //if space right of creature is empty
        spaces = spaces.concat("x");
      else if(col+1 < 10 && grid[row][col+1]!= null)//if space right of creature is occupied
        spaces = spaces.concat("o");
      else if(col+1 >= 10) //if no space at all in bounds
        spaces = spaces.concat("o"); //mark as occupied
      //Check south
      if(row+1 < 10 && grid[row+1][col] == null) //if space below creature is empty
        spaces = spaces.concat("x");
      else if(row+1< 10 && grid[row+1][col]!= null)//if space below creature is occupied
        spaces = spaces.concat("o");
      else if(row+1 >= 10) //if no space at all in bounds
        spaces = spaces.concat("o"); //mark as occupied        
      //Check west
      if(col-1 >= 0 && grid[row][col-1] == null) //if space left of creature is empty
        spaces = spaces.concat("x");
      else if(col-1 >= 0 && grid[row][col-1]!= null)//if space left of creature is occupied
        spaces = spaces.concat("o");
      else if(col-1 < 0) //if no space at all in bounds
        spaces = spaces.concat("o"); //mark as occupied
    
    return spaces; //return data on surrounding spaces
  }
  public static void PrintGrid(Creature[][] grid, int turn, char antChar, char beetleChar) //prints current state of grid
  {
      System.out.println("TURN " + turn); //print turn number
      
      for(int i = 0; i<10;i++) //print one space at a time
      {
          for(int j = 0; j<10; j++)
          {
              if(grid[i][j] == null) //print a space if no creature
              {
                  System.out.print(" ");
              }
              else if ( ((grid[i][j]).getType()) == 'A') //print ant if ant present
              {
                  System.out.print("" + antChar);
              }
              else if ( ((grid[i][j]).getType()) == 'B') //print beetle if beetle present
              {
                  System.out.print("" + beetleChar);
              }
          }
            System.out.println(); //print newline
            
      }
        System.out.println();  //print blank line between turns
      
  }
  public static String BeetleMoveTieBreaker(Creature[][] grid, int row, int col, String dist) //break any ties for beetle movement
  {
      if(dist.charAt(4) == 'd') //if moving to farthest edge (no orthogonal ants)
      {
          return dist; //do nothing- move will automatically move the beetle to the farthest edge following NESW priority in case of a tie
      }
      
        char lowest = ' '; //track lowest distance
        int winIndex = 0; //track direction of lowest distance
        boolean lowestFound = false; //track whether lowest distance has been found yet
        
        for(int i = 0; i < dist.length()-1; i++) //search first 4 letters of distances to find smallest distance
        {
            if(dist.charAt(i) != '0' && lowestFound == false) //find first nonzero distance to set as lowest ALWAYS EXECUTES ONCE
            {
                lowest = dist.charAt(i);
                lowestFound = true;
                winIndex = i;
                
            }
            if(lowestFound == true && dist.charAt(i) != '0' && dist.charAt(i) < lowest ) //after first lowest is found, check remaining to find true lowest 
            {
                lowest = dist.charAt(i);
                winIndex = i;
            }
        } 
        //lowest now holds lowest value and winIndex holds the first instance of it
        
        //search through distances again to detect ties
        boolean tie = false;
        for(int i = 0; i < dist.length()-1; i++)  //identify if there is a tie
        {
            if(dist.charAt(i) == lowest && i!= winIndex) //same element found
            {
                tie = true;
            }
        }
        if(!tie) //if no tie was detected
        {
            return dist; //do nothing
        }
        
        //if a tie was detected
        String neighbors = ""; //create string to hold number of ant neighbors in orthogonal directions
        for(int i = 0; i < dist.length()-1; i++) //fill neighbors string with number of neighbors around beetle
        {
            if(dist.charAt(i) != lowest) //no ant in this direction- we're uninterested
            {
                neighbors = neighbors.concat("x");
            }
            else if(dist.charAt(i) == lowest && i == winIndex) //first instance of tie element
            {
                int numNeighbors = 0;
                //hold ant position
                int antX = -1; 
                int antY = -1;
                
                if(i == 0) //if ant is at N, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row-d;
                    antY = col;

                }
                else if(i == 1)//if ant is at E, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row;
                    antY = col+d;
                }
                else if(i == 2)//if ant is at S, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row+d;
                    antY = col;
                }
                else if(i == 3)//if ant is at W, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row;
                    antY = col-d;
                }
                
                //step around ant, updating number of neighbors 
                if( antX-1 >=0 && grid[antX-1][antY] != null && (grid[antX-1][antY]).getType() == 'A')//ant found in N
                {numNeighbors++;}
                if( antX-1 >=0 && antY+1 < 10 && grid[antX-1][antY+1] != null && (grid[antX-1][antY+1]).getType() == 'A') //ant found in NE
                {numNeighbors++;}
                if( antY+1 <10 && grid[antX][antY+1] != null && (grid[antX][antY+1]).getType() == 'A')//ant found in E
                {numNeighbors++;}
                if( antX+1 <10 && antY+1 < 10 && grid[antX+1][antY+1] != null && (grid[antX+1][antY+1]).getType() == 'A')//ant found in SE
                {numNeighbors++;}
                if(antX+1 <10 && grid[antX+1][antY] != null && (grid[antX+1][antY]).getType() == 'A')//ant found in S
                {numNeighbors++;}
                if( antX+1 <10 && antY-1 >=0 && grid[antX+1][antY-1] != null && (grid[antX+1][antY-1]).getType() == 'A')//ant found in SW
                {numNeighbors++;}
                if(antY-1 >=0 && grid[antX][antY-1] != null && (grid[antX][antY-1]).getType() == 'A')//ant found in W
                {numNeighbors++;}
                if( antX-1 >=0 && antY-1 >= 0 && grid[antX-1][antY-1] != null && (grid[antX-1][antY-1]).getType() == 'A')//ant found in NW
                {numNeighbors++;}
                
                //update neighbors with this count
                neighbors = neighbors.concat(String.valueOf(numNeighbors));
                
                
            }
            else //following instances of tie element
            {
                int numNeighbors = 0;
                //hold ant position
                int antX = -1; 
                int antY = -1;
                if(i == 0) //if ant is at N, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row-d;
                    antY = col;

                }
                else if(i == 1)//if ant is at E, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row;
                    antY = col+d;
                }
                else if(i == 2)//if ant is at S, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row+d;
                    antY = col;
                }
                else if(i == 3)//if ant is at W, save its location
                {
                    int d = Character.getNumericValue(dist.charAt(i));  
                    antX = row;
                    antY = col-d;
                }
                

                //step around ant, updating number of neighbors 
                if( antX-1 >=0 && grid[antX-1][antY] != null && (grid[antX-1][antY]).getType() == 'A')//ant found in N
                {numNeighbors++;}
                if( antX-1 >=0 && antY+1 < 10 && grid[antX-1][antY+1] != null && (grid[antX-1][antY+1]).getType() == 'A') //ant found in NE
                {numNeighbors++;}
                if( antY+1 <10 && grid[antX][antY+1] != null && (grid[antX][antY+1]).getType() == 'A')//ant found in E
                {numNeighbors++;}
                if( antX+1 <10 && antY+1 < 10 && grid[antX+1][antY+1] != null && (grid[antX+1][antY+1]).getType() == 'A')//ant found in SE
                {numNeighbors++;}
                if(antX+1 <10 && grid[antX+1][antY] != null && (grid[antX+1][antY]).getType() == 'A')//ant found in S
                {numNeighbors++;}
                if( antX+1 <10 && antY-1 >=0 && grid[antX+1][antY-1] != null && (grid[antX+1][antY-1]).getType() == 'A')//ant found in SW
                {numNeighbors++;}
                if(antY-1 >=0 && grid[antX][antY-1] != null && (grid[antX][antY-1]).getType() == 'A')//ant found in W
                {numNeighbors++;}
                if( antX-1 >=0 && antY-1 >= 0 && grid[antX-1][antY-1] != null && (grid[antX-1][antY-1]).getType() == 'A')//ant found in NW
                {numNeighbors++;}
                    
                //update neighbors with this count
                neighbors = neighbors.concat(String.valueOf(numNeighbors));
            }
            
        }
        
        //find which direction has most number of neighbors
        char mostNeighbors = '0';
        boolean isTie = false;
        boolean found = false;
        int winI=0; //to indicate direction
        for(int i = 0; i < neighbors.length(); i++) //find highest number of neighbors
        {
            if(neighbors.charAt(i) != 'x' && found == false) //find first nonzero distance to set as highest ALWAYS EXECUTES ONCE
            {
                mostNeighbors = neighbors.charAt(i);
                found = true;
                winI = i;
                
            }
            if(found == true && neighbors.charAt(i) != 'x' && neighbors.charAt(i) > mostNeighbors ) //after first highest is found, check remaining to find true highest
            {
                mostNeighbors = neighbors.charAt(i);
                winI = i;
            }
            
        }
        isTie = false;
        for(int i = 0; i < neighbors.length(); i++)  //identify if there is a tie for this number of neighbors
        {
            if(neighbors.charAt(i) == mostNeighbors && i!= winI) //same element found
            {
                isTie = true;
            }
        }
        if(isTie) //if there is a tie of neighbors, break it and send dist back
        {
            dist = dist.substring(0,4); //chop off last character form dist
            
            if(winI == 0) //if most neighbors are north, update dist
                dist = dist.concat("n");
            if(winI == 1)//if most neighbors are east, update dist
                dist = dist.concat("e");
            if(winI == 2)//if most neighbors are south, update dist
                dist = dist.concat("s");
            if(winI == 3)//if most neighbors are west, update dist
                dist = dist.concat("w");
            return dist; //just return the string and it'll follow the NESW priority
        }
        if(winI == 0) //most neighbors North
        {
           dist = dist.substring(0,4);
        dist = dist.concat("n");
        }
        else if(winI == 1) //most neighbors east
        {
           dist = dist.substring(0,4);
           dist = dist.concat("e");
        }
        else if(winI == 2) //most neighbors south
        {
           dist = dist.substring(0,4);
        dist = dist.concat("s");
        }
        else if(winI == 3) //most neighbors west
        {
           dist = dist.substring(0,4);
        dist = dist.concat("w");
        }

        return dist; //send modified dist data back
  }
   public static String AntMoveTieBreaker(Creature[][] grid, int row, int col, String dist) 
  {
    char lowest = ' ';
    int winIndex = 0;
    boolean lowestFound = false;
    
    for(int i = 0; i < dist.length()-1; i++) //search through first 4 letters of dist to find smallest distance to beetle
    {
        if(dist.charAt(i) != '0' && lowestFound == false) //find first nonzero distance to set as lowest ALWAYS EXECUTES ONCE
        {
            lowest = dist.charAt(i);
            lowestFound = true;
            winIndex = i;
                
        }
        if(lowestFound == true && dist.charAt(i) != '0' && dist.charAt(i) < lowest ) //after first lowest is found, check remaining to find true lowest 
        {
            lowest = dist.charAt(i);
            winIndex = i;
        }
    } 
    //now lowest holds lowest value and winIndex holds first instance of it
    boolean tie = false;
    for(int i = 0; i < dist.length()-1; i++)  //search dist again to identify if there is a tie for smallest distance
    {
        if(dist.charAt(i) == lowest && i!= winIndex) //same element found
            tie = true;
    }
    if(!tie) //no tie found
    {
        return dist; //send dist back unmodified
    }
    //if there is a tie
    
    
    winIndex = -1; //reset winIndex
    for(int i = 0; i < dist.length()-1; i++) //search for clear pathways
    {
        //if any direction is 0, move there
        if(dist.charAt(i) == '0')
        {
            //clear pathway found!
            winIndex = i;
            break;//stop search
        }
    }
    
    if(winIndex >= 0) //if a clear pathway is available
    {
        if(winIndex == 0) //if north clear, add 'n'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("n");
        }
        else if(winIndex == 1)//if east clear, add 'e'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("e");
        }
        else if(winIndex == 2)//if south clear, add 's'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("s");
        }
        else if(winIndex == 3)//if west clear, add 'w'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("w");
        }
        return dist; //send dist back
    }
    
    //if ant surrounded 
    char highest = ' ';
    winIndex = 0;
    for(int i = 0; i < dist.length()-1; i++) //search for furthest distance to beetle
    {
        if(dist.charAt(i) > highest)
        {
            highest = dist.charAt(i);
            winIndex = i;
        }
    }
    if(winIndex >= 0) //if clear pathway available
    {
        if(winIndex == 0) //if north clear, add 'N'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("N");
        }
        else if(winIndex == 1)//if east clear, add 'E'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("E");
        }
        else if(winIndex == 2)//if south clear, add 'S'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("S");
        }
        else if(winIndex == 3)//if west clear, add 'W'
        {
            dist = dist.substring(0,4);
            dist = dist.concat("W");
        }
        
    }
    return dist; //return modified dist
  }
}
