package gruppoembedded.pse1819.unipd.project;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

/* this class is used to pass objects between Activities with better optimization
   (in this case the passed Object is the selected Date)

    l'informazione della data, selezionata in MainActivity, viene traspostata attraverso un oggetto
    di tipo "java.sql.Date" dal quale, all'occorrenza (in insertActivity e cameraActivity, nel metodo
    "inserimento" di DbSupport), vengono estratte le informazioni di anno mese e giorno
     da confrontare con quelle del database, per creare nuove occorrenze di pasti
    (Meal) se un pasto con quel nome e data(anno, mese e giorno) non esiste già.
*/
public class DateParcelable implements Parcelable {

    private Date mDate;

    public DateParcelable(Date date){
        mDate = date;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    //write object data to the passed in parcel
    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeLong(mDate.getTime());
    }

    //used to regenerate the object
    public static final Parcelable.Creator<DateParcelable> CREATOR= new Parcelable.Creator<DateParcelable>(){
      public DateParcelable createFromParcel(Parcel in){
          return new DateParcelable(in);
      }

      public DateParcelable[] newArray(int size){
          return new DateParcelable[size];
      }
    };

    //example constructor that takes a Parcel and gives you an object populated with its values
    protected DateParcelable(Parcel in){
        mDate = new Date(in.readLong());
    }

    //getter method to get the date
    public Date getDate(){
        return mDate;
    }
}
