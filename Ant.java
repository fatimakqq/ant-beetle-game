//Fatima Khalid
//fxk200007
public class Ant extends Creature{
    
    protected char type = 'A'; //ant type
    protected boolean movedStatus = false;
    protected boolean isOffspring = false;
    
    // Getters
    public char getType() {return type;}
    public boolean getMovedStatus() {return movedStatus;}
    public boolean getIsOffspring(){return isOffspring;}
    
    //Setters
    public void setMovedStatus(boolean b) {movedStatus = b;}
   public void setIsOffspring(boolean bool) {isOffspring = bool;}
    
    //Move    
    @Override
    public char Move(String dist)
    {
                
        if(dist.charAt(4) == 'x') //if not moving (no beetles in orthogonal)
        {
            return 'X'; //move in no direction
        }
        
        int winIndex = 0;
        if(dist.charAt(4) == 'm') //running away from nearest beetle
        {
            char lowest = ' ';
            
            boolean lowestFound = false;
            for(int i = 0; i < dist.length()-1; i++) //search for nearest beetle
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
            if(winIndex == 0) //if nearest beetle north, run from it 
            return 'N';
            if(winIndex == 1 )//if nearest beetle east, run from it 
                return 'E';
            if(winIndex == 2)//if nearest beetle south, run from it 
                return 'S';
            else //if nearest beetle west, run from it 
                return 'W';
        }
        
        //if tie was detected earlier
        if(dist.charAt(4) == 'n' || dist.charAt(4) == 'N') //move north
        {
            return 'N';
        }
        else if(dist.charAt(4) == 'e' || dist.charAt(4) == 'E') //move east
        {
            return 'E';
        }
        else if(dist.charAt(4) == 's' || dist.charAt(4) == 'S') //move south
        {
            return 'S';
        }
        else //move west
        return 'W';
    }
    
    //Breed
    @Override
    public char Breed(String spots)
    {
        if(spots.equals("oooo")) //if every spot is occupied, do not breed
        {
            return 'X';
        }
        
        //if at least one spot is empty
        int winIndex = -1;

        for(int i = 0; i < spots.length(); i++) //search string to find first empty spot 
            {
                if(spots.charAt(i) == 'x') //find first empty x
                {
                    winIndex = i;
                    break;
                }
            }
        if(winIndex == 0) //if first empty spot north, breed there
            return 'N';
        if(winIndex == 1 )//if first empty spot east, breed there
            return 'E';
        if(winIndex == 2)//if first empty spot south, breed there
            return 'S';
        else //if first empty spot west, breed there
            return 'W';
    }
}