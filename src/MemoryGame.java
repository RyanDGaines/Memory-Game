

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MemoryGame extends JFrame implements ActionListener
{
    // Core game play objects
    private Board gameBoard;
    private FlippableCard prevCard1, prevCard2;

    // Labels to display game info
    private JLabel errorLabel, timerLabel;

    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;

    // Record keeping counts and times
    private int clickCount = 0, gameTime = 0, errorCount = 0;
    private int pairsFound = 0;
    private boolean[] isMatched = new boolean[24]; //used to record the number of matches


    // Game timer: will be configured to trigger an event every second
    private Timer gameTimer;

    public MemoryGame()
    {
        // Call the base class constructor
        super("Hubble Memory Game");

        // Allocate the interface elements
        JButton restart = new JButton("Restart");
        JButton quit = new JButton("Quit");
        timerLabel = new JLabel("Timer: 0");
        errorLabel = new JLabel("Errors: 0");

        /*
         * To-Do: Setup the interface objects (timer, error counter, buttons)
         * and any event handling they need to perform
         */

        //sets all found matches to false
        for (int i = 0; i < 24; i++){
            isMatched[i] = false;
        }

        //add action listners for buttons and timers
        restart.addActionListener(this);
        quit.addActionListener(this);
        gameTimer = new Timer(1000, this);

        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with cards
        gameBoard = new Board(24, this);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(4, 6, 2, 0));
        gameBoard.fillBoardView(boardView);

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(1, 4, 2, 2));
        labelView.add(quit);
        labelView.add(restart);
        labelView.add(timerLabel);
        labelView.add(errorLabel);

        // Both panels should now be individually layed out
        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.SOUTH);

        setSize(745, 470);
        setVisible(true);

    }

    /* Handle anything that gets clicked and that uses MemoryGame as an
     * ActionListener */
    public void actionPerformed(ActionEvent e)
    {
        //for when quit button is clicked
        if (e.getActionCommand() == "Quit"){
            System.exit(0);
        }
        //for when reset button is clicked
        else if (e.getActionCommand() == "Restart"){
            gameTimer.stop();
            restartGame();
        }
        //for when the event is from the timer
        else if (e.getSource() == gameTimer){
            gameTime++;
            timerLabel.setText("Timer: "+ gameTime);
        }
        //anything else is the game card being clicked
        else{
            // Get the currently clicked card from a click event
            FlippableCard currCard = (FlippableCard)e.getSource();
            currCard.showFront();
            //initial game click, start the timer and increase click by one, set prev card1 to current card
            if (clickCount==0) {
                prevCard1 = currCard;
                clickCount++;
                gameTimer.start();
                return;
            }
            // the next click, if it is the same as last click or if already matched, ignore
            else if (clickCount==1) {
                if (prevCard1.id() == currCard.id() || isMatched[currCard.id()])
                    return;
                prevCard2 = currCard;
                clickCount++;
                if (prevCard1.customName() == prevCard2.customName()){ //if the two cards are a match
                    pairsFound++;
                }
            }
            // the next click, will ignore if match, and set the flags to true if is found match, makes click count 1 to restart process
            else if (clickCount ==2){
                if (isMatched[currCard.id()]){
                    return;
                }
                if ((prevCard1.customName()==prevCard2.customName())){
                    //these cards out of play
                    isMatched[prevCard1.id()] = true;
                    isMatched[prevCard2.id()] = true;
                }
                else{
                    //if not a match increase error
                    prevCard1.hideFront();
                    prevCard2.hideFront();
                    errorCount++;
                    errorLabel.setText("Errors: "+ errorCount);
                }

                //restarts process, click count is 1 and prevcard 1 is declared
                prevCard1= currCard;
                clickCount = 1;
            }
            if (pairsFound == 12){
                //restart game and timer
                restartGame();
                gameTimer.stop();
            }
        }
    }

    private void restartGame()
    {
        pairsFound = 0;
        gameTime = 0;
        clickCount = 0;
        errorCount = 0;
        timerLabel.setText("Timer: 0");
        errorLabel.setText("Errors: 0");

        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        gameBoard.resetBoard();
        gameBoard.fillBoardView(boardView);
        for(int i = 0; i< 24; i++)
            isMatched[i] = false;
    }

    public static void main(String args[])
    {
        MemoryGame M = new MemoryGame();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }

}
