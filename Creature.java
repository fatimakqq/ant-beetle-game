//Fatima Khalid
//fxk200007
abstract class Creature
{
    protected char type; //to hold type of creature
    
    //getters
    abstract public char getType(); 
    abstract public boolean getMovedStatus();
    abstract public boolean getIsOffspring();

    
    //Setters
    abstract public void setMovedStatus(boolean b);
    abstract public void setIsOffspring(boolean bool);

    //Move
    abstract public char Move(String dist);
    //Breed
    abstract public char Breed(String spots);

}