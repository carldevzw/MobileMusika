package viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
;
import com.grpprj.cropshop.R;

public class CropsViewholder extends RecyclerView.ViewHolder {

    public ImageView  ivCrop;
    public TextView tvPrice, tvName;
    public Button btnDetails;

    public CropsViewholder(@NonNull View itemView) {
        super(itemView);
        btnDetails = itemView.findViewById(R.id.btnDetails);
        ivCrop = itemView.findViewById(R.id.ivCrop);
        tvName = itemView.findViewById(R.id.tvName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
    }
}
