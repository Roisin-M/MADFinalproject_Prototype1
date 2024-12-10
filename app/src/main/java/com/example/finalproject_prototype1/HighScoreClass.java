package com.example.finalproject_prototype1;

public class HighScoreClass <HighScoreClass>{
    int _id;
    String _name;
    int _highscore;
    //empty constructor
    public HighScoreClass(){   }
    //constructor with all properties
    public HighScoreClass(int id, String name, int highscore){
        this._id = id;
        this._name = name;
        this._highscore = highscore;
    }
    //constructor with name and score
    public HighScoreClass(String name, int highscore){
        this._name = name;
        this._highscore = highscore;
    }
    //getters and setters
    public int getID(){
        return this._id;
    }
    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }
    public void setName(String name){
        this._name = name;
    }

    public int getHighscore(){
        return this._highscore;
    }
    public void setHighscore(int highscore){
        this._highscore = highscore;
    }
}
