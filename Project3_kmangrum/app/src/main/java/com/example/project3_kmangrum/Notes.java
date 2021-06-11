package com.example.project3_kmangrum;

public class Notes {

    private String noteTitle;
    private String noteDate;
    private int notePriorityIcon;


    public Notes(String noteTitle, String noteDate, int notePriorityIcon) {
        this.noteTitle = noteTitle;
        this.noteDate = noteDate;
        this.notePriorityIcon = notePriorityIcon;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public int getNotePriorityIcon() {
        return notePriorityIcon;
    }

    public void setNotePriorityIcon(int notePriorityIcon) {
        this.notePriorityIcon = notePriorityIcon;
    }
}
