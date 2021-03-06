package com.betteru.ucsd.myapplication4;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Yuting on 11/3/2017.
 */

public class ChallengeModel implements Serializable {
    String ownerId;
    String title;
    String timeStamp;
    ArrayList<String> activities;
    ArrayList<String> participants;
    ArrayList<String> winner;
    ArrayList<Integer> participantsIcon;
    ArrayList<Integer> activitiesIcon;
    LocalDate date;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    ChallengeModel(String ownerId, String title, String timeStamp,
                   ArrayList<String> participants, ArrayList<String> activities)
    {
        this.ownerId = ownerId;
        this.title = title;
        this.timeStamp = timeStamp;
        this.date = LocalDate.parse(timeStamp, formatter);
        this.activities = activities;
        this.participants = participants;
    }

    public void setIcon(ArrayList<Integer> parIcon, ArrayList<Integer> actIcon)
    {
        this.participantsIcon = parIcon;
        this.activitiesIcon = actIcon;
    }
}
