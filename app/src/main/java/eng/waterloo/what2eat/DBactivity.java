package eng.waterloo.what2eat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.R.attr.key;


/*
  sample database structure
  map: 1
  key: fake
  map: kenzo
  key: 4
   */
public class DBactivity extends AppCompatActivity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    HashMap<String, String> map = new LinkedHashMap<>();

    String largestVotingRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbactivity);
        //  addToDB("kenzo");
        getHighest();

    }

    public void getHighest() {

        root.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //get map of users in datasnapshot

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            map.put(d.getKey().toString(),d.getValue().toString());
                        }
                        //output();
                        /* put ratings in an array and find out the biggest rating */
                        int[] totalEle = new int[map.size()];
                        int i=0;
                        for (String c : map.keySet()) {
                            totalEle[i] = Integer.parseInt(map.get(c));
                            i++;
                        }
                        Arrays.sort(totalEle);
                        int biggest = totalEle[map.size()-1];

                        for (String c : map.keySet()){
                            if(Integer.parseInt(map.get(c))==biggest){
                                largestVotingRestaurant = c;
                                break;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


    }

public void output(){
    for (String c : map.keySet()){
        System.out.println("this is map: " + map.get(c));
        System.out.println("this is key: " + c);
    }
}
    public void addToDB(final String restaurant) {

        root.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exists = false;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (restaurant.equals(d.getKey().toString())) {
                        int votes = Integer.parseInt(d.getValue().toString());
                        root.child("restaurants").child(d.getKey()).setValue(votes + 1);
                        exists = true;
                        break;
                    }
                    if (!exists) {
                        root.child("restaurants").child(restaurant).setValue("1");
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
