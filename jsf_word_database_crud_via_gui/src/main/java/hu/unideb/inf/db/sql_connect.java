package hu.unideb.inf.db;
import java.sql.*;
import java.util.ArrayList;

public class sql_connect {
    Connection con;
    ResultSet result_set;

    //localhost,3306,szavak,root,root
    /**
     * creates connection to the database.
     * @param url the url of the database, string
     * @param port the port of the database, string
     * @param db_name the name of the database, string
     * @param username the username, string
     * @param password the password, string
     */
    public sql_connect(String url, String port, String db_name, String username, String password) {
        try {
            con = connect_to_db(url, port, db_name, username, password);
            System.out.println("connected to db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Connection connect_to_db(String url, String port, String db_name, String username, String password) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + db_name, username, password);
        return con;
    }

    /**
     *select all data from the given table
     * @param con a connection you can get from sql_connect.get_connection()
     * @param table_name a string
     * @throws SQLException if the connection is not available
     */
    public ArrayList<String> select_query(Connection con, String table_name) throws SQLException {
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("select * from " + table_name);
        set_result(rs);
        ResultSetMetaData rsmd= rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        ArrayList<String> result = new ArrayList<>();

        if(!rs.next()){
            System.out.println("its empty");
        }
        rs.beforeFirst();
        System.out.println("\n the database contains:\n");
        while (rs.next()) {
            int i = 1;
            StringBuilder data = new StringBuilder();
            while (i<=columnsNumber) {
                try {
                    //Display values
                    data.append(rsmd.getColumnName(i)).append(": ").append(rs.getObject(i)).append(" ");
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            result.add(data.toString());
            System.out.println(data);
        }
        return result;
    }

    /**
     * gets a specific data based on word
     * @param con connection you can get with sql_connect.get_connection()
     * @param table_name name of the table
     * @param wordString name of the column
     * @return the last value of the chosen col
     *
     */
    public ArrayList<String>  get_specific_data(Connection con, String table_name,String wordString) throws SQLException {
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String query = "SELECT * FROM "+table_name +" WHERE word LIKE '"+wordString+"';";
        ResultSet rs = stmt.executeQuery(query);
        set_result(rs);
        ResultSetMetaData rsmd= rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        ArrayList<String> result = new ArrayList<>();

        if(!rs.next()){
            System.out.println("its empty");
        }
        rs.beforeFirst();
        System.out.println("\n the database contains:\n");
        while (rs.next()) {
            int i = 1;
            StringBuilder data = new StringBuilder();
            while (i<=columnsNumber) {
                try {
                    //Display values
                    data.append(rsmd.getColumnName(i)).append(": ").append(rs.getObject(i)).append(" ");
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            result.add(data.toString());
            System.out.println(data);
        }
        return result;
    }

    private void set_result(ResultSet res){
        result_set = res;
    }

    public ResultSet get_result(){
        return result_set;
    }

    /**
     * delete data based on id in a give table.
     * @param conn a connection you can get from sql_connect.get_connection()
     * @param table_name a string
     * @param data_name a string (can be multiple if separated with ,)
     * @param data a string (can be multiple if separated with ,) eg: 'asd','asd2'
     */
    public void insert_data(Connection conn, String table_name,String data_name, String data) {
        try {
            Statement st = conn.createStatement();

            String query = String.format("INSERT INTO %s (%s) VALUES (%s);",table_name,data_name,data);
            st.executeUpdate(query);
        } catch (Exception e) {
            System.err.println("Got an exception while inserting! tablename: "+table_name + " dataname: "+data_name+" data:"+data);
            System.err.println(e.getMessage());
        }
    }

    /**
     * delete data based on id in a give table.
     * @param conn a connection you can get from sql_connect.get_connection()
     * @param table_name a string
     * @param id an integer , the id of the word
     * @param updatedWord a string to update the word with
     * @param difficulty a string to update the difficulty with
     * @return the updated data as string
     */
    public String update_data(Connection conn, String table_name, int id,String updatedWord,String difficulty) {
        String result ="";
        try {
            String query = "UPDATE "+table_name +
                           " SET word = '"+updatedWord+"', difficulty = '"+difficulty+"' " +
                    "WHERE id = "+id+";";
            Statement st = conn.createStatement();
            st.executeUpdate(query);

            query = "SELECT * FROM "+table_name+ " WHERE id = '"+id+"';";
            Statement st2 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st2.executeQuery(query);
            ResultSetMetaData rsmd= rs.getMetaData();
            if(!rs.next()){
                System.out.println("its empty");
            }
            rs.beforeFirst();
            System.out.println("\n the database contains:\n");
            while (rs.next()) {
                int i = 1;
                StringBuilder data = new StringBuilder();
                while (i<=rsmd.getColumnCount()) {
                    try {
                        //Display values
                        data.append(rsmd.getColumnName(i)).append(": ").append(rs.getObject(i)).append(" ");
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                result = (data.toString());
                System.out.println(data);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return result;
    }

    /**
     * delete data based on id in a give table.
     * @param conn a connection you can get from sql_connect.get_connection()
     * @param table_name a string
     * @param id an integer
     */
    public void delete_data(Connection conn, String table_name, int id) {
        try {
            String query = "delete from " + table_name + " where id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.execute();

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }



    /**
     * removes all data from the given table.
     * @param conn a connection you can get from sql_connect.get_connection()
     * @param table_name a string
     */
    public void purge_table(Connection conn,String table_name){
        try {
            Statement stmt = conn.createStatement();
            String sql = "TRUNCATE TABLE " + table_name;
            stmt.executeUpdate(sql);
            System.out.println("Table purged");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }



    /**
     * gets the current sql connection.
     * @return the sql connection
     */
    public Connection get_connection() {
        return con;
    }
}
