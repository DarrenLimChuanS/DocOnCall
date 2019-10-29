package doc.on.call.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import doc.on.call.Model.PropertyImageModel;
import doc.on.call.R;

/**
 * Created by wolfsoft1 on 9/3/18.
 */

public class PropertyImageAdapter extends RecyclerView.Adapter<PropertyImageAdapter.MyViewHolder>{

    Context context;


    private List<PropertyImageModel> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView imageNo;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            imageNo=(TextView) view.findViewById(R.id.imageNo);



        }

    }


    public PropertyImageAdapter(Context mainActivityContacts, List<PropertyImageModel> offerList) {
        this.OfferList = offerList;
        this.context = mainActivityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.property_images, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PropertyImageModel lists = OfferList.get(position);
        holder.image.setImageResource(lists.getImage());
        holder.imageNo.setText(lists.getImageNo());


    }

    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}
