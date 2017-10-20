import java.awt.event.*;
import javax.swing.*;

public class Board
{
    // Array to hold board cards
    private FlippableCard cards[];

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, ActionListener AL)
    {
        // Allocate and configure the game board: an array of cards
        cards = new FlippableCard[size];

        // Fill the Cards array
        int imageIdx = 1;
        for (int i = 0; i < size; i += 2) {

            // Load the front image from the resources folder
            String imgPath = "res/hub" + imageIdx + ".jpg";
            ImageIcon img = new ImageIcon(loader.getResource(imgPath));
            imageIdx++;  // get ready for the next pair of cards

            // Setup two cards at a time
            FlippableCard c1 = new FlippableCard(img);
            FlippableCard c2 = new FlippableCard(img);
            //the ids make sure that user doesnt click on same one twice
            c1.setID(i);
            c2.setID(i+1);
            //they are matches if same image path
            c1.setCustomName(imgPath);
            c2.setCustomName(imgPath);

            // Add them to the array
            cards[i] = c1;
            cards[i + 1] = c2;
        }

        /*
         * To-Do: Randomize the card positions
         */
        //trade places using random generator
        for (int i = 0; i <size; i++){
            int randomIcon = (int) (Math.random()*size);
            FlippableCard temp = cards[i];
            cards[i] = cards[randomIcon];
            cards[randomIcon]= temp;
        }
        //add active listener for each button
        for (int i=0; i < size; i++)
            this.cards[i].addActionListener(AL);
    }

    public void fillBoardView(JPanel view)
    {
        for (FlippableCard c : cards) {
            c.hideFront();
            view.add(c);
        }

    }

    public void resetBoard()
    {
        /*
         * To-Do: Reset the flipped cards and randomize the card positions
         */
        // remove card deck based on random generator
        for (int i = 0; i < cards.length; i++) {
            cards[i].hideFront();
            int randomIcon = (int) (Math.random() * cards.length);
            FlippableCard temp = cards[i];
            cards[i] = cards[randomIcon];
            cards[randomIcon] = temp;
        }


    }
}
