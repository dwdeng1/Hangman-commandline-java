
import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class Wordsa {
   public static final int constant = 33736;//constant
public static void main (String[] args) throws IOException
{

    System.out.println("Welcome to Words: The Word Guessing Game!");
    System.out.println("Play as many games as you like. I'll remember your top score.");
    System.out.println("and also compute your average for all games played.");
    System.out.println("- - - - - - - - - - - - - - - - - - - - - -");
    int randomNum = ThreadLocalRandom.current().nextInt(1,constant+1);//+1 at end makes it inclusive
    float largest=0;
    float average = 0;
    float sum = 0;
    float timesRun=0;
    Scanner input = new Scanner(System.in);

    FileInputStream fstream = new FileInputStream("wordlist.txt");// obj to read file
    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));//actually read it
    String fileline;
    int wordCount=0;
    String hangman="EMPTY";
   while ((fileline=br.readLine() ) != null)//read until sees nothing left
    {
        wordCount++;
        if (wordCount==randomNum)
        {
            hangman=fileline;//this is the word we want, so stop loop
            break;
        }
    }

  ;

    String disguise="";
    for (int i = 0 ; i < hangman.length(); i++)
    {
        String parseHangman = hangman.toLowerCase();//used to parse so dont run into captailization problems later
        if (parseHangman.charAt(i) == 'a'||parseHangman.charAt(i) == 'e'||parseHangman.charAt(i) == 'i'||parseHangman.charAt(i) == 'o'
       || parseHangman.charAt(i) == 'u') //parse vowels
        {
           disguise= disguise.concat("=");
        }
        else//consasant
        {
            disguise= disguise.concat("-");
        }
    }

    String guessFail="";
    String guessSuccess="";
    int guessCount=0;
    boolean doublecount = false;
    boolean playAgain=true;
    boolean reset=false;//needed to redo fileread every time
    while(playAgain)
    {
        if (reset)
        {
            FileInputStream fstream1 = new FileInputStream("wordlist.txt");// obj to read file
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fstream1));//actually read it
            Random rand = new Random();
            randomNum =   rand.nextInt((constant- 1) + 1) + 1;
            String fileline2;
            while ((fileline2=br1.readLine() ) != null)//read until sees nothing left
            {
                wordCount++;
                if (wordCount==randomNum)
                {
                    hangman=fileline2;//this is the word we want, so stop loop
                    break;
                }
            }

            ;

            disguise="";
            for (int i = 0 ; i < hangman.length(); i++)
            {
                String parseHangman = hangman.toLowerCase();//used to parse so dont run into captailization problems later
                if (parseHangman.charAt(i) == 'a'||parseHangman.charAt(i) == 'e'||parseHangman.charAt(i) == 'i'||parseHangman.charAt(i) == 'o'
                        || parseHangman.charAt(i) == 'u') //parse vowels
                {
                    disguise= disguise.concat("=");
                }
                else//consasant
                {
                    disguise= disguise.concat("-");
                }
            }
        }
        reset=false;

        boolean skip=false;
        boolean skipSuc=false;

        System.out.println("Letters already guessed: "+ guessFail);
        System.out.println("Guess a letter in this word: "+ disguise +" ??");
        String parseLater =  input.nextLine();//get as string

        if (parseLater.length()>1)//put like a string or something crazy, force new input
        {
            while ( parseLater.length()> 1) {
                System.out.println("Not a letter. Guess again:");
                parseLater = input.nextLine();
            }
        }
        char inputChar = parseLater.charAt(0); //get 1st character of string, cause should only be 1 character
        if (!Character.isLetter(inputChar))//force them to take another input
        {
            while (!Character.isLetter(inputChar)) {
                System.out.println("Not a letter. Guess again:");
                inputChar = input.nextLine().charAt(0);


            }
        }
        if (guessFail.indexOf(inputChar) != -1)//repeated
        {
            System.out.println("==> " +inputChar+ "  was already guessed.");
            skip =true;

        }
        if (guessSuccess.indexOf(inputChar) != -1)//repeated
        {
            System.out.println("==> " +inputChar+ "  was already guessed correctly.");
          skipSuc=true;

        }

        hangman=hangman.toLowerCase();
        boolean found=false;



        for (int i = 0 ; i < hangman.length(); i ++)
        {
            if (hangman.charAt(i) == inputChar && !skipSuc)
            {
                found=true;
                guessSuccess= guessSuccess.concat(String.valueOf(inputChar));
            //spicy substring math to replace the character we want, and supports multiple replacements at once!
                disguise= disguise.substring(0,i) + hangman.charAt(i) + disguise.substring(i+1);

                guessCount++;

              //iterate over success to make sure it doesnt double count
                for (int j = 0; j < guessSuccess.length();j++)
                {
                    for (int z = j+1; z< guessSuccess.length(); z++)
                    {
                        if (guessSuccess.charAt(j) == guessSuccess.charAt(z)) {
                         guessSuccess=   guessSuccess.replaceFirst(String.valueOf(guessSuccess.charAt(j)), " ");
                            doublecount = true;
                            break;
                        }
                    }
                }

            }


        }
        if (doublecount )//because of the way i parsed it, i need to remove 1 more point (only for duplicates, calc'd above)
        {
            guessCount--;
            doublecount=false;
        }
        if (!found && !skip && !skipSuc)//print guesses
        {
            guessCount++;
           guessFail= guessFail.concat(String.valueOf(inputChar));


        }
        System.out.println(disguise);


        if ( disguise.indexOf('=')==-1 && disguise.indexOf('-')==-1)//good, no more hangman
        {
            System.out.println("You guessed this word: " + hangman + " in " + guessCount + " guesses!");

            float finalValue = (100 - (float) (guessCount) / 26 * 100 + (float) (hangman.length()) / 26 * 100);
            DecimalFormat df = new DecimalFormat();//simple decimal formatting
            df.setMaximumFractionDigits(1);
            System.out.println("Your score: " + df.format(finalValue) + "%!");
            timesRun++;

            if (largest < finalValue) //replace largest every time you see a bigger one
            {
                largest=finalValue;
            }
            sum = sum + finalValue;
            System.out.println("Want to play again?");
            char input1 = input.nextLine().toLowerCase().charAt(0);

             if (input1=='y')
            {
                guessCount=0;
                guessFail="";
                guessSuccess="";

                reset=true;
            }
            else
            {
                System.out.println("Thanks for playing…");
                DecimalFormat df1 = new DecimalFormat();//simple decimal formatting
                df1.setMaximumFractionDigits(2);
                System.out.println("Your best score: " + df1.format(largest) + "%!");

                average=sum/timesRun;
                DecimalFormat df2 = new DecimalFormat();//simple decimal formatting
                df2.setMaximumFractionDigits(2);
                System.out.println("Your average score: " + df2.format(average) + "%!");
                playAgain=false;

            }
        }

    }


}
}

