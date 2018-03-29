package com.chetan.api;

import play.libs.Json;
import play.mvc.*;
import play.db.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import views.html.*;

import com.loco.api.ApiController;
import com.loco.data.SystemDatabase;
import com.loco.data.Tables;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class ApiSystem extends ApiController {
    SystemDatabase sdb;
    private ArrayNode allFloors = Json.newArray();

    @Inject
    public ApiSystem(SystemDatabase sdb)
    {
        this.sdb = sdb;
        this.init();
    }

    /**
        To set up initial structure -
        All floors and rooms layout for each of them 
    */
    private void init()
    {
        ObjectNode groundFloor = Json.newObject();
        groundFloor.put("id","groundFloor");
        groundFloor.put("label","Ground Floor");
        groundFloor.put("rooms",this.getRooms("groundFloor"));
        this.allFloors.add(groundFloor);

        ObjectNode firstFloor = Json.newObject();
        firstFloor.put("id","firstFloor");
        firstFloor.put("label","First Floor");
        firstFloor.put("rooms",this.getRooms("firstFloor"));
        this.allFloors.add(firstFloor);

        ObjectNode secondFloor = Json.newObject();
        secondFloor.put("id","secondFloor");
        secondFloor.put("label","Second Floor");
        secondFloor.put("rooms",this.getRooms("secondFloor"));
        this.allFloors.add(secondFloor);

        ObjectNode thirdFloor =Json.newObject();
        thirdFloor.put("id","thirdFloor");
        thirdFloor.put("label","Third Floor");
        thirdFloor.put("rooms",this.getRooms("thirdFloor"));
        this.allFloors.add(thirdFloor);
    }

    /**
     * Method for the api end point to get all the floors 
     * available to choose to see the occupancy level of the room
     * for the floor
     */
    public Result getAllFloorNames()
    {
        ObjectNode allFloorsObj = Json.newObject();
        ArrayNode allFloorNames = Json.newArray();
        for(int ctr=0; ctr < this.allFloors.size(); ctr++)
        {
            JsonNode floor = this.allFloors.get(ctr);
            ObjectNode newFloor = Json.newObject();
            newFloor.put("id",floor.get("id").asText());
            newFloor.put("label",floor.get("label").asText());
            allFloorNames.add(newFloor);
        }
        allFloorsObj.put("current_floor","");
        allFloorsObj.put("all_floors",allFloorNames);

        System.out.println("\n =========================================");
        System.out.println(allFloorsObj);
        return ok(allFloorsObj);
    }

    /**
     *  To get all the rooms for the floor
     */ 
    
    public ArrayNode getRooms(String floorId)
    {
        ArrayNode roomsForFloor = Json.newArray();

        if(floorId.equalsIgnoreCase("groundFloor"))
        {

            ObjectNode firstRoom = Json.newObject();
            firstRoom.put("id","firstRoom");
            firstRoom.put("label","First Room");
            firstRoom.put("type","rect");
            firstRoom.put("color","palegreen");
            firstRoom.put("startX","20");
            firstRoom.put("startY","20");
            firstRoom.put("width","40");
            firstRoom.put("height","30");
            roomsForFloor.add(firstRoom);
        
            ObjectNode secondRoom = Json.newObject();
            secondRoom.put("id","secondRoom");
            secondRoom.put("label","Second Room");
            secondRoom.put("type","rect");
            secondRoom.put("color","sandybrown");
            secondRoom.put("startX","20");
            secondRoom.put("startY","55");
            secondRoom.put("width","40");
            secondRoom.put("height","40");
            roomsForFloor.add(secondRoom);

             ObjectNode thirdRoom = Json.newObject();
            secondRoom.put("id","thirdRoom");
            thirdRoom.put("label","Third Room");
            thirdRoom.put("type","rect");
            thirdRoom.put("color","palegreen");
            thirdRoom.put("startX","20");
            thirdRoom.put("startY","100");
            thirdRoom.put("width","40");
            thirdRoom.put("height","60");
            roomsForFloor.add(thirdRoom);

             ObjectNode fourthRoom = Json.newObject();
            fourthRoom.put("id","fourthRoom");
            fourthRoom.put("label","Fourth Room");
            fourthRoom.put("type","rect");
            fourthRoom.put("color","palegreen");
            fourthRoom.put("startX","20");
            fourthRoom.put("startY","165");
            fourthRoom.put("width","40");
            fourthRoom.put("height","70");
            roomsForFloor.add(fourthRoom);

             ObjectNode fifthRoom = Json.newObject();
            fifthRoom.put("id","fifthRoom");
            fifthRoom.put("label","Fifth Room");
            fifthRoom.put("type","rect");
            fifthRoom.put("color","sandybrown");
            fifthRoom.put("startX","20");
            fifthRoom.put("startY","240");
            fifthRoom.put("width","40");
            fifthRoom.put("height","80");
            roomsForFloor.add(fifthRoom);

            ObjectNode sixthRoom = Json.newObject();
            sixthRoom.put("id","sixthRoom");
            sixthRoom.put("label","Sixth Room");
            sixthRoom.put("type","rect");
            sixthRoom.put("color","sandybrown");
            sixthRoom.put("startX","20");
            sixthRoom.put("startY","320");
            sixthRoom.put("width","40");
            sixthRoom.put("height","60");
            roomsForFloor.add(sixthRoom);

            ObjectNode seventhRoom = Json.newObject();
            seventhRoom.put("id","seventhRoom");
            seventhRoom.put("label","Seventh Room");
            seventhRoom.put("type","eightAngles");
            seventhRoom.put("color","skyblue");
            seventhRoom.put("x1","65");
            seventhRoom.put("y1","40");
             seventhRoom.put("x2","145");
            seventhRoom.put("y2","40");
             seventhRoom.put("x3","145");
            seventhRoom.put("y3","200");
             seventhRoom.put("x4","245");
            seventhRoom.put("y4","200");
             seventhRoom.put("x5","245");
            seventhRoom.put("y5","360");
             seventhRoom.put("x6","205");
            seventhRoom.put("y6","360");
             seventhRoom.put("x7","205");
            seventhRoom.put("y7","380");
             seventhRoom.put("x8","65");
            seventhRoom.put("y8","380");
            roomsForFloor.add(seventhRoom);


            // Other(right side) of the ground floor
             ObjectNode eighthRoom = Json.newObject();
            eighthRoom.put("id","eighthRoom");
            eighthRoom.put("label","Eighth Room");
            eighthRoom.put("type","rect");
            eighthRoom.put("color","palegreen");
            eighthRoom.put("startX","380");
            eighthRoom.put("startY","20");
            eighthRoom.put("width","50");
            eighthRoom.put("height","80");
            roomsForFloor.add(eighthRoom);

             ObjectNode ninthRoom = Json.newObject();
            ninthRoom.put("id","ninthRoom");
            ninthRoom.put("label","Ninth Room");
            ninthRoom.put("type","rect");
            ninthRoom.put("color","sandybrown");
            ninthRoom.put("startX","435");
            ninthRoom.put("startY","20");
            ninthRoom.put("width","40");
            ninthRoom.put("height","30");
            roomsForFloor.add(ninthRoom);

            ObjectNode tenthRoom = Json.newObject();
            tenthRoom.put("id","tenthRoom");
            tenthRoom.put("label","Tenth Room");
            tenthRoom.put("type","rect");
            tenthRoom.put("color","sandybrown");
            tenthRoom.put("startX","480");
            tenthRoom.put("startY","20");
            tenthRoom.put("width","40");
            tenthRoom.put("height","30");
            roomsForFloor.add(tenthRoom);

            ObjectNode eleventhRoom = Json.newObject();
            eleventhRoom.put("id","eleventhRoom");
            eleventhRoom.put("label","Eleventh Room");
            eleventhRoom.put("type","rect");
            eleventhRoom.put("color","sandybrown");
            eleventhRoom.put("startX","525");
            eleventhRoom.put("startY","20");
            eleventhRoom.put("width","40");
            eleventhRoom.put("height","30");
            roomsForFloor.add(eleventhRoom);

            ObjectNode thirteenthRoom = Json.newObject();
            thirteenthRoom.put("id","thirteenthRoom");
            thirteenthRoom.put("label","Thirteenth Room");
            thirteenthRoom.put("type","rect");
            thirteenthRoom.put("color","palegreen");
            thirteenthRoom.put("startX","570");
            thirteenthRoom.put("startY","20");
            thirteenthRoom.put("width","40");
            thirteenthRoom.put("height","30");
            roomsForFloor.add(thirteenthRoom);

             ObjectNode fourteenthRoom = Json.newObject();
            fourteenthRoom.put("id","fourteenthRoom");
            fourteenthRoom.put("label","Fourteenth Room");
            fourteenthRoom.put("type","rect");
            fourteenthRoom.put("color","sandybrown");
            fourteenthRoom.put("startX","615");
            fourteenthRoom.put("startY","20");
            fourteenthRoom.put("width","55");
            fourteenthRoom.put("height","30");
            roomsForFloor.add(fourteenthRoom);

             ObjectNode twelthRoom = Json.newObject();
            twelthRoom.put("id","twelthRoom");
            twelthRoom.put("label","Twelth Room");
            twelthRoom.put("type","rect");
            twelthRoom.put("color","skyblue");
            twelthRoom.put("startX","435");
            twelthRoom.put("startY","55");
            twelthRoom.put("width","175");
            twelthRoom.put("height","60");
            roomsForFloor.add(twelthRoom);

             ObjectNode fifteenthRoom = Json.newObject();
            fifteenthRoom.put("id","fifteenthRoom");
            fifteenthRoom.put("label","Fifteenth Room");
            fifteenthRoom.put("type","rect");
            fifteenthRoom.put("color","skyblue");
            fifteenthRoom.put("startX","615");
            fifteenthRoom.put("startY","55");
            fifteenthRoom.put("width","55");
            fifteenthRoom.put("height","140");
            roomsForFloor.add(fifteenthRoom);

            ObjectNode seventeenthRoom = Json.newObject();
            seventeenthRoom.put("id","seventeenthRoom");
            seventeenthRoom.put("label","Seventeenth Room");
            seventeenthRoom.put("type","rect");
            seventeenthRoom.put("color","sandybrown");
            seventeenthRoom.put("startX","615");
            seventeenthRoom.put("startY","200");
            seventeenthRoom.put("width","55");
            seventeenthRoom.put("height","50");
            roomsForFloor.add(seventeenthRoom);

            ObjectNode eightteenthRoom = Json.newObject();
            eightteenthRoom.put("id","eightteenthRoom");
            eightteenthRoom.put("label","Eightteenth Room");
            eightteenthRoom.put("type","rect");
            eightteenthRoom.put("color","sandybrown");
            eightteenthRoom.put("startX","475");
            eightteenthRoom.put("startY","120");
            eightteenthRoom.put("width","135");
            eightteenthRoom.put("height","130");
            roomsForFloor.add(eightteenthRoom);

            ObjectNode nineteenthRoom = Json.newObject();
            nineteenthRoom.put("id","nineteenthRoom");
            nineteenthRoom.put("label","Nineteenth Room");
            nineteenthRoom.put("type","rect");
            nineteenthRoom.put("color","palegreen");
            nineteenthRoom.put("startX","380");
            nineteenthRoom.put("startY","120");
            nineteenthRoom.put("width","90");
            nineteenthRoom.put("height","130");
            roomsForFloor.add(nineteenthRoom);
        }


        // First floor
        if(floorId.equalsIgnoreCase("firstFloor"))
        {

            ObjectNode firstRoom = Json.newObject();
            firstRoom.put("id","firstRoom");
            firstRoom.put("label","First Room");
            firstRoom.put("type","rect");
            firstRoom.put("color","palegreen");
            firstRoom.put("startX","20");
            firstRoom.put("startY","20");
            firstRoom.put("width","180");
            firstRoom.put("height","80");
            roomsForFloor.add(firstRoom);
        
            ObjectNode secondRoom = Json.newObject();
            secondRoom.put("id","secondRoom");
            secondRoom.put("label","Second Room");
            secondRoom.put("type","rect");
            secondRoom.put("color","skyblue");
            secondRoom.put("startX","205");
            secondRoom.put("startY","20");
            secondRoom.put("width","180");
            secondRoom.put("height","80");
            roomsForFloor.add(secondRoom);

            ObjectNode thirdRoom = Json.newObject();
            thirdRoom.put("id","thirdRoom");
            thirdRoom.put("label","Third Room");
            thirdRoom.put("type","rect");
            thirdRoom.put("color","sandybrown");
            thirdRoom.put("startX","390");
            thirdRoom.put("startY","20");
            thirdRoom.put("width","80");
            thirdRoom.put("height","80");
            roomsForFloor.add(thirdRoom);

            ObjectNode fourthRoom = Json.newObject();
            fourthRoom.put("id","fourthRoom");
            fourthRoom.put("label","Fourth Floor");
            fourthRoom.put("type","rect");
            fourthRoom.put("color","palegreen");
            fourthRoom.put("startX","475");
            fourthRoom.put("startY","20");
            fourthRoom.put("width","200");
            fourthRoom.put("height","360");
            roomsForFloor.add(fourthRoom);
        }

        
        // Second floor
        if(floorId.equalsIgnoreCase("secondFloor"))
        {

            ObjectNode firstRoom = Json.newObject();
            firstRoom.put("id","firstRoom");
            firstRoom.put("label","First Room");
            firstRoom.put("type","rect");
            firstRoom.put("color","palegreen");
            firstRoom.put("startX","20");
            firstRoom.put("startY","20");
            firstRoom.put("width","180");
            firstRoom.put("height","80");
            roomsForFloor.add(firstRoom);
        
            ObjectNode secondRoom = Json.newObject();
            secondRoom.put("id","secondRoom");
            secondRoom.put("label","Second Room");
            secondRoom.put("type","rect");
            secondRoom.put("color","skyblue");
            secondRoom.put("startX","20");
            secondRoom.put("startY","120");
            secondRoom.put("width","180");
            secondRoom.put("height","80");
            roomsForFloor.add(secondRoom);

            ObjectNode thirdRoom = Json.newObject();
            secondRoom.put("id","thirdRoom");
            thirdRoom.put("label","Third Room");
            thirdRoom.put("type","rect");
            thirdRoom.put("color","sandybrown");
            thirdRoom.put("startX","220");
            thirdRoom.put("startY","20");
            thirdRoom.put("width","80");
            thirdRoom.put("height","80");
            roomsForFloor.add(thirdRoom);

            ObjectNode fourthRoom = Json.newObject();
            fourthRoom.put("id","fourthRoom");
            fourthRoom.put("label","Fourth Floor");
            fourthRoom.put("type","rect");
            fourthRoom.put("color","palegreen");
            fourthRoom.put("startX","220");
            fourthRoom.put("startY","120");
            fourthRoom.put("width","80");
            fourthRoom.put("height","80");
            roomsForFloor.add(fourthRoom);
        }

        
        // Third floor
        if(floorId.equalsIgnoreCase("thirdFloor"))
        {

            ObjectNode firstRoom = Json.newObject();
            firstRoom.put("id","firstRoom");
            firstRoom.put("label","First Room");
            firstRoom.put("type","rect");
            firstRoom.put("color","palegreen");
            firstRoom.put("startX","20");
            firstRoom.put("startY","20");
            firstRoom.put("width","180");
            firstRoom.put("height","80");
            roomsForFloor.add(firstRoom);
        
            ObjectNode secondRoom = Json.newObject();
            secondRoom.put("id","secondRoom");
            secondRoom.put("label","Second Room");
            secondRoom.put("type","rect");
            secondRoom.put("color","skyblue");
            secondRoom.put("startX","20");
            secondRoom.put("startY","120");
            secondRoom.put("width","180");
            secondRoom.put("height","80");
            roomsForFloor.add(secondRoom);

             ObjectNode thirdRoom = Json.newObject();
            secondRoom.put("id","thirdRoom");
            thirdRoom.put("label","Third Room");
            thirdRoom.put("type","rect");
            thirdRoom.put("color","sandybrown");
            thirdRoom.put("startX","220");
            thirdRoom.put("startY","20");
            thirdRoom.put("width","80");
            thirdRoom.put("height","80");
            roomsForFloor.add(thirdRoom);

             ObjectNode fourthRoom = Json.newObject();
            fourthRoom.put("id","fourthRoom");
            fourthRoom.put("label","Fourth Floor");
            fourthRoom.put("type","rect");
            fourthRoom.put("color","palegreen");
            fourthRoom.put("startX","350");
            fourthRoom.put("startY","250");
            fourthRoom.put("width","400");
            fourthRoom.put("height","100");
            roomsForFloor.add(fourthRoom);
        }

        return roomsForFloor;
    }

    /**
        Api end point method to get rooms layout for the 
        chosen floor
     */
    public Result getFloorRooms(String floorId)
    {
        ObjectNode allRoomsObj = Json.newObject();
        ArrayNode roomsToReturn = Json.newArray();

        for(int ctr=0; ctr < this.allFloors.size();ctr++)
        {
            if(allFloors.get(ctr).get("id").asText().equalsIgnoreCase(floorId))
            {
                roomsToReturn = (ArrayNode)allFloors.get(ctr).get("rooms");
            }
        }

        allRoomsObj.put("current_floor",floorId);
        allRoomsObj.put("all_rooms",roomsToReturn);
        System.out.println(allRoomsObj);
        return ok(allRoomsObj);
    }

}