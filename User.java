public class User {
    private String myUsername, myConvName;

    public User() {
        myUsername = "Username";
        myConvName = "NoConv";
    }

    public void setUsername(String theUsername) {
        myUsername = theUsername;
    }

    public void setConvName(String theConvName) {
        myConvName = theConvName;
    }

    public String getUsername() {
        return myUsername;
    }

    public String getConvName() {
        return myConvName;
    }

    public String toString() {
        return "Username " + myUsername + "\n" + "ConvName " + myConvName + "\n";
    }

}
