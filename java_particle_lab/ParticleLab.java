/*************************************************************************
 *
 *  
 *  Author: Oscar Gastelum 
 *  Last Revised: 12/08/2021
 * 
 * ************************************************************************  
 * 
 *  Program description: In this project, you'll create what is called a 
 *  falling sand program. The software resembles a paint program, except 
 *  that the user is painting particles into the world. The software 
 *  simulates the physical behavior of those particles, which may move 
 *  (perhaps falling like grains of sand), change, generate, disappear, 
 *  interact, etc.
 * 
 *-------------------------------------------------------------------------
 *  
 *  CIS131 ParticleLab Project 
 * 
 * 
 *************************************************************************/

 
import java.awt.Color;
import java.util.*;

public class ParticleLab{
    static final int NBR_ROWS  = 200;  //180   |||
    static final int NBR_COLS  = 180;  //180   ---
    static final int CELL_SIZE = 800;  //800
    
    //static final String FILE_NAME     = "ParticleLabFile.txt";         //This is the name of the input file.
    static final String FILE_NAME     = "ParticleLabFileGreatPainting.txt";           //This is the saved file. Uncomment to load next time program is ran. 

    static final String NEW_FILE_NAME = "ParticleLabFileGreatPainting.txt";  //This is the name of the file you are saving.

    //add constants for particle types here
    public static final int EMPTY     = 0;
    public static final int GRAVITY = 1; 
    public static final int METAL     = 2;
    public static final int SAND = 3;
    public static final int WATER = 4; 
    public static final int OIL = 5; 
    public static final int GENERATOR = 6;
    public static final int DESTRUCTOR = 7;
    public static final int VAPOR = 8;
    public static final int FIRE = 9; 
    public static final int WOOD = 10; 

    public static final int YELLOW_FIRE = 20; 
    public static final int ORANGE_FIRE = 21; 
    
    
    public static final int RESET = 11; 
    public static final int CLEAR = 12; 
    public static final int SAVEFILE  = 13;

    public static final int BUTTONS = 14; 

    public static final int GRAVITY_OFF = 1; //normal 
    public static final int GRAVITY_ON = -1; //reversed 

    public static final int MIN_VALUE = 0;
    public static final int G_WRAP = -1; 

    public static final int PROB_GEN = 100;
    public static final int PROB_VAP = 35;

    //public static final int FALL_RATE = 5; 

    //do not add any more global fields
    private int row = 0;
    private int col = 0;

    private int[][] particleGrid;
    private LabDisplay display;
            
    //---------------------------------------------------------------------------------------------------------
    public static void main(String[] args){
        System.out.println("================= Starting Program =================");
        System.out.println("ROWS: " + NBR_ROWS + "\nCOLS: " + NBR_COLS + "\nCELL_SIZE: " + CELL_SIZE + "\n");
        
        ParticleLab lab = new ParticleLab(NBR_ROWS, NBR_COLS);  //creates the object
        lab.run();
        
    }//end main
    
    //---------------------------------------------------------------------------------------------------------
    






    
    /** 
    SandLab constructor - ran when the above lab object is created. Includes colors and tools to choose from.

    @param numRows = The number of rows being used for the grid. 
    @param numCols = The number of columns being used for the grid. 
    **/
    public ParticleLab(int numRows, int numCols){
        
        String[] names = new String[BUTTONS]; 
        
        //Names of tools 
        names[EMPTY]      = "Empty     ";
        names[GRAVITY]    = "Gravity   "; 
        names[METAL]      = "Metal     ";
        names[SAND]       = "Sand      "; 
        names[WATER]      = "Water     "; 
        names[OIL]        = "Oil       "; 
        names[GENERATOR]  = "Generator ";
        names[DESTRUCTOR] = "Destructor";
        names[VAPOR]      = "Vapor     ";
        names[FIRE]       = "Fire      ";
        names[WOOD]       = "Wood      ";
        names[CLEAR]      = "Clear     "; 
        names[RESET]      = "Reset     "; 
        names[SAVEFILE]   = "SaveFile  "; 
         
        display      = new LabDisplay("SandLab", numRows, numCols, names);  //uses the LabDisplay.class file 
        particleGrid = new int[numRows][numCols];
        

        if (FILE_NAME != "") {  
            System.out.println("Attempting to load: " + FILE_NAME);
            particleGrid = ParticleLabFiles.readFile(FILE_NAME);   
        } 

    }//end ParticleLab

    
    //---------------------------------------------------------------------------------------------------------
    
    
    /** 
    Updating the particle gird based on the color/ tool selected 
    called when the user clicks on a location using the given tool
    If tool == x set grid to that tools color 
    
    @param row: the row number being clicked.
    @param column: the column number being clicked.
    @param tool: the tool selected by user.
    **/
    private void locationClicked(int row, int col, int tool){  
        

        if(tool == EMPTY){
            particleGrid[row][col] = EMPTY;
        }

        if(tool == GRAVITY){
            toggleGravity(row, col); 
            return; 
        }
        //if the tool is qual to sand and the particle is not metal 
        if(tool == SAND && !particleCheck(row, col, METAL)){
            particleGrid[row][col] = SAND; 
        }   

        if(tool == METAL){
            particleGrid[row][col] = METAL;  
        }

        if(tool == WATER && !particleCheck(row, col, METAL)){
            particleGrid[row][col] = WATER;
        }

        if(tool == OIL && !particleCheck(row, col, METAL)){
            particleGrid[row][col] = OIL;
        }

        if(tool == GENERATOR){
            particleGrid[row][col]  = GENERATOR;
        }

        if(tool == DESTRUCTOR){
            particleGrid[row][col]  = DESTRUCTOR;

        }
        if(tool == VAPOR){
            particleGrid[row][col] = VAPOR;
        }

        if(tool == FIRE){
            particleGrid[row][col] = FIRE;
        }

        if(tool == WOOD){
            particleGrid[row][col] = WOOD;
        }

        if(tool ==  CLEAR){
            particleGrid[row][col] = CLEAR;
        }
        if(tool == RESET){
            particleGrid = ParticleLabFiles.readFile(NEW_FILE_NAME);  
        }

        if (tool == SAVEFILE) {
            ParticleLabFiles.writeFile(particleGrid, NEW_FILE_NAME);  
        }

    }//end locationClicked
    
    //---------------------------------------------------------------------------------------------------------
    
    /* 
    Examines each element of the 2D particleGrid and paints a color onto the display
    */
    public void updateDisplay(){
        
        //remove the below code when ready
        //---------------------------------------
        //  Provided Examples 
        //---------------------------------------
        //Color purple = new Color (192, 0, 255);              //an example of how to create a new color
        //display.setColor(0,0,purple);                        //an example of how to display the new color
        //
        //display.setColor(0,NBR_COLS-1,Color.yellow);         //an example of how to display a named Java color
        //display.setColor(NBR_ROWS-1,0,Color.green);          //an example of how to display a named Java color
        //display.setColor(NBR_ROWS-1,NBR_COLS-1,Color.red);   //an example of how to display a named Java color
        //---------------------------------------

        //Creation of new colors using their RGB color code as parameters
        Color brown = new Color(150,75,0);
        //Color darkerYellow = new Color(249,209,0,205);

        //set the colors 
        for(int r = 0; r < NBR_ROWS ; r++){
            
            for(int c = particleGrid[r].length -1; c>=0 ; c--){
                
                int grid = particleGrid[r][c];
                
                if(grid == METAL){
                    display.setColor(r, c, Color.LIGHT_GRAY);

                }
                else if(grid == SAND){ 
                    display.setColor(r, c, Color.yellow);


                } 
                else if(grid == WATER){
                    display.setColor(r, c, Color.blue);

                }
                else if(grid == OIL){
                    display.setColor(r, c, Color.orange);

                }else if(grid == GENERATOR){
                    display.setColor(r, c, Color.pink);

                }else if (grid == DESTRUCTOR){
                    display.setColor(r, c, Color.cyan);
                }
                else if(grid == EMPTY){
                    display.setColor(r, c, Color.black);

                }
                else if(grid == VAPOR){
                    display.setColor(r, c, Color.white);

                }else if(grid == FIRE){
                    display.setColor(r, c, Color.red);

                }else if (grid == WOOD){
                    display.setColor(r, c, brown);

                }else if (grid == YELLOW_FIRE){
                    display.setColor(r, c, Color.yellow);    
                    
                }else if (grid == ORANGE_FIRE){
                    display.setColor(r, c, Color.orange);                

                }


            }//end

        }//end 


        
        
    }//end updateDisplay
    
    
    //---------------------------------------------------------------------------------------------------------
    

    /* 
    Causes one random particle to fall 1 space below. 
    Called repeatedly.
    
    */
    public void step(){
    
        //set the min value to zero. 
        //get a random selection for the row value.
        //get a random selection for the column value.
        row = getRandomNumber(MIN_VALUE , NBR_ROWS ); 
        col = getRandomNumber(MIN_VALUE , NBR_COLS ); 
        /* 
        Change in gravity depending on direction.
        adding 1 will move the particle down the array to greater numeric values
        subtracting 1 wil move the particle up the array to lower numeric values.
        
        ex. 
            1 2 3 4
            2
            3

        */

        //a switch statement to decide which tool has been selected. 
        switch (particleGrid[row][col]) {
            
            case SAND: 
            /* 
            processSand() will let the sand behave as normal, with normal gravity. When lending on the ground will build up as normal sand. 
            processSandWrapYAxis() will wrap the sand across the Y-axis of the grid. AKA up to down or down to up. 
            processSandWrapXAxis() will wrap the sand across the X-axis of the grid. AKA left to right or right to left. 
            */  
            processSand(row, col, SAND);
            //processSandWrapYAxis(row, col); 
            //processSandWrapXAxis(row, col); 

                break; 

            case WATER:
                processLiquid(row, col, WATER, getGravity());

                break;

            case OIL: 
                processLiquid(row, col, OIL, getGravity());
                break; 
            //default case will break
            case GENERATOR:
                processGenerator(row, col, PROB_GEN);
                break;
            case DESTRUCTOR:
                processDestructor(row, col);
                break;
            case VAPOR:
                processGas(row, col, VAPOR, PROB_VAP);
                break;
            case CLEAR:
                processClear();
                break;

            case WOOD:
                processWood(row, col);
            
            default:
                break; 
            case FIRE:              
                processGas(row, col, FIRE, 6);
                break;

            case YELLOW_FIRE:
                processGas(row, col, YELLOW_FIRE, 10);
                break;

            case ORANGE_FIRE:
                processGas(row, col, ORANGE_FIRE, 10);
                break;

        }//end switch
      
        


    }// end step 
    
    //------------------------------------------------------------------------------------------------------




    //Will create wood which burns when in contact with fire
    public void processWood(int row, int col){

        //Check sides 
        boolean aboveFire = isParticle(row -1, col , FIRE);
        boolean belowFire = isParticle(row +1, col , FIRE);
        boolean rightFire = isParticle(row, col + 1, FIRE);
        boolean leftFire = isParticle(row, col - 1 , FIRE);


        //Probabilities
        boolean probablyTrue   = getRandomNumber(0, 3) == 1;
        boolean probablyTrueTwo   = getRandomNumber(0, 3) == 2;
        boolean probablyTrueThree   = getRandomNumber(0, 3) == 3;

     
        //If there is a fire around it it will change particle to fire. 
        if( aboveFire || belowFire || rightFire || leftFire && withinBounds(row, col)){
        
            if(probablyTrue){

                if(probablyTrueTwo){
                    if(probablyTrueThree){
                        setParticle(row, col, ORANGE_FIRE);

                    }else{
                        setParticle(row, col, YELLOW_FIRE);

                    }


                }else{
                    setParticle(row, col, VAPOR);

                }
            }else{
                setParticle(row, col, FIRE);
                
            }



        }



    }




    //will Clear the grid to empty 
    public void processClear(){
        
        //will iterate throw all rows and columns and Clear them to EMPTY
        for(int r = 0 ; r < NBR_ROWS; r++){
            for(int c = 0; c < NBR_COLS; c++){
                particleGrid[r][c] = EMPTY;
            }

        }


    }//end processClear




    /**
     * Will produce a gas with a specified tool color 
     * @param row the current row of particleGrid
     * @param col the current column of particleGrid
     * @param gas the gas color to produce
     */
    public void processGas(int row, int col, int gas, int prob){
        
        //true or false based on probability set 
        //true or false based on probability set 
        boolean probablyTrue = getRandomNumber(0, prob) == 1;


        //set tge gas value to a temp variable in order to change later
        int tempGas = gas; 

        //changing to EMPTY based on given probability 
        if(probablyTrue){
            tempGas = EMPTY;
        }

        //behaves similar to a liquid but backwards 
        processLiquid(row , col, tempGas, getGravity() * - 1);


        //if it reaches the end or probablyTrue will disappear
        if(row == 0 || row == NBR_ROWS -1){
            setParticle(row , col, EMPTY);

        }




    }//end processGas




    /**
     * Will process the behavior for destructor particle which destroys any particle 
     * above it and produces the vapor particle 
     * @param row the current row for particleGrid
     * @param col the current column for particleGrid
     */
    public void processDestructor(int row, int col){

        //get the value of the particle above the destructor 
        int aboveParticle = particleGrid[row - 1 * getGravity()][col];

        //if its not empty and not equal to a destructor, destroy and produce vapor 
        if( aboveParticle != EMPTY && aboveParticle != DESTRUCTOR){
            setParticle(row - 1 * getGravity(), col, VAPOR);

        }
                 

    }//end processDestructor






    /**
     * Will generate the particle above or below based on the state of gravity.  
     * @param row the current row in the particleGrid
     * @param col the current column in the particleGrid
     * @param probability an int number representing percentage probability of the particle
     * below being generated. 
     */
    public void processGenerator(int row, int col, int probability){

        //check for generator particle above the current location 
        boolean genAbove = isParticle(row - 1  , col, GENERATOR);

        //true or false based on probability set 
        boolean probablyTrue = getRandomNumber(0, probability) == 1;

        boolean genBelow = isParticle(row + 1 , col, GENERATOR);
        
        //the particle above the current location 
        int above = particleGrid[row-1  * getGravity()][col];
        
        /*Based on probability will generate the same particle above the generator. 
        If theres a group of generators together, they act as one whole and pass the 
        particle that is on top to the last empty spot below the generator. 
        Takes into account weather gravity is on or off
        */
        if(probablyTrue){

            if(!genAbove && !genBelow){
                setParticle(row + 1 * getGravity(), col, above);

            }
        
            //if gravity is on 
            if(!genAbove && genBelow && particleGrid[row-1 * getGravity()][col] != EMPTY &&  getGravity() == GRAVITY_OFF){
                
                //set the particle on the top of generator to temp value 
                int temp = particleGrid[row-1 * getGravity()][col];

                //for loop to find the next generator with an empty particle beneath it
                for(int i = row; i < NBR_ROWS; i++){
                    
                    //if the index for the particle is empty and the top one is a generator set the temp particle 
                    if(particleGrid[i][col] == EMPTY && particleGrid[i-1][col] == GENERATOR){

                        setParticle(i  , col, temp);
                        break;
                        
                    }
                    
                    
                }


            }

            //Same as above, except for gravity on and in reverse order
            if(genAbove && !genBelow && particleGrid[row-1 ][col] != EMPTY &&  getGravity() == GRAVITY_ON){
                
                int temp = particleGrid[row + 1 ][col];
                
                for(int i = row; i > 0 ; i--){
                    
                    if(particleGrid[i][col] == EMPTY && particleGrid[i+1][col] == GENERATOR){
                       
                        setParticle(i   , col, temp);
                        break;
                        
                    }
                    
                    
                }

            }
            

            
        }

        
        


    }//end processGenerator







    
    /**
     * Will process the particle to look and behave like real life water. 
     * @param row the current row in the particleGrid
     * @param col the current column in the particleGrid
     * @param element the element being assigned to the behavior
     * @param gStatus is the status of gravity for the liquid
     */
    public void processLiquid(int row, int col, int element, int gStatus ){

        //booleans to check weather an particle area is empty or not.
        boolean down =  isParticle(row + gStatus, col, EMPTY);
        boolean left =  isParticle(row, col - 1      , EMPTY);
        boolean right = isParticle(row, col + 1      , EMPTY); 

        //Water conditions 
        boolean waterUp = isParticle(row + gStatus*-1, col, WATER);
        boolean waterLeft  = isParticle(row, col - 1, WATER);
        boolean waterRight = isParticle(row, col + 1, WATER);


        //if left and right conditions 
        if(left && right){
            left  = randomBoolean();
            right = randomBoolean(); 

        }

        if(down){
            setParticle(row + gStatus, col,    element); 
            
        }
        else if (left){
            setParticle(row , col - 1, element); 

        } 
        else if (right){
            setParticle(row , col + 1, element); 
        }
        
        if(down || left || right){
            setParticle(row, col, EMPTY); 
        }


        if(particleGrid[row][col] == OIL){

            if(waterLeft && waterRight){
                waterLeft  = randomBoolean();
                waterRight = randomBoolean(); 
    
            }
    
            if(waterUp){
                setParticle(row + gStatus*-1, col,    element); 
                
                
            }
            else if (waterLeft){
                setParticle(row , col - 1, element); 
    
            } 
            else if (waterRight){
                setParticle(row , col + 1, element); 
            }
            
            if(waterUp || waterLeft || waterRight && particleGrid[row][col] == OIL){
                setParticle(row, col, WATER); 
            }
        }


    }//end processLiquid



        

    //------------------------------------------------------------------------------------------------------


    /** 
    will process the sand behavior making it look and act like real life sand. 

    @param row is the current row in particleGrid
    @param col is the current col in particleGrid
    **/
    public void processSand(int row, int col, int element ){
        

        //booleans to check weather an particle area is empty or not.
        boolean down =  isParticle(row + getGravity(), col    , EMPTY);
        boolean left =  isParticle(row + getGravity(), col - 1, EMPTY);
        boolean right = isParticle(row + getGravity(), col + 1, EMPTY); 
      


        
        //if left and right conditions 
        if(left && right){
            left  = randomBoolean();
            right = randomBoolean(); 
         
        }

        if(down){
            setParticle(row + getGravity(), col,    element); 
           
        //if the particle bellow the current location is water. 
        //swap locations. Sand will sink in water behavior. 
        }
        else if(row < NBR_ROWS-1 && row > 0 && particleGrid[row + getGravity()][col] == WATER){
            particleGrid[row][col] = WATER;
            particleGrid[row + getGravity()][col] = SAND;
             
        } 
        else if (left){
            setParticle(row + getGravity(), col - 1, element); 

        } 
        else if (right){
            setParticle(row + getGravity(), col + 1, element); 

        }
        
        if(down || left || right){
            setParticle(row, col, EMPTY); 

        }

        

    }// end processSand
    
    
    //---------------------------------------------------------------------------------------------------------
    

    /**
     * will process the sand, behaving as normal sand but will also wrap across the y axis of the grid. AKA up or down. 
     * 
     * @param row
     * @param col
     */
    public void processSandWrapYAxis(int row, int col){
       
        //will reset the column once it reaches the boarders in order to wrap.  
        int newRow = wrapYAxis(row); 
    
        //checking to see if the location is empty 
        boolean down =  isParticle(newRow + getGravity(), col , EMPTY);
        boolean left =  isParticle(row + getGravity(), col - 1, EMPTY);
        boolean right = isParticle(row + getGravity(), col + 1, EMPTY); 
    
        
        if(left && right){

            left  = randomBoolean();
            right = randomBoolean(); 
         
        }

        if(down){
            setParticle(newRow + getGravity(), col,    SAND); 
            
        } 
        else if (left){
            setParticle(row + getGravity(), col - 1, SAND); 

        } 
        else if (right){
            setParticle(row + getGravity(), col + 1, SAND); 

        }
        
        if(down || left || right){
            setParticle(row, col, EMPTY); 

        }

      

    }// end processSandWrapYAxis

    
    //---------------------------------------------------------------------------------------------------------
    

    /**
     * Will make the sand behave like real sand while wrapping across the X-axis of the grip. AKA, right or left. 
     * @param row
     * @param col
    */

    public void processSandWrapXAxis(int row, int col){
        //will reset the particle in order to make it wrap. 
        int newCol = wrapXAxis(col); 
    
        //checking if locations are empty. 
        boolean right = isParticle(row, newCol + getGravity(), EMPTY); 
        boolean down = isParticle(row + 1, col + getGravity(), EMPTY); 
        boolean up = isParticle(row -1, col + getGravity(), EMPTY);  


        if(up && down){
            up = randomBoolean();
            down = randomBoolean();
        }


        if(right){
            setParticle(row, newCol + getGravity(), SAND);

        }
        else if(down){
            setParticle(row + 1, newCol + getGravity(), SAND);

        }
        else if(up){
            setParticle(row - 1, newCol + getGravity(), SAND);

        }

        
        if(right || down || up){
            setParticle(row , col, EMPTY);
        }

      

    }//end processSandWrapYAxis
    

    

    //---------------------------------------------------------------------------------------------------------


    //will get the gravity status value. 
    public int getGravity(){

        for (int r = 0; r < particleGrid.length; r++) {
            for (int c = 0; c < particleGrid[0].length; c++) {

                if (particleGrid[r][c] == GRAVITY_OFF){
                    return GRAVITY_ON;
                }

            }
        }

        return GRAVITY_OFF;

    }// end getGravity

    //---------------------------------------------------------------------------------------------------------


    /**
     * Toggles gravity between on and off by creating a GRAVITY_OFF pixel
     * on the board or removing a GRAVITY_OFF pixel if one already exists
     * @param currentRow the row that was clicked on
     * @param currentCol the column that was clicked on
     */
    public void toggleGravity(int currentRow, int currentCol){

        for (int r = 0; r < particleGrid.length; r++) {
            for (int c = 0; c < particleGrid[0].length; c++) {

                if (particleGrid[r][c] == GRAVITY_OFF) {
                    particleGrid[r][c] = EMPTY;
                    return;
                }
                
            }
        }

        particleGrid[currentRow][currentCol] = GRAVITY_OFF;

    }//end toggleGravity

    //---------------------------------------------------------------------------------------------------------
    

    /** 
    will wrap the particle to the left or to the right depending on gravity status 
    if gravity down, or off = will go right, else left 
    @param row =  the current row location for particleGrid[][]
    **/
    public int wrapXAxis(int col){
        
        //if the movement across the x-axis is greater the the number of rows, set the row to 0 (Start)
        if(col + getGravity() >= NBR_COLS -1) {
            return 0; 
        //else, set it to whatever the current row is. 
        }

        //if the number of rows is less than or equal to zero, set it to the end of the number of rows.
        if(col + getGravity()   <= 0 ){
            return NBR_COLS -1; 

        }    
        
        //if none of the conditions above is true, set the row to the current row. 
        return col; 
        
    

    }//end wrapXAxis
   

    //---------------------------------------------------------------------------------------------------------
    
    /**
     * will set the values to the beginning or end of the grid, depending on weather the gravity is on
     * or off. Will make it wrap across the Y-axis. AKA, up or down. 
     * 
     * @param row the current row on the particleGrid
     */
    public int wrapYAxis(int row){
        //if the movement across the Y-axis is greater the the number of col, set the col to 0 (Start)
        if(row + getGravity()  >= NBR_ROWS-1) {
            return 0; 
        //else, set it to whatever the current col is. 
        }

        //if the number of col is less than or equal to zero, set it to the end of the number of col.
        if(row + getGravity()  <= 0 ){
            return NBR_ROWS -1; 
        }    
        
        //if none of the conditions above is true, set the cow to the current row. 
        return row;

    }// end wrapYAxis 
    

    //---------------------------------------------------------------------------------------------------------



    /**
     * Will check to make sure the particle is withing bounds. Will protect from out of bound errors. 
     * 
     * @param row the current row on particleGrid
     * @param col the current column on particleGrid
     */
    public static boolean withinBounds(int row, int col){
        return ( row >= 0 && row < NBR_ROWS  && col >= 0 && col < NBR_COLS);
    
    }//end withinRange 

    //---------------------------------------------------------------------------------------------------------
    
    /**
     * will check to see if a location on the grid is empty. 
     * 
     * 
     * @param x the current row 
     * @param y the current col
     */
    public boolean isParticle(int x, int y,int element){
        return (withinBounds(x, y) && particleGrid[x][y] == element);
    }//end isParticle
    


    //---------------------------------------------------------------------------------------------------------

    /**
     * Will set a location on the grid to a tool. 
     * 
     * @param row the current row 
     * @param col the current col 
     * @param tool the tool selection 
     */
    public  void setParticle(int row, int col, int tool){
        particleGrid[row][col] = tool; 

    }// end setParticle 

    //---------------------------------------------------------------------------------------------------------

    //will set a random choice of 0 or 1. Therefore random true or false. 
    public boolean randomBoolean(){
        int rand = getRandomNumber(-1,1); 
        return rand != 0; 
    }// end randomBoolean

    //---------------------------------------------------------------------------------------------------------

    /**
     * WIll check to see if location contains a particle type specified. Returns true or false. 
     * @param row the current row
     * @param col the current column
     * @param particle the particle to check
     * @return
     */
    public boolean particleCheck(int row, int col, int particle){
        return particleGrid[row][col] == particle; 
    }// end particleCheck 











    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    
    ////////////////////////////////////////////////////
    //DO NOT modify anything below here!!! /////////////
    ////////////////////////////////////////////////////
    
    public void run(){
        while (true){
            for (int i = 0; i < display.getSpeed(); i++){
                step();
            }
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse   
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }

    public int getRandomNumber (int low, int high){
        return (int)(Math.random() * (high - low)) + low;
    }
    
    public static int getNbrRows() {return NBR_ROWS;}
    public static int getNbrCols() {return NBR_COLS;}
    public static int getCellSize(){return CELL_SIZE;}
}

