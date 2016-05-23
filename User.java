public class User {
    private int myUserID, myConvID;
    private String myUsername, myConvName;

    public User() {
        myUserID = -1;
        myUsername = "Username";
        myConvID = -1;
        myConvName = "NoConv";
    }

    public void setUserID(int theUserID) {
        myUserID = theUserID;
    }

    public void setConvID(int theConvID) {
        myConvID = theConvID;
    }

    public void setUsername(String theUsername) {
        myUsername = theUsername;
    }

    public void setConvName(String theConvName) {
        myConvName = theConvName;
    }

    public int getUserID() {
        return myUserID;
    }

    public int getConvID() {
        return myConvID;
    }

    public String getUsername() {
        return myUsername;
    }

    public String getConvName() {
        return myConvName;
    }

    public String toString() {
        String info = "";
        info += "UserID " + myUserID + "\n";
        info += "Username " + myUsername + "\n";
        info += "ConvID " + myConvID + "\n";
        info += "ConvName " + myConvName + "\n";
        return info;
    }

}
