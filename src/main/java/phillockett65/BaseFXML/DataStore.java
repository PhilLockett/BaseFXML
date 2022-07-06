/*  BaseFXML - a JavaFX based playing card image generator.
 *
 *  Copyright 2022 Philip Lockett.
 *
 *  This file is part of BaseFXML.
 *
 *  BaseFXML is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BaseFXML is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with BaseFXML.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * DataStore is a class that serializes the settings data for saving and 
 * restoring to and from disc.
 */
package phillockett65.BaseFXML;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.scene.paint.Color;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private String myText;
    private String myBigText;

    private Boolean firstCheck;
    private Boolean secondCheck;
    private Boolean thirdCheck;

    private Integer radioSelection;

    private Integer monthIndex;
    private Integer bestDayIndex;
    private Double red;
    private Double green;
    private Double blue;

    private Integer myInteger;
    private Double myDouble;
    private Integer day;

    public DataStore() {
    }

    /**
     * Data exchange from the model to this DataStore.
     * @param model contains the data.
     * @return true if data successfully pulled from the model, false otherwise.
     */
    public boolean pull(Model model) {
        boolean success = true;

        myText = model.getMyText();
        myBigText = model.getMyBigText();

        firstCheck = model.isFirstCheck();
        secondCheck = model.isSecondCheck();
        thirdCheck = model.isThirdCheck();

        radioSelection = mapRadioSelection(model);

        monthIndex = model.getMonthIndex();
        bestDayIndex = model.getBestDayIndex();
        red = model.getMyColour().getRed();
        green = model.getMyColour().getGreen();
        blue = model.getMyColour().getBlue();

        myInteger = model.getInteger();
        myDouble = model.getDouble();
        day = model.getDayIndex();

        return success;
    }

    /**
     * Data exchange from this DataStore to the model.
     * @param model contains the data.
     * @return true if data successfully pushed to the model, false otherwise.
     */
    public boolean push(Model model) {
        boolean success = true;

        model.setMyText(myText);
        model.setMyBigText(myBigText);

        model.setFirstCheck(firstCheck);
        model.setSecondCheck(secondCheck);
        model.setThirdCheck(thirdCheck);

        unmapRadioSelection(model);

        model.setMonthIndex(monthIndex);
        model.setBestDayIndex(bestDayIndex);
        model.setMyColour(Color.color(red, green, blue));

        model.setInteger(myInteger);
        model.setDouble(myDouble);
        model.setDayIndex(day);

        return success;
    }



    /************************************************************************
     * Support code for static public interface.
     */

    /**
     * Static method that instantiates a DataStore, populates it from the 
     * model and writes it to disc.
     * @param model contains the data.
     * @return true if data successfully written to disc, false otherwise.
     */
    public static boolean writeData(Model model) {
        boolean success = false;

        DataStore dataStore = new DataStore();
        if (!dataStore.pull(model)) {
            dataStore.dump();

            return false;
        }

        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(model.getSettingsFile()));

            objectOutputStream.writeObject(dataStore);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return success;
    }

    /**
     * Static method that instantiates a DataStore, populates it from disc 
     * and writes it to the model.
     * @param model contains the data.
     * @return true if data successfully read from disc, false otherwise.
     */
    public static boolean readData(Model model) {
        boolean success = false;

        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(model.getSettingsFile()));

            DataStore dataStore = (DataStore)objectInputStream.readObject();
            success = dataStore.push(model);
            dataStore.dump();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return success;
    }


    /************************************************************************
     * Support code for mapping data.
     */

    /**
     * Maps an integer to the radio button selection.
     * @param model contains the data.
     */
    private void unmapRadioSelection(Model model) {
        if (radioSelection == 1)
            model.setFirstRadio();
        else if (radioSelection == 2)
            model.setSecondRadio();
        else
            model.setThirdRadio();
    }

    /**
     * Maps the radio button selection to an integer.
     * @param model contains the data.
     */
    private int mapRadioSelection(Model model) {
        if (model.isFirstRadio())
            return 1;

        if (model.isSecondRadio())
            return 2;

        return 3;
    }


    /************************************************************************
     * Support code for debug.
     */

     /**
      * Print data store on the command line.
      */
    private void dump() {
        // System.out.println("myText = " + myText);
        // System.out.println("myBigText = " + myBigText);
        // System.out.println("firstCheck = " + firstCheck);
        // System.out.println("secondCheck = " + secondCheck);
        // System.out.println("thirdCheck = " + thirdCheck);
        // System.out.println("radioSelection = " + radioSelection);
        // System.out.println("monthIndex = " + monthIndex);
        // System.out.println("bestDayIndex = " + bestDayIndex);
        // System.out.println("Colour = RGB(" + red + ", " + green + ", " + blue + ")");
        // System.out.println("myInteger = " + myInteger);
        // System.out.println("myDouble = " + myDouble);
        // System.out.println("day = " + day);
    }

}

