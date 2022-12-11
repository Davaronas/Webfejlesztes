package hu.unideb.inf.wordLogic;

import hu.unideb.inf.db.sql_connect;
import lombok.Data;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.ManagedBean;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;

@Named
@Data
public class Word {
    sql_connect con;
    String fetchedValues = "";
    String updatedValue = "";

    private Integer id = 0;
    private Integer idUpdate = 0;

    private String wordName = "";
    private String wordNameForFetch = "";
    private String wordNameForUpdate = "";

    private String difficulty = "";


    private void calculateDifficulty(String whichWord) {
        int len = whichWord.length();
        if (len < 5) {
            difficulty = "low";
        } else if (len < 9) {
            difficulty = "medium";
        } else {
            difficulty = "high";
        }
    }

    public void insertData() {
        calculateDifficulty(wordName);
        con.insert_data(con.get_connection(), "szavak", "id,word,difficulty", "null,'" + wordName + "','" + difficulty + "'");
    }

    public String showGreeting() {
        calculateDifficulty(wordName);

        con = new sql_connect("localhost", "3306", "szavak", "root", "root");
        return "the word has the difficulty level of " + difficulty;
    }

    public void fetchSingleData() {
        ArrayList<String> data = new ArrayList<>();
        try {
            data = con.get_specific_data(con.get_connection(), "szavak", wordNameForFetch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fetchedValues = "";
        data.forEach(x -> fetchedValues += x + "\n");
    }

    public void fetchData() {
        ArrayList<String> data = new ArrayList<>();
        try {
            data = con.select_query(con.get_connection(), "szavak");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fetchedValues = "";
        data.forEach(x -> fetchedValues += x + "\n");
    }

    public void updateData(){
        calculateDifficulty(wordNameForUpdate);
        updatedValue = con.update_data(con.get_connection(),"szavak",idUpdate,wordNameForUpdate,difficulty);
    }

    public void deleteData() {
        con.delete_data(con.get_connection(), "szavak", id);
        fetchData();
    }

    public void purgeData()
    {
        con.purge_table(con.get_connection(),"szavak");
        fetchData();
    }

    public String showUpdatedData() {
        return updatedValue;
    }

    public String showData() {
        return fetchedValues;
    }
}
