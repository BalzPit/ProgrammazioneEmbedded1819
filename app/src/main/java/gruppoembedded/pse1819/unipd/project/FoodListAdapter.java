package gruppoembedded.pse1819.unipd.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FoodListAdapter extends ArrayAdapter<Object> {
    private static final String TAG = "adapter";

    private Context mContext;
    private int mResource;

    public FoodListAdapter(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //prelevo gli elementi uno ad uno dalla lista
        String[] piet = (String[]) getItem(position);
        Log.d(TAG, "lista pietanze: "+position+"  "+piet[0]);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //accedo alle textView di una riga del mio layout
        TextView pietanza = (TextView) convertView.findViewById(R.id.pietanza);
        TextView quantità = (TextView) convertView.findViewById(R.id.quantità);

        //inserisco ogni elemento nella corretta text view
        pietanza.setText(piet[0]);
        quantità.setText(piet[1]);

        return convertView;
    }

    public String name(int position){
        String name;
        String[] piet = (String[]) getItem(position);
        name = piet[0];
        return name;
    }
}

