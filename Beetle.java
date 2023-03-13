//Fatima Khalid
//fxk200007
public class Beetle extends Creature{
    
    protected char type = 'B'; //beetle type
    protected boolean movedStatus = false;
    protected boolean isOffspring = false;
    protected int starveTimer = 5;

    // Getter
    public char getType() {return type;}
    public boolean getMovedStatus() {return movedStatus;}
    public boolean getIsOffspring(){return isOffspring;}
    public int getStarveTimer(){return starveTimer;}

    //Setter
    public void setMovedStatus(boolean b) {movedStatus = b;}
    public void setIsOffspring(boolean bool){isOffspring = bool;}
    public void setStarveTimer(int timer){starveTimer = timer;}
    
    @Override
    public char Move(String dist){
        
        //a tie was detected earlier - move to ant with most neighbors
        if(dist.charAt(4) == 'n') //most neighbors found north
        {
            return 'N';
        }
        else if(dist.charAt(4) == 'e')//most neighbors found east
        {
            return 'E';
        }
        else if(dist.charAt(4) == 's')//most neighbors found south
        {
            return 'S';
        }
        else if(dist.charAt(4) == 'w')//most neighbors found west
        {
            return 'W';
        }
        
        //no tie was detected- moving to closest ant
        else if(dist.charAt(4) == 'a') 
        {
            char lowest = ' ';
            int winIndex = 0;
            boolean lowestFound = false;
            for(int i = 0; i < dist.length()-1; i++) //search through first 4 letters of string to find smallest distance
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
            if(winIndex == 0) //smallest distance is north
                return 'N';
            if(winIndex == 1 )//smallest distance is east
                return 'E';
            if(winIndex == 2)//smallest distance is south
                return 'S';
            if(winIndex == 3)//smallest distance is west
                return 'W';
            
        }
        
        //no ants detected at all- moving towards farthest edge
        else if(dist.charAt(4) == 'd')
        {
            
            char highest = dist.charAt(0);
            int winIndex = 0;
            for(int i = 0; i < dist.length()-1; i++) //loop through string to find largest distance
            {
                if(dist.charAt(i) > highest)
                {
                    highest = dist.charAt(i);
                    winIndex = i;
                }
            }
            if(winIndex == 0) //farthest edge is north
                return 'N';
            if(winIndex == 1 )//farthest edge is east
                return 'E';
            if(winIndex == 2)//farthest edge is south
                return 'S';
            if(winIndex == 3)//farthest edge is west
                return 'W';
        }
        
        char c = 'c';
        return c;
    }
    
    //Breed
    @Override
    public char Breed(String spots)
    {
        if(spots.equals("oooo")) //if every spot is occupied
        {
            return 'X'; //do not breed
        }
        
        //if at least one spot is empty
        int winIndex = -1;

        for(int i = 0; i < spots.length(); i++) //loop through string to find first empty spot 
            {
                if(spots.charAt(i) == 'x') //find first empty x
                {
                    winIndex = i;
                    break;
                }
            }
        if(winIndex == 0) //empty spot found north- time to breed
            return 'N';
        if(winIndex == 1 )//empty spot found east- time to breed
            return 'E';
        if(winIndex == 2)//empty spot found south- time to breed
            return 'S';
        else //empty spot found west- time to breed
            return 'W';
    }
    
    public boolean Starve()
    {
        boolean d = false; //mark if beetle is dead
        if (starveTimer == 0) //if beetle's timer has run out
            d = true; //mark dead
        return d; //return status
    }
}