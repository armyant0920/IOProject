package DBTool;

public class DBParam {
    private  String server;
    private  String port;
    private  String databae;
    private  String user;
    private  String password;
    private boolean remember;


    public DBParam() {
    }

    public DBParam(String server, String port, String databae, String user, String password,boolean remember) {
        this.server = server;
        this.port = port;
        this.databae = databae;
        this.user = user;
        this.password = password;
        this.remember=remember;

    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabae() {
        return databae;
    }

    public void setDatabae(String databae) {
        this.databae = databae;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
