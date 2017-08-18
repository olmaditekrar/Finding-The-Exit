import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
//This class contains pair of variables ( x and y) . I created it so we can be more clear about coordinates when typing.
class Point{   
    int x,y;
    Point (String strPoint){
        this.x = Character.getNumericValue(strPoint.charAt(0) );
        this.y = Character.getNumericValue(strPoint.charAt(1) );
    }
}

//This class contains a path informations( a string to describe the route and coin that we have ) . It is basically helping me when typing and getting information about the path that I choose.
class Path {
    String strPath ;
    int coin ;
    Path(){
        coin = 0 ; 
        strPath = "" ;
    }
}

//MAIN CLASS
public class Main {
    //I choose my OUTPUT file for my own computer , I hope it's okay .
    private static final String OUTPUTFILE = "/Users/macbook/Desktop/Projects/Java Projects/AOA3150116825/output.txt";
    //printMatrix function is reading from Matrix and change the Matrix values in to the "Visual Path Steps"
    //Also later , it writes to OUTPUTFILE with correct format.
        public static void printMatrix (String Matrix[][],int coloumnSize,Path maxPath) throws IOException{
            //I used BufferedWriter and FileWriter , more stable than private libraries.
            	BufferedWriter bw = null;
		FileWriter fw = null;
		try {
                        //We try to execute ;
                        //Firstly we write coin information from maxPath variable that we have (Shows maximum coin path later will explain much better.)
                        String content= maxPath.coin + "\n";
                        //Than we parse the string that shows us the maximum path (PS: All the tabs are 1 character , that's why I incremented by 3 .).
                        //And we simply flag the variables with P that we choose to select to go with in Matrix.
                        for (int i = 0 ; i < maxPath.strPath.length() ; i+=3){
                            Matrix[maxPath.strPath.charAt(i) - '0' ][ maxPath.strPath.charAt(i+1)- '0'] = "P" ;
                        } 
                        //We change the Matrix and we have to show the content variable Matrix with right format.
                        for (String[] Matrix1 : Matrix) {
                            for (int k = 0; k < coloumnSize; k++) {
                               content +=Matrix1[k] + "\t" ;
                            }
                            content += "\n" ;
                        }
                        //Writing in OUTPUTFILE
			fw = new FileWriter(OUTPUTFILE);
			bw = new BufferedWriter(fw);
			bw.write(content);
                        //Feedback
			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                    
		}
        }
        //This function's main job is parse the stringMatrix (the string that we read from input.txt earlier)
        //and convert to 2d array or matrix so we can work on it easily.
        public static void convertToMatrix(String stringMatrix , String Matrix [][],int sizeOfColumn){
            //j variable is iteration variable for stringMatrix variable , we increment 2 for positive integers 
            //or 3 for the negative integers (because "-2" has 2 char characters) 
            int j = 0 ;
            for (int i = 0 ; i < Matrix.length ; i++){
                for (int k = 0 ; k < sizeOfColumn ; k++){
                    //If current index in stringMatrix is an integer (we start with 4 for the index because of dimension numbers on top of the input file)
                    if( Character.isDigit((stringMatrix.charAt(j+4))) ){
                        //We pass the value to current Matrix index's value and increment with 2. Because if it is a digit , it is positive for sure.
                       Matrix[i][k] = String.valueOf(stringMatrix.charAt(j+4)) ;
                       j+=2;
                    }
                    else if ("-".equals(String.valueOf(stringMatrix.charAt(j+4)))){
                        //We pass - and number value to current index's of Matrix and increment with 3 .
                        Matrix[i][k] = String.valueOf(stringMatrix.charAt(j+4)) + "" + String.valueOf(stringMatrix.charAt(j+5)) ;
                        j+=3;
                    }else{
                        //Else is when we found "X" in the input . We pass it to Matrix.
                        Matrix[i][k] = String.valueOf(stringMatrix.charAt(j+4));
                        j+= 2;
                    }                    
                }
             }
        }
        //Main Function 
	public static void main(String[] args) throws IOException {
            Scanner read = new Scanner(System.in) ;
            String adressFromIdeCmd = read.nextLine() ;
            //I decleare INPUTFILE in here just because we can type the path from command line.
            String INPUTFILE ;
            //I read the explanation but when I search "command line argument " they always says "args" . So to make sure I implemented 3 ways.
            if (args.length != 0){
                INPUTFILE = args[0];
            }else if (adressFromIdeCmd.length() > 4) {
                INPUTFILE = adressFromIdeCmd ;

            }else{
                INPUTFILE = "/Users/macbook/Desktop/Projects/Java Projects/AOA3150116825/input.txt";
            }
                String inputFile = "" ;
		try (BufferedReader br = new BufferedReader(new FileReader(INPUTFILE))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
                            //If we have no troubles with reading the file , we pass the entire line to the inputFile variable and go to the next line.
				inputFile = inputFile + sCurrentLine ;
                                inputFile = inputFile + "\n" ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
                //We create dimensions variable to know what is the Matrix size will be. Especially size of coloumn.
                Point dimensions = new Point(inputFile.charAt(0) + "" +  inputFile.charAt(2) ) ;
                //robotPath variable is string matrix , contains all the path variables in the input
                String [][]robotPath = new String[dimensions.x][dimensions.y] ;
                //We convert the inputFile into the matrix and pass it to robotPath variable.
                convertToMatrix(inputFile, robotPath,dimensions.y);
                //For start we declare startingPoint variable = (0,0)
                Point startingPoint = new Point("00");
                //currentPath variable will shows us current path informations (coins and a string to show our route.)
                Path currentPath = new Path();
                //maxPath variable will show us maximum coin path informations (coins and a string to show our route.)
                Path maxPath = new Path();
                //We change the last element of matrix into the value and 3 zeros at the end . So we can see when we at the finish.
                robotPath[dimensions.x-1][dimensions.y-1] += "000";
                //success variable is a lazy variable . In recursion in the function , returning value will help us to see where we stuck or we can go further.
                //We execute the matrixMovement function with starting points ,robotPath , current and maximum path variables.
                //This function will recursively go all the possible routes and find the one whix is maximum coin one.
                int success = matrixMovement(dimensions.y,startingPoint,robotPath,currentPath,maxPath);
                //Lastly we change the last element into the normal way . We finished and we don't need a flag to show us.
                robotPath[dimensions.x-1][dimensions.y-1] = robotPath[dimensions.x-1][dimensions.y-1].replace("000", "") ;
                //We call the printMatrix function to write it all the information in the OUTPUTFILE .
                printMatrix(robotPath, dimensions.y,maxPath);
                //Finished.
        }
         //matrixMovement function's main job is recursively go all the possible paths and find the one that has maximum coin .
         //We demand sizeOfColoumn to clear about code and efficent. Also we have startingPoint the shows us starting point everytime we execute the function
         //matrixPoint is a matrix with all the information about variables in inputFile, currentPath is here for we can remind the current path so we can compare with the maxPath or find the next step.
        public static int matrixMovement(int sizeOfColoumn,Point startingPoint ,String matrixPoint[][] , Path currentPath ,Path maxPath){
            //If we hadn't gone out of bounds 
            if(startingPoint.x < matrixPoint.length && startingPoint.y < sizeOfColoumn){
                //We assign the currentValue with current index value of matrixPoint.
                 String currentValue = matrixPoint[startingPoint.x][startingPoint.y] ;
                 //If currentValue isn't "X" and we can pay the coin price if it is a negative one.
                if ( !("X".equals(currentValue)) &&  currentPath.coin  >= -Integer.valueOf(currentValue)){
                    
                    //If we front with a variable whic has 3 or more characters , we simply in the finish.
                    if( currentValue.length() > 2 ){
                        //We change the last variable into the normal format, it is useless to stay this way.
                        currentValue = currentValue.replace("000", "");
                        //We increment coint with currentValue
                        //And add the coordinates into the currentPath's strPath variable , just because we can see our way in the future.
                        currentPath.coin += Integer.parseInt(currentValue) ;
                        currentPath.strPath += startingPoint.x + "" +startingPoint.y + " " ;
                        //If the currentPath has more coins than the maxPath , we change the maxPath.
                        if(currentPath.coin > maxPath.coin){
                            maxPath.coin = currentPath.coin ;
                            maxPath.strPath = currentPath.strPath ;          
                        }
                        //We delete coins and strings that comes from last variable , because we will continue with the one step back element .
                        //We will search for better path , so we have to get rid of old path informations.
                        currentPath.coin -= Integer.parseInt(currentValue);
                        currentPath.strPath= currentPath.strPath.replace(startingPoint.x + "" +startingPoint.y + " ", "");
                        //return 0 because we want to go for search again.If we type return 1 , we say the program "You can go further" but we are at the finish.
                        return 0 ;
                    }else{//If we are not at the finish and we have no "X" and we are not out of bounds , we are at the regular variable and we can go further.
                        
                        //We increment coint with currentValue
                        //And add the coordinates into the currentPath's strPath variable , just because we can see our way in the future.
                      currentPath.coin += Integer.parseInt(currentValue) ;
                      currentPath.strPath += startingPoint.x + "" +startingPoint.y + " ";
                      //We assign the right and down variables to make sure where we are , this return statements will tell us where we can go or can't.
                      Point right = new Point(startingPoint.x+""+(startingPoint.y+1));
                      Point down = new Point((startingPoint.x+1)+""+(startingPoint.y));
                        int isRight = matrixMovement(sizeOfColoumn,right, matrixPoint, currentPath, maxPath) ;
                        int isDown = matrixMovement(sizeOfColoumn,down, matrixPoint, currentPath, maxPath) ;
                        //If we have nowhere else to go , we are at the wrong place . We have to go back so we can try different options.
                        if(isDown == 0 && isRight == 0){
                        currentPath.coin -= Integer.parseInt(currentValue);
                        currentPath.strPath= currentPath.strPath.replace(startingPoint.x + "" +startingPoint.y + " ", "");
                        return 0 ;
                        }
                    }
                    //If there is no returning come to us , we can go further.
                    return 1;
                }else{
                    //We don't have much coin or we have a "X" we can't go in this way.
                    return 0 ; 
                }
            }else {
                //We are out of bounds , we can't go in this way.
                return 0;
            }
        }
}